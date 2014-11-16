package workshop.tools;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.StringLiteral;


/***
 * AST builder for JS code. 
 */
public class RhinoASTBuilder {
	/*List of strings representing the AST of the JS source code*/
	private static ArrayList<String> ast;
	/*A map of JS file names and their respective AST*/
	private static HashMap<String, ArrayList<String>> astMap = new HashMap<String, ArrayList<String>>();
	private static int lastToken = -3;


	/***
	 * Identify which Token is associated with the given tree AstNode.
	 * 
	 * @param node - The tree AstNode.
	 * @return String describing the AstNode according to it's Token.
	 */
	private static String identifyNode(AstNode node){
		String nodeStr = "";
		int type = node.getType();
		switch (type)
		{
		case Token.NAME:
			if (node instanceof Name)
			{
				Name name = (Name)node;
				nodeStr = Token.typeToName(type)+": "+name.getIdentifier();
			}
			break;
		case Token.STRING:
			if (node instanceof StringLiteral)
				nodeStr = Token.typeToName(type)+": "+((StringLiteral)node).getValue();
			break;
		case Token.VAR:
			nodeStr = (lastToken == Token.VAR? "" :  Token.typeToName(type));
			break;
		default:
			nodeStr = Token.typeToName(type);
		}
		lastToken = type;
		return nodeStr;
	}


	/***
	 * Reads the source file, parses the code and visits the nodes of
	 * the parse tree starting at the root.
	 * 
	 * @param sourcePath - Path to source file.
	 * @throws IOException
	 */
	private static void buildAST(String sourcePath) throws IOException{
		ast = new ArrayList<String>();

		class ASTtoString implements NodeVisitor {
			@Override 
			public boolean visit(AstNode node) {
				String nodeStr = identifyNode(node);
				if(!nodeStr.isEmpty()){
					ast.add(node.depth()+": "+nodeStr);
				}
				return true;
			}	
		}

		Reader reader = new FileReader(sourcePath);

		CompilerEnvirons env = new CompilerEnvirons();
		env.setRecordingLocalJsDocComments(true);
		env.setAllowSharpComments(true);
		env.setRecordingComments(true);
		env.setRecoverFromErrors(true);
		Parser parser = new Parser(env);

		try{
			AstRoot node = parser.parse(reader, sourcePath, 1);
			node.visitAll(new ASTtoString());
		}
		catch(Exception e){}
		finally{
			reader.close();
		}

	}


	/***
	 * Returns a list of strings representing the AST of the JS source code.
	 * 
	 * @param sourcePath - Path to source file.
	 * @return List of strings
	 */
	public static ArrayList<String> getAST(String sourcePath){
		if(astMap.containsKey(sourcePath)){
			return astMap.get(sourcePath);
		}
		if(!sourcePath.isEmpty()){
			try {
				buildAST(sourcePath);
			} catch (IOException e) {
				return ast;
			}
		}
		astMap.put(sourcePath, ast);
		return ast;
	}

}
