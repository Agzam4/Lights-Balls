package objects;

import java.awt.Color;

import game.Game;

public class AIBall extends Ball {

	public AIBall(Game game, int diameter, Integer direction) {
		super(game, diameter, direction);
		color = new Color(255, 0, 100);
	}
	
	int timer = 0;
	
	@Override
	public void update() {
		timer++;
		Player player = game.getPlayer();
		double dir = Math.atan2(vx, vy);
		double targerDir = Math.atan2(player.x-x, player.y-y);
		double nvx = Math.sin(targerDir);
		double nvy = Math.cos(targerDir);
		vx = (vx - nvx)/2 + nvx;
		vy = (vy - nvy)/2 + nvy;
		
		dir = Math.atan2(vx, vy);
		dir += Math.toRadians((100d/diameter)*Math.cos(timer/10d));
		vx = (20d/diameter)*Math.sin(dir);
		vy = (20d/diameter)*Math.cos(dir);
		super.update();
	}
	
	
	/*
	 * 
		Player player = game.getPlayer();
		double dir = Math.atan2(vx, vy);
		double targerDir = Math.atan2(player.x-x, player.y-y);
		double nvx = Math.sin(targerDir);
		double nvy = Math.cos(targerDir);
		vx = (vx - nvx)/2 + nvx;
		vy = (vy - nvy)/2 + nvy;
		
		dir = Math.atan2(vx, vy);
		dir += Math.toRadians(Math.random()*10 - 20);
		vx = (25d/diameter)*Math.sin(dir);
		vy = (25d/diameter)*Math.cos(dir);
		super.update();
	 */
	
	@Override
	public void destroyAddObject(int i, double dir) {
		Line line = new Line(i,  mainDiameter/5f);
		line.setColor(color);
		game.addObject(x, y, line);
		
		game.addObject(x, y, new AIBall(game, (int) (mainDiameter/1.5), (int) (dir+i)));
	}
	
}
