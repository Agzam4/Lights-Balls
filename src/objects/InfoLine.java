package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import game.Game;

public class InfoLine extends GameObject {

	private int gameHypot;
	private double deg;
	private float size;
	private float startSize;
	private float dSize;

	private int time;
	private int maxtime;
	
	private RoundGameObject spawnObject;
	private int dcr, dcg, dcb;
	private int cr, cg, cb;
	private Game game;
	

	private int x1, x2;
	private int y1, y2;
	
	public InfoLine(Game game, RoundGameObject spawnObject, Color color, double deg, float startSize, float endSize, int time) {
		this.game = game;
		this.spawnObject = spawnObject;
		this.time = maxtime = time;

		dcr = color.getRed();
		dcg = color.getGreen();
		dcb = color.getBlue();
		this.deg = deg;
		
		this.startSize = startSize;
		dSize = endSize - startSize;
	}
	
	@Override
	public void update() {
		if(gameHypot == 0) {
			gameHypot = (int) Math.hypot(gameWidth, gameHeight);
			x1 = getX2();
			x2 = getX3();
			y1 = getY2();
			y2 = getY3();
		}
		
		time--;
		size = dSize*time/maxtime;
//		cr = dcr*time/maxtime;
//		cg = dcg*time/maxtime;
//		cb = dcb*time/maxtime;
		if(time <= 0) {
			game.addObject(-x, -y, spawnObject);
			destroy();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		int xx = (int) x + gameWidth/2;
		int yy = (int) y + gameHeight/2;
		int size = (int) (startSize + this.size*10);
//		g.setStroke(new BasicStroke(size)); // 
		g.setColor(new Color(dcr,dcg,dcb));
		g.drawOval(xx-size/2, yy-size/2, size, size);
//		g.drawLine(getX(), getY(), getX2(), getY2());
//		g.drawLine(getX(), getY(), getX3(), getY3());
//		g.drawLine(x1, y1, x2, y2);
	}

	private int getGameHypot() {
		return (int) Math.hypot(gameWidth, gameHeight);
	}
	
	private int getX2() {
		return getX(x+gameHypot*Math.cos(Math.toRadians(deg)));
	}
	
	private int getY2() {
		return getY(y+gameHypot*Math.sin(Math.toRadians(deg)));
	}
	
	private int getX3() {
		return getX(x+gameHypot*Math.cos(Math.toRadians(deg+180)));
	}
	
	private int getY3() {
		return getY(y+gameHypot*Math.sin(Math.toRadians(deg+180)));
	}
	
	@Override
	public boolean isTouchesMouse() {
		return false;
	}

	@Override
	public boolean isTouchesRObject(RoundGameObject object) {
		return false;
	}
}
