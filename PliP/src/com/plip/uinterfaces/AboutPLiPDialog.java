package com.plip.uinterfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class AboutPLiPDialog extends JDialog {

	private static final long serialVersionUID = 2L;

	private final JPanel contentPanel = new JPanel();

	private AboutPLiPDialog cDialog = this;
	private JLabel webServerUrlLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AboutPLiPDialog dialog = new AboutPLiPDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutPLiPDialog() {

		setTitle("About");
		setModal(true);
		setMinimumSize(new Dimension(450, 420));
		setPreferredSize(new Dimension(450, 420));
		setBounds(50, 50, 450, 420);
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
						EtchedBorder.LOWERED, null, null), "",
						TitledBorder.CENTER, TitledBorder.TOP, null, null));

				panel.setMinimumSize(new Dimension(430, 120));
				panel.setPreferredSize(new Dimension(430, 120));
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				
				mainPanel.add(panel);
				{
					JPanel webServerUrlPanel = new JPanel();
					webServerUrlPanel.setBackground(Color.LIGHT_GRAY);
					webServerUrlPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
					webServerUrlPanel.setLayout(new FlowLayout(FlowLayout.CENTER,
							3, 3));
					panel.add(webServerUrlPanel);
					{
						String text = "PLIP, Production Line Image Processing is an innovative Computer Vision system that uses image processing for monitoring stock and ensure the loyalty of orders assembly in a logistics business."+ 
"The Overall system operation is as follows: after capturing an image of the order and having sample images of the products, PLiP image recognition routines are used to validate the order and ensure the reliability of the assembly.";
						webServerUrlLabel = new JLabel("<html>"+text+"</html>");
						webServerUrlLabel.setPreferredSize(new Dimension(430,
								120));
						webServerUrlLabel
								.setMinimumSize(new Dimension(430, 120));
						webServerUrlLabel.setAlignmentY(CENTER_ALIGNMENT);
						webServerUrlPanel.add(webServerUrlLabel);
					}
				}
				{
					JPanel logoPanel = new JPanel();
					logoPanel.setBackground(Color.LIGHT_GRAY);
					logoPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

					final JLabel plipIcon = new JLabel();	
				
					try {
					    Image img =  ImageIO.read(getClass().getResource("/plip42.png"));
					    plipIcon.setIcon(new ImageIcon(img));
					    plipIcon.setPreferredSize(new Dimension(200,220));
					    logoPanel.add(plipIcon);
					  } catch (IOException ex) {
					}	
					panel.add(logoPanel);	
				}
			}
			{
				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
				{
					JButton okButton = new JButton("OK");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {

							cDialog.dispose();
						}
					});
					okButton.setActionCommand("OK");
					buttonPane.add(okButton);
					getRootPane().setDefaultButton(okButton);
				}
			}
		}
	}
}
