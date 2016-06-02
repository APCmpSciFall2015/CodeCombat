package bots;

import java.util.ArrayList;
import lib.Vector2;
import world.Circle;
import world.Obstacle;
import world.Shield;
import world.Sprite;

public class JacknJahnBot extends Mind
{
	public JacknJahnBot(JacknJahnBot t)
	{
		super(t);
	}

	public JacknJahnBot(Circle c, float variance, float mean)
	{
		super(c, variance, mean);
	}

	public Mind copy()
	{
		return new JacknJahnBot(this);
	}

	@Override
	public void think()	{
		if(isAlive()){
			ArrayList<Sprite> inView = requestInView();
			Sprite target = null;
			Sprite value = null;
			for (Sprite s : inView){
				if (s instanceof Obstacle){ //Obstacle Avoidance
					if(s.getPosition().dist(getPosition()) < 50){
						turn((float) Circle.MAX_TURNING_ANGLE);
					}
				}
			}


			for (Sprite s : inView){
				if (s instanceof Shield){ //Shield Getting
					if(value != null && s.getPosition().dist(getPosition()) < value.getPosition().dist(getPosition())){
						value = s;
					}
					else if (value == null);{
						value = s;
					}
					if(target != null){
						Vector2 direction = value.getPosition().sub(getEyePosition());
						Vector2 left = new Vector2(1, getVelocity().angle() + Circle.FOV / 2, true);
						Vector2 right = new Vector2(1, getVelocity().angle() - Circle.FOV / 2, true);
						turn((float) (Math.acos(direction.dot(left) / (direction.mag())) - Math.acos(direction.dot(right) / (direction.mag()))));
					}
				}
			}


			for (Sprite s : inView){
				if (s instanceof Circle){ //Hunter Killer
					if(target != null && s.getPosition().dist(getPosition()) < target.getPosition().dist(getPosition())){
						target = s;
					}
					else if (target == null);{
						target = s;
					}
					if(target != null){
						Vector2 direction = target.getPosition().sub(getEyePosition());
						Vector2 left = new Vector2(1, getVelocity().angle() + Circle.FOV / 2, true);
						Vector2 right = new Vector2(1, getVelocity().angle() - Circle.FOV / 2, true);
						turn((float) (Math.acos(direction.dot(left) / (direction.mag())) - Math.acos(direction.dot(right) / (direction.mag()))));
					}
					if(target != null) shoot();
				}	
			}
		}
		else{
			getStats();
		}
	}

	@Override
	public String toString()
	{
		return "JacknJahnBot";
	}
}
	
