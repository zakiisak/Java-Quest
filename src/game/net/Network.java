package game.net;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.serialize.ArraySerializer;

import game.battle.Battle;
import game.entity.EntityComponent;
import game.entity.MPEntity;
import game.entity.Tag;
import game.entitycomponent.StatComponent;
import game.entitycomponent.StatComponent.StatComponentData;
import game.entitycomponent.TransformComponent;

public class Network {
	public static final int NetworkPort = 7667;
	
	public static void register(Kryo kryo)
	{
		final ArraySerializer arraySerializer = new ArraySerializer(kryo);
		kryo.register(PacketTransform.class);
		kryo.register(PacketPlayerList.class);
		kryo.register(PacketBattleIndicator.class);
		kryo.register(PacketJoinBattle.class);
		kryo.register(PacketBattleCommence.class);
		kryo.register(PacketStatComponent.class);
		kryo.register(PacketPlayerBattleRequest.class);
		kryo.register(TransformComponent.class);
		kryo.register(EntityComponent.class);
		kryo.register(Battle.PostEvent.class);
		kryo.register(PacketTradeGold.class);
		kryo.register(StatComponent.class, new Serializer() {
			
			@Override
			public void writeObjectData(ByteBuffer arg0, Object arg1) {
				StatComponent stats = (StatComponent) arg1;
				StatComponentData data = stats.toData();
				arraySerializer.writeObject(arg0, data.maxhp);
				arraySerializer.writeObject(arg0, data.hp);
				arraySerializer.writeObject(arg0, data.atk_min);
				arraySerializer.writeObject(arg0, data.atk_max);
				arraySerializer.writeObject(arg0, data.lvl);
				arraySerializer.writeObject(arg0, data.xp);
				arraySerializer.writeObject(arg0, data.xplimit);
				arraySerializer.writeObject(arg0, data.gold);
				/*
				arg0.put(data.maxhp);
				arg0.put(data.hp);
				arg0.put(data.atk_min);
				arg0.put(data.atk_max);
				arg0.put(data.lvl);
				arg0.put(data.xp);
				arg0.put(data.xplimit);
				arg0.put(data.gold);
				*/
			}
			
			@Override
			public <T> T readObjectData(ByteBuffer arg0, Class<T> arg1) {
				byte[] maxhp = arraySerializer.readObject(arg0, byte[].class);
				byte[] hp = arraySerializer.readObject(arg0, byte[].class);
				byte[] atk_min = arraySerializer.readObject(arg0, byte[].class);
				byte[] atk_max = arraySerializer.readObject(arg0, byte[].class);
				byte[] lvl = arraySerializer.readObject(arg0, byte[].class);
				byte[] xp = arraySerializer.readObject(arg0, byte[].class);
				byte[] xplimit = arraySerializer.readObject(arg0, byte[].class);
				byte[] gold = arraySerializer.readObject(arg0, byte[].class);
				StatComponentData data = new StatComponentData();
				data.maxhp = maxhp;
				data.hp = hp;
				data.atk_min = atk_min;
				data.atk_max = atk_max;
				data.lvl = lvl;
				data.xp = xp;
				data.xplimit = xplimit;
				data.gold = gold;
				return (T) data.toStats();
			}
		});
		kryo.register(PacketUnicast.class);
		kryo.register(PacketArenaProceed.class);
		kryo.register(PacketArenaBattleUpdate.class);
		kryo.register(byte[].class);
		kryo.register(Tag.class);
		kryo.register(MPEntity.class);
		kryo.register(MPEntity[].class);
		kryo.register(Map.class);
		kryo.register(HashMap.class);
		kryo.register(PacketBattleUpdate.class);
		kryo.register(PacketLogout.class);
		kryo.register(int[].class);
		kryo.register(float[].class);
	}
	
}
