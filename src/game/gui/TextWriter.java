package game.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class TextWriter extends Component {

	protected String text;
	protected BitmapFont font;
	
	public abstract void render(SpriteBatch batch);
	public abstract void input(char character);
	
	public String getText()
	{
		return text;
	}
	
	public BitmapFont getFont()
	{
		return font;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public void setFont(BitmapFont font)
	{
		this.font = font;
	}
}
