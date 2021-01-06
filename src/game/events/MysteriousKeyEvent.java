package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.utils.Color;
import game.utils.Event;
import game.worlds.World;

public class MysteriousKeyEvent extends TileEvent {
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		ItemPopup popup = new ItemPopup(Items.MYSTERIOUS_KEY, 2, "You found the", "MYSTERIOUS KEY!");
		popup.setObliterationEvent(new Event() {
			
			@Override
			public void run() {
			}
		});
		game.addEntity(popup);
		dead = true;
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		Items.getItemSprite(Items.MYSTERIOUS_KEY).renderWithOutline(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE, 1, Color.black);
	}
	
}
