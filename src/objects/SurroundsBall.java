package objects;

import java.awt.Color;

import game.Game;
import game.Updates;

public class SurroundsBall extends Ball {

	int k = 1;
	public SurroundsBall(Game game, int diameter, Integer direction, int k) {
		super(game, diameter, direction);
		color = new Color(75, 0, 255);
		this.k = k;
		hp = diameter*15;
		musicChannel = 1;
	}
	
	double speed = 3.378;
	
	transient boolean mode = false;

	@Override
	public void update() {
		Player player = game.getPlayer();
		double dir = Math.atan2(vx, vy);
		double targerDir = Math.atan2(player.x-x, player.y-y);
		double nvx = Math.sin(targerDir);
		double nvy = Math.cos(targerDir);

		if(Math.hypot(player.x - x, player.y-y) < mainDiameter + player.diameter*25) {
			vx = (vx - nvx)/2 + nvx;
			vy = (vy - nvy)/2 + nvy;
			
			dir = Math.atan2(vx, vy);
			dir += Math.toRadians(15*k);


			speed -= 3.378 / 1000d;
			if(speed < 3.378 / 5d) speed = 3.378 / 5d;
			if(!mode) {
				playNote();
			}
			mode = true;
		} else {
//			speed = 3.378;// / 2d;
			speed += 3.378 / 1000d;
			if(speed > 3.378) speed = 3.378;
			mode = false;
		}
		vx = (speed/3.378*(diameter)/5d)*Math.sin(dir);
		vy = (speed/3.378*(diameter)/5d)*Math.cos(dir);
		
		super.update();
	}
	
	@Override
	public void destroy() {
		Updates.$ += mainDiameter/7;
		if(mainDiameter > 10) {
			playDestroySound(true);
			double dir = Math.toDegrees(Math.atan2(vx, vy));
			for (int i = 0; i < 360; i+=120) {
				destroyAddObject(i, dir);
			}
		}else {
			playDestroySound(false);
		}
		isNeedDestroy = true;
	}
	
	@Override
	public void destroyAddObject(int i, double dir) {
		Line line = new Line(i,  mainDiameter/5f);
		line.setColor(color);
		game.addObject(x, y, line);

		game.addObject(x, y, new SurroundsBall(game, (int) (mainDiameter/1.5), (int) (dir+i), 1));
		game.addObject(x, y, new SurroundsBall(game, (int) (mainDiameter/1.5), (int) (dir+i), -1));
	}
}
