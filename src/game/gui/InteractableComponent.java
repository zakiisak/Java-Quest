package game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.graphics.Sprite;

public abstract class InteractableComponent extends Component {
	protected Sprite sprite;
	protected Sprite transitionSprite;
	protected int transitionFactor = 0;
	protected int transitionDirection = 1;
	protected boolean transitioning = false;
	protected ActionListener actionListener;
	protected boolean marked;
	
	protected void update()
	{
		if(sprite == null)
			setBothSprites(getDefaultSprite());
		if(containsMouse())
		{
			if(Gdx.input.justTouched() && Gdx.input.isButtonPressed(Buttons.LEFT))
			{
				onPressed();
				if(actionListener != null) 
				{
					actionListener.invoke(this);
				}
			}
			setTransitionSprite(marked ? getMarkedHoverSprite() : getHoverSprite());
		}
		else
		{
			setTransitionSprite(marked ? getMarkedDefaultSprite() : getDefaultSprite());
		}
		if(!sprite.equals(transitionSprite))
		{
			if(transitionFactor < 10)
			{
				transitionFactor += transitionDirection;
			}
			else
			{
				sprite = new Sprite(transitionSprite);
				transitioning = false;
			}
		}
	}
	
	public void render(SpriteBatch batch)
	{
		update();
		boolean transition = transitionSprite != null;
		if(transition)
			transition = !transitionSprite.equals(sprite);
		if(transition)
		{
			float frontAlpha = (float) transitionFactor / 10.0f;
			batch.setColor(1, 1, 1, 1);
			sprite.render(batch, x, y, width, height);
			batch.setColor(1, 1, 1, frontAlpha);
			transitionSprite.render(batch, x, y, width, height);
		}
		else
		{	
			batch.setColor(1, 1, 1, 1);
			sprite.render(batch, x, y, width, height);
		}
	}
	
	protected void onPressed()
	{
		
	}
	
	public abstract Sprite getDefaultSprite();
	public abstract Sprite getHoverSprite();
	public abstract Sprite getMarkedDefaultSprite();
	public abstract Sprite getMarkedHoverSprite();
	
	public InteractableComponent setActionListener(ActionListener actionListener)
	{
		this.actionListener = actionListener;
		return this;
	}
	
	public Sprite getSprite()
	{
		return sprite;
	}
	
	public ActionListener getActionListener()
	{
		return actionListener;
	}
	
	public boolean isMarked()
	{
		return marked;
	}
	
	protected void setBothSprites(Sprite sprite)
	{
		this.sprite = sprite;
		this.transitionSprite = sprite;
	}
	
	public void setMarked(boolean marked)
	{
		setBothSprites(marked ? getMarkedDefaultSprite() : getDefaultSprite());
		this.marked = marked;
	}
	
	
	protected void setTransitionSprite(Sprite sprite)
	{
		boolean equals = transitionSprite != null;
		if(equals)
			equals = transitionSprite.equals(sprite);
		
		if(!equals && !transitioning)
		{
			transitionFactor = 0;
			transitionSprite = new Sprite(sprite);
			transitioning = true;
		}
	}
	
}
