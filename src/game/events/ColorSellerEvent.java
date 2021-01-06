package game.events;

import java.math.BigInteger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Creatures;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.tile.Tile;
import game.entity.Player;
import game.entity.TileEvent;
import game.worlds.World;

public class ColorSellerEvent extends TileEvent {
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		super.onStepped(game, world, player);
		onInteraction(game, world, player);
	}
	
	@Override
	public boolean onInteraction(final Game game, World world, final Player player) {
		if(player.possessItem(Items.BOOK_OF_COLORS))
		{
			MessageBox.addMessage("Hi there!");
			MessageBox.addMessage("Do you want", "to sell the", "BOOK OF COLORS?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						player.addGold(new BigInteger("50"));
						game.getAudio().getSound("sell").play();
						player.removeItem(Items.BOOK_OF_COLORS);
					}
				}
			}, "Yes", "No");
		}
		else
		{
			if(player.canAfford(new BigInteger("100")))
			{
				MessageBox.addMessage("I sell", "the BOOK OF", "COLORS", "for 100 G.");
				MessageBox.addMessage("Buy?").setChoices(new ChoiceEvent() {
					
					@Override
					public void onChoice(MessageBox box, String choice) {
						if(choice.equals("Yes"))
						{
							player.removeGold(new BigInteger("100"));
							ItemPopup popup = new ItemPopup(Items.BOOK_OF_COLORS, 2, "You received", "the BOOK", "OF COLORS!", "Now your", "eye can", "comprehend more", "wavelengths!");
							game.addEntity(popup);
						}
					}
				}, "Yes", "No");
			}
			else
			{
				MessageBox.addMessage("Come back", "when you", "have " + 100 + " G.");
			}
		}
		return true;
	}
	
	public void drawPost(SpriteBatch batch)
	{
		super.drawPost(batch);
		batch.setColor(1, 1, 1, 1);
		Creatures.getCreatureSprite(66).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
