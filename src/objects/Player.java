package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.Game;
import game.Keyboard;
import game.Mouse;
import game.Updates;

public class Player extends RoundGameObject {
	
	public Game game;
	
	public Player() {
		super(25);
		setPosition(0, 0);
		fillAlpha = 255;
		reload();
	}
	
	public void reload() { 
		startHp = hp = Updates.getUpdate(Updates.UPDATE_HP);
		isAlive = true;
	}
	
	double speed = 1;
	private int startHp, hp;

	boolean isMoved = false;
	boolean isSlowMoved = false;

	public boolean isMoved() {
		return isMoved;
	}
	
	public boolean isSlowMoved() {
		return isSlowMoved;
	}
	
	private void checkMove() {
		isMoved = true;
		if(Keyboard.isKeyPressed(Keyboard.SHIFT) || Mouse.isMousePressed()) {
			isSlowMoved = true;
		}
	}

	double fastSpeed = 99;
	double slowSpeed = .25;
	
	@Override
	public void update() {
		speed = (Mouse.isMousePressed() || Keyboard.isKeyPressed(Keyboard.SHIFT)) ? 0.3 : 1;
		
		if(game.getStage() != 4) {
			if(Keyboard.isKeyPressed(Keyboard.UP)) {
				vy += speed;
				checkMove();
			}
			if(Keyboard.isKeyPressed(Keyboard.DOWN)) {
				vy -= speed;
				checkMove();
			}
			
			boolean xMove = false; 
			if(Keyboard.isKeyPressed(Keyboard.RIGHT)) {
				vx -= speed;
//				if(vx > -speed) {
//					vx -= fastSpeed;
//				} 
//				if (vx < -speed) {
//					vx = -speed;
//				}
				xMove = true;
				checkMove();
			}
			if(Keyboard.isKeyPressed(Keyboard.LEFT)) {
				vx += speed;
				xMove = true;
				checkMove();
			}
		}
		
		borderAlpha /= 1.5;
		
		moveX(vx);
		if(isTouchesBorders()) {
			borderAlpha = 255;
			moveX(-vx);
			vx /= 2;
		}
		moveY(vy);
		if(isTouchesBorders()) {
			borderAlpha = 255;
			moveY(-vy);
			vy /= 2;
		}
		
//		if(!xMove) {
//			if(vx > slowSpeed) {
//				vx -= slowSpeed;
//			} else if(vx < -slowSpeed) {
//				vx += slowSpeed;
//			} else {
//				vx = 0;
//			}
//		}
		if(game.getStage() != 4) {
			vx *= 0.8;
			vy *= 0.8;
		}
		
		if(shieldTime > 0) {
			shieldTime--;
		}

		if(shieldTime <= 0) {
			fillAlpha = 255;
		}
		if(fillAlpha > 0) {
			fillAlpha -= 10;
			if(fillAlpha < 0) {
				fillAlpha = 0;
			}
		}
		
		if(hp <= 0) {
			isAlive = false;
		}
		
		if(game.getStage() == 4) {
//
			int mx = gameWidth/2 - Mouse.getX();
			int my = gameHeight/2 - Mouse.getY();
			
			mvx = mlx - mx;
			mvy = mly - my;

			double dist = Math.hypot(my-y, mx-x);
			double dir = Math.atan2(my-y, mx-x);
			
			double maxDist = diameter*5 + Mouse.mouseSize;
			if(dist > maxDist) {
				mn += dist/(maxDist * 10d);
			} else if(dist < maxDist) {
				mn -= maxDist/(dist * 10d);
			}
			
//			if(dist < Mouse.mouseSize*2 + diameter*2) {
//				double vdir = Math.atan2(y-my, x-mx);
//				double vmod = Math.hypot(vx, vy);
//				vx += vmod*Math.cos(vdir)/5d;
//				vy += vmod*Math.sin(vdir)/5d;
//				
//				
//			} else {

				vx += 10*Math.cos(dir)/20d;
				vy += 10*Math.sin(dir)/20d;
//			}
			

//			double cdir = Math.atan2(-y, -x);
//			vx += Math.cos(cdir)/7d;
			vy -= 2.5d/dist;//Math.sin(cdir)/7d;
			

			mlx = mx;
			mly = my;
			
			if(vx > slowSpeed) {
				vx -= slowSpeed;
			} else if(vx < -slowSpeed) {
				vx += slowSpeed;
			} else {
				vx = 0;
			}
//			if(vy > slowSpeed) {
//				vy -= slowSpeed;
//			} else if(vy < -slowSpeed) {
//				vy += slowSpeed;
//			} else {
//				vy = 0;
//			}
//			
			
			if(Mouse.isMousePressed()) {
				double vdir = Math.atan2(vy, vx);
				double vmod = Math.hypot(vx, vy);
				if(vmod > diameter/4) {
					vx = diameter/4d*Math.cos(vdir);
					vy = diameter/4d*Math.sin(vdir);
				}
			}
		}
		
		super.update();
	}

	int mlx, mly;
	double mvx, mvy;
	double mn = 0;
	
	public int shieldTime;
	
	public void check(ArrayList<GameObject> objects) {
		if(shieldTime <= 0) {
			for (GameObject gameObject : objects) {
				if(gameObject.isTouchesRObject(this)) {
					hp--;
					shieldTime = 10 + Updates.getUpdate(Updates.UPDATE_TIME);
					return;
				}
			}
		}
	}
	
	boolean isAlive = true;
	
	public boolean isAlive() {
		return isAlive;
	}
	
	double borderAlpha = 0;
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(new Color(0,255,180));
		g.fillRect(5, 5, (int) ((gameWidth/7d)/startHp*hp), 7);
		g.drawRect(5, 5, gameWidth/7, 7);
		g.setColor(new Color(0,255,180,100));

		if(game.getStage() == 4) {
			g.drawLine(gameWidth/2 - (int)x, gameHeight/2 - (int)y, Mouse.getX(),  Mouse.getY());
		}
		
		super.draw(g);

		g.setColor(new Color(0,255,180,(int) borderAlpha));
		g.drawRect((int) (width*1.5), (int) (height*1.5),
				gameWidth-1-width*3, gameHeight-1-height*3);
		
	}
	
	@Override
	public boolean isTouchesBorders() {
		return y-height*2 < gameHeight/-2 || y+height*2 > gameHeight/2 ||
				x-width*2 < gameWidth/-2 || x+width*2 > gameWidth/2 ;
	}
}
