package game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class Mouse {

	protected static int gameWidth, gameHeight;
	public static int mx, my;
	public static int mouseSize = Updates.getUpdate(Updates.UPDATE_SIZE);
	public static boolean isMousePressed;
	
	public static void link(JComponent c) {
		c.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				isMousePressed = false;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				isMousePressed = true;
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		c.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mx = e.getX();
				my = e.getY();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				mx = e.getX();
				my = e.getY();
			}
		});
	}
	
	public static void setDimension(int w, int h) {
		gameWidth = w;
		gameHeight = h;
	}
	

	public static int getX() {
		return mx;
	}
	
	public static int getY() {
		return my;
	}
	
	public static int getMx() {
		return (int) (/* gameWidth/2 - */mx + mouseSize/2);
	}
	
	public static int getMy() {
		return (int) (/* gameHeight/2 - */my + mouseSize/2);
	}
	
	public static int getMouseSize() {
		return mouseSize;
	}
	
	public static void setMouseSize(int mouseSize) {
		Mouse.mouseSize = mouseSize;
	}
	
	public static boolean isMousePressed() {
		return isMousePressed;
	}
}
