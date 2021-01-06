package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Creatures;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.scene.SceneCrafting;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.World;

public class CraftingEvent extends TileEvent {
	
	@Override
	public void onStepped(final Game game, World world, Player player) {
		super.onStepped(game, world, player);
		MessageBox.addMessage("What do you", "want to craft?").setDoneEvent(new Event() {
			@Override
			public void run() {
				game.setScene(SceneCrafting.class);
			}
		});
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		onStepped(game, world, player);
		return true;
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.draw(batch);
		Creatures.getCreatureSprite(19).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
