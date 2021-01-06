package game.events;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.battle.Battle;
import game.battle.Battle.PostEvent;
import game.battle.Enemy;
import game.battle.TurnComponent;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.NetPlayer;
import game.entity.Player;
import game.entity.TileEvent;
import game.entitycomponent.SpriteComponent;
import game.net.PacketBattleCommence;
import game.net.PacketBattleIndicator;
import game.net.PacketStatComponent;
import game.scene.SceneGame;
import game.utils.Event;
import game.worlds.World;

public class ArenaEvent extends TileEvent {
	
	//What I'm gonna do is that i'm gonna disable the joined combattant to forward to
	//the next battle. The host player is gonna send a packet when it reaches the next combat and
	//so enable the joined combattants to forward.
	
	
	public static int index = 0;
	public static boolean joinedBattle = false;
	private static NetPlayer standingPlayer;
	public static List<String> enemies = new ArrayList<String>();
	
	public static void updateIndex(Battle battle)
	{
		for(int i = 0; i < enemies.size(); i++)
		{
			if(enemies.get(i).equalsIgnoreCase(battle.enemy.getSprite().nameId))
			{
				index = i;
				return;
			}
		}
	}
	
	public static boolean isLastBattle(Enemy enemy)
	{
		for(int i = 0; i < enemies.size(); i++)
		{
			if(enemies.get(i).equalsIgnoreCase(enemy.getSprite().nameId))
			{
				if(i == enemies.size() - 1)
					return true;
			}
		}
		return false;
	}
	
	private static Battle enterBattles()
	{
		enemies.clear();
		enemies.add("boar");
		enemies.add("knight");
		enemies.add("cyclops");
		enemies.add("rolpine");
		enemies.add("snake_dragon");
		enemies.add("lathros_bane");
		enemies.add("wildpine");
		enemies.add("shadow");
		enemies.add("evil_mirror");
		enemies.add("eye_of_decimation");
		enemies.add("twilight_dragon");
		enemies.add("twilight_golem");
		enemies.add("xilober");
		enemies.add("viggo_mortensen");
		enemies.add("hvasefjas");
		enemies.add("transparent_man");
		
		enemies.add("inv_boar");
		enemies.add("inv_knight");
		enemies.add("inv_cyclops");
		enemies.add("inv_rolpine");
		enemies.add("inv_snake_dragon");
		enemies.add("inv_lathros_bane");
		enemies.add("inv_wildpine");
		enemies.add("inv_evil_mirror");
		enemies.add("inv_eye_of_decimation");
		enemies.add("inv_twilight_dragon");
		enemies.add("inv_twilight_golem");
		enemies.add("inv_xilober");
		enemies.add("the_unknown");
		enemies.add("hvasefjaset");
		
		String enemy = enemies.get(index);
		Battle battle = enemy.equals("hvasefjaset") ? Game.instance.enterCrazyBossBattle(Enemy.getEnemy(enemy), true) :
			Game.instance.enterBossBattle(Enemy.getEnemy(enemy), true);
		battle.playerJoinDisabled = true;
		battle.netJoinBattleIfVictorious = joinedBattle == false;
		battle.disableGameSceneChange = true;
		battle.restoreHpAfterCombat = false;
		if(joinedBattle)
			battle.arena = true;
		else battle.hostArena = true;
		battle.battleFinishEvent = battlePostEvent;
		return battle;
	}
	
	private static void returnToWorld()
	{
		Game game = Game.instance;
		game.player.restoreHp();
		if(game.battle != null)
		{
			game.battle.abort();
			game.battle.dead = true;
		}
		PacketBattleIndicator bi = new PacketBattleIndicator(-1, false);
		game.sendTCP(bi);
		game.setScene(SceneGame.class);
		game.saveGame();
	}
	
