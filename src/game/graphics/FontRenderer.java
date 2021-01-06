package game.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.graphics.Font.BitmapGlyph;
import game.utils.Color;
import game.utils.MathUtils;

public class FontRenderer {
	
	
	public static void draw(SpriteBatch batch, Font font, String text, float x, float y, float scale)
	{
		draw(batch, font, text, x, y, scale, false);
	}
	
	public static void draw(SpriteBatch batch, Font font, String text, float x, float y, float scale, boolean disableColors)
	{
		float temp = batch.getPackedColor();
		int intBits = Float.floatToRawIntBits(temp);
		float ir = (intBits & 0xff) / 255f;
		float ig = ((intBits >>> 8) & 0xff) / 255f;
		float ib = ((intBits >>> 16) & 0xff) / 255f;
		float ia = ((intBits >>> 24) & 0xff) / 255f;
		
		final Sprite sprite = font.getSprite();
		final float baseU = sprite.u;
		final float baseV = sprite.v;
		final float uniform = font.uniform;
		float xadvance = 0;
		float yadvance = 0;
		for(int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			if(c == '\n')
			{
				xadvance = 0;
				yadvance += font.lineHeight * scale;
				continue;
			}
			//color
			//4 bits for each channel
			else if(c == '#')
			{
				if(disableColors == false)
				{
					String substr = text.substring(i + 1, i + 1 + 4);
					float r = 1;
					float g = 1;
					float b = 1;
					float a = 1;
					if(substr.charAt(0) == 'z')
						r = ir;
					else r = (float) Integer.decode("0x" + substr.charAt(0)) / 15.0f;
					if(substr.charAt(1) == 'z')
						g = ig;
					else g = (float) Integer.decode("0x" + substr.charAt(1)) / 15.0f;
					if(substr.charAt(2) == 'z')
						b = ib;
					else b = (float) Integer.decode("0x" + substr.charAt(2)) / 15.0f;
					if(substr.charAt(3) == 'z')
						a = ia;
					else a = (float) Integer.decode("0x" + substr.charAt(3)) / 15.0f;
					//System.out.println("r: " + r + ", g: " + g + ", b: " + b + ", a: " + a);
					batch.setColor(r, g, b, a);
				}
				i += 4;
				continue;
			}
			BitmapGlyph glyph = font.getGlyph(c);
			//if(glyph == null) continue;
			float u = glyph.x * uniform;
			float v = (glyph.y - 1) * uniform;
			float u2 = (glyph.width + 1) * uniform;
			float v2 = (glyph.height + 1) * uniform;
			sprite.render(batch, x + xadvance + glyph.xoffset * scale - (c == 'v' ? 1 : 0), y + glyph.yoffset * scale + yadvance - (c == 'v' ? 1 : 0), glyph.width * scale + scale, glyph.height * scale + scale, u, v, u2, v2);
			xadvance += glyph.xadvance * scale;
		}
		batch.setColor(ir, ig, ib, ia);
//		batch.setColor(1, 1, 1, 1);
	}
	
	public static void draw(SpriteBatch batch, Font font, String text, float x, float y)
	{
		draw(batch, font, text, x, y, 1, false);
	}
	
	public static void drawWithOutline(SpriteBatch batch, Font font, String text, float x, float y, float scale, int outlineWidth, Color outlineColor)
	{
		com.badlogic.gdx.graphics.Color c = batch.getColor();
		batch.setColor(outlineColor.r, outlineColor.g, outlineColor.b, outlineColor.a);
		draw(batch, font, text, x - outlineWidth, y, scale, true);
		draw(batch, font, text, x + outlineWidth, y, scale, true);
		draw(batch, font, text, x, y - outlineWidth, scale, true);
		draw(batch, font, text, x, y + outlineWidth, scale, true);
		
		
		{
			draw(batch, font, text, x - outlineWidth, y - outlineWidth, scale, true);
			draw(batch, font, text, x + outlineWidth, y - outlineWidth, scale, true);
			draw(batch, font, text, x + outlineWidth, y + outlineWidth, scale, true);
			draw(batch, font, text, x - outlineWidth, y + outlineWidth, scale, true);
		}
		
		batch.setColor(c);
		draw(batch, font, text, x, y, scale, false);
	}
	
}
