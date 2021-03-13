/*
 * @(#)TK_JTextSlider.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.*;
import java.util.*;
import com.borland.jbcl.layout.*;
import java.io.*;
import com.borland.dbswing.*;
import javax.swing.event.*;
import java.text.NumberFormat;
import javax.swing.text.*;
import TK_Classes.*;
import java.awt.event.ActionListener;
import java.util.Locale;
import TK_Interfaces.DataChangedListener;
import TK_Classes.DataChangedEvent;

/**
 * <p><b>Title: TK_JTextSlider () </b></p>
 * <p><b>Description:</b></p>
 * A bean for data input using a {@link JSlider} / {@link JTextfield} combination, where changes to one component
 * effects the state of the other.  Input verification and limit checking are being performed during input.  {@link DataChangedListeners}
 * are used to detect any changes and alert any calling environment components interested.  The calling environment
 * component can then retrieve them using setters and getters of this class.  This bean is initialized through a
 * {@link PropertyData} inner class from the calling environment.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski
 */
public class TK_JTextSlider extends JPanel implements Serializable {
  // ---------------------------------------------------------------------------
  // VARIABLE BLOCK
  //
  //
  //
  // ---------------------------------------------------------------------------
  private TitledBorder BridgeVoltageINTitledBorder;
  private JSlider VINJSlider = new JSlider();
  private JPanel VJSliderJPanel = new JPanel();
  private JPanel fillJPanel = new JPanel ();
  private JTextField VINJTextField = new JTextField();
  private JPanel TopMJPanel = new JPanel();
  private JPanel BottomMJPanel = new JPanel();
  private JPanel JSLabelsJPanel = new JPanel();
  private GridLayout LabelsGridLayout = new GridLayout();

  private VerifyDouble verifyInputDouble = new VerifyDouble();
  private DoubleTextFieldVerifier doubleVerify = new DoubleTextFieldVerifier();

