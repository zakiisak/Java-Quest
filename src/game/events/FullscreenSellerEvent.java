package game.events;

import java.math.BigInteger;

import com.badlogic.gdx.Gdx;
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

public class FullscreenSellerEvent extends TileEvent {
	
	@Override
	public void onStepped(final Game game, World world, final Player player) {
		super.onStepped(game, world, player);
		if(player.possessItem(Items.BOOK_OF_FULLSCREEN) == false)
		{
			MessageBox.addMessage("You found me!", "Take this as", "a reward.").setDoneEvent(new Event() {
				@Override
				public void run() {
					ItemPopup popup = new ItemPopup(Items.BOOK_OF_FULLSCREEN, 2, "You received", "the BOOK OF", "FULLSCREEN!", "With this in", "possession, you", "can press F", "to enter", "fullscreen!");
					game.addEntity(popup);
				}
			});
		}
		else
		{
			if(player.isInNewGamePlus() && player.possessItem(Items.FULLSCREEN_UPGRADE) == false)
			{
				MessageBox.addMessage("Do you want", "to upgrade your", "BOOK OF", "FULLSCREEN for", "13.37", "quadrillion?").setChoices(new ChoiceEvent() {
					
					@Override
					public void onChoice(MessageBox box, String choice) {
						if(choice.equals("Yes"))
						{
							if(player.canAfford(new BigInteger("13370000000000000")))
							{
								ItemPopup popup = new ItemPopup(Items.FULLSCREEN_UPGRADE, 2, "You received", "the FULLSCREEN", "UPGRADE!", "Now, fullscreen", "has a higher", "resolution.");
								game.addEntity(popup);
								Game.RES_WIDTH = Gdx.graphics.getWidth();
								Game.RES_HEIGHT = Gdx.graphics.getHeight();
							}
						}
					}
				}, "Yes", "No");
			}
			else MessageBox.addMessage("Remember that", "you can press", "F to go", "fullscreen!");
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
		Creatures.getCreatureSprite(21).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
