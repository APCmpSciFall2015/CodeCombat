package bots;

import java.util.ArrayList;
import java.util.Random;

import cc.Circle;
import cc.Sprite;
import lib.Vector2;

public class TestBot extends Mind
{
	public TestBot(TestBot t)
	{
		super(t);
	}

	public TestBot(Circle c, Random r)
	{
		super(c, r);
	}

	public Mind copy()
	{
		return new TestBot(this);
	}

	@Override
	public void think()
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
		
		
		if(target != null && new Vector2(target.getPosition().sub(getPosition())).angle() - getVelocity().angle() < 0)
		{
			turn(-Circle.MAX_TURNING_ANGLE / 4);
		}
		else
		{
			turn(Circle.MAX_TURNING_ANGLE / 4);
		}
		
		if(target != null) shoot();
	}
}
