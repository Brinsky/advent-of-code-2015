package advent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Day19
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/19.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(uniqueReplacements(input)), "output/19A.txt");
		
		// Part two
		FileUtility.stringToTextFile(Integer.toString(minReplacements(input)), "output/19B.txt");
	}
	
	private static int uniqueReplacements(String input)
	{
		// Read in input
		
		String[][] rules = buildRules(input);

		String[] lines = input.split("\r\n|\r|\n");
		String molecule = lines[lines.length - 1];
		
		// Find # of unique results from all single-step replacements
		
		Set<String> results = new HashSet<String>();
		
		// For each rule, find all valid replacements and add them to the set
		for (int i = 0; i < rules.length; ++i) {
			int index = -1;
			// Attempt to match each rule as many times as possible
			while ((index = molecule.indexOf(rules[i][0], index + 1)) >= 0) {
				results.add(replace(molecule, index,
						rules[i][0].length(), rules[i][1]));
			}
		}
		
		return results.size();
	}
	
	private static String replace(String original, int index,
			int length, String replacement)
	{
		String first = original.substring(0, index);
		
		// Only create a "last" segment if text remains beyond the match itself
		String last = "";
		if (index + length < original.length())
			last = original.substring(index + length);
		
		return first + replacement + last;
	}
	
	private static String[][] buildRules(String input)
	{
		String[] lines = input.split("\r\n|\r|\n");
		String[][] rules = new String[lines.length - 2][2];
		
		for (int i = 0; i < lines.length - 2; ++i) {
			String[] tokens = lines[i].split(" => ");
			rules[i][0] = tokens[0];
			rules[i][1] = tokens[1];
		}
		
		return rules;
	}
	
	private static int minReplacements(String input)
	{
		String[][] rules = buildRules(input);
		
		String[] lines = input.split("\r\n|\r|\n");
		String molecule = lines[lines.length - 1];
		
		return minReplacements(molecule, rules, 0);
	}
	
	/** 
	 * For some reason (related to the structure of the input), it turns out
	 * that ALL valid procedures have the same number of steps. Thus, we can
	 * terminate after finding the first solution.
	 */
	private static int minReplacements(String molecule, String[][] rules, int count)
	{
		if (molecule.equals("e"))
			return count;
		
		for (int i = 0; i < rules.length; ++i) {
			int index = -1;
			while ((index = molecule.indexOf(rules[i][1], index + 1)) >= 0) {
				int steps = minReplacements(replace(molecule, index,
						rules[i][1].length(), rules[i][0]), rules, count + 1);
				if (steps >= 0)
					return steps;
			}
		}
		
		return -1;
	}
}
