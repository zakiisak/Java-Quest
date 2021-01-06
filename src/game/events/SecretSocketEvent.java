package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.World;

public class SecretSocketEvent extends TileEvent {
	
	public int gem;
	public String name;
	public boolean inserted = false;
	
	public SecretSocketEvent(int gem, String name) 
	{
		this.gem = gem;
		this.name = name;
	}
	
	@Override
	public boolean isPassable() {
		return true; //inserted == false;
	}
	
	private static void validateGems(World world, Player player)
	{
		System.out.println("validating");
		if(player.data.getBoolean("S_EMERALD") && player.data.getBoolean("S_SAPPHIRE")
				&& player.data.getBoolean("S_RUBY") && player.data.getBoolean("S_AMETHYST")
				&& player.data.getBoolean("S_EARTH GEM") && player.data.getBoolean("S_TOPAZ")
				&& player.data.getBoolean("S_GREEN") && player.data.getBoolean("S_RED") && player.data.getBoolean("S_BLUE")
				&& player.data.getBoolean("S_DIAMOND") && world.isPassable(49, 18) == false)
		{
			System.out.println("all items are there");
			world.setPassable(true, 49, 18);
			for(int i = 0; i < 12; i++)
			{
				world.backgroundTiles[49 + (18 - i) * world.width] = 310;
			}
			Game.instance.getAudio().getSound("click").play(1, 0.75f);
		}
	}
	
	@Override
	public boolean onInteraction(Game game, final World world, final Player player) {
		boolean possessesGem = player.possessItem(gem);
		if(possessesGem && inserted == false)
		{
			MessageBox.addMessage(new MessageBox("You insert the", Items.getItemName(gem), "in the socket").setDoneEvent(new Event() {
				
				@Override
				public void run() {
					player.removeItem(gem);
					inserted = true;
					player.data.setBoolean(name, true);
					validateGems(world, player);
				}
			}));
			return true;
		}
		return false;
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		if(inserted)
		{
			Items.getItemSprite(gem).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
		}
	}
	
}
