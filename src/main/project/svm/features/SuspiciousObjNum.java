package main.project.svm.features;

import java.io.IOException;
import java.util.HashSet;

import org.javatuples.Pair;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;

import workshop.tools.BrowserBOM;
import workshop.tools.HtmlDOM;
import workshop.tools.JSObjects;
import workshop.tools.closure.compiler.ArrayNames;
import workshop.tools.closure.compiler.ClosureCompiler;
import workshop.tools.closure.compiler.NodeInfo;
import workshop.tools.svm.mal.js.detector.IFeature;

/************************************ 
 * The number of suspicious objects.
 ************************************/
public class SuspiciousObjNum implements IFeature{
	private HashSet<String> arrayNames;
	private HashSet<String> names;
	private HashSet<String> suspiciousObj;


	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		names = new HashSet<String>();
		suspiciousObj = new HashSet<String>();
		
		try {
			Pair<Node, ControlFlowGraph<Node>> cfgPair = ClosureCompiler.getCfgForFileAt(sourcePath);
			arrayNames = ArrayNames.getNames(cfgPair);
						
			Node root = cfgPair.getValue0();
			ControlFlowGraph<Node> cfg = cfgPair.getValue1();
			
			traverseNodes(root, cfg);

		} catch (IOException e) {}

		return suspiciousObj.size();
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
			String parentStr = parent.toString();
			
			//identifying non-array names in the script
			if(childStr.startsWith("NAME: ")){
				String childName = NodeInfo.getName(child);
				if(!(arrayNames.contains(childName) || HtmlDOM.contains(childName) 
						|| JSObjects.contains(childName) || BrowserBOM.contains(childName))){
					names.add(childName);
					//System.out.println(childName);
				}
			}
			
			if(parentStr.startsWith("GETELEM") && parent.hasMoreThanOneChild()){
				Node first = parent.getFirstChild();
				Node second = parent.getChildAtIndex(1);
				
				if(first.toString().startsWith("NAME: ") && second.toString().startsWith("NAME: ")){
					String firstName = NodeInfo.getName(first);
					String secondName = NodeInfo.getName(second);
					
					if(names.contains(firstName) && names.contains(secondName)){
						suspiciousObj.add(firstName+"["+secondName+"]");
					}
				}
			}
			
			if(parentStr.startsWith("GETPROP") && parent.hasMoreThanOneChild()){
				Node first = parent.getFirstChild();
				Node second = parent.getChildAtIndex(1);
				
				if(first.toString().startsWith("NAME: ") && second.toString().startsWith("STRING: ")){
					String firstName = NodeInfo.getName(first);
					String secondStr = NodeInfo.getString(second);
					
					if(names.contains(firstName) && names.contains(secondStr)){
						suspiciousObj.add(firstName+"."+secondStr);
					}
				}
			}

			traverseNodes(child, cfg);
		}
	}


}
