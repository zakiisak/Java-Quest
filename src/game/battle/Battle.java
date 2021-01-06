package game.battle;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.AttackAnimation;
import game.entity.Creatures;
import game.entity.DamageNumber;
import game.entity.Drops;
import game.entity.Drops.Drop;
import game.entity.EntityGroup;
import game.entity.HealthBar;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.NetPlayer;
import game.entity.Player;
import game.entitycomponent.StatDrawComponent;
import game.graphics.FontRenderer;
import game.net.BattleNet;
import game.net.PacketBattleIndicator;
import game.net.PacketJoinBattle;
import game.scene.SceneGame;
import game.utils.Color;
import game.utils.Event;
import game.utils.Numbers;
import game.utils.Rand;

public class Battle extends EntityGroup {
	
	public Game game;
	public Player player;
	public Enemy enemy;
	public HealthBar bar;
	public final boolean boss;
	
	public boolean victory = false;
	public PostEvent battlePreFinishEvent;
	public PostEvent battleFinishEvent;
	public String music;
	public boolean fled = false;
	
	public boolean netBattle = false;
	public boolean battleOwner = false;
	public int owner;
	public List<NetPlayer> players = new ArrayList<NetPlayer>();
	public boolean playerBattle = false;
	public NetPlayer playerEnemy;
	
	public int ownerCombatantIdCounter = 2;
	
	public int clientCombatantId = 1;
	public boolean paused = false;
	public TurnComponent turn;
	public Drop monsterDrop;
	public boolean arena;
	public boolean hostArena;
	public boolean disableGameSceneChange = false;
	public boolean doPostBattleEvent = true;
	public boolean restoreHpAfterCombat = true;
	public boolean netJoinBattleIfVictorious = false;
	
	public boolean playerJoinDisabled = false;
	
	public void addPlayer(NetPlayer player)
	{
		if(player.id == game.client.id)
			return;
		for(int i = 0; i < players.size(); i++)
		{
			if(players.get(i).id == player.id)
				return;
		}
		players.add(player);
	}
	
	public Battle(Game game, Enemy enemy, boolean boss, String music, boolean net)
	{
		if(net == false && playerJoinDisabled == false)
		{
			NetPlayer standingPlayer = game.player.input.getStandingPlayer();
			if(standingPlayer != null)
			{
				net = standingPlayer.inBattle;
			}
		}
		this.game = game;
		this.player = game.player;
		this.enemy = enemy;
		this.boss = boss;
		this.music = music;
		
		float hpBarWidth = 300;
		float hpBarHeight = 36;
		bar = new HealthBar(enemy.stats, Game.RES_WIDTH / 2 - hpBarWidth / 2, 70, hpBarWidth, hpBarHeight);
		bar.dead = !player.possessItem(Items.MONSTER_MANUAL);
		
		addComponent(turn = new TurnComponent(game, this, player, enemy, net));
		if(game.backgroundMusic != null)
			game.backgroundMusic.pause();
		if(boss)
		{
			game.getAudio().getMusic(music).setLooping(true);
			game.getAudio().getMusic(music).play(1);
			//Start boss battle music
			enemy.multiplySize(2);
		}
		else
		{
			game.getAudio().getMusic(music).setLooping(true);
			game.getAudio().getMusic(music).play(1);
			//Regular battle music
		}
		monsterDrop = Drops.rollDrop(enemy.getSprite().nameId);
	}
	
	public void reloadNetVersion()
	{
		if(turn != null)
		{
			if(turn.choices != null)
				turn.choices.dead = true;
		}
		removeComponent("turn");
		float hpBarWidth = 300;
		float hpBarHeight = 36;
		bar = new HealthBar(enemy.stats, Game.RES_WIDTH / 2 - hpBarWidth / 2, 70, hpBarWidth, hpBarHeight);
		bar.dead = !player.possessItem(Items.MONSTER_MANUAL);
		
		addComponent(turn = new TurnComponent(game, this, player, enemy, true));
		if(game.backgroundMusic != null)
			game.backgroundMusic.pause();
		if(boss)
		{
			game.getAudio().getMusic(music).setLooping(true);
			game.getAudio().getMusic(music).play(1);
			//Start boss battle music
			enemy.multiplySize(2);
		}
		else
		{
			game.getAudio().getMusic(music).setLooping(true);
			game.getAudio().getMusic(music).play(1);
			//Regular battle music
		}
		
		dead = false;
		monsterDrop = Drops.rollDrop(enemy.getSprite().nameId);
	}
	
