package workshop.tools.svm.mal.js.detector;

/***
 * An Interface for features to be extracted from JavaScript code by the feature extractor.
 */
public interface IFeature {
	
	/***
	 * Calculates the features value ( should be scaled to [0,1] ).
	 * 
	 * @param sourcePath - Path to source code.
	 * @param javascriptCode - The JavaScript code at 'sourcePath'.
	 * @return The resulting value.
	 */
	public double featureValue(String sourcePath, String javascriptCode);

}
