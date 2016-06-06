package bots;

import java.util.ArrayList;

import lib.Vector2;
import world.Circle;
import world.Sprite;

public class ZekeBot extends Mind{

	public ZekeBot(Circle c, float variance, float mean) {
		super(c, variance, mean);
		// TODO Auto-generated constructor stub
	}

	public ZekeBot(ZekeBot zekeBot) {
		super(zekeBot);
	}

	@Override
	public Mind copy() {
		return new ZekeBot(this);
	}

	@Override
	public void think() {
		if(isAlive())
		{
			ArrayList<Sprite> inView = requestInView();
			Sprite target = null;
			for (Sprite s : inView)
			{
				if (s instanceof Circle)
				{
					if(target != null && s.getPosition().dist(getPosition()) < target.getPosition().dist(getPosition()))
					{
						target = s;
					}
					else if (target == null);
					{
						target = s;
					}
				}
			}
			
	
			if(target != null)
			{
				Vector2 direction = target.getPosition().sub(getVelocity(), getVelocity());
				Vector2 left = new Vector2(1, getVelocity().angle() + Circle.FOV / 2, true);
				Vector2 right = new Vector2(1, getVelocity().angle() - Circle.FOV / 2, true);
				turn((float) (Math.acos(direction.dot(left) / (direction.mag())) - Math.acos(direction.dot(right) / (direction.mag()))));
			}
			else
			{
				turn((float) Circle.MAX_TURNING_ANGLE);
			}
	
			if(target != null) shoot();
		}
		else
		{
			getStats();
		}
		
	}
	
	@Override
	public String toString()
	{
		return "The Greatest Little Zeke That Could";
	}

}
