/*
 * @(#)BridgeTypeSelectionJPanel.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;

import java.awt.*;
import com.borland.jbcl.layout.*;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import TK_Classes.DataChangedEvent;
import javax.swing.event.EventListenerList;
import TK_Interfaces.DataChangedListener;
import java.util.EventListener;

/**
 * <p><b>Title: BridgeTypeSelectionJPanel () </b></p>
 * <p><b>Description:</b></p>
 * A two radio button JPanel for selecting between two givens.  This is a simple component where a user
 * selects one of two values.  Selecting first value deselects second value.  It is used as a bean for gage selection
 * in {@link WheatstoneBridgeJPanel}.  A {@link DataChangedEvent} is implemented to detect any changes to the data
 * fields in this class.  When a data change event is posted, the calling environment is notified through a
 * {@link DataChangedListener} listener.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class BridgeTypeSelectionJPanel extends JPanel implements Serializable {
  // ---------------------------------------------------------------------------
  // FIELD DEFINITIONS
  //
  //
  // ---------------------------------------------------------------------------
  private String selOne = "";
  private String selTwo = "";
  private String g2ToolTip = "";
  private String g4ToolTip = "";
  private String mainToolTip = "";
  private String title = "";

  private JRadioButton G2JRadioButton = new JRadioButton(selOne);
  private JRadioButton G4JRadioButton = new JRadioButton(selTwo);

  private TitledBorder GageSelectionJPanel_TitledBorder;

  private JPanel G4JPanel = new JPanel();
  private JPanel G2JPanel = new JPanel();

  private BorderLayout G4JPanelBorderLayout = new BorderLayout();
  private BorderLayout G2JPanelBorderLayout = new BorderLayout();

  private Border G2JPanelBorder;
  private Border G4JPanelBorder;
  private GridBagLayout GS_GridBagLayout = new GridBagLayout();

  // Custom Event fields -------------------------------------------------------
  private EventListenerList eventListenerList = new EventListenerList();
  // ---------------------------------------------------------------------------

  // Private Variables ---------------------------------------------------------
  private int selectionState = 2;
  private int TWO_GAGE =  2;
  private int FOUR_GAGE = 4;
  // ---------------------------------------------------------------------------
  public BridgeTypeSelectionJPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    GageSelectionJPanel_TitledBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),title);
    G2JPanelBorder = BorderFactory.createLineBorder(Color.white,1);
    G4JPanelBorder = BorderFactory.createLineBorder(Color.white,1);

    // 2 Gage Radio Button Panel -----------------------------------------------
    G2JPanel.setBorder(G2JPanelBorder);
    G2JPanel.setMinimumSize(new Dimension(0, 16));
    G2JRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
    G2JRadioButton.setBackground(Color.lightGray);
    G2JRadioButton.setToolTipText(g2ToolTip);
    G2JRadioButton.setHorizontalTextPosition(SwingConstants.LEFT);
    G2JRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        G2JRadioButton_mouseReleased(e);
      }
    });
    G2JPanel.setLayout(G2JPanelBorderLayout);
    G2JPanel.add(G2JRadioButton, BorderLayout.CENTER);
    G2JRadioButton.setSelected (true);
    // -------------------------------------------------------------------------

    // 4 Gage Radio Button Panel -----------------------------------------------
    G4JPanel.setBorder(G4JPanelBorder);
    G4JPanel.setMinimumSize(new Dimension(0, 16));
    G4JRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
    G4JRadioButton.setBackground(Color.lightGray);
    G4JRadioButton.setToolTipText(g4ToolTip);
    G4JRadioButton.setHorizontalTextPosition(SwingConstants.LEFT);
    G4JRadioButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        G4JRadioButton_mouseReleased(e);
      }
    });
    G4JPanel.setLayout(G4JPanelBorderLayout);
    G4JPanel.add(G4JRadioButton, BorderLayout.CENTER);
    G4JRadioButton.setSelected (false);
    // -------------------------------------------------------------------------
    this.setLayout(GS_GridBagLayout);
    this.setBorder(GageSelectionJPanel_TitledBorder);
    // GridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight,
    // double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady)
    this.setToolTipText(mainToolTip);
    this.add(G2JPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.1
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.add(G4JPanel,  new GridBagConstraints(1, 0, 1, 1, 1.0, 0.1
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
  }

  // -------------------------------------------------------------------------
  // CUSTOM EVENT HANDLING (DataChangedEvent)
  //
  //
  //
  //
  // -------------------------------------------------------------------------
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
  public void removeDataChangedListener (DataChangedListener dcl) {
    eventListenerList.remove(DataChangedListener.class, dcl);
  }

  /**
   * Method for processing events whenever AWT removes events from the system queue.  Whenever
   * a {@link DataChangedEvent} occurs on this class, listeners are queried for instances of
   *  {@link DataChangedListener} on which <code>dataChanged({@link DataChangedEvent} dce)</code>
   * are called.
   * @param e A data changed event removed from stack during execution.
   */
  public void processEvent (AWTEvent e) {
    if (e instanceof DataChangedEvent) {
      EventListener[] dcListeners = eventListenerList.getListeners(DataChangedListener.class);
      for (int i = 0; i < dcListeners.length; ((DataChangedListener)dcListeners[i++]).dataChanged ((DataChangedEvent)e));
    } else
      super.processEvent (e);
  }

  // ---------------------------------------------------------------------------
  // String setters and getters
  //
  //
  // private String selOne = "2 Gage";
  // private String selTwo = "4 Gage";
  // private String g2ToolTip = "Select a 2 gage Wheatstone Bridge.";
  // private String g4ToolTip = "Select a 4 gage Wheatstone Bridge.";
  // private String mainToolTip = "Select number of gages in Wheatstone Bridge.";
  // private String title = "Gage Selection";
  // ---------------------------------------------------------------------------
  /**
   * Sets the property string, tooltip text, titles etc of this bridge type selection panel.
   * These are to uniquely display each instantiation of this panel.
   * @param s1 First selection name.
   * @param s2 First selection name.
   * @param g2Tip Tooltip text for first selection.
   * @param g4Tip Tooltip text for second selection.
   * @param mainTip This panels tool tip text.
   * @param t Title of this panel.
   */
  public void setPropertyStrings (String s1, String s2, String g2Tip, String g4Tip, String mainTip, String t) {
    title = t;
    mainToolTip = mainTip;
    g4ToolTip = g4Tip;
    g2ToolTip = g2Tip;
    selTwo = s2;
    selOne = s1;

    G2JRadioButton.setToolTipText(g2ToolTip);
    G2JRadioButton.setText(selOne);
    G4JRadioButton.setToolTipText(g4ToolTip);
    G4JRadioButton.setText(selTwo);
    this.setToolTipText(mainToolTip);
    GageSelectionJPanel_TitledBorder.setTitle(title);
    // this.validate();
    // this.updateUI();
  }

  // ---------------------------------------------------------------------------
  // METHOD DEFINITIONS
  //
  //
  // ---------------------------------------------------------------------------

  // Mouse pressed event for G2JRadioButton ------------------------------------
  private void G2JRadioButton_mouseReleased(MouseEvent e) {
    G2JRadioButton.setSelected(true);
    G4JRadioButton.setSelected(false);
    selectionState = TWO_GAGE;

    // Send DataChangedEvent ---------------------------------------------------
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  // Mouse pressed event for G4JRadioButton ------------------------------------
  private void G4JRadioButton_mouseReleased(MouseEvent e) {
    G4JRadioButton.setSelected(true);
    G2JRadioButton.setSelected(false);
    selectionState = FOUR_GAGE;

    // Send DataChangedEvent ---------------------------------------------------
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Returns value of current selection instance.
   * @return Current selection.
   */
  public int getSelectionState () {
    return selectionState;
  }

  /**
   * Sets the current selection to passed value.  GUI is updated.
   * @param sS Value to set instance with.
   */
  public void setSelectionState (int sS) {
    selectionState = (sS == FOUR_GAGE) ? FOUR_GAGE : TWO_GAGE;
    if (selectionState == TWO_GAGE) {
      G2JRadioButton.setSelected(true);
      G4JRadioButton.setSelected(false);
    }
    else {
      G4JRadioButton.setSelected(true);
      G2JRadioButton.setSelected(false);
    }
  }

  //public void setG2ToolTip (String s) {
  //  g2ToolTip = s;
  //}

  //public void setG4ToolTip (String s) {
  //  g4ToolTip = s;
  //}

  //public void setMainToolTip (String s) {
  //  mainToolTip = s;
  //}

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}