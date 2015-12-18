package advent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/15.txt");

		int[][] matrix = ingredientMatrix(input); // Matrix of ingredient properties
		
		// Part one
		int[] amounts = new int[matrix.length]; // Amounts of each ingredient being used
		FileUtility.stringToTextFile(
				Integer.toString(bestCombo(matrix, amounts, 0, 0, -1).score),
				"output/15A.txt");
		
		// Part two
		amounts = new int[matrix.length]; // Amounts of each ingredient being used
		FileUtility.stringToTextFile(
				Integer.toString(bestCombo(matrix, amounts, 0, 0, 500).score),
				"output/15B.txt");
	}
	
	private static final int PROPERTY_COUNT = 5;

	/** @param calories	Calorie requirement to meet, set negative to ignore */
	private static Score bestCombo(int[][] matrix, int[] amounts, 
			int currentIngredient, int total, int calories)
	{
		if (total == 100)
			return cookieScore(matrix, amounts);

		// "Exhaust" as much of a current ingredient as possible before moving on
		// to the next one
		Score best = new Score(Integer.MIN_VALUE, -1);
		for (int i = currentIngredient; i < matrix.length; ++i) {
			amounts[i]++;

			Score current = bestCombo(matrix, amounts, i, total + 1, calories);
			// If calories are enabled, make sure they match
			if (current.score > best.score 
					&& (calories < 0 || current.calories == calories))
				best = current;

			amounts[i]--;
		}
		
		return best;
	}
	
	private static Score cookieScore(int[][] properties, int[] amounts)
	{
		int[] results = new int[PROPERTY_COUNT - 1];
		int calories = 0;
		
		for (int i = 0; i < properties.length; ++i) {
			calories += properties[i][PROPERTY_COUNT - 1] * amounts[i];
			for (int j = 0; j < results.length; ++j)
				results[j] += properties[i][j] * amounts[i];
		}
		
		int score = 1;
		for (int i = 0; i < results.length; ++i)
			if (results[i] > 0)
				score *= results[i];
		
		return new Score(score, calories);
	}
	
	private static int[][] ingredientMatrix(String input)
	{
		String[] lines = input.split("\r\n|\r|\n");
		int[][] matrix = new int[lines.length][PROPERTY_COUNT];
		
		for (int i = 0; i < lines.length; ++i) {
			int[] properties = getInts(lines[i]);
			for (int j = 0; j < properties.length; ++j)
				matrix[i][j] = properties[j];
		}
		
		return matrix;
	}
	
	private static final Pattern integer = Pattern.compile("-?\\d+");
	
	private static int[] getInts(String string)
	{
		List<Integer> ints = new ArrayList<Integer>();
		
		Matcher matcher = integer.matcher(string);
		while (matcher.find())
			ints.add(Integer.parseInt(matcher.group()));
		
		int[] array = new int[ints.size()];
		for (int i = 0; i < ints.size(); ++i)
			array[i] = ints.get(i);
		
		return array;
	}
	
	private static class Score
	{
		public final int score;
		public final int calories;
		
		public Score(int score, int calories)
		{
			this.score = score;
			this.calories = calories;
		}
	}
}
