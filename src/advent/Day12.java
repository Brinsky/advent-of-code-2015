package advent;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/12.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(sumIntegers(input)), "output/12A.txt");
		
		// Part two
		FileUtility.stringToTextFile(Integer.toString(sumNoRed(input, 0)), "output/12B.txt");
	}

	private static final Pattern integer = Pattern.compile("\\-?\\d+");
	
	private static int sumIntegers(String input)
	{
		Matcher matcher = integer.matcher(input);
		
		int sum = 0;
		while (matcher.find())
			sum += Integer.parseInt(matcher.group());
		
		return sum;
	}
	
	/** @param start	One past the last opening brace '{' */
	private static int sumNoRed(String input, int start)
	{
		// Scan through the object and sum integers - recursively explore
		// child objects and check for "red" values
		int nextObject, objectEnd, scanTil; // Index trackers
		int sum = 0;
		do {
			// This only works if '{' and '}' are never present in strings
			nextObject = input.indexOf('{', start);
			objectEnd = input.indexOf('}', start);
			
			// If there's no closing brace, use EOF
			if (objectEnd < 0)
				objectEnd = input.length();
			
			// Determine how far we can scan/sum (things not in child objects)
			scanTil = nextObject;
			if (scanTil < 0 || objectEnd < scanTil)
				scanTil = objectEnd;
			
			// Scan/sum until this object ends or a child object starts
			String toScan = input.substring(start, scanTil);
			if (toScan.contains(":\"red\""))
				return 0;
			sum += sumIntegers(toScan);
			
			// Retreive sum from child object and jump over it
			if (scanTil == nextObject) {
				sum += sumNoRed(input, scanTil + 1);
				start = findObjectEnd(input, scanTil) + 1;
			}
		} while (scanTil == nextObject);
		
		return sum;
	}
	
	/** Given an index one past an opening brace '{', returns the index of a
	 * matching closing brace '}'.
	 */
	private static int findObjectEnd(String input, int start)
	{
		int count = 1;
		for (int i = start + 1; i < input.length(); ++i) {
			if (input.charAt(i) == '{') {
				count++;
			} else if (input.charAt(i) == '}') {
				count--;
				if (count == 0)
					return i;
			}
		}
		
		return -1;
	}
}
