package advent;

import java.io.IOException;

public class Day16
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/16.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(whichSue(input, false)), "output/16A.txt");
		
		// Part two
		FileUtility.stringToTextFile(Integer.toString(whichSue(input, true)), "output/16B.txt");
	}
	
	private static final int NUM_PROPERTIES = 10;

	private static int whichSue(String input, boolean ranges)
	{
		String[] lines = input.split("\r\n|\r|\n");
		
		// Compute matches for each aunts
		int mostMatches = Integer.MIN_VALUE;
		int aunt = -1;
		for (int i = 0; i < lines.length; ++i) {
			// Pre-fill each unknown value with a -1
			int[] properties = new int[NUM_PROPERTIES];
			for (int j = 0; j < properties.length; ++j)
				properties[j] = -1;
			
			// Match each property/value pair to the correct matrix index
			String[] tokens = lines[i].split(",?:?\\s+");
			for (int j = 2; j < tokens.length; j += 2)
				properties[parseProperty(tokens[j])]
						= Integer.parseInt(tokens[j + 1]);
			
			// Calculate how many matches that Sue has
			int matches = ranges ? countMatchesRanges(properties) : countMatches(properties);
			
			// Add all non-negative Sue's to the master list of possibilities
			if (matches >= 0 && matches > mostMatches) {
				mostMatches = matches;
				aunt = i + 1;
			}
		}
		
		return aunt;
	}
	
	private static final int[] PROPERTIES = { 3, 7, 2, 3, 0, 0, 5, 3, 2, 1 };
	
	/** 
	 * Count the number of properties that match, return -1 if an explicit
	 * mismatch exists.
	 */
	public static int countMatches(int[] properties)
	{
		int matches = 0;
		
		for (int i = 0; i < NUM_PROPERTIES; ++i) {
			if (properties[i] >= 0) {
				if (properties[i] == PROPERTIES[i]) {
					matches++;
				} else {
					return -1;
				}
			}
		}
		
		return matches;
	}
	
	public static int countMatchesRanges(int[] properties)
	{
		int matches = 0;
		
		for (int i = 0; i < NUM_PROPERTIES; ++i) {
			// Separate conditions are just for clarity here
			if (properties[i] >= 0) {
				// Trees and cats are greater than
				if (i == 1 || i == 7) {
					if (properties[i] > PROPERTIES[i])
						matches++;
				// Pomeranians and goldfish are less than
				} else if (i == 3 || i == 6) {
					if (properties[i] < PROPERTIES[i])
						matches++;
				// Everything else should just equal
				} else if (properties[i] == PROPERTIES[i]) {
					matches++;
				} else {
					return -1;
				}
			}
		}
		
		return matches;
	}
	
	public static int parseProperty(String name)
	{
		switch (name) {
		case "children":	return 0;
		case "cats":		return 1;
		case "samoyeds":	return 2;
		case "pomeranians":	return 3;
		case "akitas":		return 4;
		case "vizslas":		return 5;
		case "goldfish":	return 6;
		case "trees":		return 7;
		case "cars":		return 8;
		case "perfumes":	return 9;
		default:			return -1;
		}
	}
}
