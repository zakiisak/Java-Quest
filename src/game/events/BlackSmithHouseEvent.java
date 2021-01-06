package game.events;

import java.math.BigInteger;

import game.Game;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.worlds.World;

public class BlackSmithHouseEvent extends TileEvent {
	
	private boolean opened = false;
	
	@Override
	public void onStepped(Game game, final World world, final Player player) {
		if(player.possessItem(Items.SMITH_KEY))
		{
			if(opened == false)
			{
				MessageBox.addMessage(new MessageBox("The door can", "be unlocked with", "the SMITH KEY"));
				MessageBox.addMessage(new MessageBox("Open it?").setChoices(new ChoiceEvent() {
					
					@Override
					public void onChoice(MessageBox box, String choice) {
						if(choice.equals("Yes"))
						{
							opened = true;
							smithShop(world, player);
						}
					}
				}, "Yes", "No"));
				
			}
			else smithShop(world, player);
		}
		else
			MessageBox.addMessage(new MessageBox("The door is", "locked!"));
	}
	
	private void smithShop(final World world, final Player player)
	{
		MessageBox.addMessage(new MessageBox("Welcome!"));
		MessageBox.addMessage(new MessageBox("I sell the ", "spear of ", "cyclopium"));
		if(player.possessItem(Items.SPEAR_OF_CYLOPIUM))
		{
			MessageBox.addMessage(new MessageBox("You already", "have it"));
		}
		else
		{
			if(player.canAfford(new BigInteger("" + 800)))
			{
				MessageBox.addMessage(new MessageBox("Buy for ", "800 G?").setChoices(new ChoiceEvent() {
					
					@Override
					public void onChoice(MessageBox box, String choice) {
						if(choice.equals("Yes"))
						{
							ItemPopup popup = new ItemPopup(Items.SPEAR_OF_CYLOPIUM, 2, "You received the", "SPEAR OF", "CYCLOPIUM!");
							Game.instance.addEntity(popup);
							player.removeGold(new BigInteger("" + 800));
						}
						else
						{
							MessageBox.addMessage(new MessageBox("Too bad", "Come again"));
						}
					}
				}, "Yes", "No"));
			}
			else
			{
				MessageBox.addMessage(new MessageBox("Come back", "when you have", "800G"));
			}
		}
	}
	
}
