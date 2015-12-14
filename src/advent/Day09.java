package advent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day09
{
	public static void main(String[] args) throws IOException 
	{
		String input = FileUtility.textFileToString("input/09.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(shortestRoute(input)), "output/09A.txt");

		// Part two
		FileUtility.stringToTextFile(Integer.toString(longestRoute(input)), "output/09B.txt");
	}
	
	private static int shortestRoute(String input)
	{
		Graph graph = buildGraph(input);
		
		// Path can start from anywhere, so we try all possible start locations
		int shortestPath = Integer.MAX_VALUE;
		for (int i = 0; i < graph.size(); ++i) {
			int pathLength = shortestHamiltonianPath(graph, i, new boolean[graph.size()]);
			if (pathLength < shortestPath)
				shortestPath = pathLength;
		}
		
		return shortestPath;
	}
	
	private static int shortestHamiltonianPath(Graph graph, int node, boolean[] visited)
	{	
		visited[node] = true;
		
		// If every node has been visited, we have found a Hamiltonian path
		if (allTrue(visited)) {
			visited[node] = false;
			return 0;
		}
		
		int[] neighbors = graph.getNeighbors(node);
		
		int shortestPath = -1;
		for (int neighbor : neighbors) {
			if (!visited[neighbor]) {
				int pathLength = shortestHamiltonianPath(graph, neighbor, visited);
				
				// Allow any non-negative path shorter than the current one
				// (with special check against -1, used to indicate no H. paths)
				if ((shortestPath == -1
						|| pathLength + graph.getEdge(node, neighbor) < shortestPath)
						&& pathLength >= 0)
					shortestPath = pathLength + graph.getEdge(node, neighbor);
			}
		}
		
		// This will return -1 if the path found was not Hamiltonian, ensuring
		// we ignore it
		visited[node] = false;
		return shortestPath;
	}
	
	private static int longestRoute(String input)
	{
		Graph graph = buildGraph(input);

		// Path can start from anywhere, so we try all possible start locations
		int longestPath = Integer.MIN_VALUE;
		for (int i = 0; i < graph.size(); ++i) {
			int pathLength = longestHamiltonianPath(graph, i, new boolean[graph.size()]);
			if (pathLength > longestPath)
				longestPath = pathLength;
		}
		
		return longestPath;
	}
	
	private static int longestHamiltonianPath(Graph graph, int node, boolean[] visited)
	{	
		visited[node] = true;
		
		// If every node has been visited, we have found a Hamiltonian path
		if (allTrue(visited)) {
			visited[node] = false;
			return 0;
		}
		
		int[] neighbors = graph.getNeighbors(node);
		
		int longestPath = -1;
		for (int neighbor : neighbors) {
			if (!visited[neighbor]) {
				int pathLength = longestHamiltonianPath(graph, neighbor, visited);
				
				// Allow any non-negative path longer than the current one
				// (with special check against -1, used to indicate no H. paths)
				if ((longestPath == -1
						|| pathLength + graph.getEdge(node, neighbor) > longestPath)
						&& pathLength >= 0)
					longestPath = pathLength + graph.getEdge(node, neighbor);
			}
		}
		
		// This will return -1 if the path found was not Hamiltonian, ensuring
		// we ignore it
		visited[node] = false;
		return longestPath;
	}
	
	private static boolean allTrue(boolean[] array)
	{
		for (boolean value : array)
			if (!value)
				return false;
		
		return true;
	}
	
	private static Graph buildGraph(String input)
	{
		String[] lines = input.split("\r\n|\r|\n");
		Map<String, Integer> places = new HashMap<String, Integer>();
		int place = 0;
		
		// First pass to collect the names of places
		for (String line : lines) {
			String[] tokens = line.split("\\s+");
			place = addIfMissing(places, tokens[0], place);
			place = addIfMissing(places, tokens[2], place);
		}
		
		// Second pass to build edges
		Graph graph = new Graph(places.size());
		for (String line : lines) {
			String[] tokens = line.split("\\s+");
			graph.addEdge(places.get(tokens[0]), 
					places.get(tokens[2]),
					Integer.parseInt(tokens[4]));
		}
		
		return graph;
	}
	
	private static int addIfMissing(Map<String, Integer> map, String name, int id)
	{
		if (map.containsKey(name)) {
			return id;
		} else {
			map.put(name, id);
			return id + 1;
		}
	}

	/** An undirected graph class */
	private static class Graph
	{
		private final int[][] edges;
		
		public Graph(int nodes)
		{
			edges = new int[nodes][nodes];
			for (int i = 0; i < nodes; ++i)
				for (int j = 0; j < nodes; ++j)
					edges[i][j] = -1;
		}
		
		public void addEdge(int a, int b, int weight)
		{
			edges[a][b] = weight;
			edges[b][a] = weight;
		}
		
		public int getEdge(int a, int b)
		{
			return edges[a][b];
		}
		
		public boolean hasEdge(int a, int b)
		{
			return edges[a][b] >= 0;
		}
		
		public int[] getNeighbors(int node)
		{
			int count = 0;
			for (int i = 0; i < edges.length; ++i)
				if (edges[node][i] >= 0)
					count++;
			
			int[] neighbors = new int[count];
			
			int j = 0;
			for (int i = 0; i < edges.length; ++i) {
				if (edges[node][i] >= 0) {
					neighbors[j] = i;
					j++;
				}
			}
			
			return neighbors;
		}
		
		public int size()
		{
			return edges.length;
		}
	}
}
