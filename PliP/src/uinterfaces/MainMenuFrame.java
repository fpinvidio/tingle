package uinterfaces;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import systemmonitor.MainSystemMonitor;
import eventhandling.events.TrayArrivalEvent;
import eventhandling.listeners.GenericEventListener;

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
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				InputEvent.ALT_MASK));
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenu mnRun = new JMenu("Run");
		menuBar.add(mnRun);

		JMenuItem mntmRun = new JMenuItem("Run");
		mnRun.add(mntmRun);

		JMenuItem mntmRunHistory = new JMenuItem("Run History");
		mnRun.add(mntmRunHistory);

		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);

		JMenuItem mntmCalibrateThreshold = new JMenuItem("Calibrate Threshold");
		mntmCalibrateThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalibrationDialog().setVisible(true);
			}
		});
		mntmCalibrateThreshold.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_T, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK));
		mnTools.add(mntmCalibrateThreshold);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmHelpContents = new JMenuItem("Help Contents");
		mnHelp.add(mntmHelpContents);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel westPanel = new JPanel();
		westPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		westPanel.setMinimumSize(new Dimension(200, 10));
		westPanel.setPreferredSize(new Dimension(200, 10));
		westPanel.setSize(new Dimension(200, 0));
		contentPane.add(westPanel, BorderLayout.WEST);
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.X_AXIS));

		final VideoDisplayPanel videoDisplayPanel = new VideoDisplayPanel();
		final WaitLayerUI layerUI = new WaitLayerUI();

		final Timer stopper = new Timer(8000, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				layerUI.stop();
			}
		});
		stopper.setRepeats(false);

		JLayer<JPanel> layer = new JLayer<JPanel>();
		layer = new JLayer<JPanel>(videoDisplayPanel, layerUI);
		westPanel.add(layer);

		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

		JPanel startButtonPanel = new JPanel();
		centerPanel.add(startButtonPanel);
		startButtonPanel.setLayout(new BoxLayout(startButtonPanel,
				BoxLayout.Y_AXIS));

		final JLabel capturingLabel = new JLabel("Not Capturing");
		capturingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButtonPanel.add(capturingLabel);

		JToggleButton tglbtnStart = new JToggleButton("Start");
		tglbtnStart.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				final JToggleButton startButton = (JToggleButton) e.getItem();
				if (startButton.isSelected()) {
					capturingLabel.setText("Capturing");
					startButton.setText("Stop");
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
								if (!startButton.isSelected()) {
									break;
								}
							}
						}
					}).start();

				} else {
					capturingLabel.setText("Not Capturing");
					startButton.setText("Start");
					stopper.stop();
					videoDisplayPanel.repaint();
				}
			}
		});
		tglbtnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButtonPanel.add(tglbtnStart);

		JPanel logPanel = new JPanel();
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
		contentPane.add(northPanel, BorderLayout.NORTH);

		JPanel southPanel = new JPanel();
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
				Highgui.imwrite("Tray.jpg", tray);
			}
		}
		logTextPane.append(text);
		logTextPane.setCaretPosition(logTextPane.getDocument().getLength());
	}

}
