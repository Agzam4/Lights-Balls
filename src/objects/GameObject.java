package objects;

import java.awt.Graphics2D;
import java.io.Serializable;

public abstract class GameObject implements Serializable {
	
	private static final long serialVersionUID = 5L;
	
	protected double x = 0, y = 0;
	protected double vx, vy;
	protected int width, height;
	
	protected boolean isNeedDestroy = false;

	protected int gameWidth, gameHeight;

	public abstract void update();

	public abstract void draw(Graphics2D g);
	public abstract  boolean isTouchesMouse();
	public abstract  boolean isTouchesRObject(RoundGameObject object);
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDimension(int w, int h) {
		gameWidth = w;
		gameHeight = h;
	}

	public void moveX(double vx2) {
		this.x += vx2;
	}
	
	public void moveY(double y) {
		this.y += y;
	}

	
	public int getX() {
		return getX(x);
	}
	
	public int getY() {
		return getY(y);
	}
	public int getX(double x) {
		return (int) (gameWidth/2 - x - width/2);
	}
	
	public int getY(double y) {
		return (int) (gameHeight/2 - y - height/2);
	}
	
	public void destroy() {
		isNeedDestroy = true;
	}
	
	public boolean isNeedDestroy() {
		return isNeedDestroy;
	}
	
	public boolean isTouchesBorders() {
		return y-height/2 < gameHeight/-2 || y+height/2 > gameHeight/2 ||
				x-width/2 < gameWidth/-2 || x+width/2 > gameWidth/2 ;
	}
	
	public void drawDebug(Graphics2D g) {
		g.drawString((int)x + ":" + (int)y + " " + gameWidth + "x" + gameHeight, 35, 35);
	}

	public void turnAwayFrom(Shield shield) {
		double speed = Math.hypot(vx, vy);
		double dir = Math.atan2(x-shield.x, y-shield.y);
		vx = speed*Math.sin(dir);
		vy = speed*Math.cos(dir);
	}
}
