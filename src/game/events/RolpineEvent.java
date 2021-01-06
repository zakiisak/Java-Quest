package game.events;

import java.io.Serializable;
import java.math.BigInteger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.battle.Battle;
import game.battle.Enemy;
import game.battle.TurnComponent;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.entity.Worlds;
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.DarkCave;
import game.worlds.World;

public class RolpineEvent extends TileEvent implements Serializable {
	
	public boolean victory = false;
	public boolean potionQuestAccepted = false;
	public boolean wildpineQuestAccepted = false;
	public boolean receivedReward = false;
	
	public RolpineEvent()
	{
		
	}
	
	@Override
	public void onStepped(final Game game, World world, final Player player) {
		if(victory)
		{
			if(player.possessItem(Items.TOPAZ))
			{
				wildpineQuestAccepted = true;
				potionQuestAccepted = true;
			}
			if(wildpineQuestAccepted)
			{
				if(!player.possessItem(Items.TOPAZ))
				{
					if(!player.possessItem(Items.GLOVES_OF_STRENGTH))
					{
						MessageBox.addMessage(new MessageBox("The GLOVES", "OF STRENGTH, are", "very expensive!"));
						MessageBox.addMessage(new MessageBox("I've heard of", "a merchant that", "sell them..."));
					}
					else MessageBox.addMessage(new MessageBox("Seek the cave", "to the north", "east...."));
				}
				else if(!receivedReward)
				{
					MessageBox.addMessage(new MessageBox("I can't thank", "you enough!"));
					MessageBox.addMessage(new MessageBox("I will upgrade", "your health").setDoneEvent(new Event() {
						
						@Override
						public void run() {
							game.getAudio().getSound("item").play();
							player.stats.maxhp = player.stats.maxhp.add(new BigInteger("200"));
							receivedReward = true;
						}
					}));
					MessageBox.addMessage(new MessageBox("You received", "200 max hp"));
				}
				else
				{
					MessageBox.addMessage(new MessageBox("Thanks again!"));
				}
			}
			else if(!potionQuestAccepted)
			{
				MessageBox.addMessage(new MessageBox("You are quite", "strong! In fact", "strong enough"));
				MessageBox.addMessage(new MessageBox("to help me", "with something."));
				MessageBox.addMessage(new MessageBox("My brother has", "gone wild to", "a wildpine"));
				MessageBox.addMessage(new MessageBox("With your", "strength, you", "should be able"));
				MessageBox.addMessage(new MessageBox("to acquire the", "CALMING POTION,", "that lies dormant"));
				MessageBox.addMessage(new MessageBox("deep within", "a cave to the", "northwest"));
				MessageBox.addMessage(new MessageBox("Will you help", "me?").setChoices(new ChoiceEvent() {
					
					@Override
					public void onChoice(MessageBox box, String choice) {
						if(choice.equals("Yes"))
						{
							MessageBox.addMessage(new MessageBox("Thank you!", "Come to me,", "when you have"));
							MessageBox.addMessage(new MessageBox("found it."));
							World northWestCave = Worlds.getWorld(DarkCave.class);
							northWestCave.putEvent(new CalmingPotionEvent(), 49, 49);
							potionQuestAccepted = true;
						}
						else 
						{
							MessageBox.addMessage(new MessageBox("Oh well,", "if you change", "your mind,"));
							MessageBox.addMessage(new MessageBox("Come to me", "again."));
						}
					}
				}, "Yes", "No"));
			}
			else if(player.possessItem(Items.CALMING_POTION) == false)
			{
				MessageBox.addMessage(new MessageBox("Have you found", "the CALMING POTION", "yet? It should"));
				MessageBox.addMessage(new MessageBox("lie within a", "cave to the", "northwest...."));
			}
			else
			{
				MessageBox.addMessage(new MessageBox("As I would", "have expected", "of you"));
				MessageBox.addMessage(new MessageBox("You found the", "CALMING POTION!", "Now I can tell"));
				MessageBox.addMessage(new MessageBox("you more about", "wildpine, my ", "brother.."));
				MessageBox.addMessage(new MessageBox("His power far", "exceeds my own", "and to get"));
				MessageBox.addMessage(new MessageBox("to him, it ", "requires special", "gloves..."));
				MessageBox.addMessage(new MessageBox("The last time", "I saw him,", "he entered the"));
				MessageBox.addMessage(new MessageBox("Great Cave", "north east of", "here."));
				MessageBox.addMessage(new MessageBox("I have faith", "in you!").setDoneEvent(new Event() {
					@Override
					public void run() {
						wildpineQuestAccepted = true;
					}
				}));
			}
		}
		else
		{
			MessageBox.addMessage(new MessageBox("If you can", "defeat me, I", "will give you"));
			MessageBox.addMessage(new MessageBox("something", "valuable..."));
			MessageBox.addMessage(new MessageBox("Battle Rolpine?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						interactionInitiated.run();
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
			Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy("rolpine"));
			battle.battleFinishEvent = battleFinished;
			battle.battlePreFinishEvent = battlePreFinished;
		}
	};
	
	private Battle.PostEvent battlePreFinished = new Battle.PostEvent()  {
		
		public void onEnded(final Battle battle) {
			if(!battle.victory) //Only do this code if the player was victorious
				return;
			battle.enemy.dead = true;
			ItemPopup popup = new ItemPopup(Items.AMETHYST, 5, "You received the", "AMETHYST!");
			popup.obliterationEvent = new Event() {
				@Override
				public void run() {
					((TurnComponent)battle.getComponent("turn")).turn = -2;
				}
			};
			battle.game.addEntity(popup);
			battle.dead = false;
		}
	};
	
	private Battle.PostEvent battleFinished = new Battle.PostEvent()  {
		
		public void onEnded(Battle battle) {
			if(battle.victory)
			{
				victory = true;
				onStepped(Game.instance, Game.instance.world, Game.instance.player);
			}
		}
	};
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Sprite.getSprite("rolpine").render(batch, transform.x * Tile.SIZE + Tile.SIZE, transform.y * Tile.SIZE, -Tile.SIZE, Tile.SIZE);
	}
	
}
