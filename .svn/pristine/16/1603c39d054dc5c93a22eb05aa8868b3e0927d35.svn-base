package game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.graphics.Sprite;

public class ScrollBarVertical extends ScrollBar {
	
	protected void updateBarMovement()
	{
		float delta = (float) Gdx.input.getDeltaY();
		y += delta;
		if(y + height > boxHeight)
			y = boxHeight - height;
		if(y < 0)
			y = 0;
		
	}
	
	protected void update()
	{
		if(sprite == null) sprite = Sprite.scrollbar_default;
		if(containsMouse())
		{
			setTransitionSprite(Sprite.scrollbar_hovered);
			if(Gdx.input.justTouched()) 
			{
				if(actionListener != null && Gdx.input.isButtonPressed(Buttons.LEFT)) actionListener.invoke(this);
			}
			if(Gdx.input.isButtonPressed(Buttons.LEFT))
			{
				dragging = true;
			}
		}
		else
		{
			setTransitionSprite(Sprite.scrollbar_button_default);
		}
		
		if(dragging && !Gdx.input.isButtonPressed(Buttons.LEFT))
			dragging = false;
		
		if(dragging)
		{
			updateBarMovement();
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
	
	public void render(SpriteBatch batch) {
		update();
		TiledRenderer.drawStretched(batch, Sprite.scrollbar_back, x, document.getY(), 16, document.getHeight());
		if(!transitionSprite.equals(sprite))
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
	}

}
