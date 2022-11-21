import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.Saving;
import game.Updates;
import objects.AIBall;
import objects.SpinlineBall;
import objects.SurroundsBall;

public class JMain extends JFrame {

	/**
	 * 0L
	 */
	private static final long serialVersionUID = 0L;
	

	public static final String ARGUMENT_MAX_OBJ = "maxObj";
	public static final String ARGUMENT_ACCELERATION = "speed";
	private static MyPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		Updates.load();
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
			if(args[i].startsWith(ARGUMENT_MAX_OBJ)) {
				try {
					Updates.maxObj = Integer.parseInt(args[i].replaceFirst(ARGUMENT_MAX_OBJ, ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(args[i].startsWith(ARGUMENT_ACCELERATION)) {
				try {
					Updates.skipFrames = Integer.parseInt(args[i].replaceFirst(ARGUMENT_ACCELERATION, ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JMain frame = new JMain();
					frame.setVisible(true);
					contentPane.go();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setBounds(100, 100, 450, 300);
		contentPane = new MyPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				save();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		
		contentPane.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					save();
					System.exit(0);
				}
			}
		});
	}

	
	private void save() {
		Updates.save();
		try {
			Saving.writeObject(contentPane.getMyGame(), "game.game");
		} catch (Exception ex) {
		}
	}
}
