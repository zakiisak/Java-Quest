package game.entity;

import java.util.List;

import game.entity.Recipe.Integrant;

public class Recipes {
	
	public static void addBaseRecipes(List<Recipe> list)
	{
		//HealthPotion for 3 herbs
		list.add(new Recipe(new Integrant(Items.HEALTH_POTION, 1), new Integrant(Items.HERB), new Integrant(Items.HERB), new Integrant(Items.HERB)).setOnlyOneAtATime());
		add(list, Items.COPPER_INGOT, new Integrant(Items.COPPER_ORE), new Integrant(Items.COPPER_ORE), new Integrant(Items.COPPER_ORE), new Integrant(Items.GOLD(), 100));
		add(list, Items.LEAD_INGOT, new Integrant(Items.LEAD_ORE), new Integrant(Items.LEAD_ORE), new Integrant(Items.LEAD_ORE), new Integrant(Items.GOLD(), 500));
		add(list, Items.SILVER_INGOT, new Integrant(Items.SILVER_ORE), new Integrant(Items.SILVER_ORE), new Integrant(Items.SILVER_ORE), new Integrant(Items.GOLD(), 1000));
		add(list, Items.MALACHITE_INGOT, new Integrant(Items.MALACHITE_ORE), new Integrant(Items.MALACHITE_ORE), new Integrant(Items.MALACHITE_ORE), new Integrant(Items.GOLD(), 5000));
		add(list, Items.DAIZUM_INGOT, new Integrant(Items.DAIZUM_ORE), new Integrant(Items.DAIZUM_ORE), new Integrant(Items.DAIZUM_ORE), new Integrant(Items.GOLD(), 20000));
		add(list, Items.DARK_INGOT, new Integrant(Items.DARK_COMPOUND), new Integrant(Items.DARK_COMPOUND), new Integrant(Items.DARK_COMPOUND), new Integrant(Items.GOLD(), 100000));
	}
	
	
	public static void add(List<Recipe> list, int output, Integrant... input)
	{
		list.add(new Recipe(new Integrant(output), input));
	}
}
