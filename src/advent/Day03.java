package advent;
import java.util.HashSet;
import java.util.Set;

public class Day03 
{
	public static void main(String[] args) throws Exception
	{
		String input = FileUtility.textFileToString("input/03.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(getUniqueHouses(input, 1)), "output/03A.txt");
		
		// Part two
		FileUtility.stringToTextFile(Integer.toString(getUniqueHouses(input, 2)), "output/03B.txt");
	}
	
	/** General solution supporting any positive number of santas */
	private static int getUniqueHouses(String input, int santas) throws Exception
	{
		Set<Point> houses = new HashSet<Point>(input.length());
		
		// All start at zero
		int[] x = new int[santas];
		int[] y = new int[santas];
			
		houses.add(new Point(0, 0));
		
		// Modulo to get current position for the correct santa / robo santa
		// at the given step
		for (int i = 0; i < input.length(); ++i) {
			switch (input.charAt(i)) {
			case '>': x[i % santas]++; break;
			case '^': y[i % santas]++; break;
			case '<': x[i % santas]--; break;
			case 'v': y[i % santas]--; break;
			default: throw new Exception("Invalid directional character: " + input.charAt(i));
			}
			
			houses.add(new Point(x[i % santas], y[i % santas]));
		}
		
		return houses.size();
	}

	private static class Point
	{
		public final int a;
		public final int b;
		
		public Point(int a, int b)
		{
			this.a = a;
			this.b = b;
		}
		
		@Override
		public int hashCode()
		{
			// Just made this up - maybe it will give decent distribution of similar #s?
			// (the multipliers are prime numbers)
			return a * 97 + b * 47;
		}
		
		@Override
		public boolean equals(Object o)
		{
			if (o instanceof Point) {
				Point p = (Point) o;
				return a == p.a && b == p.b;
			}
			
			return false;
		}
	}
}
