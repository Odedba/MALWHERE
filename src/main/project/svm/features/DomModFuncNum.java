package main.project.svm.features;

import java.io.IOException;
import org.javatuples.Pair;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;
import workshop.tools.HtmlDOM;
import workshop.tools.closure.compiler.ClosureCompiler;
import workshop.tools.svm.mal.js.detector.IFeature;

/**************************************** 
 * The number of DOM modifying functions
 ****************************************/
public class DomModFuncNum implements IFeature {
	private double result;

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		result = 0;

		try {
			Pair<Node, ControlFlowGraph<Node>> cfgPair = ClosureCompiler.getCfgForFileAt(sourcePath);

			Node root = cfgPair.getValue0();
			ControlFlowGraph<Node> cfg = cfgPair.getValue1();

			traverseNodes(root, cfg);

		} catch (IOException e) {}

		return result;

	}


	/**
	 * Traverses the Nodes of the graph, and extracts the information. 
	 * 
	 * @param parent - The current parent Node.
	 * @param cfg - The graph.
	 */
	private void traverseNodes(Node parent, ControlFlowGraph<Node> cfg) {
		// edges
		for (Node child = parent.getFirstChild(); child != null; child = child.getNext()) {

			//handle call or assignment to DOM using dot notation or '[]' operator
			if(parent.toString().startsWith("GETPROP") || parent.toString().startsWith("GETELEM")){
				if(child.toString().startsWith("NAME: document")){
					result++;
				}
				else if(child.getNext() != null){
					Node sibling = child.getNext();

					String siblingStr = sibling.toString();
					int start = ("STRING: ").length();
					int end = siblingStr.indexOf(' ', start);
					siblingStr = siblingStr.substring(start, end);

					if(HtmlDOM.contains(siblingStr)){
						result++;
					}
				}
			}

			traverseNodes(child, cfg);
		}
	}

}
