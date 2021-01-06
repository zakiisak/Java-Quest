package game.entity;

import com.badlogic.gdx.Gdx;

import game.Game;
import game.entitycomponent.CentifierComponent;
import game.entitycomponent.CounterComponent;
import game.entitycomponent.SizeComponent;
import game.entitycomponent.SpriteComponent;
import game.entitycomponent.TransformComponent;
import game.graphics.Sprite;
import game.utils.Color;
import game.utils.Event;

public class AttackAnimation extends Entity {
	public static final float DEFAULT_SPEED = 6;
	public static final float UPGRADE_SPEED = 90;
	
	protected TransformComponent transform;
	protected SizeComponent size;
	protected CounterComponent counter;
	public Event onKilledEvent;
	public boolean reversed;
	
	public AttackAnimation(Color color, float xoffset, float yoffset, float speed)
	{
		transform = new TransformComponent();
		size = new SizeComponent();
		counter = new CounterComponent(0, 90, speed);
		counter.endEvent = new CounterComponent.Event() {
			@Override
			public void endReached(CounterComponent component) {
				AttackAnimation.this.endReached(component);
			}
		};
		
		addComponent(transform);
		addComponent(size);
		addComponent(new CentifierComponent(transform, size).setOffset(xoffset, yoffset));
		addComponent(counter);
		SpriteComponent sprite = new SpriteComponent(Sprite.white, transform, size);
		sprite.post = true;
		sprite.color = color.cpy();
		addComponent(sprite);
	}
	
	public AttackAnimation(Color color, float xoffset, float yoffset) {
		this(color, xoffset, yoffset, DEFAULT_SPEED);
	}
	
	public AttackAnimation setEndReachedEvent(Event event)
	{
		this.onKilledEvent = event;
		return this;
	}
	
	public AttackAnimation setReversed()
	{
		reversed = true;
		return this;
	}
	
	protected void endReached(CounterComponent component)
	{
		if(dead) return;
		dead = true;
		if(onKilledEvent != null)
			onKilledEvent.run();
	}
	
	@Override
	public void tick() {
		size.width = (int) ((float) Math.sin(Math.toRadians((reversed ? 90 : 0) + counter.current)) * Game.RES_WIDTH * (reversed ? 1.0f : 1.2f));
		size.height = (int) ((float) Math.sin(Math.toRadians((reversed ? 90 : 0) + counter.current)) * Game.RES_HEIGHT * (reversed ? 1.0f : 1.2f));
		super.tick();
	}
	
}
