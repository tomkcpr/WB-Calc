package TK_Beans;

import java.awt.*;
import com.borland.jbcl.layout.*;
import javax.swing.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class URL_SelectionJPanel extends JPanel {
  private XYLayout xYLayout1 = new XYLayout();
  private JLabel server_URLJLabel = new JLabel();
  private JTextField serverURL_JTextField = new JTextField();
  private JPanel middleFillJPanel = new JPanel();
  private JLabel serverServiceJLabel = new JLabel();
  private JTextField serverServiceJTextField = new JTextField();
  private JButton jButton1 = new JButton();
  private JPanel jPanel1 = new JPanel();

  public URL_SelectionJPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    server_URLJLabel.setOpaque(true);
    server_URLJLabel.setText("  Server URL:  ");
    this.setLayout(xYLayout1);
    this.setBackground(Color.lightGray);
    serverURL_JTextField.setText("192.75.71.100");
    middleFillJPanel.setBackground(Color.lightGray);
    serverServiceJLabel.setOpaque(true);
    serverServiceJLabel.setText("  Server Service:  ");
    serverServiceJTextField.setText("PTCompute");
    jButton1.setActionCommand("connectJButton");
    jButton1.setText("Connect ...");
    this.add(server_URLJLabel,   new XYConstraints(4, 4, 83, 30));
    this.add(serverURL_JTextField,  new XYConstraints(94, 4, 95, 31));
    this.add(middleFillJPanel,   new XYConstraints(193, 4, 46, 32));
    this.add(serverServiceJLabel, new XYConstraints(241, 5, 104, 30));
    this.add(serverServiceJTextField, new XYConstraints(349, 6, 109, 27));
    this.add(jButton1, new XYConstraints(462, 6, 113, 28));
    this.add(jPanel1,  new XYConstraints(577, 6, 229, 28));
  }
}