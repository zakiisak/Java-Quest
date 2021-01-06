package game.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.MessageBox.ChoiceEvent;
import game.entitycomponent.SizeComponent;
import game.entitycomponent.SpriteComponent;
import game.entitycomponent.StatDrawComponent;
import game.entitycomponent.TransformComponent;
import game.graphics.FontRenderer;
import game.graphics.Sprite;
import game.net.PacketJoinBattle;
import game.net.PacketPlayerBattleRequest;
import game.net.PacketStatComponent;
import game.net.PacketTradeGold;
import game.tile.Tile;
import game.utils.Color;
import game.utils.Numbers;

public class NetPlayer extends MPEntity {
	
	public SizeComponent size;
	public StatDrawComponent statDraw;
	public SpriteComponent sprite;
	public HealthBar hb;
	
	public NetPlayer(MPEntity entity)
	{
		this();
		this.id = entity.id;
		this.transform = entity.transform;
		this.world = entity.world;
		this.lastTimeUpdated = entity.lastTimeUpdated;
		this.inBattle = entity.inBattle;
		this.name = entity.name;
		this.stats = entity.stats;
	}
	
	public NetPlayer() {
		transform = new TransformComponent(0, 0);
		size = new SizeComponent(Tile.SIZE, Tile.SIZE);
		sprite = new SpriteComponent(Creatures.getCreatureSprite(6), transform, size);
		sprite.translationMultiplier = Tile.SIZE;
		statDraw = new StatDrawComponent(stats);
		hb = new HealthBar(stats, 0, 0, 0, 0);
		hb.color = Color.green;
	}
	
	private void trade(final Player player)
	{
		MessageBox.addMessage("-----TRADE-----", "Trade money?").setChoices(new ChoiceEvent() {
			
			@Override
			public void onChoice(MessageBox box, String choice) {
				if(choice.equals("Yes"))
				{
					List<String> choicesList = new ArrayList<String>();
					BigInteger maxAmount = Game.instance.player.getGold();
					if(maxAmount.compareTo(Numbers.FIVE) > 0)
						choicesList.add("" + 5);
					if(maxAmount.compareTo(Numbers.TEN) > 0)
						choicesList.add("" + 10);
					if(maxAmount.compareTo(new BigInteger("100")) > 0)
						choicesList.add("" + 100);
					if(maxAmount.compareTo(new BigInteger("1000")) > 0)
						choicesList.add("" + 1000);
					choicesList.add("" + maxAmount.divide(Numbers.FOUR));
					choicesList.add("" + maxAmount.divide(Numbers.THREE));
					choicesList.add("" + maxAmount.divide(Numbers.TWO));
					choicesList.add("" + maxAmount);
					choicesList.add("Cancel");
					
					String[] choices = new String[choicesList.size()];
					for(int i = 0; i < choicesList.size(); i++)
					{
						choices[i] = choicesList.get(i);
					}
					
					MessageBox.addMessage("How much G", "do you want", "to send?").setChoices(new ChoiceEvent() {
						
						@Override
						public void onChoice(MessageBox box, String choice) {
							if(choice.equals("Cancel"))
							{
								return;
							}
							BigInteger gold = new BigInteger(choice);
							player.removeGold(gold);
							Game.instance.sendTCP(new PacketStatComponent(Game.instance.client.id, player.stats, player.name));
							Game.instance.sendTCP(new PacketTradeGold(-1, id, gold));
						}
					}, choices);
				}
			}
		}, "Yes", "No");
	}
	
	private void battle(Player player)
	{
		Game.instance.client.sendStats();
		MessageBox.addMessage("Sent request", "to ", name);
		PacketPlayerBattleRequest pbr = new PacketPlayerBattleRequest();
		pbr.fromId = Game.instance.client.id;
		pbr.toId = id;
		pbr.name = name;
		pbr.stats = stats;
		Game.instance.sendTCP(pbr);
	}
	
	public void interacted(final Player player)
	{
		if(inBattle == false)
		{
			MessageBox.addMessage("Choose an", "action with", name).setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Trade"))
					{
						trade(player);
					}
					else if(choice.equals("Battle"))
					{
						battle(player);
					}
				}
			}, "Trade", "Battle", "Cancel");
		}
			
	}
	
	public void joinBattle()
	{
		if(inBattle)
		{
			PacketJoinBattle jb = new PacketJoinBattle(-1, id);
			Game.instance.sendTCP(jb);
		}
	}
	
	public void playerSteppedOn(Player player)
	{
		if(inBattle)
		{
			PacketJoinBattle jb = new PacketJoinBattle(-1, id);
			Game.instance.sendTCP(jb);
		}
	}
	
	@Override
	public void onAdded(Object owner) {
		super.onAdded(owner);
		dead = false;
	}
	
	@Override
	public void tick() {
		if(dead) return;
		super.tick();
		/*
		if(data.getInteger("timeoutTime") > 0)
			data.setInteger("timeoutTime", data.getInteger("timeoutTime") - 1);
		if(Game.instance.client.isConnected() == false || data.getInteger("timeoutTime") <= 0)
		{
			dead = true;
			for(int i = Game.instance.client.clients.size() - 1; i >= 0; i--)
			{
				NetPlayer player = Game.instance.client.clients.get(i);
				if(player == this)
				{
					Game.instance.client.clients.remove(i);
				}
			}
		}
		*/
	}	
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		batch.setColor(1, 1, 1, 1);
		Creatures.getCreatureSprite(6).render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
		if(inBattle) 
		{
			Sprite.getSprite("battle_indicator").renderWithOutline(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE, 1, Color.black);
		}
		FontRenderer.drawWithOutline(batch, Game.baseFont, name, transform.x * Tile.SIZE + Tile.SIZE / 2 - Game.baseFont.getWidth(name) / 2 , transform.y * Tile.SIZE - 6, 1, 1, Color.black);
	}
	
}
