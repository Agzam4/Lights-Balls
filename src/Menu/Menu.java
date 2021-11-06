package Menu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Iterator;

import game.Keyboard;
import game.Updates;

public class Menu implements Serializable {

	private static final long serialVersionUID = 3L;
	
	private double menuY;
	boolean isVisible;
	boolean isNeedStart;
	int gameWidth, gameHeight;
	
	String title = "";
	
	public Menu() {
		menuY = 0;
		for (int i = 0; i < updates.length; i++) {
			updates[i] = new Button("+" + updatesText[i]);
		}
		for (int i = 0; i < updatesInfo.length; i++) {
			updatesInfo[i] = new Text("");
		}
	}
	
	public void update() {
		padding = gameHeight/10;
		stroke = gameHeight/25;
		
		if(isVisible) {
			if(menuY < -gameHeight) {
				menuY = -gameHeight;
			}
			menuY /= 1.05;
		}else {
			menuY = (menuY - gameHeight)/1.05 + menuY;
		}
		
		if(isVisible && Keyboard.isKeyPressed(Keyboard.SPACE)) {
			hide();
			isNeedStart = true;
		}
		int p = (gameHeight-padding)/8;
		
		space.setMaxWidth(gameWidth - padding*2 - stroke*2);
		space.setFontSize(p/2);
		space.setPosition(gameWidth/2, (int) (p*8.75+menuY-padding - stroke));
		
		titleText.setMaxWidth(gameWidth - padding*2 - stroke*2);
		titleText.setFontSize((int) (p*1));
		titleText.setAlignment(Text.ALIGNMENT_LEFT);
		titleText.setPosition((int) (padding+ gameWidth/25 + stroke), (int) (p*4+menuY-padding - stroke));
		titleText.setTxt(title);

		info$.setMaxWidth(gameWidth - padding*2 - stroke*2);
		info$.setFontSize(p/2);
		info$.setPosition((int) (padding+ gameWidth/25 + stroke), (int) (p*8.75+menuY-padding - stroke));
		info$.setAlignment(Text.ALIGNMENT_LEFT);
		info$.setTxt(Updates.$ + "$");
		
		for (int i = 0; i < updates.length; i++) {
			updates[i].setEnable(Updates.canUpdate(i));
			updates[i].update();
			updates[i].setMaxWidth(gameWidth - padding*2 - stroke*2);
			updates[i].setFontSize(p/2);
			updates[i].setPosition((int) (padding+ gameWidth/25 + stroke), (int) (p*(5+i)+menuY-padding - stroke - 5));
			updates[i].setAlignment(Text.ALIGNMENT_LEFT);
			
			if(updates[i].isClicked()) {
				Updates.update(i);
				updates[i].unClick();
			}
		}
		
		for (int i = 0; i < updatesInfo.length; i++) {
			updatesInfo[i].setMaxWidth(gameWidth - padding*2 - stroke*2);
			updatesInfo[i].setPosition(updates[i].getButtonWidth() + (int) (padding+ gameWidth/25 + stroke), (int) (p*(5+i)+menuY-padding - stroke - 5));
			updatesInfo[i].setFontSize(p/2);
			updatesInfo[i].setAlignment(Text.ALIGNMENT_LEFT);
			updatesInfo[i].setTxt(Updates.getCost(i) + "$ " + Updates.getLvl(i) + "LVL");
		}
//
//		setInfoText(0, Updates.getHp$(), Updates.hp-Updates.defHp);
//		setInfoText(1, Updates.getShieldTime$(), Updates.hp-Updates.defShieldTime);
//		setInfoText(2, Updates.getSize$(), Updates.size-Updates.defSize);
//		setInfoText(3, Updates.getDamage$(), Updates.damage-Updates.defDamage);
	}
	
	private void setInfoText(int id, int c, int lvl) {
		updatesInfo[id].setTxt(c + "$ " + (lvl+1) + "LVL");
	}

	int padding = 50;
	int stroke = 14;

	Text space = new Text("Press the space to continue");
	Text titleText = new Text("");
	Text info$ = new Text("");
	
	Text[] updatesInfo = new Text[4];
	
	Button[] updates = new Button[4];
	String[] updatesText = {"HP","Shield Time", "Weapon size", "Damage"};
	Button hp = new Button("+HP");
	
	public void draw(Graphics2D g) {
		g.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(Color.BLACK);
		g.fillRect(padding, (int) (menuY+padding), gameWidth-padding*2, gameHeight-padding*2);
		g.setColor(Color.WHITE);
		drawRect(g, padding, (int) (menuY+padding), gameWidth-padding*2, gameHeight-padding*2);

		space.draw(g);
		info$.draw(g);
		titleText.draw(g);
		for (Button button : updates) {
			button.draw(g);
		}

		g.setColor(Color.WHITE);
		for (Text txt : updatesInfo) {
			txt.draw(g);
		}
	}
	
	private void drawRect(Graphics2D g, int x, int y, int w, int h) {
		g.drawPolygon(new int[] {
				x, x+w, x+w, x
		}, new int[] {
				y, y, y+h, y+h
		}, 4);
		
//		g.drawLine(x, y, x+w, y);
//		g.drawLine(x, y+h, x+w, y+h);
//		g.drawLine(x, y, x, y+h);
//		g.drawLine(x+w, y, x+w, y+h);
//		g.drawRect(padding, (int) (menuY+padding), gameWidth-padding*2, gameHeight-padding*2);
	}
	
	public void setDimension(int w, int h) {
		gameWidth = w;
		gameHeight = h;
		
		space.setDimension(w, h);
//		if(!isVisible) {
//			menuY = -gameHeight;
//		}
	}
	
	public void show(String title) {
		isVisible = true;
		this.title = title;
//		menuY = 0;
	}
	
	public void hide() {
		isVisible = false;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public boolean isNeedStart() {
		return isNeedStart;
	}
	
	public void notNeedStart() {
		isNeedStart = false;
	}
}
