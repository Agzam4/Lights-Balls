package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.Keyboard;
import game.Mouse;
import game.Updates;

public class Player extends RoundGameObject {

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
	int startHp, hp;
	

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
	
	@Override
	public void update() {
		speed = (Mouse.isMousePressed() || Keyboard.isKeyPressed(Keyboard.SHIFT)) ? 0.3 : 1;
		if(Keyboard.isKeyPressed(Keyboard.UP)) {
			vy += speed;
			checkMove();
		}
		if(Keyboard.isKeyPressed(Keyboard.DOWN)) {
			vy -= speed;
			checkMove();
		}
		
		if(Keyboard.isKeyPressed(Keyboard.RIGHT)) {
			vx -= speed;
			checkMove();
		}
		if(Keyboard.isKeyPressed(Keyboard.LEFT)) {
			vx += speed;
			checkMove();
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

		vx *= 0.8;
		vy *= 0.8;
		
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
		
		super.update();
	}
	
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
