package main.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import workshop.tools.svm.mal.js.detector.IFeature;
import workshop.tools.svm.mal.js.detector.SVM_Detector;
import main.project.svm.features.AvgLineLength;
import main.project.svm.features.AvgStringLength;
import main.project.svm.features.DecodingRoutineNum;
import main.project.svm.features.DomModFuncNum;
import main.project.svm.features.EvalNum;
import main.project.svm.features.EventAttachNum;
import main.project.svm.features.HexCharNum;
import main.project.svm.features.IFrameStringNum;
import main.project.svm.features.KeywordWordRatio;
import main.project.svm.features.LongStringNum;
import main.project.svm.features.MaxStringEntropy;
import main.project.svm.features.MaxStringLength;
import main.project.svm.features.PrsIntFrmChrcodeNum;
import main.project.svm.features.ScriptEntropy;
import main.project.svm.features.ScriptLength;
import main.project.svm.features.SetTimeoutNum;
import main.project.svm.features.ShellcodeProbability;
import main.project.svm.features.StringAssignNum;
import main.project.svm.features.StringEntropy;
import main.project.svm.features.StringModFuncNum;
import main.project.svm.features.SuspiciousObjNum;
import main.project.svm.features.SuspiciousStringNum;
import main.project.svm.features.SuspiciousTagNum;
import main.project.svm.features.UnescEscNum;
import main.project.svm.features.WhitespacePercentage;

public class MaliciousnessTester {
	private static final String PROBLEM_FILE_PATH = "./.problem";
	private static final String BENIGN_FOLDER_PATH = "./training/benign_set";
	private static final String SUSPICIOUS_FOLDER_PATH = "./training/suspicious_set";
	private static final String MALICIOUS_FOLDER_PATH = "./training/malicious_set";
	private static SVM_Detector detector = new SVM_Detector();
	

	/***
	 * Sets the features to extract and creates the model. 
	 * 
	 * @param detector - The SVM-based detector.
	 * @param javascriptCode - The code to extract the feature values from.
	 * @throws IOException 
	 */
	private static void createModel(SVM_Detector detector, String javascriptCode) throws IOException {

		//building the feature list
		List<IFeature> featureList = new ArrayList<IFeature>();
		featureList.add(new AvgLineLength());
		featureList.add(new AvgStringLength());
		featureList.add(new DecodingRoutineNum());
		featureList.add(new DomModFuncNum());
		featureList.add(new EvalNum());
		featureList.add(new EventAttachNum());
		featureList.add(new HexCharNum());
		featureList.add(new IFrameStringNum());
		featureList.add(new KeywordWordRatio());
		featureList.add(new LongStringNum());
		featureList.add(new MaxStringEntropy());
		featureList.add(new MaxStringLength());
		featureList.add(new PrsIntFrmChrcodeNum());
		featureList.add(new ScriptEntropy());
		featureList.add(new ScriptLength());
		featureList.add(new SetTimeoutNum());
		featureList.add(new ShellcodeProbability());
		featureList.add(new StringAssignNum());
		featureList.add(new StringEntropy());
		featureList.add(new StringModFuncNum());
		featureList.add(new SuspiciousObjNum());
		featureList.add(new SuspiciousStringNum());
		featureList.add(new SuspiciousTagNum());
		featureList.add(new UnescEscNum());
		featureList.add(new WhitespacePercentage());

		//building the model and saving the problem
		detector.addToFeatureSet(featureList);
		detector.populateTrainingSet(
				BENIGN_FOLDER_PATH, 
				SUSPICIOUS_FOLDER_PATH, 
				MALICIOUS_FOLDER_PATH);
		detector.trainSVM();
		detector.save_problem(PROBLEM_FILE_PATH);
	}


	/**
	 * <p>Returns the maliciousness score of the suspected file. The score is the double equivalent of the 
	 * value of:</p><p> MALICIOUS (default: -1), BENIGN (default: 1) or UNKNOWN (default: 0),</p> 
	 * <p>if the file was found to be malicious, benign, or if the test was inconclusive respectively.</p>   
	 * 
	 * @param javascriptCode - the JavaScriptCode
	 * @param suspectedFile - the file from which the code was extracted.
	 * @return the test score 
	 * @throws IOException thrown by detector
	 */
	public static double getMalScore(String javascriptCode, File suspectedFile) throws IOException{

		//model not yet built
		if(!detector.modelBuilt()){

			//try loading a problem that was built in a previous session
			File problemFile = new File(PROBLEM_FILE_PATH);
			if(problemFile.exists()){
				detector.load_problem(PROBLEM_FILE_PATH);
			}

			//building a new model
			createModel(detector, javascriptCode);
		}

		return detector.test(suspectedFile);
	}

}
