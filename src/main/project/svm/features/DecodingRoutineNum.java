package main.project.svm.features;

import java.io.IOException;
import org.javatuples.Pair;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;
import workshop.tools.closure.compiler.ClosureCompiler;
import workshop.tools.svm.mal.js.detector.IFeature;

/******************************************************************************
 * <p>The number of code segments that resemble decoding routines, 
 * meaning loops that use "long" strings (40+ characters). </p>
 ******************************************************************************/
public class DecodingRoutineNum implements IFeature{
	private double result;
	private final int LONG_SRT_LENGTH = 40;

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
			String childStr = child.toString();
			
			if(childStr.startsWith("STRING: ")){
				int begin = childStr.indexOf(' ') + 1;
				int end = childStr.lastIndexOf(" <");
				
				if(end > begin + LONG_SRT_LENGTH){
					childStr = childStr.substring(begin, end);	
					Node it = parent;
					
					while(it != null){
						String itStr = it.toString();
						
						if(itStr.startsWith("FOR") || itStr.startsWith("DO") || itStr.startsWith("WHILE")){
							result++;
							break;
						}
						
						it = it.getParent();
					}	
				}
			}
			
			traverseNodes(child, cfg);
		}
	}

}
