package cc;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

public class UI {
	private MainApplet main;

	public UI(MainApplet main) {
		this.main = main;
	}

	public void drawPauseScreen(Graphics g) {
		String s = "Paused";

		// setup font
		Font font = new Font("Serif", Font.PLAIN, 44);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();

		// setup placement
		int width = (int) main.getWorld().getSize().getX();
		int height = (int) main.getWorld().getSize().getY();
		int x = width / 2 - fm.stringWidth(s) / 2;
		int y = height / 2 - fm.getHeight() / 2;

		// draw paused message
		g.setColor(Color.CYAN);
		g.drawString(s, x - 1, y - 1);
		g.setColor(Color.BLUE);
		g.drawString(s, x, y);
	}

	public void drawFullStatsOverlay(Graphics g) {
		// gather stats data
		LinkedList<Circle> circles = new LinkedList<Circle>();
		int numProjectiles = 0;
		int numObstacles = 0;

		for (Sprite s : main.getWorld().getSprites()) {
			if (s instanceof Circle) {
				circles.add((Circle) s);
			} else if (s instanceof Obstacle) {
				numObstacles++;
			} else {
				numProjectiles++;
			}
		}
		Collections.sort(circles);

		// setup font
		Font font = new Font("Serif", Font.PLAIN, 18);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();

		int lineHeight = fm.getHeight();
		int width = (int) main.getWorld().getSize().getX();
		int height = (int) main.getWorld().getSize().getY();
		int x = width / 8;
		int y = width / 8 + lineHeight * 2;

		// draw background
		g.setColor(new Color(55, 55, 55, 230));
		g.fillRect(width / 8, height / 8, width * 3 / 4, height * 3 / 4);
		g.setColor(Color.RED);
		g.drawString("Full Stats", width / 8 + lineHeight / 2, height / 8 + lineHeight);

		// display circle data
		x += lineHeight;
		g.drawString("id: k|d|a alive?", x, y);
		y += lineHeight;
		g.drawString("================", x, y);
		y += lineHeight;
		ListIterator<Circle> iter = circles.listIterator(circles.size());
		for(Circle c : circles)
		{
			g.setColor(c.getColor());
			g.drawString("" + c.getId() + ": " + c.getKills() + "|" + c.getDeaths() + "|"
					+ String.format("%.2f", c.getAccuracy()) + " " + c.isAlive(), x, y);
			y += lineHeight;
		}

		// overlay data about existing objects
		if (Main.DEBUG) {
			y = width / 8 + lineHeight * 2;
			x = width - width / 2;
			g.setColor(Color.RED);
			g.drawString("Total Ticks: " + main.getTicks(), x, y);
			y += lineHeight;
			g.drawString("Time Elapsed: " + main.getTicks() / 60 + " sec", x, y);
			y += lineHeight;
			g.drawString("Total Sprites: " + main.getWorld().getSprites().size(), x, y);
			y += lineHeight;
			g.drawString("Circles: " + circles.size(), x, y);
			y += lineHeight;
			g.drawString("Projectiles: " + numProjectiles, x, y);
			y += lineHeight;
			g.drawString("Obstacles: " + numObstacles, x, y);
		}
	}
}
