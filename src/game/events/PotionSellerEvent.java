package game.events;

import java.math.BigInteger;

import game.Game;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.utils.Event;
import game.worlds.World;

public class PotionSellerEvent extends TileEvent {
	
	@Override
	public void onStepped(final Game game, World world, final Player player) {
		MessageBox.addMessage(new MessageBox("--POTION SHOP--"));
		if(player.possessItem(Items.HEALTH_POTION))
		{
			MessageBox.addMessage(new MessageBox("You already", "have a potion."));
		}
		else
		{
			if(player.canAfford(new BigInteger("300")))
			{
				MessageBox.addMessage(new MessageBox("Buy", "HEALING POTION", "for 300 G?").setChoices(new ChoiceEvent() {
					
					@Override
					public void onChoice(MessageBox box, String choice) {
						if(choice.equals("Yes"))
						{
							ItemPopup popup = new ItemPopup(Items.HEALTH_POTION, 2, "You received a", "HEALING POTION!", "", "This potion", "heals 50%", "of max hp");
							popup.setObliterationEvent(new Event() {
								
								@Override
								public void run() {
								}
							});
							game.addEntity(popup);
							player.removeGold(new BigInteger("300"));
						}
					}
				}, "Yes", "No"));
			}
			else
			{
				MessageBox.addMessage(new MessageBox("Come back", "when you", "have 300 G"));
			}
		}
	}
	
}