	public Battle(Game game, Enemy enemy, boolean boss, String music) {
		this(game, enemy, boss, music, false);
	}
	
	
	public Battle(Game game, Enemy enemy, boolean boss) {
		this(game, enemy, boss, boss ? "boss" : "battle", false);
	}
	
	public void hitOpponent(final BigInteger dmg, final boolean ignoreNet)
	{
		game.addEntity(new AttackAnimation(new Color(1.0f, 1.0f, 1.0f, 0.65f), 0, 40, player.possessItem(Items.BATTLE_SPEED_UPGRADE) ? AttackAnimation.UPGRADE_SPEED : AttackAnimation.DEFAULT_SPEED).setEndReachedEvent(new Event() {
			
			@Override
			public void run() {
				Game.instance.getAudio().getSound("DMGv2").play(1.0f, 0.75f);
				enemy.showOverlay();
				enemy.stats.hp = enemy.stats.hp.subtract(dmg);
				addEntity(new DamageNumber(Game.RES_WIDTH / 2, Game.RES_HEIGHT / 2, dmg));
				if(netBattle == false) turn.doingAnimation = false;
			}
		}).setReversed());
		if(netBattle == false) turn.doingAnimation = true;
		if(ignoreNet == false && netBattle)
		{
			BattleNet.attackEnemy(this, dmg);
		}
	}
	
	public void doPlayerHitAnimation(final Event endReachedEvent)
	{
		AttackAnimation atk = new AttackAnimation(new Color(1.0f, 0, 0, 0.65f), 0, 40, player.possessItem(Items.BATTLE_SPEED_UPGRADE) ? AttackAnimation.UPGRADE_SPEED : AttackAnimation.DEFAULT_SPEED).setEndReachedEvent(new Event() {
			
			@Override
			public void run() {
				Game.instance.getAudio().getSound("DMGv2").play();
				if(endReachedEvent != null)
					endReachedEvent.run();
					
			}
		});
		game.addEntity(atk);
	}
	
	public void doPlayerHealAnimation(final Event endReachedEvent)
	{
		AttackAnimation heal = new AttackAnimation(new Color(0.0f, 1.0f, 0, 0.65f), 0, 40, player.possessItem(Items.BATTLE_SPEED_UPGRADE) ? AttackAnimation.UPGRADE_SPEED : AttackAnimation.DEFAULT_SPEED).setEndReachedEvent(new Event() {
			
			@Override
			public void run() {
				if(endReachedEvent != null)
					endReachedEvent.run();
					
			}
		});
		game.addEntity(heal);
	}
	
	@Override
	public void onAdded(Object owner) {
		super.onAdded(owner);
		Game.instance.battle = this;
		NetPlayer standingPlayer = player.input.getStandingPlayer();
		if(standingPlayer != null && playerJoinDisabled == false)
		{
			if(standingPlayer.inBattle)
			{
				//paused = true;
				PacketJoinBattle jb = new PacketJoinBattle(-1, standingPlayer.id);
				Game.instance.sendTCP(jb);
			}
		}
		player.statDraw.drawHealth = true;
	}
	
	@Override
	public void onKilled() {
		boolean allDead = false;
		if(player.stats.hp.compareTo(BigInteger.ZERO) <= 0)
		{
			allDead = true;
			for(int i = 0; i < players.size(); i++)
			{
				if(players.get(i).stats.hp.compareTo(BigInteger.ZERO) > 0)
				{
					allDead = false;
					break;
				}
			}
		}
		if(allDead == false || (arena == false && hostArena == false))
		{
			
			if(battleFinishEvent != null && doPostBattleEvent)
				battleFinishEvent.onEnded(this);
		}
		if((arena == false && disableGameSceneChange == false && hostArena == false) || (player.killed && hostArena == false && arena == false) || allDead)
		{
			Game.instance.battle = null;
			game.setScene(SceneGame.class);
			if(victory) 
			{
				game.saveGame();
				if(game.backgroundMusic != null)
					game.backgroundMusic.resume();
			}
			else if(fled)
			{
				if(game.backgroundMusic != null)
					game.backgroundMusic.resume();
			}
			player.statDraw.drawHealth = false;
		}
		if(player.stats.hp.compareTo(BigInteger.ZERO) > 0 && restoreHpAfterCombat)
			player.restoreHp();
	}
	
