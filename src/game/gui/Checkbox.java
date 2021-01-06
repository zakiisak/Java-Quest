package game.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.graphics.Sprite;

public class Checkbox extends InteractableComponent {
	
	
	public Checkbox()
	{
		this.marked = false;
	}
	
	public Checkbox(float x, float y, boolean marked)
	{
		setBounds(x, y, 16, 16);
		setMarked(marked);
	}
	
	public Checkbox(float x, float y)
	{
		this(x, y, false);
	}
	
	@Override
	protected void onPressed() {
		marked = !marked;
	}

	@Override
	public Sprite getDefaultSprite() {
		return Sprite.checkbox_default;
	}

	@Override
	public Sprite getHoverSprite() {
		return Sprite.checkbox_hovered;
	}

	@Override
	public Sprite getMarkedDefaultSprite() {
		return Sprite.checkbox_default_marked;
	}

	@Override
	public Sprite getMarkedHoverSprite() {
		return Sprite.checkbox_hovered_marked;
	}
	
}
