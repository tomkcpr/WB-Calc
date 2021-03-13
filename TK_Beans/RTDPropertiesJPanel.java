/*
 * @(#)RTDPropertiesJPanel.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;

/**
 * <p><b>Title: RTDPropertiesJPanel </b></p>
 * <p><b>Description:</b></p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * Panel for entering graph and RTD specific constraings.  Any changes in components of this panel
 * are reflected by events posted for each change.  Calling environment can access these through appropriate
 * listeners and act on the changes.
 * <p><b>Company: NONE </b></p>
 * @version Project B (V1.0)
 * @author Tom Kacperski
 */

import java.awt.*;
import com.borland.jbcl.layout.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.io.*;
import java.text.NumberFormat;
import TK_Classes.VerifyInteger;
import TK_Classes.IntegerTextFieldVerifier;
import TK_Classes.VerifyDouble;
import TK_Classes.DoubleTextFieldVerifier;
import javax.swing.event.EventListenerList;
import TK_Interfaces.DataTriggerListener;
import TK_Classes.DataChangedEvent;
import TK_Classes.DataTriggerEvent;
import java.util.EventListener;
import TK_Interfaces.DataChangedListener;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class RTDPropertiesJPanel extends JPanel implements Serializable{
  private JPanel topInnerJPanel = new JPanel();
  private JPanel bottomInnerJPanel = new JPanel();
  private TitledBorder titledBorder1;
  private JPanel resistanceJPanel = new JPanel();
  private TitledBorder titledBorder2;
  private JPanel tLowJPanel = new JPanel();
  private TitledBorder titledBorder3;
  private JPanel tHighJPanel = new JPanel();
  private TitledBorder titledBorder4;
  private JPanel alphaJPanel = new JPanel();
  private JPanel maxPowerJPanel = new JPanel();
  private TitledBorder titledBorder5;
  private TitledBorder titledBorder6;
  private JSlider plotPointsJSlider = new JSlider();
  private TitledBorder titledBorder7;
  private JComboBox resistanceJComboBox = new JComboBox();
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private JTextField tLowJTextField = new JTextField();
  private JTextField tHighJTextField = new JTextField();
  private BorderLayout borderLayout3 = new BorderLayout();
  private BorderLayout borderLayout4 = new BorderLayout();
  private JTextField alphaJTextField = new JTextField();
  private JTextField maxPowerJTextField = new JTextField();
  private BorderLayout borderLayout6 = new BorderLayout();
  private TitledBorder titledBorder8;
  private JLabel sliderStatusJLabel = new JLabel();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout3 = new GridBagLayout();
  private Border border9;

  // ---------------------------------------------------------------------------
  // CUSTOM INSTANCE FIELDS
  //
  //
  // ---------------------------------------------------------------------------
  private int graphPoints = 5;
  private int resistor = 100;
  private int tHigh = 100;
  private int tLow = 0;
  private int power = 50;
  private double alpha = 0.004;

  private int CURRENT_HIGH_TEMP = 1000;
  private int CURRENT_LOW_TEMP = (int)(25 - 1 / alpha);

  private int VALID_RESISTORS[] = { 100, 200, 500, 1000, 2000};
  private int MAX_POWER = 10000;
  private int MIN_POWER = 0;
  private int DEFAULT_POWER = 500;
  private int DEFAULT_RESISTOR = 100;
  private int MAX_GRAPH_POINTS = 40;
  private int MIN_GRAPH_POINTS = 5;
  private double MIN_LOW_ALPHA = 0.0001;
  private double DEFAULT_ALPHA = 0.004;
  private double MAX_HIGH_ALPHA = 1.0;
  private int MIN_VOLTAGE = 1;
  private int ALPHA_LOWEST_TEMP = (int)(25 - 1 / MIN_LOW_ALPHA);
  private NumberFormat formatter = NumberFormat.getNumberInstance();
  private String changeMessage = "";

  private EventListenerList changeEventListenerList = new EventListenerList();
  private EventListenerList triggerEventListenerList = new EventListenerList ();

  // Dynamic verifiers.
  private VerifyInteger tHighVerifyInteger = new VerifyInteger();
  private IntegerTextFieldVerifier tHighVerifier = new IntegerTextFieldVerifier();

  private VerifyInteger tLowVerifyInteger = new VerifyInteger();
  private IntegerTextFieldVerifier tLowVerifier = new IntegerTextFieldVerifier();

  private VerifyInteger maxPowerVerifyInteger = new VerifyInteger();
  private IntegerTextFieldVerifier maxPowerVerifier = new IntegerTextFieldVerifier();

  // Update Flag(s)
  private boolean externalUpdate = false;

  public RTDPropertiesJPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"RTD Properties");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"R (\u03A9)");
    titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"T (Low)");
    titledBorder4 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"T (High)");
    titledBorder5 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"T.C. (A)");
    titledBorder6 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Plot Points");
    titledBorder7 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"P(mW)");
    titledBorder8 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Plot Points");
    border9 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    // -------------------------------------------------------------------------
    formatter.setMaximumFractionDigits(4);
    formatter.setMinimumIntegerDigits(1);

    // -------------------------------------------------------------------------
    // Top Inner Panel
    topInnerJPanel.setToolTipText("Select RTD properties to use for this circuit.");
    topInnerJPanel.setBackground(Color.lightGray);
    topInnerJPanel.setLayout(gridBagLayout3);

    // resistance Java Combo Box
    resistanceJComboBox.setMinimumSize(new Dimension (96, 24));
    resistanceJComboBox.setPreferredSize(new Dimension (128, 24));
    resistanceJComboBox.setToolTipText("Select a standard resistance for this RTD.");
    for (int i = 0; i < VALID_RESISTORS.length; resistanceJComboBox.addItem ("" + VALID_RESISTORS[i++]));
    if (resistanceJComboBox != null)
      resistanceJComboBox.setSelectedIndex(0);
    resistanceJComboBox.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) { resistorChanged (); }
    });
    resistanceJPanel.setBorder(titledBorder2);
    resistanceJPanel.setLayout(borderLayout1);
    resistanceJPanel.add(resistanceJComboBox, BorderLayout.CENTER);
    resistanceJPanel.setToolTipText("Select a standard resistance for this RTD.");

    // tLow Java Text Field.
    tLowJTextField.setMinimumSize(new Dimension (64, 24));
    tLowJTextField.setPreferredSize(new Dimension (64, 24));
    tLowJTextField.setToolTipText("Select low end temperature to analyze RTD from. (" + CURRENT_LOW_TEMP + " > )");
    tLowJTextField.setText("" + tLow);

    tLowVerifyInteger.setConstraints(CURRENT_LOW_TEMP, tHigh - 1);
    tLowVerifier.setLimits(tLowVerifyInteger);
    tLowJTextField.setInputVerifier(tLowVerifier);

    tLowJTextField.getDocument().addDocumentListener(new DocumentListener () {
      public void changedUpdate (DocumentEvent ce) {}
      public void removeUpdate (DocumentEvent ce) { lowTemperatureChanged (); }
      public void insertUpdate (DocumentEvent ce) { lowTemperatureChanged (); }
    });
    tLowJPanel.setBorder(titledBorder3);
    tLowJPanel.setLayout(borderLayout2);
    tLowJPanel.add(tLowJTextField, BorderLayout.CENTER);
    tLowJPanel.setToolTipText("Select low end temperature to analyze RTD from.(" + CURRENT_LOW_TEMP + " > )");

    // tHigh Java Text Field.
    tHighJTextField.setMinimumSize(new Dimension (64, 24));
    tHighJTextField.setPreferredSize(new Dimension (64, 24));
    tHighJPanel.setToolTipText("Selet high end temperature to analyze RTD to.( < " + CURRENT_HIGH_TEMP + ")");

    tHighVerifyInteger.setConstraints(tLow + 1, CURRENT_HIGH_TEMP);
    tHighVerifier.setLimits(tHighVerifyInteger);
    tHighJTextField.setInputVerifier(tHighVerifier);

    tHighJTextField.setText("" + tHigh);
    tHighJTextField.getDocument().addDocumentListener(new DocumentListener () {
      public void changedUpdate (DocumentEvent ce) {}
      public void removeUpdate (DocumentEvent ce) { highTemperatureChanged (); }
      public void insertUpdate (DocumentEvent ce) { highTemperatureChanged (); }
    });

    tHighJPanel.setBorder(titledBorder4);
    tHighJPanel.setLayout(borderLayout3);
    tHighJPanel.add(tHighJTextField, BorderLayout.CENTER);
    tHighJTextField.setToolTipText("Selet high end temperature to analyze RTD to.( < " + CURRENT_HIGH_TEMP + ")");

    topInnerJPanel.add(resistanceJPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    topInnerJPanel.add(tLowJPanel,  new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    topInnerJPanel.add(tHighJPanel,  new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    // -------------------------------------------------------------------------
    // Bottom Inner Panel
    bottomInnerJPanel.setBackground(Color.lightGray);
    bottomInnerJPanel.setLayout(gridBagLayout2);
    bottomInnerJPanel.setToolTipText("Select RTD properties to use for this circuit.");

    plotPointsJSlider.setMajorTickSpacing(10);
    plotPointsJSlider.setMaximum(MAX_GRAPH_POINTS);
    plotPointsJSlider.setMinimum(MIN_GRAPH_POINTS);
    plotPointsJSlider.setMinorTickSpacing(5);
    plotPointsJSlider.setPaintLabels(true);
    plotPointsJSlider.setPaintTicks(true);
    plotPointsJSlider.setBorder(titledBorder8);
    plotPointsJSlider.setMinimumSize (new Dimension (128, 48));
    plotPointsJSlider.setPreferredSize (new Dimension (128, 48));
    plotPointsJSlider.setToolTipText("Select graph/calculations step value.");
    plotPointsJSlider.setValue(graphPoints);
    plotPointsJSlider.addChangeListener(new ChangeListener () {
      public void stateChanged (ChangeEvent ce) { graphPointsChanged(); }
    });

    // Max Power
    maxPowerJTextField.setMinimumSize (new Dimension (64, 24));
    maxPowerJTextField.setPreferredSize(new Dimension (64, 24));
    maxPowerJTextField.setColumns(3);
    maxPowerJTextField.setToolTipText("Select maximum power dissipation allowed for this RTD.");
    maxPowerJTextField.setText("" + power);

    maxPowerVerifyInteger.setConstraints(MIN_POWER, MAX_POWER);
    maxPowerVerifier.setLimits(maxPowerVerifyInteger);
    maxPowerJTextField.setInputVerifier(maxPowerVerifier);
    maxPowerJTextField.setEnabled(false);

    maxPowerJTextField.getDocument().addDocumentListener(new DocumentListener () {
      public void changedUpdate (DocumentEvent ce) {}
      public void removeUpdate (DocumentEvent ce) { maxPowerChanged(); }
      public void insertUpdate (DocumentEvent ce) { maxPowerChanged(); }
    });
    maxPowerJPanel.setBorder(titledBorder7);
    maxPowerJPanel.setLayout(borderLayout6);
    maxPowerJPanel.add(maxPowerJTextField, BorderLayout.CENTER);
    maxPowerJPanel.setToolTipText("Select maximum allowed power dissipation for RTD.");
    maxPowerJPanel.setEnabled(false);

    // Temperature Coefficient (Alpha)
    alphaJTextField.setMinimumSize (new Dimension (64, 24));
    alphaJTextField.setPreferredSize(new Dimension (64, 24));
    alphaJTextField.setColumns(3);
    alphaJTextField.setToolTipText("Select the Temperature Coefficient (Alpha) for this RTD.");
    alphaJTextField.setText("" + formatter.format(alpha));

    VerifyDouble alphaVerifyDouble = new VerifyDouble();
    alphaVerifyDouble.setConstraints(MIN_LOW_ALPHA, MAX_HIGH_ALPHA);
    DoubleTextFieldVerifier alphaVerifier = new DoubleTextFieldVerifier();
    alphaVerifier.setLimits(alphaVerifyDouble);
    alphaJTextField.setInputVerifier(alphaVerifier);

    alphaJTextField.getDocument().addDocumentListener(new DocumentListener () {
      public void changedUpdate (DocumentEvent ce) {}
      public void removeUpdate (DocumentEvent ce) { alphaChanged(); }
      public void insertUpdate (DocumentEvent ce) { alphaChanged(); }
    });
    alphaJPanel.setLayout(borderLayout4);
    alphaJPanel.setBorder(titledBorder5);
    alphaJPanel.add(alphaJTextField, BorderLayout.CENTER);
    alphaJPanel.setToolTipText("Select the Temperature Coefficient (Alpha) for this RTD.");

    sliderStatusJLabel.setBorder(border9);
    sliderStatusJLabel.setOpaque(true);
    sliderStatusJLabel.setMinimumSize(new Dimension (128, 24));
    sliderStatusJLabel.setPreferredSize(new Dimension (128, 24));
    sliderStatusJLabel.setToolTipText("Displayes current points selected for graph.");
    sliderStatusJLabel.setText("" + graphPoints);
    sliderStatusJLabel.setHorizontalAlignment(SwingConstants.CENTER);

    bottomInnerJPanel.add(plotPointsJSlider,  new GridBagConstraints(0, 0, 2, 2, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 2), 0, 0));
    bottomInnerJPanel.add(alphaJPanel,  new GridBagConstraints(2, 0, 1, 1, 1.0, 0.1
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 3, 3, 2), 0, 0));
    bottomInnerJPanel.add(maxPowerJPanel,  new GridBagConstraints(3, 0, 1, 1, 1.0, 0.1
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 3, 3, 5), 0, 0));
    bottomInnerJPanel.add(sliderStatusJLabel,  new GridBagConstraints(2, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 3, 2, 5), 0, 0));

    this.setLayout(gridBagLayout1);
    this.setBorder(titledBorder1);
    this.add(topInnerJPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.25
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(bottomInnerJPanel,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }
  // ---------------------------------------------------------------------------
  // CUSTOM EVENTS AND HANDLING
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Adds a {@link TriggerEventListener} for detecting data changes in this class.
   * @param dcl The <code>TriggerEventListener</code>
   */
  public void addTriggerEventListener (DataTriggerListener tel) {
    triggerEventListenerList.add(DataTriggerListener.class, tel);
  }

  /**
   * Removes a {@link TriggerEventListener} associated with this class.
   * @param dcl The <code>TriggerEventListener</code>
   */
  public void removeTriggerEventListener (DataTriggerListener tel) {
    triggerEventListenerList.remove(DataTriggerListener.class, tel);
  }

  /**
   * Adds a {@link DataChangeListener} for detecting data changes in this class.
   * @param dcl The <code>DataChangedListener</code>
   */
  public void addDataChangedListener (DataChangedListener dcl) {
    changeEventListenerList.add(DataChangedListener.class, dcl);
  }

  /**
   * Removes a {@link DataChangeListener} associated with this class.
   * @param dcl The <code>DataChangedListener</code>
   */
  public void removeDataChangedListener (DataChangedListener dcl) {
    changeEventListenerList.remove(DataChangedListener.class, dcl);
  }

  /**
   * Method for processing events whenever AWT removes events from the system queue.  Whenever
   * a {@link DataChangedEvent} occurs on this class, listeners are queried for instances of
   *  {@link DataChangedListener} on which <code>dataChanged({@link DataChangedEvent} dce)</code>
   * are called.
   *
   *
   * Whenever a {@link TriggerEvent} occurs on this class, listeners are queried for instances of
   *  {@link DataTriggerListener} on which <code>dataChanged({@link TriggerEvent} dce)</code>
   * are called.
   * @param e A data trigger event removed from stack during execution.
   */
  protected void processEvent (AWTEvent e) {
    if (e instanceof DataChangedEvent) {
      EventListener[] changeEventListeners = changeEventListenerList.getListeners(DataChangedListener.class);
      for (int i = 0; i < changeEventListeners.length;
           ((DataChangedListener)changeEventListeners[i++]).dataChanged((DataChangedEvent)e));
    } else
      if (e instanceof DataTriggerEvent) {
        EventListener[] triggerEventListeners = triggerEventListenerList.getListeners(DataTriggerListener.class);
        for (int i = 0; i < triggerEventListeners.length;
             ((DataTriggerListener)triggerEventListeners[i++]).triggerReceived((DataTriggerEvent)e));
      } else
        super.processEvent (e);
  }

  // ---------------------------------------------------------------------------
  // Listener Methods
  //
  //
  // ---------------------------------------------------------------------------
  // Trigger Event
  private void resistorChanged () {
    resistor = VALID_RESISTORS[resistanceJComboBox.getSelectedIndex()];

    powerRange (resistor, alpha, tHigh);

    if (!externalUpdate) {
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      eventQueue.postEvent(new DataTriggerEvent(this));
      changeMessage = "Circuit resistor: " + resistor;
    }
  }

  private void maxPowerChanged () {
    int currentPower = DEFAULT_POWER;
    try {
      currentPower = Integer.parseInt(maxPowerJTextField.getText().trim());
    } catch (NumberFormatException nfe) {}
    power = currentPower;

    if (!externalUpdate) {
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      eventQueue.postEvent(new DataTriggerEvent(this));
      changeMessage = "Circuit Max Power: " + power;
    }
  }

  private void alphaChanged () {
    double currentAlpha = DEFAULT_ALPHA;
    int alphaT = 0;
    try {
      currentAlpha = Double.parseDouble (alphaJTextField.getText().trim());
    } catch (NumberFormatException nfe) {}
    alpha = currentAlpha;
    alphaT  = (int)(25 - 1 / alpha);
    if (alphaT > CURRENT_LOW_TEMP) {
      CURRENT_LOW_TEMP = alphaT;
      if (alphaT > CURRENT_HIGH_TEMP)
        CURRENT_HIGH_TEMP = CURRENT_LOW_TEMP + 1;

      if (tLow < CURRENT_LOW_TEMP) {
        tLow = CURRENT_LOW_TEMP;
        tLowJTextField.setText("" + tLow);
      }

      if (tHigh <= tLow) {
        tHigh = tLow + 1;
        tHighJTextField.setText("" + tHigh);
      }
      tHighVerifyInteger.setConstraints(tHigh, CURRENT_HIGH_TEMP);
      tLowVerifyInteger.setConstraints(CURRENT_LOW_TEMP, tLow);

      tLowJPanel.setToolTipText("Selet high end temperature to analyze RTD to.(" + CURRENT_LOW_TEMP + " > )");
      tLowJTextField.setToolTipText("Selet high end temperature to analyze RTD to.(" + CURRENT_LOW_TEMP + " > )");
      tHighJPanel.setToolTipText("Selet high end temperature to analyze RTD to.( < " + CURRENT_HIGH_TEMP + ")");
      tHighJTextField.setToolTipText("Selet high end temperature to analyze RTD to.( < " + CURRENT_HIGH_TEMP + ")");
    }
    else {
      if (alphaT < CURRENT_LOW_TEMP) {
        CURRENT_LOW_TEMP = alphaT;
        tLowVerifyInteger.setConstraints (CURRENT_LOW_TEMP, tLow);
        tLowJPanel.setToolTipText("Selet high end temperature to analyze RTD to.( < " + CURRENT_LOW_TEMP + ")");
        tLowJTextField.setToolTipText("Selet high end temperature to analyze RTD to.( < " + CURRENT_LOW_TEMP + ")");
      }
    }

    powerRange (resistor, alpha, tHigh);

    if (!externalUpdate) {
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      eventQueue.postEvent(new DataTriggerEvent(this));
      changeMessage = "Temperature Coefficient: " + formatter.format(alpha);
    }
  }

  private void lowTemperatureChanged () {
    int currentLow = CURRENT_LOW_TEMP;
    try {
      currentLow = Integer.parseInt(tLowJTextField.getText());
    } catch (NumberFormatException nfe) {}

    currentLow = (currentLow >= CURRENT_LOW_TEMP && currentLow < tHigh) ? currentLow : CURRENT_LOW_TEMP;
    tHighVerifyInteger.setConstraints(currentLow + 1, CURRENT_HIGH_TEMP);
    tLow = currentLow;

    if (!externalUpdate) {
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      eventQueue.postEvent(new DataTriggerEvent(this));
      changeMessage = "Low Temperature: " + tLow;
    }
  }

  // Data Event
  private void highTemperatureChanged () {
    int currentHigh = CURRENT_HIGH_TEMP;
    try {
      currentHigh = Integer.parseInt(tHighJTextField.getText().trim());
    } catch (NumberFormatException nfe) {}

    currentHigh = (currentHigh > tLow && currentHigh <= CURRENT_HIGH_TEMP) ? currentHigh : CURRENT_HIGH_TEMP;
    tLowVerifyInteger.setConstraints(CURRENT_LOW_TEMP, currentHigh - 1);
    tHigh = currentHigh;

    powerRange (resistor, alpha, tHigh);

    if (!externalUpdate) {
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      eventQueue.postEvent(new DataChangedEvent(this));
      changeMessage = "High Temperature: " + tHigh;
    }
  }

  private void graphPointsChanged () {
    graphPoints = plotPointsJSlider.getValue();
    sliderStatusJLabel.setText("" + graphPoints);

    if (!externalUpdate) {
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      eventQueue.postEvent(new DataChangedEvent(this));
      changeMessage = "Graph points: " + graphPoints;
    }
  }

  // Helper method (NOT Listener method) Returns resistor array index values on resistor choices.
  // Not entirely accurate.  Power selected is displayed as an 'int'.
  private double powerRange (int Ro, double alpha, int tH) {
    double Rd = Ro * (1 + alpha * (tH - 25));
    double Ia = (double)MIN_VOLTAGE / (double)(Rd + Ro);
    double Ib = (double)MIN_VOLTAGE / (double)(Ro * 2);
    int cMPower = 0;
    if (Rd >= Ro) {
      cMPower = (int)((Ro * Math.pow (Ib, 2)) * 1000.0 + 1.0);
    } else
      if (Ro > Rd) {
        cMPower = (int)((Ro * Math.pow (Ia, 2)) * 1000.0 + 1.0);
      }

    //System.out.println("cMPower = " + cMPower + ", Ia = " + Ia + ", Ib = " + Ib + ", Rd = " + Rd);

    if (MIN_POWER < cMPower) {
      MIN_POWER = cMPower;
      if (power < MIN_POWER)
        power = MIN_POWER;
      if (MAX_POWER <= MIN_POWER)
        MAX_POWER = MIN_POWER + 1;
      maxPowerVerifyInteger.setConstraints(MIN_POWER, MAX_POWER);
      maxPowerJTextField.setText("" + power);
    }
    return cMPower;
  }

  private int vR (int r) {
    int state = -1;
    for (int i = 0; i < VALID_RESISTORS.length; i++)
      if (r == VALID_RESISTORS[i])
        return i;
    return state;
  }
  // ---------------------------------------------------------------------------
  // GETTERS
  //
  // ---------------------------------------------------------------------------
  /**
   * Returns change string.  This string is set anytime a selection is made.
   * @return Change string.
   */
  public String getChangeString () {
    return changeMessage;
  }

  // Trigger Event
  /**
   * Returns selected Low Temperature.
   * @return Low temperature.
   */
  public int getTLow () {
    return tLow;
  }

  /**
   * Returns selected power specification.
   * @return Circuit max power.
   */
  public int getPower () {
    return power;
  }

  /**
   * Returns temperature coefficient of this RTD gage.
   * @return alpha (Temperature Coefficient)
   */
  public double getAlpha () {
    return alpha;
  }

  /**
   * Returns selected resistor for this circuit.
   * @return Selected resistor.
   */
  public int getResistor () {
    return resistor;
  }

  // Data Event
  /**
   * Returns selected graph points.
   * @return Graph points selected.
   */
  public int getGraphPoints () {
    return graphPoints;
  }

  /**
   * Returns high temperature selected for this circuit.
   * @return High temperature selected.
   */
  public int getTHigh () {
    return tHigh;
  }
  // ---------------------------------------------------------------------------
  // SETTERS
  //
  // ---------------------------------------------------------------------------
  /**
   * Sets low temperature of this circuit.
   * @param tL Low temperature setting.
   */
  public void setTLow (int tL) {
    tLow = (tL >= CURRENT_LOW_TEMP && tL <= tHigh) ? tL : CURRENT_LOW_TEMP;
    externalUpdate = true;
    tLowJTextField.setText("" + tLow);
    externalUpdate = false;
  }

  /**
   * Sets high temperature of this circuit.
   * @param tH High temperature setting.
   */
  public void setTHigh (int tH) {
    tHigh = (tH >= CURRENT_LOW_TEMP && tH <= CURRENT_HIGH_TEMP) ? tH : CURRENT_HIGH_TEMP;
    externalUpdate = true;
    tHighJTextField.setText("" + tHigh);
    externalUpdate = false;
  }

  /**
   * Sets max power dissipation allowed for this circuit.
   * @param p Max power dissipation allowed.
   */
  public void setPower (int p) {
    power = (p >= 0 && p <= MAX_POWER) ? p : MAX_POWER;
    externalUpdate = true;
    maxPowerJTextField.setText("" + power);
    externalUpdate = false;
  }

  /**
   * Sets temperature coefficient (alpha) for this RTD.
   * @param a Temperature coefficient (alpha)
   */
  public void setAlpha (double a) {
    alpha = (a >= MIN_LOW_ALPHA && a <= MAX_HIGH_ALPHA) ? a : DEFAULT_ALPHA ;
    externalUpdate = true;
    alphaJTextField.setText("" + alpha);
    externalUpdate = false;
  }

  /**
   * Sets graph points JSlider value selection.
   * @param gp Graph points to use.
   */
  public void setGraphPoints (int gp) {
    graphPoints = (gp >= MIN_GRAPH_POINTS && gp <= MAX_GRAPH_POINTS) ? gp : MIN_GRAPH_POINTS;
    externalUpdate = true;
    System.out.println ("<0>");
    plotPointsJSlider.setValue(graphPoints);
    System.out.println ("<1>");
    externalUpdate = false;
  }

  /**
   * Sets bridge initial resistor value.
   * @param r R (ohms) initial.
   */
  public void setResistor (int r) {
    int resistorMatch = vR (r);
    resistor = (resistorMatch != -1) ? r : DEFAULT_RESISTOR;
    externalUpdate = true;
    if (resistorMatch == -1)
      resistanceJComboBox.setSelectedIndex(0);
    else
      resistanceJComboBox.setSelectedIndex(resistorMatch);
    externalUpdate = false;
  }

  // Writers & readers
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}