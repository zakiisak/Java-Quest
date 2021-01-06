package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Creatures;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.World;

public class MagicDiceSellerEvent extends TileEvent {
	
	@Override
	public void onStepped(final Game game, World world, final Player player) {
		super.onStepped(game, world, player);
		if(player.possessItem(Items.MAGIC_DICE) == false)
		{
			MessageBox.addMessage("You found me!", "Take this as", "a reward.").setDoneEvent(new Event() {
				@Override
				public void run() {
					ItemPopup popup = new ItemPopup(Items.MAGIC_DICE, 2, "You received", "the MAGIC DICE!", "This enables", "automatic", "escape!");
					game.addEntity(popup);
				}
			});
		}
		else
		{
			MessageBox.addMessage("Hope you", "like the", "loaded die", "*wink*");
		}
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		onStepped(game, world, player);
		return true;
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		Creatures.getCreatureSprite(40).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
