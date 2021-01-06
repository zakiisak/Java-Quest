package game.events;

import java.math.BigInteger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Creatures;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.tile.Tile;
import game.utils.Color;
import game.entity.Player;
import game.entity.TileEvent;
import game.worlds.World;

public class ItemPayerEvent extends TileEvent {
	
	public int item, amount;
	
	public ItemPayerEvent(int item, int amount)
	{
		this.item = item;
		this.amount = amount;
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		super.onStepped(game, world, player);
		//onInteraction(game, world, player);
	}
	
	@Override
	public boolean onInteraction(Game game, final World world, final Player player) {
		if(player.getItemCount(item).compareTo(new BigInteger("" + amount)) >= 0)
		{
			MessageBox.addMessage("Pay " + amount, Items.getItemName(item), "to proceed?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						player.removeItem(item, amount);
						world.setPassable(true, transform.getIntX(), transform.getIntY() - 1);
						dead = true;
						Game.instance.getAudio().getSound("click").play();
					}
				}
			}, "Yes", "No");
		}
		else
		{
			MessageBox.addMessage("To proceed: ", "Come back", "when you", "have " + amount, Items.getItemName(item) + "S.");
		}
		return true;
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		Items.getItemSprite(item).renderWithOutline(batch, transform.x * Tile.SIZE, (transform.y - 1) * Tile.SIZE, Tile.SIZE, Tile.SIZE, 0, Color.black);
	}
	
}
