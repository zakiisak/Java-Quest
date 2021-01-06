package game.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.worlds.World;

public class Entity implements Serializable {
	public Map<String, EntityComponent> components = new HashMap<String, EntityComponent>();
	public final Tag data = new Tag();
	public boolean dead = false;
	public boolean added = false;
	
	public void tick()
	{
		for(EntityComponent component : components.values())
		{
			if(component.isTickable())
				component.tick();
		}
	}
	
	public void draw(SpriteBatch batch)
	{
		for(EntityComponent component : components.values())
		{
			if(component.isVisible())
				component.draw(batch);
		}
	}
	
	public void drawPost(SpriteBatch batch)
	{
		for(EntityComponent component : components.values())
		{
			if(component.isPostVisible())
				component.drawPost(batch);
		}
	}
	
	public void onKilled() {}
	
	public void onAdded(Object owner) {}
	
	public void addComponent(EntityComponent component)
	{
		if(component.getName() == null) System.err.println("Component: " + component + " name is null! Not acceptable");
		if(this.components.containsKey(component.getName()))
		{
			int counter = 2;
			while(this.components.containsKey(component.getName() + counter)) counter++;
			this.components.put(component.getName() + counter, component);
		}
		else this.components.put(component.getName(), component);
	}
	
	
	public EntityComponent getComponent(String name)
	{
		//List
		return components.get(name);
	}
	
	public void removeComponent(String name)
	{
		if(!components.containsKey(name))
			return;
		components.remove(name);
	}
	
}
