package advent;

import java.io.IOException;
import java.util.Arrays;

public class Day22 
{
	public static void main(String[] args) throws IOException 
	{
		String input = FileUtility.textFileToString("input/22.txt");
		
		/* Input handling */
		
		String[] lines = input.split("\r\n|\r|\n");
		
		int bossHP = Integer.parseInt(lines[0].split("\\D+")[1]);
		int bossDamage = Integer.parseInt(lines[1].split("\\D+")[1]);

		Boss boss = new Boss(bossHP, bossDamage);
		Player player = new Player(50, 0, 500, 0);
		int[] timers = new int[NUM_SPELLS];
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(
				minimumMana(player, boss, timers, 0, false)), "output/22A.txt");

		// Part two
		FileUtility.stringToTextFile(Integer.toString(
				minimumMana(player, boss, timers, 0, true)), "output/22B.txt");
	}
	
	/* Spell behavior */
	private static final int NUM_SPELLS =		5;
	private static final int[] MANA_COSTS = 	{ 53, 73, 113, 173, 229 };
	private static final int[] DURATION =		{ 0, 0, 6, 6, 5 };
	
	// Immediate impacts
	private static final int[] HP =				{ 0, 2, 0, 0, 0 };
	private static final int[] DAMAGE =			{ 4, 2, 0, 0, 0 };
	
	// Duration effects
	private static final int[] ARMOR_EFFECT = 	{ 0, 0, 7, 0, 0 };
	private static final int[] MANA_EFFECT = 	{ 0, 0, 0, 0, 101 };
	private static final int[] DAMAGE_EFFECT =	{ 0, 0, 0, 3, 0 };
	
	private static class Player
	{
		public int hp = 0;
		public int armor = 0;
		public int mana = 0;
		public int manaSpent = 0;
		
		public Player(int hp, int armor, int mana, int manaSpent)
		{
			this.hp = hp;
			this.armor = armor;
			this.mana = mana;
			this.manaSpent = manaSpent;
		}
		
		public Player clone()
		{
			return new Player(hp, armor, mana, manaSpent);
		}
	}
	
	private static class Boss
	{
		public int hp = 0;
		public int damage = 0;
		
		public Boss(int hp, int damage)
		{
			this.hp = hp;
			this.damage = damage;
		}
		
		public Boss clone()
		{
			return new Boss(hp, damage);
		}
	}
	
	/** Does NOT modify any of its parameters */
	private static int minimumMana(Player playerInitial, Boss bossInitial,
			int[] timersInitial, int turn, boolean hardMode)
	{
		int minimumMana = Integer.MAX_VALUE;
		for (int spell = 0; spell < NUM_SPELLS; ++spell) {
			if (isLegal(spell, playerInitial.mana, timersInitial)) {
				// Clone all data to avoid modifying other branches
				Player player = playerInitial.clone();
				Boss boss = bossInitial.clone();
				int[] timers = Arrays.copyOf(timersInitial, timersInitial.length);
				
				// Process player's turn and check for victory
				playerTurn(player, boss, spell, timers, hardMode);
				if (boss.hp <= 0) {
					if (player.manaSpent < minimumMana)
						minimumMana = player.manaSpent;
					continue;
				}
				
				// Process boss's turn and check for victory or loss
				bossTurn(player, boss, timers);
				if (boss.hp <= 0) {
					if (player.manaSpent < minimumMana)
						minimumMana = player.manaSpent;
					continue;
				} else if (player.hp <= 0) {
					continue;
				}
				
				// If neither opponent is dead, continue taking turns
				int mana = minimumMana(player, boss, timers, turn + 1, hardMode);
				if (minimumMana < 0 || (mana > 0 && mana < minimumMana))
					minimumMana = mana;
			}
		}
		
		return minimumMana;
	}
	
	private static boolean isLegal(int spell, int mana, int[] timers)
	{
		// To cast an effect spell, any current instance of it must be ending
		// this turn (or not be active at all)
		return MANA_COSTS[spell] <= mana && timers[spell] <= 1;
	}
	
	private static void playerTurn(Player player, Boss boss, int spell,
			int[] timers, boolean hardMode)
	{
		if (hardMode) {
			player.hp -= 1;
			if (player.hp <= 0)
				return;
		}
		
		// Process immediate impact of spell
		player.mana -= MANA_COSTS[spell];
		player.manaSpent += MANA_COSTS[spell];
		player.hp += HP[spell];
		boss.hp -= DAMAGE[spell];
		
		applyEffects(player, boss, timers);
		
		timers[spell] = DURATION[spell];
	}
	
	private static void bossTurn(Player player, Boss boss, int[] timers)
	{
		applyEffects(player, boss, timers);
		
		if (boss.hp <= 0)
			return;
		
		player.hp -= Math.max(boss.damage - player.armor, 1);
	}
	
	/** Returns the amount of armor temporarily provided to the player */
	private static void applyEffects(Player player, Boss boss, int[] timers)
	{
		player.armor = 0; // Reset armor each turn
		
		// Process currently active effects
		for (int i = 0; i < NUM_SPELLS; ++i) {
			if (timers[i] > 0) {
				player.armor += ARMOR_EFFECT[i];
				player.mana += MANA_EFFECT[i];
				boss.hp -= DAMAGE_EFFECT[i];
				timers[i]--;
			}
		}
	}
}
