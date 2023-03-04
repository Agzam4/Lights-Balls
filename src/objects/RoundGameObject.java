package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import game.Mouse;

public class RoundGameObject extends GameObject {

	int diameter;
	
	public RoundGameObject(int diameter) {
		this.diameter = diameter;
		width = diameter;
		height = diameter;
	}
	
	@Override
	public void update() {
	}

	@Override
	public void draw(Graphics2D g) {
		width = diameter;
		height = diameter;
		g.setStroke(new BasicStroke(1f));
		g.drawOval(getX(), getY(), width, height);
		if(fillAlpha > 0) {
			Color c = g.getColor();
			if(fillAlpha < 240) {
				g.setColor(new Color(c.getRed()*fillAlpha/255, c.getGreen()*fillAlpha/255, c.getBlue()*fillAlpha/255, fillAlpha));
			} else {
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), fillAlpha));
			}
			g.fillOval(getX(), getY(), width, height);
			g.setColor(c);
		}
	}

	private int getCenterX() {
		return (int) (getX() + width/2);
	}
	

	private int getCenterY() {
		return (int) (getY() + height/2);
	}
	
	int fillAlpha;

	@Override
	public boolean isTouchesMouse() {
		return Math.hypot(Mouse.getX()-getCenterX(), Mouse.getY()-getCenterY()) < diameter/2 + Mouse.getMouseSize()/2;
	}

	@Override
	public boolean isTouchesRObject(RoundGameObject o) {
		return Math.hypot(x-o.x, y-o.y) < diameter/2 + o.diameter/2;
	}
	
	public int getDiameter() {
		return diameter;
	}
	
}
