package game.net;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import game.Game;
import game.battle.Battle;
import game.battle.Enemy;
import game.entity.Creatures;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.NetPlayer;
import game.entity.Worlds;
import game.entitycomponent.SpriteComponent;
import game.events.ArenaEvent;
import game.utils.Event;
import game.utils.Numbers;
import game.worlds.World;

//TODO Finish up the player battle system
//To do it:
//Figure out a packet way to make two players able to join battles with each other,
//but making the playerBattle flag true. I'm thinking that the player hosting the battle,
//should wait in some event in the world for a specific packet. And when the hosting player
//receives this packet, it begins a battle, from the information contained in the requestPlayerBattle
//packet, and sends back a playerCommencePacket. This method reduces the number of new packets to create by one,
//because we can reuse the PacketBattleCommence class. This way we just have to make one new packet. The components
//contained in this packet should be the id of the player that requests the battle, a StatComponent field and a name.

public class NetClient extends Listener {
	public static final int MAX_NETWORK_TRANSFER_SIZE = 768 * 1000;
	
	public List<NetPlayer> clients = new ArrayList<NetPlayer>();
	public int id;
	
	private boolean loggedIn = false;
	private Client client;
	
	public void stop()
	{
		if(client != null)
			client.stop();
		for(int i = 0; i < clients.size(); i++)
		{
			clients.get(i).dead = true;
			clients.remove(i);
			i--;
		}
		this.id = -1;
	}
	
	public void sendStats()
	{
		Game game = Game.instance;
		PacketStatComponent sc = new PacketStatComponent(game.client.id, game.player.stats, game.player.name);
		game.sendTCP(sc);
	}
	
