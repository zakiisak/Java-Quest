package game.net;

import game.battle.Battle;
import game.entitycomponent.StatComponent;

public class PacketBattleCommence {
	
	public int fromId;
	public int id;
	public String enemyCodeName;
	public StatComponent stats;
	public String battleMusic;
	public boolean boss;
	public int combatantId;
	public int[] players;
	public int turn;
	public boolean playerBattle;
	public boolean force;
	public boolean arena;
	
	public PacketBattleCommence(){
		
	}
	
	public PacketBattleCommence(int fromId, int id, String enemyCodeName, StatComponent stats, String battleMusic, 
			boolean boss, int combatantId, int[] players, int turn, boolean playerBattle) {
		this.fromId = fromId;
		this.id = id;
		this.enemyCodeName = enemyCodeName;
		this.stats = stats;
		this.battleMusic = battleMusic;
		this.boss = boss;
		this.combatantId = combatantId;
		this.players = players;
		this.turn = turn;
		this.playerBattle = playerBattle;
	}
	
}
