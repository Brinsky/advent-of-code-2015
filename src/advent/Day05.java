package advent;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Day05 
{
	public static void main(String[] args) throws IOException 
	{
		String input  = FileUtility.textFileToString("input/05.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(countNice(input)), "output/05A.txt");

		// Part two
		FileUtility.stringToTextFile(Integer.toString(countNewNice(input)), "output/05B.txt");
	}
	
	private static int countNice(String input)
	{
		Scanner in = new Scanner(input);
		
		int nice = 0;
		while (in.hasNextLine())
			if (isNice(in.nextLine()))
				nice++;
		
		in.close();
		return nice;
	}
	
	private static int countNewNice(String input)
	{
		Scanner in = new Scanner(input);
		
		int nice = 0;
		while (in.hasNextLine())
			if (isNewNice(in.nextLine()))
				nice++;
		
		in.close();
		return nice;
	}
	
	private static final Pattern pairTwice = Pattern.compile("(..).*\\1");
	private static final Pattern repeatSkip = Pattern.compile("(.).\\1");
	
	private static boolean isNewNice(String string)
	{
		// Check for non-overlapping pair repeats and a repeat after skipping one char
		return pairTwice.matcher(string).find() && repeatSkip.matcher(string).find();
	}

	private static final Pattern twiceOrMore = Pattern.compile("(.)\\1");
	private static final Pattern excluded = Pattern.compile("(ab)|(cd)|(pq)|(xy)");
	
	private static boolean isNice(String string)
	{
		// Count vowels
		int vowels = 0;
		for (char current : string.toCharArray())
			if (isVowel(current))
				vowels++;
		
		if (vowels < 3)
			return false;
		
		// Check for side-by-side appearances of the same character and for disallowed characters
		return !twiceOrMore.matcher(string).find() && excluded.matcher(string).find();
	}
	
	private static boolean isVowel(char value)
	{
		return value == 'a' || value == 'e' || value == 'i' 
				|| value == 'o' || value == 'u';
	}
}