  private GridBagLayout BridgeVINGridBagLayout = new GridBagLayout();
  private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  private boolean remoteUpdate = true;
  private boolean removeAllowed = true;
  private boolean insertAllowed = true;
  NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("en", "CA"));
  EventListenerList eventListenerList = new EventListenerList();

  // -------------------------------------------------------------------------
  // INSTANCE FIELDS (for individual TK_JTextSlider instances)
  //
  //
  //
  // -------------------------------------------------------------------------
  private double voltage;

  private int sliderMax;
  private int sliderMin;
  private int sliderMajorTickSpacing;
  private int sliderMinorTickSpacing;
  private int sliderInitialValue;
  private double validFinalMax;
  private double validFinalMin;
  private double sliderToValueRatio;
  private String labelStringArray[];
  // ToolTipText
  private String mainPanelTextToolTip;
  private String jsliderToolTipText;
  String jtextfieldTextToolTip;
  private String title;

  /**
   * Default constructor that sets up this component according to the public inner class {@link PropertyData} default
   * values.
   */
  public TK_JTextSlider() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    BridgeVoltageINTitledBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),title);
    setProperties (new PropertyData());

    // Bridge Voltage IN JSlider -----------------------------------------------
    VINJSlider.setLabelTable(null);
    VINJSlider.setPaintTicks(true);
    VINJSlider.setBackground(Color.lightGray);

    VINJSlider.setMajorTickSpacing(sliderMajorTickSpacing);
    VINJSlider.setMaximum(sliderMax);
    VINJSlider.setMinimum(sliderMin);
    VINJSlider.setMinimumSize(new Dimension (0, 0));
    VINJSlider.setMinorTickSpacing(sliderMinorTickSpacing);
    VINJSlider.setToolTipText(jsliderToolTipText);
    VINJSlider.setValue(sliderInitialValue);

    VINJSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        BridgeVINJSlider_stateChanged(e);
      }
    });
    // -------------------------------------------------------------------------

    // JScroller Labels --------------------------------------------------------
    JSLabelsJPanel.setBackground(Color.lightGray);
    JSLabelsJPanel.setToolTipText(jsliderToolTipText);
    //JSLabelsJPanel.setLayout(LabelsGridLayout);
    //LabelsGridLayout.setColumns(8);
    //LabelsGridLayout.setRows(1);
    addLabel (true, SwingConstants.CENTER, SwingConstants.CENTER, Color.lightGray, labelStringArray);
    // -------------------------------------------------------------------------

    // Bridge Voltage INput Text field -----------------------------------------
    verifyInputDouble.setConstraints(validFinalMin, validFinalMax);
    doubleVerify.setLimits(verifyInputDouble);
    VINJTextField.setInputVerifier (doubleVerify);
    VINJTextField.getDocument().addDocumentListener(new DocumentListener(){
        public void changedUpdate (DocumentEvent e) { };
        public void insertUpdate (DocumentEvent e) { if (insertAllowed) textChanged(e); };
        public void removeUpdate (DocumentEvent e) { if (removeAllowed) textChanged(e); };
    });
    VINJTextField.setColumns(3);
    VINJTextField.setToolTipText(jtextfieldTextToolTip);
    VINJTextField.setText ("" + voltage);
    // -------------------------------------------------------------------------

    // Panel under Text Field for Manual voltage input -------------------------
    TopMJPanel.setLayout(verticalFlowLayout1);
    TopMJPanel.setBackground(Color.lightGray);
    TopMJPanel.setToolTipText(jtextfieldTextToolTip);
    TopMJPanel.add(VINJTextField, null);
    // -------------------------------------------------------------------------

    // Bottom Manual input Panel.  ---------------------------------------------
    BottomMJPanel.setBackground(Color.lightGray);
    BottomMJPanel.setToolTipText(jtextfieldTextToolTip);
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // BEAN'S BASE PANEL
    //
    //
    // -------------------------------------------------------------------------
    this.setToolTipText(mainPanelTextToolTip);
    this.setLayout(BridgeVINGridBagLayout);
    this.setBorder(BridgeVoltageINTitledBorder);
    // -------------------------------------------------------------------------
    // GridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight,
    // double weightx, double weighty, int anchor, int fill, Insets insets,
    // int ipadx, int ipady)
    // -------------------------------------------------------------------------
    this.add(VINJSlider, new GridBagConstraints(0, 0, 12, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(JSLabelsJPanel,  new GridBagConstraints(0, 1, 12, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    this.add(TopMJPanel,  new GridBagConstraints(12, 0, 3, 1, 0.2, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(BottomMJPanel,  new GridBagConstraints(12, 1, 3, 1, 0.2, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }

  // ---------------------------------------------------------------------------
  // MUTATORS AND ACCESSORS
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * <p><b>Title: PropertyData () </b></p>
   * <p><b>Description:</b></p>
   * An convenience inner class of {@link TK_JTextSlider} for initializing individual components within <code>TK_JTextSlider</code>
   * to desired values.  Values are set through public instance fields, then the entire object is passed to #setProperties of
   * TK_JTextSlider.
   * <p><b>Copyright (c) 2003 Tom Kacperski.  All rights reserved. </b></p>
   * <p><b>Company: NONE </b></p>
   * @version Project A (V1.0)
   * @author Tom Kacperski
   */
  public static class PropertyData {
    // COPY AND PASTE BLOCK (Use these to quickly add an initialization block to any code.)
    // PropertyData.sliderMax = ; // int
    // PropertyData.sliderMin = ; // int
    // PropertyData.sliderMajorTickSpacing = ; // int
    // PropertyData.sliderMinorTickSpacing = ; // int
    // PropertyData.sliderInitialValue = ; // int
    // PropertyData.validFinalMax = ; // double
    // PropertyData.validFinalMin = ; // double
    // PropertyData.sliderToValueRatio = ; // double
    // PropertyData.labelStringArray[] = ; // String
    // PropertyData.mainPanelToolTipText = ; // String
    // PropertyData.jsliderToolTipText = ; // String
    // PropertyData.jtextfieldToolTipText = ; // String
    // PropertyData.title = ; // String
    /**  Sliders maximum value.     */
    public int sliderMax = 0;
    /**  Sliders minimum value. */
    public int sliderMin = 0;
    /**  Sliders major tick spacing. */
    public int sliderMajorTickSpacing = 0;
    /**  Sliders minor tick spacing.   */
    public int sliderMinorTickSpacing = 0;
    /**  Sliders initial value.   */
    public int sliderInitialValue = 0;
    /**  Valid final maximum that the JTextArea and JTextSlider are allowed to reach.   */
    public double validFinalMax = 0.0;
    /**  Valid final minimum that the JTextArea and JTextSlider are allowed to reach.   */
    public double validFinalMin = 0.0;
    /**
     *  Slider to actual value expected.  A slider max of 1000 has a sliderToValueRatio of 100
     *  when a value of 10 is used for validFinalMax.
     */
    public double sliderToValueRatio = 0.0;
    /**  A string array with the desired labels.   */
    public String labelStringArray[] = {"0"};

    // ToolTipText
    /**  Tool tip text for main panel.   */
    public String mainPanelToolTipText = " ";
    /**  Tool tip text for slider.   */
    public String jsliderToolTipText = " ";
    /**  Tool tip text for text field.   */
    public String jtextfieldToolTipText = " ";
    /**  Title for the bean.   */
    public String title = " ";

    /**
     * Default constructor that initializes nothing and does nothing.
     */
    public PropertyData () { }

    /**
     * Calculates a new slider ratio based on newly supplied parameters.
     * @return A <code>boolean</code> value indicating success of operation.
     */
    public boolean calcNewSliderRatio () {
      if (validFinalMax == validFinalMin)
        return false;
      sliderToValueRatio = (double)(sliderMax - sliderMin) / (validFinalMax - validFinalMin);
      return true;
    }

    /**
     * No parameter method for painting labels for JSlider.  This should be called after #sliderMin and
     * #sliderMax are set.
     */
    public void setLabels () {
      setLabels (sliderMin, sliderMax);
    }

    private void setLabels (int lowerLimit, int upperLimit) {
      if (upperLimit < lowerLimit || lowerLimit < 0)
        return;
      calcNewSliderRatio();
      int labelNumber = (int)((double)upperLimit / sliderToValueRatio);
      String labelString[] = new String [labelNumber];
      for (int i = 0; i < labelNumber; labelString[i] = "" + ++i);
      labelStringArray = labelString;
    }
  }

  /**
   * Sets the properties of this {@link TK_JTextSlider} to fields in {@link PropertyData} instance.
   * Values are set or computed in <code>TK_JTextSlider</code> according to the new values / specifications in
   * <code>PropertyData</code>.
   * @param pd PropertyData instance.
   */
  public void setProperties (PropertyData pd) {
    sliderMax = pd.sliderMax;
    sliderMin = pd.sliderMin;
    sliderMajorTickSpacing = pd.sliderMajorTickSpacing;
    sliderMinorTickSpacing = pd.sliderMinorTickSpacing;
    sliderInitialValue = pd.sliderInitialValue;
    sliderToValueRatio = pd.sliderToValueRatio;
    labelStringArray = pd.labelStringArray;

    // Tool tip text.
    mainPanelTextToolTip = pd.mainPanelToolTipText;
    jsliderToolTipText = pd.jsliderToolTipText;
    jtextfieldTextToolTip = pd.jtextfieldToolTipText;
    title = pd.title;

    removeAllowed = false;
    insertAllowed = false;
    VINJSlider.setMajorTickSpacing(sliderMajorTickSpacing);
    VINJSlider.setMaximum(sliderMax);
    VINJSlider.setMinimum(sliderMin);
    VINJSlider.setMinorTickSpacing(sliderMinorTickSpacing);
    VINJSlider.setToolTipText(jsliderToolTipText);
    insertAllowed = true;
    VINJSlider.setValue(sliderInitialValue);
    removeAllowed = true;

    validFinalMax = pd.validFinalMax;
    validFinalMin = pd.validFinalMin;
    verifyInputDouble.setConstraints(validFinalMin, validFinalMax);

    JSLabelsJPanel.setToolTipText(jsliderToolTipText);
    JSLabelsJPanel.removeAll();  // Remove all previous components from this JPanel
    //System.out.println("[1]");
    addLabel (true, SwingConstants.CENTER, SwingConstants.CENTER, Color.lightGray, labelStringArray);
    JSLabelsJPanel.validate();
    JSLabelsJPanel.updateUI();
    //System.out.println("[2]");

    TopMJPanel.setToolTipText(jtextfieldTextToolTip);
    BottomMJPanel.setToolTipText(jtextfieldTextToolTip);

    VINJTextField.setToolTipText(jtextfieldTextToolTip);

    BridgeVoltageINTitledBorder.setTitle(title);

    this.setToolTipText(mainPanelTextToolTip);
  }

  // ---------------------------------------------------------------------------
  // HELPER METHODS
  //
  //
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // addLabel
  //
  // ---------------------------------------------------------------------------
  private void addLabel (boolean opaque, int hA, int tA, Color cN, String[] lbls) {
    // -------------------------------------------------------------------------
    // GridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight,
    // double weightx, double weighty, int anchor, int fill, Insets insets,
    // int ipadx, int ipady)
    // -------------------------------------------------------------------------
    //this.add(VINJSlider, new GridBagConstraints(0, 0, 12, 1, 1.0, 1.0
    //        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    JSLabelsJPanel.setLayout(new GridBagLayout());
    int labelColumns = 1;
    boolean toggleColor = true;
    int nextPos = 0;
    double stretchAmount = 1.0;
    for (int i = 0; i < lbls.length; i++) {
      JLabel label = new JLabel();
      label.setMinimumSize(new Dimension (0,0));
      label.setOpaque(opaque);
      label.setHorizontalTextPosition(hA);
      label.setBackground(cN);
      label.setText(lbls[i]);

      if ((i == 0) || (i + 1 == lbls.length)) {
        if (i == 0)
          label.setHorizontalAlignment(SwingConstants.LEFT);
        else
          if (i + 1 == lbls.length)
            label.setHorizontalAlignment(SwingConstants.RIGHT);
        if (lbls[i].length() == 1)
          stretchAmount = 0.5;
        else
          if (lbls[i].length() == 2)
            stretchAmount = 0.0;

        labelColumns = 1;
      }
      else {
        if (lbls[i].length() == 1)
          stretchAmount = 1.0;
        else
          if (lbls[i].length() == 2)
            stretchAmount = 0.5;

        label.setHorizontalAlignment(tA);
        labelColumns = 2;
      }

      //System.out.println ("i = " + i + ", nextPos = " + nextPos + ", labelColumns = " + labelColumns);
      JSLabelsJPanel.add(label, new GridBagConstraints (nextPos, 0, labelColumns, 1, stretchAmount, 1.0, GridBagConstraints.CENTER,
          GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));

      nextPos += labelColumns;
    }

    if (sliderToValueRatio != 0.0) {
      int endLabel = sliderMax / (int)sliderToValueRatio;
      if (endLabel < validFinalMax) {
        stretchAmount = validFinalMax - endLabel;
        JSLabelsJPanel.add(new JLabel(), new GridBagConstraints (nextPos, 0, labelColumns, 1, stretchAmount, 1.0, GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
      }
    }
  }

  // ---------------------------------------------------------------------------
  // BridgeVINJSlider_stateChanged(...)  (ActionListener)
  //
  // -------------------------------------------------------------------------
  private void BridgeVINJSlider_stateChanged(ChangeEvent e) {
    if (e.getSource() instanceof JSlider && (JSlider)e.getSource() == VINJSlider && remoteUpdate) {
      int sliderValue;
      double ratio, chosenVoltage;
      formatter.setMaximumFractionDigits(1);
      formatter.setMinimumIntegerDigits (1);

      sliderValue = ((JSlider)e.getSource()).getValue();
      ratio = 1.0 / sliderToValueRatio;
      chosenVoltage = sliderValue * ratio;
      voltage = chosenVoltage;
      if (chosenVoltage < 10) {
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(1);
      }
      VINJTextField.setText ("" + formatter.format(chosenVoltage));
      //System.out.println ("StateChanged(1)");
    }
  }

  // ---------------------------------------------------------------------------
  // textChanged () [inserted or removed]
  //
  // ---------------------------------------------------------------------------
  private void textChanged (DocumentEvent e) {
    double outVoltage = 0.0;
    int sliderVoltageValue = 0;
    try {
      outVoltage = Double.parseDouble(VINJTextField.getText());
    } catch (NumberFormatException nfe) {
      outVoltage = validFinalMin;
    }
    voltage = outVoltage;

    formatter.setMinimumIntegerDigits (1);
    formatter.setMinimumFractionDigits(1);
    if (voltage < 10)
      formatter.setMaximumFractionDigits(2);
    else
      formatter.setMaximumFractionDigits(1);

    sliderVoltageValue = (int)(voltage * sliderToValueRatio + 0.5);
    // NOTE: remoteUpdate does not prevent slider from being set.  Only prevents
    // stateChanged(ChangeEvent e) to be called from any listeners in JSlider.
    remoteUpdate = false;
    VINJSlider.setValue (sliderVoltageValue);
    remoteUpdate = true;

    // Send DataChangedEvent ---------------------------------------------------
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
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
  // setVoltage()
  //
  // ---------------------------------------------------------------------------
  /**
   * Sets the voltage instance variable and updates the GUI.
   * @param v Voltage value to set instance variable with.
   */
  public void setVoltage (double v) {
    if (v < validFinalMin && v > validFinalMax)
      voltage = validFinalMin;
    else
      voltage = v;
    VINJTextField.setText ("" + voltage);
  }

  // ---------------------------------------------------------------------------
  // getVoltage()
  //
  // ---------------------------------------------------------------------------
  /**
   * Retrieves the voltage value from instance voltage variable of this class.
   * @return Voltage instance field value.
   */
  public double getVoltage () {
    return voltage;
  }

  public void setEnabled (boolean enable) {
    VINJTextField.setEnabled(enable);
    VINJSlider.setEnabled(enable);
    JSLabelsJPanel.setEnabled(enable);
    super.setEnabled(enable);
  }
  // ---------------------------------------------------------------------------
  //
  //
  // ---------------------------------------------------------------------------
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}