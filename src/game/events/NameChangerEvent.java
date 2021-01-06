package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Creatures;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.worlds.World;

public class NameChangerEvent extends TileEvent {
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		super.onStepped(game, world, player);
		onInteraction(game, world, player);
	}
	
	@Override
	public boolean onInteraction(final Game game, World world, final Player player) {
		MessageBox.addMessage("Welcome!", "here you", "can change", "your name.");
		MessageBox.addMessage("Change Name?").setChoices(new game.entity.MessageBox.ChoiceEvent() {
			@Override
			public void onChoice(MessageBox box, String choice) {
				if(choice.equals("Yes"))
				{
					String name = javax.swing.JOptionPane.showInputDialog(null, "Enter new name:", "New Name", javax.swing.JOptionPane.QUESTION_MESSAGE);
					if(name.isEmpty() == false)
					{
						Game.instance.player.name = name;
						Game.instance.client.sendStats();
						MessageBox.addMessage("You name", "changed to", name + ".");
					}
				}
			}
		}, "Yes", "No");
		return true;
	}
	
	public void drawPost(SpriteBatch batch)
	{
		super.drawPost(batch);
		batch.setColor(1, 1, 1, 1);
		Creatures.getCreatureSprite(70).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
