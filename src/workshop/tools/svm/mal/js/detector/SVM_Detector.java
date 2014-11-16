package workshop.tools.svm.mal.js.detector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import workshop.tools.FileToString;
import workshop.tools.RhinoASTBuilder;
import workshop.tools.StringToFile;


/***
 * <b>SVM detector as proposed by the paper:</b>
 * 
 * <p>"A Static Malicious Javascript Detection Using SVM"
 * 
 * <p><b>Written by:</b>
 * 
 * <p>WANG Wei-Hong, LV Yin-Jun, CHEN Hui-Bing and FANG Zhao-Lin, 
 * <p>Zhejiang University of Technology HangZhou, China.
 *
 */
public class SVM_Detector {
	/*the list of featured tests for the SVM to learn from*/
	private List<IFeature> featureList;
	/*List of files containing harmless JS code*/
	private List<File> benignExampleList;
	/*List of files containing suspicious JS code*/
	private List<File> suspiciousExampleList;
	/*List of files containing malicious JS code*/
	private List<File> malExampleList;
	/*The SVM model*/
	private svm_model model;
	/*The SVM problem*/
	private svm_problem problem;
	/*value assigned to malicious JS code examples*/
	private static final int MALICIOUS = -1;
	/*value assigned to suspicious JS code examples*/
	private static final int UNKNOWN = 0;
	/*value assigned to harmless JS code examples*/
	private static final int BENIGN = 1;
	/* 2 == RBF kernel */
	private static final int KERNEL_TYPE = 2; 	
	/*kernel function parameter gamma*/
	private static final int GAMMA = 4;
	/*penalty factor C*/
	private static final int PENALTY_FACTOR = 27;
	/*cache size for the operation of the SVM (in MB)*/
	private static final int CACHE_SIZE = 100;
	/*accuracy of the result*/
	private static final double EPSILON = 0.0001;
	/*weight factor for separate files (0 == non)*/
	private static final int NR_WEIGHT = 0;


	/***
	 * default CTOR.
	 */
	public SVM_Detector(){
		featureList = new ArrayList<IFeature>();
		benignExampleList = new ArrayList<File>();
		suspiciousExampleList = new ArrayList<File>();
		malExampleList = new ArrayList<File>();
		model = null;
		problem = null;
	}


	/**
	 * Checks if the model for the current problem was built.
	 * 
	 * @return true if the model was built for this problem, false otherwise.
	 */
	public boolean modelBuilt(){
		return (model != null);
	}


	/**
	 * Checks if the SVM has a problem to build a model for.
	 * 
	 * @return true if has the problem, false otherwise.
	 */
	public boolean hasProblem(){
		return (problem != null);
	}


	/***
	 * Adds to the list of featured tests from the given list.
	 * 
	 * @param features - a list of features to add.
	 */
	public void addToFeatureSet(List<IFeature> features){
		featureList.addAll(features);
	}


	/***
	 * Fills the lists of example JS code files for the SVM to learn from. 
	 * 
	 * @param benignFolderPath - Path to benign training examples.
	 * @param suspiciousFolderPath - Path to suspicious training examples.
	 * @param malFolderPath - Path to malicious training examples.
	 * @throws IOException
	 */
	public void populateTrainingSet(
			String benignFolderPath, 
			String suspiciousFolderPath, 
			String malFolderPath) throws IOException{

		//initialization
		File benignFolder = new File(benignFolderPath);
		File suspiciousFolder = new File(suspiciousFolderPath);
		File malFolder = new File(malFolderPath);

		if(!(benignFolder.isDirectory() && suspiciousFolder.isDirectory() && malFolder.isDirectory())){
			throw new IOException();
		}

		//filling file lists
		benignExampleList = Arrays.asList(benignFolder.listFiles());
		suspiciousExampleList = Arrays.asList(suspiciousFolder.listFiles());
		malExampleList = Arrays.asList(malFolder.listFiles());
	}


