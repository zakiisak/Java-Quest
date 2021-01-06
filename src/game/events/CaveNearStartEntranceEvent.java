package game.events;

import game.Game;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.entity.Worlds;
import game.utils.Event;
import game.worlds.CavePassage;
import game.worlds.World;

public class CaveNearStartEntranceEvent extends TileEvent {
	
	@Override
	public void onStepped(Game game, final World world, Player player) {
		if(player.possessItem(Items.GLOVES_OF_STRENGTH))
		{
			MessageBox.addMessage(new MessageBox("There are", "heavy rocks", "blocking the way"));
			MessageBox.addMessage(new MessageBox("Lift them?").setChoices(new MessageBox.ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						MessageBox.addMessage(new MessageBox("You remove them", "with the GLOVES", "OF STRENGTH.").setDoneEvent(new Event() {
							@Override
							public void run() {
								world.putEvent(new TransferEvent(CavePassage.class, 97, 173), (int) transform.x, (int) transform.y);
								Worlds.transferPlayer(CavePassage.class, 97, 173);
							}
						}));
					}
				}
			}, "Yes", "No"));
		}
		else
		{
			//Worlds.transferPlayer(CavePassage.class, 97, 173);
			MessageBox.addMessage(new MessageBox("The entrance", "is blocked by", "heavy rocks..."));
		}
	}
	
}
