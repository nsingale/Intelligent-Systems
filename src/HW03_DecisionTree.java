import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author Ninad
 *
 */
public class HW03_DecisionTree {
	
	static double [][] inputData;
	static double [][] attributeInfoGain=new double[5][3];
	static double[] attributeClassEntropy=new double[4];
	static double[][] decisionTree=new double[5][2];
	static int iteration=0; 
	
	private static void readData() {
		// TODO Auto-generated method stub
		try 
		{
			Scanner inputScanner=new Scanner (new BufferedReader(new FileReader("E:/FIS/HW3/HW#3/DT-datasetHW#3.csv")));
			//		  inputScanner.nextLine();
			ArrayList <String> stringInput=new ArrayList<String>();
			inputScanner.nextLine();
			while(inputScanner.hasNextLine()){
				String input=inputScanner.nextLine();
				stringInput.add(input);
			}

			int rowNumber=0;

			String[] inputString=new String[stringInput.size()];

			inputData=new double[stringInput.size()][stringInput.get(1).split(",").length];

			for (int i=0;i<stringInput.size();i++){
				inputString=stringInput.get(i).split(",");
				for (int j=0;j<inputString.length;j++){
					inputData[i][j]=Double.parseDouble(inputString[j]); 
				}
			}

			System.out.println(inputData.length+"\t"+inputData[0].length);

			//		  for (int i=0;i<inputData.length;i++){
			//			  for (int j=0;j<inputData[0].length;j++){
			//				  System.out.print(inputData[i][j]+"\t");
			//			  }
			//			  System.out.println();
			//		  }

			//		  calculateEPS(inputData);

		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void recursiveFun() {
		// TODO Auto-generated method stub
//		for (int i=0;i<2;i++){
			if(inputData.length>(0.1*43));
			{
				calculateEntropy(inputData);
//				buildNewMatrix(decisionTree[iteration][0],decisionTree[iteration][1]);
//				iteration++;
			}
		}
//	}

	
	private static void buildNewMatrix(double attr, double thr) {
		// TODO Auto-generated method stub
		int count=0;
		int attrIndex=(int)attr;
		System.out.println("Attribute "+attrIndex+" Threshold "+thr);
		
		for (int i=0;i<inputData.length;i++){
			if (inputData[i][attrIndex]<(double)thr)
				count++;
		}
		
		System.out.println("Number of data elements remaining "+count);
		
		double[][] matrix=new double[count][6];
		
//		System.out.println(matrix.length);
		int rowCount=0;
		for (int i=0;i<inputData.length;i++){
			if (inputData[i][attrIndex-1]<thr){
				for (int j=0;j<5;j++){
					matrix[rowCount][j]=inputData[i][j];
				}
				rowCount++;
			}
		}
//		
		inputData=null;
		inputData=new double[count][6];
		for (int i=0;i<count;i++){
			for (int j=0;j<matrix[0].length;j++){
				inputData[i][j]=matrix[i][j];
			}
		}		
		
//		System.out.println(inputData.length);
		iteration++;
	}

	public static void calculateEntropy(double[][] inputData){
		double totalCountLessThan=0;
		
		double countClassB=0;
		double countClassG=0;
		double countClassM=0;
		double countClassN=0;				
		for (int j=0;j<inputData[0].length;j++){			
			double maxInfoGain=0;
			for (int i=0;i<inputData.length;i++){
				double countClassLeftB=0;
				double countClassLeftG=0;
				double countClassLeftM=0;
				double countClassLeftN=0;
				
				double countClassRightB=0;
				double countClassRightG=0;
				double countClassRightM=0;
				double countClassRightN=0;

				
				double entropy=0;
				double min=inputData[i][j];
				
				if (inputData[i][inputData[0].length-1]==1)
					countClassB++;
				else if (inputData[i][inputData[0].length-1]==2)
					countClassG++;
				else if (inputData[i][inputData[0].length-1]==3)
					countClassM++;
				else
					countClassN++;

				double total_entropy=
						(((-1)*(countClassB/(inputData.length)))*(Math.log(countClassB/(inputData.length)))/(Math.log(2)))
					   +(((-1)*(countClassG/(inputData.length)))*(Math.log(countClassG/(inputData.length)))/(Math.log(2)))
					   +(((-1)*(countClassM/(inputData.length)))*(Math.log(countClassM/(inputData.length)))/(Math.log(2)))
					   +(((-1)*(countClassN/(inputData.length)))*(Math.log(countClassN/(inputData.length)))/(Math.log(2)));
				
				///////////////////////////////////
				if (inputData[i][j]<=min && inputData[i][inputData[0].length-1]==1)
					{
						totalCountLessThan++;
						countClassLeftB++;
					}
				else if (inputData[i][j]<=min && inputData[i][inputData[0].length-1]==2)
				{
					totalCountLessThan++;
					countClassLeftG++;
				}
				else if (inputData[i][j]<=min && inputData[i][inputData[0].length-1]==3)
				{
					totalCountLessThan++;
					countClassLeftM++;
				}
				else
				{
					totalCountLessThan++;
					countClassLeftN++;
				}
				
				double left_entropy=
						(((-1)*(countClassLeftB/(totalCountLessThan)))*(Math.log(countClassLeftB/(totalCountLessThan)))/(Math.log(2)))
					   +(((-1)*(countClassLeftG/(totalCountLessThan)))*(Math.log(countClassLeftG/(totalCountLessThan)))/(Math.log(2)))
					   +(((-1)*(countClassLeftM/(totalCountLessThan)))*(Math.log(countClassLeftM/(totalCountLessThan)))/(Math.log(2)))
					   +(((-1)*(countClassLeftN/(totalCountLessThan)))*(Math.log(countClassLeftN/(totalCountLessThan)))/(Math.log(2)));
				
				if (Double.isNaN(left_entropy)){
						left_entropy=0;
					}

				
			/////////////////////////////////				
				
			if (inputData[i][j]>min && inputData[i][inputData[0].length-1]==1)
			{
//				totalCountLessThan++;
				countClassRightB++;
			}
			else if (inputData[i][j]>=min && inputData[i][inputData[0].length-1]==2)
			{
//				totalCountLessThan++;
				countClassRightG++;
			}
			else if (inputData[i][j]>=min && inputData[i][inputData[0].length-1]==3)
			{
//				totalCountLessThan++;
				countClassRightM++;
			}
			else
			{
//				totalCountLessThan++;
				countClassRightN++;
			}
	

			double right_entropy=
					(((-1)*(countClassRightB/(totalCountLessThan)))*(Math.log(countClassRightB/(totalCountLessThan)))/(Math.log(2)))
				   +(((-1)*(countClassRightB/(totalCountLessThan)))*(Math.log(countClassRightG/(totalCountLessThan)))/(Math.log(2)))
				   +(((-1)*(countClassRightM/(totalCountLessThan)))*(Math.log(countClassRightM/(totalCountLessThan)))/(Math.log(2)))
				   +(((-1)*(countClassRightN/(totalCountLessThan)))*(Math.log(countClassRightN/(totalCountLessThan)))/(Math.log(2)));
			
			if (Double.isNaN(right_entropy)){
					right_entropy=0;
				}
			
			double information_Gain=
					total_entropy-(((totalCountLessThan/inputData.length)*left_entropy)
					+(((inputData.length-totalCountLessThan)/inputData.length)*right_entropy));
			
			
			
//			System.out.println(information_Gain);
			
				if (information_Gain>maxInfoGain)
				{
					maxInfoGain=information_Gain;
//					System.out.println((j+1)+"\t"+inputData[i][j]+"\t"+totalCountLessThan+"\t"+countClassLeftB+"\t"+countClassLeftG+"\t"+countClassLeftM+"\t"+countClassLeftN+"\t"+maxInfoGain);
					attributeInfoGain[j][0]=(j+1);
					attributeInfoGain[j][1]=inputData[i][j];
					attributeInfoGain[j][2]=maxInfoGain;
				}
			}
			
//			System.out.println("************************");
		}
		
		double maxInfo=0;
		
		for (int k=0;k<attributeInfoGain.length;k++){
			for (int l=0;l<attributeInfoGain[0].length;l++){
					System.out.print(attributeInfoGain[k][l]+"\t");
				}
				System.out.println();
			}
//		}
		
//		for (int i=0;i<decisionTree.length;i++){
//			for (int j=0;j<decisionTree[0].length;j++){
//				System.out.print(decisionTree[i][j]+"\t");
//			}
//			System.out.println();
//		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readData();
//		calculateEntropy();
		recursiveFun();
	}

}