	/***
	 * Builds the SVM problem which contains the training data to train the
	 * machine by.
	 * 
	 * @return The SVM problem.
	 * @throws IOException 
	 */
	private svm_problem buildProblem() throws IOException{

		//initialization
		problem = new svm_problem();
		int rows1 = benignExampleList.size();
		int rows2 = suspiciousExampleList.size();
		int rows3 = malExampleList.size();

		svm_node[][] nodeMat = new svm_node[rows1+rows2+rows3][];		
		problem.l = rows1 + rows2 + rows3;
		problem.y = new double[rows1+rows2+rows3];

		//filling the training data for the problem
		String sourcePath;
		String javascriptCode;
		for (int i = 0; i < rows1+rows2+rows3; i++) {
			if(i < rows1){
				problem.y[i] = BENIGN;
				sourcePath = benignExampleList.get(i).getPath();
				javascriptCode = FileToString.read(benignExampleList.get(i));
			}
			else if(i < rows1+rows2){
				problem.y[i] = UNKNOWN;
				sourcePath = suspiciousExampleList.get(i - rows1).getPath();
				javascriptCode = FileToString.read(suspiciousExampleList.get(i - rows1));

			}
			else{
				problem.y[i] = MALICIOUS;
				sourcePath = malExampleList.get(i - (rows1+rows2)).getPath();
				javascriptCode = FileToString.read(malExampleList.get(i - (rows1+rows2)));
			}
			RhinoASTBuilder.getAST(sourcePath);
			nodeMat[i] = FeatureExtractor.extractFeature(featureList, sourcePath, javascriptCode);	
		}
		problem.x = nodeMat;
		return problem;
	}


	/***
	 * Trains the SVM using the training data obtained from the example input files,
	 * and creates a model to predict by. 
	 * 
	 * @throws IOException 
	 */
	public void trainSVM() throws IOException{
		model = new svm_model();

		//fill problem parameters
		svm_parameter parameter = new svm_parameter();
		parameter.kernel_type = KERNEL_TYPE;
		parameter.gamma = GAMMA;
		parameter.C = PENALTY_FACTOR;
		parameter.cache_size = CACHE_SIZE;
		parameter.eps = EPSILON;
		parameter.nr_weight = NR_WEIGHT;

		//build the problem if no problem is loaded
		if(problem == null){
			problem = buildProblem();
		}

		//create the model
		model = svm.svm_train(problem, parameter);
	}


	/**
	 * Saves the problem to the specified file. 
	 * 
	 * @param saveFilePath - The file to save to.
	 * @throws IOException
	 */
	public void save_problem(String saveFilePath) throws IOException{
		if(problem == null){
			throw new IOException();
		}

		String output = "";

		output+= "l: "+problem.l+"\n";

		output+= "y: ";
		for (int i = 0; i < problem.y.length; i++) {
			output+= problem.y[i];
			if(i < problem.y.length - 1){
				output+= " ";
			}
		}
		output+="\n";

		output+= "x:\n";
		for (int i = 0; i < problem.x.length; i++) {
			for (int j = 0; j < problem.x[i].length; j++) {
				output+= problem.x[i][j].value;

				if(j < problem.x[i].length - 1){
					output+= " ";
				}
			}
			output+="\n";
		}

		StringToFile.write(output, saveFilePath);
	}


	/***
	 * Loads the problem from file.
	 * 
	 * @param problemFilePath - The file to load the problem from.
	 * @throws IOException
	 */
	public void load_problem(String problemFilePath) throws IOException{

		//initialization
		File problemFile = new File(problemFilePath);
		problem = new svm_problem();
		BufferedReader br = new BufferedReader(new FileReader(problemFile));
		svm_node[][] nodeMat = null;
		int j = 0;

		//extracting the problem data from file
		String line = null;
		while ((line = br.readLine()) != null) {
			if(line.startsWith("l:")){
				String numStr = line.substring(line.indexOf(' ') + 1);
				problem.l = Integer.parseInt(numStr);
				nodeMat = new svm_node[problem.l][];
			}

			else if(line.startsWith("y:")){
				String[] arrStr = line.substring(line.indexOf(' ') + 1).split(" ");
				problem.y = new double[arrStr.length];
				for (int i = 0; i < arrStr.length; i++) {
					problem.y[i] = Double.parseDouble(arrStr[i]);
				}
			}

			else if(line.startsWith("x:")){
				continue;
			}

			else{
				String[] arrStr = line.split(" ");
				svm_node[] arrLine = new svm_node[arrStr.length];
				for (int i = 0; i < arrStr.length; i++) {
					arrLine[i] = new svm_node();
					arrLine[i].index = i;
					arrLine[i].value = Double.parseDouble(arrStr[i]);
				}
				if(nodeMat != null && j < nodeMat.length){
					nodeMat[j] = arrLine;
					j++;
				}
			}
		}
		problem.x = nodeMat;

		br.close();
	}


	/***
	 * Tests a suspected file using the SVM.
	 * 
	 * @param suspectedFile - The file to test.
	 * @return Double result value of test.
	 * @throws IOException 
	 */
	public double test(File suspectedFile) throws IOException{
		String suspectCode = FileToString.read(suspectedFile);
		svm_node[] suspectValues = FeatureExtractor.extractFeature(featureList, suspectedFile.getPath(), suspectCode);
		return svm.svm_predict(model, suspectValues);
	}

}
