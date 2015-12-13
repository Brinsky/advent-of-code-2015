package advent;
import java.io.IOException;
import java.util.Scanner;

public class Day06 {

	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/06.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(lightsOn(input)), "output/06A.txt");

		// Part two
		FileUtility.stringToTextFile(Integer.toString(lightsOnBrightness(input)), "output/06B.txt");
	}
	
	private static int lightsOn(String input)
	{
		Scanner in = new Scanner(input);
		boolean[][] lights = new boolean[1000][1000];
		
		Scanner lineIn;
		int lightsOn = 0;
		while (in.hasNextLine()) {
			String line = in.nextLine();
			Command cmd = Command.parseCommand(line);
			
			lineIn = new Scanner(line.replaceAll("[a-zA-Z ,]+", " "));
			Rect rect = new Rect(lineIn.nextInt(), lineIn.nextInt(),
					lineIn.nextInt(), lineIn.nextInt());
			lightsOn += enableLights(lights, rect, cmd);
			lineIn.close();
		}
		
		in.close();
		return lightsOn;
	}
	
	private static int enableLights(boolean[][] lights, Rect rect, Command cmd)
	{
		// Track lights that go from off-to-on (+1) and lights that go from
		// on-to-off (-1)
		int deltaOn = 0;
		for (int x = rect.x1; x <= rect.x2; ++x) {
			for (int y = rect.y1; y <= rect.y2; ++y) {
				switch (cmd) {
				case ON:
					if (lights[x][y] == false)
						deltaOn++;
					lights[x][y] = true;
					break;
				case OFF:
					if (lights[x][y] == true)
						deltaOn--;
					lights[x][y] = false;
					break;
				case TOGGLE:
					if (lights[x][y] == true)
						deltaOn--;
					else
						deltaOn++;
					lights[x][y] = !lights[x][y];
					break;
				}
			}
		}

		return deltaOn;
	}
	
	private static int lightsOnBrightness(String input)
	{
		Scanner in = new Scanner(input);
		int[][] lights = new int[1000][1000];
		
		Scanner lineIn;
		int brightness = 0;
		while (in.hasNextLine()) {
			String line = in.nextLine();
			Command cmd = Command.parseCommand(line);
			
			lineIn = new Scanner(line.replaceAll("[a-zA-Z ,]+", " "));
			Rect rect = new Rect(lineIn.nextInt(), lineIn.nextInt(),
					lineIn.nextInt(), lineIn.nextInt());
			brightness += alterBrightness(lights, rect, cmd);
			lineIn.close();
		}
		
		in.close();
		return brightness;
	}
	
	private static int alterBrightness(int[][] lights, Rect rect, Command cmd)
	{
		int deltaBrightness = 0;
		for (int x = rect.x1; x <= rect.x2; ++x) {
			for (int y = rect.y1; y <= rect.y2; ++y) {
				switch (cmd) {
				case ON:
					deltaBrightness++;
					lights[x][y]++;
					break;
				case OFF:
					if (lights[x][y] > 0) {
						deltaBrightness--;
						lights[x][y]--;
					}
					break;
				case TOGGLE:
					deltaBrightness += 2;
					lights[x][y] += 2;
					break;
				}
			}
		}

		return deltaBrightness;
	}
	
	private static enum Command 
	{
		ON, OFF, TOGGLE;
		
		public static Command parseCommand(String statement)
		{
			if (statement.startsWith("turn on"))
				return Command.ON;
			else if (statement.startsWith("turn off"))
				return Command.OFF;
			else
				return Command.TOGGLE;
		}
	}
	
	private static class Rect
	{
		public final int x1, x2, y1, y2; // Coordinates of the sides
		
		public Rect(int x1, int y1, int x2, int y2)
		{
			// Put the smaller coords in x1 and y1
			this.x1 = (x1 < x2) ? x1 : x2;
			this.x2 = (x1 > x2) ? x1 : x2;
			this.y1 = (y1 < y2) ? y1 : y2;
			this.y2 = (y1 > y2) ? y1 : y2;
		}
	}
}
