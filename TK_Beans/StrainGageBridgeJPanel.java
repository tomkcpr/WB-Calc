/*
 * @(#)StrainGageBridgeJPanel.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;

import java.awt.*;
import com.borland.jbcl.layout.*;
import javax.swing.border.*;
import javax.swing.*;
import java.io.Serializable;
import java.awt.event.*;
import java.text.NumberFormat;
import TK_Interfaces.DataChangedListener;
import TK_Classes.DataChangedEvent;
import java.util.Locale;

/**
 * <p><b>Title: StrainGageBridgeJPanel () </b></p>
 * <p><b>Description:</b></p>
 * The parent JPanel for most beans in this Application.  This is the JPanel through which data is entered and sent
 * to {@link DataIntermediate} for storage and future retrieval.  Any data entered through instance components is sent
 * for storage to {@link DataIntermediate} for storage.  All components use an instance of {@link DataIntermediate} through
 * which all input and output to this panels occurs.  All components instantiated are alerted and set to data changes in
 * {@link DataIntermediate} class.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class StrainGageBridgeJPanel extends JPanel implements Serializable {
  private BridgeTypeSelectionJPanel wBGageSelectionJPanel = new BridgeTypeSelectionJPanel();

  private TK_JTextSlider bridgeVoltageIN = new TK_JTextSlider();
  private TK_JTextSlider.PropertyData bridgePropertyData = new TK_JTextSlider.PropertyData();

  private TK_JTextSlider ampVoltageOUT = new TK_JTextSlider();
  private TK_JTextSlider.PropertyData amplifierPropertyData = new TK_JTextSlider.PropertyData();

  private StrainGageProperties gageProperties = new StrainGageProperties();

  private JLabel statusWindowJLabel = new JLabel();
  private Border whiteBorder;
  private GridBagLayout wheatstoneBridgeGridBagLayout = new GridBagLayout();
  private NumberFormat formatter = NumberFormat.getNumberInstance(new Locale ("en", "ca"));
  private DataIntermediate dataIntermediate = new DataIntermediate();
  private Color DARK_GREEN = new Color (0, 125, 0);

  // The following prevent infinite loops in event handling.
  private boolean userModified = true;
  private boolean updateEnabled = true;
  // Prevents DataIntermediate() from reupdating a component that just finished
  // updating DataIntermediate()
  private boolean internalChange = true;
  private boolean valuesLoaded = false;

  public StrainGageBridgeJPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    whiteBorder = BorderFactory.createLineBorder(Color.white,2);
    formatter.setMinimumFractionDigits(1);
    formatter.setMaximumFractionDigits(1);
    formatter.setMinimumIntegerDigits(1);

    // -------------------------------------------------------------------------
    // Bridge Voltage IN JSlider
    //
    //
    // -------------------------------------------------------------------------
    double voltage = checkPower (dataIntermediate.getPower(), (double)dataIntermediate.getBridgeResistance());
    bridgePropertyData.sliderMax = (int)(voltage * 100);
    bridgePropertyData.sliderMin = 100;
    bridgePropertyData.sliderMajorTickSpacing = 100;
    bridgePropertyData.sliderMinorTickSpacing = 20;
    bridgePropertyData.sliderInitialValue = 100;
    bridgePropertyData.validFinalMax = (double)((int)(voltage * 100)) / 100.0;
    bridgePropertyData.validFinalMin = 1.0;
    // 'sliderMax' should be set for this.
    bridgePropertyData.calcNewSliderRatio();
    // Sets labels dynamically.
    bridgePropertyData.setLabels();
    // ToolTipText
    setFormatter (10.0);
    String custBridgeLabel = "(" + formatter.format(bridgePropertyData.validFinalMin) + " - " + formatter.format(bridgePropertyData.validFinalMax) + ")";
    bridgePropertyData.mainPanelToolTipText = "Bridge input voltage selection " + custBridgeLabel;
    bridgePropertyData.jsliderToolTipText = "Select bridge voltage input " + custBridgeLabel;
    bridgePropertyData.jtextfieldToolTipText = "Enter bridge input voltage " + custBridgeLabel;
    bridgePropertyData.title = "Bridge Voltage IN DC.";
    bridgeVoltageIN.setProperties (bridgePropertyData);

    bridgeVoltageIN.addDataChangedListener(new DataChangedListener() {
      public void dataChanged (DataChangedEvent dce) {
        if (/*updateEnabled*/!dataIntermediate.hasMutated())
          dataIntermediate.setBridgeVoltageIN(bridgeVoltageIN.getVoltage());
        if (userModified /* && /*updateEnabled\*\/!dataIntermediate.hasMutated() */) {
          statusWindowJLabel.setForeground(DARK_GREEN);
          statusWindowJLabel.setText("System Bridge voltage is " + formatter.format(dataIntermediate.getVoltageIN()) + ".");
        }
        System.out.println ("[3]");

        userModified = true;
        dataIntermediate.clearMutated();
        //internalChange = true;
        //updateEnabled = true;
        //valuesLoaded = false;
      };
    });

    // -------------------------------------------------------------------------
    // Bridge AMP Voltage OUT JSlider
    //
    //
    // -------------------------------------------------------------------------
    amplifierPropertyData.sliderMax = 500;
    amplifierPropertyData.sliderMin = 100;
    amplifierPropertyData.sliderMajorTickSpacing = 100;
    amplifierPropertyData.sliderMinorTickSpacing = 20;
    amplifierPropertyData.sliderInitialValue = 100;
    amplifierPropertyData.validFinalMax = 5.0;
    amplifierPropertyData.validFinalMin = 1.0;
    // 'sliderMax' should be set for this.
    amplifierPropertyData.calcNewSliderRatio();
    // Sets labels dynamically.
    amplifierPropertyData.setLabels();
    String custAmplifierLabel = "(" + formatter.format(amplifierPropertyData.validFinalMin) + " - " + formatter.format(amplifierPropertyData.validFinalMax) + ")";
    // Tooltip Text
    amplifierPropertyData.mainPanelToolTipText = "Amplifier max voltage output selection " + custAmplifierLabel;
    amplifierPropertyData.jsliderToolTipText = "Select amplifier voltage output " + custAmplifierLabel;
    amplifierPropertyData.jtextfieldToolTipText = "Enter amplifier voltage output " + custAmplifierLabel;
    amplifierPropertyData.title = "Amplifier Max Voltage OUT DC";
    ampVoltageOUT.setProperties (amplifierPropertyData);

    ampVoltageOUT.addDataChangedListener(new DataChangedListener() {
      public void dataChanged (DataChangedEvent dce) {
        if (/*updateEnabled*/!dataIntermediate.hasMutated()) {
          dataIntermediate.setAmplifierVoltageOUT(ampVoltageOUT.getVoltage());
          statusWindowJLabel.setForeground(DARK_GREEN);
          statusWindowJLabel.setText("System AMP voltage is " + formatter.format(dataIntermediate.getAmplifierVoltageOUT()) + ".");
          //internalChange = true;
        }
        dataIntermediate.clearMutated();
        //updateEnabled = true;
        //valuesLoaded = false;
      };
    });

    // -------------------------------------------------------------------------
    // Gage Selection JPanel
    //
    //
    // -------------------------------------------------------------------------
    wBGageSelectionJPanel.setPropertyStrings("2 Gage", "4 Gage", "Select a 2 gage WheatstoneBridge.",
        "Select a 4 gage Wheatstone Bridge.", "Select number of gages in Wheatstone Bridge.", "Gage Selection");
    wBGageSelectionJPanel.setSelectionState(dataIntermediate.getGageNumber());
    wBGageSelectionJPanel.addDataChangedListener(new DataChangedListener() {
      public void dataChanged (DataChangedEvent dce){
        if (/*updateEnabled*/!dataIntermediate.hasMutated()) {
          dataIntermediate.setGageNumber(wBGageSelectionJPanel.getSelectionState());
          statusWindowJLabel.setForeground(DARK_GREEN);
          statusWindowJLabel.setText("System gage number is " + dataIntermediate.getGageNumber() + ".");
          //internalChange = true;
        }
        dataIntermediate.clearMutated();
        //updateEnabled = true;
        //valuesLoaded = false;
      }
    });

    // -------------------------------------------------------------------------
    // Gage Properties JPanel
    //
    // -------------------------------------------------------------------------
    gageProperties.setGageResistance (dataIntermediate.getBridgeResistance());
    gageProperties.setMaxPower(dataIntermediate.getPower());
    gageProperties.setWeight (dataIntermediate.getWeight());
    gageProperties.addDataChangedListener(new DataChangedListener() {
      public void dataChanged (DataChangedEvent dce) {
        if (/*updateEnabled*/!dataIntermediate.hasMutated()) {
          dataIntermediate.setBridgeResistance(gageProperties.getGageResistance());
          dataIntermediate.setPower(gageProperties.getMaxPower());
          dataIntermediate.setWeight(gageProperties.getWeight());
          dataIntermediate.setStepSize(gageProperties.getStepSize());
          dataIntermediate.setPower(gageProperties.getMaxPower());

          double voltage = checkPower (dataIntermediate.getPower(),
                                     (double)dataIntermediate.getBridgeResistance());

          bridgePropertyData.sliderMax = (int)(voltage * 100.0);
          bridgePropertyData.sliderMin = 100;
          bridgePropertyData.sliderInitialValue = (int)(dataIntermediate.getVoltageIN() * 100.0);
          bridgePropertyData.validFinalMax = (double)((int)(voltage * 100)) / 100.0;
          bridgePropertyData.calcNewSliderRatio();
          bridgePropertyData.setLabels();
          setFormatter (10.0);
          String custBridgeLabel = "(" + formatter.format(bridgePropertyData.validFinalMin) + " - " + formatter.format(bridgePropertyData.validFinalMax) + ")";
          bridgePropertyData.mainPanelToolTipText = "Bridge input voltage selection " + custBridgeLabel;
          bridgePropertyData.jsliderToolTipText = "Select bridge voltage input " + custBridgeLabel;
          bridgePropertyData.jtextfieldToolTipText = "Enter bridge input voltage " + custBridgeLabel;

          bridgeVoltageIN.setProperties (bridgePropertyData);

          statusWindowJLabel.setForeground (Color.blue);
          statusWindowJLabel.setText("Max voltage recomputed! Max allowed is " + formatter.format( ((int)(voltage * 100)) * 0.01 ) + ".");
          userModified = false;
          //internalChange = true;
        }
        dataIntermediate.clearMutated();
        //updateEnabled = true;
        //valuesLoaded = false;
      }
    });

    // -------------------------------------------------------------------------
    // Radio buttons JPanel and ButtonGroup
    //
    // -------------------------------------------------------------------------
    statusWindowJLabel.setBackground(Color.lightGray);
    statusWindowJLabel.setBorder(whiteBorder);
    statusWindowJLabel.setOpaque(true);
    statusWindowJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    statusWindowJLabel.setForeground(Color.black);
    statusWindowJLabel.setText("Weight Scale Circuit Properties!");

    // -------------------------------------------------------------------------
    // DataIntermediate
    //
    // -------------------------------------------------------------------------
    dataIntermediate.addDataChangedListener( new DataChangedListener() {
      public void dataChanged(DataChangedEvent dce) {
        //System.out.println ("<1> DataIntermediate() START");
        if (/*!internalChange &&*/dataIntermediate.hasMutated()) {
          //updateEnabled = false;
          //System.out.println ("dataIntermediate.addDataChangedListener() [updateEnabled = " + updateEnabled + "]");
          wBGageSelectionJPanel.setSelectionState(dataIntermediate.getGageNumber());

          gageProperties.setGageResistance (dataIntermediate.getBridgeResistance());
          gageProperties.setMaxPower(dataIntermediate.getPower());
          gageProperties.setWeight (dataIntermediate.getWeight());

          ampVoltageOUT.setVoltage (dataIntermediate.getAmplifierVoltageOUT());
          System.out.println ("[1]");
          bridgeVoltageIN.setVoltage(dataIntermediate.getVoltageIN());
          System.out.println ("[2]");

          statusWindowJLabel.setForeground(Color.blue);
          statusWindowJLabel.setText("Settings loaded from file!");
          //updateEnabled = true;
          //valuesLoaded = true;
          dataIntermediate.clearMutated();
        }
        //System.out.println ("<2> DataIntermediate() END");
      };
    });

    this.setLayout(wheatstoneBridgeGridBagLayout);
    this.add(wBGageSelectionJPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.1
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(gageProperties,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(bridgeVoltageIN,  new GridBagConstraints(0, 2, 1, 1, 1.0, 0.3
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(ampVoltageOUT,  new GridBagConstraints(0, 3, 1, 1, 1.0, 0.3
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(statusWindowJLabel,      new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.setMinimumSize(new Dimension(279, 351));
    // -------------------------------------------------------------------------
  }

  private void setFormatter (double ceiling) {
    if (bridgePropertyData.validFinalMax < ceiling) {
      formatter.setMaximumFractionDigits(2);
    } else {
      formatter.setMaximumFractionDigits(1);
    }
  }
  // ---------------------------------------------------------------------------
  // checkPower (double voltage, double resistance)
  //
  // Returns correct voltage if power exceeded.
  // power ( in milliWatts)
  // resistance ( in Ohms)
  // ---------------------------------------------------------------------------
  private double checkPower (int power, double resistance) {
    double rMax = resistance * 1.025;
    double voltage = Math.sqrt(power * 0.001 * rMax) * (rMax + resistance) / rMax;

    if (voltage > 12.0) {
      return 12.0;
    } else
      return voltage;
  }

  // ---------------------------------------------------------------------------
  // Adds DataIntermediate
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Custom sets the current inner DataIntermediate object from the calling environment.
   * @param di A new object of DataIntermediate
   */
  public void setDataIntermediate (DataIntermediate di) {
    dataIntermediate = di;
  }

  // ---------------------------------------------------------------------------
  // Retrieves DataIntermediate
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Returns a reference to the inner DataIntermediate object.  Once returned, actions can be performed on
   * the DataIntermediate object which will have a direct effect on the GUI state of this class.
   * @return The inner <code>DataIntermediate</code> object.
   */
  public DataIntermediate getDataIntermediate () {
    return dataIntermediate;
  }
}