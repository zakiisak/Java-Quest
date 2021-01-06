package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.CaveOfDestiny;
import game.worlds.World;

public class GemSocketEvent extends TileEvent {
	
	public int gem;
	public String name;
	public boolean inserted = false;
	
	public GemSocketEvent(int gem, String name) 
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
		if(player.data.getBoolean("EMERALD") && player.data.getBoolean("SAPPHIRE")
				&& player.data.getBoolean("RUBY") && player.data.getBoolean("AMETHYST")
				&& player.data.getBoolean("EARTH GEM") && player.data.getBoolean("TOPAZ") && world.getEvent(49, 134) == null)
		{
			System.out.println("all items are there");
			world.foregroundTiles[49 + 134 * world.width] -= 2;
			world.putEvent(new TransferEvent(45, 94)
					{
						@Override
						public void onStepped(Game game, World world, Player player) {
							super.onStepped(game, world, player);
							if(game.backgroundMusic != null)
							{
								if(game.backgroundMusic != game.getAudio().getMusic("cave2"))
								{
									game.stopBackgroundMusic();
									game.startBackgroundMusic("cave2", 1);
								}
							}
							else game.startBackgroundMusic("cave2", 1);
						}
					}, 49, 134);
			Game.instance.getAudio().getSound("click").play();
			
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