	@Override
	public void tick() {
		if(paused || dead) return;
		if(!victory && !enemy.dead) 
		{
			enemy.tick();
			if(bar.dead == false) bar.tick();
		}
		super.tick();
		if(playerBattle)
		{
			if(players.size() > 0)
				players.clear();
			if(Game.instance.client.isCommunicationNull())
			{
				MessageBox.addMessage("Lost", "connection", "to server.");
				dead = true;
				return;
			}
			if(game.client.getPlayer(playerEnemy.id) == null)
			{
				MessageBox.addMessage("Battling", "player", "disconnected!");
				dead = true;
				return;
			}
			enemy.setSprite(Creatures.getCreatureSprite(Creatures.PLAYER));
			enemy.overlay.sprite = Creatures.getCreatureSpriteOverlay(Creatures.PLAYER);
			enemy.stats.set(playerEnemy.stats);
			enemy.setName(playerEnemy.name);
		}
		else
			enemy.setName(enemy.name);
		for(int i = 0; i < players.size(); i++)
		{
			StatDrawComponent draw = players.get(i).statDraw;
			draw.stats = players.get(i).stats;
			draw.tick();
		}
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		if(paused) return;
		if(!victory && !enemy.dead)
		{
			enemy.draw(batch);
			if(!bar.dead)
			{
				bar.draw(batch);
				bar.drawPost(batch);
			}
		}
		super.drawPost(batch);
		batch.setColor(1, 1, 1, 1);
		
		float xoffset = player.statDraw.width + 12;
		for(int i = 0; i < players.size(); i++)
		{
			StatDrawComponent draw = players.get(i).statDraw;
			draw.drawHealth = true;
			NetPlayer player = players.get(i);
			draw.stats = player.stats;
			draw.draw(batch, xoffset, 0, players.get(i).name);
			xoffset += (draw.width + 12);
		}
		
		if(Game.debug)
		{
			if(turn.turn == clientCombatantId)
				batch.setColor(0.5f, 1, 0.5f, 1);
			else batch.setColor(1, 1, 1, 1);
			FontRenderer.drawWithOutline(batch, Game.baseFont, "turn: " + turn.turn + "\ncombatant: " + clientCombatantId, 10, 10, 2, 1, Color.black);
		}
		
		batch.setColor(1, 1, 1, 1);
	}
	
	
	private boolean preEventCalled = false;
	public void abort()
	{
		if(netBattle && hostArena == false && arena == false)
		{
			PacketBattleIndicator bi = new PacketBattleIndicator(-1, false);
			game.sendTCP(bi);
		}
		game.getAudio().getMusic(music).stop();
		dead = true;
		if(battlePreFinishEvent != null && preEventCalled == false)
		{
			battlePreFinishEvent.onEnded(this);
			preEventCalled = true;
		}
		if(player.killed && hostArena == false && arena == false)
		{
			game.getAudio().getMusic("gameover").play();
			//play death sound
		}
	}


	public void victory() {
		if(victory)
			return;
		game.getAudio().getMusic(music).stop();
		if(restoreHpAfterCombat)
			player.restoreHp();
		player.stats.xp = player.stats.xp.add(enemy.stats.xp);
		if(playerBattle)
		{
			if(player.stats.hp.compareTo(BigInteger.ZERO) <= 0)
			{
				if(player.getGold().compareTo(BigInteger.ONE) == 0)
					player.removeGold(BigInteger.ONE);
				else
					player.removeGold(player.getGold().divide(new BigInteger("2")));
			}
			else
				player.stats.gold = player.stats.gold.add(enemy.stats.gold.divide(Numbers.TWO));
		}
		else
		{
			player.stats.gold = player.stats.gold.add(enemy.stats.gold);
		}
		if(player.canLevelUp())
		{
			((TurnComponent) getComponent("turn")).levelUp = true;
		}
		game.getAudio().getSound("victory").play(1, 1.15f + Rand.rng.nextFloat() * 0.4f);
		victory = true;
		enemy.dead = true;
	}
	
	public static class PostEvent implements Serializable
	{
		public void onEnded(Battle battle) {}
	}
	
	
}
