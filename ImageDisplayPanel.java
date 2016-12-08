import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;


class ImageDisplayPanel extends JPanel
    {
        private BufferedImage image;
        private Graphics2D g2d;
       
        //------------------------------------------------------------------------
        // constructor
        public ImageDisplayPanel( BufferedImage image )
        {
            this.image = image;
            g2d = image.createGraphics();
            Dimension size = new Dimension( image.getWidth(), image.getHeight() );
            setMinimumSize( size );
            setPreferredSize( size );
        }

        private void setupImage() {
            g2d = (Graphics2D) image.createGraphics();
            g2d.setColor( Color.WHITE );
            g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        public void paintComponent( Graphics g )
        {
            super.paintComponent( g );
            g.drawImage( image, 0, 0, null );
        }
        
    }

