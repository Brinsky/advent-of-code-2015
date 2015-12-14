package advent;

import java.io.IOException;

public class Day10
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/10.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(sequenceLength(input, 40)), "output/10A.txt");

		// Part one
		FileUtility.stringToTextFile(Integer.toString(sequenceLength(input, 50)), "output/10B.txt");
	}
	
	private static int sequenceLength(String input, int iterations)
	{
		for (int i = 0; i < iterations; ++i)
			input = nextSequence(input);
		
		return input.length();
	}
	
	/** Performs one iteration of the "look-and-say" game on a decimal number */
	private static String nextSequence(String input)
	{
		StringBuilder builder = new StringBuilder();

		// Preload values for 0th element
		int count = 1;
		int previous = input.charAt(0) - '0';
		
		// Start at element 1 and check backwards
		for (int i = 1; i < input.length(); ++i) {
			// Convert to a numeric type
			int digit = input.charAt(i) - '0';
			
			// Output previous repetition info if digit has changed
			if (digit != previous) {
				builder.append(count); // Count may be > 9
				builder.append(previous);
				count = 0;
			}
				
			previous = digit;
			count++;
		}
		
		// Append info about final group
		builder.append(count);
		builder.append(previous);
		
		return builder.toString();
	}

	
}
