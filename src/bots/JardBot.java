package bots;

import java.util.ArrayList;

import lib.Vector2;
import world.Circle;
import world.CircleStats;
import world.Mine;
import world.Obstacle;
import world.Sprite;

public class JardBot extends Mind
{
	CircleStats stats;
	boolean obs = false;
	int numRecurring = 0;
	
	public JardBot(JardBot r)
	{
		super(r);
		this.stats = r.stats;
	}
	
	public JardBot(Circle c, float variance, float mean)
	{
		super(c, variance, mean);
	}

	@Override
	public Mind copy()
	{
		return new JardBot(this);
	}

	@Override
	public void think()
	{
		if (isAlive())
		{
			ArrayList<Sprite> inView = requestInView();
			Sprite target = null;
			for (Sprite s : inView)
			{

				if( s instanceof Mine || s instanceof Obstacle){
					if(s.getPosition().dist(getPosition()) < 15){
					obs = true;
					numRecurring += 1;
					}
					else{
						obs = false;
						numRecurring = 0;
					}
				}
				
				else if (s instanceof Circle)
				{
					if (target != null
							&& s.getPosition().dist(getPosition()) < target.getPosition().dist(getPosition()))
					{
						target = s;
					}
					else if (target == null)
					{
						target = s;
					}
				}
			}
			
			if(obs){
		
					turn((float) Circle.MAX_TURNING_ANGLE);
			}

			else if(target != null)
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
			else
			{
				turn((float) Circle.MAX_TURNING_ANGLE);
			}

			if (target != null && !obs)
				shoot();
		}
		else
		{
			stats = getStats();
		}
	}
	
	@Override
	public String toString()
	{
		return "JardBot";
	}
}