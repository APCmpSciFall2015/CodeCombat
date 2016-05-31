package cc;

import java.util.ArrayList;
import java.util.Random;

public abstract class Mind
{
	private Circle circle;
	private Random random;
	private ArrayList<Sprite> inView;
	
	
	public Mind(Circle circle, float variance)
	{
		this.circle = circle;
	}
	
	public final void shoot()
	{
		circle.shoot();
	}
	
	public final void requestInView()
	{
		inView = circle.requestInView();
	}
	
	public final void turn(float deltaTheta)
	{
		circle.turn(deltaTheta);
	}

	// Getters and Setters
	// ---------------------------------------------
	
	public Random getRandom()
	{
		return random;
	}

	public void setRandom(Random random)
	{
		this.random = random;
	}

	public ArrayList<Sprite> getInView()
	{
		return inView;
	}

	public void setInView(ArrayList<Sprite> inView)
	{
		this.inView = inView;
	}
}
