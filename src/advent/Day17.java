package advent;

import java.io.IOException;

public class Day17
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/17.txt");
		
		// Read in containers
		String[] lines = input.split("\r\n|\r|\n");
		int[] containers = new int[lines.length];
		for (int i = 0; i < lines.length; ++i)
			containers[i] = Integer.parseInt(lines[i]);
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(
				containerCombos(150, containers, 0, 0)), "output/17A.txt");
		
		// Part two
		FileUtility.stringToTextFile(Integer.toString(
				minimumComboCount(150, containers, 0, 0, 0).size),
				"output/17B.txt");
	}
	
	private static int containerCombos(int liters, int[] containers, 
			int current, int sum)
	{
		if (sum > liters)
			return 0;
		else if (sum == liters)
			return 1;
		
		int legalCombos = 0;
		for (int i = current; i < containers.length; ++i) {
			sum += containers[i];
			legalCombos += containerCombos(liters, containers, i + 1, sum);
			sum -= containers[i];
		}
		
		return legalCombos;
	}
	
	private static ComboGroup minimumComboCount(int liters, int[] containers, 
			int current, int sum, int containerCount)
	{
		if (sum > liters)
			return new ComboGroup();
		else if (sum == liters)
			return new ComboGroup(1, containerCount);
		
		ComboGroup fewest = new ComboGroup();
		for (int i = current; i < containers.length; ++i) {
			sum += containers[i];
			
			ComboGroup discovered = minimumComboCount(liters, containers, 
					i + 1, sum, containerCount + 1);
			
			if (discovered.size < fewest.size)
				fewest = discovered;
			else if (discovered.size == fewest.size)
				fewest.occurances += discovered.occurances;
			
			sum -= containers[i];
		}
		
		return fewest;
	}
	
	private static class ComboGroup
	{
		public int occurances = -1;
		public int size = Integer.MAX_VALUE;
		
		public ComboGroup() { }
		
		public ComboGroup(int occurances, int size)
		{
			this.occurances = occurances;
			this.size = size;
		}
	}
}
