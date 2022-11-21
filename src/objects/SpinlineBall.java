package objects;

import java.awt.Color;

import game.Game;

public class SpinlineBall extends Ball {

	public SpinlineBall(Game game, int diameter, Integer direction) {
		super(game, diameter, direction);
		color = new Color(200,255,0);
		vx /= 2;
		vy /= 2;

		hp = diameter*50;
	}
	
	@Override
	public void update() {
		Player player = game.getPlayer();
		double dist = Math.hypot(player.x - x, player.y-y);
		
		double targerDir = Math.atan2(player.y-y, player.x-x);

		vx += diameter/500d*Math.cos(targerDir);
		vy += diameter/500d*Math.sin(targerDir);

		double dir = Math.atan2(vy, vx);
		double mod = Math.hypot(vx, vy);
		if(mod > diameter/4) {
			vx = diameter/4d*Math.cos(dir);
			vy = diameter/4d*Math.sin(dir);
		}

		double _vx = vx;
		double _vy = vy;
		
		super.update();
		
		if(_vx != vx) {
			Line line = new Line(0,  mainDiameter/5f);
			line.setColor(color);
			game.addObject(x, y, line);
		}
		
		if(_vy != vy) {
			Line line = new Line(90,  mainDiameter/5f);
			line.setColor(color);
			game.addObject(x, y, line);
		}
//		if()
	}

	@Override
	public void destroyAddObject(int i, double dir) {
		SpinningLine line = new SpinningLine(i, mainDiameter/5f);
		line.setColor(color);
		game.addObject(x, y, line);
		
		game.addObject(x, y, new SpinlineBall(game, (int) (mainDiameter/2), (int) (dir+i)));
	}
}
