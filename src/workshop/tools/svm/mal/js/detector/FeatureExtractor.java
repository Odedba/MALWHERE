package workshop.tools.svm.mal.js.detector;

import java.util.List;

import libsvm.svm_node;

/***
 * <b>Feature extractor for SVM as proposed by the paper:</b>
 * 
 * <p>"A Static Malicious Javascript Detection Using SVM"
 * 
 * <p><b>Written by:</b>
 * 
 * <p>WANG Wei-Hong, LV Yin-Jun, CHEN Hui-Bing and FANG Zhao-Lin, 
 * <p>Zhejiang University of Technology HangZhou, China.
 *
 */
public class FeatureExtractor {

	/***
	 * Extracts the feature values vector for the given code and feature list.
	 * 
	 * @param featureList - The list of features.
	 * @param sourcePath - Path to source file.
	 * @param javascriptCode - Code found at 'sourcePath'. 
	 * @return
	 */
	public static svm_node[] extractFeature(List<IFeature> featureList, String sourcePath, String javascriptCode){
		svm_node[] featureValues = new svm_node[featureList.size()];
		
		for (int i = 0; i < featureList.size(); i++) {
			featureValues[i] = new svm_node();
			featureValues[i].index = i;
			
			double featureValue = featureList.get(i).featureValue(sourcePath, javascriptCode);
			
			//normalizing feature value
			featureValue = (Math.abs(featureValue) <= 1 ? featureValue : 1/featureValue);
			
			featureValues[i].value = featureValue;

		}
		return featureValues;
	}

}
