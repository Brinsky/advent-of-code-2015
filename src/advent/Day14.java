package advent;

import java.io.IOException;

public class Day14
{
	public static void main(String[] args) throws IOException 
	{
		String input = FileUtility.textFileToString("input/14.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(farthestDistance(input, 2503)), "output/14A.txt");

		// Part two
		FileUtility.stringToTextFile(Integer.toString(mostPoints(input, 2503)), "output/14B.txt");
	}
	
	private static int farthestDistance(String input, int time)
	{
		String[] lines = input.split("\r\n|\r|\n");
		
		// Read in the stats for each reindeer and compute their distance
		int farthest = Integer.MIN_VALUE;
		for (int i = 0; i < lines.length; ++i) {
			String[] tokens = lines[i].split("\\D+");

			int speed = Integer.parseInt(tokens[1]);
			int duration = Integer.parseInt(tokens[2]);
			int rest = Integer.parseInt(tokens[3]);
			
			int fullCycles = time / (duration + rest); // Number of full sprint-rest cycles
			int remainder = time % (duration + rest); // Remaining time after last full cycle
			
			// Only track the last cycle up to the point where resting begins
			if (remainder > duration)
				remainder = duration;
			
			int total = speed * (fullCycles * duration + remainder);
			
			if (total > farthest)
				farthest = total;
		}
		
		return farthest;
	}
	
	private static int mostPoints(String input, int time)
	{
		String[] lines = input.split("\r\n|\r|\n");
		Reindeer[] reindeer = new Reindeer[lines.length];
		
		// Read in the stats for each reindeer
		for (int i = 0; i < lines.length; ++i) {
			String[] tokens = lines[i].split("\\D+");
			reindeer[i] = new Reindeer(Integer.parseInt(tokens[1]),
					Integer.parseInt(tokens[2]),
					Integer.parseInt(tokens[3]));
		}
		
		// Step through each 1-second interval
		for (int i = 0; i < time; ++i) {
			// Compute new positions (and farthest) for all reindeer
			int farthest = Integer.MIN_VALUE;
			for (int j = 0; j < reindeer.length; ++j) {
				reindeer[j].takeTurn();
				if (reindeer[j].getPosition() > farthest)
					farthest = reindeer[j].getPosition();
			}
			
			// Award points to those in the lead
			for (int j = 0; j < reindeer.length; ++j)
				if (reindeer[j].getPosition() == farthest)
					reindeer[j].awardPoint();
		}
		
		// Determine who received the most points
		int mostPoints = Integer.MIN_VALUE;
		for (int i = 0; i < reindeer.length; ++i)
			if (reindeer[i].getPoints() > mostPoints)
				mostPoints = reindeer[i].getPoints();
		
		return mostPoints;
	}
	
	private static class Reindeer
	{
		public final int speed;
		public final int duration;
		public final int rest;
		
		private int position = 0;
		private int points = 0;
		private int time = 0;
		
		public Reindeer(int speed, int duration, int rest)
		{
			this.speed = speed;
			this.duration = duration;
			this.rest = rest;
		}
		
		public void takeTurn()
		{
			// Increment speed whenever we're in the "sprint" period
			if (time % (duration + rest) < duration)
				position += speed;

			time++;
		}
		
		public int getPosition()
		{
			return position;
		}
		
		public void awardPoint()
		{
			points++;
		}
		
		public int getPoints()
		{
			return points;
		}
	}
}
