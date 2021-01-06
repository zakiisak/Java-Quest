package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.Recipe;
import game.entity.Recipe.Integrant;
import game.entity.TileEvent;
import game.tile.Tile;
import game.utils.Event;
import game.utils.Numbers;
import game.worlds.World;

public class MissingNoteEvent extends TileEvent {

	@Override
	public void onStepped(Game game, World world, Player player) {
		super.onStepped(game, world, player);
		onInteraction(game, world, player);
	}
	
	private void validate(final Game game, World world, final Player player)
	{
		if(player.getItemCount(Items.MISSING_NOTE).compareTo(Numbers.FOUR) >= 0)
		{
			player.removeItem(Items.MISSING_NOTE, 4);
			MessageBox.addMessage("You have", "successfully", "assembled all", "the missing", "notes!").setDoneEvent(new Event() {
				
				@Override
				public void run() {
					ItemPopup popup = new ItemPopup(Items.ANCIENT_SCROLL, 2, "You assembled", "the ANCIENT", "SCROLL!", "Upon further", "investigation", "it looks like", "some kind of", "recipe....                             ");
					player.recipes.add(new Recipe(new Integrant(Items.MAGIC_KEY), new Integrant(Items.SILVER_INGOT, 50), new Integrant(Items.MALACHITE_INGOT, 35), new Integrant(Items.DAIZUM_INGOT, 20), new Integrant(Items.GOLD(), 1000000000)).setOneTimer());
					game.addEntity(popup);
				}
			});
		}
	}
	
	@Override
	public boolean onInteraction(final Game game, final World world, final Player player) {
		ItemPopup popup = new ItemPopup(Items.MISSING_NOTE, 2, "You found a ", "MISSING NOTE!");
		popup.setObliterationEvent(new Event() {
			
			@Override
			public void run() {
				validate(game, world, player);
			}
		});
		game.addEntity(popup);
		dead = true;
		return true;
	}
	
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		Items.getItemSprite(Items.MISSING_NOTE).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
