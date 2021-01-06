package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.TileEvent;
import game.tile.Tile;

public class BlockerEvent extends TileEvent {
	@Override
	public boolean isPassable() {
		return false;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Tile.tiles[176].draw(batch, transform.getIntX(), transform.getIntY());
	}
}