	public void connected(Connection c) {
		loggedIn = true;
		sendStats();
	}
	public void received(Connection c, Object o) {
		if(o instanceof PacketPlayerList)
		{
			final PacketPlayerList list = (PacketPlayerList) o;
			this.id = list.yourId;
			for(int i = 0; i < list.players.length; i++)
			{
				NetPlayer player = new NetPlayer(list.players[i]);
				clients.add(player);
			}
			for(int i = 0; i < clients.size(); i++)
			{
				NetPlayer player = clients.get(i);
				Worlds.worlds[player.world].addEntity(player);
			}
			sendStats();
		}
		else if(o instanceof PacketTransform)
		{
			PacketTransform transform = (PacketTransform) o;
			boolean foundPlayer = false;
			for(int i = 0; i < clients.size(); i++)
			{
				final NetPlayer player = clients.get(i);
				if(player.id == transform.id)
				{
					if(foundPlayer)
					{
						//Remove player
						clients.get(i).dead = true;
						clients.remove(i);
						i--;
						continue;
					}
					foundPlayer = true;
					player.transform.set(transform.x, transform.y);
					player.data.setInteger("timeoutTime", 60*60);
					if(player.world != transform.world)
					{
						final int currentWorld = player.world;
						Game.instance.netActions.add(new NetAction() {
							@Override
							public void run(Game game, NetClient client) {
								World world = Worlds.worlds[currentWorld];
								world.entities.remove(player);
							}
						});
						player.dead = true;
						player.world = transform.world;
						Worlds.worlds[transform.world].addEntity(player);
					}
				}
			}
			if(foundPlayer == false)
			{
				NetPlayer player = new NetPlayer();
				player.transform.set(transform.x, transform.y);
				player.world = transform.world;
				player.id = transform.id;
				player.data.setInteger("timeoutTime", 60*60);
				clients.add(player);
				Worlds.worlds[transform.world].addEntity(player);
				sendStats();
			}
		}
		else if(o instanceof PacketBattleIndicator)
		{
			PacketBattleIndicator bi = (PacketBattleIndicator) o;
			for(int i = 0; i < clients.size(); i++)
			{
				if(clients.get(i).id == bi.id)
				{
					clients.get(i).inBattle = bi.inBattle;
					break;
				}
			}
		}
		else if(o instanceof PacketLogout)
		{
			PacketLogout pl = (PacketLogout) o;
			for(int i = 0; i < clients.size(); i++)
			{
				if(clients.get(i).id == pl.id)
				{
					clients.get(i).dead = true;
					clients.remove(i);
					break;
				}
			}
		}
		else if(o instanceof PacketJoinBattle)
		{
			final PacketJoinBattle jb = (PacketJoinBattle) o;
			Game.instance.netActions.add(new NetAction() {
				
				@Override
				public void run(Game game, NetClient client) {
					Battle battle = game.battle;
					if(battle == null)
					{
						//Abort
						return;
					}
					if((battle.victory || battle.enemy.stats.hp.compareTo(Numbers.ZERO) <= 0) && battle.arena == false && battle.hostArena == false)
						return;
					if(battle.playerBattle)
						return;
					if(battle.netBattle && battle.battleOwner == false)
					{
						//Client battle
						jb.toId = battle.owner;
						game.sendTCP(jb);
						return;
					}
					battle.netBattle = true;
					battle.owner = NetClient.this.id;
					battle.battleOwner = true;
					
					for(int i = 0; i < clients.size(); i++)
					{
						if(clients.get(i).id == jb.fromId)
						{
							battle.addPlayer(clients.get(i));
							break;
						}
					}
					int[] players = new int[battle.players.size() + 1];
					players[0] = NetClient.this.id;
					for(int i = 1; i < players.length; i++)
					{
						players[i] = battle.players.get(i - 1).id;
					}
					BattleNet.playerJoin(battle, jb.fromId);
					PacketStatComponent sc = new PacketStatComponent(NetClient.this.id, game.player.stats, game.player.name);
					game.sendTCP(sc);
					PacketBattleCommence bc = new PacketBattleCommence(NetClient.this.id, jb.fromId, ((SpriteComponent) battle.enemy.getComponent("sprite")).sprite.nameId, 
							battle.enemy.stats, battle.music, battle.boss, battle.players.size() + 1, players, battle.turn.turn, battle.playerBattle); 
					game.sendTCP(bc);
				}
			});
		}
		else if(o instanceof PacketBattleCommence)
		{
			final PacketBattleCommence bc = (PacketBattleCommence) o;
			Game.instance.netActions.add(new NetAction() {
				
				@Override
				public void run(Game game, NetClient client) {
					
					if(game.battle != null)
					{
						if(game.battle.battleOwner || (game.battle.owner == bc.fromId && bc.force == false) || game.battle.hostArena)
							return;
					}
					System.out.println("BCENEMY CODE: " + bc.enemyCodeName);
					Enemy enemy = null;
					if(bc.playerBattle)
					{
						enemy = new Enemy(Creatures.getCreatureSprite(Creatures.PLAYER), Creatures.getCreatureSpriteOverlay(Creatures.PLAYER), 
								Numbers.ZERO, Numbers.ZERO, Numbers.ZERO, Numbers.ZERO, Numbers.ZERO);
						getPlayer(bc.fromId).stats = bc.stats;
						enemy.stats = bc.stats;
						enemy.stats.gold = Numbers.ZERO;
						enemy.stats.xp = Numbers.ZERO;
					}
					else
					{
						enemy = Enemy.getEnemy(bc.enemyCodeName);
					}
					enemy.stats = bc.stats;
					Battle battle = Game.instance.battle != null && bc.force == false ? Game.instance.battle : new Battle(Game.instance, enemy, bc.boss, bc.battleMusic, true);				
					battle.enemy = enemy;
					battle.reloadNetVersion();
					battle.owner = bc.fromId;
					battle.turn.turn = bc.turn;
					battle.netBattle = true;
					battle.clientCombatantId = bc.combatantId;
					battle.paused = false;
					battle.playerBattle = bc.playerBattle;
					battle.playerEnemy = getPlayer(bc.fromId);
					battle.dead = false;
					battle.victory = false;
					battle.enemy.dead = false;
					battle.arena = bc.arena;
					if(battle.arena)
					{
						battle.disableGameSceneChange = true;
						battle.restoreHpAfterCombat = false;
						if(ArenaEvent.isLastBattle(battle.enemy))
						{
							ArenaEvent.index = ArenaEvent.enemies.size() - 1;
							battle.battleFinishEvent = ArenaEvent.battlePostEvent;
						}
						System.out.println("battle ARENA!");
					}
					
					if(battle.playerBattle == false)
					{
						for(int i = 0; i < bc.players.length; i++)
						{
							NetPlayer player = getPlayer(bc.players[i]);
							if(player != null)
								battle.addPlayer(player);
						}
						for(int i = 0; i < clients.size(); i++)
						{
							NetPlayer player = clients.get(i);
							if(player.id == bc.fromId)
							{
								battle.addPlayer(player);
								break;
							}
						}
						if(bc.turn == bc.combatantId)
						{
							battle.turn.renewActions();
						}
					}
					else
					{
//						battle.clientCombatantId = 2;
					}
					PacketStatComponent sc = new PacketStatComponent(NetClient.this.id, game.player.stats, game.player.name);
					game.sendTCP(sc);
					if(game.battle == null || bc.force)
					{
						if(bc.force)
							game.deleteAllBattles();
						game.enterBattle(battle, bc.force);
					}
				}
			});
		}
		else if(o instanceof PacketBattleUpdate)
		{
			final Object data = ((PacketBattleUpdate) o).data;
//			System.out.println("[client] received bu: " + data);
			Game.instance.netActions.add(new NetAction() {
				@Override
				public void run(Game game, NetClient client) {
					Battle battle = game.battle;
					if(battle == null)
						return;
					if(data instanceof String)
					{
						String s = (String) data;
						if(s.startsWith("edmg"))
						{
							final BigInteger dmg = new BigInteger(s.substring(4));
							if(battle.playerBattle)
							{
								battle.doPlayerHitAnimation(new Event() {
									
									@Override
									public void run() {
										Game.instance.player.stats.hp = Game.instance.player.stats.hp.subtract(dmg);
										if(Game.instance.player.stats.hp.compareTo(Numbers.ZERO) <= 0)
											Game.instance.player.killed = true;
										sendStats();
									}
								});
							}
							else
								battle.hitOpponent(dmg, true);
						}
						else if(s.startsWith("pdmg"))
						{
							String[] splitter = s.split("_");
							final int playerId = Integer.parseInt(splitter[1]);
							final BigInteger dmg = new BigInteger(splitter[2]);
							battle.doPlayerHitAnimation(new Event() {
								@Override
								public void run() {
									if(playerId == NetClient.this.id)
									{
										Game.instance.player.stats.hp = Game.instance.player.stats.hp.subtract(dmg);
										if(Game.instance.player.stats.hp.compareTo(Numbers.ZERO) <= 0)
											Game.instance.player.killed = true;
									}
									else
									{
										for(int i = 0; i < clients.size(); i++)
										{
											if(clients.get(i).id == playerId)
											{
												clients.get(i).stats.hp = clients.get(i).stats.hp.subtract(dmg);
												break;
											}
										}
									}
									
								}
							});
						}
						else if(s.startsWith("heal"))
						{
							String[] splitter = s.split("_");
							final int playerId = Integer.parseInt(splitter[1]);
							final BigInteger heal = new BigInteger(splitter[2]);
							battle.doPlayerHealAnimation(new Event() {
								@Override
								public void run() {
									for(int i = 0; i < clients.size(); i++)
									{
										if(clients.get(i).id == playerId)
										{
											NetPlayer player = clients.get(i); 
											player.stats.hp = player.stats.hp.add(heal);
											if(player.stats.hp.compareTo(player.stats.maxhp) > 0)
												player.stats.hp = player.stats.maxhp;
											break;
										}
									}
								}
							});
						}
						else if(s.startsWith("turn"))
						{
							int turn = Integer.parseInt(s.substring(4));
							if(turn == battle.clientCombatantId)
								battle.turn.renewActions();
							battle.turn.turn = turn;
							battle.turn.doingAnimation = false;
							PacketStatComponent sc = new PacketStatComponent(NetClient.this.id, game.player.stats, game.player.name);
							game.sendTCP(sc);
						}
						else if(s.startsWith("join"))
						{
							int playerId = Integer.parseInt(s.substring(4));
							NetPlayer player = getPlayer(playerId);
							if(player != null)
							{
								battle.addPlayer(player);
							}
						}
					}
				}
			});
			
		}
		else if(o instanceof PacketPlayerBattleRequest)
		{
			final PacketPlayerBattleRequest pbr = (PacketPlayerBattleRequest) o;
			Game.instance.netActions.add(new NetAction() {
				@Override
				public void run(Game game, NetClient client) {
					final NetPlayer netPlayer = getPlayer(pbr.fromId);
					netPlayer.stats = pbr.stats;
					if(netPlayer == null || game.battle != null)
						return;
					if(netPlayer.inBattle)
						return;
					MessageBox.addMessage(netPlayer.name, "wants to battle", "with you.");
					MessageBox.addMessage("Accept?").setChoices(new ChoiceEvent() {
						
						@Override
						public void onChoice(MessageBox box, String choice) {
							if(choice.equals("Yes") && netPlayer.inBattle == false)
							{
								Battle battle = new Battle(Game.instance, Enemy.getEnemy("slime"), false);
								battle.netBattle = true;
								battle.owner = NetClient.this.id;
								battle.battleOwner = true;
								battle.playerEnemy = getPlayer(pbr.fromId);
								battle.playerBattle = true;
								if(battle.playerEnemy != null)
								{
									Game.instance.deleteAllBattles();
									Game.instance.enterBattle(battle, true); //Might change to true
									PacketBattleCommence bc = new PacketBattleCommence(NetClient.this.id, pbr.fromId, "slime", 
											Game.instance.player.stats, battle.music, battle.boss, 2, new int[] {}, battle.turn.turn, battle.playerBattle); 
									Game.instance.sendTCP(bc);
								}
								else MessageBox.addMessage("The requested", "player is not", "available.");
								
							}
							else if(netPlayer.inBattle)
							{
								MessageBox.addMessage(netPlayer.name, "is busy.");
							}
						}
					}, "Yes", "No");
				}
			});
		}
		else if(o instanceof PacketStatComponent)
		{
			PacketStatComponent sc = (PacketStatComponent) o;
			NetPlayer player = getPlayer(sc.id);
			if(player != null)
			{
				player.stats = sc.stats;
				player.name = sc.name;
				player.data.setInteger("timeoutTime", 60*60);
			}
			else if(sc.id == this.id)
			{
				Game.instance.player.stats.set(sc.stats);
			}
		}
		else if(o instanceof PacketTradeGold)
		{
			PacketTradeGold tg = (PacketTradeGold) o;
			NetPlayer fromPlayer = getPlayer(tg.fromId);
			String name = "Anonymous";
			if(fromPlayer != null)
				name = fromPlayer.name;
			Game.instance.player.stats.gold = Game.instance.player.stats.gold.add(new BigInteger(tg.gold));
			Game.instance.getAudio().getSound("sell").play();
			MessageBox.addMessage("You received", new BigInteger(tg.gold)	 + " G from", name);
		}
		else if(o instanceof PacketArenaProceed)
		{
			
		}
		else if(o instanceof PacketArenaBattleUpdate)
		{
			final PacketArenaBattleUpdate abu = (PacketArenaBattleUpdate) o;
			Game.instance.netActions.add(new NetAction() {
				@Override
				public void run(Game game, NetClient client) {
					String header = abu.header;
					Battle battle = game.battle;
					if(battle == null)
						return;
					if(battle.netBattle == false)
						return;
					//Change owner
					if(header.equals("newowner"))
					{
						ArenaEvent.updateIndex(battle);
						ArenaEvent.joinedBattle = false;
						battle.battleFinishEvent = ArenaEvent.battlePostEvent;
						
						battle.battleOwner = true;
						battle.owner = NetClient.this.id;
						battle.clientCombatantId = 1;
						battle.turn.turn = 1;
						battle.arena = false;
						battle.hostArena = true;
						battle.netJoinBattleIfVictorious = true;
						battle.restoreHpAfterCombat = false;
						int counter = 2;
						List<NetPlayer> rearranged = new ArrayList<NetPlayer>();
						for(int i = 0; i < battle.players.size(); i++)
						{
							if(battle.players.get(i).stats.hp.compareTo(Numbers.ZERO) > 0)
								rearranged.add(battle.players.get(i));
						}
						battle.players = rearranged;
						for(int i = 0; i < battle.players.size(); i++)
						{
							if(battle.players.get(i).stats.hp.compareTo(Numbers.ZERO) > 0)
							{
								PacketArenaBattleUpdate abu = new PacketArenaBattleUpdate(NetClient.this.id, battle.players.get(i).id, "chgcmbid", counter);
								game.sendTCP(abu);
							}
							counter++;
						}
						BattleNet.shiftTurn(battle);
					}
					//change Combattant id
					else if(header.equals("chgcmbid"))
					{
						battle.clientCombatantId = abu.data;
					}
					
				}
			});
		}
	}
	
