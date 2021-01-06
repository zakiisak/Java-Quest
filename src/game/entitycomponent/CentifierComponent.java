package game.entitycomponent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.EntityComponent;

public class CentifierComponent extends EntityComponent {

	public TransformComponent transform;
	public SizeComponent size;
	public float xoffset, yoffset;
	public boolean x = true;
	public boolean y = true;
	
	public CentifierComponent(TransformComponent transform, SizeComponent size) 
	{
		this.transform = transform;
		this.size = size;
	}
	
	public CentifierComponent disableX()
	{
		x = false;
		return this;
	}
	
	public CentifierComponent disableY()
	{
		y = false;
		return this;
	}
	
	
	public CentifierComponent setOffset(float x, float y) {
		this.xoffset = x;
		this.yoffset = y;
		return this;
	}
	
	@Override
	public String getName() {
		return "centifier";
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		if(x)
			transform.x = (float) Game.RES_WIDTH / 2 - (float) size.width / 2 + xoffset;
		if(y)
			transform.y = (float) Game.RES_HEIGHT / 2 - (float) size.height / 2 + yoffset;
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

}
