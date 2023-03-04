package objects;

import game.Music;
import work.CodeWork;

public class SpinningLine extends Line {

	private static final long serialVersionUID = CodeWork.generateSerialVersionUIDbyClassName(SpinningLine.class);

	public Music music;
	
	private boolean isInfinity = false;
	
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
			playNote();
		}
		dirr += 0.2;
		if(dirr > 360 && !isInfinity) {
			destroy();
		}
	}
	
	public double getDir() {
		return dir;
	}
	
	@Override
	public boolean isTouchesRObject(RoundGameObject o) {
		if(dirr < 1) return false;
		return super.isTouchesRObject(o);
	}
	
	public void setInfinity(boolean isInfinity) {
		this.isInfinity = isInfinity;
	}
}
