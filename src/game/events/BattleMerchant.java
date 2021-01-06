package game.events;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Creatures;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.worlds.World;

public class BattleMerchant extends TileEvent {
	
	@Override
	public void onStepped(final Game game, World world, final Player player) {
		super.onStepped(game, world, player);
		MessageBox.addMessage("Welcome!");
		List<String> choicesList = new ArrayList<String>();
		if(player.possessItem(Items.FASTBATTLE_BOOK) == false)
			choicesList.add("FAST BATTLE BOOK");
		else choicesList.add("Sell FAST BATTLE BOOK");
		if(player.possessItem(Items.BATTLE_SPEED_UPGRADE) == false)
			choicesList.add("BATTLE SPEED UPGRADE");
		else choicesList.add("Sell BATTLE SPEED UPGRADE");
		choicesList.add("Cancel");
		if(choicesList.size() > 1)
		{
			String[] choicesArray = new String[choicesList.size()];
			for(int i = 0; i < choicesList.size(); i++)
			{
				choicesArray[i] = choicesList.get(i);
			}
			MessageBox.addMessage("Select a", "thing to buy").setChoices(new ChoiceEvent() {
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("FAST BATTLE BOOK"))
					{
						MessageBox.addMessage("It costs", "5000 G", "Buy?").setChoices(new ChoiceEvent() {
							
							@Override
							public void onChoice(MessageBox box, String choice) {
								if(choice.equals("Yes"))
								{
									if(player.canAfford(new BigInteger("" + 5000)))
									{
										player.removeGold(new BigInteger("" + 5000));
										ItemPopup popup = new ItemPopup(Items.FASTBATTLE_BOOK, 2, "You received", "the FAST BATTLE", "BOOK!", 
												"This gives", "you the ability", "to hold down", "space instead", "of pressing", "it constantly.");
										game.addEntity(popup);
									}
									else
									{
										MessageBox.addMessage("Sorry,", "you don't", "have enough");
									}
								}
							}
						}, "Yes", "No");
					}
					else if(choice.equals("BATTLE SPEED UPGRADE"))
					{
	
						MessageBox.addMessage("It costs", "10000 G", "Buy?").setChoices(new ChoiceEvent() {
							
							@Override
							public void onChoice(MessageBox box, String choice) {
								if(choice.equals("Yes"))
								{
									if(player.canAfford(new BigInteger("" + 10000)))
									{
										player.removeGold(new BigInteger("" + 10000));
										ItemPopup popup = new ItemPopup(Items.BATTLE_SPEED_UPGRADE, 2, "You received", "the BATTLE", "SPEED UPGRADE!", 
												"This upgrades", "the battle", "speed by", "removing", "animations.");
										game.addEntity(popup);
									}
									else
									{
										MessageBox.addMessage("Sorry,", "you don't", "have enough");
									}
								}
							}
						}, "Yes", "No");
					
					}
					else if(choice.equals("Sell FAST BATTLE BOOK"))
					{
						player.stats.gold = player.stats.gold.add(new BigInteger("" + 2500));
						game.getAudio().getSound("sell").play();
						player.removeItem(Items.FASTBATTLE_BOOK);
					}
					else if(choice.equals("Sell BATTLE SPEED UPGRADE"))
					{
						player.stats.gold = player.stats.gold.add(new BigInteger("" + 5000));
						game.getAudio().getSound("sell").play();
						player.removeItem(Items.BATTLE_SPEED_UPGRADE);
					}
				}
			}, choicesArray);
		}
		else
		{
			MessageBox.addMessage("You've bought", "all I have..");
		}
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		onStepped(game, world, player);
		return true;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Creatures.getCreatureSprite(19).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
