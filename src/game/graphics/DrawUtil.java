package game.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DrawUtil {
	
	public static void drawFilledRect(SpriteBatch batch, float x, float y, float width, float height)
	{
		Sprite.white.render(batch, x, y, width, height);
	}
	
}
