package advent;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 
{
	public static void main(String[] args) throws IOException
	{
		String input;
		
		input = FileUtility.textFileToString("input/12.txt");
		FileUtility.stringToTextFile(Integer.toString(sumIntegers(input)), "output/12A.txt");
	}

	private static final Pattern integer = Pattern.compile("\\-?\\d+");
	
	private static final int sumIntegers(String input)
	{
		Matcher matcher = integer.matcher(input);
		
		int sum = 0;
		while (matcher.find())
			sum += Integer.parseInt(matcher.group());
		
		return sum;
	}
}
