package advent;
import java.io.IOException;
import java.util.Scanner;

public class Day02
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/02.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(getTotalArea(input)), "output/02A.txt");

		// Part two
		FileUtility.stringToTextFile(Integer.toString(getRibbonLength(input)), "output/02B.txt");
	}
	
	private static int getTotalArea(String input)
	{
		Scanner in = new Scanner(input);
		int area = 0;
		
		while (in.hasNextLine()) {
			String[] dimensions = in.nextLine().split("x");
			int x = Integer.parseInt(dimensions[0]);
			int y = Integer.parseInt(dimensions[1]);
			int z = Integer.parseInt(dimensions[2]);
			
			area += 2 * (x * y + x * z + y * z);
			area += minimum(x * y, x * z, y * z);
		}
		
		in.close();
		return area;
	}
	
	private static int getRibbonLength(String input)
	{
		Scanner in = new Scanner(input);
		int length = 0;
		
		while (in.hasNextLine()) {
			String[] dimensions = in.nextLine().split("x");
			int x = Integer.parseInt(dimensions[0]);
			int y = Integer.parseInt(dimensions[1]);
			int z = Integer.parseInt(dimensions[2]);
			
			length += minimum(2 * x + 2 * y, 2 * x + 2 * z, 2 * y + 2 * z);
			length += x * y * z;
		}
		
		in.close();
		return length;
	}
	
	private static int minimum(int ... n)
	{
		int minimum = Integer.MAX_VALUE;
		
		for (int i : n)
			if (i < minimum)
				minimum = i;
		
		return minimum;
	}
}
