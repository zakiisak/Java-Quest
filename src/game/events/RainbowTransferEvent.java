package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.Creatures;
import game.entity.TileEvent;
import game.tile.Tile;
import game.utils.Color;
import game.utils.Point;
import game.worlds.World;

public class RainbowTransferEvent extends TransferEvent {

	public float hue = 0.0f;
	public float saturation = 1.0f;
	public float brightness = 1.0f;
	
	public RainbowTransferEvent(Class<? extends World> worldClass, int x, int y) {
		super(worldClass, x, y);
	}
	
	public RainbowTransferEvent(int world, int x, int y)
	{
		super(world, x, y);
	}
	
	public RainbowTransferEvent(int x, int y)
	{
		super(x, y);
	}
	
	
	@Override
	public void tick() {
		super.tick();
		hue += 4.0f;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
//		Color hsb = Color.getHSB(hue, saturation, brightness);
//		batch.setColor(hsb.r, hsb.g, hsb.b, 1.0f);
		Color sr = Color.getSineRainbow(hue);
		batch.setColor(sr.r, sr.g, sr.b, 1.0f);
		Creatures.getCreatureSprite(7).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
		batch.setColor(1, 1, 1, 1);
	}
	
	
	
}
