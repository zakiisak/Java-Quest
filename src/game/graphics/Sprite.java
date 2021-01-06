package game.graphics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.FileLoader;
import game.utils.Color;

public class Sprite implements Serializable {
	
	private static Map<Integer, int[]> sheetSizes = new HashMap<Integer, int[]>();
	private static Map<Integer, Texture> textures = new HashMap<Integer, Texture>();
	
	public static Sprite white;
	
	public static Sprite button_default;
	public static Sprite button_hovered;
	public static Sprite button_pressed;
	public static Sprite textbox;
	public static Sprite checkbox_default;
	public static Sprite checkbox_default_marked;
	public static Sprite checkbox_hovered;
	public static Sprite checkbox_hovered_marked;
	public static Sprite radiobutton_default;
	public static Sprite radiobutton_default_marked;
	public static Sprite radiobutton_hovered;
	public static Sprite radiobutton_hovered_marked;
	public static Sprite document;
	public static Sprite scrollbar_back;
	public static Sprite scrollbar_default;
	public static Sprite scrollbar_hovered;
	
	public static Sprite scrollbar_button_back;
	public static Sprite scrollbar_button_default;
	public static Sprite scrollbar_button_hovered;
	
	public static void load()
	{
		SheetPacker.addImage("res/ui/white.png");
		SheetPacker.addImage("res/ui/button_default.png");
		SheetPacker.addImage("res/ui/button_hovered.png");
		SheetPacker.addImage("res/ui/button_pressed.png");
		SheetPacker.addImage("res/ui/textbox.png");
		SheetPacker.addImage("res/ui/checkbox_default.png");
		SheetPacker.addImage("res/ui/checkbox_default_marked.png");
		SheetPacker.addImage("res/ui/checkbox_hovered.png");
		SheetPacker.addImage("res/ui/checkbox_hovered_marked.png");
		SheetPacker.addImage("res/ui/radiobutton_default.png");
		SheetPacker.addImage("res/ui/radiobutton_default_marked.png");
		SheetPacker.addImage("res/ui/radiobutton_hovered.png");
		SheetPacker.addImage("res/ui/radiobutton_hovered_marked.png");
		SheetPacker.addImage("res/ui/document.png");
		SheetPacker.addImage("res/ui/scrollbar_back.png");
		SheetPacker.addImage("res/ui/scrollbar_default.png");
		SheetPacker.addImage("res/ui/scrollbar_hovered.png");
		SheetPacker.addImage("res/ui/scrollbar_button_back.png");
		SheetPacker.addImage("res/ui/scrollbar_button_default.png");
		SheetPacker.addImage("res/ui/scrollbar_button_hovered.png");
		SheetPacker.addImage("res/roguelikecreatures.png").addWhiteOverlay = true;
		SheetPacker.addImage("res/roguelikeitems.png").addWhiteOverlay = true;
		SheetPacker.addImage("res/overworld_5.png");
		SheetPacker.addImage("res/overworld_6.png");
		SheetPacker.addImage("res/overworld_7.png");
		SheetPacker.addImage("res/statborder.png");
		SheetPacker.addImage("res/cave_10.png");
		SheetPacker.addImage("res/cave_11.png");
		SheetPacker.addImage("res/cave_12.png");
		SheetPacker.addImage("res/cave_13.png");
		SheetPacker.addImage("res/cave_14.png");
		SheetPacker.addImage("res/cave_15.png");
		SheetPacker.addImage("res/cave_16.png");
		SheetPacker.addImage("res/cave_17.png");
		SheetPacker.addImage("res/overworld.png");
		SheetPacker.addImage("res/dark_world.png");
		SheetPacker.addImage("res/tppoint.png");
		SheetPacker.addImage("res/tppoint2.png");
		SheetPacker.addImage("res/battle_indicator.png");
		SheetPacker.addImage("res/dead_player.png");
		SheetPacker.addImage("res/dead_player_gray.png");
		
		SheetPacker.addImage("res/Untitled.png");
		SheetPacker.addImage("res/Warrier 2.png").addWhiteOverlay = true;
		SheetPacker.addImage("res/battle_back.png");
		SheetPacker.addImage("res/cave_back.png");
		SheetPacker.addImage("res/arrow.png");
		SheetPacker.addImage("res/hori_arrow.png");
		SheetPacker.addImage("res/healthbar.png");
		SheetPacker.addImage("res/plus.png");
		SheetPacker.addImage("res/crafting_arrow.png");
		SheetPacker.addImage("res/escape_identifier.png");
		SheetPacker.addImage("res/jq_flame.png");
		SheetPacker.addImage("res/jq_flame_2.png");
		SheetPacker.addImage("res/jq_text.png");
		SheetPacker.addImage("res/title_particle.png");
		SheetPacker.addImage("res/title_particle_2.png");
		
		FileHandle[] handles = FileLoader.list("res/enemy_sprites/");
		
		for(FileHandle handle : handles)
		{
			SheetPacker.addImage(handle).addWhiteOverlay = true;
		}
	}
	
