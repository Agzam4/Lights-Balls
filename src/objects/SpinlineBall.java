package objects;

import java.awt.Color;

import game.Game;

public class SpinlineBall extends Ball {

	public SpinlineBall(Game game, int diameter, Integer direction) {
		super(game, diameter, direction);
		color = new Color(200,255,0);
	}

	@Override
	public void destroyAddObject(int i, double dir) {
		SpinningLine line = new SpinningLine(i, mainDiameter/5f);
		line.setColor(color);
		game.addObject(x, y, line);
		
		game.addObject(x, y, new SpinlineBall(game, (int) (mainDiameter/2), (int) (dir+i)));
	}
}
