package game.entity;

import java.math.BigInteger;

import game.Game;
import game.entitycomponent.CentifierComponent;
import game.entitycomponent.CounterComponent;
import game.entitycomponent.MovementComponent;
import game.entitycomponent.SizeComponent;
import game.entitycomponent.SpriteComponent;
import game.entitycomponent.TransformComponent;
import game.utils.Event;
import game.utils.Numbers;

public class ItemPopup extends Entity {
	
	public static int activePopups = 0;
	public static boolean noPopups()
	{
		return activePopups <= 0;
	}
	
	public int item;
	public TransformComponent transform;
	public SizeComponent size;
	public CentifierComponent centifier;
	public CounterComponent counter;
	public String[] message;
	public int scale;
	
	public Event obliterationEvent;
	private boolean reachedAnimationEnd = false;
	
	public ItemPopup(int item, int scale, String... message) {
		this(item, Numbers.ONE, scale, message);
	}
	
	public ItemPopup(int item, BigInteger count, int scale, String... message)
	{
		((MovementComponent) Game.instance.player.getComponent("movement")).enabled = false;
		if(item == Items.GOLD())
			Game.instance.player.stats.gold = Game.instance.player.stats.gold.add(count);
		else
			Game.instance.player.addItem(item, count.longValue());
		this.scale = scale;
		this.message = message;
		this.item = item;
		transform = new TransformComponent();
		size = new SizeComponent((int) 16 * scale, 16 * scale);
		centifier = new CentifierComponent(transform, size);
		counter = new CounterComponent(0, 90, 1.5f);
		SpriteComponent sprite = new SpriteComponent(Items.getItemSprite(item), transform, size);
		sprite.outlineWidth = 1;
		addComponent(transform);
		addComponent(size);
		addComponent(sprite);
		addComponent(centifier);
		addComponent(counter);
		Game.instance.getAudio().getSound("item").play();
	}
	
	public ItemPopup setObliterationEvent(Event event)
	{
		this.obliterationEvent = event;
		return this;
	}
	
	@Override
	public void onAdded(Object owner) {
		activePopups++;
		super.onAdded(owner);
	}
	
	
	@Override
	public void onKilled() {
		activePopups--;
		super.onKilled();
		((MovementComponent) Game.instance.player.getComponent("movement")).enabled = true;
		if(obliterationEvent != null)
			obliterationEvent.run();
	}
	
	@Override
	public void tick() {
		if(counter.current >= counter.end && !reachedAnimationEnd && !Game.instance.getAudio().getSound("item").isPlaying())
		{
			removeComponent("counter");
			MessageBox.addMessage(message).setDoneEvent(new Event()
			{
				@Override
				public void run() {
					dead = true;
				}
			});
			reachedAnimationEnd = true;
		}
		centifier.yoffset = - 20 * scale * (float) Math.sin(Math.toRadians(counter.current));
		super.tick();
	}
	
}
