package ProjectC;

import java.awt.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import javax.swing.border.*;
import java.awt.event.*;
import TK_Classes.WB_File;


/**
 * <p>Title: Help Frame </p>
 * <p>Description: Frame is designed to help user with basics of Wheatstone
 * Bridge and Difference Amplifier. It also helps to input data into the
 * program.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Pavel Lyssenko & Tom Kacperski
 * @version 1.0
 */

public class HelpFrame extends JFrame

{
  private JPanel jPanel1 = new JPanel();
  private JButton BridgeHelpButton = new JButton();
  private JButton AmpHelpButton = new JButton();
  private JButton InputHelpButton = new JButton();
  private TitledBorder titledBorder1;
  private TitledBorder titledBorder2;
  private TitledBorder titledBorder3;
  private String WB;
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JTextArea HelpTextArea = new JTextArea();
  private ImageIcon Icon = new ImageIcon("");
  private JLabel WBLabel = new JLabel(Icon);
  private JPanel jPanel2 = new JPanel();
  private Border border1;
  private BorderLayout borderLayout1 = new BorderLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private JButton rtdInputHelpButton = new JButton();
  private TitledBorder titledBorder4;


  /**
   *  Help Frame Constructor with jbInint method parameter
   */
  public HelpFrame() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

  }
  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    border1 = BorderFactory.createLineBorder(Color.white,1);

    titledBorder4 = new TitledBorder("");
    BridgeHelpButton.setBorder(titledBorder1);
    BridgeHelpButton.setText("Wheatstone Bridge");
    BridgeHelpButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        BridgeHelpButton_mouseReleased(e);
      }
    });

    AmpHelpButton.setBorder(titledBorder2);
    AmpHelpButton.setText("Amplifier");
    AmpHelpButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        AmpHelpButton_mouseReleased(e);
      }
    });

    InputHelpButton.setBorder(titledBorder3);
    InputHelpButton.setText("Strain Gage Help");
    InputHelpButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        InputHelpButton_mouseReleased(e);
      }
    });

    WBLabel.setBackground(Color.lightGray);
    WBLabel.setBorder(BorderFactory.createRaisedBevelBorder());
    WBLabel.setOpaque(true);
    WBLabel.setText("");

    jPanel2.setLayout(gridBagLayout2);
    jPanel2.setBorder(border1);

    rtdInputHelpButton.setBorder(titledBorder4);
    rtdInputHelpButton.setText("RTD Input Help");
    rtdInputHelpButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        rtdInputHelpButton_mouseReleased(e);
      }
    });

    jPanel2.add(BridgeHelpButton,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    jPanel2.add(AmpHelpButton,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    jPanel2.add(InputHelpButton,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    jPanel2.add(rtdInputHelpButton,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));

    HelpTextArea.setEditable(false);
    HelpTextArea.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE,15) );
    HelpTextArea.setLineWrap(true);
    HelpTextArea.setWrapStyleWord(true);

    jPanel1.setBackground(new Color(136, 192, 192));
    jPanel1.setLayout(gridBagLayout1);
    jScrollPane1.getViewport().add(HelpTextArea, null);

    jPanel1.add(jScrollPane1,  new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    jPanel1.add(WBLabel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.25
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    jPanel1.add(jPanel2,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.25
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    this.getContentPane().setLayout(borderLayout1);
    this.setTitle("Help Panel");
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    this.setSize(new Dimension(549, 427));
  }

  /**
   * Bridge button clicked -> change picture, get text from file,
   * change text in text area.
   * @param e
   */
  void BridgeHelpButton_mouseReleased(MouseEvent e)
  {
    WBLabel.setIcon(new ImageIcon("wb.jpg"));
    WB_File Text = new WB_File();
    Text.TextFileName=("wb.txt");


    Text.openText();
    HelpTextArea.setText(Text.Text);
  }

  /**
   * Amplifier Help button clicked -> change picture, get text from file,
   * change text in text area.
   * @param e
   */
  void AmpHelpButton_mouseReleased(MouseEvent e)
  {
    WBLabel.setIcon(new ImageIcon("difference_amp.gif"));
    WB_File Text = new WB_File();
      Text.TextFileName=("amp.txt");


      Text.openText();
      HelpTextArea.setText(Text.Text);
  }

  /**
   * Input Help button clicked -> change picture, get text from file,
   * change text in text area.
   * @param e
   */
  void InputHelpButton_mouseReleased(MouseEvent e)
  {
      WBLabel.setIcon(new ImageIcon("input.jpg"));
      WB_File Text = new WB_File();
      Text.TextFileName=("input.txt");


      Text.openText();
      HelpTextArea.setText(Text.Text);
  }

  void rtdInputHelpButton_mouseReleased(MouseEvent e) {
    WBLabel.setIcon(new ImageIcon("rtd_input.jpg"));
    WB_File Text = new WB_File();
    Text.TextFileName = "rtd_inputhelp.txt";
    Text.openText();
    HelpTextArea.setText (Text.Text);
  }

}
