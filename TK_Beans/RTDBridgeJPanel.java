/*
 * @(#)RTDBridgeJPanel.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;
/**
 * <p><b>Title: RTDBridgeJPanel </b></p>
 * <p><b>Description:</b></p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * RTDBridgeJPanel for entering RTD circuit specific information.  Panel contains several components for entering
 * RTD circuit specific information.  Input verification is used to determine the validity of each value before
 * calculations are allowed to take place.
 * <p><b>Company: NONE </b></p>
 * @version Project B (V1.0)
 * @author Tom Kacperski
 */

import java.awt.*;
import com.borland.jbcl.layout.*;
import javax.swing.*;
import javax.swing.border.*;
import TK_Beans.RTDPropertiesJPanel;
import TK_Interfaces.DataChangedListener;
import TK_Classes.DataChangedEvent;
import TK_Interfaces.DataTriggerListener;
import TK_Classes.DataTriggerEvent;
import java.text.*;
import java.util.Locale;
import java.io.*;

public class RTDBridgeJPanel extends JPanel implements Serializable {
  private BridgeTypeSelectionJPanel bridgeTypeSelectionJPanel = new BridgeTypeSelectionJPanel();
  private RTDPropertiesJPanel rtdProperties = new RTDPropertiesJPanel();

  private TK_JTextSlider bridgeVoltageTK_JTextSlider = new TK_JTextSlider();
  private TK_JTextSlider.PropertyData bridgePropertyData = new TK_JTextSlider.PropertyData();

  private TK_JTextSlider ampVoltageTK_JTextSlider = new TK_JTextSlider();
  private TK_JTextSlider.PropertyData ampPropertyData = new TK_JTextSlider.PropertyData();

  private JLabel statusBoxJLabel = new JLabel();
  private Border border1;
  private GridBagLayout rtdBridgeGridBagLayout = new GridBagLayout();
  private DataArbitrate dataArbitrate = new DataArbitrate();
  private NumberFormat formatter = NumberFormat.getNumberInstance (new Locale ("en", "ca"));
  private Color DARK_GREEN = new Color (0, 125, 0);
  private boolean userModified = true;

  public RTDBridgeJPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    // Formatter initialization
    formatter.setMinimumFractionDigits(1);
    formatter.setMaximumFractionDigits(1);
    formatter.setMinimumIntegerDigits(1);

    // Status Box
    border1 = BorderFactory.createLineBorder(Color.white,1);
    statusBoxJLabel.setBackground(Color.lightGray);
    statusBoxJLabel.setBorder(border1);
    statusBoxJLabel.setOpaque(true);
    statusBoxJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    statusBoxJLabel.setText("RTD Bridge Status Window!");

    // Bridge Type Selection JPanel
    bridgeTypeSelectionJPanel.setPropertyStrings("2 RTD", "4 RTD", "Select a 2 RTD bridge.", "Select a 4 RTD bridge.",
        "Select number of RTD's for this bridge.", "RTD Selection");
    bridgeTypeSelectionJPanel.setSelectionState(dataArbitrate.getRTDNumber());
    bridgeTypeSelectionJPanel.addDataChangedListener(new DataChangedListener () {
      public void dataChanged (DataChangedEvent dce) {
        if (!dataArbitrate.hasMutated()) {
          dataArbitrate.setRTDNumber(bridgeTypeSelectionJPanel.getSelectionState());
          statusBoxJLabel.setForeground(DARK_GREEN);
          statusBoxJLabel.setText("RTD number is " + formatter.format(dataArbitrate.getRTDNumber()) + ".");
        }
        dataArbitrate.clearMutated();
      };
    });

