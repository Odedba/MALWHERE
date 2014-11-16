package workshop.tools.deobfuscator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import js.evaluator.JSAlertHandler;

import org.javatuples.Pair;

import workshop.tools.closure.compiler.ArrayNames;
import workshop.tools.closure.compiler.ClosureCompiler;
import workshop.tools.closure.compiler.ClosureCompilerException;
import workshop.tools.closure.compiler.NodeInfo;
import workshop.tools.js.beautifier.JSBeautifier;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Compiler.CodeBuilder;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;


public class JSDeobfuscator {
	/*names of arrays that were declared during the script*/
	private static HashSet<String> arrayNames = null;
	/*assignments to objects that were made during the script*/
	private static HashSet<Node> assignToObjets = null;
	/*list of Nodes that are loop blocks inside the script*/
	private static ArrayList<Node> loopBlockNodes = null; 
	/*the JavaScript beautifier*/
	private static JSBeautifier beautifier = new JSBeautifier();
	/*maximum length of names of elements that were declared during the script*/
	private static int maxNameLength;
	/*counter for number of new generated names that were added to scripts*/
	private static int newNameCounter = 1; 
	/*number of iterations allowed for execution of loops inside script*/
	private static int ITERATIONS = 20;


	/**
	 * <p>Given a JavaScript code 'jsCode', evaluates the code using HTMLUnit's script engine, and returns a list
	 * of all the Strings that were passed to the JavaScript 'alert' function.</p>
	 * <p><b>Important:</b></p> This code does not allow for the engine to use functions of JavaScript that may 
	 * cause harmful side-effects, and instead redefines them to only alert the operation and the parameter passed to it.</p>
	 * 
	 * @see <a href="http://htmlunit.sourceforge.net/">http://htmlunit.sourceforge.net/</a>
	 * 
	 * @param jsCode - The JavaScript code.
	 * @return A list of all the 'alert'ed Strings.
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 */
	private static HashSet<DeobfuscatorResult> evaluate(String jsCode) throws FailingHttpStatusCodeException, IOException{	
		HashSet<DeobfuscatorResult> results = new HashSet<DeobfuscatorResult>();
		HashSet<String> strResults = new HashSet<String>();
		Stack<String> alertStack = new Stack<String>();

		//Creating a stream to hold the error stream
		ByteArrayOutputStream sysErrStream = new ByteArrayOutputStream();
		PrintStream errPrintStream = new PrintStream(sysErrStream);

		//Saving the old System.err
		PrintStream oldErr = System.err;

		//set java to use special stream
		System.setErr(errPrintStream);

		//redefine 'dangerous' functions. 
		String newDocWrite = 
				"document.write = function(c){ alert('INFO: DOCUMENT_WRITE: '+c); };\n";
		String newDocWriteln = 
				"document.writeln = function(c){ alert('INFO: DOCUMENT_WRITELN: '+c); };\n";
		String newCreateElem = 
				"document.createElement = function(c){ alert('INFO: CREATE_ELEMENT: '+c); return c;};\n";
		String newEval = 
				"eval = function(c){ alert('INFO: EVAL: '+c); return c; };\n";
		String newSetTimeout =
				"setTimeout = function(a,b){ alert('INFO: SET_TIMEOUT: '+a); };\n";
		String newEncURIComp = 
				"encodeURIComponent = function(c){ alert('INFO: ENCODE_URI_COMPONENT: '+c); return c;};\n"; 
		String newDocGetElmById = 
				"document.getElementById = function(c){ alert('INFO: DOCUMENT_GET_ELEMENT_BY_ID: '+c); return new Object();};\n";

		//defining prompt to prevent infinite-loop of warnings by JSAlertHandler
		String newPrompt = 
				"window.prompt = function(c){ return c; };\n";

		//putting the whole script into a main function to allow exiting using 'return' 
		String mainFuncName = getNewName();
		jsCode = "function "+mainFuncName+"(){"+jsCode+"}\n"+mainFuncName+"();";

		//evaluate the script and get 'alert'ed Strings
		strResults.addAll(JSAlertHandler.getAlerts(newDocWrite + newDocWriteln + newCreateElem + newEval 
				+ newSetTimeout + newEncURIComp + newPrompt + newDocGetElmById + jsCode));

		//handling multiple layers of obfuscation
		alertStack.addAll(strResults);
		while(!alertStack.isEmpty()){
			String alert = alertStack.pop();

			//removing label from alert String and trying to re-evaluate the resulting expression
			int infoLabelEnd = alert.indexOf(' ');
			if(infoLabelEnd >= 0){
				int secondLabelEnd = alert.indexOf(' ', infoLabelEnd + 1);
				if(secondLabelEnd >= 0){

					String secondLabel = alert.substring(infoLabelEnd + 1, secondLabelEnd);

					alert = alert.substring(secondLabelEnd + 1);

					results.add(new DeobfuscatorResult(secondLabel, alert));

					alert = beautifier.js_beautify(alert, null);

					alert = "function "+mainFuncName+"(){"+alert+"}\n"+mainFuncName+"();";

					strResults.clear();
					strResults.addAll(JSAlertHandler.getAlerts(newDocWrite + newDocWriteln + newCreateElem + newEval 
							+ newSetTimeout + newEncURIComp + newPrompt + newDocGetElmById + alert));

					alertStack.addAll(strResults);
				}
			}
		}

		//putting things back
		System.err.flush();
		System.setErr(oldErr);

		return results;
	}


