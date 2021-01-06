package game.entitycomponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.EntityComponent;
import game.utils.Point;

public class TransformFloatComponent extends EntityComponent {
	public int x, y;
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void set(Point point)
	{
		this.x = point.x;
		this.y = point.y;
	}
	
	@Override
	public String getName() {
		return "transform";
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
