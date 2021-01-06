package game.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.Entity;
import game.graphics.Sprite;

public class Tile {
	public static final int SIZE = 32;
	
	public short id;
	public Sprite sprite;
	public boolean passableBackground = true, passableForeground = true;
	
	public static Tile[] tiles = new Tile[2000];
	
	public static TileSet[] tilesets = new TileSet[11];
	
	public static void load()
	{
		Sprite[] overworld5 = Sprite.getAtlasFromSprite(Sprite.getSprite("overworld_5"), 9, 9);
		Sprite[] overworld6 = Sprite.getAtlasFromSprite(Sprite.getSprite("overworld_6"), 9, 9);
		Sprite[] overworld7 = Sprite.getAtlasFromSprite(Sprite.getSprite("overworld_7"), 4, 4);
		
		Sprite[] cave10 = Sprite.getAtlasFromSprite(Sprite.getSprite("cave_10"), 9, 9);
		Sprite[] cave11 = Sprite.getAtlasFromSprite(Sprite.getSprite("cave_11"), 9, 9);
		Sprite[] cave12 = Sprite.getAtlasFromSprite(Sprite.getSprite("cave_12"), 7, 7);
		Sprite[] cave13 = Sprite.getAtlasFromSprite(Sprite.getSprite("cave_13"), 9, 9);
		Sprite[] cave14 = Sprite.getAtlasFromSprite(Sprite.getSprite("cave_14"), 9, 9);
		Sprite[] cave15 = Sprite.getAtlasFromSprite(Sprite.getSprite("cave_15"), 9, 9);
		Sprite[] cave16 = Sprite.getAtlasFromSprite(Sprite.getSprite("cave_16"), 9, 9);
		Sprite[] cave17 = Sprite.getAtlasFromSprite(Sprite.getSprite("cave_17"), 9, 9);
		
		for(int i = 0; i < tilesets.length; i++)
		{
			tilesets[i] = new TileSet();
		}
		tilesets[0].set(overworld5, 9, 9);
		tilesets[1].set(overworld6, 9, 9);
		tilesets[2].set(overworld7, 4, 4);
		tilesets[3].set(cave10, 9, 9);
		tilesets[4].set(cave11, 9, 9);
		tilesets[5].set(cave12, 7, 7);
		tilesets[6].set(cave13, 9, 9);
		tilesets[7].set(cave14, 9, 9);
		tilesets[8].set(cave15, 9, 9);
		tilesets[9].set(cave16, 9, 9);
		tilesets[10].set(cave17, 9, 9);
		
		int addition = 1; //Account for the air tile with id 0. Start from 1
		Tile air = tiles[0] = new Tile();
		air.id = 0;
		air.sprite = null;
		/*
		for(int y = 0; y < 9; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				int i = x + (8 - y) * 9;
				Tile t = tiles[addition + i] = new Tile();
				t.id = (short) (addition + i);
				t.sprite = overworld5[i];
			}
		}
		addition += overworld5.length;
		
		for(int y = 0; y < 9; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				int i = x + (8 - y) * 9;
				Tile t = tiles[addition + i] = new Tile();
				t.id = (short) (addition + i);
				t.sprite = overworld6[i];
			}
		}
		addition += overworld6.length;
		
		for(int y = 0; y < 4; y++)
		{
			for(int x = 0; x < 4; x++)
			{
				int i = x + (3 - y) * 4;
				Tile t = tiles[addition + i] = new Tile();
				t.id = (short) (addition + i);
				t.sprite = overworld7[i];
			}
		}
		addition += overworld7.length;
		*/
		for(int i = 0; i < tilesets.length; i++)
			addition = addTiles(tilesets[i], addition);
		/*
		addition = addTiles(tilesets[3], addition);
		addition = addTiles(tilesets[4], addition);
		addition = addTiles(tilesets[5], addition);
		addition = addTiles(tilesets[6], addition);
		addition = addTiles(tilesets[7], addition);
		addition = addTiles(tilesets[8], addition);
		addition = addTiles(tilesets[9], addition);
		addition = addTiles(tilesets[10], addition);
		*/
		

		
		addSolids();
	}
	
