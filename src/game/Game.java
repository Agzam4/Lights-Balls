package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.IntStream;

import Menu.Menu;
import objects.AIBall;
import objects.Ball;
import objects.GameObject;
import objects.Player;
import objects.Shield;
import objects.SpinlineBall;
import objects.SpinningLine;
import objects.SurroundsBall;

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int STAGE_TUTORIAL = -1;
	
	private int gameWidth, gameHeight;
	private ArrayList<GameObject> objects;
	private Player player;
	private Shield shield;
	
	Menu menu;
	
	public Game() {
		objects = new ArrayList<GameObject>();
		tmpObjects = new ArrayList<GameObject>();
		player = new Player();
		player.reload();
		shield = new Shield(player);
		
		menu = new Menu();
		
		start();
		
		Updates.stage = stage;
		txtLen = 0;
	}

	int startBallSize = 10;  // 10
	int stage = STAGE_TUTORIAL;
	
	int stageTutorial = TUTORIAL_KEYS;
	private static final int TUTORIAL_KEYS = 0;
	private static final int TUTORIAL_SHIFT = 1;
	private static final int TUTORIAL_BALL = 2;
	
	private void start() {
		if(startBallSize > 50) {
			Updates.setAllUpdate(stage+1);
			Updates.$ = 0;
			startBallSize = 10;
			stage++;
		}
		
		player.reload();
		objects.clear();
		tmpObjects.clear();
		
		
		
		
		Updates.stage = stage;
		
		objects.add(new Ball(this, startBallSize+stage, null));

		if(stage%2==1) {
			objects.add(new SurroundsBall(this, startBallSize+(stage-1), null, 1));	
			objects.add(new SurroundsBall(this, startBallSize+(stage-1), null, -1));	
		}
		if(stage%4==3) {
			objects.add(new AIBall(this, startBallSize+(stage-2), null));	
		}
		if(stage%10==9) {
			objects.add(new SpinlineBall(this, startBallSize, null));	
		}
//		objects.add(ball2);		
//		objects.add(ball3);		
//		objects.add(ball4);	
		
//		objects.add(new SurroundsBall(this, startBallSize/2, null, -1));	
		
		
		setDimension(gameWidth, gameHeight);
	}

	ArrayList<GameObject> tmpObjects;
	public void addObject(double x, double y, GameObject object) {
		object.setDimension(gameWidth, gameHeight);
		object.setPosition(x, y);
		if(getObjectsCount() < Updates.maxObj) {
			objects.add(object);
		}else {
			tmpObjects.add(object);
		}
	}
	
	public void update() {
//		Updates.setAllUpdate(99);
		Mouse.mouseSize = Updates.getUpdate(Updates.UPDATE_SIZE);

		
		if(txtLen < tutorialText.length()) {
			txtLen+=0.3;
		}else if (txtLen > tutorialText.length()) {
			txtLen = tutorialText.length();
		}
//		System.out.println("\"" + tutorialText + "\" " + txtLen);

		if(stageTutorial == TUTORIAL_BALL) {
			tutorialText = "Hover mouse over the ball to destroy it";
		}
		
		if(stage == STAGE_TUTORIAL && stageTutorial != TUTORIAL_BALL) {
			if(stageTutorial == TUTORIAL_BALL) {
				player.update();
			}
			if(stageTutorial == TUTORIAL_SHIFT) {
				tutorialText = "Use Shift or Press Mouse to slow-moving ";
				player.update();
				if(player.isSlowMoved()) {
					stageTutorial = TUTORIAL_BALL;
					txtLen = 0;
				}
			}
			if(stageTutorial == TUTORIAL_KEYS) {
				tutorialText = "Use Arrow Keys to Move";
				player.update();
				if(player.isMoved()) {
					stageTutorial = TUTORIAL_SHIFT;
					txtLen = 0;
				}
			}
			return;
//			shield.update();
//			player.check(objects);
		}
		
		menu.update();
		if(menu.isNeedStart()) {
			start();
			menu.notNeedStart();
		}
		if(menu.isVisible()) return;
		
		if(getObjectsCount() == 0) {
			if(stage == STAGE_TUTORIAL) {
				stage = 0;
			}
			menu.show("You win");
			startBallSize += 5;
		}
		
		if(!player.isAlive() && stage != STAGE_TUTORIAL) {
			player.reload();
			menu.show("You lose");
		}
		
		if(tmpObjects.size() > 0) {
			if(getObjectsCount() < Updates.maxObj) {
				while (true) {
					if(tmpObjects.size() > 0 && getObjectsCount() < Updates.maxObj) {
						objects.add(tmpObjects.get(0));
						tmpObjects.remove(0);
					}else {
						break;
					}
				}
			}
		}
		
		//*
		long start = System.nanoTime();
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).update();
			if(objects.get(i).isNeedDestroy()) {
				objects.remove(i);
			}
			if(start - System.nanoTime() > 500) {
				break;
			}
		}
		start = System.nanoTime();
		for (int i = 0; i < objects.size(); i++) {
			if(objects.get(i).isTouchesRObject(shield)) {
				objects.get(i).turnAwayFrom(shield);
			}
			if(start - System.nanoTime() > 500) {
				break;
			}
		}//*/
		player.update();
		shield.update();
		player.check(objects);
	}

	int lastMX, lastMY;

	String tutorialText = "";
	double txtLen = 0;
	
	public void draw(Graphics2D g) {
		
		g.setColor(new Color(0,0,0,20)); // 20
		g.fillRect(0, 0, gameWidth, gameHeight);
		
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, gameWidth-1, gameHeight-1);
		
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		String exitStr = "Esc or Alt + F4 to exit";
		g.drawString(exitStr,
				gameWidth - g.getFontMetrics().stringWidth(exitStr + "_"),
				gameHeight - g.getFont().getSize());
		
		if(!menu.isVisible()) {
			
			if(STAGE_TUTORIAL == stage) {
				g.drawString("TUTORIAL", gameWidth/7 + 15, 15);
				g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 35));
				String newTxt = tutorialText.substring(0, (int) txtLen);
