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
import game.utils.Event;
import game.worlds.World;

public class SellerEvent extends TileEvent {
	
	private boolean reward = false;
	
	private void bootsOfEscaping(final Game game, final World world, final Player player)
	{
		if(player.canAfford(new BigInteger("400")))
		{
			MessageBox.addMessage(new MessageBox("Buy the BOOTS", "OF ESCAPING", "for 400 G?").setChoices(new ChoiceEvent() {
				@Override
				public void onChoice(MessageBox box, String choice) {
					if (choice.equals("Yes")) {
						ItemPopup popup = new ItemPopup(Items.BOOTS_OF_ESCAPING, 2, "You received", "the BOOTS", "OF ESCAPING!");
						game.addEntity(popup);
						popup.setObliterationEvent(new Event() {
							@Override
							public void run() {
								player.removeGold(new BigInteger("400"));
							}
						});
					} else {
						MessageBox.addMessage(new MessageBox("Too bad"));
					}
				}
			}, "Yes", "No"));
		}
		else
		{
			MessageBox.addMessage(new MessageBox("Come back when", "you have 400 G", ""));
		}
	}
	
	private void monsterManual(final Game game, final World world, final Player player)
	{
		if(player.canAfford(new BigInteger("1000")))
		{
			MessageBox.addMessage(new MessageBox("Buy the ", "MONSTER MANUAL", "for 1000 G?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						ItemPopup popup = new ItemPopup(Items.MONSTER_MANUAL, 2, "You received", "the MONSTER", "MANUAL!");
						game.addEntity(popup);
						popup.setObliterationEvent(new Event() {
							
							@Override
							public void run() {
								player.removeGold(new BigInteger("1000"));
							}
						});
					}
					else
					{
						MessageBox.addMessage(new MessageBox("Too bad"));
					}
				}
			}, "Yes", "No"));
		}
		else
		{
			MessageBox.addMessage(new MessageBox("Come back when", "you have 1000 G", ""));
		}
	}
	
	private void glovesOfStrength(final Game game, final World world, final Player player)
	{

		//Sell power gloves
		if(player.canAfford(new BigInteger("5000")))
		{
			MessageBox.addMessage(new MessageBox("Buy the Gloves", "Of Strength", "for 5000 G?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						ItemPopup popup = new ItemPopup(Items.GLOVES_OF_STRENGTH, 2, "You received", "the GLOVES", "OF STRENGTH!");
						game.addEntity(popup);
						popup.setObliterationEvent(new Event() {
							
							@Override
							public void run() {
								player.removeGold(new BigInteger("5000"));
							}
						});
					}
					else
					{
						MessageBox.addMessage(new MessageBox("Too bad"));
					}
				}
			}, "Yes", "No"));
		}
		else
		{
			MessageBox.addMessage(new MessageBox("Come back when", "you have 5000 G", ""));
		}
	
	}
	
	private void bookOfDamageNumbers(final Game game, final World world, final Player player)
	{

		//Sell damage number book
		if(player.canAfford(new BigInteger("500")))
		{
			MessageBox.addMessage(new MessageBox("Buy the BOOK", "OF DMG NUMBERS", "for 500 G?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						ItemPopup popup = new ItemPopup(Items.BOOK_OF_DAMAGE_NUMBERS, 2, "You received", "the BOOK OF", "DMG NUMBERS!");
						game.addEntity(popup);
						popup.setObliterationEvent(new Event() {
							
							@Override
							public void run() {
								player.removeGold(new BigInteger("500"));
							}
						});
					}
					else
					{
						MessageBox.addMessage(new MessageBox("Too bad"));
					}
				}
			}, "Yes", "No"));
		}
		else
		{
			MessageBox.addMessage(new MessageBox("Come back when", "you have 500 G", ""));
		}
	
	}
	
	@Override
	public void onStepped(final Game game, final World world, final Player player) {
		List<String> choices = new ArrayList<String>();
		if(player.possessItem(Items.BOOTS_OF_ESCAPING) == false)
		{
			choices.add("Boots Of Escaping");
		}
		if(player.possessItem(Items.MONSTER_MANUAL) == false)
		{
			choices.add("Monster Manual");
		}
		if(player.possessItem(Items.GLOVES_OF_STRENGTH) == false)
		{
			choices.add("Gloves of Strength");
		}
		if(player.possessItem(Items.BOOK_OF_DAMAGE_NUMBERS) == false)
		{
			choices.add("Book of Damage Numbers");
		}
		choices.add("Cancel");
		if(choices.size() == 1) 
		{
			if(reward == false)
			{
				MessageBox.addMessage("Here's a gift", "for being my", "best costumer").setDoneEvent(new Event() {
					
					@Override
					public void run() {
						ItemPopup popup = new ItemPopup(Items.CURSED_RING, 2, "You received", "the CURSED", "RING!", "With this, you", "can now press", "M to enter", "combat!");
						game.addEntity(popup);
						reward = true;
						
					}
				});
			}
			else
				MessageBox.addMessage(new MessageBox("I'm all out!", "Come again in", "the future"));
		}
		else
		{
			String[] choicesArray = new String[choices.size()];
			for(int i = 0; i < choices.size(); i++)
			{
				choicesArray[i] = choices.get(i);
			}
			MessageBox.addMessage(new MessageBox("Select something", "to buy....").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Boots Of Escaping"))
					{
						bootsOfEscaping(game, world, player);
					}
					else if(choice.equals("Monster Manual"))
					{
						monsterManual(game, world, player);
					}
					else if(choice.equals("Gloves of Strength"))
					{
						glovesOfStrength(game, world, player);
					}
					else if(choice.equals("Book of Damage Numbers"))
					{
						bookOfDamageNumbers(game, world, player);
					}
				}
			}, choicesArray));
		}
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		onStepped(game, world, player);
		return true;
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		Creatures.getCreatureSprite(2).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