	public static short getCaveID(short tileId, byte spr)
	{
		int counter = tileId - 10;
		short id = 178;
		for(int i = 0; i < counter; i++)
		{
			TileSet set = tilesets[3 + i];
			id += set.width * set.height;
		}
		id += spr + 1;
		return id;
	}
	
	private static int addTiles(TileSet set, int addition)
	{
		for(int x = 0; x < set.width; x++)
		{
			for(int y = 0; y < set.height; y++)
			{
				int i = x + (set.width - 1 - y) * set.width;
				Tile t = tiles[addition + i] = new Tile();
				t.id = (short) (addition + i);
				t.sprite = set.set[i];
			}
		}
		return addition + set.width * set.height;
	}
	
	private static void addSolids()
	{
		for(int i = 1; i <= 8; i++)
			addSolid(i, false, false);
		
		addSolid(4, false, false);
		
		
		addSolid(10, false, false);
		addSolid(12, false, false);
		addSolid(13, false, false);
		addSolid(14, false, false);
		addSolid(15, false, false);
		addSolid(17, false, false);
		addSolid(19, false, false);
		addSolid(20, false, false);
		addSolid(21, false, false);
		addSolid(24, false, false);
		addSolid(25, false, false);
		addSolid(26, false, false);
		
		addSolid(44, false, false);
		addSolid(45, false, false);
		addSolid(51, false, false);
		addSolid(52, false, false);
		addSolid(54, false, false);
		addSolid(55, false, false);
		addSolid(56, false, false);
		addSolid(57, false, false);
		addSolid(58, false, false);
		addSolid(59, false, false);
		addSolid(60, false, false);
		addSolid(61, false, false);
		addSolid(64, false, false);
		addSolid(66, false, false);
		addSolid(67, false, false);
		addSolid(68, false, false);
		addSolid(69, false, false);
		addSolid(70, false, false);
		addSolid(78, false, false);
		addSolid(79, false, false);
		addSolid(80, false, false);
		addSolid(81, false, false);
		
		
		for(int i = 73; i <= 81; i++)
			addSolid(i, false, false);
		
		
		//Sheet 2
		int addition = 81;
		addSolid(addition + 21, false, false);
		addSolid(addition + 22, false, false);
		addSolid(addition + 24, false, false);
		addSolid(addition + 25, false, false);
		addSolid(addition + 26, false, false);
		addSolid(addition + 30, false, false);
		addSolid(addition + 31, false, false);
		addSolid(addition + 33, false, false);
		addSolid(addition + 34, false, false);
		addSolid(addition + 35, false, false);
		addSolid(addition + 37, false, false);
		addSolid(addition + 39, false, false);
		addSolid(addition + 40, false, false);
		addSolid(addition + 46, false, false);
		addSolid(addition + 48, false, false);
		addSolid(addition + 49, false, false);
		addSolid(addition + 57, false, false);
		addSolid(addition + 58, false, false);
		addSolid(addition + 60, false, false);
		addSolid(addition + 64, false, false);
		addSolid(addition + 65, false, false);
		addSolid(addition + 81, false, false);
		
		
	}
	
	
	public static void addSolid(int id, boolean background, boolean foreground)
	{
		//First set
		if(id < 81 && id > 0)
		{
			int x = id % 9;
			int y = id / 9;
			id = x + (8 - y) * 9;
		}
		if(id < 162 && id >= 81)
		{
			id -= 81;
			int x = id % 9;
			int y = id / 9;
			id = x + (8 - y) * 9;
		}
		tiles[id].passableBackground = background;
		tiles[id].passableForeground = foreground;
	}
	
	public void onStepped(Entity entity, int tileX, int tileY)
	{
		
	}
	
	public void draw(SpriteBatch batch, int tx, int ty)
	{
		if(sprite != null)
		{
			sprite.render(batch, tx * 32, ty * 32, 32, 32);
		}
	}
	
	public boolean isPassable(boolean foreground)
	{
		return foreground ? passableForeground : passableBackground;
	}
	
	
	public static class TileSet
	{
		public Sprite[] set;
		public int width, height;
		
		public void set(Sprite[] set, int width, int height)
		{
			this.set = set;
			this.width = width;
			this.height = height;
		}
	}
	
}