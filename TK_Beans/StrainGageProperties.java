/*
 * @(#)WB_GageProperties.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;

import java.awt.*;
import javax.swing.border.*;
import com.borland.jbcl.layout.*;
import javax.swing.*;
import TK_Classes.VerifyInteger;
import TK_Classes.IntegerTextFieldVerifier;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import TK_Classes.VerifyDouble;
import javax.swing.event.EventListenerList;
import java.util.EventListener;
import TK_Classes.DataChangedEvent;
import TK_Interfaces.DataChangedListener;
import java.io.*;

/**
 * <p><b>Title: WB_GageProperties () </b></p>
 * <p><b>Description:</b></p>
 * A bean for strain gage characteristic input.  This bean includes four text fields for entering strain gage properties.
 * A {@link JComboBox} for entering Resistance.
 * A {@link JTextField} for entering Weight.
 * A {@link JTextField} for entering Maximum Power Dissipation.
 * A {@link JTextField} for entering plot step size.
 * Whenever a change occurs to the data in this class a {@link DataChangedEvent} is posted and any interested
 * listeners of class {@link DataChangedListener} are dispatched.  The calling environment can insert own listeners
 * into this class to receive {@link DataChangedEvent} updates.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class StrainGageProperties extends JPanel implements Serializable {
  private TitledBorder weightTitledBorder;
  private TitledBorder mainTitledBorder;
  private TitledBorder gageRTitledBorder;
  private TitledBorder maxPowerTitledBorder;
  private TitledBorder stepSizeTitledBorder;

  private JComboBox gageRJComboBox = new JComboBox();
  private JTextField maxGageLoadJTextField = new JTextField();
  private JTextField maxPowerJTextField = new JTextField();
  private JTextField stepSizeJTextField = new JTextField();

  private JPanel weightJPanel = new JPanel();
  private JPanel gageResistanceJPanel = new JPanel();
  private JPanel bridgePowerJPanel = new JPanel();
  private JPanel stepSizeJPanel = new JPanel();

  private BorderLayout weightBorderLayout = new BorderLayout();
  private GridBagLayout baseGridBagLayout = new GridBagLayout();
  private BorderLayout gageResistanceBorderLayout = new BorderLayout();
  private BorderLayout maxPowerBorderLayout = new BorderLayout();
  private BorderLayout stepSizeBorderLayout = new BorderLayout();

  private EventListenerList eventListenerList = new EventListenerList();

  // Custom Classes. -----------------------------------------------------------
  private VerifyInteger verifyWeight = new VerifyInteger ();
  private IntegerTextFieldVerifier weightInputVerifier = new IntegerTextFieldVerifier ();

  private VerifyInteger verifyPower = new VerifyInteger ();
  private IntegerTextFieldVerifier powerInputVerifier = new IntegerTextFieldVerifier ();

  private VerifyInteger verifyStepSize = new VerifyInteger ();
  private IntegerTextFieldVerifier stepSizeVerifier = new IntegerTextFieldVerifier ();

  private String mainText = "Choose gage specific properties.";
  private String gageText = "Select from available gages (120, 350, 1000)";
  private String weightText = "Enter maximum weight for gage.";
  private String powerText = "Specify maximum power allowed for gage.";
  private String stepSizeText = "Specify calculation increment size. Specify 4 " +
                                "for calculations every 4 pounds.";
  // ---------------------------------------------------------------------------

  // Private property variables. -----------------------------------------------
  private int gageResistance = 120;
  private int weight = 100;
  private int maxPower = 10;
  private int stepSize = 10;
  private int MAX_WEIGHT = 32000;
  private int MAX_POWER = 1000;
  private int MAX_STEPSIZE = weight;
  // ---------------------------------------------------------------------------

  public StrainGageProperties() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    mainTitledBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Gage Specifications");
    weightTitledBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"Load(lb)");
    gageRTitledBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"Gage(\u03A9)");
    maxPowerTitledBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"Pwr(mW)");
    stepSizeTitledBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"Step");

    // Increment (Step Size) for data table. (JTextField)
    stepSizeJTextField.setColumns (3);
    stepSizeJTextField.setMinimumSize (new Dimension (128, 16));
    verifyStepSize.setConstraints(0, MAX_STEPSIZE);
    stepSizeVerifier.setLimits(verifyStepSize);
    stepSizeJTextField.setInputVerifier (stepSizeVerifier);
    stepSizeJTextField.setText ("" + stepSize);
    stepSizeJTextField.setToolTipText (stepSizeText);
    stepSizeJTextField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate (DocumentEvent e) { changeStepSize(e); };
      public void insertUpdate (DocumentEvent e) { changeStepSize(e); };
      public void removeUpdate (DocumentEvent e) { changeStepSize(e); };
    });

    // Maximum Gage Load (JTextField)
    maxGageLoadJTextField.setColumns(3);
    maxGageLoadJTextField.setMinimumSize(new Dimension (128, 16));
    verifyWeight.setConstraints(0, MAX_WEIGHT);
    weightInputVerifier.setLimits(verifyWeight);
    maxGageLoadJTextField.setInputVerifier(weightInputVerifier);
    maxGageLoadJTextField.setText("" + weight);
    maxGageLoadJTextField.setToolTipText(weightText);
    maxGageLoadJTextField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate (DocumentEvent e) { changeWeight(e); };
      public void insertUpdate (DocumentEvent e) { changeWeight(e); };
      public void removeUpdate (DocumentEvent e) { changeWeight(e); };
    });

    // Maximum Power allowed (JTextField)
    maxPowerJTextField.setColumns(3);
    maxPowerJTextField.setMinimumSize(new Dimension (128, 16));
    verifyPower.setConstraints(0, MAX_POWER);
    powerInputVerifier.setLimits(verifyPower);
    maxPowerJTextField.setInputVerifier(powerInputVerifier);
    maxPowerJTextField.setText ("" + maxPower);
    maxPowerJTextField.setToolTipText(powerText);
    maxPowerJTextField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate (DocumentEvent e) { changePower(e); };
      public void insertUpdate (DocumentEvent e) { changePower(e); };
      public void removeUpdate (DocumentEvent e) { changePower(e); };

    });

    // JComboBox for Gage resistance input.
    gageRJComboBox.addItem("120");
    gageRJComboBox.addItem("350");
    gageRJComboBox.addItem("1000");
    gageRJComboBox.setSelectedIndex (0);
    gageRJComboBox.setToolTipText (gageText);
    gageRJComboBox.addItemListener(new ItemListener () {
      public void itemStateChanged (ItemEvent e) {
        changeResistance (e);
      }
    });

    // -------------------------------------------------------------------------
    // Panels for each of the three inputs.
    //
    //
    // -------------------------------------------------------------------------
    weightJPanel.setBackground(Color.lightGray);
    weightJPanel.setBorder(weightTitledBorder);
    weightJPanel.setLayout(weightBorderLayout);
    weightJPanel.setToolTipText(weightText);
    stepSizeJPanel.setLayout(stepSizeBorderLayout);
    weightJPanel.add(maxGageLoadJTextField, BorderLayout.CENTER);

    gageResistanceJPanel.setBackground(Color.lightGray);
    gageResistanceJPanel.setBorder(gageRTitledBorder);
    gageResistanceJPanel.setLayout(gageResistanceBorderLayout);
    gageResistanceJPanel.setToolTipText (gageText);
    gageResistanceJPanel.add(gageRJComboBox, BorderLayout.CENTER);

    bridgePowerJPanel.setBorder(maxPowerTitledBorder);
    bridgePowerJPanel.setBackground(Color.lightGray);
    bridgePowerJPanel.setLayout(maxPowerBorderLayout);
    bridgePowerJPanel.setToolTipText(powerText);
    bridgePowerJPanel.add (maxPowerJTextField, BorderLayout.CENTER);

    stepSizeJPanel.setBackground(Color.lightGray);
    stepSizeJPanel.setBorder(stepSizeTitledBorder);
    stepSizeJPanel.add(stepSizeJTextField, BorderLayout.CENTER);

    this.setToolTipText(mainText);
    this.setBorder(mainTitledBorder);
    this.setLayout (baseGridBagLayout);
    this.add(gageResistanceJPanel,    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 1), 0, 0));
    this.add(weightJPanel,    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 1), 0, 0));
    this.add(bridgePowerJPanel,    new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 1), 0, 0));
    this.add(stepSizeJPanel,        new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 1), 0, 0));
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
  // METHODS
  //
  // ---------------------------------------------------------------------------
  // Called by listener when a new STEP SIZE is entered ------------------------
  private void changeStepSize (DocumentEvent de) {
    if (verifyStepSize.verify(stepSizeJTextField.getText())) {
      stepSize = Integer.parseInt(stepSizeJTextField.getText());

      EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      queue.postEvent(new DataChangedEvent (this));
    }
  }

  // Called by listener when a new WEIGHT is entered ---------------------------
  private void changeWeight (DocumentEvent de) {
    if (verifyWeight.verify(maxGageLoadJTextField.getText())) {
      weight = Integer.parseInt(maxGageLoadJTextField.getText());
      MAX_STEPSIZE = weight;
      verifyStepSize.setConstraints(0, MAX_STEPSIZE);

      EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      queue.postEvent(new DataChangedEvent (this));
    }
  }

  // Called by listener when new POWER limit is entered ------------------------
  private void changePower (DocumentEvent de) {
    if (verifyPower.verify(maxPowerJTextField.getText())) {
      maxPower = Integer.parseInt(maxPowerJTextField.getText());

      // Send DataChangedEvent -------------------------------------------------
      EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      queue.postEvent(new DataChangedEvent (this));
    }
  }

  // Called when JComboBox selection changes -----------------------------------
  private void changeResistance (ItemEvent ie) {
    if (ie.getSource() instanceof JComboBox) {
      String resistanceStr = (String)((JComboBox)(ie.getSource())).getSelectedItem();
      try {
        gageResistance = Integer.parseInt (resistanceStr);
      } catch (NumberFormatException nfe) {
        gageResistance = 120;
        gageRJComboBox.setSelectedItem ("" + gageResistance);
      }

      // Send DataChangedEvent ---------------------------------------------------
     EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
     queue.postEvent(new DataChangedEvent (this));
    }
  }

  // ---------------------------------------------------------------------------
  // SETTERS AND GETTERS
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Sets the gage resistance choice of the Resistance JComboBox.  A resistance value is passed,
   * then is verified before the class instance and JComboBox is set.  Defaults to 120 otherwise.
   * @param gr The new value to set resistors too.
   */
  public void setGageResistance (int gr) {
    switch (gr) {
      case 120:
        gageRJComboBox.setSelectedIndex (0);
        gageResistance = gr;
        break;
      case 350:
        gageRJComboBox.setSelectedIndex (1);
        gageResistance = gr;
        break;
      case 1000:
        gageRJComboBox.setSelectedIndex (2);
        gageResistance = gr;
        break;
      default:
        gageRJComboBox.setSelectedIndex (0);
        gageResistance = 120;
    }
  }

  /**
   * Sets the weight parameter of this class.  Passed value is verified to be between 0 and MAX_WEIGHT
   * before being inserted.
   * @param w The new weight value.
   */
  public void setWeight (int w) {
    weight = (w > 0 && w < MAX_WEIGHT) ? w : MAX_WEIGHT;
    maxGageLoadJTextField.setText("" + weight);
  }

  /**
   * Sets the max allowed power dissipation instance variable of this class.  Passed value is verified to be
   * between 0 and MAX_POWER before being inserted.
   * @param mp
   */
  public void setMaxPower (int mp) {
    maxPower = (mp > 0 && mp < MAX_POWER) ? mp : MAX_POWER;
    maxPowerJTextField.setText ("" + maxPower);
  }

  /**
   * Sets the maximum stepsize for table computations.  A stepsize is the increment at which calculations
   * for the JTable take place.  Passed value is verified to be between 0 and MAX_STEPSIZE before initializing
   * class instance variable.  Step size is between 0 and weight (@see #setWeight).
   * @param sS
   */
  public void setStepSize (int sS) {
    stepSize = (sS > 0 && sS < MAX_STEPSIZE) ? sS : MAX_STEPSIZE;
    stepSizeJTextField.setText("" + stepSize);
  }

  /**
   * Returns value of the gage resistance instance field of this class.
   * @return Bridge gage resistance.
   */
  public int getGageResistance () {
    return gageResistance;
  }

  /**
   * Returns value of weight instance of this class.
   * @return Max allowed weight for gage(s).
   */
  public int getWeight () {
    return weight;
  }

  /**
   * Returns value of power instance of this class.
   * @return Max allowed power for gage(s).
   */
  public int getMaxPower () {
    return maxPower;
  }

  /**
   * Returns value of step size instance of this class.
   * @return Step value entered.
   */
  public int getStepSize () {
    return stepSize;
  }
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}