	/**	 
	 * Creates the CFG for the given JavaScript code, and returns the edited and beautified version of it.
	 * 
	 * @param symbolTable - A symbol table for the code
	 * @param code - The JavaScript code
	 * @param level - The compilation level
	 * @return The edited and beautified code.
	 * @throws IOException 
	 */
	private static String processCode(String code, CompilationLevel level) throws IOException{
		//creating and editing the AST of this code
		Pair<Node, ControlFlowGraph<Node>> cfg = ClosureCompiler.getCfg(code);

		code = editCFG(cfg, level);

		//beautifying the edited code and returning the result 
		code = beautifier.js_beautify(code, null);

		return code;
	}


	/**
	 * <p>Given an AST based CFG of Nodes, returns the code generated from an edited version of that graph with some
	 * added code.</p>
	 * <p>Specifically, this function iterates over the Nodes of the AST, extracts useful information about names
	 * of elements declared throughout the script and occurrences of assignments to non-locally declared Objects 
	 * like BOM or DOM using the operator '[]', and adds a declaration for a new function to 'alert' the right-hand-side 
	 * expressions of those assignments.</p>
	 * 
	 * @param cfg - The CFG of the Code. 
	 * @param level - The compilation level.
	 * @return The edited code.
	 */
	private static String editCFG(Pair<Node, ControlFlowGraph<Node>> cfg, CompilationLevel level) {

		//initialization
		Node root = cfg.getValue0();
		ControlFlowGraph<Node> graph = cfg.getValue1();

		//traversing graph and extracting useful information
		arrayNames = ArrayNames.getNames(cfg);
		
		assignToObjets = new HashSet<Node>();
		loopBlockNodes = new ArrayList<Node>();
		maxNameLength = 0;

		traverseNodes(root, graph);

		//restricting number of iterations allowed for loops 
		if(ITERATIONS > 0){
			for (Node blockNode : loopBlockNodes) {
				addLoopBreak(blockNode, ITERATIONS);
			}
		}

		//replacing unauthorized assignments to objects
		String newFuncName = alertObjAssign();

		//reconstructing the code from root of edited tree
		String editedCode = codeFromCFG(root, level);

		//adding function to alert assignments to objects
		String newFunc = 
				newFuncName+" = function(c){ alert('INFO: OBJECT_ASSIGN: '+c); };\n";

		editedCode = newFunc + editedCode;


		return editedCode;
	}


