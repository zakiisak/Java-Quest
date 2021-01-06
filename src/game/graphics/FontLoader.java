package game.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontLoader {
	private static final boolean FlipY = true;
	
	public static BitmapFont loadFont(FileHandle handle, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(handle);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.flip = FlipY;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}
	
	public static BitmapFont loadFontWithShadow(FileHandle handle, int size, Color shadowColor, int shadowOffsetX, int shadowOffsetY)
	{
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.shadowColor = shadowColor;
		parameter.shadowOffsetX = shadowOffsetX;
		parameter.shadowOffsetY = shadowOffsetY;
		parameter.flip = FlipY;
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		return loadFont(handle, parameter);
	}
	
	public static BitmapFont loadFontWithOutline(FileHandle handle, int size, Color outlineColor, int thickness, boolean mellow)
	{
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.borderColor = outlineColor;
		parameter.borderWidth = thickness;
		parameter.borderStraight = !mellow;
		parameter.flip = FlipY;
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		return loadFont(handle, parameter);
	}
	
	public static BitmapFont loadFontWithOutlineAndShadow(FileHandle handle, int size, Color fontColor, Color outlineColor, int thickness, boolean mellow, Color shadowColor, int shadowOffsetX, int shadowOffsetY)
	{
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.borderColor = outlineColor;
		parameter.borderWidth = thickness;
		parameter.borderStraight = !mellow;
		parameter.color = fontColor;
		parameter.shadowColor = shadowColor;
		parameter.shadowOffsetX = shadowOffsetX;
		parameter.shadowOffsetY = shadowOffsetY;
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		parameter.flip = FlipY;
		return loadFont(handle, parameter);
	}
	
	public static BitmapFont loadFontWithOutline(FileHandle handle, int size, Color fontColor, Color outlineColor, int thickness, boolean mellow)
	{
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.borderColor = outlineColor;
		parameter.borderWidth = thickness;
		parameter.borderStraight = !mellow;
		parameter.color = fontColor;
		parameter.flip = FlipY;
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		return loadFont(handle, parameter);
	}
	
	public static BitmapFont loadFont(FileHandle handle, FreeTypeFontParameter parameter) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(handle);
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}
	
}
