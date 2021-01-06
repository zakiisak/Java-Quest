package game.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.graphics.Sprite;

public class RadioButton extends InteractableComponent {
	
	protected String tooltip = "";
	
	
	public RadioButton(boolean marked)
	{
		this(0, 0, marked, "");
	}
	
	public RadioButton(String tooltip, boolean marked)
	{
		this(0, 0, marked, tooltip);
	}
	
	public RadioButton(String tooltip)
	{
		this(0, 0, false, tooltip);
	}
	
	public RadioButton()
	{
		this(false);
	}
	
	public RadioButton(float x, float y, boolean marked, String tooltip)
	{
		setBounds(x, y, 16, 16);
		setMarked(marked);
		setTooltip(tooltip);
		transitionSprite = Sprite.checkbox_default;
	}
	
	public RadioButton(float x, float y)
	{
		this(x, y, false, "");
	}
	
	public String getTooltip()
	{
		return tooltip;
	}
	
	public RadioButton setTooltip(String tooltip)
	{
		this.tooltip = tooltip;
		return this;
	}
	
	@Override
	protected void onPressed() {
		if(!marked)
			marked = true;
	}

	@Override
	public Sprite getDefaultSprite() {
		return Sprite.radiobutton_default;
	}

	@Override
	public Sprite getHoverSprite() {
		return Sprite.radiobutton_hovered;
	}

	@Override
	public Sprite getMarkedDefaultSprite() {
		return Sprite.radiobutton_default_marked;
	}

	@Override
	public Sprite getMarkedHoverSprite() {
		return Sprite.radiobutton_hovered_marked;
	}
	
}
