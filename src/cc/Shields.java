package cc;

import lib.Vector2;

public class Shields extends Sprite {
	
	public Shields(){
		super(new Vector2(40,40),
				
				);
	}

	
	@Override
	public void update()
	{
		if (isAlive())
		{
			super.update();
			// update position
			setPosition(getPosition().add(getVelocity()));

			// test change in velocity and shooting
			setVelocity(new Vector2(2f, (float) (getVelocity().angle() + Math.PI / 500), true));
		}
		else
		{
			
		}
		
	}
	
	@Override
	public Sprite copy() {
		return null;
	}

	@Override
	public void collide(Sprite s) {
		
	}

}
