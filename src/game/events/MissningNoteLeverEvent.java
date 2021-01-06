package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.entity.Worlds;
import game.tile.Tile;
import game.worlds.CavePassage;
import game.worlds.World;

public class MissningNoteLeverEvent extends TileEvent {
	
	public final String switchName;
	public final String other;
	
	public MissningNoteLeverEvent(String switchName, String other)
	{
		this.switchName = switchName;
		this.other = other;
	}
	
	private void validate(Player player, World world)
	{
		if(player.data.getBoolean(switchName) && player.data.getBoolean(other))
		{
			MessageBox.addMessage("You sense", "that something", "has happened", "in the ", "over world...          ");
			World cavePassage = Worlds.getWorld(CavePassage.class);
			cavePassage.setPassable(true, 34, 161);
			cavePassage.foregroundTiles[34 + 161 * cavePassage.width] = 0;
		}
	}
	
	@Override
	public boolean onInteraction(final Game game, final World world, final Player player) {
		
		if(player.data.getBoolean(switchName))
		{
			return false;
		}
		MessageBox.addMessage("There's a lever", "hidden inside..", "Switch it?").setChoices(new ChoiceEvent() {
			
			@Override
			public void onChoice(MessageBox box, String choice) {
				if(choice.equals("Yes"))
				{
					player.data.setBoolean(switchName, true);
					game.getAudio().getSound("click").play();
					validate(player, world);
				}
			}
		}, "Yes", "No");
		
		return true;
	}
	
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Tile.tiles[271].draw(batch, transform.getIntX(), transform.getIntY());
	}
	
}
