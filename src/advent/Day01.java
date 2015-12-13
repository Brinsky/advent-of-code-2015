package advent;
import java.io.IOException;

public class Day01
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/01.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(getFloor(input)), "output/01A.txt");
		
		// Part two
		FileUtility.stringToTextFile(Integer.toString(getBasementEntry(input)), "output/01B.txt");
	}
	
	private static int getFloor(String input)
	{
		int floor = 0;
		
		for (char current : input.toCharArray())
			floor += (current == '(') ? 1 : -1;
		
		return floor;
	}
	
	private static int getBasementEntry(String input)
	{
		int floor = 0;
		int index = 1;
		
		for (char current : input.toCharArray()) {
			floor += (current == '(') ? 1 : -1;
		
			if (floor == -1)
				return index;
			
			index++;
		}
		
		return -1;
	}
}
