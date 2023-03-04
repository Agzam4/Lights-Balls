package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public class Line extends GameObject implements Serializable {

	float size;
	double dir;
	int len;
	Color color = Color.WHITE;

	public Line(int dir, float size) {
		this.size = size;
		this.dir = dir;
		len = (int) Math.hypot(gameWidth, gameHeight);
	}
	
	public Line(double dir, float size) {
		this.size = size;
		this.dir = dir;
		len = (int) Math.hypot(gameWidth, gameHeight);
	}
	
	@Override
	public void setDimension(int w, int h) {
		len = (int) Math.hypot(w, h);
		super.setDimension(w, h);
	}
	
	@Override
	public void update() {
		if(power < 1) {
			destroy();
		}
		power--;
	}

	int power = 5;
	
	@Override
	public void draw(Graphics2D g) {
		if(power < 1) return;
		g.setColor(color);
		g.setStroke(new BasicStroke(size));
		g.drawLine(getX(), getY(), getX2(), getY2());
		g.drawLine(getX(), getY(), getX3(), getY3());
	}

	private int getX2() {
		return getX(x+len*Math.cos(Math.toRadians(dir)));
	}
	
	private int getY2() {
		return getY(y+len*Math.sin(Math.toRadians(dir)));
	}
	
	private int getX3() {
		return getX(x+len*Math.cos(Math.toRadians(dir+180)));
	}
	
	private int getY3() {
		return getY(y+len*Math.sin(Math.toRadians(dir+180)));
	}

	@Override
	public boolean isTouchesMouse() {
		return false;
	}

	@Override
	public boolean isTouchesRObject(RoundGameObject o) {// LINE/CIRCLE
		if(power >= 5) return false;
		
		double r = o.diameter/2 + size/2;
		
		double x1 = o.getX() - getX() + r;
		double x2 = o.getX() - getX2() + r;
		double y1 = o.getY() - getY() + r;
		double y2 = o.getY() - getY2() + r;

		double dx = x2 - x1;
		double dy = y2 - y1;

		double dr = Math.hypot(dx, dy);
		
		double D = x1*y2 - x2*y1;

		double d = r*r*dr*dr - D*D;
		
		return d >= 0;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}
