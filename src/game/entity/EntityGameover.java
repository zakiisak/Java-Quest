package game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.graphics.Sprite;
import game.utils.Event;

public class EntityGameover extends Entity {
	
	public Event onFinished;
	
	private int tick;
	
	public EntityGameover(Event onFinished)
	{
		this.onFinished = onFinished;
		Game.instance.player.visible = false;
	}
	@Override
	public void onKilled() {
		super.onKilled();
		if(onFinished != null)
			onFinished.run();
	}
	
	@Override
	public void tick() {
		super.tick();

		tick++;
		if(tick >= 320)
		{
			Game.instance.player.respawn();
			tick = 0;
			dead = true;
		}
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		batch.setColor(1, 1, 1, 1);
		float rotation = (float) tick;
		if(tick > 90)
			rotation = 90;
		if(tick > 180)
		{
			float percentage = (float) (tick - 180) / (250.0f - 180.0f);
			if(percentage > 1)
				percentage = 1;
			batch.setColor(0, 0, 0, percentage);
			Sprite.getSprite("white").render(batch, 0, 0, Game.RES_WIDTH, Game.RES_HEIGHT);
		}
		if(tick >= 240)
		{
			float percentage = (float) (tick - 240) / (300.0f - 240.0f);
			if(percentage > 1)
				percentage = 1;
			batch.setColor(1, 1, 1, 1);
			Sprite.getSprite("dead_player").render(batch, Game.RES_WIDTH / 2 - 26 / 2, Game.RES_HEIGHT / 2 - 35 / 2, 26, 35, 180 + rotation);
			batch.setColor(1, 1, 1, percentage);
			Sprite.getSprite("dead_player_gray").render(batch, Game.RES_WIDTH / 2 - 26 / 2, Game.RES_HEIGHT / 2 - 35 / 2, 26, 35, 180 + rotation);
		}
		else
		{
			batch.setColor(1, 1, 1, 1);
			Sprite.getSprite("dead_player").render(batch, Game.RES_WIDTH / 2 - 26 / 2, Game.RES_HEIGHT / 2 - 35 / 2, 26, 35, 180 + rotation);
		}
	}
	
	
	
}
