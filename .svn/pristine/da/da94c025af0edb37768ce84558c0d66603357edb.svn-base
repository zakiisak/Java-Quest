package game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.graphics.FontBatch;
import game.utils.FontUtils;

public class Label extends Component {
	
	protected String text;
	protected BitmapFont font;
	protected Color color;
	protected int allignMode = FontUtils.ALLIGN_LEFT;
	
	public Label(float x, float y, String text, BitmapFont font, Color color, int allignMode)
	{
		setPosition(x, y);
		this.text = text;
		this.font = font;
		this.color = color;
		this.allignMode = allignMode;
		updateSize();
	}
	
	public Label(float x, float y, String text, Color color, int allignMode)
	{
		this(x, y, text, Gui.DefaultFont, color, allignMode);
	}
	
	public Label(float x, float y, String text, Color color)
	{
		this(x, y, text, Gui.DefaultFont, color, FontUtils.ALLIGN_LEFT);
	}
	
	public Label(float x, float y, String text)
	{
		this(x, y, text, Gui.DefaultFont, Color.WHITE, FontUtils.ALLIGN_LEFT);
	}
	
	public Label(float x, float y)
	{
		this(x, y, "", Gui.DefaultFont, Color.WHITE, FontUtils.ALLIGN_LEFT);
	}
	
	public Label(String text, BitmapFont font, Color color, int allignMode)
	{
		this(0, 0, text, font, color, allignMode);
	}
	
	public Label(String text, BitmapFont font, Color color)
	{
		this(text, font, color, FontUtils.ALLIGN_LEFT);
	}
	
	public Label(String text, BitmapFont font)
	{
		this(text, font, Color.WHITE);
	}
	
	public Label(String text)
	{
		this(text, Gui.DefaultFont);
	}
	
	public void render(SpriteBatch batch) {
		font.setColor(color);
		switch(allignMode)
		{
			case FontUtils.ALLIGN_LEFT:
			{
				font.draw(batch, text, x, y);
				break;
			}
			case FontUtils.ALLIGN_MIDDLE:
			{
				font.draw(batch, text, x - width / 2, y);
				break;
			}
			case FontUtils.ALLIGN_RIGHT:
			{
				font.draw(batch, text, x - width, y);
				break;
			}
		}
	}
	
	public void render(SpriteBatch batch, float x, float y, int allignMode)
	{
		font.setColor(color);
		switch(allignMode)
		{
			case FontUtils.ALLIGN_LEFT:
			{
				FontBatch.addText(font, text, x, y, color);
				break;
			}
			case FontUtils.ALLIGN_MIDDLE:
			{
				FontBatch.addText(font, text, x - width / 2, y, color);
				break;
			}
			case FontUtils.ALLIGN_RIGHT:
			{
				FontBatch.addText(font, text, x - width, y, color);
				break;
			}
		}
	}
	
	public String getText()
	{
		return text;
	}
	
	public BitmapFont getFont()
	{
		return font;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public int getAllignMode()
	{
		return allignMode;
	}
	
	public Label setText(String text)
	{
		this.text = text;
		updateSize();
		return this;
	}
	
	public Label setFont(BitmapFont font)
	{
		this.font = font;
		updateSize();
		return this;
	}
	
	public Label setColor(Color color)
	{
		this.color = color;
		return this;
	}
	
	public Label setAllignMode(int allignMode)
	{
		this.allignMode = allignMode;
		return this;
	}
	
	private void updateSize()
	{
		setSize(FontUtils.getWidth(font, text), FontUtils.getHeight(font, text));
	}
	
}
