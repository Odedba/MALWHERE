package main.project.svm.features;

import workshop.tools.StringEntropyCalc;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The entropy of the script as a whole.
 */
public class ScriptEntropy implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		return StringEntropyCalc.calc(javascriptCode);
	}	
}
