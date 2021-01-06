package game.net;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import game.entity.MPEntity;
import game.entity.NetPlayer;

public class NetServer extends Listener {
	public static final int MAX_NETWORK_TRANSFER_SIZE = 768 * 10000;
	
	private Server server;
	private List<NetPlayer> players = new ArrayList<NetPlayer>();
	
	public void stop()
	{
		server.stop();
		server = null;
		players.clear();
	}
	
	public void start(int tcpPort, int udpPort) {
		this.server = new Server(MAX_NETWORK_TRANSFER_SIZE, MAX_NETWORK_TRANSFER_SIZE);
		Network.register(this.server.getKryo());
		try {
			this.server.bind(tcpPort, udpPort);
		} catch (Exception e) {
			System.out.println(
					"Unable to start server on ports: [tcpPort=" + tcpPort
							+ ";udpPort=" + udpPort + "] - \n" + e.getMessage());
			if(server != null)
			{
				try
				{
					server.stop();
				}
				catch(Exception e2)
				{
					e.printStackTrace();
				}
				server = null;
			}
			return;
		}
		this.server.start();
		this.server.addListener(this);
	}
	
	public void connected(Connection c) {
		System.out.println("Client connected!");
		PacketPlayerList list = new PacketPlayerList();
		list.players = new MPEntity[players.size()];
		list.yourId = c.getID();
		for(int i = 0; i < players.size(); i++)
		{
			NetPlayer p = players.get(i);
			list.players[i] = new MPEntity(p);
		}
		NetPlayer player = new NetPlayer();
		player.id = c.getID();
		players.add(player);
		c.sendTCP(list);
		
	}

	public void received(Connection c, Object o) {
		if(o instanceof PacketTransform)
		{
			int id = -1;
			for(int i = 0; i < players.size(); i++)
			{
				if(players.get(i).id == c.getID())
				{
					if(id > -1)
					{
						players.get(i).dead = true;
						players.remove(i);
						i--;
					}
					else 
						id = i;
				}
			}
			NetPlayer player = players.get(id);
			PacketTransform transform = (PacketTransform) o;
			player.world = transform.world;
			player.transform.set(transform.x, transform.y);
			transform.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), transform);
		}
		else if(o instanceof PacketBattleIndicator)
		{
			setBattleIndication((PacketBattleIndicator) o, c);
		}
		else if(o instanceof PacketJoinBattle)
		{
			PacketJoinBattle jb = (PacketJoinBattle) o;
			if(jb.fromId == -1)
				jb.fromId = c.getID();
			server.sendToTCP(jb.toId, jb);
			setBattleIndication(new PacketBattleIndicator(c.getID(), true), c);
			
		}
		else if(o instanceof PacketBattleCommence)
		{
			PacketBattleCommence bc = (PacketBattleCommence) o;
			server.sendToTCP(bc.id, bc);
		}
		else if(o instanceof PacketBattleUpdate)
		{
			PacketBattleUpdate bu = (PacketBattleUpdate) o;
			System.out.println("[server] bu update from " + c.getID() + ": " + bu.data);
			for(int i = 0; i < bu.players.length; i++)
			{
				if(bu.players[i] == c.getID())
					continue;
				server.sendToTCP(bu.players[i], bu);
			}
		}
		else if(o instanceof PacketPlayerBattleRequest)
		{
			PacketPlayerBattleRequest pbr = (PacketPlayerBattleRequest) o;
			server.sendToTCP(pbr.toId, o);
		}
		else if(o instanceof PacketStatComponent)
		{
			PacketStatComponent sc = (PacketStatComponent) o;
			NetPlayer player = getPlayer(sc.id);
			if(player != null)
			{
				player.stats = sc.stats;
				player.name = sc.name;
				server.sendToAllExceptTCP(c.getID(), sc);
			}
		}
		else if(o instanceof PacketTradeGold)
		{
			PacketTradeGold tg = (PacketTradeGold) o;
			if(tg.fromId == -1)
				tg.fromId = c.getID();
			server.sendToTCP(tg.toId, tg);
		}
		else if(o instanceof PacketUnicast)
		{
			server.sendToTCP(((PacketUnicast) o).toId, o);
		}
	}
	
	public NetPlayer getPlayer(int id)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.get(i).id == id)
			{
				return players.get(i);
			}
		}
		return null;
	}
	
	private void setBattleIndication(PacketBattleIndicator bi, Connection c)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.get(i).id == c.getID())
			{
				players.get(i).inBattle = bi.inBattle;
				break;
			}
		}
		bi.id = c.getID();
		server.sendToAllExceptTCP(c.getID(), bi);
	}

	public void disconnected(Connection c) {
		System.out.println("Disconnection id: " + c.getID());
		for(int i = 0; i < players.size(); i++)
		{
			if(players.get(i).id == c.getID())
			{
				players.remove(i);
				break;
			}
		}
		server.sendToAllExceptTCP(c.getID(), new PacketLogout(c.getID()));
	}
	
	public Server getCommunication() {
		return server;
	}
	
	public boolean isStarted()
	{
		return server != null;
	}
	
}
