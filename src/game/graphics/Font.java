package game.graphics;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import game.FileLoader;


public class Font {
	
	private Sprite fontSprite;
	private String name;
	private Map<Integer, BitmapGlyph> glyphs = new HashMap<Integer, BitmapGlyph>();
	public final float uniform;
	public int lineHeight;
	public int atlasWidth, atlasHeight;
	
	/**
	 * 
	 * @param file: is the filename of the font without extensions.
	 * @param name: is the name of the font sprite.
	 */
	public Font(FileHandle handle, String name)
	{
		SheetPacker.ImageData data = SheetPacker.addImage(handle.path() + "_0.png", "font_" + name, new SheetPacker.SpriteReferencer() {
			@Override
			public void onInitialized(Sprite sprite) {
				fontSprite = sprite;
			}
		});
		atlasWidth = data.image.getWidth();
		atlasHeight = data.image.getHeight();
		uniform = 1.0f / (float) SheetPacker.SIZE;
		loadGlyphs((FileLoader.get(handle.path() + ".fnt")));
		this.name = name;
	}
	
	private void loadGlyphs(FileHandle handle)
	{
		long before = System.nanoTime();
		String[] lines = handle.readString().split("\n");
		for(int i = 0; i < lines.length; i++)
		{
			String line = lines[i];
			String[] splitter = line.split(" ");
			int charId = -1;
			BitmapGlyph glyph = new BitmapGlyph();
			for(String str : splitter)
			{
				if(str.startsWith("id"))
				{
					charId = Integer.parseInt(str.split("=")[1]);
					continue;
				}
				else if(str.startsWith("x="))
				{
					glyph.x = Integer.parseInt(str.split("=")[1]);
				}
				else if(str.startsWith("y="))
				{
					glyph.y = Integer.parseInt(str.split("=")[1]);
				}
				else if(str.startsWith("width"))
				{
					glyph.width = Integer.parseInt(str.split("=")[1]);
				}
				else if(str.startsWith("height"))
				{
					glyph.height = Integer.parseInt(str.split("=")[1]);
				}
				else if(str.startsWith("xoffset"))
				{
					glyph.xoffset = Integer.parseInt(str.split("=")[1]);
				}
				else if(str.startsWith("yoffset"))
				{
					glyph.yoffset = Integer.parseInt(str.split("=")[1]);
				}
				else if(str.startsWith("xadvance"))
				{
					glyph.xadvance = Integer.parseInt(str.split("=")[1]);
				}
				else if(str.startsWith("lineHeight"))
				{
					lineHeight = Integer.parseInt(str.split("=")[1]);
				}
				else if(str.startsWith("scaleW"))
				{
					atlasWidth = Integer.parseInt(str.split("=")[1]);
				}
				else if(str.startsWith("scaleH"))
				{
					atlasHeight = Integer.parseInt(str.split("=")[1]);
				}
			}
			glyphs.put(charId, glyph);
			
		}
	
		System.out.println("it took " + ((double) (System.nanoTime() - before) / 1000000.0) + " milliseconds to load font");
	}
	
	public Sprite getSprite()
	{
		return fontSprite;
	}
	
	public BitmapGlyph getGlyph(char character)
	{
		return glyphs.get((int) character);
	}
	
	public float getWidth(String text)
	{
		float width = 0;
		for(int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			BitmapGlyph glyph = getGlyph(c);
			if(glyph == null) continue;
			if(c == '#')
			{
				i += 4;
				continue;
			}
			else if(c == '�')
			{
				if(text.charAt(i + 1) == 'C')
				{
					int endIndex = i + 2 + 6;
					i = endIndex - 1;
				}
				else if(text.charAt(i + 1) == 'S')
				{
					int endIndex = text.indexOf('S', i + 2);
					i = endIndex;
					width += 28;
				}
				continue;
			}
			width += glyph.xadvance;
		}
		return width;
	}
	
	public float getHeight(String text)
	{
		float height = 0;
		for(int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			BitmapGlyph glyph = getGlyph(c);
			if(glyph == null) continue;
			else if(c == '�')
			{
				int endIndex = i + 1 + 6;
				i = endIndex - 1;
				continue;
			}
			if(glyph.height > height)
				height = glyph.height;
		}
		return height;
	}
	
	public String getName()
	{
		return name;
	}
	
	public static class BitmapGlyph
	{
		public int x;
		public int y;
		public int width;
		public int height;
		public int xoffset;
		public int yoffset;
		public int xadvance;
	}
	
	public static class Kerning
	{
		public int first;
		public int second;
	}
	
}
