package advent;

import java.io.IOException;

public class Day25
{
	public static void main(String[] args) throws IOException
	{
		// Part one
		FileUtility.stringToTextFile(Long.toString(generateUntil(3075, 2981, 6, 6, 27995004)), "output/25A.txt");
	
		// ... only true survivors will know of part two
	}

	private static long generateUntil(int stopX, int stopY, int startX, int startY, long start)
	{
		int x = startX, y = startY;
		long current = start;
		while (!(x == stopX && y == stopY)) {
			x++;
			y--;
			if (y <= 0) {
				y = x;
				x = 1;
			}
			current = (current * 252533) % 33554393;
		}
		
		return current;
	}
}
