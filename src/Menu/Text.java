package Menu;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;

public class Text implements Serializable {

	private static final long serialVersionUID = 4L;
	
	public static final int ALIGNMENT_LEFT = -1;
	public static final int ALIGNMENT_CENTER = 0;
	public static final int ALIGNMENT_RIGHT = 1;
	
	String txt;
	int fontSize = 12;
	int alignment = 0;
	int maxWidth;
	int gameWidth, gameHeight;
	
	int x, y;
	
	public Text(String txt) {
		this.txt = txt;
	}
	
	protected void setFixedFont(Graphics2D g) {
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, fontSize));
		int w = g.getFontMetrics().stringWidth(txt);
		if(w > maxWidth) {
			g.setFont(new Font(Font.DIALOG, Font.PLAIN, fontSize*maxWidth/w));
		}
	}
	
	public int getStringWidth(Graphics2D g) {
		return g.getFontMetrics().stringWidth(txt);
	}
	
	public void draw(Graphics2D g) {
		setFixedFont(g);
		int w = g.getFontMetrics().stringWidth(txt);
		int nx = 0;
		switch (alignment) {
		case ALIGNMENT_LEFT:
			nx = x;
			break;
		case ALIGNMENT_CENTER:
			nx = x - w/2;
			break;
		case ALIGNMENT_RIGHT:
			nx = x - w;
			break;
		default:
			break;
		}
		
		g.drawString(txt, nx, y);
	}
	
	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public void setDimension(int w, int h) {
		gameWidth = w;
		gameHeight = h;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setMaxWidth(int mw) {
		maxWidth = mw;
	}
	
	public void setTxt(String txt) {
		this.txt = txt;
	}
}
