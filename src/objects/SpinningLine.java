package objects;

import game.Music;

public class SpinningLine extends Line {

	public Music music;
	
	protected void playNote() {
		double dAngle = dirr;
		if(dAngle < 0) dAngle = 360 + dAngle%360;
		music.justplayNote(3, dAngle / 360d);
	}
	
	public SpinningLine(int dir, float size) {
		super(dir, size);
		dirr = 0;
		power = 4;
	}

	double dirr;
	
	@Override
	public void update() {
		dir += 0.2;
		if(dir%30 == 0) {
			System.out.println("HI");
			playNote();
		}
		dirr += 0.2;
		if(dirr > 360) {
			destroy();
		}
	}
	
	@Override
	public boolean isTouchesRObject(RoundGameObject o) {
		if(dirr < 1) return false;
		return super.isTouchesRObject(o);
	}
}
