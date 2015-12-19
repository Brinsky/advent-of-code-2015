package advent;

import java.io.IOException;

public class Day18
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/18.txt");
		
		/* Part one */
		
		World world = new World(input, false);
		for (int i = 0; i < 100; ++i)
			world.takeStep();
		
		FileUtility.stringToTextFile(Integer.toString(world.countLights()), "output/18A.txt");
		
		/* Part two */
		
		world = new World(input, true);
		for (int i = 0; i < 100; ++i)
			world.takeStep();
		
		FileUtility.stringToTextFile(Integer.toString(world.countLights()), "output/18B.txt");
		
	}
	
	private static class World
	{
		private boolean[][] world = new boolean[100][100];
		private boolean[][] buffer = new boolean[100][100];
		
		private final boolean cornersStuckOn;
		
		public World(String input, boolean cornersStuckOn)
		{
			this.cornersStuckOn = cornersStuckOn;
			
			String[] lines = input.split("\r\n|\r|\n");
			
			for (int i = 0; i < 100; ++i) {
				for (int j = 0; j < 100; ++j) {
					if (isCorner(world, i, j))
						world[i][j] = true;
					else
						world[i][j] = lines[i].charAt(j) == '#';
				}
			}
		}
		
		public void takeStep()
		{
			for (int i = 0; i < 100; ++i) {
				for (int j = 0; j < 100; ++j) {
					int neighbors = neighbors(world, i, j);
					
					if (cornersStuckOn && isCorner(world, i, j)) {
						buffer[i][j] = true;
					} else if (world[i][j]) {
						if (neighbors == 2 || neighbors == 3)
							buffer[i][j] = true;
						else
							buffer[i][j] = false;
					} else {
						if (neighbors == 3)
							buffer[i][j] = true;
						else
							buffer[i][j] = false;
					}
				}
			}
			
			// Swap the two matrices (old world becomes new buffer)
			boolean[][] temp = world;
			world = buffer;
			buffer = temp;
		}
		
		public int countLights()
		{
			int count = 0;
			
			for (int i = 0; i < 100; ++i)
				for (int j = 0; j < 100; ++j)
					if (world[i][j])
						count++;
			
			return count;
		}
		
		/** Determines if a given point is in a corner of the world */
		private static boolean isCorner(boolean[][] world, int row, int column)
		{
			return (row == 0 && (column == 0 || column == world[0].length - 1))
					|| (row == world.length - 1 && (column == 0 || column == world[0].length - 1));
		}
		
		private static int neighbors(boolean[][] world, int row, int column)
		{
			int count = 0;
			
			// Loop over the 3x3 square surrounding a location, skipping the center
			for (int i = row - 1; i <= row + 1; ++i)
				for (int j = column - 1; j <= column + 1; ++j)
					if (!(i == row && j == column) && inBounds(world, i, j) && world[i][j])
						count++;
					
			return count;
		}
		
		private static boolean inBounds(boolean[][] world, int r, int c)
		{
			return r >= 0 && r < world.length && c >= 0 && c < world[0].length;
		}
	}
}