//				if(txtLen != tutorialText.length()) {
//					newTxt += "___";
//				}
				String addTxt = "";
				for (int i = (int) txtLen; i < tutorialText.length(); i++) {
					addTxt += "_";
				}
				g.drawString(newTxt + addTxt,
						g.getFontMetrics().stringWidth(tutorialText)/-2 + gameWidth/2, gameHeight/3*2);
				g.drawString(newTxt.replaceAll(".", " ") + addTxt,
						g.getFontMetrics().stringWidth(tutorialText)/-2 + gameWidth/2, gameHeight/3*2 - 35);
				
				g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
			}else {
				g.drawString((startBallSize/5-1) + " - " + (stage+1), gameWidth/7 + 15, 15);
			}
			
			objects.stream().sequential().forEach(gameObject -> {
				g.setColor(Color.WHITE);
				gameObject.draw(g);
			});
			long start = System.nanoTime();
//			for (GameObject gameObject : objects) {
//				g.setColor(Color.WHITE);
//				gameObject.draw(g);
//				if(start - System.nanoTime() > 500) {
//					break;
//				}
//			}
			player.draw(g);
			shield.draw(g);

			int mx = Mouse.getX();//-Mouse.getMouseSize()/2;
			int my = Mouse.getY();//-Mouse.getMouseSize()/2;
			g.setColor(new Color(0,255,180));
			g.setStroke(new BasicStroke(Mouse.getMouseSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawLine(mx, my, lastMX, lastMY);
			g.drawLine(mx, my, mx, my);
			//		g.fillOval(mx, my, Mouse.getMouseSize(), Mouse.getMouseSize());

			lastMX = mx;
			lastMY = my;
		}
		if(stage != STAGE_TUTORIAL) menu.draw(g);
	}

	public void setDimension(int w, int h) {
		gameWidth = w;
		gameHeight = h;
		for (GameObject gameObject : objects) {
			gameObject.setDimension(gameWidth, gameHeight);
		}

		player.setDimension(gameWidth, gameHeight);
		shield.setDimension(gameWidth, gameHeight);
		menu.setDimension(gameWidth, gameHeight);
	}
	
	
	public int getObjectsCount() {
		return objects.size();
	}
	
	
	public Player getPlayer() {
		return player;
	}
}
