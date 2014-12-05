import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class Solver {
	static Instances structure;
	static Instances newData;
	static final int newExamplesCount = 50;
	static final int kNeighbours = 25;

	public Solver() {

	}

	double getDistance(Instance a, Instance b) {
		double distance = 0;
		for (int valInd = 0; valInd < a.numAttributes(); valInd++) {
			double val = a.value(valInd) - b.value(valInd);
			distance += val * val;
		}
		return Math.sqrt(distance);
	}

	List<Instance> getClosest(Instance newElement) {
		TreeMap<Double, Instance> distanceData = new TreeMap<>();
		List<Instance> neighbours = new ArrayList<>();
		for (int clInd = 0; clInd <= structure.numInstances()
				- newExamplesCount; clInd++) {
			double distance = getDistance(newElement, structure.instance(clInd));
			distanceData.put(distance, structure.instance(clInd));
		}
		int count = 0;
		for (Entry<Double, Instance> entry : distanceData.entrySet()) {
			neighbours.add(entry.getValue());
			count++;
			if (count > kNeighbours) {
				break;
			}
		}
		return neighbours;
	}

	Double getUnclassifiedCategory(Instance newElement) {
		Map<Double, Integer> candidates = new TreeMap<>();
		List<Instance> bestNeighbours = getClosest(newElement);
		for (Instance neigh : bestNeighbours) {
			Double category = neigh.value(neigh.numAttributes() - 1);
			if (candidates.containsKey(category)) {
				int count = candidates.get(category) + 1;
				candidates.put(category, count);
			} else {
				candidates.put(category, 1);
			}
		}
		int mostSupporting = 0;
		Double bestCategory = 0.0;
		for (Entry<Double, Integer> entry : candidates.entrySet()) {
			int supporting = candidates.get(entry.getKey());
			if (supporting > mostSupporting) {
				mostSupporting = supporting;
				bestCategory = entry.getKey();
			}
		}
		return bestCategory;
	}

	void classifyInstances() {
		for (int unclassifiedIndex = structure.numInstances()
				- newExamplesCount; unclassifiedIndex < structure
				.numInstances(); unclassifiedIndex++) {
			Instance element = structure.instance(unclassifiedIndex);
			Double expected = element.value(element.numAttributes() - 1);
			Double actual = getUnclassifiedCategory(structure
					.instance(unclassifiedIndex));
			System.out.printf("Expected category %s Actual category %s\n",
					expected, actual);
		}
	}

	void read() throws IOException {
		ArffLoader loader = new ArffLoader();
		String path = new File("").getAbsolutePath() + File.separator
				+ "resources" + File.separator + "iris.arff";
		System.out.println(path);
		loader.setFile(new File(path));
		structure = loader.getStructure();
		structure.setClassIndex(structure.numAttributes() - 1);
		structure = loader.getDataSet();
		structure.resample(new Random());
		/*
		 * for (int i = 0; i < structure.numInstances(); i++) { //
		 * System.out.println("====================");
		 * System.out.println(structure.instance(i)); Instance inst =
		 * structure.instance(i); for (int j = 0; j < inst.numAttributes(); j++)
		 * { System.out.println(inst.value(j)); } }
		 */
	}

	public static void main(String[] args) throws IOException {
		Solver solver = new Solver();
		solver.read();
		solver.classifyInstances();
	}
}
