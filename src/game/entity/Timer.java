package game.entity;

import game.entitycomponent.CounterComponent;
import game.utils.Event;

public class Timer extends Entity {
	
	public Event event;
	public boolean killOnEnd;
	private CounterComponent counter;
	
	public Timer(float seconds, Event event, boolean killOnEnd)
	{
		this.event = event;
		this.killOnEnd = killOnEnd;
		addComponent(counter = new CounterComponent(0, seconds * 60f, 1));
		counter.endEvent = new CounterComponent.Event() {
			
			@Override
			public void endReached(CounterComponent component) {
				component.current = component.start;
				Timer.this.event.run();
				if(Timer.this.killOnEnd) Timer.this.dead = true;
			}
		};
	}
	
	
	public Timer(float seconds, Event event) {
		this(seconds, event, true);
	}
	
}
