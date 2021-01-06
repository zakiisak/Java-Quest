package game.entitycomponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.EntityComponent;
import game.graphics.Sprite;
import game.utils.Color;

public class SpriteOverlayComponent extends EntityComponent {

	public TransformComponent transform;
	public SizeComponent size;
	public Sprite sprite;
	public Color tint = Color.white.cpy();
	public float counter = 0.0f;
	public float counterSpeed = 4.0f;
	
	
	public SpriteOverlayComponent(TransformComponent transform, SizeComponent size, Sprite sprite) 
	{
		this.transform = transform;
		this.size = size;
		this.sprite = sprite;
	}
	
	@Override
	public String getName() {
		return "sprite_overlay";
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		if(counter > 0) counter -= counterSpeed;
		if(counter < 0)
			counter = 0;
	}
	
	public void show()
	{
		counter = 90.0f;
	}

	@Override
	public void draw(SpriteBatch batch) {
		com.badlogic.gdx.graphics.Color c = batch.getColor();
		batch.setColor(tint.r, tint.g, tint.b, 1.0f * (float) Math.sin(Math.toRadians(counter)));
		sprite.render(batch, transform.x, transform.y, size.width, size.height);
		batch.setColor(c);
	}
	
	
}
