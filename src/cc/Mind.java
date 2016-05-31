package cc;

import java.util.ArrayList;
import java.util.Random;

public abstract class Mind
{
	private Circle circle;
	private Random random;
	private ArrayList<Sprite> inView;
	
	
	public Mind(Circle circle)
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
	
	public final void turn()
	{
		
	}
}
