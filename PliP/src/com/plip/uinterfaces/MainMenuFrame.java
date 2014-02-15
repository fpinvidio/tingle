package com.plip.uinterfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Mat;

import com.plip.eventhandlers.events.TrayArrivalEvent;
import com.plip.eventhandlers.listeners.GenericEventListener;
import com.plip.exceptions.persistence.ImageNotFoundException;
import com.plip.system.config.trainers.PlipTrainer;
import com.plip.system.monitors.MainSystemMonitor;

public class MainMenuFrame extends JFrame implements GenericEventListener {

	public MainSystemMonitor msm = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane logScrollPane;
	private JTextArea logTextPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenuFrame frame = new MainMenuFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenuFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("PliP");
		
		setBounds(100, 100, 1300, 900);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuBar.setBackground(Color.WHITE);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmAboutPliP = new JMenuItem("About Plip");
		mntmAboutPliP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		mnFile.add(mntmAboutPliP);
		
		JMenuItem mntmPreferences= new JMenuItem("Preferences");
		mntmAboutPliP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		mnFile.add(mntmPreferences);

		JMenuItem mntmExit = new JMenuItem("Quit Plip");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				InputEvent.ALT_MASK));
		mnFile.add(mntmExit);

		JMenu mnSystem = new JMenu("System");
		menuBar.add(mnSystem);

		JMenuItem mntmRun = new JMenuItem("Run");
		mnSystem.add(mntmRun);

		JMenuItem mntmSystem = new JMenuItem("Stop");
		mnSystem.add(mntmSystem);

		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);

		JMenuItem mntmCalibrateThreshold = new JMenuItem("Camera");
		mntmCalibrateThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalibrationDialog().setVisible(true);
			}
		});
		mntmCalibrateThreshold.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_T, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK));
		mnTools.add(mntmCalibrateThreshold);

		JMenuItem mntmInitialize = new JMenuItem("Initialize PliP");
		mntmInitialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PlipTrainer trainer = new PlipTrainer();
				trainer.initializeSystem();
			}
		});

		mnTools.add(mntmInitialize);

		JMenuItem mntmTrainer = new JMenuItem("Start Training");
		mntmTrainer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PlipTrainer trainer = new PlipTrainer();
				try {
					trainer.train();
				} catch (ImageNotFoundException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(getParent(), "Eggs are not supposed to be green.");
				}
			}
		});

		mnTools.add(mntmTrainer);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmHelpContents = new JMenuItem("Help Contents");
		mnHelp.add(mntmHelpContents);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		

		JPanel westPanel = new JPanel();
		westPanel.setBackground(Color.WHITE);
		westPanel.setBorder(new EmptyBorder(10, 15, 10, 10));
		westPanel.setMinimumSize(new Dimension(200, 10));
		westPanel.setPreferredSize(new Dimension(940, 640));
		westPanel.setSize(new Dimension(940, 640));
		contentPane.add(westPanel, BorderLayout.WEST);
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.X_AXIS));

		final VideoDisplayPanel videoDisplayPanel = new VideoDisplayPanel();
		
		final WaitLayerUI layerUI = new WaitLayerUI();
		
		final Timer stopper = new Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				layerUI.stop();
			}
		});
		stopper.setRepeats(false);

		JLayer<JPanel> layer = new JLayer<JPanel>();
		layer = new JLayer<JPanel>(videoDisplayPanel, layerUI);
		
		westPanel.add(layer);
		

		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		contentPane.add(centerPanel, BorderLayout.CENTER);
	
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

		JPanel startButtonPanel = new JPanel();
		startButtonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		startButtonPanel.setBackground(Color.WHITE);
		centerPanel.add(startButtonPanel);
		startButtonPanel.setLayout(new BoxLayout(startButtonPanel,
				BoxLayout.Y_AXIS));

		//final JLabel capturingLabel = new JLabel("Not Capturing");
		//capturingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		
		//capturingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		//startButtonPanel.add(capturingLabel);

		JToggleButton tglbtnStart = new JToggleButton();
		tglbtnStart.setBorderPainted(false);
		
		try {
		    Image img =  ImageIO.read(getClass().getResource("/play.png"));
		    tglbtnStart.setIcon(new ImageIcon(img));
		  } catch (IOException ex) {
		}
		
		tglbtnStart.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				final JToggleButton stopButton = (JToggleButton) e.getItem();
				if (stopButton.isSelected()) {
					//capturingLabel.setText("Capturing");
					
					try {
					    Image img =  ImageIO.read(getClass().getResource("/stop.png"));
					    stopButton.setIcon(new ImageIcon(img));
					  } catch (IOException ex) {
						  ex.printStackTrace();
					}
					
					layerUI.start();
					if (!stopper.isRunning()) {
						stopper.start();
					}
					new Thread(new Runnable() {
						public void run() {
							while (true) {
								if (!stopper.isRunning()) {
									Mat capturedImage = msm.captureVideoFrame();
									if (capturedImage != null) {
										videoDisplayPanel
												.matToBufferedImage(capturedImage);
										videoDisplayPanel.repaint();
									}
								}
								if (!stopButton.isSelected()) {
									break;
								}
							}
						}
					}).start();

				} else {
					//capturingLabel.setText("Not Capturing");
					stopper.stop();
					videoDisplayPanel.repaint();
					
					try {
					    Image img =  ImageIO.read(getClass().getResource("/play.png"));
					    stopButton.setIcon(new ImageIcon(img));
					  } catch (IOException ex) {
						  ex.printStackTrace();
					}
				}
			}
		});
		tglbtnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButtonPanel.add(tglbtnStart);

		JPanel logPanel = new JPanel();
		logPanel.setBackground(Color.WHITE);
		centerPanel.add(logPanel);
		logPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.X_AXIS));

		logTextPane = new JTextArea();
		logTextPane.setEditable(false);
		logScrollPane = new JScrollPane(logTextPane);
		logScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		logPanel.add(logScrollPane);

		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		contentPane.add(northPanel, BorderLayout.NORTH);

		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.WHITE);
		contentPane.add(southPanel, BorderLayout.SOUTH);
	}

	public MainMenuFrame(MainSystemMonitor msm) {
		this();
		this.msm = msm;
	}

	@Override
	public void handleEvent(EventObject event) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String shortTimeStr = sdf.format(new Date());
		String text = "\n" + shortTimeStr + " - "
				+ event.getClass().getSimpleName();
		if (event instanceof TrayArrivalEvent) {
			TrayArrivalEvent tae = (TrayArrivalEvent) event;
			Mat[] trayArray = tae.getTray_images();
			for (Mat tray : trayArray) {
				try {
					// Highgui.imwrite("Tray.jpg", tray);
				} catch (Exception e) {
					System.out.println("Imagen NULA");
				}
			}
		}
		logTextPane.append(text);
		logTextPane.setCaretPosition(logTextPane.getDocument().getLength());
	}
}
