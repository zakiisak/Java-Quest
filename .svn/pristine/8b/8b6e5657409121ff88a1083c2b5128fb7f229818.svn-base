package game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import game.graphics.FontBatch;
import game.graphics.FontBatch.Text;

public class FontUtils {
	public static final int ALLIGN_LEFT = 0, ALLIGN_MIDDLE = 1, ALLIGN_RIGHT = 2, ALLIGN_MIDDLE_MIDDLE = 3, ALLIGN_RIGHT_ALTERNATIVE = 4;
	private static GlyphLayout layout = new GlyphLayout();
	
	public static float getWidth(BitmapFont font, String text)
	{
		layout.setText(font, text);
		return layout.width;
	}
	
	public static float getHeight(BitmapFont font, String text)
	{
		layout.setText(font, text);
		return layout.height;
	}
	
	public static GlyphLayout getGlyphLayout(BitmapFont font, String text)
	{
		layout.setText(font, text);
		return layout;
	}
	
	public static void draw(BitmapFont font, Batch batch, String text, float x, float y, int allign) {
		switch (allign) {
			case ALLIGN_LEFT:
				font.draw(batch, text, x, y);
				break;
			case ALLIGN_MIDDLE: {
				float width = getWidth(font, text);
				font.draw(batch, text, x - width / 2, y);
				break;
			}
			case ALLIGN_MIDDLE_MIDDLE:
			{
				float width = getWidth(font, text);
				float height = getHeight(font, text);
				font.draw(batch, text, x - width / 2, y - height / 2);
				break;
			}
			case ALLIGN_RIGHT: {
				float width = getWidth(font, text);
				font.draw(batch, text, x - width, y);
				break;
			}
			default: {
				font.draw(batch, text, x, y);
				break;
			}
		}
	}

	public static Text getText(BitmapFont font, String text, float x, float y, Color color, int allign) {
		Text t = new Text();
		t.text = text;

		switch (allign) {
			case ALLIGN_LEFT:
			{
				t.x = x;
				t.y = y;
				break;
			}
			case ALLIGN_MIDDLE:
			{
				float width = getWidth(font, text);
				t.x = x - width / 2;
				t.y = y;
				break;
			}
			case ALLIGN_MIDDLE_MIDDLE:
			{
				float width = getWidth(font, text);
				float height = getHeight(font, text);
				t.x = x - width / 2;
				t.y = y - height / 2;
				break;
			}
			case ALLIGN_RIGHT:
			{
				float width = getWidth(font, text);
				t.x = x - width;
				t.y = y;
				break;
			}
			default:
			{
				t.x = x;
				t.y = y;
				break;
			}
		}
		
		t.color = color;
		return t;
	}
	
	public static Text getText(BitmapFont font, String text, float x, float y, int allign)
	{
		return getText(font, text, x, y, font.getColor(), allign);
	}
	
	public static void addText(BitmapFont font, String text, float x, float y, Color color, int allign)
	{
		FontBatch.addText(font, getText(font, text, x, y, color, allign));
	}
	
	public static void addText(BitmapFont font, String text, float x, float y, int allign)
	{
		FontBatch.addText(font, getText(font, text, x, y, allign));
	}
}
