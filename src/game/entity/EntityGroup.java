package game.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityGroup extends Entity {
	
	public List<Entity> entities = new ArrayList<Entity>();
	
	public void addEntity(Entity entity)
	{
		entities.add(entity);
		entity.added = true;
	}
	
	@Override
	public void tick() {
		super.tick();
		for(int i = 0; i < entities.size(); i++)
		{
			Entity entity = entities.get(i);
			entity.tick();
			if(entity.dead)
			{
				entity.onKilled();
				entity.added = false;
				entities.remove(i);
				i--;
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).draw(batch);
		}
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).drawPost(batch);
		}
	}
	
}
