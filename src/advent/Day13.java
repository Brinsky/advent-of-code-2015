package advent;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day13 
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/13.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(optimalSeats(input)), "output/13A.txt");

		// Part two
		FileUtility.stringToTextFile(Integer.toString(optimalSeatsApathy(input)), "output/13B.txt");
	}
	
	private static int optimalSeats(String input)
	{
		int[][] happiness = buildHappinessMatrix(input);
		
		// Compute optimal seating arrangement
		int[] seats = new int[happiness.length];
		boolean[] seatedPeople = new boolean[happiness.length];
		return optimalSeats(happiness, seats, seatedPeople, 0);
	}
	
	private static int optimalSeatsApathy(String input)
	{
		int[][] withoutMe = buildHappinessMatrix(input);
		
		// New happiness matrix with one extra row and column
		int[][] happiness = new int[withoutMe.length + 1][withoutMe.length + 1];
		
		// Copy original data over, leave new rows/columns as zeros
		for (int i = 0; i < withoutMe.length; ++i) {
			for (int j = 0; j < withoutMe.length; ++j) {
				happiness[i][j] = withoutMe[i][j];
			}
		}

		// Compute optimal seating arrangement
		int[] seats = new int[happiness.length];
		boolean[] seatedPeople = new boolean[happiness.length];
		return optimalSeats(happiness, seats, seatedPeople, 0);
	}
	
	private static int[][] buildHappinessMatrix(String input)
	{
		// Separate the lines and group them by name
		String[] lines = input.split("\r\n|\r|\n");
		Arrays.sort(lines);
		
		// Count all the different people and assign them ID numbers
		Map<String, Integer> people = new HashMap<String, Integer>();
		int id = 0;
		for (String line : lines) {
			String person = line.split("\\s+")[0];
			if (!people.containsKey(person)) {
				people.put(person, id);
				id++;
			}
		}
		
		// Determine happiness values between each pair of people (from both directions)
		int[][] happiness = new int[id][id];
		for (String line : lines) {
			String[] tokens = line.split("\\s+|\\.");
			
			int to = people.get(tokens[0]);
			int from = people.get(tokens[10]);
			int sign = tokens[2].equals("gain") ? 1 : -1;
			
			happiness[to][from] = sign * Integer.parseInt(tokens[3]);
		}
		
		return happiness;
	}
	
	private static int optimalSeats(int[][] happiness, int[] seats,
			boolean[] people, int index)
	{
		if (index == seats.length)
			return computeHappiness(happiness, seats);
		
		int best = Integer.MIN_VALUE;
		for (int i = 0; i < people.length; ++i) {
			if (!people[i]) {
				people[i] = true;
				seats[index] = i;
				
				int delta = optimalSeats(happiness, seats, people, index + 1);
				if (delta > best)
					best = delta;
				
				people[i] = false;
			}
		}
		
		return best;
	}
	
	/**s Computes total happiness for a given arrangment */
	private static int computeHappiness(int[][] happiness, int[] seats)
	{
		int total = 0;
		
		for (int i = 0; i < seats.length; ++i) {
			total += happiness[seats[i]][seats[(i + 1) % seats.length]];
			total += happiness[seats[(i + 1) % seats.length]][seats[i]];
		}
		
		return total;
	}
}
