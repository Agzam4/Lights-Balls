package bossthreads;

import java.util.Iterator;

import objects.AIBall;
import objects.Ball;
import objects.InfoLine;
import objects.SurroundsBall;

public class Boss3Thread extends BossThread {

	int attack = 0;
	
	@Override
	protected void step(int step) {
		if(attack == 2) {
			if(step < 25*5*5) {
				if(step%25 != 0) return;
				int s = step/25;

				int x = s%5;
				int y = (s-x)/5;
				
				int size = 15;
				if(y == 5) size = 20;

				
				if(x%5 == 0) {
					addAiBall(size, 0, 150, +game.getGameWidth()*x/12, +game.getGameHeight()*y/12);
					addAiBall(size, 0, 150, +game.getGameWidth()*x/12, -game.getGameHeight()*y/12);
					addAiBall(size, 0, 150, -game.getGameWidth()*x/12, +game.getGameHeight()*y/12);
					addAiBall(size, 0, 150, -game.getGameWidth()*x/12, -game.getGameHeight()*y/12);
				} else {
//					addBall(size, 45,   25, +game.getGameWidth()*x/22, +game.getGameHeight()*y/22);
//					addBall(size, -45,  25, +game.getGameWidth()*x/22, -game.getGameHeight()*y/22);
//					addBall(size, 135,  25, -game.getGameWidth()*x/22, +game.getGameHeight()*y/22);
//					addBall(size, -135, 25, -game.getGameWidth()*x/22, -game.getGameHeight()*y/22);
					
					addBall(size, 0,   25, +game.getGameWidth()*x/12, +game.getGameHeight()*y/12);
					addBall(size, 180,  25, +game.getGameWidth()*x/12, -game.getGameHeight()*y/12);
					addBall(size, 0,  25, -game.getGameWidth()*x/12, +game.getGameHeight()*y/12);
					addBall(size, -180, 25, -game.getGameWidth()*x/12, -game.getGameHeight()*y/12);
				}
				
//				for (int i = 0; i < 360; i+=60) {
//					if(i%120 == 0) {
//						addBall(i%90 == 45 ? 10 : 15, i, 150, -game.getGameWidth()*s/15 + 10, -game.getGameHeight()*s/15);
//						addBall(i%90 == 45 ? 10 : 15, i, 150, game.getGameWidth()*s/15 + 10, -game.getGameHeight()*s/15);
//						addBall(i%90 == 45 ? 10 : 15, i, 150, -game.getGameWidth()*s/15 + 10, game.getGameHeight()*s/15);
//						addBall(i%90 == 45 ? 10 : 15, i, 150, game.getGameWidth()*s/15 + 10, game.getGameHeight()*s/15);
//					} else {
//						addAiBall(i%90 == 45 ? 5 : 10, i, 150, -game.getGameWidth()*s/15 + 10, -game.getGameHeight()*s/15);
//						addAiBall(i%90 == 45 ? 5 : 10, i, 150, game.getGameWidth()*s/15 + 10, -game.getGameHeight()*s/15);
//						addAiBall(i%90 == 45 ? 5 : 10, i, 150, -game.getGameWidth()*s/15 + 10, game.getGameHeight()*s/15);
//						addAiBall(i%90 == 45 ? 5 : 10, i, 150, game.getGameWidth()*s/15 + 10, game.getGameHeight()*s/15);
//					}
//				}
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
						addAiBall(20, i, 150, (int) (x + dist * Math.cos(Math.toRadians(i))), (int) (y + dist * Math.sin(Math.toRadians(i))));
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
					addAiBall(15, i, 250, (int) (x + dist * Math.cos(Math.toRadians(i))), (int) (y + dist * Math.sin(Math.toRadians(i))));
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
	
	private void addAiBall(int diameter, Integer direction, int spawntime, int x, int y) {
		AIBall ball = new AIBall(game, diameter, direction);
		ball.setSpawnTime(spawntime);
		InfoLine infoLine = new InfoLine(game, ball, ball.getColor(), direction, 0, diameter, 150);
		game.addObject(x, y, infoLine);
	}

//	private void addBall(int diameter, Integer direction, int spawntime, int x, int y) {
//		Ball ball = new Ball(game, diameter, direction);
//		ball.setSpawnTime(spawntime);
//		game.addObject(x, y, ball);
//	}

//	private void addSurroundsBall(int diameter, Integer direction, int spawntime, int x, int y, int k) {
//		SurroundsBall ball = new SurroundsBall(game, diameter, direction, k);
//		ball.setSpawnTime(spawntime);
//		game.addObject(x, y, ball);
//	}

}
