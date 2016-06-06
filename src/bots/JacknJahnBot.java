package bots;

import java.util.ArrayList;
import lib.Vector2;
import world.Circle;
import world.Mine;
import world.Obstacle;
import world.Shield;
import world.Sprite;

public class JacknJahnBot extends Mind{

	public JacknJahnBot(JacknJahnBot t){
		super(t);
	}

	public JacknJahnBot(Circle c, float variance, float mean){
		super(c, variance, mean);
	}

	public Mind copy(){
		return new JacknJahnBot(this);
	}

	@Override
	public void think(){		
		if(isAlive()){
			ArrayList<Sprite> inView = requestInView();
			Sprite target = null;
			Sprite shield = null;
			Sprite mine = null;
			Sprite obstruction = null;
			int priorityQueue = 0;	

			for (Sprite s : inView){
				if (s instanceof Circle){
					priorityQueue = 4;
				}
			}
			if(!super.isShielded()){
				for (Sprite s : inView){
					if (s instanceof Shield){
						if(s.getPosition().dist(getPosition()) < 200){
							priorityQueue = 3;
						}
					}
				}
			}
			for (Sprite s : inView){
				if (s instanceof Obstacle){
					if(s.getPosition().dist(getPosition()) < 50){
						priorityQueue = 2;
					}
				}
			}
			for (Sprite s : inView){
				if (s instanceof Mine){
					if(s.getPosition().dist(getPosition()) < 100){
						priorityQueue = 1;
					}
				}
			}
			switch (priorityQueue){
			case 1://mine avoidance
				for (Sprite s : inView){
					if (s instanceof Mine){
						if(s.getPosition().dist(getPosition()) < 100){
							mine = s;
							Vector2 direction = mine.getPosition().sub(getEyePosition());
							Vector2 left = new Vector2(1, getVelocity().angle() + Circle.FOV / 2, true);
							Vector2 right = new Vector2(1, getVelocity().angle() - Circle.FOV / 2, true);
							turn((float) -(Math.acos(direction.dot(left) / (direction.mag())) - Math.acos(direction.dot(right) / (direction.mag()))));
						}
					}
				}
			case 2://obstacle avoidance
				for (Sprite s : inView){
					if (s instanceof Obstacle){
						if(s.getPosition().dist(getPosition()) < 50){
							obstruction = s;
							Vector2 direction = obstruction.getPosition().sub(getEyePosition());
							Vector2 left = new Vector2(1, getVelocity().angle() + Circle.FOV / 2, true);
							Vector2 right = new Vector2(1, getVelocity().angle() - Circle.FOV / 2, true);
							turn((float) -(Math.acos(direction.dot(left) / (direction.mag())) - Math.acos(direction.dot(right) / (direction.mag()))));
						}
					}
				}
				break;
			case 3://get shield
				for (Sprite s : inView){
					if (s instanceof Shield){ //Shield Getting
						if(shield != null && s.getPosition().dist(getPosition()) < shield.getPosition().dist(getPosition())){
							shield = s;
						}
						else if (shield == null);{
							shield = s;
						}
						if(target != null){
							Vector2 direction = shield.getPosition().sub(getEyePosition());
							Vector2 left = new Vector2(1, getVelocity().angle() + Circle.FOV / 2, true);
							Vector2 right = new Vector2(1, getVelocity().angle() - Circle.FOV / 2, true);
							turn((float) (Math.acos(direction.dot(left) / (direction.mag())) - Math.acos(direction.dot(right) / (direction.mag()))));
						}
					}
				}
				break;
			case 4: //murder
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
							shoot();
						}
					}	
				}
				break;
			default: turn((float) Circle.MAX_TURNING_ANGLE);
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
	
