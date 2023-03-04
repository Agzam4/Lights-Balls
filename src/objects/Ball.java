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
	
	int musicChannel = 0;
	
	protected void playNote() {
		double dAngle = Math.toDegrees(Math.atan2(vy, vx));
		if(dAngle < 0) dAngle = 360 + dAngle%360;
		game.getMusic().playNote(musicChannel, dAngle / 360d);
	}
	
	@Override
	public void update() {
		time++;
		moveX(vx);
		if(isTouchesBorders()) {
			vx *= -1;
			moveX(vx);
			playNote();
		}
		moveY(vy);
		if(isTouchesBorders()) {
			vy *= -1;
			moveY(vy);
			playNote();
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
		
		/*
		 ***************************************************************************************************** 
		 ***************************************************************************************************** 
		 ***************************************************************************************************** 
		 ***************************************************************************************************** 
		 ***************************************************************************************************** 
		 */
		if(game.getStage() == 4) {
//			Player player = game.getPlayer();
//			double dist = Math.hypot(player.x - x, player.y-y);
//			if(dist < 1) dist = 1;
//			double targerDir = Math.atan2(player.y-y, player.x-x);
//			
//			vx += Math.cos(targerDir)*diameter/5d/dist;
//			vy += Math.sin(targerDir)*diameter/5d/dist;
//			
//			int ky = -1;
//			if(game.getPlayer().y < 0) ky = 1;
			
//			int kx = -1;
//			if(game.getPlayer().x < 0) kx = 1;

//			vx -= diameter*kx/50d;
//			vy -= .25d;
//			
//			double dir = Math.atan2(vy, vx);
//			double mod = Math.hypot(vx, vy);
//			if(mod > diameter/2) {
//				vx = diameter/2d*Math.cos(dir);
//				vy = diameter/2d*Math.sin(dir);
//			}
		}
	}
	
	public void setTargetDiameter(double d) {
		targetDiameter = (targetDiameter-d)/1.5 + d;
	}

	transient public static Sound soundDeath = new Sound("/sounds/ball.death.mp3");
	transient public static Sound soundPop = new Sound("/sounds/ball.pop.mp3");
	
	protected void doDestroy(boolean isDestroyed) {
		Updates.$ += mainDiameter/10 * (game.getStage()+1);
		playDestroySound(isDestroyed);
	}
	
	protected void playDestroySound(boolean isDestroyed) {
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
		Line line = new Line(i,  mainDiameter/5f);
		line.setColor(color);
		game.addObject(x, y, line);
		Ball ball = new Ball(game, (int) (mainDiameter/1.5), (int) (dir+i));
		ball.setColor(color);
		game.addObject(x, y, ball);
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
	
	public void setSpawnTime(int spawnTime) {
		this.spawnTime = spawnTime;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
