package bossthreads;

import java.util.Iterator;

import objects.Ball;

public class Boss1Thread extends BossThread {

	int attack = 0;
	
	@Override
	protected void step(int step) {
		if(attack == 2) {
			if(step < 25*5) {
				if(step%25 != 0) return;
				int s = step/25;
				
				for (int i = 0; i < 360; i+=45) {
					addBall(i%90 == 45 ? 15 : 20, i, 150, -game.getGameWidth()*s/15 + 10, -game.getGameHeight()*s/15);
					addBall(i%90 == 45 ? 15 : 20, i, 150, game.getGameWidth()*s/15 + 10, -game.getGameHeight()*s/15);
					addBall(i%90 == 45 ? 15 : 20, i, 150, -game.getGameWidth()*s/15 + 10, game.getGameHeight()*s/15);
					addBall(i%90 == 45 ? 15 : 20, i, 150, game.getGameWidth()*s/15 + 10, game.getGameHeight()*s/15);
				}
				
//				addBall(step%30 == 0 ? 25 : 10, -step*2 + 90, 150, game.getGameWidth()/2 - 10, 0);
//				addBall(25, (270 - (step)), 150, 0, 0);
			} else {
				if(game.getObjectsCount() == 0) {
					isEnded = true;
					attack = 3;
					setStep(0);
				}
			}
		}
		
		if(attack == 1) {
			if(step < 360) {
				if(step % 60 == 0) {
					for (int i = 0; i < 360; i+=15) {
						addBall(i%2 == 0 ? 10 : 20, i, 150, 0, 0);
					}
				}
			} else {
				if(game.getObjectsCount() == 0) {
					attack = 2;
					setStep(0);
				}
			}
		}
		
		if(attack == 0) {
			if(step < 360) {
				Ball ball = new Ball(game, 10, step*2);
				ball.setSpawnTime(150);
				game.addObject(0, 0, ball);
			} else {
				if(game.getObjectsCount() == 0) {
					attack = 1;
					setStep(0);
				}
			}
		}
		
	}
	
	private void addBall(int diameter, Integer direction, int spawntime, int x, int y) {
		Ball ball = new Ball(game, diameter, direction);
		ball.setSpawnTime(spawntime);
		game.addObject(x, y, ball);
	}

}
