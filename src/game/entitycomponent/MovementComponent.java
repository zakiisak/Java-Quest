package game.entitycomponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.EntityComponent;
import game.worlds.World;

public class MovementComponent extends EntityComponent {

	public World world;
	public TransformComponent transform;
	public boolean noclip = false;
	public boolean enabled = true;
	public boolean free = false;
	
	public MovementComponent(World world, TransformComponent transform)
	{
		this.world = world;
		this.transform = transform;
	}
	
	@Override
	public String getName() {
		return "movement";
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
	
	private boolean collides(float mx, float my)
	{
		if(noclip) return false;
		for(int c = 0; c < 4; c++)
		{
			int tx = (int) (transform.x + 0.01f +(c % 2) * 0.98f + mx);
			int ty = (int) (transform.y + + 0.01f + (c / 2) * 0.98f + my);
			if(world.isPassable(tx, ty) == false)
				return true;
			if(world.isEventPassable(tx, ty) == false)
				return true;
		}
		return false;
	}
	
	private float positive(float m)
	{
		if(m < 0)
			return -m;
		return m;
	}
	
	private static final float STEP = 0.125f;
	private float abs(float m)
	{
		if(m < 0)
			return -STEP;
		return STEP;
	}
	
	private boolean moveFree(float mx, float my)
	{
		boolean hasMoved = false;
		mx *= 0.125f;
		my *= 0.125f;
		
		float absX = positive(mx);
		float absY = positive(my);
		float xm = abs(mx);
		float ym = abs(my);
		
		for(float i = 0; i < absX; i += STEP)
		{
			if(collides(xm, 0))
			{
				break;
			}
			transform.x += xm;
			hasMoved = true;
		}
		for(float i = 0; i < absY; i += STEP)
		{
			if(collides(0, ym))
			{
				break;
			}
			transform.y += ym;
			hasMoved = true;
		}
		
		return hasMoved;
	}
	
	public boolean move(float mx, float my)
	{
		if(enabled == false)
			return false;
		if(free) 
		{
			return moveFree(mx, my);
		}
		if(noclip)
		{
			transform.x += mx;
			transform.y += my;
			return mx != 0 || my != 0;
		}
		boolean hasMoved = false;
		if(world.isPassable((int) transform.x + (int) mx, (int) transform.y) && world.isEventPassable((int) transform.x + (int) mx, (int) transform.y))
		{
			transform.x += mx;
			hasMoved = true;
		}
		if(world.isPassable((int) transform.x, (int) transform.y + (int) my) && world.isEventPassable((int) transform.x, (int) transform.y + (int) my))
		{
			transform.y += my;
			hasMoved = true;
		}
		return hasMoved;
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
