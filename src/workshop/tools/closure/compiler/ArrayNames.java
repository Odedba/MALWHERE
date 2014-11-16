package workshop.tools.closure.compiler;

import java.util.HashSet;
import org.javatuples.Pair;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;


public class ArrayNames {
	/*names of arrays that were declared during the script*/
	private static HashSet<String> arrayNames = null;

	
	/**
	 * Finds and returns all the names of Array objects declared in the script according to its
	 * AST based CFG. 
	 * 
	 * @param cfgPair - AST based CFG.
	 * @return All the names of Arrays.
	 */
	public static HashSet<String> getNames(Pair<Node, ControlFlowGraph<Node>> cfgPair){
		arrayNames = new HashSet<String>();
		
		Node root = cfgPair.getValue0();
		ControlFlowGraph<Node> cfg = cfgPair.getValue1();
		
		traverseNodes(root, cfg);
		
		return arrayNames;
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

			//identify names of arrays declared using the '[]' operator
			if(child.toString().startsWith("ARRAYLIT")){
				if(parent.toString().startsWith("NAME: ")){
					String parentName = NodeInfo.getName(parent);
					arrayNames.add(parentName);
				}
			}
			
			if(parent.toString().startsWith("ASSIGN") && parent.hasMoreThanOneChild()){
				Node first = parent.getFirstChild();
				Node second = parent.getChildAtIndex(1);
				
				if(first.toString().startsWith("NAME: ") && second.toString().startsWith("ARRAYLIT")){
					arrayNames.add(NodeInfo.getName(first));
				}
					
			}

			//identify names of arrays declared using 'new Array'
			if(child.toString().startsWith("NAME: Array") && parent.toString().startsWith("NEW")){
				Node grandparent = parent.getParent();

				if(grandparent != null && grandparent.toString().startsWith("ASSIGN")){
					Node firstchild = grandparent.getFirstChild();

					if(firstchild.toString().startsWith("NAME: ")){

						String firstchildName = NodeInfo.getName(firstchild);
						arrayNames.add(firstchildName);						
					}
				}
				
				else if(grandparent != null && grandparent.toString().startsWith("NAME: ")){
					arrayNames.add(NodeInfo.getName(grandparent));
				}
			}

			traverseNodes(child, cfg);
		}
	}
	
	
}
