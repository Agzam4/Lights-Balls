package objects;

import java.awt.Color;
import java.awt.Graphics2D;


public class Shield extends RoundGameObject {

	Player player;
	public Shield(Player player) {
		super(0);
		this.player = player;
		fillAlpha = 50;
	}
	
	double size = 0;
	
	@Override
	public void update() {
		if(player.shieldTime > 0) {
			size += 10;
		}else {
			size /= 1.5;
		}
		diameter = (int) size;
		
		setPosition(player.x, player.y);
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(new Color(0,255,180,100));
		super.draw(g);
	}
}
