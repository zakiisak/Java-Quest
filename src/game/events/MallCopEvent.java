package game.events;

import java.io.Serializable;
import java.math.BigInteger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.battle.Battle;
import game.battle.Enemy;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.Event;
import game.utils.Numbers;
import game.worlds.World;

public class MallCopEvent extends TileEvent implements Serializable {
	
	public boolean victory = false;
	
	public MallCopEvent()
	{
		
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
		MessageBox box = new MessageBox("+1 HP", "for 400 G").setChoices(new ChoiceEvent() {
			
			@Override
			public void onChoice(MessageBox box, String choice) {
				if(choice.equals("Cancel"))
					return;
				BigInteger hp = new BigInteger(choice.substring(1));
				BigInteger price = hp.multiply(new BigInteger("400"));
				if(player.canAfford(price))
				{
					player.stats.maxhp = player.stats.maxhp.add(hp);
					player.stats.hp = player.stats.hp.add(hp);
					player.removeGold(price);
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
		}, "+" + (player.getGold().divide(new BigInteger("400"))), "+5" + suffix, "+20" + suffix, "+50" + suffix, "+100" + suffix, "+500" + suffix, "+1000" + suffix, "Cancel");
		box.preSelectedChoice = choice;
		MessageBox.addMessage(box);
	}
	
	@Override
	public void onStepped(final Game game, World world, final Player player) {
		if(victory)
		{
			MessageBox.addMessage(new MessageBox("I can upgrade", "your hp"));
			shop(game, world, player, 0);
		}
		else
		{
			MessageBox.addMessage(new MessageBox("If you can", "defeat me, I", "will help you"));
			MessageBox.addMessage(new MessageBox("Battle", "Mall Cop?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						MessageBox.addMessage(new MessageBox("Mall mall cop", "Paul Blart", "Mall mall cop"));
						MessageBox.addMessage(new MessageBox("Paul Blart", "mall mall cop", "Paul Blart"));
						MessageBox.addMessage(new MessageBox("mall mall cop", "Paul Blart").setDoneEvent(interactionInitiated));
					}
				}
			}, "Yes", "No"));
		}
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		return false;
	}
	
	private Event interactionInitiated = new Event()  {
		
		@Override
		public void run() {
			Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy("mall_cop"));
			battle.battleFinishEvent = battleFinished;
		}
	};
	
	private Battle.PostEvent battleFinished = new Battle.PostEvent()  {
		
		public void onEnded(Battle battle) {
			if(battle.victory)
				victory = true;
		}
	};
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Sprite.getSprite("mall_cop").render(batch, transform.x * Tile.SIZE + Tile.SIZE, transform.y * Tile.SIZE, -Tile.SIZE, Tile.SIZE);
	}
	
}
