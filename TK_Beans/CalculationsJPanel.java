/*
 * @(#)CalculationsJPanel.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;

import java.awt.*;
import com.borland.jbcl.layout.*;
import javax.swing.*;
import javax.swing.border.*;
import TK_Classes.WB_Calculator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.event.EventListenerList;
import TK_Interfaces.DataChangedListener;
import TK_Classes.DataChangedEvent;
import java.util.EventListener;
import java.io.*;
import TK_Classes.CommandEvent;
import TK_Interfaces.CommandListener;

/**
 * <p><b>Title: CalculationsJPanel () </b></p>
 * <p><b>Description:</b></p>
 * Data entered in {@link WheatstoneBridgeJPanel} is transferred through {@link DataIntermediate} directly to an object
 * of {@link WB_Calculator} in this class.  An object of {@link WB_Calculator} class here should, therefore, always be
 * in sync with any selections user makes.  The updates are dynamic requireing little verification.  This bean contains
 * a simple interface for calculating, accepting, demoing and clearing data in a {@link TK_JTablePanel}.  Verification
 * will be done by the 'Accept' button to ensure object of {@link WB_Calculator} in this class is in sync with an
 * object of {@link DataIntermediate} in {@link StrainGageBridgeJPanel} bean. </p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class CalculationsJPanel extends JPanel implements Serializable {
  private EventListenerList eventListenerList = new EventListenerList();
  private JPanel imageJPanel = new JPanel() {
    //public void paint (Graphics g) {
    //  paintComponent (g);
    //}
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      URL imageLocation = CalculationsJPanel.class.getResource("wb.jpg");
      Image bridgeImage = Toolkit.getDefaultToolkit().getImage (imageLocation/*"wb.jpg"*/);
      //ImageIcon mIcon = new ImageIcon (bridgeImage);

      MediaTracker tracker = new MediaTracker (this);
      tracker.addImage(bridgeImage, 0);

      try { tracker.waitForID(0); } catch (InterruptedException exception) { System.out.println("Image bridgeImage not loaded!"); }

      if (getWidth() - 10 > 5 && getHeight() - 10 > 5)
        g2d.drawImage(bridgeImage, 5, 5, getWidth() - 10, getHeight() - 10, this);
    }
  };

  private JPanel buttonJPanel = new JPanel();

  private JButton acceptJButton = new JButton();
  private JButton calculateJButton = new JButton();
  private JButton clearJButton = new JButton();
  private JButton graphJButton = new JButton();

  private Border whiteButtonBorder;
  private JLabel calcStatusJLabel = new JLabel();
  private TitledBorder statusTitledBorder;
  private Border imageBorder;
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout3 = new GridBagLayout();
  private JComboBox circuitSelectionJComboBox = new JComboBox();
  private JLabel circuitSelectionJLabel = new JLabel();
  private int COMMAND_ID = -1;
  //private JLabel WBPic = new JLabel(new ImageIcon("wb.jpg"));

  /**
   * Constructs a default <code>CalculationsJPanel()</code> object setting up interface to default values.
   * Swing components are added and listeners for all required actions allowed are implemented.
   */
  public CalculationsJPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    whiteButtonBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white,2),BorderFactory.createEmptyBorder(1,1,1,1));
    imageBorder = BorderFactory.createLineBorder(Color.white,2);
    statusTitledBorder = new TitledBorder(BorderFactory.createLineBorder(Color.white,2),"");

    imageJPanel.setBackground(Color.lightGray);
    imageJPanel.setMinimumSize(new Dimension (0, 0));
    imageJPanel.setBorder(imageBorder);
    imageJPanel.setPreferredSize (new Dimension (0, 256));
    imageJPanel.setLayout(gridBagLayout3);
    imageJPanel.setToolTipText("Image of an Wheatstone Bridge circuit.");
    //imageJPanel.imageUpdate()

    buttonJPanel.setBackground(Color.lightGray);
    buttonJPanel.setBorder(whiteButtonBorder);
    buttonJPanel.setLayout(gridBagLayout1);
    buttonJPanel.setMinimumSize(new Dimension (0, 0));
    buttonJPanel.setPreferredSize (new Dimension (0, 64));
    buttonJPanel.setToolTipText("Select an action.");

    acceptJButton.setText(" Accept  ");
    acceptJButton.setMinimumSize(new Dimension (0, 0));
    acceptJButton.setPreferredSize (new Dimension (0, 32));
    acceptJButton.setToolTipText("Verifies that data for table is ready to be calculated.");
    acceptJButton.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        //System.out.println ("Action Triggered!");
        COMMAND_ID = 0;
        sendCommandOccurred();
      }
    });

    calculateJButton.setText("Calculate");
    calculateJButton.setMinimumSize(new Dimension (0, 0));
    calculateJButton.setPreferredSize (new Dimension (0, 32));
    calculateJButton.setToolTipText("Press to calculate with selected values and display in table.");
    calculateJButton.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        COMMAND_ID = 1;
        sendCommandOccurred();
        graphJButton.setEnabled(true);
      }
    });

    clearJButton.setText("  Clear  ");
    clearJButton.setMinimumSize(new Dimension (0, 0));
    clearJButton.setPreferredSize (new Dimension (0, 32));
    clearJButton.setToolTipText("Press to clear the table.");
    clearJButton.addActionListener (new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        COMMAND_ID = 2;
        sendCommandOccurred();
        graphJButton.setEnabled(false);
      }
    });

    graphJButton.setText("  Graph   ");
    graphJButton.setMinimumSize(new Dimension (0, 0));
    graphJButton.setPreferredSize (new Dimension (0, 32));
    graphJButton.setToolTipText("Press to demonstrate calculations using default values.");
    graphJButton.setEnabled(false);
    graphJButton.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        COMMAND_ID = 3;
        sendCommandOccurred();
      }
    });
    //graphJButton.setEnabled(false);

    circuitSelectionJComboBox.addItem("Strain Gage");
    circuitSelectionJComboBox.addItem("RTD");
    circuitSelectionJComboBox.setMinimumSize (new Dimension (0, 24));
    circuitSelectionJComboBox.setPreferredSize (new Dimension (0, 24));
    circuitSelectionJComboBox.addActionListener (new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        COMMAND_ID = 4;
        sendCommandOccurred();
      }
    });

    circuitSelectionJLabel.setMinimumSize (new Dimension (0, 24));
    circuitSelectionJLabel.setPreferredSize(new Dimension (0, 24));
    circuitSelectionJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    circuitSelectionJLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    circuitSelectionJLabel.setText("CIRCUIT  ");

    calcStatusJLabel.setBackground(Color.lightGray);
    calcStatusJLabel.setBorder(statusTitledBorder);
    calcStatusJLabel.setOpaque(true);
    calcStatusJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    calcStatusJLabel.setText("Table/Graph Calculations.");
    calcStatusJLabel.setMinimumSize(new Dimension (0, 0));
    calcStatusJLabel.setPreferredSize (new Dimension (0, 32));
    calcStatusJLabel.setToolTipText("Displayes additional information about choices made in this panel.");

    buttonJPanel.add(acceptJButton,        new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
    buttonJPanel.add(clearJButton,         new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
    buttonJPanel.add(calculateJButton,       new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
    buttonJPanel.add(graphJButton,       new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));

    buttonJPanel.add(circuitSelectionJLabel,     new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));

    buttonJPanel.add(circuitSelectionJComboBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));

    this.setLayout(gridBagLayout2);
    this.setToolTipText("Panel for calculating from specified circuit parameters.");
    this.add(imageJPanel,  new GridBagConstraints(0, 0, 1, 4, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
    this.add(buttonJPanel,  new GridBagConstraints(0, 4, 1, 1, 1.0, 0.4
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
    this.add(calcStatusJLabel,  new GridBagConstraints(0, 5, 1, 1, 1.0, 0.2
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
  }

  // ---------------------------------------------------------------------------
  // EVENTS
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Adds a {@link DataChangeListener} for detecting data changes in this class.
   * @param dcl The <code>DataChangedListener</code>
   */
  public void addDataChangedListener (DataChangedListener dcl) {
    eventListenerList.add(DataChangedListener.class, dcl);
  }

  /**
   * Removes a {@link DataChangeListener} associated with this class.
   * @param dcl The <code>DataChangedListener</code>
   */
  public void removeDataChangedListener (DataChangedListener dcl ) {
    eventListenerList.remove(DataChangedListener.class, dcl);
  }

  /**
   * Adds a {@link CommandListener} to this panel.  Whenever an action on a component within
   * this panel occurs, commandGiven will be called on the {@link CommandListener}.
   * @param cl Command listener to add.
   */
  public void addCommandListener (CommandListener cl) {
    eventListenerList.add(CommandListener.class, cl);
  }

  /**
   * Removes an associated {@link CommandListener} from this panel.  Events will no longer be
   * delivered to it upon removal.
   * @param cl CommandListener to remove.
   */
  public void removeCommandListener (CommandListener cl) {
    eventListenerList.remove(CommandListener.class, cl);
  }

  /**
   * Method for processing events whenever AWT removes events from the system queue.  Whenever
   * a {@link DataChangedEvent} occurs on this class, listeners are queried for instances of
   *  {@link DataChangedListener} on which <code>dataChanged({@link DataChangedEvent} dce)</code>
   * are called.
   *  A CommandEvent is created anytime a button is pressed or a selection made. {@link CommandEvent)
   * @param e A data changed event removed from stack during execution.
   */
  public void processEvent (AWTEvent e) {
    if (e instanceof DataChangedEvent) {
      EventListener[] dataListeners = eventListenerList.getListeners(DataChangedListener.class);
      for (int i = 0; i < dataListeners.length; ((DataChangedListener)dataListeners[i++]).dataChanged((DataChangedEvent)e));
    } else
      if (e instanceof CommandEvent) {
        EventListener[] commandListeners = eventListenerList.getListeners(CommandListener.class);
        for (int i = 0; i < commandListeners.length; ((CommandListener)commandListeners[i++]).commandGiven ((CommandEvent)e));
      } else {
      super.processEvent(e);
      }
  }
  // ---------------------------------------------------------------------------
  // Set WB_Calculator to
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Sends an event alerting calling environment of an action.
   */
  public void sendCommandOccurred () {
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new CommandEvent(this));
  }

  /**
   * Retrieves the current command of an event.
   * @return Current command
   */
  public int retrieveCurrentCommand () {
    return COMMAND_ID;
  }

  /**
   * Returns current selected circuit for which calculations and graphing is to be done.
   * @return The circuit for which actions will take place.
   */
  public int getCircuitSelection () {
    return circuitSelectionJComboBox.getSelectedIndex();
  }

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }

  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}