	public static void setSpriteReferences()
	{
		white = getSprite("white");
		button_default = getSprite("button_default");
		button_hovered = getSprite("button_hovered");
		button_pressed = getSprite("button_pressed");
		textbox = getSprite("textbox");
		checkbox_default = getSprite("checkbox_default");
		checkbox_default_marked = getSprite("checkbox_default_marked");
		checkbox_hovered = getSprite("checkbox_hovered");
		checkbox_hovered_marked = getSprite("checkbox_hovered_marked");
		radiobutton_default = getSprite("radiobutton_default");
		radiobutton_default_marked = getSprite("radiobutton_default_marked");
		radiobutton_hovered = getSprite("radiobutton_hovered");
		radiobutton_hovered_marked = getSprite("radiobutton_hovered_marked");
		document = getSprite("document");
		scrollbar_back = getSprite("scrollbar_back");
		scrollbar_default = getSprite("scrollbar_default");
		scrollbar_hovered = getSprite("scrollbar_hovered");
		scrollbar_button_back = getSprite("scrollbar_button_back");
		scrollbar_button_default = getSprite("scrollbar_button_default");
		scrollbar_button_hovered = getSprite("scrollbar_button_hovered");
		System.out.println("Button default: " + button_default);
	}
	
	public static void registerTextureSize(int texture, int width, int height)
	{
		sheetSizes.put(texture, new int[] {width, height});
	}
	
	public static void registerTexture(int textureNumber, Texture texture)
	{
		textures.put(textureNumber, texture);
	}
	
	public boolean equals(Sprite sprite)
	{
		if(sprite == null) return false;
		final Texture texture = getTexture();
		return u == sprite.u && v == sprite.v && u2 == sprite.u2 && v2 == sprite.v2 
				&& texture.getTextureObjectHandle() == sprite.getTexture().getTextureObjectHandle();
	}
	
	public final int textureNumber;
	public final float u, v, u2, v2;
	public String nameId;
	
	public Sprite(int textureNumber, float u, float v, float u2, float v2)
	{
		this.textureNumber = textureNumber;
		this.u = u;
		this.v = v;
		this.u2 = u2;
		this.v2 = v2;
	}
	
	public Sprite(Sprite sprite)
	{
		this.textureNumber = sprite.textureNumber;
		this.u = sprite.u;
		this.v = sprite.v;
		this.u2 = sprite.u2;
		this.v2 = sprite.v2;
	}
	
	public float getWidth()
	{
		return (u2 - u) * sheetSizes.get(getTexture().getTextureObjectHandle())[0];
	}
	
	public float getHeight()
	{
		return (v2 - v) * sheetSizes.get(getTexture().getTextureObjectHandle())[1];
	}
	
	public float getUniform()
	{
		return 1.0f / (float) sheetSizes.get(getTexture().getTextureObjectHandle())[0];
	}
	
	public Texture getTexture()
	{
		return textures.get(textureNumber);
	}
	
	public Sprite cpy()
	{
		return new Sprite(this);
	}
	
	public void render(SpriteBatch batch, float x, float y, float width, float height)
	{
		batch.draw(getTexture(), x, y, width, height, u, v, u2, v2);
	}
	
