import java.io.*;
import java.util.*;

/**
 * Implementation of a classifier model using Decision Tree ID3 (Homework4 -
 * part 2)
 * 
 * @author Harshit Shah
 */

public class ID3 {

	int num_of_attributes;
	String[] attributeNames;
	private int classAttribute; // last column from the given DataSet

	ArrayList<String>[] domains;

	TreeNode root = new TreeNode();
	
	/*
	 * This method reads the data from the given file. The last column is taken
	 * as the class attribute.
	 */
	@SuppressWarnings("unchecked")
	public int parseCSVFile(String filename) throws Exception {

		FileInputStream fis = null;

		try {
			File inputFile = new File(filename);
			fis = new FileInputStream(inputFile);
		} catch (Exception e) {
			System.err.println("Unable to open the input file: " + filename);
			System.err.println(e);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line;
		line = br.readLine();
		if (line == null) {
			System.err.println("No data found in the input file: " + filename);
			return 0;
		}

		StringTokenizer tokens = new StringTokenizer(line, ",");
		num_of_attributes = tokens.countTokens();
		System.out.println(num_of_attributes);

		// last column 'Play' as classAttribute from the given DataSet
		classAttribute = num_of_attributes - 1;

		if (num_of_attributes <= 1) {
			System.err.println("Error, while reading line: " + line);
			return 0;
		}

		domains = new ArrayList[num_of_attributes];
		for (int i = 0; i < num_of_attributes; i++)
			domains[i] = new ArrayList<String>();
		attributeNames = new String[num_of_attributes];

		for (int i = 0; i < num_of_attributes; i++) {
			attributeNames[i] = tokens.nextToken();
		}

		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			tokens = new StringTokenizer(line, ",");
			int numtokens = tokens.countTokens();
			if (numtokens != num_of_attributes) {
				System.err.println("Last line read: " + line);
				System.err.println("Expecting " + num_of_attributes
						+ " attributes");
				return 0;
			}

			Attribute attribute = new Attribute(num_of_attributes);
			for (int i = 0; i < num_of_attributes; i++) {
				String value = tokens.nextToken();
				int index = domains[i].indexOf(value);
				if (index < 0) {
					domains[i].add(value);
					attribute.attributes[i] = domains[i].size() - 1;
					continue;
				}
				attribute.attributes[i] = index;
			}
			root.data.add(attribute);
		}

		br.close();

		return 1;

	}

	/*
	 * This method decomposes the specified node according to the id3 algorithm.
	 * Recursively divides all children nodes until it reaches to the leaf node.
	 */
	public void makeDecisionTreeByID3Algorithm(TreeNode node) {

		double bestEntropy = 0;
		boolean flag_best_entropy = false;
		int selectedAttribute = 0;

		int data_size = node.data.size();
		int input_attributes = num_of_attributes - 1;
		node.entropy = calculateEntropy(node.data);
		if (node.entropy == 0)
			return;

		for (int i = 0; i < input_attributes; i++) {
			if (classAttribute == i) {
				continue;
			}
			int size = domains[i].size();
			if (oldNode(node, i))
				continue;
			double entropy = 0;
			for (int j = 0; j < size; j++) {
				ArrayList<Attribute> child = getChild(node.data, i, j);
				if (child.size() == 0)
					continue;
				double subentropy = calculateEntropy(child);
				entropy += subentropy * child.size();
			}
			entropy = entropy / data_size;
			if (flag_best_entropy == false) {
				flag_best_entropy = true;
				bestEntropy = entropy;
				selectedAttribute = i;
			} else {
				if (entropy < bestEntropy) {
					flag_best_entropy = true;
					bestEntropy = entropy;
					selectedAttribute = i;
				}
			}
		}

		if (flag_best_entropy == false)
			return;

		int size = domains[selectedAttribute].size();
		node.rootAttribute = selectedAttribute;
		node.children = new TreeNode[size];
		for (int j = 0; j < size; j++) {
			node.children[j] = new TreeNode();
			node.children[j].parent = node;
			node.children[j].data = getChild(node.data, selectedAttribute, j);
			node.children[j].rootIndex = j;
		}
		for (int j = 0; j < size; j++) {
			makeDecisionTreeByID3Algorithm(node.children[j]);
		}
	}

