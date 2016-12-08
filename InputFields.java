import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;


class InputFields extends JPanel {
    JTextField initiatorField;
    JTextField[] prodRuleFields;
    private boolean isUpdate;
 
    public InputFields( Container contentPane ) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel textPnl = new JPanel();
        textPnl.setLayout( new BoxLayout(textPnl, BoxLayout.PAGE_AXIS) );
        textPnl.add(createEntryFields());
        textPnl.add(createButtons());

        textPnl.setPreferredSize(new Dimension(200, 400));
        contentPane.add(textPnl, BorderLayout.WEST);
    }
 
    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
 
        JButton button = new JButton("Update");
        button.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent event )
                {
                  update();
                    
                }
         } );
        panel.add(button);
 
        button = new JButton("Clear");
        button.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent event )
                {
                     initiatorField.setText("");
                     for(int i = 0; i < prodRuleFields.length; i++)
                     {
                        prodRuleFields[i].setText("");
                     }
                     update();
                }
        } );
        panel.add(button);
 
        panel.setBorder( BorderFactory.createEmptyBorder(0, 0, 5, 5) );
        return panel;
    }

    /* Called when one of the fields gets the focus so that we can select the focused field. */
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c instanceof JTextField) {
            ((JTextField)c).selectAll();
        }
    }
 
    protected JComponent createEntryFields() {
        JPanel panel = new JPanel( new GridLayout( 0, 1) );

        prodRuleFields = new JTextField[15];

        String[] labelStrings = {
            "Initiator string: ",
            "Production rules (one per line): "
        };
    
        int numComps = prodRuleFields.length + labelStrings.length - 1;

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[numComps];
        int fieldNum = 0;
 
        //Create the text field and set it up.
        initiatorField  = new JTextField();
        fields[fieldNum++] = initiatorField;
 
        for(int i = 0; i < prodRuleFields.length; i++) {
            prodRuleFields[i] = new JTextField();
            fields[fieldNum++] = prodRuleFields[i];
        }
 
        // add everything to the panel
        for (int i = 0; i < numComps; i++) {
            if( i < labelStrings.length) {
                labels[i] = new JLabel(labelStrings[i], JLabel.CENTER);
                panel.add(labels[i], BorderLayout.CENTER);
            }
            panel.add(fields[i], BorderLayout.CENTER);
 
            JTextField tf = (JTextField)fields[i];
            
            tf.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent event )
                {
                  update();
                }
            } );
        }
       
        return panel;
    }

    public String[] getProdRules() {

        if(prodRuleFields.length <= 0) {
            return null;
        }

        String[] rules = new String[prodRuleFields.length + 1];
        rules[0] = initiatorField.getText();
        for(int i = 1; i <= prodRuleFields.length; i++) {
           rules[i] = prodRuleFields[i - 1].getText();
        } 
        return rules;
    }
    
    /* Called when the user clicks the button or presses enter in a text field. */
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





