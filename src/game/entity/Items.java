package game.entity;

import java.util.ArrayList;
import java.util.List;

import game.graphics.Sprite;

public class Items {
	
	public static Sprite[] itemSprites;
	public static Sprite[] itemSprites_overlay;
	public static List<Item> availableItems = new ArrayList<Item>();
	
	public static final int EARTH_GEM = 39;
	public static final int SAPPHIRE = 40;
	public static final int EMERALD = 41;
	public static final int RUBY = 42;
	public static final int AMETHYST = 43;
	public static final int DIAMOND = 44;
	public static final int TOPAZ = 45;
	
	public static final int BLUE_PENDANT = 21;
	public static final int GREEN_PENDANT = 22;
	public static final int RED_PENDANT = 23;
	
//	public static final int GOLD = 46;
	
	public static final int ANCIENT_SCROLL = 35 + 13;
	public static final int MAGICAL_CAPE = 36 + 13;
	public static final int MAGIC_KEY = 37 + 13;
	public static final int SMITH_KEY = 153;
	
	public static final int ENCHANTED_SWORD = 103;
	public static final int CROWN = 35;
	
	public static final int CALMING_POTION = 64;
	public static final int CURSED_RING = 6;
	
	public static final int MONSTER_MANUAL = 168;
	public static final int BOOK_OF_DAMAGE_NUMBERS = 164;
	public static final int TELEPORT_UPGRADE = 165;
	public static final int FREE_MOVEMENT_BOOK = 181;
	public static final int SPEAR_OF_CYLOPIUM = 96;
	public static final int FASTBATTLE_BOOK = 167;
	public static final int BATTLE_SPEED_UPGRADE = 166;
	
	public static final int GLOVES_OF_STRENGTH = 140;
	public static final int MISSING_NOTE = 142;
	public static final int MAGIC_DICE = 155;
	public static final int MAGICAL_LEAF = 157;
	public static final int ROCK = 141;
	public static final int HERB = 171;
	
	public static final int HEALTH_POTION = 63;
	public static final int BOOK_OF_CONCEDE = 180;
	public static final int BOOTS_OF_ESCAPING = 135;
	public static final int RING_OF_TELEPORTATION = 3;
	
	public static final int BOOK_OF_FULLSCREEN = 179;
	public static final int FULLSCREEN_UPGRADE = 178;
	
	public static final int COPPER_ORE = 53;
	public static final int SILVER_ORE = 54;
	public static final int MALACHITE_ORE = 55;
	public static final int LEAD_ORE = 56;
	public static final int DAIZUM_ORE = 57;
	public static final int DARK_COMPOUND = 58;
	
	public static final int COPPER_INGOT = 53 + 13;
	public static final int SILVER_INGOT = 54 + 13;
	public static final int MALACHITE_INGOT = 55 + 13;
	public static final int LEAD_INGOT = 56 + 13;
	public static final int DAIZUM_INGOT = 57 + 13;
	public static final int DARK_INGOT = 58 + 13;
	
	public static final int MYSTERIOUS_KEY = 188;
	public static final int BOOK_OF_ALL_KNOWING = 176;
	public static final int OMEGA_PICKAXE = 83;
	
	public static final int BOOK_OF_COLORS = 177;
	
	public static final int OMNI_GEM = 182;
	
	public static int GOLD()
	{
		return 46;
	}
	
	public static class Item 
	{
		public String name;
		public int id;
		public Item(String name, int id)
		{
			this.name = name;
			this.id = id;
		}
	}
	
	public static void init()
	{
		itemSprites = Sprite.getAtlasFromSprite(Sprite.getSprite("roguelikeitems"), 13, 15, false);
		itemSprites_overlay = Sprite.getAtlasFromSprite(Sprite.getSprite("roguelikeitems_overlay"), 13, 15, false);
	}
	
	public static Sprite getItemSprite(int id)
	{
		return itemSprites[id];
	}
	
	public static Sprite getItemSpriteOverlay(int id)
	{
		return itemSprites_overlay[id];
	}
	
	
	public static String getItemName(int id)
	{
		for(int i = 0; i < availableItems.size(); i++)
		{
			Item item = availableItems.get(i);
			if(id == item.id)
			{
				return item.name;
			}
		}
		return "Unknown";
	}
	
}