	public static PostEvent battlePostEvent = new PostEvent()
	{
		public void onEnded(Battle battle) {
			System.out.println("battle ended! POST EVENT");
			if(battle.victory)
			{
				index++;
				System.out.println("victorY!, index: " + index);
				if(index == enemies.size())
				{
					Player player = Game.instance.player;
					MessageBox box = MessageBox.addMessage("You won!");
					
					String key = "arena_" + player.data.getObject("enemy_multiplier", 0);
					if(player.data.hasKey(key) == false)
					{
						box.setDoneEvent(new Event() {
							
							@Override
							public void run() {
								ItemPopup popup = new ItemPopup(Items.OMNI_GEM, 5, "You received", "an OMNI GEM!");
								popup.setObliterationEvent(new Event() {
									
									@Override
									public void run() {
										returnToWorld();
									}
								});
								Game.instance.addEntity(popup);
							}
						});

						player.data.setBoolean(key, true);
					}
					else
					{
						box.setDoneEvent(new Event() {
							
							@Override
							public void run() {
								returnToWorld();
							}
						});
					}
					
//					MessageBox.addMessage("You won!");
//					MessageBox.addMessage("You sense", "a change", "in the world...            ").setDoneEvent(new Event() {
//						
//						@Override
//						public void run() {
//
//						}
//					});
					
				}
				else 
				{
					Battle battle2 = null;
					if(joinedBattle)
					{
						System.out.println("Joined Battle!");
						/*
						String enemyName = battle.enemy.getSprite().nameId;
						for(int i = 0; i < enemies.size(); i++)
						{
							if(enemies.get(i).equalsIgnoreCase(enemyName.toLowerCase()))
							{
								System.out.println("Changed index from " + index + " to " + (i + 1));
								index = i + 1;
								break;
							}
						}
						battle2 = enterBattles();
						for(int i = 0; i < battle.players.size(); i++)
						{
							if(battle.players.get(i).stats.hp > 0)
								battle2.addPlayer(battle.players.get(i));
						}
						standingPlayer.joinBattle();
						 */
					}
					else if(battle.players.size() > 0)
					{
						battle2 = enterBattles();
						battle2.players = battle.players;
						//Host side
						sendBattleCommences(battle2);
						battle2.clientCombatantId = 1;
						System.out.println("New battle started!");
					}
					else battle2 = enterBattles();
				}
			}
		}
	};
	
	private static void sendBattleCommences(Battle battle)
	{
		Game game = Game.instance;
		int[] players = new int[battle.players.size() + 1];
		players[0] = game.client.id;
		for(int i = 1; i < players.length; i++)
		{
			players[i] = battle.players.get(i - 1).id;
		}
		PacketStatComponent sc = new PacketStatComponent(game.client.id, game.player.stats, game.player.name);
		game.sendTCP(sc);
		int combattantId = 2;
		for(int i = 0; i < battle.players.size(); i++)
		{
			PacketBattleCommence bc = new PacketBattleCommence(game.client.id, battle.players.get(i).id, ((SpriteComponent) battle.enemy.getComponent("sprite")).sprite.nameId, 
					battle.enemy.stats, battle.music, battle.boss, combattantId++, players, battle.turn.turn, battle.playerBattle); 
			bc.force = true;
			bc.arena = true;
			game.sendTCP(bc);
		}
	}
	
	private void arena()
	{
		MessageBox.addMessage("Enter arena?").setChoices(new ChoiceEvent() {
			
			@Override
			public void onChoice(MessageBox box, String choice) {
				if(choice.equals("Yes"))
				{
					enterBattles();
				}
			}
		}, "Yes", "No");
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		super.onStepped(game, world, player);
		index = 0;
		joinedBattle = false;
		standingPlayer = player.input.getStandingPlayer();
		if(standingPlayer == null)
			arena();
		else if(standingPlayer.inBattle)
		{
			MessageBox.addMessage("Join", standingPlayer.name, "in arena?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						joinedBattle = true;
						enterBattles();
						standingPlayer.joinBattle();
					}
					else
					{
						arena();
					}
				}
			}, "Yes", "No");
		}

	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		onStepped(game, world, player);
		return true;
	}
	
}
