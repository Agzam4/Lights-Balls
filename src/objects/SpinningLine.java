package objects;

public class SpinningLine extends Line {

	public SpinningLine(int dir, float size) {
		super(dir, size);
		dirr = 0;
		power = 4;
	}

	double dirr;
	
	@Override
	public void update() {
		dir += 0.2;
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
