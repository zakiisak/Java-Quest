package game.battle;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.Input;
import game.entity.AttackAnimation;
import game.entity.Choices;
import game.entity.DamageNumber;
import game.entity.EntityComponent;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.NetPlayer;
import game.entity.Player;
import game.net.BattleNet;
import game.net.PacketStatComponent;
import game.utils.Color;
import game.utils.Event;
import game.utils.Numbers;
import game.utils.Rand;

public class TurnComponent extends EntityComponent {
	
	public int turn = 1;
	
	public Game game;
	public Player player;
	public Enemy enemy;
	public boolean doingAnimation;
	
	public Battle battle;
	public int action = 0;
	public BigInteger dmgTemp = new BigInteger("0");
	public int playerDamaging;
	
	public List<BattleAction> actions = new ArrayList<BattleAction>();
	public Choices choices;
	
	private int lastTurn = 0;
	
	public TurnComponent(Game game, Battle battle, Player player, Enemy enemy, boolean net) 
	{
		this.game = game;
		this.battle = battle;
		this.player = player;
		this.enemy = enemy;
		if(net == false)
			setActions();
		lastTurn = turn;

	}
	
	private void setChoices()
	{
		if(choices != null)
			choices.dead = true;
		actions.clear();
		
		actions.add(Actions.attack);
		if(player.possessItem(Items.HEALTH_POTION))
			actions.add(Actions.potion);
		if(player.possessItem(Items.BOOTS_OF_ESCAPING) && battle.boss == false && battle.netBattle == false)
			actions.add(Actions.escape);
		
		actions.add(Actions.guard);
		
		if(player.possessItem(Items.BOOK_OF_CONCEDE))
			actions.add(Actions.concede);
		
		String[] actionNames = new String[actions.size()];
		for(int i = 0; i < actions.size(); i++)
		{
			actionNames[i] = actions.get(i).getName();
		}
		int lastHoldDownCounter = choices != null ? choices.holdDownCounter : 0;
		choices = new Choices(new Choices.Event() {
			
			@Override
			public void onChoice(Choices choices, String choice) {
				for(int i = 0; i < actions.size(); i++)
				{
					BattleAction action = actions.get(i);
					if(choice.equals(action.getName().replaceAll("§", "")))
					{
						action.onSelected(game, TurnComponent.this, battle, player);
						TurnComponent.this.action = i;
						break;
					}
				}
				choices.dead = true;
			}
		}, actionNames);
		choices.choice = action;
		choices.holdDownCounter = lastHoldDownCounter;
		choices.setLocation(10, Game.RES_HEIGHT - choices.size.height - 8 - 40 - 8 - 4 - (battle.netBattle ? 20 : 4));
		choices.tickable = false;
		choices.choicesRecursive = false;
		choices.soundsEnabled = false;
		if(player.possessItem(Items.FASTBATTLE_BOOK))
			choices.keyJustCheck = false;
	}
	
	private void setActions()
	{
		setChoices();
		battle.addEntity(choices);
	}
	
	public void renewActions()
	{
		int actionCount = 2;
		if(player.possessItem(Items.HEALTH_POTION))
			actionCount++;
		if(player.possessItem(Items.BOOTS_OF_ESCAPING) && battle.boss == false && battle.netBattle == false)
			actionCount++;
		if(player.possessItem(Items.BOOK_OF_CONCEDE))
			actionCount++;
		if(actions.size() != actionCount)
		{
			action = 0;
		}
		setActions();
	}
	
	@Override
	public String getName() {
		return "turn";
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}
	
	private boolean spacePressedForPreEvent = false;
	protected boolean levelUp = false;
	private boolean spacePressedForLevelUp = false;
	private boolean dropFinished = false;
	private boolean dropLock = false;
	
