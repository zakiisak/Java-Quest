package game.entity;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class EntityComponent implements Serializable {
	private static final long serialVersionUID = -6631856169451991464L;

	public boolean isTickable()
	{
		return true;
	}
	
	public boolean isVisible()
	{
		return true;
	}
	
	public boolean isPostVisible()
	{
		return false;
	}
	
	public abstract String getName();
	public abstract void create();
	public abstract void tick();
	public abstract void draw(SpriteBatch batch);
	
	public void drawPost(SpriteBatch batch) {}
}