	/*
	 * This method uses Shannon's entropy formula. It calculates the entropy of
	 * the set of data attributes. The entropy is calculated using the values of
	 * the class attribute.
	 */
	public double calculateEntropy(ArrayList<Attribute> data) {

		int data_size = data.size();
		if (data_size == 0)
			return 0;
		int attributeIndex = classAttribute;
		int size = domains[attributeIndex].size();
		double sum = 0;
		for (int i = 0; i < size; i++) {
			int count = 0;
			for (int j = 0; j < data_size; j++) {
				Attribute attribute = (Attribute) data.get(j);
				if (attribute.attributes[attributeIndex] == i)
					count++;
			}
			double probability = (double) 1 * count / data_size;
			if (count > 0)
				sum += -probability * Math.log(probability);
		}
		return sum;
	}

	/*
	 * This method recursively checks if the specified attribute is used in any
	 * of the parents node. parent nodes.
	 */
	public boolean oldNode(TreeNode node, int attributeIndex) {
		if (node.children != null) {
			if (node.rootAttribute == attributeIndex)
				return true;
		}
		if (node.parent == null)
			return false;
		return oldNode(node.parent, attributeIndex);
	}
	
	/*
	 * This method returns a child of data with the list of attributes
	 * containing specified value.
	 */
	public ArrayList<Attribute> getChild(ArrayList<Attribute> data,
			int attributeIndex, int value) {
		ArrayList<Attribute> child = new ArrayList<Attribute>();

		int size = data.size();
		for (int i = 0; i < size; i++) {
			Attribute attribute = (Attribute) data.get(i);
			if (attribute.attributes[attributeIndex] == value)
				child.add(attribute);
		}
		return child;
	}
	
	/*
	 * This method displays classification rules to print the decision tree.
	 */
	public void displayClassificationRules(TreeNode node, String string) {

		int outputAttribute = classAttribute;
		if (node.children == null) {

			ArrayList<String> columns = new ArrayList<String>();
			int size = node.data.size();
			for (int i = 0; i < size; i++) {
				Attribute attribute = (Attribute) node.data.get(i);
				String value = (String) domains[outputAttribute]
						.get(attribute.attributes[outputAttribute]);
				int index = columns.indexOf(value);
				if (index < 0) {
					columns.add(value);
				}
			}
			int[] values = new int[columns.size()];
			for (int i = 0; i < values.length; i++) {
				String value = (String) columns.get(i);
				values[i] = domains[outputAttribute].indexOf(value);
			}

			if (values.length == 1) {
				System.out.println(string // + ""
						+ attributeNames[outputAttribute]
//						+ " = "
//						+ domains[outputAttribute].get(values[0])
								);
				return;
			}
//			System.out.print(string + "" + attributeNames[outputAttribute]
//					+ " = {");
//			for (int i = 0; i < values.length; i++) {
//				System.out.print("\"" + domains[outputAttribute].get(values[i])
//						+ "\" ");
//				if (i != values.length - 1)
//					System.out.print(" , ");
//			}
//			System.out.println(" };");
			return;
		}

		int size = node.children.length;
		for (int i = 0; i < size; i++) {
			System.out.print(string + attributeNames[node.rootAttribute]
//					+ " = " + domains[node.rootAttribute].get(i)
					);
			System.out.println();
			displayClassificationRules(node.children[i], string + "|   ");
		}

	}

	// main method
	public static void main(String[] args) throws Exception {

		ID3 instance = new ID3();
		String filename = "E:/FIS/HW3/HW#3/DT-datasetHW#3_try.csv";
		instance.parseCSVFile(filename);
		instance.makeDecisionTreeByID3Algorithm(instance.root);
		instance.displayClassificationRules(instance.root, "");

//		System.out.println("==========================================");

//		ID3 instance1 = new ID3();
//		filename = "E:/FIS/HW3/HW#3/DT-datasetHW#3.csv";
//		instance1.parseCSVFile(filename);
//		instance1.makeDecisionTreeByID3Algorithm(instance1.root);
//		instance1.displayClassificationRules(instance1.root, "");
	}
	
	/*
	 * The class represents an array of attributes
	 */
	class Attribute {
		public int[] attributes;

		public Attribute(int num_of_attributes) {
			attributes = new int[num_of_attributes];
		}
	}

	/*
	 * The class represents a node in the tree.
	 */
	class TreeNode {
		public double entropy;
		public ArrayList<Attribute> data;
		public int rootAttribute;
		public int rootIndex;
		public TreeNode[] children;
		public TreeNode parent;

		public TreeNode() {
			data = new ArrayList<Attribute>();
		}
	}
}
