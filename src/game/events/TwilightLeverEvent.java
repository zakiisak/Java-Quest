package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.worlds.World;

public class TwilightLeverEvent extends TileEvent {
	
	public final String switchName;
	public final String other;
	
	public TwilightLeverEvent(String switchName, String other)
	{
		this.switchName = switchName;
		this.other = other;
	}
	
	private void validate(Player player, World world)
	{
		if(player.data.getBoolean(switchName) && player.data.getBoolean(other))
		{
			world.setPassable(true, 36, 45);
			world.setPassable(true, 37, 45);
			world.setPassable(true, 38, 45);
			world.foregroundTiles[36 + 45 * world.width] = 0;
			world.foregroundTiles[37 + 45 * world.width] = 0;
			world.foregroundTiles[38 + 45 * world.width] = 0;
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
