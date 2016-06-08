package bots;

import java.util.ArrayList;

import lib.Vector2;
import world.Circle;
import world.Mine;
import world.Obstacle;
import world.Projectile;
import world.Shield;
import world.Sprite;


public class RicardoBot extends Mind{
	private boolean targeting = false;
	public static final int MINE_INDEX = 0;
	public static final int OBSTACLE_INDEX = 1;
	public static final int PROJECTILE_INDEX = 2;
	public static final int SHIELD_INDEX = 3;
	public static final int CIRCLE_INDEX = 4;
	Vector2 lastPos;
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
		if(isAlive())
		{
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
			targeting = false;
			ArrayList<Sprite> inView = requestInView();
			ArrayList<ArrayList<Sprite>> sortedItems = new ArrayList<ArrayList<Sprite>>();

			//check to see if we moved
			if(lastPos != null && lastPos.dist(getPosition()) < 1)
			{
				System.out.println("Scramble");
				avoid();
			}
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

			if(sortedItems.get(PROJECTILE_INDEX).size() > 0)
			{
				System.out.println("Targeting Projectiles");
				for(Sprite s : sortedItems.get(PROJECTILE_INDEX))
				{
					if(s.getPosition().dist(getPosition()) < 35)
					{
						target(s);
						shoot();
					}
				}
			}
			
			if(sortedItems.get(MINE_INDEX).size() > 0)
			{
				System.out.println("Targeting Mines");
				for(Sprite s : sortedItems.get(MINE_INDEX))
				{
					if(s.getPosition().dist(getPosition()) < 35)
					{
						target(s);
						shoot();
					}
				}
			}


			if(sortedItems.get(OBSTACLE_INDEX).size() > 0)
			{
				System.out.println("Targeting Obstacles");
				for(Sprite s : sortedItems.get(OBSTACLE_INDEX))
				{
					if (s.getPosition().dist(getPosition()) < 35)
					{
						System.out.println("Avoiding Obstacles");
						avoid();
					}
				}
			}

			if(!isShielded())
			{
				if(sortedItems.get(SHIELD_INDEX).size() > 0)
				{
					Sprite shortest;
					System.out.println("Targeting Shields");
					shortest = sortedItems.get(SHIELD_INDEX).get(0);
					for(Sprite s : sortedItems.get(SHIELD_INDEX))
					{
						if(s.getPosition().dist(getPosition()) < shortest.getPosition().dist(getPosition()))
						{
							shortest = s;
						}
					}
					target(shortest);
				}
			}

			if(sortedItems.get(CIRCLE_INDEX).size() > 0)
			{
				System.out.println("Targeting Circles");
				Sprite shortest = sortedItems.get(CIRCLE_INDEX).get(0);
				for(Sprite s : sortedItems.get(CIRCLE_INDEX))
				{
					if(s.getPosition().dist(getPosition()) < shortest.getPosition().dist(getPosition()))
					{
						shortest = s;
					}
				}
				target(shortest);
				shoot();
			}

			if(!targeting)
			{
				System.out.println("Roaming");
				turn((float) (Math.PI / 2));
			}
			lastPos = getPosition().copy();
		}
	}

	private void target(Sprite target)
	{
		targeting = true;
		if (target != null)
		{
			Vector2 direction = target.getPosition().sub(calcEyePosition(getPosition(), getVelocity()));
			Vector2 left = new Vector2(1, getVelocity().angle() - Circle.FOV / 2, true);
			Vector2 right = new Vector2(1, getVelocity().angle() + Circle.FOV / 2, true);
			if(Math.acos(direction.dot(left) / (direction.mag()))
					- Math.acos(direction.dot(right) / (direction.mag())) > 0)
			{
				turn((float) Math.acos(direction.dot(getVelocity()) / direction.mag() / getVelocity().mag()));
			}
			else
			{
				turn((float) -Math.acos(direction.dot(getVelocity()) / direction.mag() / getVelocity().mag()));
			}

		}
	}

	private void avoid()
	{
		turn(Circle.MAX_TURNING_ANGLE);
	}

	@Override
	public String toString()
	{
		return "RicardoBot";
	}

}