	@Override
	public void tick() {
		if(MessageBox.current() != null) 
		{
			if(choices != null) choices.dead = true;
			return;
		}
		/*
		if(battle.hostArena && battle.dead == false)
		{
			if(player.killed || player.stats.hp <= 0)
			{
				battle.disableGameSceneChange = false;
				battle.arena = false;
				battle.hostArena = false;
				battle.victory = false;
				player.stats.hp = 0;
				player.killed = true;
				battle.abort();
				int id = -1;
				for(int i = 0; i < battle.players.size(); i++)
				{
					if(battle.players.get(i).stats.hp > 0)
					{
						id = battle.players.get(i).id;
						break;
					}
				}
				if(id != -1)
				{
					PacketArenaBattleUpdate abu = new PacketArenaBattleUpdate(game.client.id, id, "newowner", 0);
					game.sendTCP(abu);
				}
				return;
			}
		}
		*/
		/*
		if(battle.playerBattle)
		{
			if(battle.playerEnemy.inBattle == false)
			{
				battle.abort();
				return;
			}
		}
		*/
		
		if(battle.arena || battle.hostArena)
		{
			if(player.stats.hp.compareTo(Numbers.ZERO) <= 0)
			{
				boolean allDead = true;
				for(int i = 0; i < battle.players.size(); i++)
				{
					if(battle.players.get(i).stats.hp.compareTo(Numbers.ZERO) > 0)
					{
						allDead = false;
						break;
					}
				}
				if(allDead)
				{
					battle.abort();
					return;
				}
			}
		}
		
		
		if(turn == battle.clientCombatantId) renewActions();
		
		
		if(turn == -2 && battle.arena == false)
		{
			if((player.possessItem(Items.FASTBATTLE_BOOK) ? Gdx.input.isKeyPressed(Keys.SPACE) : Input.keyJustPressed(Keys.SPACE)))
			{
				battle.abort();
			}
		}
		
//		if(battle.netBattle && battle.hostArena && turn > 1)
//		{
//			/*
//			for(int i = 0; i < battle.players.size(); i++)
//			{
//				if(battle.players.get(i).stats.hp <= 0)
//				{
//					battle.players.remove(i);
//					i--;
//				}
//			}
//			*/
//			int playerIndex = turn - 2;
//			if(playerIndex < battle.players.size())
//			{
//				if(battle.players.get(playerIndex).stats.hp <= 0 || battle.players.get(playerIndex).inBattle == false)
//				{
//					turn++;
//					BattleNet.shiftTurn(battle);
//				}
//			}
//		}
	
		
		//Victory
		if(turn == 0)
		{
			if(battle.arena)
			{
				if((player.possessItem(Items.FASTBATTLE_BOOK) ? Gdx.input.isKeyPressed(Keys.SPACE) : Input.keyJustPressed(Keys.SPACE)))
				{
					if(battle.victory == false)
						battle.victory();
				}
			}
			else if((player.killed || player.stats.hp.compareTo(Numbers.ZERO) <= 0) && battle.hostArena == false)
			{
				battle.abort();
				return;
			}
			else if((player.possessItem(Items.FASTBATTLE_BOOK) ? Gdx.input.isKeyPressed(Keys.SPACE) : Input.keyJustPressed(Keys.SPACE)))
			{
				if(battle.victory)
				{
					if(game.getAudio().getSound("victory").isPlaying() == false && game.getAudio().getSound("levelup").isPlaying() == false)
					{
						if(levelUp && !spacePressedForLevelUp)
						{
							for(int i = 0; i < 100000; i++)
							{
								if(battle.player.checkLevelUp())
								{
									game.getAudio().get("levelup").play(1, 1.25f + Rand.rng.nextFloat() * 0.25f);
								}
								else break;
							}
							spacePressedForLevelUp = true;
						}
						else if(battle.monsterDrop != null && dropFinished == false)
						{
							if(dropLock == false)
							{
								ItemPopup popup = new ItemPopup(battle.monsterDrop.getItem(), new BigInteger("" + battle.monsterDrop.getCount()), 5, "You received " + battle.monsterDrop.getCount(), Items.getItemName(battle.monsterDrop.getItem()) + (battle.monsterDrop.getCount()> 1 ? "S" : ""));
								popup.setObliterationEvent(new Event() {
									@Override
									public void run() {
										dropFinished = true;
									}
								});
								game.addEntity(popup);
								dropLock = true;
							}
						}
						else if(battle.battlePreFinishEvent != null && !spacePressedForPreEvent)
						{
							spacePressedForPreEvent = true;
						}
						else
						{
							battle.abort();
							turn = -1;
						}
					}
					return;
				}
				else
				{
					if(player.stats.hp.compareTo(Numbers.ZERO) <= 0 && battle.hostArena == false)
					{
						battle.abort();
						return;
					}
					else
						battle.victory();
				}
			}
		}
		//Player
		else if(battle.netBattle ? turn == battle.clientCombatantId : turn == 1)
		{
			if(player.stats.hp.compareTo(Numbers.ZERO) <= 0)
			{
				if(battle.playerBattle)
				{
					turn = 0;
					BattleNet.shiftTurn(battle);
					battle.abort();
				}
				else
				{
					choices.dead = true;
					turn++;
					BattleNet.shiftTurn(battle);
				}
			}
			else if(!doingAnimation)
			{
				//Horrible solution (setting tickable to true and false), but I'm tired right now
				choices.tickable = true;
				choices.tick();
				choices.tickable = false;
				this.action = choices.choice;
			}
		}
		//Enemy
		if(battle.netBattle)
		{
			if((battle.playerBattle ? turn == 3 : turn == battle.players.size() + 2) && battle.battleOwner == true && !doingAnimation)
			{
				if(battle.playerBattle)
				{
					turn = 1;
					BattleNet.shiftTurn(battle);
				}
				else
				{
					doingAnimation = true;
					game.addEntity(new AttackAnimation(new Color(1, 0, 0, 0.65f), 0, 40, player.possessItem(Items.BATTLE_SPEED_UPGRADE) ? AttackAnimation.UPGRADE_SPEED : AttackAnimation.DEFAULT_SPEED).setEndReachedEvent(animationEnd));
					dmgTemp = enemy.stats.atk();
					
					List<Integer> playersLeft = new ArrayList<Integer>();
					for(int i = 0; i < battle.players.size(); i++)
					{
						NetPlayer player = battle.players.get(i);
						if(player.stats.hp.compareTo(Numbers.ZERO) > 0)
							playersLeft.add(i);
					}
					if(player.stats.hp.compareTo(Numbers.ZERO) > 0)
						playersLeft.add(-1);
					
					if(playersLeft.size() > 0)
					{
						playerDamaging = playersLeft.get(Rand.rng.nextInt(playersLeft.size()));
						if(playerDamaging == -1)
							playerDamaging = game.client.id;
						else playerDamaging = battle.players.get(playerDamaging).id;
						BattleNet.attackPlayer(battle, playerDamaging, dmgTemp);
					}
					else
					{
						player.killed = true;
						turn = 0;
						BattleNet.shiftTurn(battle);
					}
				}
			}
		}
		else if(turn == 2)
		{
			if((player.possessItem(Items.FASTBATTLE_BOOK) ? Gdx.input.isKeyPressed(Keys.SPACE) : Input.keyJustPressed(Keys.SPACE)) && !doingAnimation)
			{
				game.addEntity(new AttackAnimation(new Color(1, 0, 0, 0.65f), 0, 40, player.possessItem(Items.BATTLE_SPEED_UPGRADE) ? AttackAnimation.UPGRADE_SPEED : AttackAnimation.DEFAULT_SPEED).setEndReachedEvent(animationEnd));
				doingAnimation = true;
			}
		}
		if(lastTurn != turn)
		{
			game.sendTCP(new PacketStatComponent(game.client.id, game.player.stats, game.player.name));
		}
		lastTurn = turn;
	}
	
