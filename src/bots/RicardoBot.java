package bots;

import java.util.ArrayList;

import world.Circle;
import world.Mine;
import world.Obstacle;
import world.Projectile;
import world.Shield;
import world.Sprite;


public class RicardoBot extends Mind{
	public static final int MINE_INDEX = 0;
	public static final int OBSTACLE_INDEX = 1;
	public static final int PROJECTILE_INDEX = 2;
	public static final int SHIELD_INDEX = 3;
	public static final int CIRCLE_INDEX = 4;
	public RicardoBot(RicardoBot c)
	{
		super(c);
	}
	
	public RicardoBot(Circle c, float variance, float mean)
	{
		super(c, variance, mean);
	}

	@Override
	public Mind copy() {
		return new RicardoBot(this);
	}

	@Override
	public void think() {
		/*
		 * What this robot wants to do:
		 * avoid projectiles
		 * avoid mines
		 * avoid obstacles
		 * get shields
		 * shoot other robots
		 * spin in circles
		 */
		
		//get the stuff in view
		ArrayList<Sprite> inView = requestInView();
		ArrayList<ArrayList<Sprite>> sortedItems = new ArrayList<ArrayList<Sprite>>();
		//add each list of items
		for(int x = 0; x < 5; x++)
		{
		sortedItems.add(new ArrayList<Sprite>());
		}
		
		for(Sprite s : inView)
		{
			if(s instanceof Mine)
			{
				sortedItems.get(MINE_INDEX).add(s);
			}
			else if(s instanceof Obstacle)
			{
				sortedItems.get(OBSTACLE_INDEX).add(s);
			}
			else if(s instanceof Projectile)
			{
				sortedItems.get(PROJECTILE_INDEX).add(s);
			}
			else if(s instanceof Shield)
			{
				sortedItems.get(SHIELD_INDEX).add(s);
			}
			else if(s instanceof Circle)
			{
				sortedItems.get(CIRCLE_INDEX).add(s);
			}
		}
		
		
		
		
		
	}
	
	@Override
	public String toString()
	{
		return "RicardoBot";
	}

}
