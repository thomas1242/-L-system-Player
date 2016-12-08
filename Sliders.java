import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

   public class Sliders extends JPanel
    {
        private BufferedImage image;
        private Graphics2D g2d;
        JSlider slider_1 = new JSlider(JSlider.HORIZONTAL, 0, 8, 4);
        JSlider slider_2 = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
        JSlider slider_3 = new JSlider(JSlider.HORIZONTAL, 0, 360, 90);
        JSlider slider_4 = new JSlider(JSlider.HORIZONTAL, 1, 60, 30);

        boolean isUpdate = true;
  
        // Panel whose color we change to display the mixed color.
        // private JPanel colorPanel = new JPanel();
        //------------------------------------------------------------------------
        // constructor
        public Sliders( Container contentPane )
        {
            slider_1.addChangeListener(new ChangeListener() {
               public void stateChanged(ChangeEvent event) {
                   update();
                }
            });
            slider_2.addChangeListener(new ChangeListener() {
               public void stateChanged(ChangeEvent event) {
                   update();
                }
           });
            slider_3.addChangeListener(new ChangeListener() {
               public void stateChanged(ChangeEvent event) {
                   update();
                }
            });
            slider_4.addChangeListener(new ChangeListener() {
               public void stateChanged(ChangeEvent event) {
                   update();
                }
            });

            JLabel slider_1_label = new JLabel("   generations ", JLabel.CENTER);
            JLabel slider_2_label = new JLabel("   delta (°)       ", JLabel.CENTER);
            JLabel slider_3_label = new JLabel("   orientation  ", JLabel.CENTER);
            JLabel slider_4_label = new JLabel("   scale           ", JLabel.CENTER);


             Font font = new Font("Serif", Font.ITALIC, 10);

             slider_1.setMajorTickSpacing(2);
             slider_1.setMinorTickSpacing(1);
             slider_1.setPaintTicks(true);
             slider_1.setPaintLabels(true);
             slider_1.setBorder( BorderFactory.createEmptyBorder(0,0,0,0));
             slider_1.setFont(font);

             slider_2.setMajorTickSpacing(90);
             slider_2.setMinorTickSpacing(45);
             slider_2.setPaintTicks(true);
             slider_2.setPaintLabels(true);
             slider_2.setBorder( BorderFactory.createEmptyBorder(0,0,0,0));
             slider_2.setFont(font);

             slider_3.setMajorTickSpacing(90);
             slider_3.setMinorTickSpacing(45);
             slider_3.setPaintTicks(true);
             slider_3.setPaintLabels(true);
             slider_3.setBorder( BorderFactory.createEmptyBorder(0,0,0,0));
             slider_3.setFont(font);

             slider_4.setMajorTickSpacing(1);
             slider_4.setMinorTickSpacing(1);
             slider_4.setBorder( BorderFactory.createEmptyBorder(0,0,0,0));

        JPanel allSliders = new JPanel();
        allSliders.setLayout (new GridLayout( 4, 1));

        JPanel s1 = new JPanel();
        s1.setLayout(new BorderLayout());
        s1.add( slider_1_label, BorderLayout.WEST );
        s1.add( slider_1,       BorderLayout.CENTER );
        JPanel s2 = new JPanel();
        s2.setLayout(new BorderLayout());
        s2.add( slider_2_label, BorderLayout.WEST );
        s2.add( slider_2,       BorderLayout.CENTER );
        JPanel s3 = new JPanel();
        s3.setLayout(new BorderLayout());
        s3.add( slider_3_label, BorderLayout.WEST );
        s3.add( slider_3,       BorderLayout.CENTER );
        JPanel s4 = new JPanel();
        s4.setLayout(new BorderLayout());
        s4.add( slider_4_label, BorderLayout.WEST );
        s4.add( slider_4,       BorderLayout.CENTER );

        allSliders.add ( s1 );
        allSliders.add ( s2 );
        allSliders.add ( s3 );
        allSliders.add ( s4 );


        contentPane.add(allSliders, BorderLayout.SOUTH);
    }

    public int getValueS1() {
        return slider_1.getValue();
    }
    public int getValueS2() {
        return slider_2.getValue();
    }
    public double getValueS3() {
        return (double)(slider_3.getValue() * Math.PI / 180);
    }
    public double getValueS4() {
        return (double)(slider_4.getValue() / 10);
    }

    private void update() {
        isUpdate = true;
    }
    public boolean isChanged() {
        return isUpdate;
    }
    public void isUpdate() {
        isUpdate = false;
    }





  }

