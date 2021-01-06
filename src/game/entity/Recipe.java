package game.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import game.Game;
import game.utils.Event;
import game.utils.Numbers;

public class Recipe implements Serializable{
	
	public Integrant[] input;
	public Integrant[] output;
	public boolean discovered = false;
	public boolean oneTimer = false;
	//Prevents from crafting multiple items that you should only have one of.
	public boolean onlyOneItemAtATime;
	
	public boolean canPlayerProduce(Player player)
	{
		if(onlyOneItemAtATime && player.possessItem(output[0].item))
			return false;
			
		Map<Integer, BigInteger> itemMemory = new HashMap<Integer, BigInteger>();
		for(int i = 0; i < input.length; i++)
		{
			Integrant ingredient = input[i];
			BigInteger playerCount = player.getItemCount(ingredient.item); 
			if(playerCount.compareTo(ingredient.count) < 0)
				return false;
			boolean containsKey = itemMemory.containsKey(ingredient.item);
			if(containsKey)
			{
				if(playerCount.subtract(itemMemory.get(ingredient.item)).compareTo(ingredient.count) < 0)
					return false;
			}
			itemMemory.put(ingredient.item, (containsKey ? itemMemory.get(ingredient.item) : BigInteger.ZERO).add(ingredient.count));
		}
		return true;
	}
	
	public void produce(Player player, int popupScale, final Event popupsFinished)
	{
		for(int i = 0; i < input.length; i++)
		{
			Integrant ingredient = input[i];
			if(ingredient.item == Items.GOLD())
			{
				player.removeGold(ingredient.count);
			}
			else player.removeItem(ingredient.item, ingredient.count.longValue());
		}
		ItemPopup last = null;
		for(int i = 0; i < output.length; i++)
		{
			Integrant result = output[i];
			final ItemPopup popup = new ItemPopup(result.item, result.count, popupScale, "You received " + result.count, Items.getItemName(result.item), "");
			if(last == null)
			{
				Game.instance.addEntity(popup);
			}
			else
			{
				last.setObliterationEvent(new Event() {
					@Override
					public void run() {
						Game.instance.addEntity(popup);
					}
				});
			}
			last = popup;
		}
		last.setObliterationEvent(new Event() {
			@Override
			public void run() {
				if(popupsFinished != null)
					popupsFinished.run();
			}
		});
		discovered = true;
	}
	
	public Recipe()
	{
		
	}
	
	public Recipe(Integrant[] input, Integrant[] output)
	{
		this.input = input;
		this.output = output;
	}
	
	public Recipe(Integrant output, Integrant... input)
	{
		this(input, new Integrant[] {output});
	}
	
	public Recipe setOnlyOneAtATime()
	{
		this.onlyOneItemAtATime = true;
		return this;
	}
	
	public Recipe setOneTimer()
	{
		this.oneTimer = true;
		return this;
	}
	
	public boolean equals(Object o)
	{
		if((o instanceof Recipe) == false)
			return false;
		Recipe r = (Recipe) o;
		if(r.input.length != input.length || r.output.length != output.length)
			return false;
		for(int i = 0; i < input.length; i++)
		{
			if(input[i].equals(r.input[i]) == false)
				return false;
		}
		
		for(int i = 0; i < output.length; i++)
		{
			if(output[i].equals(r.output[i]) == false)
				return false;
		}
		
		return true;
	}
	
	
	
	public static class Integrant implements Serializable
	{
		public int item;
		public BigInteger count;
		
		public Integrant()
		{
			
		}
		
		public Integrant(int item, BigInteger count)
		{
			this.item = item;
			this.count = count;
		}
		
		public Integrant(int item, long count)
		{
			this(item, new BigInteger("" + count));
		}
		
		public Integrant(int item)
		{
			this(item, Numbers.ONE);
		}
		
		public boolean equals(Object o)
		{
			if((o instanceof Integrant) == false)
				return false;
			Integrant i = (Integrant) o;
			return item == i.item && count == i.count;
		}
	}
	
}
