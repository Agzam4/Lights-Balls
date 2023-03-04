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
import bossthreads.Boss1Thread;
import bossthreads.Boss2Thread;
import bossthreads.Boss3Thread;
import bossthreads.Boss4Thread;
import bossthreads.BossThread;
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
	
	private transient Music music;
	
	Menu menu;
	
	public Game() {
		
		objects = new ArrayList<GameObject>();
		tmpObjects = new ArrayList<GameObject>();
		player = new Player();
		player.game = this;
		player.reload();
		shield = new Shield(player);
		
		menu = new Menu();
		
		start();
		
		loadMusic();
		
		Updates.stage = stage;
		txtLen = 0;
	}

	int startBallSize = 10;  // 10
	int stage = STAGE_TUTORIAL;
	
	int stageTutorial = TUTORIAL_KEYS;
	private static final int TUTORIAL_KEYS = 0;
	private static final int TUTORIAL_SHIFT = 1;
	private static final int TUTORIAL_BALL = 2;
	private static final int GAME_END = 4;
	
//	private static final String[] levels = {
//			"W",
//			"WWB",
//			"WWWR",
//			"WBY"
//	};
	
	private transient boolean isRecording = false;
	
	BossThread bossThread;
	
	private void start() {
//		getMusic().clearLog();
		
		if(startBallSize > 50) {
			if(stage == 0) {
				bossThread = new Boss1Thread();
			} else if(stage == 1) {
				bossThread = new Boss2Thread();
			} else if(stage == 2) {
				bossThread = new Boss3Thread();
			} else if(stage == 3){
				bossThread = new Boss4Thread();
			} else {
				startBallSize = 55;
			}
			if(bossThread != null) bossThread.setGame(this);
		}
		
		if(startBallSize > 55) { // 55
			bossThread = null;
			Updates.setAllUpdate(stage+1);
			Updates.$ = 0;
			startBallSize = 10;
			stage++;
		}
		
		if(stage == 4 && startBallSize > 10) {
			startBallSize = 10;
		}
		
		player.reload();
		objects.clear();
		tmpObjects.clear();
		
		Updates.stage = stage;
		
		if(bossThread == null) {
			objects.add(new Ball(this, startBallSize+stage, null));
			
			
			int count = Math.min(3, stage);
			
			int stageNum = stage;
			if(stage >= 6) stageNum++;
//			if(stage >= 7) stageNum+=3;
			
			if(stageNum%2==1 && stage != 7 || stage == 8) {
				objects.add(new SurroundsBall(this, startBallSize+(stage-1), null, 1));	
				objects.add(new SurroundsBall(this, startBallSize+(stage-1), null, -1));
				count--;
			}
			if(stageNum%3==2 || stage == 7 || stage == 8) {
				objects.add(new AIBall(this, startBallSize+(stage-2), null));	
				count--;
			}
			if(stageNum%4==3 || stage == 7 || stage == 8) {
				objects.add(new SpinlineBall(this, startBallSize, null));	
				count--;
			}
			
			for (int i = 0; i < count; i++) {
				objects.add(new Ball(this, startBallSize+stage, null));
			}
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
	
	private transient boolean waitMusic = true;
	
	public void update() {
		if(music == null) {
			loadMusic();
		}
		
		if(!getMusic().isLoaded() && waitMusic) {
			if(Keyboard.isKeyPressed(Keyboard.SPACE)) {
				waitMusic = false;
				Keyboard.releaseKey(Keyboard.SPACE);
			}
			return;
		}
		
		if(bossThread != null) {
			bossThread.update();
		}
		
//		if(Keyboard.isKeyPressed(Keyboard.RECORD)) {
//			isRecording = !isRecording;
//			if(isRecording) {
//				getMusic().clearLog();
//			} else {
//				System.out.println(getMusic().getLog().toString());
//			}
//			Keyboard.releaseKey(Keyboard.RECORD);
//		}
		
		
//		System.out.println(stage);
//		Updates.setAllUpdate(99);
		Mouse.mouseSize = Updates.getUpdate(Updates.UPDATE_SIZE);

//		if(Keyboard.isKeyPressed(Keyboard.UP) && Keyboard.isKeyPressed(Keyboard.DOWN) && Keyboard.isKeyPressed(Keyboard.SPACE) && objects.size() > 0) {
//			objects.clear();
//			Keyboard.releaseKey(Keyboard.SPACE);
//		}
//
//		if(Keyboard.isKeyPressed(Keyboard.LEFT) && Keyboard.isKeyPressed(Keyboard.RIGHT) && Keyboard.isKeyPressed(Keyboard.SPACE) && objects.size() > 0) {
//			Updates.$ += 100;
//			Keyboard.releaseKey(Keyboard.SPACE);
//		}
//		
//		
//		if(Keyboard.isKeyPressed(Keyboard.RECORD) && Keyboard.isKeyPressed(Keyboard.SPACE)) {
//			player.kill();
//			Keyboard.releaseKey(Keyboard.SPACE);
//		}
		
		if(txtLen < tutorialText.length()) {
			txtLen+=0.3;
		}else if (txtLen > tutorialText.length()) {
			txtLen = tutorialText.length();
		}
//		System.out.println("\"" + tutorialText + "\" " + txtLen);

		if(stageTutorial == TUTORIAL_BALL) {
			tutorialText = "Hover mouse over the ball to destroy it ";
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
				tutorialText = "Use WASD to Move ";
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
		
		if(stage == GAME_END) {
			tutorialText = "Thanks for playing! ";
		}
		
		menu.update();
		if(menu.isNeedStart()) {
			start();
			menu.notNeedStart();
		}
		if(menu.isVisible()) return;
			
		if(getObjectsCount() == 0) {
			boolean isWin = true;
			if(bossThread != null) {
				isWin = bossThread.isEnded();
			}
			if(isWin) {
				if(stage == STAGE_TUTORIAL) {
					stage = 0;
				}
				menu.show("You win");
				Updates.$ += startBallSize * 50 * (stage+1);
				startBallSize += 5;
			}
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

	private transient double progressWD = 0;
	private transient int loadingtimer = 0;
	private transient double loadingk = 1;
	private transient boolean needshowendprogress = false;
	
	public void draw(Graphics2D g) {
		g.setColor(new Color(0,0,0,20)); // 20
		g.fillRect(0, 0, gameWidth, gameHeight);

		
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, gameWidth-1, gameHeight-1);

		boolean isMusicLoaded = getMusic().isLoaded();
		
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		String exitStr = (!isMusicLoaded ? "LOADING |" : "") + "Esc or Alt + F4 to exit";
		
		if(stage == GAME_END) {
			exitStr = "Game by Agzam4 | This is end of game, just press Esc or Alt + F4 to exit";
		}
		
		g.drawString(exitStr,
				gameWidth - g.getFontMetrics().stringWidth(exitStr + "_"),
				gameHeight - g.getFont().getSize());

		if((!isMusicLoaded || needshowendprogress) && waitMusic) {
			loadingtimer++;
			String progresstext = "Loading music";
			String progresssumtext = "(press the space bar to load asynchronously)";
			g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 35));

			int progressW = gameWidth/2; // g.getFontMetrics().stringWidth(progresstext)*2
			int progressH = 35;
			
			int progressw = progressW*getMusic().getMusicLoadedCount()/getMusic().getNeedLoadMusicCount();
			
			progressWD = (progressWD - progressw)/1.25 + progressw;

			g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

			double k = 1 - Math.abs(Math.sin(Math.PI/2d * getMusic().getMusicLoadedCount()/getMusic().getNeedLoadMusicCount()));
			loadingk = (loadingk - k)*0.8 + k;
			
			g.setColor(new Color(0,255,180));
			int rw = (int) ((gameWidth-progressW)*loadingk + progressW);
			int rh = (int) ((gameHeight-progressH)*loadingk + progressH);
			g.drawRect((gameWidth-rw)/2, (gameHeight-rh)/2, rw, rh);
			
//			for (double kk = loadingk; kk < k; kk+=0.1) {
////				double k = ((loadingtimer + i)/50d)%1;
//				
//			}
			g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			
			g.setColor(new Color(0,255,180));
			g.fillRect((int) ((gameWidth-progressWD)/2), (gameHeight-progressH)/2, (int) progressWD, progressH);
			g.drawRect((gameWidth-progressW)/2, (gameHeight-progressH)/2, progressW, progressH);

			g.setColor(Color.WHITE);
			g.drawString(progresstext, g.getFontMetrics().stringWidth(progresstext)/-2 + gameWidth/2, gameHeight/2 + progressH/2 + 35);

			g.setColor(Color.LIGHT_GRAY);
			g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
			g.drawString(progresssumtext, g.getFontMetrics().stringWidth(progresssumtext)/-2 + gameWidth/2, gameHeight/2 + progressH/2 + 55);
		
			if(isMusicLoaded) {
				needshowendprogress = false;
				g.setColor(new Color(0,255,180));
				g.fillRect(0, 0, gameWidth, gameHeight);
			} else {
				needshowendprogress = true;
			}
			return;
		}
		
		if(!menu.isVisible()) {
			
			if(STAGE_TUTORIAL == stage || stage == GAME_END) {
				g.drawString("TUTORIAL", gameWidth/7 + 15, 15);
				g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 35));
				String newTxt = tutorialText.substring(0, (int) Math.min(tutorialText.length()-1, txtLen));
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
	
	public int getStage() {
		return stage;
	}
	
	public Music getMusic() {
		if(music == null) {
			loadMusic();
		}
		return music;
	}

	transient boolean isMusicLoading = false;
	
	private void loadMusic() {

		if(music == null) music = new Music();//new Music[4];

		if(!isMusicLoading) {
			waitMusic = true;
			new Thread(() -> {
				music.load();
				music.run();
			}).start();
		}
		isMusicLoading = true;
//		for (int i = 0; i < music.length; i++) {
//			music[i] = musicTypes[i], musicTime[i], musicCount[i]);
//			music[i].run();
//		}		
	}
	
	public int getGameWidth() {
		return gameWidth;
	}
	
	public int getGameHeight() {
		return gameHeight;
	}
}
