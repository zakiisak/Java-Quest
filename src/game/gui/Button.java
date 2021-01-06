package game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.graphics.Sprite;
import game.utils.FontUtils;

public class Button extends InteractableComponent {
	
	protected String text;
	protected Color textColor = Color.BLACK;
	protected BitmapFont font;
	
	public Button(float x, float y, float width, float height, String text, BitmapFont font)
	{
		setBounds(x, y, width, height);
		setText(text);
		setFont(font);
		transitionSprite = Sprite.button_default;
	}
	
	public Button(float x, float y, float width, float height, String text)
	{
		this(x, y, width, height, text, Gui.DefaultFont);
	}
	
	public Button(float x, float y, float width, float height)
	{
		this(x, y, width, height, "", Gui.DefaultFont);
	}
	
	public Button(float x, float y, BitmapFont font, String text)
	{
		this(x, y, FontUtils.getWidth(font, text), FontUtils.getHeight(font, text) * 1.5f, text, font);
	}
	
	public Button(float x, float y, String text)
	{
		this(x, y, Gui.DefaultFont, text);
	}
	
	public Button()
	{
		this(0, 0, "");
	}
	
	@Override
	public void render(SpriteBatch batch) {
		update();
		boolean transition = transitionSprite != null;
		if(transition)
			transition = !transitionSprite.equals(sprite);
		if(transition)
		{
			float frontAlpha = (float) transitionFactor / 10.0f;
			batch.setColor(1, 1, 1, 1);
			TiledRenderer.drawTiledBox(batch, sprite, x, y, width, height, 1);
			batch.setColor(1, 1, 1, frontAlpha);
			TiledRenderer.drawTiledBox(batch, transitionSprite, x, y, width, height, 1);
		}
		else
		{	
			batch.setColor(1, 1, 1, 1);
			TiledRenderer.drawTiledBox(batch, sprite, x, y, width, height, 1);
		}
		FontUtils.addText(font, text, x + width / 2, y + height / 2, textColor, FontUtils.ALLIGN_MIDDLE_MIDDLE);
	}
	

	public Button setText(String text)
	{
		this.text = text;
		return this;
	}
	
	public Button setFont(BitmapFont font)
	{
		this.font = font;
		return this;
	}
	
	public String getText()
	{
		return text;
	}
	
	public BitmapFont getFont()
	{
		return font;
	}
	public Color getTextColor()
	{
		return textColor;
	}
	
	public Button setTextColor(Color textColor)
	{
		this.textColor = textColor;
		return this;
	}

	@Override
	public Sprite getDefaultSprite() {
		return Sprite.button_default;
	}

	@Override
	public Sprite getHoverSprite() {
		return Sprite.button_hovered;
	}

	@Override
	public Sprite getMarkedDefaultSprite() {
		return Sprite.button_default;
	}

	@Override
	public Sprite getMarkedHoverSprite() {
		return Sprite.button_hovered;
	}
	
}
