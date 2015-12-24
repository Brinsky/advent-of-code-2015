package advent;

import java.io.IOException;

public class Day21
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/21.txt");
		
		/* Input processing */
		
		String[] lines = input.split("\r\n|\r|\n");
		
		int bossHP = Integer.parseInt(lines[0].split("\\D+")[1]);
		int bossAttack = Integer.parseInt(lines[1].split("\\D+")[1]);
		int bossArmor = Integer.parseInt(lines[2].split("\\D+")[1]);
		
		int[] items;
		
		// Part one
		items = new int[TYPE_VARIATIONS.length];
		FileUtility.stringToTextFile(Integer.toString(
				minimumCost(items, 0, 100, bossHP, bossAttack, bossArmor)), "output/21A.txt");

		// Part two
		items = new int[TYPE_VARIATIONS.length];
		FileUtility.stringToTextFile(Integer.toString(
				maximumCost(items, 0, 100, bossHP, bossAttack, bossArmor)), "output/21B.txt");
	}
	
	private static int minimumCost(int[] items, int index, int playerHP,
			int bossHP, int bossAttack, int bossArmor)
	{
		// Return the cost for any legal, winning item arrangement
		if (index >= items.length) {
			if (isLegal(items) && playerWins(items, playerHP, bossHP, bossAttack, bossArmor))
				return computeStat(items, COSTS);
			else
				return -1;
		}
		
		// Permute through all possible item arrangements
		int minimumCost = -1;
		for (int i = 0; i < TYPE_VARIATIONS[index]; ++i) {
			items[index] = i;
			int cost = minimumCost(items, index + 1, playerHP, bossHP, bossAttack, bossArmor);
			
			if (minimumCost < 0 || (cost > 0 && cost < minimumCost))
				minimumCost = cost;
		}
		
		return minimumCost;
	}

	private static int maximumCost(int[] items, int index, int playerHP,
			int bossHP, int bossAttack, int bossArmor)
	{
		// Return the cost for any legal, winning item arrangement
		if (index >= items.length) {
			if (isLegal(items) && !playerWins(items, playerHP, bossHP, bossAttack, bossArmor))
				return computeStat(items, COSTS);
			else
				return -1;
		}
		
		// Permute through all possible item arrangements
		int maximumCost = -1;
		for (int i = 0; i < TYPE_VARIATIONS[index]; ++i) {
			items[index] = i;
			int cost = maximumCost(items, index + 1, playerHP, bossHP, bossAttack, bossArmor);
			
			if (cost > maximumCost)
				maximumCost = cost;
		}
		
		return maximumCost;
	}
	
	private static final int[] TYPE_VARIATIONS = { 5, 6, 7, 7 };
	
	private static final int[][] COSTS =
	{
			{ 8, 10, 25, 40, 74 },
			{ 0, 13, 31, 53, 75, 102 },
			{ 0, 25, 50, 100, 20, 40, 80 },
			{ 0, 25, 50, 100, 20, 40, 80 }
	};
	
	private static final int[][] ATTACK =
	{
			{ 4, 5, 6, 7, 8 },
			{ 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 2, 3, 0, 0, 0 },
			{ 0, 1, 2, 3, 0, 0, 0 }
	};
	
	private static final int[][] ARMOR =
	{
			{ 0, 0, 0, 0, 0 },
			{ 0, 1, 2, 3, 4, 5 },
			{ 0, 0, 0, 0, 1, 2, 3 },
			{ 0, 0, 0, 0, 1, 2, 3 }
	};
	
	private static boolean playerWins(int[] items, int playerHP,
			int bossHP, int bossAttack, int bossArmor)
	{
		int playerAttack = computeStat(items, ATTACK);
		int playerArmor = computeStat(items, ARMOR);
		
		// Damage dealt by the given opponent per turn
		int playerDeals = Math.max(playerAttack - bossArmor, 1);
		int bossDeals = Math.max(bossAttack - playerArmor, 1);
		
		// Determine how many turns each opponent will survive for.
		// Round up if any remainder is present
		int bossLasts = bossHP / playerDeals 
				+ ((bossHP % playerDeals == 0) ? 0 : 1);
		int playerLasts = playerHP / bossDeals 
				+ ((playerHP % bossDeals == 0) ? 0 : 1);
		
		// >= because player goes first
		return playerLasts >= bossLasts;
	}
	
	private static boolean isLegal(int[] items)
	{
		return (items[2] == 0 || items[2] != items[3]);
	}
	
	private static int computeStat(int[] items, int[][] stats)
	{
		int value = 0;
		
		for (int type = 0; type < items.length; ++type)
			value += stats[type][items[type]];
		
		return value;
	}
}
