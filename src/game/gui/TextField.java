package game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Input;
import game.graphics.DrawUtil;
import game.graphics.Sprite;
import game.utils.FontUtils;

public class TextField extends TextWriter {
	
	private int counter = 0;
	private boolean caretVisible = false;
	
	public TextField(float x, float y, float width, float height, BitmapFont font, String text)
	{
		setBounds(x, y, width, height);
		setFont(font);
		setText(text);
	}
	
	public TextField(float x, float y, float width, float height)
	{
		this(x, y, width, height, Gui.DefaultFont, "");
	}
	
	public TextField(float x, float y, BitmapFont font, String text)
	{
		this(x, y, FontUtils.getWidth(font, text), FontUtils.getHeight(font, text) * 1.5f, font, text);
	}
	
	public TextField(float x, float y, String text)
	{
		this(x, y, Gui.DefaultFont, text);
	}
	
	public TextField()
	{
		this(0, 0, "");
	}
	
	protected void update()
	{
		if(containsMouse())
		{
			if(Gdx.input.justTouched() && Gdx.input.isButtonPressed(Buttons.LEFT))
			{
				bind();
			}
		}
		else
		{
			if(Gdx.input.justTouched() && Gdx.input.isButtonPressed(Buttons.LEFT))
			{
				if(isBound()) unbind();
			}
		}
		
		if(isBound())
		{
			if(counter < 30)
				counter++;
			else
			{
				counter = 0;
				caretVisible = !caretVisible;
			}
		}
	}
	
	public void bind()
	{
		Input.bindTextWriter(this);
		caretVisible = true;
		counter = 0;
	}
	
	public void unbind()
	{
		Input.bindTextWriter(null);
		counter = 0;
		caretVisible = false;
	}
	
	public boolean isBound()
	{
		return Input.getBoundTextWriter() == this;
	}
	
	public void render(SpriteBatch batch) {
		update();
		batch.setColor(1, 1, 1, 1);
		TiledRenderer.drawTiledBox(batch, Sprite.textbox, x, y, width, height, 1);
		font.draw(batch, text, x, y + height / 2 - FontUtils.getHeight(font, text) / 2);
		batch.setColor(0, 0, 0, 1);
		if(caretVisible) DrawUtil.drawFilledRect(batch, x + FontUtils.getWidth(font, text) - 1, y, 1, font.getLineHeight());
	}

	public void input(char character) {
		System.out.println("character input: " + character + " (int) character: " + (int) character);
		int lengthBefore = text.length();
		if(character >= 32)
		{
			text += character;
		}
		else if(character == 8)
		{
			if(text.length() > 0) text = text.substring(0, text.length() - 1);
		}
		
		if(text.length() != lengthBefore)
		{
			caretVisible = true;
			counter = 0;
		}
	}
	
	
	
}
