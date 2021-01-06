package game.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.utils.Rand;

public class Drops {
	
	public static Map<String, List<Drop>> drops = new HashMap<String, List<Drop>>();
	
	public static void init()
	{
		addDrops("angry_stones", new Drop(Items.COPPER_ORE, 1), new Drop(Items.LEAD_ORE, 1), new Drop(Items.ROCK, 3), null);
		addDrops("rotten2", null, new Drop(Items.COPPER_ORE, 1), null, new Drop(Items.COPPER_ORE, 2), null);
		addDrops("fire", null, null, new Drop(Items.COPPER_ORE, 1), null);
		
		addDrops("imp", null, null, new Drop(Items.LEAD_ORE, 1), null);
		addDrops("black_bandit", null, new Drop(Items.LEAD_ORE, 1), new Drop(Items.LEAD_ORE, 2), null, null);
		addDrops("dark_goblin", null, new Drop(Items.LEAD_ORE, 7), new Drop(Items.LEAD_ORE, 3),  new Drop(Items.LEAD_ORE, 1), null, null);
		addDrops("udsmideren", new Drop(Items.LEAD_ORE, 3), new Drop(Items.LEAD_ORE, 6), null);
		addDrops("transparent_man", null, new Drop(Items.DARK_COMPOUND, 1), null, null, null);
		addDrops("cursed_sword", null, new Drop(Items.LEAD_ORE, 3), new Drop(Items.SILVER_ORE, 3),  new Drop(Items.LEAD_ORE, 1), null, null);
		
		addDrops("treant", null, new Drop(Items.MAGICAL_LEAF, 3), null, null, new Drop(Items.LEAD_ORE, 1), null, new Drop(Items.HERB, 10));
		addDrops("sapling", null, null, null, new Drop(Items.MAGICAL_LEAF, 1), null, null, new Drop(Items.HERB, 2), new Drop(Items.LEAD_ORE, 1), null, null);
		
		addDrops("rotten", null, null, new Drop(Items.HERB, 1), null, null, new Drop(Items.SILVER_ORE, 1), null, null);
		
		addDrops("orc_closeenough", null, null, new Drop(Items.MALACHITE_ORE, 1), null, null, new Drop(Items.HERB, 1), null);
		addDrops("snake", null, null, null, null, null, new Drop(Items.MALACHITE_ORE, 1), null, null, null, new Drop(Items.HERB, 1));
		addDrops("slime2", null, null, null, null, null, new Drop(Items.MALACHITE_ORE, 1), null, null, null, null, new Drop(Items.HERB, 1), null, null);
		addDrops("slime3", null, new Drop(Items.HERB, 1), null, null, null, new Drop(Items.MALACHITE_ORE, 1), null, new Drop(Items.HERB, 1), null, null, new Drop(Items.HERB, 1), null, null);
		addDrops("turtle", null, new Drop(Items.MALACHITE_ORE, 1), null, null);
		addDrops("goblin", null, null, new Drop(Items.MALACHITE_ORE, 1), null, null, null, new Drop(Items.HERB, 1), null, new Drop(Items.HERB, 2));
		
		
		addDrops("rotten", new Drop(Items.ROCK, 1), null);
		addDrops("slime2", null, new Drop(Items.ROCK, 1), new Drop(Items.HERB, 1), new Drop(Items.HERB, 2));
		addDrops("bluetooth", null, null, null, null, new Drop(Items.DAIZUM_ORE, 1), null, null, null, null, null);
		addDrops("frost_skeleton", null, null, null, null, new Drop(Items.DAIZUM_ORE, 1), null, null, null, null, null, null);
		addDrops("mask_of_horror", null, null, null, null, new Drop(Items.DAIZUM_ORE, 1), null, null, null, null, null, null, null);
		addDrops("slime4", null, null, null, null, null, new Drop(Items.DAIZUM_ORE, 1), null, null, null, null, null, null, null);
		addDrops("eye_of_twilight", null, null, null, null, new Drop(Items.DAIZUM_ORE, 1), null, null, null);
		addDrops("twilight_golem", null, null, new Drop(Items.DAIZUM_ORE, 2), null, new Drop(Items.DAIZUM_ORE, 1));
		addDrops("snake_of_twilight", null, null, null, new Drop(Items.DAIZUM_ORE, 1), null, null, null);
		addDrops("xilober", null, null, new Drop(Items.DAIZUM_ORE, 5), null, new Drop(Items.DAIZUM_ORE, 2), null, null);
		addDrops("twilight_dragon", null, null, new Drop(Items.DAIZUM_ORE, 1), null, new Drop(Items.DAIZUM_ORE, 2), null, null);
		addDrops("eye_of_twilight", null, null, null, null, new Drop(Items.DAIZUM_ORE, 1), null, null, null);
		
	}
	
	public static void addNulls(String enemy, int count)
	{
		List<Drop> drops = Drops.drops.get(enemy);
		if(drops == null)
		{
			drops = new ArrayList<Drop>();
			Drops.drops.put(enemy, drops);
		}
		
		for(int i = 0; i < drops.size(); i++)
		{
			drops.add(null);
		}
	}
	
	public static void addDrops(String enemy, Drop... dropArray)
	{
		List<Drop> drops = Drops.drops.get(enemy);
		if(drops == null)
		{
			drops = new ArrayList<Drop>();
			Drops.drops.put(enemy, drops);
		}
		for(int i = 0; i < dropArray.length; i++)
		{
			drops.add(dropArray[i]);
		}
	}
	
	public static void addDrop(String enemy, Drop drop)
	{
		List<Drop> drops = Drops.drops.get(enemy);
		if(drops == null)
		{
			drops = new ArrayList<Drop>();
			Drops.drops.put(enemy, drops);
		}
		drops.add(drop);
	}
	
	//May return null
	public static Drop rollDrop(String enemy)
	{
		if(enemy == null)
			return null;
		boolean inversed = false;
		if(enemy.startsWith("inv_"))
			inversed = true;
		List<Drop> drops = Drops.drops.get(enemy);
		if(drops == null)
			return null;
		int random = Rand.rng.nextInt(drops.size());
		Drop drop = drops.get(random);
		if(drop != null)
			return new Drop(drop.item, drop.getCount() + (inversed ? drop.getCount() * 2 : 0));
		return null;
	}
	
	public static class Drop
	{
		private int item, countMin, countMax;
		
		public Drop(int item, int count)
		{
			this.item = item;
			this.countMin = count;
			this.countMax = count;
		}
		
		public Drop(int item, int countMin, int countMax)
		{
			this.item = item;
			this.countMin = countMin;
			this.countMax = countMax;
		}
		
		public int getCount()
		{
			return countMin + Rand.rng.nextInt(countMax);
		}
		
		public int getItem()
		{
			return item;
		}
	}
	
}