	public void render(SpriteBatch batch, float x, float y, float width, float height, float rotation)
	{
		final Texture texture = getTexture();
		int srcX = (int) (u * texture.getWidth());
		int srcY = (int) (v * texture.getHeight()) - 1;
		int srcWidth = (int) ((u2 - u) * texture.getWidth());
		int srcHeight = (int) ((v2 - v) * texture.getHeight());
		batch.draw(getTexture(), x, y, width / 2, height / 2, width, height, 1, 1, rotation, srcX, srcY, srcWidth, srcHeight, false, false);
	}
	
	public void renderWithOutline(SpriteBatch batch, float x, float y, float width, float height, float borderWidth, Color outlineColor)
	{
		com.badlogic.gdx.graphics.Color c = batch.getColor();
		batch.setColor(outlineColor.r, outlineColor.g, outlineColor.b, outlineColor.a);
		render(batch, x - borderWidth, y, width, height);
		render(batch, x + borderWidth, y, width, height);
		render(batch, x, y - borderWidth, width, height);
		render(batch, x, y + borderWidth, width, height);
		
		//Extra expensive, but extra good
		render(batch, x - borderWidth, y - borderWidth, width, height);
		render(batch, x + borderWidth, y - borderWidth, width, height);
		render(batch, x + borderWidth, y + borderWidth, width, height);
		render(batch, x - borderWidth, y + borderWidth, width, height);
		
		batch.setColor(c);
		render(batch, x, y, width, height);
	}
	
	/** The uv add parameters are in texture space rather than sprite space.
	 */
	public void render(SpriteBatch batch, float x, float y, float width, float height, float uOffset, float vOffset, float u2Width, float v2Height)
	{
		batch.draw(getTexture(), x, y, width, height, u + uOffset, v + vOffset, u + uOffset + u2Width, v + vOffset + v2Height);
	}
	
	public static Sprite getSprite(String name)
	{
		String lower = name.toLowerCase();
		for(int i = 0; i < SheetPacker.sprites.size(); i++)
		{
			Sprite sprite = SheetPacker.sprites.get(i).get(lower);
			if(sprite != null)
				return sprite;
		}
		return null;
	}
	
	public static Sprite getSprite(String name, int sheetTexture)
	{
		int index = 0;
		for(int i = 0; i < SheetPacker.sprites.size(); i++)
		{
			if(SheetPacker.sprites.get(i).size() < 1)
				continue;
			if(SheetPacker.sprites.get(i).get(0).getTexture().getTextureObjectHandle() == sheetTexture)
			{
				index = i;
				break;
			}
		}
		
		return SheetPacker.sprites.get(index).get(name);
	}
	
	public static Sprite[] getAtlasFromSprite(Sprite atlas, int gridWidth, int gridHeight, boolean flipY)
	{
		Sprite[] sprites = new Sprite[gridWidth * gridHeight];
		final float startU = atlas.u;
		final float startV = atlas.v;
		final float uWidth = atlas.u2 - atlas.u;
		final float vHeight = atlas.v2 - atlas.v;
		
		final float xUniform = uWidth / (float) gridWidth;
		final float yUniform = vHeight / (float) gridHeight;
		
		for(int x = 0; x < gridWidth; x++)
		{
			for(int y = 0; y < gridHeight; y++)
			{
				final float u = startU + x * xUniform;
				final float v = startV + y * yUniform;
				Sprite sprite = new Sprite(atlas.textureNumber, u, v, u + xUniform, v + yUniform);
				sprites[x + (flipY ? (gridHeight - 1 - y) : y) * gridWidth] = sprite;
			}
		}
		return sprites;
	}
	
	public static Sprite[] getAtlasFromSprite(Sprite atlas, int gridWidth, int gridHeight)
	{
		return getAtlasFromSprite(atlas, gridWidth, gridHeight, true);
	}
	
	public static Sprite getSpritePart(Sprite atlas, int gridWidth, int gridHeight, int spriteCountPerLine, int index)
	{
		final float startU = atlas.u;
		final float startV = atlas.v;
		final float uWidth = atlas.u2 - atlas.u;
		final float vHeight = atlas.v2 - atlas.v;
		
		final float xUniform = uWidth / (float) gridWidth;
		final float yUniform = vHeight / (float) gridHeight;
		final float u = startU + (index % spriteCountPerLine) * xUniform;
		final float v = startV + (index / spriteCountPerLine) * yUniform;
		return new Sprite(atlas.textureNumber, u, v, u + xUniform, v + yUniform);
	}
	
}
