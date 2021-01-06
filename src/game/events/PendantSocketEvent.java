package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.utils.Color;
import game.utils.Event;
import game.worlds.CaveOfDestiny;
import game.worlds.World;

public class PendantSocketEvent extends TileEvent {
	
	public int gem;
	public String name;
	public boolean inserted = false;
	
	public PendantSocketEvent(int gem, String name) 
	{
		this.gem = gem;
		this.name = name;
	}
	
	@Override
	public boolean isPassable() {
		return true;//inserted == false;
	}
	
	private static void validateGems(World world, Player player)
	{
		System.out.println("validating");
		if(player.data.getBoolean("GREEN PENDANT") && player.data.getBoolean("RED PENDANT")
				&& player.data.getBoolean("BLUE PENDANT"))
		{
			for(int i = 0; i < 7; i++)
			{
				world.foregroundTiles[50 + (23 - i) * world.width] = 0;
				world.setPassable(true, 50, 23 - i);
			}
		}
	}
	
	@Override
	public boolean onInteraction(Game game, final World world, final Player player) {
		boolean possessesGem = player.possessItem(gem);
		if(possessesGem && inserted == false)
		{
			MessageBox.addMessage(new MessageBox("You insert the", name, "in the socket").setDoneEvent(new Event() {
				
				@Override
				public void run() {
					player.removeItem(gem);
					inserted = true;
					player.data.setBoolean(name, true);
					world.foregroundTiles[transform.getIntX() + transform.getIntY() * world.width] = 0;
					validateGems(world, player);
				}
			}));
			return true;
		}
		return false;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.drawPost(batch);
		if(inserted)
		{
			Items.getItemSprite(gem).renderWithOutline(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE, 2, Color.black);
		}
	}
	
}
