package objects;

import java.awt.Color;

import game.Game;

public class AIBall extends Ball {

	public AIBall(Game game, int diameter, Integer direction) {
		super(game, diameter, direction);
		color = new Color(255, 0, 100);
		hp = diameter*5;
		spawnTime = 9999998;
		
		speed = -.5;
		if(direction != null) savedDir = Math.toRadians(direction);
	}
	
	int timer = 0;

	double speed = 0;
	double savedDir = 0;
	boolean isDashSet = false;
	
	@Override
	public void update() {
		timer++;
		Player player = game.getPlayer();
		double dist = Math.hypot(player.x - x, player.y-y);
		
		double dir = Math.atan2(vx, vy);
		double targerDir = Math.atan2(player.x-x, player.y-y);
		double nvx = Math.sin(targerDir);
		double nvy = Math.cos(targerDir);
		vx = (vx - nvx)/2 + nvx;
		vy = (vy - nvy)/2 + nvy;
		
		dir = Math.atan2(vx, vy);
		dir += Math.toRadians((100d/diameter)*Math.cos(timer/10d));
		vx = (10d/diameter)*(speed > 0 ? speed : 0)*Math.sin(dir);
		vy = (10d/diameter)*(speed > 0 ? speed : 0)*Math.cos(dir);
		
		
		speed -= 0.005 * Math.random();
		if(speed <= -.75 && !(speed <= -1.25)) {
			vx += (diameter/2d)*Math.sin(savedDir);
			vy += (diameter/2d)*Math.cos(savedDir);
			dir = savedDir;
			if(spawnTime > 0 && !isDashSet) {
				Line line = new Line(0,  mainDiameter/5f);
				line.setColor(color);
				game.addObject(x, y, line);
				Line line2 = new Line(90,  mainDiameter/5f);
				line2.setColor(color);
				game.addObject(x, y, line2);
				isDashSet = true;
			}
		}else if(speed <= -1.25) {
			speed = 2;
			dir = savedDir;
			savedDir = targerDir;
			spawnTime = 0;
		} else {
			savedDir = targerDir;
		}
		
		super.update();

		dir = Math.atan2(vx, vy);
		if(dir != savedDir) {
			vx /= 5d;
			vy /= 5d;
			dir = savedDir = targerDir;
		}
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
//		Line line = new Line(i,  mainDiameter/5f);
//		line.setColor(color);
//		game.addObject(x, y, line);
		if(i % (120) == 0) {
			game.addObject(x, y, new AIBall(game, (int) (mainDiameter/1.5), (int) (dir+i)));
		}
	}
	
}
