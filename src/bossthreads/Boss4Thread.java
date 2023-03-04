package bossthreads;

import java.awt.Color;

import objects.Ball;
import objects.InfoLine;
import objects.SpinlineBall;
import objects.SpinningLine;
import objects.SurroundsBall;
import work.CodeWork;

public class Boss4Thread extends BossThread {

	private static final long serialVersionUID = CodeWork.generateSerialVersionUIDbyClassName(Boss4Thread.class);

	int attack = 0;
	
	SpinningLine line;
	
	float saturation = 1f;
	
	@Override
	protected void step(int step) {
		int attackLen = 5;
		if(line == null) {
			line = new SpinningLine(0, 5);
			line.setColor(Color.WHITE);
			line.setInfinity(true);
			game.addObject(0, 0, line);
			
			setStep(0);
			attack = 1;
		}
		if(line != null) {
			if(saturation < 1f) {
				saturation += .01f;
			}
			if(saturation > 1f) saturation = 1f;
			line.setColor(new Color(Color.HSBtoRGB(step/1000f, saturation, 1f)));
		}

		if(attack == 0) {
			
		} else if(attack == 1) {
			if(step < 360*attackLen) {
				double dist = Math.cos(step/100d) * Math.min(game.getGameWidth()/2, game.getGameHeight()/2) - 8;
				
				if(line != null && step%10 == 0) {
					addBall(10, (int) line.getDir(), 10, 
							(int)(dist*Math.cos(Math.toRadians(line.getDir()))), 
							(int)(dist*Math.sin(Math.toRadians(line.getDir()))));
				}
			} else if(game.getObjectsCount() == 1) {
				attack = 2;
				setStep(0);
			}
		} else if(attack == 2) {
			if(step < 360*attackLen) {
				double dist = Math.min(game.getGameWidth()/2, game.getGameHeight()/2) - 8;
				
				if(line != null && step%30 == 0) {
					addBall(15, (int) line.getDir() + 360, 10, 
							(int)(dist*Math.cos(Math.toRadians(line.getDir()))), 
							(int)(dist*Math.sin(Math.toRadians(line.getDir()))));
				}
			} else if(game.getObjectsCount() == 1) {
				attack = 3;
				setStep(0);
			}
		} else if(attack == 3) {
			
			if(step < 360*attackLen) {
				double dist = Math.cos(step/100d) * Math.min(game.getGameWidth()/2, game.getGameHeight()/2) - 8;
//				
				int x = 0;//(int)(dist*Math.cos(Math.toRadians(line.getDir())));
				int y = 0;//(int)(dist*Math.sin(Math.toRadians(line.getDir())));
				if(line != null && step%30 == 0) {
					addBall(16, (int) line.getDir(), 100, x, y);
					addSurroundsBalls(10, (int) line.getDir(), 100, x, y);
				}
			} else if(game.getObjectsCount() == 1) {
				attack = 4;
				setStep(0);
			}
		} else if(attack == 4) {
			
			if(step < 360*5) {
				if(line != null && step%60 == 0) {
					addSpinlineBall(13, (int) line.getDir(), 100, 0, 0);
				}
			} else if(game.getObjectsCount() == 1) {
				attack = 5;
				setStep(0);
				isEnded = true;
				line.destroy();
			}
		}
		
		
	}

	private void addSpinlineBall(int diameter, Integer direction, int spawntime, int x, int y) {
		SpinlineBall ball = new SpinlineBall(game, diameter, direction);
		ball.setColor(line.getColor());
		ball.setSpawnTime(spawntime);
		InfoLine infoLine = new InfoLine(game, ball, line.getColor(), direction, 0, diameter, 150);
		game.addObject(x, y, infoLine);
		infoLine.setPosition(x, y);
	}
	
	private void addBall(int diameter, Integer direction, int spawntime, int x, int y) {
		Ball ball = new Ball(game, diameter, direction);
		ball.setColor(line.getColor());
		ball.setSpawnTime(spawntime);
		InfoLine infoLine = new InfoLine(game, ball, line.getColor(), direction, 0, diameter, 150);
		game.addObject(x, y, infoLine);
		infoLine.setPosition(x, y);
	}
	
	private void addSurroundsBall(int diameter, Integer direction, int spawntime, int x, int y, int k) {
		SurroundsBall ball = new SurroundsBall(game, diameter, direction, k);
		ball.setSpawnTime(spawntime);
		InfoLine infoLine = new InfoLine(game, ball, ball.getColor(), direction, 0, diameter, 150);
		game.addObject(x, y, infoLine);
	}

	private void addSurroundsBalls(int diameter, Integer direction, int spawntime, int x, int y) {
		addSurroundsBall(diameter, direction, spawntime, x, y, 1);
		addSurroundsBall(diameter, direction, spawntime, x, y, -1);
	}
}
