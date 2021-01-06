package game.events;

import java.math.BigInteger;

import game.Game;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.entitycomponent.MovementComponent;
import game.worlds.World;

public class FreeMovementEvent extends TileEvent {
	
	@Override
	public void onStepped(final Game game, World world, final Player player) {
		if(player.possessItem(Items.FREE_MOVEMENT_BOOK) == false)
		{
			if(player.canAfford(new BigInteger("1337")))
			{
				MessageBox.addMessage(new MessageBox("Buy", "FREE MOVEMENT", "for 1337 G?").setChoices(new ChoiceEvent() {
					@Override
					public void onChoice(MessageBox box, String choice) {
						if(choice.equals("Yes"))
						{
							ItemPopup popup = new ItemPopup(Items.FREE_MOVEMENT_BOOK, 2, "You received the", "FREE MOVEMENT", "BOOK!");
							game.addEntity(popup);
							((MovementComponent) player.getComponent("movement")).free = true;
							player.removeGold(new BigInteger("1337"));
						}
					}
				}, "Yes", "No"));
			}
			else
			{
				MessageBox.addMessage(new MessageBox("Come back", "when you", "have LEET"));
			}
		}
		else
		{
			final MovementComponent movement = (MovementComponent) player.getComponent("movement");
			String headLine = "-FREE MOVEMENT-";
			MessageBox.addMessage(new MessageBox(headLine, movement.free ? "Disable" : "Enable", "Free Movement?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						movement.free = !movement.free;
						if(movement.free == false)
						{
							player.transform.x = (int) player.transform.x;
							player.transform.y = (int) player.transform.y;
						}
					}
				}
			}, "Yes", "No"));
		}
	}
	
}
