package game.entitycomponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.EntityComponent;

public class CounterComponent extends EntityComponent {

	public float start, end, speed, current;
	public Event endEvent;
	
	public CounterComponent(float start, float end, float speed)
	{
		this.start = start;
		this.end = end;
		this.speed = speed;
		this.current = start;
	}
	
	public CounterComponent reverse()
	{
		float start = this.start;
		this.start = end;
		end = start;
		speed *= -1;
		return this;
	}
	
	@Override
	public String getName() {
		return "counter";
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}
	
	private void dispatchEvent()
	{
		if(endEvent != null)
			endEvent.endReached(this);
	}

	@Override
	public void tick() {
		current += speed;
		if(speed > 0)
		{
			if(current >= end)
			{
				current = end;
				dispatchEvent();
			}
		}
		else if(speed < 0)
		{
			if(current <= end)
			{
				dispatchEvent();
				current = end;
			}
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	
	public interface Event
	{
		public void endReached(CounterComponent component);
	}

}
