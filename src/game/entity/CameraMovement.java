package game.entity;

import game.Game;
import game.entitycomponent.TransformComponent;
import game.utils.Event;
import game.utils.MathUtils;
import game.utils.Point;

public class CameraMovement extends Entity {
	
	public TransformComponent transform;
	public float speed;
	public Point start, end;
	public float angle;
	public boolean tickable = true;
	
	public Event endEvent;
	
	public CameraMovement(Point startPoint, Point endPoint, float speed)
	{
		transform = new TransformComponent(startPoint.x, startPoint.y);
		this.start = startPoint;
		this.end = endPoint;
		this.speed = speed;
		this.angle = MathUtils.getAngle(start, end);
		Game.instance.player.tickable = false;
		setCamera();
	}
	
	public CameraMovement setEndEvent(Event endEvent)
	{
		this.endEvent = endEvent;
		return this;
	}
	
	@Override
	public void onKilled() {
		super.onKilled();
		Game.instance.player.tickable = true;
		resetCamera();
	}
	
	@Override
	public void tick() {
		if(tickable == false) return;
		super.tick();
		for(int i = 0; i < (int) (speed * 32f); i++)
		{
			transform.x += Math.cos(Math.toRadians(angle)) / 32f;
			transform.y += Math.sin(Math.toRadians(angle)) / 32f;
			if(MathUtils.dist(transform.x, transform.y, end.x + 0.5f, end.y + 0.5f) <= 1.0f)
			{
				dead = true;
				if(endEvent != null)
					endEvent.run();
				tickable = false;
				return;
			}
		}
	}
	
	private void setCamera()
	{
		Game.instance.player.tickable = false;
		Game.instance.world.attachedCameraFollower = transform;
	}
	
	private void resetCamera()
	{
		Game.instance.world.attachedCameraFollower = Game.instance.player.transform;
	}
	
}