    // RTD Properties
    rtdProperties.setAlpha(dataArbitrate.getTemperatureCoefficient());
    rtdProperties.setPower(dataArbitrate.getPower());
    rtdProperties.setTHigh(dataArbitrate.getTemperatureHigh());
    rtdProperties.setTLow(dataArbitrate.getTemperatureLow());
    rtdProperties.setGraphPoints(dataArbitrate.getStepSize());
    rtdProperties.setResistor(dataArbitrate.getBridgeResistance());
    rtdProperties.addDataChangedListener(new DataChangedListener () {
      public void dataChanged (DataChangedEvent dce) {
        if (!dataArbitrate.hasMutated()) {
          dataArbitrate.setHighTemperature(rtdProperties.getTHigh());
          dataArbitrate.setStepSize(rtdProperties.getGraphPoints());

          statusBoxJLabel.setForeground(DARK_GREEN);
          statusBoxJLabel.setText(rtdProperties.getChangeString());
        }
        dataArbitrate.clearMutated();
      }
    });
    rtdProperties.addTriggerEventListener(new DataTriggerListener() {
      public void triggerReceived (DataTriggerEvent dte) {
        if (!dataArbitrate.hasMutated()) {
          dataArbitrate.setLowTemperature(rtdProperties.getTLow());
          dataArbitrate.setHighTemperature(rtdProperties.getTHigh());
          dataArbitrate.setTemperatureCoefficient(rtdProperties.getAlpha());
          dataArbitrate.setBridgeResistance(rtdProperties.getResistor());
          dataArbitrate.setPower(rtdProperties.getPower());

          // Reset bridge slider
          bridgePropertyData.validFinalMax = (double)((int)(highestVoltage(10.0, 1.0) * 100) / 100.0); // double
          bridgePropertyData.sliderMax = (int)(bridgePropertyData.validFinalMax * 100); // int
          bridgePropertyData.sliderInitialValue = (int)(dataArbitrate.getBridgeVoltageIN() * 100.0); // int

          bridgePropertyData.calcNewSliderRatio(); // Sets ampPropertyData.sliderToValueRatio (double)
          bridgePropertyData.setLabels(); // Sets ampPropertyData.labelStringArray = ; // String

          setFormatter (10.0, bridgePropertyData.validFinalMin);
          String custBridgeLabel = "(" + formatter.format(bridgePropertyData.validFinalMin) + " - ";
          setFormatter (10.0, bridgePropertyData.validFinalMax);
          custBridgeLabel += formatter.format(bridgePropertyData.validFinalMax) + ")";
          bridgePropertyData.mainPanelToolTipText = "Enter bridge input voltage for RTD circuit. " + custBridgeLabel; // String
          bridgePropertyData.jsliderToolTipText = "Select bridge input voltage for RTD circuit. " + custBridgeLabel; // String
          bridgePropertyData.jtextfieldToolTipText = "Type bridge input voltage for RTD circuit. " + custBridgeLabel; // String
          bridgeVoltageTK_JTextSlider.setProperties(bridgePropertyData);

          statusBoxJLabel.setForeground(Color.blue);
          statusBoxJLabel.setText(rtdProperties.getChangeString());
          userModified = false;
        }
        dataArbitrate.clearMutated();
      }
    });

    // Bridge Voltage IN
    bridgePropertyData.validFinalMax = (double)((int)(highestVoltage(10.0, 1.0) * 100) / 100.0); // double
    bridgePropertyData.validFinalMin = 1.0; // double
    bridgePropertyData.sliderMax = (int)(bridgePropertyData.validFinalMax * 100); // int
    bridgePropertyData.sliderMin = 100; // int
    bridgePropertyData.sliderMajorTickSpacing = 100; // int
    bridgePropertyData.sliderMinorTickSpacing = 20; // int
    bridgePropertyData.sliderInitialValue = 100; // int
    bridgePropertyData.calcNewSliderRatio(); // Sets ampPropertyData.sliderToValueRatio (double)
    bridgePropertyData.setLabels(); // Sets ampPropertyData.labelStringArray = ; // String

