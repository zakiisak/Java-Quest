package game.net;

public class PacketBattleIndicator {
	public int id;
	public boolean inBattle;
	
	public PacketBattleIndicator() {
	}
	
	public PacketBattleIndicator(int id, boolean inBattle) {
		this.id = id;
		this.inBattle = inBattle;
	}
	
}