	/**
	 * Adds to the CFG EXPR_RESULT sub-trees with CALL to a new function in-order to 'alert' 
	 * right-hand-side expressions of assignments to non-locally declared Objects using 
	 * the operator '[]'.
	 * 
	 * @return The name of the new function to declare.
	 */
	private static String alertObjAssign(){

		//getting a new name for the new function to be added
		String funcName = getNewName();

		for (Node node : assignToObjets) {

			//searching for root of expression in which the assignment occurs
			Node it = node.getParent();			
			while(it != null){
				String itLabel = it.toString();
				if(itLabel.startsWith("BLOCK") || itLabel.startsWith("SCRIPT")){
					break;
				}

				it = it.getParent();
			}

			//creating a new EXPR_RESULT sub-tree to 'alert' the right-hand-side expression
			Node newExprRslt = new Node(Token.EXPR_RESULT);
			it.addChildrenToBack(newExprRslt);


			//adding the new sub-tree to the root that was found
			Node callRoot = node.cloneTree();
			newExprRslt.addChildrenToBack(callRoot);

			callRoot.setType(Token.CALL);
			callRoot.getFirstChild().detachFromParent();
			Node newNode = Node.newString(Token.NAME, funcName);
			callRoot.addChildToFront(newNode);
		}

		return funcName;
	}


	/**
	 * Returns a name that does not occur in the script, based on the maximal length out of names encountered during
	 * traversal of the script's AST.  
	 * 
	 * @return The name for a new Function.
	 */
	private static String getNewName(){
		String newFuncName = "a";

		while(newFuncName.length() < maxNameLength) {
			newFuncName+= "a";
		}
		newFuncName+= newNameCounter;
		newNameCounter++;
		return newFuncName;
	}


	/**
	 * <p>Given the Node 'root' of a CFG constructed using the closure compiler at 'level' CompilationLevel,</p>
	 * <p>rebuilds the code using the CFG from the given 'root'.</p> 
	 * 
	 * @param root - Root Node of the CFG. 
	 * @param level - CompilationLevel.
	 * @return the rebuilt code.
	 */
	private static String codeFromCFG(Node root, CompilationLevel level){

		//initializing the closure compiler
		Compiler compiler = new Compiler();
		compiler.disableThreads();
		CompilerOptions options = new CompilerOptions();
		level.setOptionsForCompilationLevel(options);
		compiler.initOptions(options);

		//rebuilding the code from the root of the CFG
		CodeBuilder cb = new CodeBuilder();
		compiler.toSource(cb, 0, root);

		return cb.toString();
	}


	/**
	 * A sort of deobfuscator for JavaScript code, which works by extracting all the parameters in the code that were
	 * passed to JavaScript functions that evaluate code or have side-effects like creating new DOM elements and editing them. 
	 * 
	 * @param sourceCode - The JavaScript source code.
	 * @return Parameters in the code that were passed to 'evaluating' functions.
	 * @throws ClosureCompilerException 
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 */
	public static HashSet<DeobfuscatorResult> deobfuscate(String sourceCodePath) throws ClosureCompilerException, FailingHttpStatusCodeException, IOException {

		//initialization
		String code;
		HashSet<DeobfuscatorResult> result = new HashSet<DeobfuscatorResult>();

		//beautifying the code and calling closure compiler to simplify the CFG of the code
		code = ClosureCompiler.compile(sourceCodePath, CompilationLevel.WHITESPACE_ONLY, true);
		code = beautifier.js_beautify(code, null);

		//evaluating code
		result = evaluate(processCode(code, CompilationLevel.WHITESPACE_ONLY));


		return result;
	}


	/**
	 * Traverses the Nodes of the graph, and extracts useful information. 
	 * 
	 * @param parent - The current parent Node.
	 * @param cfg - The graph.
	 */
	private static void traverseNodes(Node parent, ControlFlowGraph<Node> cfg) {

		// edges
		for (Node child = parent.getFirstChild(); child != null; child = child.getNext()) {

			//identify loop blocks
			if(child.toString().startsWith("BLOCK")){
				String parentStr = parent.toString();

				if(parentStr.startsWith("FOR") || parentStr.startsWith("DO") || parentStr.startsWith("WHILE")){
					loopBlockNodes.add(child);
				}
			}

			//find max length of names
			if(child.toString().startsWith("NAME: ")){
				String childName = NodeInfo.getName(child);
				maxNameLength = Math.max(maxNameLength, childName.length());
			}

			//identify assignment to BOM or DOM object elements using the operator '[]'
			if(parent.toString().startsWith("ASSIGN") && child == parent.getFirstChild()){
				if(child.toString().startsWith("GETELEM") && child.getFirstChild() != null){
					Node grandchild = child.getFirstChild();

					if(grandchild.toString().startsWith("NAME: ")){
						String grandchildName = NodeInfo.getName(grandchild);

						if(!arrayNames.contains(grandchildName)){
							assignToObjets.add(parent);
						}
					}
				}
			}

			traverseNodes(child, cfg);
		}
	}