	public Event animationEnd = new Event()
	{
		public void run()
		{
			choices.dead = true;
			//Only happens on the player having the turn
			if(battle.netBattle ? turn == battle.clientCombatantId : turn == 1)
			{
				//Damage enemy
				Game.instance.getAudio().getSound("DMGv2").play(1.0f, 0.75f);
				enemy.showOverlay();
				BigInteger atk = dmgTemp;
				enemy.stats.hp = enemy.stats.hp.subtract(atk);
				battle.addEntity(new DamageNumber(Game.RES_WIDTH / 2, Game.RES_HEIGHT / 2, atk));
				if(enemy.stats.hp.compareTo(Numbers.ZERO) <= 0)
				{
					turn = 0;
				}
				else
				{
//					setChoices();
					turn++;
				}
				BattleNet.shiftTurn(battle);
				doingAnimation = false;
				return;
			}
			//Only server side if mp
			if(battle.netBattle ? turn == battle.players.size() + 2 && battle.battleOwner == true : turn == 2)
			{
				if(battle.netBattle)
				{
					System.out.println("DOING THUS: " + battle.clientCombatantId);
					Game.instance.getAudio().getSound("DMGv2").play(1.0f, 1.0f);
					if(playerDamaging == game.client.id)
					{
						player.stats.hp = player.stats.hp.subtract(dmgTemp);
						if(player.stats.hp.compareTo(Numbers.ZERO) <= 0)
						{
							player.killed = true;
						}
					}
					else
					{
						for(int i = 0; i < game.client.clients.size(); i++)
						{
							NetPlayer player = game.client.clients.get(i);
							if(player.id == playerDamaging)
							{
								player.stats.hp = player.stats.hp.subtract(dmgTemp);
							}
						}
					}
					
					//Check if any player is still alive
					boolean alive = battle.player.stats.hp.compareTo(Numbers.ZERO) > 0;
					for(int i = 0; i < battle.players.size(); i++)
					{
						if(battle.players.get(i).stats.hp.compareTo(Numbers.ZERO) > 0)
						{
							alive = true;
						}
					}
					
					if(alive)
						turn = 1;
					else 
					{
						turn = 0;
						player.killed = true;
					}
					BattleNet.shiftTurn(battle);
				}
				else
				{
					//Damage player
					Game.instance.getAudio().getSound("DMGv2").play(1.0f, 1.0f);
					player.stats.hp = player.stats.hp.subtract(enemy.stats.atk());
					if(player.stats.hp.compareTo(Numbers.ZERO) <= 0)
					{
						player.killed = true;
						battle.abort();
					}
					turn = 1;
					renewActions();
				}
			}
			doingAnimation = false;
		}
	};

	@Override
	public void draw(SpriteBatch batch) {

	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		/*
		if(turn == 1 && !doingAnimation && actions.size() > 1)
		{
			System.out.println("Drawing");
			float lineHeight = Game.baseFont.lineHeight * 2 * 1.1f;
			float height = lineHeight * actions.size();
			
			float xp = 4;
			float yp = Game.RES_HEIGHT - 40 - 16 - 104;
			float width = 70;
			TiledRenderer.drawTiledBox(batch, Sprite.getSprite("statborder"), xp, yp, width, height, 1);
			for(int i = 0; i < actions.size(); i++)
			{
				BattleAction action = actions.get(i);
				FontRenderer.draw(batch, Game.baseFont, action.getName(), xp + width / 2 - Game.baseFont.getWidth(action.getName()), yp + 4);
				yp += lineHeight;
			}
		}
		*/
		super.drawPost(batch);
//		if(actions.size() > 1)
//		{
//		}
	}
	
	@Override
	public boolean isVisible() {
		return true;
	}
	
	@Override
	public boolean isPostVisible() {
		return true;
	}

}
