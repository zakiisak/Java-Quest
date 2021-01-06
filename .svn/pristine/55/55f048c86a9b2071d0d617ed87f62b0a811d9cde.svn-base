package game.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.graphics.Sprite;
import game.utils.FontUtils;

public class TiledRenderer {

	public static void drawTiledBox(SpriteBatch batch, Sprite sprite, float x, float y, float width, float height, int scale)
	{
		int size = 16 * scale;
		int border = 8 * scale;
		int corner = 4 * scale;
		
		sprite.draw(batch, x - corner, y - corner, corner, corner, 0, 0, corner, corner);
		sprite.draw(batch, x + width, y - corner, corner, corner, size - corner, 0, corner, corner);
		sprite.draw(batch, x + width, y + height, corner, corner, size - corner, size - corner, corner, corner);
		sprite.draw(batch, x - corner, y + height, corner, corner, 0, size - corner, corner, corner);
		
		//Top
		sprite.draw(batch, x, y - corner, width, corner, corner, 0, border, corner);
		
		//Right
		sprite.draw(batch, x + width, y, corner, height, size - corner, corner, corner, border);
		
		//Bottom
		sprite.draw(batch, x, y + height, width, corner, corner, size - corner, border, corner);
		
		//Left
		sprite.draw(batch, x - corner, y, corner, height, 0, corner, corner, border);
		
		sprite.draw(batch, x, y, width, height, corner, corner, border, border);
	}
	
	public static void drawTiledBorder(SpriteBatch batch, Sprite sprite, float x, float y, float width, float height, int scale)
	{
		int size = 16 * scale;
		int border = 8 * scale;
		int corner = 4 * scale;
		
		sprite.draw(batch, x - corner, y - corner, corner, corner, 0, 0, corner, corner);
		sprite.draw(batch, x + width, y - corner, corner, corner, size - corner, 0, corner, corner);
		sprite.draw(batch, x + width, y + height, corner, corner, size - corner, size - corner, corner, corner);
		sprite.draw(batch, x - corner, y + height, corner, corner, 0, size - corner, corner, corner);
		
		//Top
		sprite.draw(batch, x, y - corner, width, corner, corner, 0, border, corner);
		
		//Right
		sprite.draw(batch, x + width, y, corner, height, size - corner, corner, corner, border);
		
		//Bottom
		sprite.draw(batch, x, y + height, width, corner, corner, size - corner, border, corner);
		
		//Left
		sprite.draw(batch, x - corner, y, corner, height, 0, corner, corner, border);
	}
	
	public static void drawTiledBorderWithLabel(SpriteBatch batch, Sprite sprite, float x, float y, float width, float height, int scale, Label label)
	{
		int size = 16 * scale;
		int border = 8 * scale;
		int corner = 4 * scale;
		
		sprite.draw(batch, x - corner, y - corner, corner, corner, 0, 0, corner, corner);
		sprite.draw(batch, x + width, y - corner, corner, corner, size - corner, 0, corner, corner);
		sprite.draw(batch, x + width, y + height, corner, corner, size - corner, size - corner, corner, corner);
		sprite.draw(batch, x - corner, y + height, corner, corner, 0, size - corner, corner, corner);
		
		//Top
		sprite.draw(batch, x, y - corner, 4, corner, corner, 0, border, corner);
		label.render(batch, x + 5 + label.width, y - label.getHeight() * 0.75f, FontUtils.ALLIGN_RIGHT);
		sprite.draw(batch, x + 4 + label.width + 4, y - corner, width - 8 - label.width, corner, corner, 0, border, corner);
		
		//Right
		sprite.draw(batch, x + width, y, corner, height, size - corner, corner, corner, border);
		
		//Bottom
		sprite.draw(batch, x, y + height, width, corner, corner, size - corner, border, corner);
		
		//Left
		sprite.draw(batch, x - corner, y, corner, height, 0, corner, corner, border);
	}
	
	public static void drawStretched(SpriteBatch batch, Sprite sprite, float x, float y, float width, float height)
	{
		sprite.render(batch, x, y, width, height);
	}
	
}
