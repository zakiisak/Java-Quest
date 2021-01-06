package game.events;

import java.math.BigInteger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.Numbers;
import game.worlds.World;

public class CalmWildPineEvent extends TileEvent {
	
	@Override
	public void onStepped(Game game, World world, final Player player) {
		MessageBox.addMessage(new MessageBox("I will", "upgrade your", "attack"));
		shop(game, world, player, 0);
		
	}
	
	private void shop(final Game game, final World world, final Player player, int choice)
	{
		String suffix = "";
		BigInteger multiplier = player.getEnemyMultiplier(); //NG+
		if(multiplier.compareTo(Numbers.ONE) > 0)
		{
			String multStrForm = multiplier.toString().substring(1);
			for(int i = 0; i < multStrForm.length(); i++)
				suffix += '0';
		}
		MessageBox box = new MessageBox("1000 G", "per attack", "point").setChoices(new ChoiceEvent() {
			
			@Override
			public void onChoice(MessageBox box, String choice) {
				if(choice.equals("Cancel")) return;
				BigInteger attack = new BigInteger(choice.substring(1));
				BigInteger price = attack.multiply(new BigInteger("1000"));
				if(player.canAfford(price))
				{
					player.removeGold(price);
					player.stats.atk_min = player.stats.atk_min.add(attack);
					player.stats.atk_max = player.stats.atk_max.add(attack);
					if(player.canAfford(price))
					{
						int preSelectedChoice = box.choicesEntity.choice;
						shop(game, world, player, preSelectedChoice);
					}
				}
				else
				{
					MessageBox.addMessage(new MessageBox("Sorry, you", "don't have", "enough"));
				}
			}
		}, "+" + (player.getGold().divide(new BigInteger("1000"))), "+2" + suffix, "+5" + suffix, "+10" + suffix, "+50" + suffix, "+100" + suffix, "+1000" + suffix, "Cancel");
		box.preSelectedChoice = choice;
		MessageBox.addMessage(box);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Sprite.getSprite("wildpine").render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
