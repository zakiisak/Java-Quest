package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Creatures;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.tile.Tile;
import game.utils.Event;
import game.entity.Player;
import game.entity.TileEvent;
import game.worlds.World;
import game.worlds.WorldOfInversement;

public class InversementManEvent extends TileEvent {
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		super.onStepped(game, world, player);
		onInteraction(game, world, player);
	}
	
	@Override
	public boolean onInteraction(final Game game, final World world, Player player) {
		MessageBox.addMessage("I will show", "you something", "interesting, if", "you give me", "the magical", "key from", "legends.");
		if(player.possessItem(Items.MAGIC_KEY))
		{
			MessageBox.addMessage("Give the ", "MAGICAL KEY?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						MessageBox.addMessage("Get ready...").setDoneEvent(new Event() {
							@Override
							public void run() {
								world.putEvent(new RainbowTransferEvent(WorldOfInversement.class, 49, 88), transform.getIntX(), transform.getIntY());
								game.getAudio().getSound("teleport").play(1, 0.4f);
							}
						});
					}
				}
			}, "Yes", "No");
		}
		return true;
	}
	
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		Creatures.getCreatureSprite(52).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
