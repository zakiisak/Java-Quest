package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.worlds.World;

public class MysteriousLockEvent extends TileEvent {
	
	@Override
	public boolean onInteraction(final Game game, final World world, final Player player) {
		
		if(player.possessItem(Items.MYSTERIOUS_KEY))
		{
			MessageBox.addMessage("Unlock with", "the MYSTERIOUS", "KEY?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						player.removeItem(Items.MYSTERIOUS_KEY);
						world.setPassable(true, transform.getIntX(), transform.getIntY() - 1);
						dead = true;
						game.getAudio().getSound("click").play();
					}
				}
			}, "Yes", "No");
		}
		return true;
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		Tile.tiles[438].draw(batch, transform.getIntX(), transform.getIntY() - 1);
		batch.setColor(0, 0, 0, 1);
		Items.getItemSpriteOverlay(Items.MYSTERIOUS_KEY).render(batch, transform.x * Tile.SIZE, (transform.y - 1) * Tile.SIZE, Tile.SIZE, Tile.SIZE);
		batch.setColor(1, 1, 1, 1);
	}
	
}
