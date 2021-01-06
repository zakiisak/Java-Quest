package game.net;

import game.entitycomponent.StatComponent;

public class PacketPlayerBattleRequest {
	public int fromId;
	public int toId;
	public StatComponent stats;
	public String name;
}
