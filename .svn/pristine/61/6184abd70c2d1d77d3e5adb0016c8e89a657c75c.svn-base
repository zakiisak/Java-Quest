package game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Component {
	
	protected float x;
	protected float y;
	protected float width;
	protected float height;
	
	public abstract void render(SpriteBatch batch);
	
	public float getX()
	{
		return x;
	}
	public void setX(float x) 
	{
		this.x = x;
	}
	
	public float getY() 
	{
		return y;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public float getWidth() 
	{
		return width;
	}
	
	public void setWidth(float width) 
	{
		this.width = width;
	}
	
	public float getHeight() 
	{
		return height;
	}
	
	public void setHeight(float height)
	{
		this.height = height;
	}
	
	public void setPosition(float x, float y)
	{
		setX(x);
		setY(y);
	}
	
	public void setSize(float width, float height)
	{
		setWidth(width);
		setHeight(height);
	}
	
	public void setBounds(float x, float y, float width, float height)
	{
		setPosition(x, y);
		setSize(width, height);
	}
	
	public boolean containsPoint(float x, float y)
	{
		return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
	}
	
	public boolean containsMouse()
	{
		return containsPoint(Gdx.input.getX(), Gdx.input.getY());
	}
	
	
	
}
