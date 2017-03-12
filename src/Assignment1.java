import java.text.DecimalFormat;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


/**
 * This class implements 2 algorithms namely Dijkstras and Greedy BFS. 
 * Dijkstra is uninformed as it selects the path with least cost but 
 * explores each and every node. Greedy BFS is informed because it 
 * takes the least cost path and does not explore any other nodes.    
 */

/**
 * @author Ninad Ingale
 * Date : 02/15/2016
 */

public class Assignment1 {
	
	static String[] rooms;
	static double [][]mapCoordinates;
	static Set<Integer> settled=new HashSet<Integer>();
	static Set<Integer> unSettled=new HashSet<Integer>();
	static Queue<Integer> queue;
	
	//Initialize the room names and distances between them.  
	static void initialize(){
		rooms=new String[]{
				"3435","3445",
				"34551","35151","35171",
				"35191","3519",	"3517",
				"3515","3511","3509",
				"3610","3510","3600",
				"3441","3500","3430",
				"3440","RND","Computational Studies",
				"Honors","Xerox","Adjunct Office"
				};
		
		mapCoordinates=new double[][]
			{
				{43.084450, -77.679715},{43.084394, -77.679716},
				{43.084313,-77.679679},{43.084244,-77.679726},{43.084221,-77.679725},
				{43.084200,-77.679726},{43.084192,-77.680066},{43.084213,-77.680065},
				{43.084234,-77.680069},{43.084262,-77.680076},{43.084288,-77.680068},
				{43.084214,-77.679837},{43.084215,-77.679967},{43.084275,-77.679835},
				{43.084279,-77.679909},{43.084279,-77.679978},{43.084363,-77.679969},
				{43.084356,-77.679883},{43.084451,-77.679985},{43.084452,-77.679899},
				{43.084456,-77.679822},{43.084398,-77.679824},{43.084337,-77.679828}
			};
			
			
	}

	//Calculate Distance between points from the lattitude and longitude given.  
	static double[][]buildDistanceMatrix(){
		double[][] distanceMatrix=new double[mapCoordinates.length][mapCoordinates.length]; 
		double R=6371000;
		DecimalFormat f=new DecimalFormat("##.00");
		
		for (int i=0;i<mapCoordinates.length;i++){
			for (int j=0;j<mapCoordinates.length;j++){
				double distance=0; 
				double x=
						(mapCoordinates[j][0]-mapCoordinates[i][0])*Math.cos((mapCoordinates[j][1]+mapCoordinates[i][1])/2);
				double y=mapCoordinates[j][1]-mapCoordinates[i][1];
				String s=f.format((Math.sqrt(x*x+y*y)*R)/10);
				distance=Double.parseDouble(s);
//				System.out.println(distance);
				distanceMatrix[i][j]=distance;
			}
		}
		
//		buildGraph (distanceMatrix);
		return distanceMatrix;
	}
	
	// Display the contents of the matrix. 
	// Testing purpose. 
	static void display(double[][] distanceMatrix){
		
			for (int i=0;i<distanceMatrix.length;i++){
				for (int j=0;j<distanceMatrix.length;j++){
					System.out.print(distanceMatrix[i][j]+"\t");
				}
				System.out.println("");
			}
		}
		
  /**
	* This method Dijkstra algorithm
	* Parameters passed 
	* 	Double[][] distance  This is the weighted graph. 
	*	Index  This is the index of the source vertex.  
	*/
	static double[] dijkstra(double[][] graph,int index){
		double []distance=new double[graph.length];
		String []previous=new String[graph.length];
		int node; 		
		for (int i=0;i<graph.length;i++){
			distance[i]=9999;
		}
		
		unSettled.add(index);
		
//		Iterator iter=unSettled.iterator();
//		while(iter.hasNext()){
//			System.out.println("Unsettled " + " "+iter.next());
//		}
		
		distance[index]=0;
		
		while (!unSettled.isEmpty()){
			node=min(distance,index);
//			System.out.println("Node is"+node);
			unSettled.remove(node);
			
//			unSettled.r
			settled.add(node);
//			Iterator iter1=settled.iterator();
//			while(iter1.hasNext()){
//				System.out.println("Settled " + " "+iter1.next());
//			}

			neighbours(graph,distance,node);			
		}
		
//		System.out.print("Here");
		return distance;
	}
	
   /**
	* This method find least cost vertex
	* Parameters passed 
	* 	Double[] distance  This is the distance of each vertex from source. 
	*	Index  This is the index of the source vertex.  
	*/
	static int min(double[] distance,int index){
		
		Iterator<Integer> sequence=unSettled.iterator();
		double min_dist=distance[0];
		int count=sequence.next();

		for (int i=0;i<distance.length;i++)
		{
			if (distance[i]<min_dist)
			{
				min_dist=distance[i];
				count=i;
			}
		}
		
//		System.out.print("Back to dijkstra "+" "+count);
		return count;
	}
	
   /**
	* This method is responsible for finding if the vertex under consideration
	* has any of the neighbors. If the vertex has any neighbors , neighbor with
	* least cost would be selected and would be added to the priority queue.    
	* Parameters passed 
	* 	Double[][] distance  This is the weighted graph. 
	*	Index  This is the index of the source vertex.
	*	Double[] distance This array stores distance of each point from the source under
	*					  consideration. 	  
	*/	
	static void neighbours(double[][] graph,double[] distance,int index){
		double edge=-1;
		double min=1;
		
		for (int i=0;i<distance.length;i++)
		{
			if (!settled.contains(i))
			{
				if(graph[index][i]!=9999)
				{
					edge=graph[index][i];
					min=distance[index]+edge;
					if (min<distance[i])
						distance[i]=min;
					settled.add(i);
				}
			}
		}
	}
	
   /**
	* This method Greedy BFS algorithm
	* Parameters passed 
	* 	Double[][] distance  This is the weighted graph. 
	*	Index  This is the index of the source vertex.  
	*/	
	static double[] BFS(double[][] graph,int index){
		double[] distance=new double[graph.length];
		boolean[] visited=new boolean[graph.length];
		queue=new LinkedList<Integer>();
		//Initialize
		for (int i=0;i<graph.length;i++){
			distance[i]=9999;
		}
		//Distance from node to self.
		distance[index]=0;
		visited[index]=true; 
		queue.add(index);
		
		//Find least cost branch and expand that. 
		while (!queue.isEmpty()){
			int node=queue.remove();
			//For each node check if the node is visited. 
			for (int i=0;i<graph.length;i++){
				if (!visited[i]){
					double min=0;
					min=min+graph[node][i];
					if (min<distance[i]){
						distance[i]=min;
					}
					visited[i]=true;
					queue.add(i);
				}
			}	
		}	
		return distance; 
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initialize();
		double [][]distanceMatrix=buildDistanceMatrix();
//		double [][]graph=buildGraph(distanceMatrix); 
//		display (graph);
		double[] d_dijkstra=dijkstra(distanceMatrix,10);
		for (int i=0;i<distanceMatrix.length;i++){
			System.out.println(rooms[i]+" "+d_dijkstra[i]);
		}
		double[] d_BFS=BFS(distanceMatrix,10);
		
		System.out.println("*****BFS*****");
		for (int i=0;i<distanceMatrix.length;i++){
			System.out.println(rooms[i]+" "+d_BFS[i]);
		}	
	}
}
