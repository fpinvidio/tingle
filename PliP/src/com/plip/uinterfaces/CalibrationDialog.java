package com.plip.uinterfaces;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import com.plip.imageprocessing.processors.TrayProcessor;

public class CalibrationDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private VideoCapture vcapture;
	private TrayProcessor iprocessor;

	private CalibrationDialog cDialog = this;
	private JSlider minHueSlider;
	private JSlider minSatSlider;
	private JSlider minValueSlider;
	private JSlider maxHueSlider;
	private JSlider maxSatSlider;
	private JSlider maxValueSlider;
	private VideoDisplayPanel videoDisplayPanel;
	private JLabel minHueLabel, minSatLabel, minValueLabel, maxHueLabel,
			maxSatLabel, maxValueLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CalibrationDialog dialog = new CalibrationDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CalibrationDialog() {
		setMinimumSize(new Dimension(600, 380));
		setPreferredSize(new Dimension(600, 350));
		setTitle("Calibrate Tray");
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(new EtchedBorder(
					EtchedBorder.LOWERED, null, null), "HSV Values",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(0, 1, 0, 0));
			{
				JPanel minPanel = new JPanel();
				minPanel.setBorder(new TitledBorder(new EtchedBorder(
						EtchedBorder.LOWERED, null, null), "Min. Threshold",
						TitledBorder.CENTER, TitledBorder.TOP, null, null));
				panel.add(minPanel);
				minPanel.setLayout(new BoxLayout(minPanel, BoxLayout.Y_AXIS));
				{
					JPanel minHuePanel = new JPanel();
					minHuePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
					minPanel.add(minHuePanel);
					{
						minHueLabel = new JLabel("Hue:");
						minHueLabel.setPreferredSize(new Dimension(100, 16));
						minHueLabel.setMinimumSize(new Dimension(100, 16));
						minHuePanel.add(minHueLabel);
					}
					{
						minHueSlider = new JSlider();
						minHueSlider.addChangeListener(new ChangeListener() {
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
					minSatPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
					minPanel.add(minSatPanel);
					{
						minSatLabel = new JLabel("Saturation:");
						minSatLabel.setMinimumSize(new Dimension(100, 16));
						minSatLabel.setPreferredSize(new Dimension(100, 16));
						minSatPanel.add(minSatLabel);
					}
					{
						minSatSlider = new JSlider();
						minSatSlider.addChangeListener(new ChangeListener() {
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
					minValPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
					minPanel.add(minValPanel);
					{
						minValueLabel = new JLabel("Value:");
						minValueLabel.setMinimumSize(new Dimension(100, 16));
						minValueLabel.setPreferredSize(new Dimension(100, 16));
						minValPanel.add(minValueLabel);
					}
					{
						minValueSlider = new JSlider();
						minValueSlider.addChangeListener(new ChangeListener() {
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
						EtchedBorder.LOWERED, null, null), "Max. Threshold",
						TitledBorder.CENTER, TitledBorder.TOP, null, null));
				panel.add(maxPanel);
				maxPanel.setLayout(new BoxLayout(maxPanel, BoxLayout.Y_AXIS));
				{
					JPanel panel_1 = new JPanel();
					panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
					maxPanel.add(panel_1);
					{
						maxHueLabel = new JLabel("Hue:");
						maxHueLabel.setPreferredSize(new Dimension(100, 16));
						maxHueLabel.setMinimumSize(new Dimension(100, 16));
						panel_1.add(maxHueLabel);
					}
					{
						maxHueSlider = new JSlider();
						maxHueSlider.addChangeListener(new ChangeListener() {
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
					panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
					maxPanel.add(panel_1);
					{
						maxSatLabel = new JLabel("Saturation:");
						maxSatLabel.setMinimumSize(new Dimension(100, 16));
						maxSatLabel.setPreferredSize(new Dimension(100, 16));
						panel_1.add(maxSatLabel);
					}
					{
						maxSatSlider = new JSlider();
						maxSatSlider.addChangeListener(new ChangeListener() {
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
					maxPanel.add(panel_1);
					{
						maxValueLabel = new JLabel("Value:");
						maxValueLabel.setPreferredSize(new Dimension(100, 16));
						maxValueLabel.setMinimumSize(new Dimension(100, 16));
						panel_1.add(maxValueLabel);
					}
					{
						maxValueSlider = new JSlider();
						maxValueSlider.addChangeListener(new ChangeListener() {
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
							Properties props = new Properties();
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
							File f = new File("./res/config.properties");
							OutputStream out = new FileOutputStream(f);
							props.store(out,
									"This file has been modified.");
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
	}

	public void initializeVideoCapture() {
		System.loadLibrary("opencv_java246");
		vcapture = new VideoCapture(1);
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
