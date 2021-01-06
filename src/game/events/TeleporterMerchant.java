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
import game.tile.Tile;
import game.worlds.World;

public class TeleporterMerchant extends TileEvent {
	
	@Override
	public void onStepped(Game game, World world, final Player player) {
		super.onStepped(game, world, player);
		if(player.possessItem(Items.RING_OF_TELEPORTATION) == false)
		{
			MessageBox.addMessage("I will give", "you something", "if you give", "me 5000 G");
			MessageBox.addMessage("Are you", "up for it?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						if(player.canAfford(new BigInteger("5000")))
						{
							player.removeGold(new BigInteger("5000"));
							ItemPopup popup = new ItemPopup(Items.RING_OF_TELEPORTATION, 2, "You received", "the RING OF", "TELEPORTATION!", "With this, you", "can teleport to", "any bought", "spawnpoint, by", "pressing T.", "Be wary of", "caves though");
							Game.instance.addEntity(popup);
						}
						else
						{
							MessageBox.addMessage("It seems", "you don't", "have enough");
						}
					}
				}
			}, "Yes", "No");
		}
		else if(player.possessItem(Items.TELEPORT_UPGRADE) == false)
		{
			MessageBox.addMessage("I can upgrade", "your ring", "for 3000 G");
			MessageBox.addMessage("Are you", "up for it?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						if(player.canAfford(new BigInteger("3000")))
						{
							player.removeGold(new BigInteger("3000"));
							ItemPopup popup = new ItemPopup(Items.TELEPORT_UPGRADE, 2, "You received", "the TELEPORT", "UPGRADE!", "Now you can", "teleport to", "other players!");
							Game.instance.addEntity(popup);
						}
						else
						{
							MessageBox.addMessage("It seems", "you don't", "have enough");
						}
					}
				}
			}, "Yes", "No");
		}
		else
		{
			MessageBox.addMessage("How is it", "going with the", "ring?", "Remember not", "to teleport", "in caves", "unless you", "want to meet", "a terrible fate");
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
		Creatures.getCreatureSprite(18).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
