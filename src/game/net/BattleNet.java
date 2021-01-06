package game.net;

import java.math.BigInteger;

import game.Game;
import game.battle.Battle;

public class BattleNet {
	
	public static void attackEnemy(Battle battle, BigInteger dmg)
	{
		PacketBattleUpdate pu = new PacketBattleUpdate();
		pu.players = new int[battle.players.size() + (battle.playerBattle ? 1 : 0)];
		pu.data = "edmg" + dmg;
		if(battle.playerBattle)
		{
			pu.players[0] = battle.playerEnemy.id;
		}
		else
		{
			for(int i = 0; i < pu.players.length; i++)
			{
				pu.players[i] = battle.players.get(i).id;
			}
		}
		Game.instance.sendTCP(pu);
	}
	
	public static void attackPlayer(Battle battle, int playerId, BigInteger dmg)
	{
		if(battle.battleOwner == false)
			return;
		PacketBattleUpdate pu = new PacketBattleUpdate();
		pu.players = new int[battle.players.size()];
		for(int i = 0; i < pu.players.length; i++)
		{
			pu.players[i] = battle.players.get(i).id;
		}
		pu.data = "pdmg_" + playerId + "_" + dmg;
		Game.instance.sendTCP(pu);
	}
	
	public static void healPlayer(Battle battle, int playerId, BigInteger heal)
	{
		if(battle.battleOwner == false)
			return;
		PacketBattleUpdate pu = new PacketBattleUpdate();
		pu.players = new int[battle.players.size()];
		for(int i = 0; i < pu.players.length; i++)
		{
			pu.players[i] = battle.players.get(i).id;
		}
		pu.data = "heal_" + playerId + "_" + heal;
		Game.instance.sendTCP(pu);
	}
	
	public static void shiftTurn(Battle battle)
	{
		PacketBattleUpdate pu = new PacketBattleUpdate();
		pu.players = new int[battle.players.size() + (battle.playerBattle ? 1 : 0)];
		if(battle.playerBattle)
		{
			pu.players[0] = battle.playerEnemy.id;
		}
		else
		{
			for(int i = 0; i < pu.players.length; i++)
			{
				pu.players[i] = battle.players.get(i).id;
			}
		}
		pu.data = "turn" + battle.turn.turn;
		Game.instance.sendTCP(pu);
	}
	
	public static void playerJoin(Battle battle, int playerId)
	{
		PacketBattleUpdate pu = new PacketBattleUpdate();
		pu.players = new int[battle.players.size()];
		for(int i = 0; i < pu.players.length; i++)
		{
			pu.players[i] = battle.players.get(i).id;
		}
		pu.data = "join" + playerId;
		Game.instance.sendTCP(pu);
	}
	
	
}
