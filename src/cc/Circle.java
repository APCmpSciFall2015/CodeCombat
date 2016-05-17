package cc;

import java.awt.Color;
import java.awt.Graphics;

import lib.Vector2;

public class Circle extends Sprite
{
	public Circle(float x, float y, World w)
	{
		super(x, y, w);
		setVelocity(new Vector2(1, 0));
	}

	public static final float MAX_TURNING_ANGLE = (float) Math.PI / 60f;
	
	public void paint(Graphics g) 
	{
		g.setColor(Color.RED);
		g.fillOval((int) getPosition().getX() - 20, (int) getPosition().getY() - 20, 40, 40);
		g.setColor(Color.MAGENTA);
		Vector2 direction = getAcceleration().normalize();
		g.fillOval((int) (getPosition().getX() + direction.getX() * 20) - 5, (int) (getPosition().getY() + direction.getY() * 20) - 5, 10, 10);
		g.setColor(Color.BLUE);
		g.fillOval((int) (getPosition().getX() + getVelocity().normalize().getX() * 20) - 5, (int) (getPosition().getY() + getVelocity().normalize().getY() * 20) - 5, 10, 10);
	}

	@Override
	public void update()
	{
		setAcceleration(new Vector2(getVelocity().getY(), -getVelocity().getX()).normalize().mult(2));
		System.out.println(getVelocity());
		System.out.println(getAcceleration() + "\n");
		Vector2 oldVelocity = getVelocity();
		
		if(getVelocity().mag() < 20) setVelocity(getVelocity().add(getAcceleration())); // update the instantaneous vel
		else setVelocity(getVelocity().add(getAcceleration()).normalize().mult(20)); // truncate the max vel
		
		setPosition(getPosition().add(getVelocity().add(oldVelocity).div(2))); // calc avg vel over interval for UAM and add to pos
	}
}
