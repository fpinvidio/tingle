package com.plip.uinterfaces;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import com.plip.imageprocessing.matchers.MinDistanceMatcher;
import com.plip.imageprocessing.processors.ObjectCounter;
import com.plip.imageprocessing.processors.TrayProcessor;
import com.plip.persistence.managers.images.HashImageManager;
import com.plip.system.config.trainers.PlipTrainer;
import com.plip.system.monitors.MainSystemMonitor;

public class CameraDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private VideoCapture vcapture;
	private TrayProcessor iprocessor;

	private CameraDialog cDialog = this;
	private JSlider minHueSlider;
	private JSlider minSatSlider;
	private JSlider minValueSlider;
	private JSlider maxHueSlider;
	private JSlider maxSatSlider;
	private JSlider maxValueSlider;
	private JTextField imageResolutionTextFieldW, imageResolutionTextFieldH,
			videoResolutionTextFieldW, videoResolutionTextFieldH,
			cameraPortTextField;
	private VideoDisplayPanel videoDisplayPanel;
	private JLabel minHueLabel, minSatLabel, minValueLabel, maxHueLabel,
			maxSatLabel, maxValueLabel, imageResolutionLabel,
			videoResolutionLabel, cameraPortLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CameraDialog dialog = new CameraDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.intitializeDialog();
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CameraDialog() {

		setTitle("Camera");
		setModal(true);
		setMinimumSize(new Dimension(650, 530));
		setPreferredSize(new Dimension(650, 530));
		setBounds(100, 100, 650, 530);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JPanel mainPanel = new JPanel();
			mainPanel.setAlignmentY(LEFT_ALIGNMENT);
			contentPanel.add(mainPanel);
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			{

				JPanel panel = new JPanel();
				panel.setBorder(new TitledBorder(new EtchedBorder(
						EtchedBorder.LOWERED, null, null), "Calibrate",
						TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.setMinimumSize(new Dimension(300, 340));

				panel.setPreferredSize(new Dimension(300, 340));

				mainPanel.add(panel);
				panel.setLayout(new GridLayout(0, 1, 0, 0));
				{
					JPanel minPanel = new JPanel();
					minPanel.setBorder(new TitledBorder(new EtchedBorder(
							EtchedBorder.LOWERED, null, null),
							"Min. Threshold", TitledBorder.CENTER,
							TitledBorder.TOP, null, null));
					panel.add(minPanel);
					minPanel.setLayout(new BoxLayout(minPanel, BoxLayout.Y_AXIS));
					{
						JPanel minHuePanel = new JPanel();
						minHuePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
						minHuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
						minPanel.add(minHuePanel);
						{
							minHueLabel = new JLabel("Hue:");
							minHueLabel
									.setPreferredSize(new Dimension(100, 16));
							minHueLabel.setMinimumSize(new Dimension(100, 16));
							minHuePanel.add(minHueLabel);
						}
						{
							minHueSlider = new JSlider();
							minHueSlider
									.addChangeListener(new ChangeListener() {
										public void stateChanged(ChangeEvent e) {
											minHueLabel.setText("Hue: "
													+ minHueSlider.getValue());
										}
									});
							minHueSlider.setMaximum(500);
							minHuePanel.add(minHueSlider);
						}
					}
					{
						JPanel minSatPanel = new JPanel();
						minSatPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
						minSatPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
						minPanel.add(minSatPanel);
						{
							minSatLabel = new JLabel("Saturation:");
							minSatLabel.setMinimumSize(new Dimension(100, 16));
							minSatLabel
									.setPreferredSize(new Dimension(100, 16));
							minSatPanel.add(minSatLabel);
						}
						{
							minSatSlider = new JSlider();
							minSatSlider
									.addChangeListener(new ChangeListener() {
										public void stateChanged(ChangeEvent e) {
											minSatLabel.setText("Saturation: "
													+ minSatSlider.getValue());
										}
									});
							minSatSlider.setMaximum(500);
							minSatPanel.add(minSatSlider);
						}
					}
					{
						JPanel minValPanel = new JPanel();
						minValPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
						minValPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
						minPanel.add(minValPanel);
						{
							minValueLabel = new JLabel("Value:");
							minValueLabel
									.setMinimumSize(new Dimension(100, 16));
							minValueLabel.setPreferredSize(new Dimension(100,
									16));
							minValPanel.add(minValueLabel);
						}
						{
							minValueSlider = new JSlider();
							minValueSlider
									.addChangeListener(new ChangeListener() {
										public void stateChanged(ChangeEvent e) {
											minValueLabel.setText("Value: "
													+ minValueSlider.getValue());
										}
									});
							minValueSlider.setMaximum(500);
							minValPanel.add(minValueSlider);
						}
					}
				}
				{
					JPanel maxPanel = new JPanel();
					maxPanel.setBorder(new TitledBorder(new EtchedBorder(
							EtchedBorder.LOWERED, null, null),
							"Max. Threshold", TitledBorder.CENTER,
							TitledBorder.TOP, null, null));
					panel.add(maxPanel);
					maxPanel.setLayout(new BoxLayout(maxPanel, BoxLayout.Y_AXIS));
					{
						JPanel panel_1 = new JPanel();
						panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
						panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
						maxPanel.add(panel_1);
						{
							maxHueLabel = new JLabel("Hue:");
							maxHueLabel
									.setPreferredSize(new Dimension(100, 16));
							maxHueLabel.setMinimumSize(new Dimension(100, 16));
							panel_1.add(maxHueLabel);
						}
						{
							maxHueSlider = new JSlider();
							maxHueSlider
									.addChangeListener(new ChangeListener() {
										public void stateChanged(ChangeEvent e) {
											maxHueLabel.setText("Hue: "
													+ maxHueSlider.getValue());
										}
									});
							maxHueSlider.setMaximum(500);
							panel_1.add(maxHueSlider);
						}
					}
					{
						JPanel panel_1 = new JPanel();
						panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
						panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
						maxPanel.add(panel_1);
						{
							maxSatLabel = new JLabel("Saturation:");
							maxSatLabel.setMinimumSize(new Dimension(100, 16));
							maxSatLabel
									.setPreferredSize(new Dimension(100, 16));
							panel_1.add(maxSatLabel);
						}
						{
							maxSatSlider = new JSlider();
							maxSatSlider
									.addChangeListener(new ChangeListener() {
										public void stateChanged(ChangeEvent e) {
											maxSatLabel.setText("Saturation: "
													+ maxSatSlider.getValue());
										}
									});
							maxSatSlider.setMaximum(500);
							panel_1.add(maxSatSlider);
						}
					}
					{
						JPanel panel_1 = new JPanel();
						panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
						panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
						maxPanel.add(panel_1);
						{
							maxValueLabel = new JLabel("Value:");
							maxValueLabel.setPreferredSize(new Dimension(100,
									16));
							maxValueLabel
									.setMinimumSize(new Dimension(100, 16));
							panel_1.add(maxValueLabel);
						}
						{
							maxValueSlider = new JSlider();
							maxValueSlider
									.addChangeListener(new ChangeListener() {
										public void stateChanged(ChangeEvent e) {
											maxValueLabel.setText("Value: "
													+ maxValueSlider.getValue());
										}
									});
							maxValueSlider.setMaximum(500);
							panel_1.add(maxValueSlider);
						}
					}
				}
			}
			{
				JPanel settingsPanel = new JPanel();
				mainPanel.setAlignmentY(LEFT_ALIGNMENT);
				settingsPanel.setBorder(new TitledBorder(new EtchedBorder(
						EtchedBorder.LOWERED, null, null), "Settings",
						TitledBorder.LEADING, TitledBorder.TOP, null, null));
				mainPanel.add(settingsPanel);
				settingsPanel.setLayout(new GridLayout(0, 1, 0, 0));
				{
					{
						JPanel minPanel = new JPanel();
						minPanel.setBorder(new TitledBorder(new EtchedBorder(
								EtchedBorder.LOWERED, null, null), "",
								TitledBorder.CENTER, TitledBorder.TOP, null,
								null));
						settingsPanel.add(minPanel);
						minPanel.setLayout(new BoxLayout(minPanel,
								BoxLayout.Y_AXIS));
						{
							JPanel imageResolutionPanel = new JPanel();
							imageResolutionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
							imageResolutionPanel.setBorder(new EmptyBorder(0,
									0, 0, 0));
							minPanel.add(imageResolutionPanel);
							{
								imageResolutionLabel = new JLabel(
										"Image resolution:");
								imageResolutionLabel
										.setPreferredSize(new Dimension(130, 16));
								imageResolutionLabel
										.setMinimumSize(new Dimension(130, 16));
								imageResolutionPanel.add(imageResolutionLabel);
							}
							{
								imageResolutionTextFieldW = new JTextField(3);
								imageResolutionTextFieldW
										.setToolTipText("Width");
								imageResolutionTextFieldH = new JTextField(3);
								imageResolutionTextFieldH
										.setToolTipText("Height");
								imageResolutionTextFieldH
										.setPreferredSize(new Dimension(80, 18));
								imageResolutionTextFieldH
										.setMinimumSize(new Dimension(80, 18));
								imageResolutionTextFieldW
										.setPreferredSize(new Dimension(80, 18));
								imageResolutionTextFieldW
										.setMinimumSize(new Dimension(80, 18));
								imageResolutionPanel
										.add(imageResolutionTextFieldH);
								imageResolutionPanel
										.add(imageResolutionTextFieldW);
							}
						}
						{
							JPanel videoResolutionPanel = new JPanel();
							videoResolutionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
							videoResolutionPanel.setBorder(new EmptyBorder(0,
									0, 0, 0));
							minPanel.add(videoResolutionPanel);
							{
								videoResolutionLabel = new JLabel(
										"Video resolution:");
								videoResolutionLabel
										.setMinimumSize(new Dimension(130, 16));
								videoResolutionLabel
										.setPreferredSize(new Dimension(130, 16));
								videoResolutionPanel.add(videoResolutionLabel);
							}
							{
								videoResolutionTextFieldW = new JTextField(3);
								videoResolutionTextFieldW
										.setToolTipText("Width");
								videoResolutionTextFieldH = new JTextField(3);
								videoResolutionTextFieldH
										.setToolTipText("Height");
								videoResolutionTextFieldH
										.setPreferredSize(new Dimension(80, 18));
								videoResolutionTextFieldH
										.setMinimumSize(new Dimension(80, 18));
								videoResolutionTextFieldW
										.setPreferredSize(new Dimension(80, 18));
								videoResolutionTextFieldW
										.setMinimumSize(new Dimension(80, 18));
								videoResolutionPanel
										.add(videoResolutionTextFieldH);
								videoResolutionPanel
										.add(videoResolutionTextFieldW);
							}
						}
						{
							JPanel cameraPortPanel = new JPanel();
							cameraPortPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
							cameraPortPanel.setBorder(new EmptyBorder(0, 0, 0,
									0));
							minPanel.add(cameraPortPanel);
							{
								cameraPortLabel = new JLabel("Camera Port:");
								cameraPortLabel.setMinimumSize(new Dimension(
										130, 16));
								cameraPortLabel.setPreferredSize(new Dimension(
										130, 16));
								cameraPortPanel.add(cameraPortLabel);
							}
							{
								cameraPortTextField = new JTextField(1);
								cameraPortTextField
										.setPreferredSize(new Dimension(80, 18));
								cameraPortTextField
										.setMinimumSize(new Dimension(80, 18));
								cameraPortPanel.add(cameraPortTextField);
							}
						}
					}
				}
			}
		}
		{
			JPanel videoPanel = new JPanel();
			videoPanel.setBorder(new TitledBorder(null, "Video",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(videoPanel);
			videoPanel.setLayout(new BoxLayout(videoPanel, BoxLayout.X_AXIS));
			{
				videoDisplayPanel = new VideoDisplayPanel();
				videoDisplayPanel.setMinimumSize(new Dimension(240, 10));
				videoDisplayPanel.setPreferredSize(new Dimension(240, 10));
				videoPanel.add(videoDisplayPanel);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							File f = null;
							Properties props = new Properties();
							InputStream is = null;
							try {
								f = new File("./res/config.properties");
								is = new FileInputStream(f);
							} catch (Exception e1) {
								is = null;
							}

							try {
								if (is == null) {
									is = getClass().getResourceAsStream(
											"./res/config.properties");
								}

								props.load(is);
							} catch (Exception e2) {
							}

							props.setProperty("minHueThreshold",
									minHueSlider.getValue() + "");
							props.setProperty("minSaturationThreshold",
									minSatSlider.getValue() + "");
							props.setProperty("minValueThreshold",
									minValueSlider.getValue() + "");
							props.setProperty("maxHueThreshold",
									maxHueSlider.getValue() + "");
							props.setProperty("maxSaturationThreshold",
									maxSatSlider.getValue() + "");
							props.setProperty("maxValueThreshold",
									maxValueSlider.getValue() + "");
							props.setProperty("imageResolutionWidth",
									imageResolutionTextFieldW.getText() + "");
							props.setProperty("imageResolutionHeight",
									imageResolutionTextFieldH.getText() + "");
							props.setProperty("captureResolutionHeight",
									videoResolutionTextFieldH.getText() + "");
							props.setProperty("captureResolutionWidth",
									videoResolutionTextFieldW.getText() + "");
							props.setProperty("cameraInput",
									cameraPortTextField.getText() + "");

							OutputStream out = new FileOutputStream(f);
							props.store(out, "This file has been modified.");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						cDialog.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cDialog.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		initializeVideoCapture();
		intitializeDialog();
	}

	public void intitializeDialog() {
		Properties props = new Properties();
		InputStream is = null;
		try {
			File f = new File("./res/config.properties");
			is = new FileInputStream(f);
		} catch (Exception e) {
			is = null;
		}

		try {
			if (is == null) {
				is = getClass().getResourceAsStream("./res/config.properties");
			}

			props.load(is);
		} catch (Exception e) {
		}

		minHueSlider.setValue(Integer.valueOf(props
				.getProperty("minHueThreshold")));
		minSatSlider.setValue(Integer.valueOf(props
				.getProperty("minSatThreshold")));
		minValueSlider.setValue(Integer.valueOf(props
				.getProperty("minValueThreshold")));
		maxHueSlider.setValue(Integer.valueOf(props
				.getProperty("maxHueThreshold")));
		maxSatSlider.setValue(Integer.valueOf(props
				.getProperty("maxSatThreshold")));
		maxValueSlider.setValue(Integer.valueOf(props
				.getProperty("maxValueThreshold")));

		imageResolutionTextFieldW.setText(props
				.getProperty("imageResolutionWidth"));
		imageResolutionTextFieldH.setText(props
				.getProperty("imageResolutionHeight"));
		videoResolutionTextFieldW.setText(props
				.getProperty("captureResolutionWidth"));
		videoResolutionTextFieldH.setText(props
				.getProperty("captureResolutionHeight"));

		cameraPortTextField.setText(props.getProperty("cameraInput"));
	}

	public void initializeVideoCapture() {
		System.loadLibrary("opencv_java246");
		vcapture = new VideoCapture(0);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 240);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 240);
		iprocessor = new TrayProcessor();
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(8000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (vcapture.isOpened()) {
					while (true) {
						Mat capturedFrame = new Mat();
						vcapture.read(capturedFrame);
						if (!capturedFrame.empty()) {
							TrayProcessor.thr1 = minHueSlider.getValue();
							TrayProcessor.thr2 = minSatSlider.getValue();
							TrayProcessor.thr3 = minValueSlider.getValue();
							TrayProcessor.thr4 = maxHueSlider.getValue();
							TrayProcessor.thr5 = maxSatSlider.getValue();
							TrayProcessor.thr6 = maxValueSlider.getValue();
							capturedFrame = iprocessor
									.findRectangleInImage(capturedFrame);
							videoDisplayPanel.matToBufferedImage(capturedFrame);
							videoDisplayPanel.repaint();
						}
					}
				}
			}
		}).start();
	}
}
