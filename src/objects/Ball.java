package objects;

import java.awt.Color;
import java.awt.Graphics2D;

import game.Game;
import game.Sound;
import game.Updates;

public class Ball extends RoundGameObject {

	Game game;
	
	int hp;
	int mainDiameter;
	double targetDiameter;
	Color color = Color.WHITE;
	
	public Ball(Game game, int diameter, Integer direction) {
		super(diameter);
		this.game = game;
		int dir;
		if(direction == null) {
			dir = (int) (Math.random()*360);
			spawnTime = 150;
		}else {
			dir = direction.intValue();
			spawnTime = 0;
		}
		vx = diameter/7*Math.cos(Math.toRadians(dir));
		vy = diameter/7*Math.sin(Math.toRadians(dir));

		x = gameWidth/2;
		x = gameHeight/2;
		
		mainDiameter = diameter;
		hp = diameter*10;
	}
	
	int spawnTime;
	int time = 0;
	
	@Override
	public void update() {
		time++;
		moveX(vx);
		if(isTouchesBorders()) {
			vx *= -1;
			moveX(vx);
		}
		moveY(vy);
		if(isTouchesBorders()) {
			vy *= -1;
			moveY(vy);
		}
		
		if(spawnTime > 0) {
//			diameter = spawnTime;
			fillAlpha = (int) (125 + 125*Math.cos(time/5d));
			spawnTime--;
		}
		if(spawnTime < 0) {
			spawnTime = 0;
		}
		if(spawnTime == 0) {
			if(fillAlpha < 255) {
				fillAlpha+= 15;
				if(fillAlpha > 255) {
					fillAlpha = 255;
				}
			}
			if(isTouchesMouse()) {
				hp -= Updates.getUpdate(Updates.UPDATE_DAMAGE);
				setTargetDiameter(mainDiameter*1.25);
			}else {
				setTargetDiameter(mainDiameter);
				diameter = mainDiameter;
			}
			diameter = (int) targetDiameter;
			
			if(hp <= 0) {
				destroy(); 
			}
		}else {
			setTargetDiameter(mainDiameter);
		}
	}
	
	public void setTargetDiameter(double d) {
		targetDiameter = (targetDiameter-d)/1.5 + d;
	}

	transient public static Sound soundDeath = new Sound("/sounds/ball.death.mp3");
	transient public static Sound soundPop = new Sound("/sounds/ball.pop.mp3");
	
	protected void doDestroy(boolean isDestroyed) {
		Updates.$ += mainDiameter/10;
		if(isDestroyed) {
			soundDeath.play(0);
		}else {
			soundPop.play(0);
		}
	}

	public void destroy() {
		doDestroy(mainDiameter > 10);
		if(mainDiameter > 10) {
			double dir = Math.toDegrees(Math.atan2(vx, vy));
			for (int i = 0; i < 360; i+=60) {
				destroyAddObject(i, dir);
			}
		}
		super.destroy();
	}
	
	public void destroyAddObject(int i, double dir) {
		game.addObject(x, y, new Line(i,  mainDiameter/5f));
		game.addObject(x, y, new Ball(game, (int) (mainDiameter/1.5), (int) (dir+i)));
	}
	
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		super.draw(g);
	}
	
	@Override
	public boolean isTouchesRObject(RoundGameObject o) {
		if(spawnTime > 0) return false;
		return super.isTouchesRObject(o);
	}

	
	@Override
	public void turnAwayFrom(Shield shield) {
		hp -= Updates.shieldDamage;
		super.turnAwayFrom(shield);
	}
}