	public NetPlayer getPlayer(int id)
	{
		for(int i = 0; i < clients.size(); i++)
		{
			if(clients.get(i).id == id)
			{
				return clients.get(i);
			}
		}
		return null;
	}
	
	public void disconnected(Connection c) {
		for(int i = 0; i < clients.size(); i++)
		{
			clients.get(i).dead = true;
			clients.remove(i);
		}
		clients.clear();
		MessageBox.addMessage("You were", "disconnected.");
		loggedIn = false;
		if(Game.instance.server.isStarted())
		{
			Game.instance.server.stop();
		}
		this.id = -1;
	}
	
	public void connect(String ip, int tcpPort, int udpPort) {
		this.id = -1;
		int maxSize = MAX_NETWORK_TRANSFER_SIZE;
		this.client = new Client(maxSize, maxSize);
		Network.register(this.client.getKryo());
		this.client.addListener(this);
		this.client.start();
		
		try {
			this.client.connect(5000, ip, tcpPort, udpPort);
		}
		catch(Exception e) {
			this.client = null;
			return;
		}
	}
	
	public Client getCommunication()
	{
		return client;
	}
	
	public boolean isCommunicationUp()
	{
		return isCommunicationNull() == false;
	}
	
	public boolean isCommunicationNull()
	{
		return client == null;
	}
	
	public boolean isConnected()
	{
		if(this.client == null) loggedIn = false;
		return loggedIn && this.client != null;
	}
	
}
