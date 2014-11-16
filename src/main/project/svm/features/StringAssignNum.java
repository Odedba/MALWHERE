package main.project.svm.features;

import java.io.IOException;
import java.util.HashSet;
import org.javatuples.Pair;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;
import workshop.tools.closure.compiler.ClosureCompiler;
import workshop.tools.closure.compiler.NodeInfo;
import workshop.tools.svm.mal.js.detector.IFeature;

/**************************************** 
 * Number of direct string assignments.
 ****************************************/
public class StringAssignNum implements IFeature {
	private double result;
	private HashSet<String> arrayNames = null;
	
	
	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		result = 0;

		try {
			Pair<Node, ControlFlowGraph<Node>> cfgPair = ClosureCompiler.getCfgForFileAt(sourcePath);

			Node root = cfgPair.getValue0();
			ControlFlowGraph<Node> cfg = cfgPair.getValue1();

			arrayNames = new HashSet<String>();
			
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
			String parentStr = parent.toString();
			String childStr = child.toString();
			
			if(parentStr.startsWith("NAME: ") && childStr.startsWith("NEW") && child.hasMoreThanOneChild()){								
				if(child.getFirstChild().toString().startsWith("NAME: Array")){
					arrayNames.add(NodeInfo.getName(parent));
				 }
			}
			
			//checking if an array of strings is 'join'ed.
			if(parentStr.startsWith("GETPROP") && childStr.startsWith("NAME: ")){
				String childName = NodeInfo.getName(child);
				if(arrayNames.contains(childName) && child.getNext() != null){
					String siblingStr = NodeInfo.getString(child.getNext());
					if(siblingStr.equals("join")){
						result++;
					}
				}
			}
			
			//checking for direct assignment
			if(parentStr.startsWith("ASSIGN") && childStr.startsWith("STRING: ")){
				result++;
			}
			
			//checking for direct assignment to a variable
			if(parentStr.startsWith("VAR") && child.hasChildren()){
				if(child.getFirstChild().toString().startsWith("STRING: ")){
					result++;
				}
			}
			
			//checking 'String' constructor invocation
			if(parentStr.startsWith("NEW") && childStr.startsWith("NAME: ")){
				String name = NodeInfo.getName(child);
				
				if(name.equals("String")){
					result++;
				}
			}

			traverseNodes(child, cfg);
		}
	}
	

}
