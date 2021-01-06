package game.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FontBatch {
	
	private static Map<BitmapFont, List<Text>> cache = new HashMap<BitmapFont, List<Text>>();
	
	public static void addText(BitmapFont font, Text text)
	{
		List<Text> batch = cache.get(font);
		if(batch == null)
		{
			batch = new ArrayList<Text>();
			cache.put(font, batch);
		}
		batch.add(text);
	}
	
	public static void addText(BitmapFont font, String text, float x, float y)
	{
		addText(font, text, x, y, font.getColor());
	}
	
	public static void addText(BitmapFont font, String text, float x, float y, final Color color)
	{
		Text t = new Text();
		t.text = text;
		t.x = x;
		t.y = y;
		t.color = color;
		addText(font, t);
	}
	
	private static void drawBatch(SpriteBatch batch, BitmapFont font, List<Text> texts)
	{
		for(Text text : texts)
		{
			font.setColor(text.color);
			font.draw(batch, text.text, text.x, text.y);
		}
	}
	
	public static void flush(SpriteBatch spriteBatch, BitmapFont font)
	{
		List<Text> batch = cache.get(font);
		drawBatch(spriteBatch, font, batch);
		batch.clear();
	}
	
	public static void flushAll(SpriteBatch spriteBatch)
	{
		for(BitmapFont font : cache.keySet())
		{
			flush(spriteBatch, font);
		}
	}
	
	public static void clear(BitmapFont font)
	{
		cache.get(font).clear();
	}
	
	public static void removeEntry(BitmapFont font)
	{
		cache.remove(font);
	}
	
	public static void clearAll()
	{
		for(BitmapFont font : cache.keySet())
		{
			clear(font);
		}
	}
	
	public static void removeAllEntries()
	{
		for(BitmapFont font : cache.keySet())
		{
			cache.remove(font);
		}
	}
	
	public static List<Text> getEntry(BitmapFont font)
	{
		return cache.get(font);
	}

	
	public static boolean containsBatch(BitmapFont font)
	{
		return getEntry(font) != null;
	}
	
	public static class Text
	{
		public Color color = Color.WHITE;
		public String text;
		public float x;
		public float y;
	}
	
}
