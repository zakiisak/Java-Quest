package game.entity;

import game.graphics.Sprite;

public class Creatures {
	public static Sprite[] creatureSprites;
	public static Sprite[] creatureSprites_overlay;
	
	
	public static final int PLAYER = 6;
	
	public static void init()
	{
		creatureSprites = Sprite.getAtlasFromSprite(Sprite.getSprite("roguelikecreatures"), 8, 9, false);
		creatureSprites_overlay = Sprite.getAtlasFromSprite(Sprite.getSprite("roguelikecreatures_overlay"), 8, 9, false);
	}
	
	public static Sprite getCreatureSprite(int id)
	{
		return creatureSprites[id];
	}
	
	public static Sprite getCreatureSpriteOverlay(int id)
	{
		return creatureSprites_overlay[id];
	}
}
