package com.plip.uinterfaces;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;


public class VideoDisplayPanel extends JPanel{
	private static final long serialVersionUID = 1L;  
    private BufferedImage image;  
    // Create a constructor method  
    public VideoDisplayPanel(){  
         super();   
    }  
    /*  
     * Converts/writes a Mat into a BufferedImage.  
     *   
     * @param matrix Mat of type CV_8UC3 or CV_8UC1  
     * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
     */       
    public boolean matToBufferedImage(Mat matrix) {  
         MatOfByte mb=new MatOfByte();  
         Highgui.imencode(".jpg", matrix, mb);  
         try {  
              this.image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));  
         } catch (IOException e) {  
              e.printStackTrace();  
              return false; // Error  
         }  
      return true; // Successful  
    }  
    public void paintComponent(Graphics g){  
         super.paintComponent(g);   
         if (this.image==null) return;  
          g.drawImage(this.image,0,0,this.image.getWidth(),this.image.getHeight(), null);  
         //g.drawString("This is my custom Panel!",10,20);  
    }

}
