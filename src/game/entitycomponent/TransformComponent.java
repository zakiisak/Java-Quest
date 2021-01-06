package game.entitycomponent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.EntityComponent;
import game.utils.Point;

public class TransformComponent extends EntityComponent {
	public float x, y;
	
	public TransformComponent() 
	{
	}
	
	public TransformComponent(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public int getIntX()
	{
		return (int) x;
	}
	
	public int getIntY()
	{
		return (int) y;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void set(float x, float y)
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
	
	public void centify(boolean x_axis, boolean y_axis, SizeComponent size)
	{
		if(x_axis)
			x = Game.RES_WIDTH / 2 - size.width / 2;
		if(y_axis)
			y = Game.RES_HEIGHT / 2 - size.height / 2;
	}
	
	public Point point()
	{
		return new Point(this);
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
