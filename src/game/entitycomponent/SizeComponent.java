package game.entitycomponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.EntityComponent;

public class SizeComponent extends EntityComponent {

	public int width, height;
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public void set(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public SizeComponent() {}
	
	public SizeComponent(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	@Override
	public String getName() {
		return "size";
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isTickable() {
		return false;
	}
	
	@Override
	public boolean isVisible() {
		return false;
	}

}
