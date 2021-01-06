package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Creatures;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.scene.SceneItems;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.World;

public class ItemShowerEvent extends TileEvent {
	
	@Override
	public void onStepped(final Game game, World world, Player player) {
		super.onStepped(game, world, player);
		MessageBox.addMessage("I will show", "you your", "items").setDoneEvent(new Event() {
			@Override
			public void run() {
				game.setScene(SceneItems.class);
				
			}
		});
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		onStepped(game, world, player);
		return true;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Creatures.getCreatureSprite(17).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
