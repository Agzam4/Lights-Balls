package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

public class Keyboard {

	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	public static final int SPACE = 4;
	public static final int SHIFT = 5;
	public static final int RECORD = 6;
	public static boolean[] keys = new boolean[7];
	
	public static void link(JComponent c) {
		c.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				set(false, e.getKeyCode());
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				set(true, e.getKeyCode());
			}
			
			private void set(boolean b, int key) {
				switch (key) {
				case KeyEvent.VK_D:
					keys[RIGHT] = b;
					break;
				case KeyEvent.VK_A:
					keys[LEFT] = b;
					break;
				case KeyEvent.VK_W:
					keys[UP] = b;
					break;
				case KeyEvent.VK_S:
					keys[DOWN] = b;
					break;
				case KeyEvent.VK_SPACE:
					keys[SPACE] = b;
					break;
				case KeyEvent.VK_SHIFT:
					keys[SHIFT] = b;
					break;
				case KeyEvent.VK_R:
					keys[RECORD] = b;
					break;
				default:
					break;
				}
			}
		});
	}
	
	public static boolean isKeyPressed(int key) {
		return keys[key];
	}
	
	public static void releaseKey(int key) {
		keys[key] = false;
	}
}