	/**
	 * <p>Given that 'blockNode' is a 'BLOCK' Node under a loop Node in the AST of the JavaScript code,</p>
	 * <p>Adds sub-trees to the AST to generate code that controls the number of loop iterations.</p>
	 *  
	 * @param blockNode - 'BLOCK' Node under a loop Node 
	 * @param iter - The number of loop iterations to execute.
	 */
	private static void addLoopBreak(Node blockNode, double iter){
		Node it = blockNode.getParent();

		//searching for parent of loop node
		while(it != null){

			//it now points to parent of loop Node 
			if(it.toString().startsWith("SCRIPT") || it.toString().startsWith("BLOCK")){
				break;
			}

			it = it.getParent();
		}

		//adding a new loop-counter variable

		String myVarName = getNewName();
		Node root = new Node(Token.EXPR_RESULT); //root is now a new 'EXPR_RESULT' Node
		it.addChildrenToFront(root); //loop Node now has 'EXPR_RESULT' sibling

		Node firstChild = new Node(Token.ASSIGN);
		root.addChildrenToFront(firstChild); //root now has 'ASSIGN' as first child

		root = root.getFirstChild(); //root now points to new 'ASSIGN' Node
		firstChild = Node.newString(Token.NAME, myVarName);
		root.addChildrenToFront(firstChild); //root now has 'NAME' as first child

		Node secondChild = Node.newNumber(0.0);
		root.addChildrenToBack(secondChild); //root now has 'NUMBER' as second child

		//adding conditional for the new variable

		root = new Node(Token.IF); //root is now a new 'IF' Node
		blockNode.addChildrenToFront(root); //blockNode now has 'IF' as first child
		it = root; //it now points to new 'IF' Node

		firstChild = new Node(Token.GT);
		it.addChildrenToFront(firstChild); //it now has 'GT' as first child

		secondChild = new Node(Token.BLOCK);
		it.addChildrenToBack(secondChild); //it now has 'BLOCK' as second child

		it = it.getFirstChild(); //it now points to 'GT' Node 
		firstChild = Node.newString(Token.NAME, myVarName);
		it.addChildrenToFront(firstChild); //it now has 'NAME' as first child

		secondChild = Node.newNumber(iter);
		it.addChildrenToBack(secondChild); //it now has 'NUMBER' as second child

		it = root.getChildAtIndex(1); //it now points to 'BLOCK' Node
		firstChild = new Node(Token.BREAK);
		it.addChildrenToFront(firstChild); //it now has 'BREAK' as first child

		//incrementing the new variable

		it = new Node(Token.EXPR_RESULT); //it is now a new 'EXPR_RESULT' Node
		blockNode.addChildrenToBack(it); //blockNode now has 'EXPR_RESULT' as second child

		firstChild = new Node(Token.INC);
		it.addChildrenToFront(firstChild); //it now has 'INC' as first child

		it = it.getFirstChild(); //it now points to 'INC' Node
		firstChild = Node.newString(Token.NAME, myVarName);
		it.addChildrenToFront(firstChild); //it now has 'NAME' as first child
	}
	

	/**
	 * <p>Sets the iteration number for loops inside the script that deobfuscator is allowed to execute,</p>
	 * <p>Where zero or less means that there is no limit.</p>
	 * 
	 * @param iter - The number of iterations.
	 */
	public static void setIterations(int iter){
		ITERATIONS = iter;
	}
	
	
	/**
	 * <p>Returns the number of iterations loops inside the script are allowed to execute by the deobfuscator,</p>
	 * <p>Where zero or less means that there is no limit.</p>
	 * 
	 * @return The number of iterations.
	 */
	public static int getItrations(){
		return ITERATIONS;
	}


}
