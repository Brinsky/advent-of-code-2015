package advent;

import java.io.IOException;

public class Day24
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/24.txt");
		
		// Part one
		FileUtility.stringToTextFile(Long.toString(idealQuantum(input, 3)), "output/24A.txt");

		// Part two
		FileUtility.stringToTextFile(Long.toString(idealQuantum(input, 4)), "output/24B.txt");
	}
	
	private static class FirstGroup implements Comparable<FirstGroup>
	{
		public final long quantum;
		public final int packages;
		
		public FirstGroup(long quantum, int packages)
		{
			this.quantum = quantum;
			this.packages = packages;
		}
		
		@Override
		public int compareTo(FirstGroup other)
		{
			if (other == null || packages < other.packages
					|| (packages == other.packages && quantum < other.quantum))
				return -1;
			
			if (quantum == other.quantum)
				return 0;
			else
				return 1;
		}
	}
	
	private static long idealQuantum(String input, int groups)
	{
		String[] lines = input.split("\r\n|\r|\n");
		int[] weights = new int[lines.length];
		int totalSum = 0;
		
		for (int i = 0; i < lines.length; ++i) {
			weights[i] = Integer.parseInt(lines[i]);
			totalSum += weights[i];
		}
		
		boolean[] picked = new boolean[lines.length];
		
		return bestFirstGroup(picked, weights, 0, 0, 0, 1,
				totalSum / groups, groups).quantum;
	}

	private static FirstGroup bestFirstGroup(boolean[] groups, int[] weights,
			int index, int count, int sum, long product, int targetSum,
			int numGroups)
	{
		// If we find a group with the right sum, we must verify that
		// corresponding groups also exist
		if (sum == targetSum) {
			if (verifyGroups(groups, weights, 0, 0, targetSum, numGroups - 2))
				return new FirstGroup(product, count);
			else
				return null;
		}
		
		// Conditions in which the group is invalid
		if (sum > targetSum || index >= groups.length)
			return null;

		FirstGroup best = null;
		FirstGroup group;
		
		// Pursue the branch where this package is included
		groups[index] = true;
		group = bestFirstGroup(groups, weights, index + 1, count + 1,
				sum + weights[index], product * weights[index], targetSum, numGroups);
		if (group != null && group.compareTo(best) < 0)
			best = group;
		
		// Pursue the branch where this package is NOT included
		groups[index] = false;
		group = bestFirstGroup(groups, weights, index + 1, count,
				sum, product, targetSum, numGroups);
		if (group != null && group.compareTo(best) < 0)
			best = group;
		
		return best;
	}
	
	/** Ensures that N other valid groups exist */
	private static boolean verifyGroups(boolean[] groups, int[] weights,
			int index, int sum, int targetSum, int numGroups)
	{
		// If we find a group with the right sum, we must verify that
		// corresponding groups also exist
		if (sum == targetSum) {
			if (numGroups == 1)
				return true;
			else
				return verifyGroups(groups, weights, 0, 0,
						targetSum, numGroups - 1);
		}

		// Conditions in which the group is invalid
		if (sum > targetSum || index >= groups.length)
			return false;
		
		// If this package is still available, attempt to include it
		if (!groups[index]) {
			groups[index] = true;
			if (verifyGroups(groups, weights, index + 1, sum + weights[index],
					targetSum, numGroups))
				return true;
		}

		// Pursue the branch where this package is NOT included in this group.
		// If this fails, the entire search has failed
		groups[index] = false;
		return verifyGroups(groups, weights, index + 1, sum, targetSum, numGroups);
	}
}