    setFormatter (10.0, bridgePropertyData.validFinalMin);
    String custBridgeLabel = "(" + formatter.format(bridgePropertyData.validFinalMin) + " - ";
    setFormatter (10.0, bridgePropertyData.validFinalMax);
    custBridgeLabel += formatter.format(bridgePropertyData.validFinalMax) + ")";
    bridgePropertyData.mainPanelToolTipText = "Enter bridge input voltage for RTD circuit. " + custBridgeLabel; // String
    bridgePropertyData.jsliderToolTipText = "Select bridge input voltage for RTD circuit. " + custBridgeLabel; // String
    bridgePropertyData.jtextfieldToolTipText = "Type bridge input voltage for RTD circuit. " + custBridgeLabel; // String
    bridgePropertyData.title = "Bridge Voltage IN"; // String
    bridgeVoltageTK_JTextSlider.setProperties(bridgePropertyData);
    bridgeVoltageTK_JTextSlider.addDataChangedListener(new DataChangedListener () {
      public void dataChanged (DataChangedEvent dce) {
        if (!dataArbitrate.hasMutated())
          dataArbitrate.setBridgeVoltageIN(bridgeVoltageTK_JTextSlider.getVoltage());
        if (userModified) {
          statusBoxJLabel.setForeground(DARK_GREEN);
          setFormatter (dataArbitrate.getBridgeVoltageIN(), 10.0);
          statusBoxJLabel.setText("Bridge Voltage: " + formatter.format (dataArbitrate.getBridgeVoltageIN()));
        }
        userModified = true;
        dataArbitrate.clearMutated();
      }
    });

    // Amplifier Voltage OUT
    ampPropertyData.sliderMax = 1000; // int
    ampPropertyData.sliderMin = 100; // int
    ampPropertyData.sliderMajorTickSpacing = 100; // int
    ampPropertyData.sliderMinorTickSpacing = 20; // int
    ampPropertyData.sliderInitialValue = 100; // int
    ampPropertyData.validFinalMax = 10.0; // double
    ampPropertyData.validFinalMin = 1; // double
    ampPropertyData.calcNewSliderRatio(); // Sets ampPropertyData.sliderToValueRatio (double)
    ampPropertyData.setLabels(); // Sets ampPropertyData.labelStringArray = ; // String

    setFormatter (10.0, ampPropertyData.validFinalMin);
    String custAmplifierLabel = "(" + formatter.format(ampPropertyData.validFinalMin) + " - ";
    setFormatter (10.0, ampPropertyData.validFinalMax);
    custAmplifierLabel += formatter.format(ampPropertyData.validFinalMax) + ")";
    ampPropertyData.mainPanelToolTipText = "Enter an amplifier output voltage for RTD circuit. " + custAmplifierLabel; // String

    ampPropertyData.jsliderToolTipText = "Select an amplifier output voltage for RTD circuit. " + custAmplifierLabel; // String
    ampPropertyData.jtextfieldToolTipText = "Type an amplifier output voltage for RTD circuit. " + custAmplifierLabel; // String
    ampPropertyData.title = "AMP Voltage OUT"; // String
    ampVoltageTK_JTextSlider.setProperties(ampPropertyData);
    ampVoltageTK_JTextSlider.addDataChangedListener(new DataChangedListener() {
      public void dataChanged (DataChangedEvent dce) {
        if (!dataArbitrate.hasMutated()) {
          dataArbitrate.setAmplifierVoltageOUT(ampVoltageTK_JTextSlider.getVoltage());

          statusBoxJLabel.setForeground(DARK_GREEN);
          setFormatter (dataArbitrate.getAmplifierVoltageOUT(), 10.0);
          statusBoxJLabel.setText("Amp Voltage: " + formatter.format (dataArbitrate.getAmplifierVoltageOUT()));
        }
        dataArbitrate.clearMutated();
      }
    });
    //ampVoltageTK_JTextSlider.setEnabled(false);

