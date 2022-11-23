package bossthreads;

import java.io.Serializable;

import game.Game;

public abstract class BossThread implements Serializable {

	private int step;
	protected boolean isEnded = false;
	
	protected Game game;
	
	public void update() {
		step(step);
		step++;
	}

	protected abstract void step(int step);
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public boolean isEnded() {
		return isEnded;
	}
	
	public void setStep(int step) {
		this.step = step;
	}
}
