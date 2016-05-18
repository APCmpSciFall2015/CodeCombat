package cc;

import java.awt.Graphics;
import java.util.ArrayList;

public class World {
	private ArrayList<Sprite> sprites;

	public World() {
		sprites = new ArrayList<Sprite>();
		sprites.add(new Circle(400, 300, this));
	}

	public void update() {
		for (Sprite s : sprites) {
			s.update();
		}
	}

	public boolean checkCollisions() {
		boolean collisions = false;
		for (int i = 0; i < sprites.size() - 1; i++) {
			for (int j = i + 1; j < sprites.size(); j++) {
				if (checkCollision(sprites.get(i), sprites.get(j))) {
					collisions = true;
					sprites.get(i).collision(sprites.get(j));
					sprites.get(j).collision(sprites.get(i));
				}
			}
		}
		return collisions;
	}

	public boolean checkCollision(Sprite A, Sprite B) {
		return checkCollision(A.getPosition().getX() - A.getWidth() / 2, A.getPosition().getY() + A.getHeight() / 2,
				A.getPosition().getX() + A.getWidth() / 2, A.getPosition().getY() - A.getHeight() / 2,
				B.getPosition().getX() - B.getWidth() / 2, B.getPosition().getY() + B.getHeight() / 2,
				B.getPosition().getX() + B.getWidth() / 2, B.getPosition().getY() - B.getHeight() / 2);
	}

	public boolean checkCollision(float Ax, float Ay, float AX, float AY, float Bx, float By, float BX, float BY) {
		return !(AX < Bx || BX < Ax || AY < By || BY < Ay);
	}

	public void paint(Graphics g) {
		for (Sprite s : sprites) {
			s.paint(g);
		}
	}
}