    // Data Arbitrate Listener
    dataArbitrate.addDataChangedListener(new DataChangedListener () {
      public void dataChanged (DataChangedEvent dce) {
        if (dataArbitrate.hasMutated()) {
          bridgeTypeSelectionJPanel.setSelectionState(dataArbitrate.getRTDNumber());
          rtdProperties.setResistor(dataArbitrate.getBridgeResistance());
          rtdProperties.setTHigh(dataArbitrate.getTemperatureHigh());
          rtdProperties.setTLow(dataArbitrate.getTemperatureLow());
          rtdProperties.setGraphPoints(dataArbitrate.getStepSize());
          rtdProperties.setAlpha(dataArbitrate.getTemperatureCoefficient());
          rtdProperties.setPower(dataArbitrate.getPower());
          bridgeVoltageTK_JTextSlider.setVoltage(dataArbitrate.getBridgeVoltageIN());
          ampVoltageTK_JTextSlider.setVoltage(dataArbitrate.getAmplifierVoltageOUT());

          dataArbitrate.clearMutated();
        }
      }
    });

    this.setLayout(rtdBridgeGridBagLayout);
    this.add(bridgeTypeSelectionJPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(rtdProperties,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(bridgeVoltageTK_JTextSlider,  new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    //this.add(ampVoltageTK_JTextSlider,  new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
    //        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(statusBoxJLabel,  new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }

  /**
   * Returns the dataArbitrate object, the default data class of this RTD panel.
   * @return Inner DataArbitrate object.
   */
  public DataArbitrate getDataArbitrate () {
    return dataArbitrate;
  }

  /**
   * Sets the DataArbitrate object of this panel to one specified from calling environment.
   * @param da DataArbitrate object to set corresponding inner class with.
   */
  public void setDataArbitrate (DataArbitrate da) {
    dataArbitrate = da;
  }

  private double highestvoltage () {
    return highestVoltage (10.0, 1.0);
  }

  private double highestVoltage (double Vmax, double Vmin) {
    return highestVoltage (dataArbitrate.getRTDNumber(), dataArbitrate.getPower(), dataArbitrate.getBridgeResistance(),
                           dataArbitrate.getTemperatureCoefficient(), dataArbitrate.getTemperatureLow(), Vmax, Vmin);
  }

  private double highestVoltage (int gage, int power, int Ro, double alpha, int tLow, double Vmax, double Vmin) {
    double Rd = Ro * (1 + alpha * (tLow - 25));
    double Ir = 0.0;
    double Rt = 1 / (1.0 / ((double)Ro * 2) + 1.0 / (Rd + (double)Ro));
    double Ia = 0.0;
    double Ib = 0.0;
    double It = 0.0;
    double Vt = 0.0;
    if (gage == 2) {      // 2 Gage
      Ir = Math.sqrt (((double)power / 1000.0) / (double)Ro);
      if (Ro >= Rd) { // Greater current over Ro + Rd
        Ia = Ir;
        Ib =  (Ro + Rd) / (Ro * 2) * Ia;
      } else  // Greater current over Ro + Ro
        if (Ro < Rd) {
          Ib = Ir;
          Ia = (Ro * 2) / (Ro + Rd) * Ib;
        }
      It = Ia + Ib;
      Vt = It * Rt;
      // System.out.println ("[0]Vt = " + Vt + ", Rt = " + Rt + ", It = " + It + ", Ia = " + Ia + ", Ib = " + Ib);
      if (Vt < Vmin)
        return Vmin;
      return (Vt < Vmax) ? Vt : Vmax;
    } else
      if (gage == 4) {      // 4 Gage
        Ir = Math.sqrt (power / Rd);
        Vt = Ir * (Ro + Rd);
        // System.out.println ("[1]Vt = " + Vt + ", Rt = " + Rt + ", It = " + It + ", Ia = " + Ia + ", Ib = " + Ib);
        if (Vt < Vmin)
          return Vmin;
        return (Vt < Vmax) ? Vt : Vmax;
      }
      // System.out.println ("[2]Vt = " + Vt + ", Rt = " + Rt + ", It = " + It + ", Ia = " + Ia + ", Ib = " + Ib);
    return Vmax;
  }

  private void setFormatter (double changePoint, double against) {
    if (against < changePoint) {
      formatter.setMaximumFractionDigits(2);
    } else {
      formatter.setMaximumFractionDigits(1);
    }
  }

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }

  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}