package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Creatures;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.World;

public class GhostFirstBrotherEvent extends TileEvent {
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		if(player.data.getBoolean("second_brother_assisted"))
		{
			if(player.possessItem(Items.ENCHANTED_SWORD) == false)
			{
				MessageBox.addMessage("I can't thank", "you enough!");
				MessageBox.addMessage("Here,", "take this!").setDoneEvent(new Event() {
					@Override
					public void run() {
						ItemPopup popup = new ItemPopup(Items.ENCHANTED_SWORD, 2, "You received", "the ", "ENCHANTED SWORD", "This gives you", "the ability to", "hit magical", "beings!");
						Game.instance.addEntity(popup);
					}
				});
			}
			else
			{
				MessageBox.addMessage("The ENCHANTED", "SWORD was", "once owned", "by the king", "of Rohan...");
			}
		}
		else
			MessageBox.addMessage(new MessageBox("Have you seen", "my brother?"));
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		Creatures.getCreatureSprite(56).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
