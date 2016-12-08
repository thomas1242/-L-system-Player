import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import java.awt.geom.*;

public class LSystemPlayer
{
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    
    public static void main( String[] args ) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        } );
    }
    
    // Create the GUI and show it.  For thread safety, this method should be invoked from the EDT
    private static void createAndShowGUI()
    {
        JFrame frame = new MyFrame( WIDTH, HEIGHT);             // setup new frame
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );    // exit when the user closes the frame
        frame.setVisible( true );                         		   // make the frame visible
    }
}

class MyFrame extends JFrame
{
    private BufferedImage image = null;
    private Graphics2D g2d = null;
    private int imageWidth;
    private int imageHeight;
    private int foregroundColor;
    private int backgroundColor;
    public int numGenerations;
    double initial_x, initial_y, bearing, baseSeg;
    String[] productionRules = null;
    double delta, segScalingFactor;
    String input, initString;
    Turtle turtle = null;
    Line2D.Double line = null;                      // each frame has an associated Ellipse2D.Double
    String nGenerationStr;
    Timer timer = null;

    private Sliders sliders = null;                 // GUI components
    private InputFields inputs = null;
    private ImageDisplayPanel imgPanel = null;

    
    public MyFrame( int width, int height)   // LSystemPlayer constructor
    {
        this.setTitle( "L-System Player" ); 	     
        this.setSize( width, height );                 
        addMenu();                          // add GUI components to the extended frame

        sliders = new Sliders( this.getContentPane()  );
        inputs = new InputFields( this.getContentPane() );
        setupImage();

        addMouseMotionListener( new MouseMotionListener()
        {
            public void mouseMoved(MouseEvent event) {
               if( sliders.isChanged() ) {
                    
                    numGenerations = sliders.getValueS1();
                    delta   = sliders.getValueS2();
                    bearing = sliders.getValueS3() * Math.PI / 180;
                    segScalingFactor = sliders.getValueS4();
                   resetOrigin();
                    displayL();                                 // interpret n-generation string
                    sliders.isUpdate();
                }
                if( inputs.isChanged() ) {
                   
                    productionRules = inputs.getProdRules();
                    nGenerationString();                        // recompute n-generation string
                    resetOrigin();
                    displayL();                                 // interpret n-generation string
                    inputs.isUpdate();
                }
            }
            public void mouseDragged(MouseEvent event) {
                if( sliders.isChanged() ) {
                    
                    numGenerations = sliders.getValueS1();
                    delta = sliders.getValueS2();
                    resetOrigin();
                    displayL();                                 // interpret n-generation string          
                    sliders.isUpdate();

                }
                if( inputs.isChanged() ) {
                   
                    productionRules = inputs.getProdRules();
                    nGenerationString();                        // recompute n-generation string
                    resetOrigin();
                    displayL();                                 // interpret n-generation string
                    inputs.isUpdate();

                }
            }
        } );

        timer = new Timer(50, new ActionListener()      // 20 fps
          {
            public void actionPerformed( ActionEvent e)
            {
                timer.stop();
               
                 if( sliders.isChanged() && inputs.isChanged() ) {
                    numGenerations = sliders.getValueS1();
                    delta = sliders.getValueS2();
                    bearing = sliders.getValueS3();
                    segScalingFactor = sliders.getValueS4();
                    productionRules = inputs.getProdRules();
                    nGenerationString();                        // recompute n-generation string
                    displayL();                                 // interpret n-generation string          
                    sliders.isUpdate();
                    inputs.isUpdate();
                }
                else if( sliders.isChanged() ) {
                    numGenerations = sliders.getValueS1();
                    delta = sliders.getValueS2();
                    bearing = sliders.getValueS3();
                    segScalingFactor = sliders.getValueS4();
                    displayL();                                 // interpret n-generation string          
                    sliders.isUpdate();
                }
                else if( inputs.isChanged() ) {
                    productionRules = inputs.getProdRules();
                    nGenerationString();                        // recompute n-generation string
                    displayL();                                 // interpret n-generation string
                    inputs.isUpdate();
                }
                
    
                timer.restart();
            }
          } );

        timer.start();
       
        
        initial_x = 0;
        initial_y = 0;
        addMouseListener( new MouseAdapter()
                         {
            public void mousePressed( MouseEvent event )
            {
                if(event.getButton() == MouseEvent.BUTTON1)
                {
                }
                else if(event.getButton() == MouseEvent.BUTTON2)
                {
                }
                else if(event.getButton() == MouseEvent.BUTTON3)
                {
                }
            }
        } );
        addMouseMotionListener( new MouseMotionAdapter()
                               {
            public void mouseDragged(MouseEvent event)
            {
            }
        } );
        addMouseListener( new MouseAdapter()
                         {
            public void mouseReleased(MouseEvent event)
            {
                mouseIsReleased( event.getPoint() );
            }
        } );

        
        
        
        
        repaint();
    }
    
    
    private void mouseIsReleased( Point p )
    {
        initial_x = p.getX()/image.getWidth() - 1.0;
        initial_y = 0.5 - p.getY()/image.getHeight() ;
        displayL();
    }
    


