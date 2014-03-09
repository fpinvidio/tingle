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

import org.opencv.highgui.VideoCapture;

import com.plip.imageprocessing.processors.TrayProcessor;

public class PreferencesDialog extends JDialog {

	private static final long serialVersionUID = 2L;

	private final JPanel contentPanel = new JPanel();

	private PreferencesDialog cDialog = this;
	private JLabel webServerUrlLabel, clientServerUrlLabel, imagesPathLabel,
			webServerImagesPathLabel, minAreaLabel, maxDistanceLabel,
			trayAreaLabel, trayCoordinatesLabel;
	private JTextField webServerUrlTextField, clientServerUrlTextField,
			imagesPathTextField, webServerImagesPathTextField,
			minAreaTextField, maxAreaTextField, trayAreaTextFieldW,
			trayAreaTextFieldH, maxDistanceTextField,
			trayCoordinatesTextFieldX, trayCoordinatesTextFieldY;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PreferencesDialog dialog = new PreferencesDialog();
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
	public PreferencesDialog() {

		setTitle("Preferences");
		setModal(true);
		setMinimumSize(new Dimension(450, 450));
		setPreferredSize(new Dimension(450, 450));
		setBounds(50, 50, 450, 450);
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
						EtchedBorder.LOWERED, null, null), "Communication",
						TitledBorder.CENTER, TitledBorder.TOP, null, null));

				panel.setMinimumSize(new Dimension(200, 50));
				panel.setPreferredSize(new Dimension(200, 50));

				mainPanel.add(panel);
				panel.setLayout(new GridLayout(0, 1, 0, 0));
				{
					JPanel webServerUrlPanel = new JPanel();
					webServerUrlPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
					webServerUrlPanel.setLayout(new FlowLayout(FlowLayout.LEFT,
							3, 3));
					panel.add(webServerUrlPanel);
					{
						webServerUrlLabel = new JLabel("WebServer");
						webServerUrlLabel.setPreferredSize(new Dimension(100,
								16));
						webServerUrlLabel
								.setMinimumSize(new Dimension(100, 16));
						webServerUrlPanel.add(webServerUrlLabel);
					}
					{
						webServerUrlTextField = new JTextField(25);
						webServerUrlTextField.setPreferredSize(new Dimension(
								80, 18));
						webServerUrlTextField.setMinimumSize(new Dimension(80,
								18));
						webServerUrlPanel.add(webServerUrlTextField);
					}
				}
				{
					JPanel clientServerUrlPanel = new JPanel();
					clientServerUrlPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
					clientServerUrlPanel.setLayout(new FlowLayout(
							FlowLayout.LEFT, 3, 3));
					panel.add(clientServerUrlPanel);
					{
						clientServerUrlLabel = new JLabel("ClientServer:");
						clientServerUrlLabel.setMinimumSize(new Dimension(100,
								16));
						clientServerUrlLabel.setPreferredSize(new Dimension(
								100, 16));
						clientServerUrlPanel.add(clientServerUrlLabel);
					}
					{
						clientServerUrlTextField = new JTextField(25);
						clientServerUrlTextField
								.setPreferredSize(new Dimension(80, 18));
						clientServerUrlTextField.setMinimumSize(new Dimension(
								80, 18));
						clientServerUrlPanel.add(clientServerUrlTextField);
					}
				}

			}

			{
				JPanel imagesPanel = new JPanel();
				imagesPanel.setBorder(new TitledBorder(new EtchedBorder(
						EtchedBorder.LOWERED, null, null), "Images",
						TitledBorder.CENTER, TitledBorder.TOP, null, null));
				imagesPanel.setMinimumSize(new Dimension(200, 15));
				imagesPanel.setPreferredSize(new Dimension(200, 15));
				mainPanel.add(imagesPanel);
				imagesPanel.setLayout(new BoxLayout(imagesPanel,
						BoxLayout.Y_AXIS));
				{
					JPanel panel_1 = new JPanel();
					panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
					panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
					imagesPanel.add(panel_1);
					{
						imagesPathLabel = new JLabel("Images path:");
						imagesPathLabel
								.setPreferredSize(new Dimension(100, 16));
						imagesPathLabel.setMinimumSize(new Dimension(100, 16));
						panel_1.add(imagesPathLabel);
					}
					{
						imagesPathTextField = new JTextField(25);
						imagesPathTextField.setPreferredSize(new Dimension(80,
								18));
						imagesPathTextField
								.setMinimumSize(new Dimension(80, 18));
						panel_1.add(imagesPathTextField);
					}
				}
				{
					JPanel panel_1 = new JPanel();
					panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
					panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
					imagesPanel.add(panel_1);
					{
						webServerImagesPathLabel = new JLabel("WSImages path:");
						webServerImagesPathLabel
								.setPreferredSize(new Dimension(100, 16));
						webServerImagesPathLabel.setMinimumSize(new Dimension(
								100, 16));
						panel_1.add(webServerImagesPathLabel);
					}
					{
						webServerImagesPathTextField = new JTextField(25);
						webServerImagesPathTextField
								.setPreferredSize(new Dimension(80, 18));
						webServerImagesPathTextField
								.setMinimumSize(new Dimension(80, 18));
						panel_1.add(webServerImagesPathTextField);
					}
				}

			}

			{
				JPanel areaThresholdsPanel = new JPanel();
				areaThresholdsPanel.setBorder(new TitledBorder(
						new EtchedBorder(EtchedBorder.LOWERED, null, null),
						"Area thresholds", TitledBorder.CENTER,
						TitledBorder.TOP, null, null));
				areaThresholdsPanel.setMinimumSize(new Dimension(200, 15));
				areaThresholdsPanel.setPreferredSize(new Dimension(200, 15));
				mainPanel.add(areaThresholdsPanel);
				areaThresholdsPanel.setLayout(new BoxLayout(
						areaThresholdsPanel, BoxLayout.Y_AXIS));
				{
					JPanel panel_1 = new JPanel();
					panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
					panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
					areaThresholdsPanel.add(panel_1);
					{
						minAreaLabel = new JLabel("Min area:");
						minAreaLabel.setPreferredSize(new Dimension(100, 16));
						minAreaLabel.setMinimumSize(new Dimension(100, 16));
						panel_1.add(minAreaLabel);
					}
					{
						minAreaTextField = new JTextField(8);
						minAreaTextField
								.setPreferredSize(new Dimension(80, 18));
						minAreaTextField.setMinimumSize(new Dimension(80, 18));
						panel_1.add(minAreaTextField);
					}
				}
				{
					JPanel panel_1 = new JPanel();
					panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
					panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
					areaThresholdsPanel.add(panel_1);
					{
						webServerImagesPathLabel = new JLabel("Max area:");
						webServerImagesPathLabel
								.setPreferredSize(new Dimension(100, 16));
						webServerImagesPathLabel.setMinimumSize(new Dimension(
								100, 16));
						panel_1.add(webServerImagesPathLabel);
					}
					{
						maxAreaTextField = new JTextField(8);
						maxAreaTextField
								.setPreferredSize(new Dimension(80, 18));
						maxAreaTextField.setMinimumSize(new Dimension(80, 18));
						panel_1.add(maxAreaTextField);
					}
				}
				{
					JPanel panel_1 = new JPanel();
					panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
					panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
					areaThresholdsPanel.add(panel_1);
					{
						trayAreaLabel = new JLabel("Tray area:");
						trayAreaLabel.setPreferredSize(new Dimension(110, 16));
						trayAreaLabel.setMinimumSize(new Dimension(110, 16));
						panel_1.add(trayAreaLabel);
					}
					{
						trayAreaTextFieldW = new JTextField(5);
						trayAreaTextFieldW.setPreferredSize(new Dimension(80,
								18));
						trayAreaTextFieldW.setToolTipText("Width");
						trayAreaTextFieldW
								.setMinimumSize(new Dimension(80, 18));
						panel_1.add(trayAreaTextFieldW);
					}
					{
						trayAreaTextFieldH = new JTextField(5);
						trayAreaTextFieldH.setPreferredSize(new Dimension(80,
								18));
						trayAreaTextFieldH
								.setMinimumSize(new Dimension(80, 18));
						trayAreaTextFieldH.setToolTipText("Height");
						panel_1.add(trayAreaTextFieldH);
					}
				}
				{
					JPanel panel_1 = new JPanel();
					panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
					panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
					areaThresholdsPanel.add(panel_1);
					{
						trayCoordinatesLabel = new JLabel("Tray coordinates:");
						trayCoordinatesLabel.setPreferredSize(new Dimension(
								110, 16));
						trayCoordinatesLabel.setMinimumSize(new Dimension(110,
								16));
						panel_1.add(trayCoordinatesLabel);
					}
					{
						trayCoordinatesTextFieldX = new JTextField(5);
						trayCoordinatesTextFieldX
								.setPreferredSize(new Dimension(80, 18));
						trayCoordinatesTextFieldX.setToolTipText("X");
						trayCoordinatesTextFieldX.setMinimumSize(new Dimension(
								80, 18));
						panel_1.add(trayCoordinatesTextFieldX);
					}
					{
						trayCoordinatesTextFieldY = new JTextField(5);
						trayCoordinatesTextFieldY
								.setPreferredSize(new Dimension(80, 18));
						trayCoordinatesTextFieldY.setToolTipText("Y");
						trayCoordinatesTextFieldY.setMinimumSize(new Dimension(
								80, 18));
						panel_1.add(trayCoordinatesTextFieldY);
					}
				}

			}
			{
				JPanel maxMathingDistancePanel = new JPanel();
				maxMathingDistancePanel.setBorder(new TitledBorder(
						new EtchedBorder(EtchedBorder.LOWERED, null, null),
						"Recognition", TitledBorder.CENTER, TitledBorder.TOP,
						null, null));
				maxMathingDistancePanel.setMinimumSize(new Dimension(250, 15));
				maxMathingDistancePanel
						.setPreferredSize(new Dimension(250, 15));
				mainPanel.add(maxMathingDistancePanel);
				maxMathingDistancePanel.setLayout(new BoxLayout(
						maxMathingDistancePanel, BoxLayout.Y_AXIS));
				{
					JPanel panel_1 = new JPanel();
					panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
					panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
					maxMathingDistancePanel.add(panel_1);
					{
						maxDistanceLabel = new JLabel("Max matching distance:");
						maxDistanceLabel
								.setPreferredSize(new Dimension(150, 16));
						maxDistanceLabel.setMinimumSize(new Dimension(150, 16));
						panel_1.add(maxDistanceLabel);
					}
					{
						maxDistanceTextField = new JTextField(3);
						maxDistanceTextField.setPreferredSize(new Dimension(80,
								18));
						maxDistanceTextField.setMinimumSize(new Dimension(80,
								18));
						panel_1.add(maxDistanceTextField);
					}
				}
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

							props.setProperty("plipAdministratorPanelUrl",
									webServerUrlTextField.getText());
							props.setProperty("DusaServer",
									clientServerUrlTextField.getText());
							props.setProperty("imagesPath",
									imagesPathTextField.getText());
							props.setProperty("imagesUploadPath",
									webServerImagesPathTextField.getText());
							props.setProperty("minAreaThreshold",
									minAreaTextField.getText());
							props.setProperty("maxAreaThreshold",
									maxAreaTextField.getText());
							props.setProperty("trayFloorWidth",
									trayAreaTextFieldW.getText());
							props.setProperty("trayFloorHeight",
									trayAreaTextFieldH.getText());
							props.setProperty("trayFloorX",
									trayCoordinatesTextFieldX.getText());
							props.setProperty("trayFloorY",
									trayCoordinatesTextFieldY.getText());
							props.setProperty("minMatchingDistance",
									maxDistanceTextField.getText());

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

		webServerUrlTextField.setText(props
				.getProperty("plipAdministratorPanelUrl"));
		clientServerUrlTextField.setText(props.getProperty("DusaServer"));
		imagesPathTextField.setText(props
				.getProperty("imagesPath"));
		webServerImagesPathTextField.setText(props.getProperty("imagesUploadPath"));
		minAreaTextField.setText(props.getProperty("minAreaThreshold"));
		maxAreaTextField.setText(props.getProperty("maxAreaThreshold"));
		trayAreaTextFieldW.setText(props.getProperty("trayFloorWidth"));
		trayAreaTextFieldH.setText(props.getProperty("trayFloorHeight"));
		trayCoordinatesTextFieldX.setText(props.getProperty("trayFloorX"));
		trayCoordinatesTextFieldY.setText(props.getProperty("trayFloorY"));
		maxDistanceTextField.setText(props.getProperty("minMatchingDistance"));
	}
}
