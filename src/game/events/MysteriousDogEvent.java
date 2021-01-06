package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Creatures;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.utils.Event;
import game.utils.Numbers;
import game.worlds.World;

public class MysteriousDogEvent extends TileEvent {
	
	public boolean done;
	
	public MysteriousDogEvent() {
	}
	
	public MysteriousDogEvent(boolean done) {
		this.done = done;
	}
	
	@Override
	public boolean onInteraction(Game game, final World world, Player player) {
		if(done == false)
		{
			if(player.getItemCount(Items.MAGICAL_LEAF).compareTo(Numbers.TEN) >= 0)
			{
				MessageBox.addMessage("Give the", "mysterious dog", "10 magical", "leaves?").setChoices(new ChoiceEvent() {
					
					@Override
					public void onChoice(MessageBox box, String choice) {
						if(choice.equals("Yes"))
						{
							MessageBox.addMessage("Thank you", "very much!").setDoneEvent(new Event() {
								
								@Override
								public void run() {
									world.putEvent(new MysteriousDogEvent(true), transform.getIntX() + 1, transform.getIntY() - 1);
									world.setPassable(true, transform.getIntX(), transform.getIntY() - 1);
									dead = true;
								}
							});
						}
					}
				}, "Yes", "No");
			}
			else
			{
				MessageBox.addMessage("I will grant", "you access once", "you lend me", "10 magical", "leaves...");
			}
		}
		else
		{
			MessageBox.addMessage("Umnumnumnum...");
		}
		return true;
	}
	
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		if(done == false)
			Creatures.getCreatureSprite(11).render(batch, transform.x * Tile.SIZE, (transform.y - 1) * Tile.SIZE, Tile.SIZE, Tile.SIZE);
		else
			Creatures.getCreatureSprite(11).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
