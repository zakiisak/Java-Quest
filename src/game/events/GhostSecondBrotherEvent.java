package game.events;

import java.math.BigInteger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Creatures;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.entity.Worlds;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.Overworld;
import game.worlds.World;

public class GhostSecondBrotherEvent extends TileEvent {
	
	@Override
	public void onStepped(final Game game, World world, final Player player) {
		if(player.data.getBoolean("second_brother_assisted"))
		{
			MessageBox.addMessage("You are ", "the best!");
			if(player.possessItem(Items.HEALTH_POTION) == false)
			{
				MessageBox.addMessage("Here, take", "this").setDoneEvent(new Event() {
					@Override
					public void run() {
						ItemPopup popup = new ItemPopup(Items.HEALTH_POTION, 2, "You received a", "HEALING POTION!", "", "This potion", "heals 50%", "of max hp");
						game.addEntity(popup);
					}
				});
			}
		}
		else
		{
			MessageBox.addMessage("Please help me!", "I need 200000 G", "to get out", "of here...", "Will you", "support me?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						if(player.canAfford(new BigInteger("200000")))
						{
							player.removeGold(new BigInteger("200000"));
							player.data.setBoolean("second_brother_assisted", true);
							MessageBox.addMessage("Thank you", "so much!", "Now I can", "Go back to", "my brother!").setDoneEvent(new Event() {
								
								@Override
								public void run() {
									dead = true;
									Worlds.getWorld(Overworld.class).putEvent(new GhostSecondBrotherEvent(), 26, 39);
								}
							});
						}
						else
						{
							MessageBox.addMessage("Fantastic!", "Come back when", "you have them");
						}
					}
				}
			}, "Yes", "No");
		}
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		Creatures.getCreatureSprite(58).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
