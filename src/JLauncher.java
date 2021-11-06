import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game.Updates;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class JLauncher extends JFrame {

	private static JBGPanel main;
	private JPanel contentPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JLauncher frame = new JLauncher();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public static final Color BG_COLOR = new Color(75,100,255);
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public JLauncher() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, screenSize.width/2, screenSize.height/2);
//		setUndecorated(true);
		setExtendedState(MAXIMIZED_BOTH);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPane.setBackground(new Color(0,0,0));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBackground(BG_COLOR);
		scrollPane.setViewportView(panel);
		panel.setBorder(new EmptyBorder(0, 0, 50, 0));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		main = new JBGPanel();
		main.setBackground(new Color(0,0,0,0));
		main.setLayout(new BorderLayout(5, 5));;
		panel.add(main);
		main.repaint();
		
		JPanel playPanel = new JPanel();
		playPanel.setBackground(BG_COLOR);
		BoxLayout playLayout = new BoxLayout(playPanel, BoxLayout.X_AXIS);
		playPanel.setLayout(playLayout);
		playPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		main.add(playPanel, BorderLayout.SOUTH);

		ArrayList<File> jars = new ArrayList<File>();
		File userdir = new File(System.getProperty("user.dir") + "\\versions");
		String[] versionsArr;
		File[] fileArr;
		if(userdir.exists()) {
			for (File file : userdir.listFiles()) {
				String name = file.getName();
				if(name.endsWith(".jar")) {
					jars.add(file);
				}
			}
			jars.sort(new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					return o2.getName().compareTo(o1.getName());
				}
			});

			versionsArr = new String[jars.size()];
			fileArr = new File[jars.size()];
			for (int i = 0; i < fileArr.length; i++) {
				fileArr[i] = jars.get(i);
				String name = jars.get(i).getName();
				versionsArr[i] = name;
			}
			
		}else {
			JOptionPane.showMessageDialog(null, "Can't find \"versions\" directory");
			System.exit(-1);
			return;
		}
		JComboBox<String> versions = new JComboBox<String>();
		versions.setFocusable(false);
		versions.setModel(new DefaultComboBoxModel<String>(versionsArr));
		playPanel.add(versions);
		
		JLabel space = new JLabel(" ");
		space.setBorder(new EmptyBorder(0, 5, 0, 5));
		playPanel.add(space);
		
		JButton play = new JButton("Play");
		play.setFocusable(false);
		playPanel.add(play);
		play.addActionListener(e -> {
			String args = "";
			args += " " + JMain.ARGUMENT_MAX_OBJ + runArgs[0];
			args += " " + JMain.ARGUMENT_ACCELERATION + runArgs[1];
			
			String javaPath = System.getProperty("java.home") + "\\bin\\java.exe";
			String file = fileArr[versions.getSelectedIndex()].getPath();
			String command = "\"" + javaPath + "\" -jar \"" + file + "\"" + args;
			System.out.println(command);
			Thread thread = new Thread( () -> {
				try {
					setVisible(false);
					Process proc = Runtime.getRuntime().exec(command);
					String errs = "";
					BufferedReader err = new BufferedReader(new 
						     InputStreamReader(proc.getErrorStream()));
					String line = null;
					while ((line = err.readLine()) != null) {
						errs += line + "\n";
						System.err.println(line);
					}
					if(!errs.isEmpty()) {
						JTextArea errsArea = new JTextArea(errs);
						errsArea.setEditable(false);
						errsArea.setOpaque(false);
						JOptionPane.showMessageDialog(null, errsArea);
					}
					setVisible(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			thread.start();
		});

		JPanel configs = new JPanel();
		configs.setLayout(new BoxLayout(configs, BoxLayout.Y_AXIS));
		TitledBorder titledBorder = new TitledBorder("Arguments:");
		titledBorder.setTitleColor(Color.WHITE);
		titledBorder.setTitleFont(new Font(configs.getFont().getFamily(),
				Font.PLAIN, (int) (configs.getFont().getSize()*1.5)));
		configs.setBorder(titledBorder);
		configs.setBackground(BG_COLOR);
		configs.setForeground(Color.WHITE);
		panel.add(configs);

		Updates.load();
		configs.add(getArgumentPanel(0, "Max Objects", Updates.maxObj, 1000, Integer.MAX_VALUE, 500));
		configs.add(getArgumentPanel(1, "Acceleration at deceleration in ", Updates.skipFrames, 1, Integer.MAX_VALUE, 1));
	}

	int runArgs[] = new int[2]; 
	
	private JPanel getArgumentPanel(int id, String name, int value, int min, int max, int step) {
		runArgs[id] = value; 
		JPanel arg = new JPanel();
		arg.setBackground(BG_COLOR);
		arg.setBorder(new EmptyBorder(5, 10, 5, 10));
		arg.setLayout(new BoxLayout(arg, BoxLayout.X_AXIS));
		
		JLabel lbl = new JLabel(name + " ");
		lbl.setForeground(Color.WHITE);
		lbl.setBorder(new EmptyBorder(0, 0, 0, 10));
		arg.add(lbl);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(value, min, max, step));
		spinner.setBorder(new EmptyBorder(3, 5, 3, 5));
		arg.add(spinner);
		spinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				runArgs[id] = ((Integer)spinner.getValue()).intValue(); 
			}
		});
		return arg;
	}
	
	public boolean Configs_showCmd = true;
//	enum Configs {
//		SHOW_CMD,
//		MAX_OBJ,
//	}
//	
	class JBGPanel extends JPanel {

		BufferedImage bg;
		
		int ph;
		
		public JBGPanel() throws IOException {
			bg = ImageIO.read(JLauncher.class.getResourceAsStream("/launcher/background.png"));
			
//			Thread t = new Thread(() -> {
//				while (true) {
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			});
//			t.start();
			
			addComponentListener(new ComponentAdapter() {
	            @Override
	            public void componentResized(ComponentEvent e) {
	            	double scale = getScale();
	    			int h = (int) Math.ceil(bg.getHeight()*scale);
	    			ph = h;
					setPreferredSize(new Dimension(getPreferredSize().width, ph));
					setMaximumSize(new Dimension(Integer.MAX_VALUE, ph));
					setMinimumSize(new Dimension(getMaximumSize().width, ph));
					revalidate();
	            }
	        });
		}
		
//		@Override
//		public void update(Graphics g) {
//			double scale = updateSize();
//			int h = (int) (bg.getHeight()*scale);
//			g.drawImage(bg, 0, 0, (int)(bg.getWidth()*scale), h, null);
//			super.update(g);
//		}
		
		@Override
		public void paint(Graphics g) {
			double scale = getScale();
			int h = (int) (bg.getHeight()*scale);
			g.drawImage(bg, 0, 0, (int)(bg.getWidth()*scale), h, null);
			super.paint(g);
		}
		
		public double getScale() {
			double iw = bg.getWidth();
			return getWidth()/iw;
		}
	}
}
