import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import game.Game;
import game.Keyboard;
import game.Mouse;
import game.Saving;
import game.Updates;

public class MyPanel extends JPanel {

	/**
	 * 2L
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Focusable - true
	 * Background - Black
	 */
	public MyPanel() {
		setFocusable(true);
		setBackground(Color.BLACK);
		Mouse.link(this);
		Keyboard.link(this);
	}

	public static int imageType = BufferedImage.TYPE_INT_RGB;
	
	BufferedImage game;
	public static long sleep = 5;
	
	public void go() {
		updateImage();
		Thread update = new Thread(() -> {
			long start = System.nanoTime();
			long wait = 0;
			long lags = 0;
			int skipFrames = 0;
			while (true) {
				start = System.nanoTime();
				if(game.getWidth() != getWidth() || 
						game.getHeight() != getHeight()) {
					updateImage();
				}
//				long ii = System.nanoTime();
//				for (int i = 0; i < lags+sleep; i+=sleep) {
//					System.out.print(i + " ");
//					if(ii - System.nanoTime() > 1000) {
//						break;
//					}
//				}
				for (int i = 0; i < skipFrames; i++) {
					update();
				}
				draw();
				wait = sleep*skipFrames - (System.nanoTime() - start)/1_000_000;
				if(wait < 0) {
					lags = -wait;
					wait = 0;
					if(lags > sleep*Updates.skipFrames) {
						skipFrames++;
					}
				}else {
					skipFrames=1;
				}
				if(wait > 0) {
					try {
						Thread.sleep(wait);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		update.start();
	}
	
	private void updateImage() {
		double scale = 1;
		game = new BufferedImage((int) (getWidth()*scale), (int) (getHeight()*scale), imageType);
				
		Graphics2D g = (Graphics2D)game.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		game = toCompatibleImage(game);
		
		myGame.setDimension(game.getWidth(), game.getHeight());
		Mouse.setDimension(game.getWidth(), game.getHeight());
	}
	
	Game myGame = loadGame();

	public void update() {
		myGame.update();
	}
	
	private Game loadGame() {
		try {
			return (Game) Saving.readObject("game.game");
		} catch (ClassNotFoundException | IOException e) {
//			e.printStackTrace();
		}
		return new Game();
	}

	public void draw() {
		Graphics2D g = (Graphics2D) game.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		myGame.draw(g);
		g.dispose();
		
		Graphics2D pg = (Graphics2D) getGraphics();
		pg.drawImage(game, 0, 0, getWidth(), getHeight(), null);
		pg.dispose();
	}
	
	private BufferedImage toCompatibleImage(BufferedImage image)
	{
	    // obtain the current system graphical settings
	    GraphicsConfiguration gfxConfig = GraphicsEnvironment.
	        getLocalGraphicsEnvironment().getDefaultScreenDevice().
	        getDefaultConfiguration();

	    /*
	     * if image is already compatible and optimized for current system 
	     * settings, simply return it
	     */
	    if (image.getColorModel().equals(gfxConfig.getColorModel()))
	        return image;

	    // image is not optimized, so create a new image that is
	    BufferedImage newImage = gfxConfig.createCompatibleImage(
	            image.getWidth(), image.getHeight(), image.getTransparency());

	    // get the graphics context of the new image to draw the old image on
	    Graphics2D g2d = newImage.createGraphics();

	    // actually draw the image and dispose of context no longer needed
	    g2d.drawImage(image, 0, 0, null);
	    g2d.dispose();

	    // return the new optimized image
	    return newImage; 
	}
	
	public Game getMyGame() {
		return myGame;
	}
}
