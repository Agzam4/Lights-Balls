package Menu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

import game.Mouse;

public class Button extends Text {

	public Button(String txt) {
		super(txt);
	}
	
	int fontSizeDef;
	int nx;
	int nw;
	
	
	public int getButtonWidth() {
		return (int) (nw + stroke);
	}
	
	boolean onMouse;
	
	double dFontSize;
	
	public void update() {
		int mx = Mouse.getX();
		int my = Mouse.getY();
		int bx2 = (int) (nx+nw+stroke);
		int by1 = (int) (y-stroke);
		onMouse = mx > nx && mx < bx2 && my > by1 && my < y;

		if(isEnable) {
			if(onMouse) {
				setDGray(25);
				isClicked = Mouse.isMousePressed();
			}else {
				setDGray(255);
				isClicked = false;
			}
		}else {
			setDGray(100);
		}
		if(!isClicked) {
			clickTime = 0;
			clickTiming = 100;
		}

		if(clickTime > 0) clickTime--;
		
		gray = (int) dGray;
		
	}
	
	boolean isClicked;

	double clickTiming;
	int clickTime;
	
	public void unClick() {
		clickTime = (int) clickTiming;
		clickTiming = clickTiming/1.15;
	}
	
	public boolean isClicked() {
		if(clickTime > 0) return false;
		return isClicked;
	}
	
	public void setDGray(double d) {
		dGray = (dGray - d)/1.75 + d;
	}
	
	float stroke;
	double dGray;
	int gray;
	
	@Override
	public void draw(Graphics2D g) {
		setFixedFont(g);
		int ny = y - g.getFont().getSize()/2 + 2;
		stroke = g.getFont().getSize()+2;
		if(stroke < 1.001f) {
			stroke = 1.001f;
		}
		nx = 0;
		nw = getStringWidth(g);
		switch (alignment) {
		case ALIGNMENT_LEFT:
			nx = x;
			break;
		case ALIGNMENT_CENTER:
			nx = x - getStringWidth(g)/2;
			break;
		case ALIGNMENT_RIGHT:
			nx = x + getStringWidth(g);
			break;
		default:
			break;
		}

		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawLine(nx, ny, nx+getStringWidth(g), ny);
		g.setStroke(new BasicStroke(stroke-1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(new Color(gray,gray,gray));
		g.drawLine(nx, ny, nx+getStringWidth(g), ny);
		g.setColor(new Color(255-gray,255-gray,255-gray));
		
		super.draw(g);
	}
	
	boolean isEnable = true;
	
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
}