    private void addMenu() {                       	   // addMenu() method used to setup a frame's menu bar
        
        // === setup the “File” menu ================================================================
        
        JMenu fileMenu = new JMenu( "File" );     // create a new menu that will appear as "File" when added to menu bar
        
        JMenuItem exitItem = new JMenuItem( "Exit" ); // create a new menu item that will appear as "Exit" within a menu
        exitItem.addActionListener( new ActionListener() 	// define what happens when this menu item is selected
                                   {
            public void actionPerformed( ActionEvent event )
            {
                System.exit( 0 );                     // terminate the program
            }
        } );
        
        JMenuItem saveItem = new JMenuItem( "Save image" ); 	// create a new menu item
        saveItem.addActionListener( new ActionListener()
                                   {
            public void actionPerformed( ActionEvent event )
            {
                saveImage();
            }                                                           		   // given valid input, it will display an image
        } );
        
        fileMenu.add ( saveItem );
        fileMenu.add( exitItem );                	     // attach "Exit" menu item to the "File" menu
        
        // ==== attach the new “File” menu to a menu bar and then attach the menu bar to the frame ===============
        
        JMenuBar menuBar = new JMenuBar();                  // create a new menu bar
        menuBar.add( fileMenu );                           	       // add the "File" menu to the menu bar
        this.setJMenuBar( menuBar );                                 // attach the menu bar to this frame
        
    }
    
    public void displayBufferedImage( BufferedImage image )
    {
        this.setContentPane( new JScrollPane( new JLabel( new ImageIcon( image ))));  // display the image
        this.validate();  // causes the container to lay out its subcomponents that may have been modified
    }
    
    private void saveImage() {  // prompt the user to specify the size of the n by n image
        String inputString = JOptionPane.showInputDialog("ouput file?");
        if(inputString == null || inputString.length() == 0) {
            return;
        }
        try
        {
            File outputFile = new File( inputString );
            ImageIO.write( image, "png", outputFile );
        }
        catch ( IOException e )
        {
            JOptionPane.showMessageDialog( MyFrame.this,
                                          "Error saving file",
                                          "oops!",
                                          JOptionPane.ERROR_MESSAGE );
        }
    }

    private void setupImage() {
        image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);         
        g2d = (Graphics2D) image.createGraphics();
        g2d.setColor( Color.WHITE );
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        imgPanel = new ImageDisplayPanel( image );

