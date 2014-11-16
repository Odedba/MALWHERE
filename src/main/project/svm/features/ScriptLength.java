package main.project.svm.features;

import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * 
 * The length of the script in characters.
 *
 */
public class ScriptLength implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		return javascriptCode.length();
	}

}
