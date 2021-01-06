package game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.graphics.Sprite;

public abstract class ScrollBar extends Component {
	
	protected float boxWidth;
	protected float boxHeight;
	
	protected Document document;
	protected Sprite sprite;
	protected Sprite transitionSprite;
	protected int transitionFactor = 0;
	protected int transitionDirection = 1;
	protected ActionListener actionListener;
	protected boolean transitioning = false;
	protected boolean dragging = false;
	
	protected ScrollBar()
	{
		transitionSprite = Sprite.scrollbar_default;
	}
	
	public ScrollBar(Document attachedDocument, int scrollMode)
	{
		setBounds(x, y, width, height);
		transitionSprite = Sprite.button_default;
		this.document = attachedDocument;
	}
	
	
	protected void update()
	{
		if(sprite == null) sprite = Sprite.scrollbar_default;
		if(containsMouse())
		{
			setTransitionSprite(Sprite.button_hovered);
			if(Gdx.input.justTouched()) 
			{
				if(actionListener != null && Gdx.input.isButtonPressed(Buttons.LEFT)) actionListener.invoke(this);
			}
		}
		else
		{
			setTransitionSprite(Sprite.button_default);
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
	
	protected void setTransitionSprite(Sprite sprite)
	{
		if(!transitionSprite.equals(sprite) && !transitioning)
		{
			transitionFactor = 0;
			transitionSprite = new Sprite(sprite);
			transitioning = true;
		}
	}
	
	@Override
	public abstract void render(SpriteBatch batch);
	
	public ScrollBar setActionListener(ActionListener actionListener)
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
	
	public Document getDocument()
	{
		return document;
	}
	
}
