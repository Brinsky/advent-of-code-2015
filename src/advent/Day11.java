package advent;

import java.io.IOException;

public class Day11 
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/11.txt");
		
		// Part one
		String result = findNextPassword(input);
		FileUtility.stringToTextFile(result, "output/11A.txt");
		
		// Part two
		FileUtility.stringToTextFile(findNextPassword(result), "output/11B.txt");
	}
	
	private static String findNextPassword(String old)
	{
		char[] digits = old.toCharArray();
		
		while (true) {
			for (int i = digits.length - 1; i >= 0; --i) {
				if (digits[i] == 'z') {
					digits[i] = 'a';
				} else {
					digits[i]++;
					break;
				}
			}
			
			if (legalCharacters(digits) 
					&& hasThreeAscending(digits) 
					&& twoUniquePairs(digits))
				return new String(digits);
		}
	}
	
	private static boolean legalCharacters(char[] digits)
	{
		for (char current : digits)
			if (current == 'i' || current == 'o' || current == 'l')
				return false;
		
		return true;
	}
	
	private static boolean hasThreeAscending(char[] digits)
	{
		for (int i = 0; i < digits.length - 3; ++i)
			if (digits[i] == digits[i + 1] - 1 
					&& digits[i] == digits[i + 2] - 2)
				return true;
		
		return false;
	}
	
	private static boolean twoUniquePairs(char[] digits)
	{
		boolean foundPair = false;
		char firstPair = 0;
		for (int i = 1; i < digits.length; ++i) {
			if (digits[i - 1] == digits[i]) {
				if (!foundPair) {
					foundPair = true;
					firstPair = digits[i];
					i++; // Next pair would be incl. same char, skip one
				} else if (digits[i] != firstPair) {
					return true;
				} else {
					// Current char is invalid and next pair would include it,
					// so skip one
					i++;
				}
			}
		}
		
		return false;
	}
}