        this.getContentPane().add( imgPanel, BorderLayout.CENTER );
        this.pack();
    }



    // === Phase I: string rewriting 
    private void nGenerationString() {

        initString = productionRules[0];

        // build dictionary of production rules
        HashMap<Character, String> map = new HashMap<Character, String>();
        for(int i = 1; i < productionRules.length; i++) {
            try{
                 map.put(productionRules[i].charAt(0), productionRules[i].substring(4, productionRules[i].length()) );
            }
            catch(Exception e) {}
        }
        
        // Algorithm
        String input = initString;
        StringBuilder output;
        
        for(int i = 0; i < numGenerations; i++) {             // for each generation
            output = new StringBuilder();                           // output = empty string
            for(int j = 0; j < input.length(); j++) {               // for each character in the input string
                char currKey = input.charAt(j);
                if(map.get( currKey ) != null) {                    // if there exists a production rule for this symbol
                    output.append(map.get( currKey ));                  // append symbol's rule to output string
                }
                else {                                              // if no known production rule for this symbol
                    output.append(currKey);                             // symbol is copied unchanged
                }
            }
            input = output.toString();                              // input = output
        }
        
        // disp dictionary
        // for (Character key : map.keySet()) {
        //     System.out.println(key + " " + map.get(key));
        // }
        // System.out.println("generation " + numGenerations + " string = " + input + '\n');

        nGenerationStr = input;
    }

    private void displayL() {

        if(nGenerationStr == null)
            return;

        // image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);                // create square image
        g2d = (Graphics2D) image.createGraphics();                                                      // get a Graphics2D reference
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );     // specify preference for antialiasing
        
        g2d.setColor(  Color.WHITE  );
        g2d.fillRect( 0, 0, image.getWidth(), image.getHeight() );  // draw background
        g2d.setColor( Color.BLACK  );
        
        /* Phase II: Turtle interpretation ------------------------------------------------------------------------ *
         * visually interpret the n-generation string on the current image (as discussed in class)
         * the length of each segment will be base_segment_length / scaling_factor ^ n                                                                      */
        turtle = new Turtle(initial_x, initial_y, delta, nGenerationStr);        // create turtle
        turtle.run();
        
        repaint();
    }
    
     class Turtle {
        
        String commandString;   // list of commands
        nodeStack state;        // turtle state stack
        double curr_x;
        double curr_y;
        
        public Turtle(double x, double y, double delta, String commands) {
            curr_x = image.getWidth() / 2 + x * image.getWidth() / 2;
            curr_y = image.getHeight() / 2 - y * image.getHeight() / 2;
            commandString = new String( commands );
            state = new nodeStack();
            line = new Line2D.Double();
        }
        
        public void print() {
            System.out.println("turtle_x = " + curr_x);
            System.out.println("turtle_y = " + curr_y);
            System.out.println("init bearing = " + bearing);
            System.out.println("commands = " + commandString);
        }
        
        public void run() {
            
            baseSeg = (.5 * image.getHeight() * 0.5) / Math.pow(segScalingFactor, numGenerations);
            
            // interpret commands -------------------------------------------------------------------------
            
            for(int i = 0; i < commandString.length(); i++){        // for each symbol in the instruction string
                
                char c = commandString.charAt( i );
                
                if(c == '[' || c == ']' || c == '+' || c == '-' || c == 'L' || c == 'l' || c == 'R' || c == 'r' || c == 'F' || c == 'f')
                {                                                   // if known, execute corresponding action
                    executeCommand(c);
                }
                else
                {}                                                  // if unknown, ignore it
            }
        }
        
        private void executeCommand(char command) {
            if(command == '[') {
                state.push(curr_x, curr_y, bearing);    // push current state onto stack
            }
            else if(command == ']') {
                node newState = state.pop();
                if(newState == null) {
                    return;
                }
                else {
                    curr_x = newState.getX();
                    curr_y = newState.getY();
                    bearing = newState.getDelta();
                }
            }
            else if(command == '+') {
                bearing += delta * Math.PI / 180;
            }
            else if(command == '-') {
                bearing -= delta * Math.PI / 180;
            }
            else if(command == 'F') {
                takeStep('F');
            }
            else if(command == 'f') {
                takeStep('f');
            }
        }
        
        private void takeStep(char c) {
            double temp_x =  curr_x + baseSeg * Math.cos( bearing );
            double temp_y =  curr_y - baseSeg * Math.sin( bearing );
            if(c == 'F') {
                line.setLine( curr_x, curr_y, temp_x, temp_y);
                g2d.draw( line );
            }
            else if (c == 'f') {
                
            }
            curr_x = temp_x;
            curr_y = temp_y;
        }
        
    }   // end turtle class
    
    public void resetOrigin() {
        initial_x = 0;
        initial_y = 0;
    }

}	// end of ImageFrame class




