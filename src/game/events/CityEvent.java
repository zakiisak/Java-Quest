package game.events;

import game.Game;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.entity.Worlds;
import game.worlds.Overworld;
import game.worlds.World;

public class CityEvent extends TileEvent {
	
	
	private void tip(Player player)
	{
		if(player.possessItem(Items.EMERALD))
		{
			if(player.possessItem(Items.GLOVES_OF_STRENGTH) == false)
			{
				MessageBox.addMessage("Seek the", "GLOVES OF", "STRENGTH");
			}
			else
			{
				if(Worlds.getWorld(Overworld.class).getEvent(47, 70) instanceof TransferEvent == false)
				{
					MessageBox.addMessage("Lift the", "heavy boulders", "to the south", "in front of", "the cave....");
				}
				else if(player.possessItem(Items.AMETHYST) == false)
				{
					MessageBox.addMessage("Seek the", "mysterious", "creature", "in the south", "cave......");
				}
				else if(player.possessItem(Items.RUBY) == false)
				{
					MessageBox.addMessage("An ancient", "dragon in the", "north west cave", "holds something", "unique....");
				}
				else if(player.possessItem(Items.SAPPHIRE) == false)
				{
					MessageBox.addMessage("Watch the water", "closely at the", "north east bay");
				}
				else if(player.possessItem(Items.TOPAZ) == false)
				{
					if(player.possessItem(Items.CALMING_POTION) == false)
					{
						MessageBox.addMessage("Help Rolpine", "and achieve", "the CALMING", "POTION in the", "north west", "cave");
					}
					else
					{
						MessageBox.addMessage("Bring Wildpine", "to his senses", "with the", "CALMING POTION");
					}
				}
				else if(Worlds.getWorld(Overworld.class).getEvent(49, 134) == null)
				{
					MessageBox.addMessage("Assemble all", "the magical gems", "in the ", "CAVE OF DESTINY");
				}
				else if(Worlds.getWorld(Overworld.class).getEvent(44, 51) == null)
				{
					MessageBox.addMessage("Defeat SHAROG", "the Shadow", "outcast");
				}
				else 
				{
					MessageBox.addMessage("Prevent", "calamity on the", "other side.....");
				}
			}
		}
		else
		{
			if(player.possessItem(Items.SMITH_KEY) == false)
			{
				MessageBox.addMessage("An ancient", "treant to the", "east holds", "the key....");
			}
			else if(player.possessItem(Items.SPEAR_OF_CYLOPIUM) == false)
			{
				MessageBox.addMessage("The SPEAR", "OF CYCLOPIUM", "will devestate", "the CYCLOPS");
			}
			else
			{
				MessageBox.addMessage("Use the SPEAR", "OF CYCLOPIUM", "to annilihate", "the CYCLOPS");
			}
		}
	}
	
	@Override
	public void onStepped(Game game, World world, final Player player) {
		//What happens here?
		MessageBox.addMessage(new MessageBox("City:", "What to do?").setChoices(new MessageBox.ChoiceEvent() {
			@Override
			public void onChoice(MessageBox box, String choice) {
				if(choice.equals("Get a tip"))
				{
					tip(player);
				}
			}
		}, "Get a tip", "Leave"));
	}
	
}
