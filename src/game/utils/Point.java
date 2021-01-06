package game.utils;

import java.io.Serializable;

import game.entitycomponent.TransformComponent;

public class Point implements Serializable {
	public int x, y;
	
	public Point() {}
	
	public Point(TransformComponent transform)
	{
		this.x = transform.getIntX();
		this.y = transform.getIntY();
	}
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
