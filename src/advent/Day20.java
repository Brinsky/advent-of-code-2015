package advent;

import java.io.IOException;

public class Day20 
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/20.txt");
		
		int minimum = Integer.parseInt(input);
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(firstHouse(minimum)), "output/20A.txt");
		
		// Part two
		FileUtility.stringToTextFile(Integer.toString(firstHouseLimited(minimum)), "output/20B.txt");
	}
	
	private static int firstHouse(int minimum)
	{
		int presents;
		int house = 1;
		
		do {
			presents = 0;
			house++;
			
			// Sum the factors of each house (times ten)
			// Unique factor-pairs only exist up to sqrt(house)
			for (int i = 1; i*i < house; ++i)
				if (house % i == 0)
					presents += i + (house / i);
			
			presents *= 10;
			
		} while (presents < minimum);
		
		return house;
	}
	
	/**
	 * The first house to rise above the minimum, where elves are limited to
	 * delivering 50 presents each
	 */
	private static int firstHouseLimited(int minimum)
	{
		int presents;
		int house = 1;
		
		// The lowest numbered elf who is still delivering
		int lower = 1;
		
		do {
			presents = 0;
			house++;
			
			// After 50 deliveries the lowest elf changes
			if (house / lower > 50)
				lower++;
			
			// Sum the factors of each house (times ten)
			// Unique factor-pairs only exist up to sqrt(house)
			for (int i = 1; i*i < house; ++i) {
				if (house % i == 0) {
					// Only accept presents from elves who are still delivering
					if (i > lower)
						presents += i;
					if (house / i > lower)
						presents += house / i;
				}
			}
			
			presents *= 11;
			
		} while (presents < minimum);
		
		return house;
	}
}
