package advent;
import java.io.IOException;

public class Day08
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/08.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(encodedToDecoded(input)), "output/08A.txt");
		
		// Part two
		FileUtility.stringToTextFile(Integer.toString(decodedToEncoded(input)), "output/08B.txt");
	}
	
	private static int encodedToDecoded(String input)
	{
		String[] lines = input.split("\r\n|\r|\n");

		int total = 0;
		for (String line : lines)
			total += line.length() - decodedSize(line);
		
		return total;
	}
	
	private static int decodedSize(String encoded)
	{
		// Turn all escape sequences into single characters
		String decoded = encoded.replaceAll("(\\\\x[\\da-f][\\da-f])|(\\\\\")|(\\\\\\\\)", "%");
		
		return decoded.length() - 2; // -2 for enclosing quotes
	}
	
	private static int decodedToEncoded(String input)
	{
		String[] lines = input.split("\r\n|\r|\n");

		int total = 0;
		for (String line : lines)
			total += encodedSize(line) - line.length();
		
		return total;
	}
	
	private static int encodedSize(String decoded)
	{
		// Only quotes and backslashes need to be dealt with
		String encoded = decoded.replace("\\", "\\\\");
		encoded = encoded.replace("\"", "\\\"");
		
		return encoded.length() + 2; // +2 for enclosing quotes
	}
}
