/**
 * A variant of the ÍNearestNeighbours algorithm for the Iris Data Set.
 * I was not presented on the exercise where this algorithm was explained, 
 * but after some discussions with colleagues that were there, I think I got the idea.
 * This task is definitely not OOP-structured, but that is on purpose. I think that
 * this solution is better, because it is direct and that makes it far easier - no need
 * of implementing middle-layer access methods. However, I tried to make it as generalized
 * as I could. If we want to change the data set, we just need to change the values of
 * some constants, as long as its the same type. The solution is not optimal, because
 * there will be quite a lot of auto-boxing, but ArrayList was cleaner to use in this case.
 * 
 * @author Dimitar Kermedchiev
 */
package solution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class KNearestNeighboursIrisDataSet {

	public static final String FILE_PATH = "iris.txt";
	public static final int EXPECTED_LINES_COUNT = 150;
	public static final int TEST_SET_SIZE = 20;
	public static final int TYPE_COUNT = 3;
	public static final String TYPE_ONE = "Iris-setosa";
	public static final String TYPE_TWO = "Iris-versicolor";
	public static final String TYPE_THREE = "Iris-virginica";

	Random random = new Random();

	ArrayList<Double> sepalLengthTest = new ArrayList<Double>();
	ArrayList<Double> sepalWidthTest = new ArrayList<Double>();
	ArrayList<Double> petalLengthTest = new ArrayList<Double>();
	ArrayList<Double> petalWidthTest = new ArrayList<Double>();
	ArrayList<String> classNamesTest = new ArrayList<String>();

	ArrayList<Double> sepalLengthCorpus = new ArrayList<Double>();
	ArrayList<Double> sepalWidthCorpus = new ArrayList<Double>();
	ArrayList<Double> petalLengthCorpus = new ArrayList<Double>();
	ArrayList<Double> petalWidthCorpus = new ArrayList<Double>();
	ArrayList<String> classNamesCorpus = new ArrayList<String>();

	ArrayList<String> checkClassNames = new ArrayList<String>();

	// Read data from a file and fill Corpus parameters with it.
	public void readData() throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
			int linesCount = 0;
			String line = null;
			while (linesCount < EXPECTED_LINES_COUNT) {
				line = br.readLine();
				String[] splited = line.split(",");
				sepalLengthCorpus.add(Double.parseDouble(splited[0]));
				sepalWidthCorpus.add(Double.parseDouble(splited[1]));
				petalLengthCorpus.add(Double.parseDouble(splited[2]));
				petalWidthCorpus.add(Double.parseDouble(splited[3]));
				classNamesCorpus.add(splited[4]);
				linesCount++;
			}
		}
	}

	// Generate test set with random elements from the data and remove them from
	// the corpus.
	public void generateTestSet() {
		for (int i = 0; i < TEST_SET_SIZE; i++) {
			int randIndex = random.nextInt(sepalLengthCorpus.size());
			sepalLengthTest.add(sepalLengthCorpus.remove(randIndex));
			sepalWidthTest.add(sepalWidthCorpus.remove(randIndex));
			petalLengthTest.add(petalLengthCorpus.remove(randIndex));
			petalWidthTest.add(petalWidthCorpus.remove(randIndex));

			// Add className in special list - used as check later.
			checkClassNames.add(classNamesCorpus.remove(randIndex));

			// This line is used to test if names match the predictions:
			// System.out.println(classNamesCorpus.get(randIndex));
		}
		System.out.println();
	}

	// Transform data in an arrayList to be fit for the task (to be in the
	// interval [0, 1]).
	private void transformArrayData(ArrayList<Double> arrList) {
		double max = Collections.max(arrList);
		double min = Collections.min(arrList);
		for (int i = 0; i < arrList.size(); i++) {
			arrList.set(i, (arrList.get(i) - min) / (max - min));
		}
	}

	// Transform all arrayLists.
	public void trasnformAllData() {
		transformArrayData(sepalLengthTest);
		transformArrayData(sepalWidthTest);
		transformArrayData(petalLengthTest);
		transformArrayData(petalWidthTest);

		transformArrayData(sepalLengthCorpus);
		transformArrayData(sepalWidthCorpus);
		transformArrayData(petalLengthCorpus);
		transformArrayData(petalWidthCorpus);
	}

	// Calculate Euclidean distance for a given position(index).
	private ArrayList<Double> calculateEuclideanDist(int currentIndex) {
		ArrayList<Double> dist = new ArrayList<Double>();
		for (int i = 0; i < EXPECTED_LINES_COUNT - TEST_SET_SIZE; i++) {
			double sLength = Math.pow(sepalLengthCorpus.get(i) - sepalLengthTest.get(currentIndex), 2);
			double sWidth = Math.pow(sepalWidthCorpus.get(i) - sepalWidthTest.get(currentIndex), 2);
			double pLength = Math.pow(petalLengthCorpus.get(i) - petalLengthTest.get(currentIndex), 2);
			double pWidth = Math.pow(petalWidthCorpus.get(i) - petalWidthTest.get(currentIndex), 2);
			dist.add(Math.sqrt(sLength + sWidth + pLength + pWidth));
		}
		return dist;
	}

	// Find the index of the minimal value in an arrayList.
	private int indexOfMinValue(ArrayList<Double> arrList) {
		return arrList.indexOf(Collections.min(arrList));
	}

	/**
	 * This method makes prediction for the className of an element based on the
	 * name of its closest neighbor - the one with the smallest euclidean
	 * distance. The type with the most occurrences is set as name in the
	 * classNamesTest arrayList.
	 * 
	 * @param currentIndex
	 * @param k
	 */
	public void makePrediction(int currentIndex, int k) {
		ArrayList<Double> dist = calculateEuclideanDist(currentIndex);
		int[] typeValues = new int[TYPE_COUNT];
		for (int i = 0; i < k; i++) {
			int current = indexOfMinValue(dist);
			String currentName = classNamesCorpus.get(current);
			if (currentName.equals(TYPE_ONE)) {
				typeValues[0]++;
			} else {
				if (currentName.equals(TYPE_TWO)) {
					typeValues[1]++;
				} else {
					if (currentName.equals(TYPE_THREE)) {
						typeValues[2]++;
					}
				}
			}
			dist.remove(current);
		}
		int maxType = Math.max(Math.max(typeValues[0], typeValues[1]), typeValues[2]);
		if (maxType == typeValues[0]) {
			classNamesTest.add(TYPE_ONE);
		}
		if (maxType == typeValues[1]) {
			classNamesTest.add(TYPE_TWO);
		}
		if (maxType == typeValues[2]) {
			classNamesTest.add(TYPE_THREE);
		}
	}

	// Check if the prediction is correct(using the special checkClassNames
	// arrayList) and print all classNames for the test objects.
	public boolean checkPrediction(int currentIndex) {
		String testName = classNamesTest.get(currentIndex);
		System.out.println(testName);
		if (testName.equals(checkClassNames.get(currentIndex))) {
			return true;
		}
		return false;
	}

	// Execute the kNN algorithm and print accuracy.
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		int correctPredictionsCount = 0;
		double accuracy = 0.0;
		int k = 0;
		while (k < 1 || k > EXPECTED_LINES_COUNT - TEST_SET_SIZE) {
			System.out.println("Please enter k:");
			k = sc.nextInt();
		}

		KNearestNeighboursIrisDataSet kNN = new KNearestNeighboursIrisDataSet();
		kNN.readData();
		kNN.generateTestSet();
		for (int i = 0; i < TEST_SET_SIZE; i++) {
			kNN.makePrediction(i, k);
			if (kNN.checkPrediction(i)) {
				correctPredictionsCount++;
			}
		}
		accuracy = (double) correctPredictionsCount / TEST_SET_SIZE;
		System.out.println("Accuracy: " + accuracy * 100 + "%");
		sc.close();
	}
}
