package bots;

import java.util.ArrayList;

import lib.Vector2;
import world.Circle;
import world.Sprite;

public class TestBot extends Mind
{

	public TestBot(TestBot t)
	{
		super(t);
	}

	public TestBot(Circle c, float variance, float mean)
	{
		super(c, variance, mean);
	}

	public Mind copy()
	{
		return new TestBot(this);
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
				if (s instanceof Circle)
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
			else
			{
				turn((float) Circle.MAX_TURNING_ANGLE);
			}

			if (target != null)
				shoot();
		}
	}

	@Override
	public String toString()
	{
		return "TestBot";
	}
}
