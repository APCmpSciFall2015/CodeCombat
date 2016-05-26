package cc;

import java.awt.Color;
import java.awt.Graphics;

import lib.Vector2;

public class Shield extends Sprite {

	private boolean unbound;
	private Sprite owner;

	/**
	 * 3-Argument Circle constructor
	 * @param x x pos
	 * @param y y pos
	 * @param world plane of existence
	 */
	public Shield(float x, float y, World world)
	{
		// @formatter:off
		super(
				new Vector2(40, 40), // size
				new Vector2(x, y), new Vector2(1, 0, true), new Vector2(0, 0), // pos, vel, acc
				new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)), // random color
				world);
		unbound = true;
		// @formatter:on
	}

	public Shield(Shield s)
	{
		super(s);
		this.unbound = s.isUnbound();
		this.owner = s.getOwner();
	}


	@Override
	public void update()
	{
		
		if (unbound)
		{
			super.update();
			// update position
			setPosition(getPosition().add(getVelocity()));

			// test change in velocity and shooting
			setVelocity(new Vector2(2f, (float) (getVelocity().angle() + Math.PI / 444), true));
		}
		else
		{
		}

	}

	public final void paint(Graphics g)
	{
		if (unbound)
		{
			// @formatter:off
			g.setColor(Color.CYAN);
			// paint circle
			g.fillOval(
					(int) (getPosition().getX() - getSize().getX() / 2),
					(int) (getPosition().getY() - getSize().getX() / 2),
					(int) getSize().getX(), (int) getSize().getY());
		}
		else
		{
			// @formatter:off
			g.setColor(Color.CYAN);
			// paint circle
			g.drawOval(
					(int) (getPosition().getX() - getSize().getX() / 2),
					(int) (getPosition().getY() - getSize().getX() / 2),
					(int) getSize().getX(), (int) getSize().getY());
		}
	}

	@Override
	public Sprite copy() {
		return new Shield(this);
	}

	@Override
	public void collide(Sprite s) {
		if (!unbound && s.getId() == owner.getId())
		{
			setPosition(s.getPosition());
		}
		else if(s instanceof Circle){
			setOwner(s);
			unbound = !unbound;
			setSize(new Vector2(60,60));
			setPosition(s.getPosition());
		}
		if(!unbound){
			if(s instanceof Projectile && !((Projectile) s).isOwner(owner)){
				this.setExistence(false);
			}
		}
	}

	public boolean isUnbound() {
		return unbound;
	}

	public void setUnbound(boolean unbound) {
		this.unbound = unbound;
	}

	public Sprite getOwner() {
		return owner;
	}

	public void setOwner(Sprite owner) {
		this.owner = owner;
	}



}
