package bossthreads;

import java.util.Iterator;

import objects.Ball;
import objects.InfoLine;
import objects.SurroundsBall;

public class Boss2Thread extends BossThread {

	int attack = 0;
	
	@Override
	protected void step(int step) {
		if(attack == 2) {
			if(step < 15*5) {
				if(step%15 != 0) return;
				int s = step/15;
				for (int i = 0; i < 360; i+=60) {
					if(i%120 == 0) {
						addBall(i%90 == 45 ? 10 : 15, i, 150, -game.getGameWidth()*s/15 + 10, -game.getGameHeight()*s/15);
						addBall(i%90 == 45 ? 10 : 15, i, 150, game.getGameWidth()*s/15 + 10, -game.getGameHeight()*s/15);
						addBall(i%90 == 45 ? 10 : 15, i, 150, -game.getGameWidth()*s/15 + 10, game.getGameHeight()*s/15);
						addBall(i%90 == 45 ? 10 : 15, i, 150, game.getGameWidth()*s/15 + 10, game.getGameHeight()*s/15);
					} else {
						addSurroundsBalls(i%90 == 45 ? 5 : 10, i, 150, -game.getGameWidth()*s/15 + 10, -game.getGameHeight()*s/15);
						addSurroundsBalls(i%90 == 45 ? 5 : 10, i, 150, game.getGameWidth()*s/15 + 10, -game.getGameHeight()*s/15);
						addSurroundsBalls(i%90 == 45 ? 5 : 10, i, 150, -game.getGameWidth()*s/15 + 10, game.getGameHeight()*s/15);
						addSurroundsBalls(i%90 == 45 ? 5 : 10, i, 150, game.getGameWidth()*s/15 + 10, game.getGameHeight()*s/15);
					}
				}
			} else {
				if(game.getObjectsCount() == 0) {
					isEnded = true;
					attack = 3;
					setStep(0);
				}
			}
		}
		
		if(attack == 1) {
			if(step == 1) {
				game.getPlayer().setPosition(0, 0);
				int x = getPlayerX();
				int y = getPlayerY();
				
				int dist = Math.min(game.getGameWidth(), game.getGameHeight())/3;
				
				for (int i = 0; i < 360; i+=15) {
					if(i%2 == 0) {
						addSurroundsBalls(20, i, 150, (int) (x + dist * Math.cos(Math.toRadians(i))), (int) (y + dist * Math.sin(Math.toRadians(i))));
					} else {
						addBall(15, i, 150, (int) (x + dist * Math.cos(Math.toRadians(i))), (int) (y + dist * Math.sin(Math.toRadians(i))));
					}
				}
			} else if(step > 10) {
				if(game.getObjectsCount() == 0) {
					attack = 2;
					setStep(0);
				}
			}
		}
		
		if(attack == 0) {
			if(step == 1) {
				game.getPlayer().setPosition(0, 0);
				int x = getPlayerX();
				int y = getPlayerY();
				
				int dist = Math.min(game.getGameWidth(), game.getGameHeight())/3;
				
				for (int i = 0; i < 360; i+=15) {
					addSurroundsBalls(15, i, 150, (int) (x + dist * Math.cos(Math.toRadians(i))), (int) (y + dist * Math.sin(Math.toRadians(i))));
				}
			} else if(step > 10) {
				if(game.getObjectsCount() == 0) {
					attack = 1;
					setStep(0);
				}
			}
//			if(step < 360) {
//				if(step%2 == 0) {
//				}else {
//					addSurroundsBalls(10, step*2, 150, x, y);
//				}
//			} else {
//			}
		}
		
	}

	private void addBall(int diameter, Integer direction, int spawntime, int x, int y) {
		Ball ball = new Ball(game, diameter, direction);
		ball.setSpawnTime(spawntime);
		InfoLine infoLine = new InfoLine(game, ball, ball.getColor(), direction, 0, diameter, 150);
		infoLine.setPosition(x, y);
		game.addObject(x, y, infoLine);
	}
	
	private void addSurroundsBall(int diameter, Integer direction, int spawntime, int x, int y, int k) {
		SurroundsBall ball = new SurroundsBall(game, diameter, direction, k);
		ball.setSpawnTime(spawntime);
		InfoLine infoLine = new InfoLine(game, ball, ball.getColor(), direction, 0, diameter, 150);
		game.addObject(x, y, infoLine);
	}

//	private void addBall(int diameter, Integer direction, int spawntime, int x, int y) {
//		Ball ball = new Ball(game, diameter, direction);
//		ball.setSpawnTime(spawntime);
//		game.addObject(x, y, ball);
//	}

	private void addSurroundsBalls(int diameter, Integer direction, int spawntime, int x, int y) {
		addSurroundsBall(diameter, direction, spawntime, x, y, 1);
		addSurroundsBall(diameter, direction, spawntime, x, y, -1);
	}
	
//	private void addSurroundsBall(int diameter, Integer direction, int spawntime, int x, int y, int k) {
//		SurroundsBall ball = new SurroundsBall(game, diameter, direction, k);
//		ball.setSpawnTime(spawntime);
//		game.addObject(x, y, ball);
//	}

}
