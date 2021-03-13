/*
 * @(#)DataArbitrate.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;
import java.awt.AWTEvent;
import TK_Classes.DataChangedEvent;
import javax.swing.event.EventListenerList;
import TK_Interfaces.DataChangedListener;
import java.util.EventListener;
import javax.swing.*;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.*;

/**
 * <p><b>Title: DataArbitrate () </b></p>
 * <p><b>Description:</b></p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * Default data class of RTDBridgeJPanel.  Methods in this class include getters and setters for storing
 * and moving data.
 * <p><b>Company: NONE </b></p>
 * @version Project B (V1.0)
 * @author Tom Kacperski
 */

public class DataArbitrate extends JPanel implements Serializable {
  // TAGS
  private int TWO = 2;
  private int FOUR = 4;

  // VARIABLES set to defaults.
  private int RTDNumber = TWO;
  private double bridgeVoltageInput = 1;
  private int R1 = 100;
  private int R2 = 100;
  private int R3 = 100;
  private int R4 = 100;
  private double amplifierVoltageOutput=1;
  private int power = 500;
  private int stepSize = 5;

  private double temperatureCoefficient = 0.004;
  private int tLow = 0;
  private int tHigh = 100;
  private boolean mutated = false;

  private double MAX_BRIDGE_VOLTAGE_INPUT = 10;
  private double MAX_AMPLIFIER_VOLTAGE_OUT = 10;
  private int MAX_HIGH_TEMPERATURE = 100;
  private int MAX_LOW_TEMPERATURE = 0;
  private int MAX_POWER = 10000;

  private double MIN_TEMPERATURE_COEFFICIENT = 0.0001;
  private double MAX_TEMPERATURE_COEFFICIENT = 1.0;
  private int MAX_STEPSIZE = 10;
  private int MIN_STEPSIZE = 40;

  // EVENT HANDLING
  EventListenerList eventListenerList = new EventListenerList();

  // ---------------------------------------------------------------------------
  // Constructors
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Constructs a default <code>DataArbitrate()</code> with default values.
   */
  public DataArbitrate () {}

  // ---------------------------------------------------------------------------
  // MUTATORS
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Sets all circuit parameters of this circuit in one method.
   * @param RTDs Number of RTD's in bridge.
   * @param bridgeVIn Bridge voltage input.
   * @param RTDResistance RTD initial resistance.
   * @param ampVOut (Disabled) Amplifier output voltage.
   * @param p Max power dissipation of this circuit.
   * @param tC Temperature coefficient (alpha) of this circuit.
   * @param tL Temperature low of circuit.
   * @param tH Temperature high of circuit.
   * @param sS Plot points of circuit.
   */
  public void setCircuitParameters (int RTDs, double bridgeVIn, int RTDResistance, double ampVOut, int p,
                                    double tC, int tL, int tH, int sS) {
    RTDNumber = (RTDs == TWO || RTDs == FOUR) ? RTDs : TWO;
    bridgeVoltageInput = (bridgeVIn >= 1 && bridgeVIn <= MAX_BRIDGE_VOLTAGE_INPUT) ? bridgeVIn : 1;
    R1 = R2 = R3 = R4 = (RTDResistance >= 100 && RTDResistance <= 2000) ? RTDResistance : 100;
    amplifierVoltageOutput = (ampVOut >= 1 && ampVOut <= MAX_BRIDGE_VOLTAGE_INPUT) ? ampVOut : 1;
    power = (p >= 0 && p <= MAX_POWER) ? p : MAX_POWER;
    stepSize = (sS >= MIN_STEPSIZE && sS <= MAX_STEPSIZE) ? sS : 10;

    temperatureCoefficient = (tC >= MIN_TEMPERATURE_COEFFICIENT && tC <= MAX_TEMPERATURE_COEFFICIENT) ? tC : 0.004;
    tLow = (tL >= MAX_LOW_TEMPERATURE && tL < tH) ? tL : MAX_LOW_TEMPERATURE;
    tHigh = (tH >= tL && tH <= MAX_HIGH_TEMPERATURE) ? tH : MAX_HIGH_TEMPERATURE;

    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets low temperature value.
   * @param tL Temperature Low.
   */
  public void setLowTemperature (int tL) {
    tLow = (tL >= MAX_LOW_TEMPERATURE && tL < tHigh) ? tL : MAX_LOW_TEMPERATURE;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets high temperature value.
   * @param tH Temperature High.
   */
  public void setHighTemperature (int tH) {
    tHigh = (tH >= tLow && tH <= MAX_HIGH_TEMPERATURE) ? tH : MAX_HIGH_TEMPERATURE;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets temperature coefficient (alpha) value.
   * @param tC Temperature Coefficient (alpha).
   */
  public void setTemperatureCoefficient (double tC) {
    temperatureCoefficient = (tC >= MIN_TEMPERATURE_COEFFICIENT && tC <= MAX_TEMPERATURE_COEFFICIENT) ? tC : 0.004;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets RTD number to use value.
   * @param RTDs RTDs in this bridge.
   */
  public void setRTDNumber (int RTDs) {
    RTDNumber = (RTDs == TWO || RTDs == FOUR) ? RTDs : TWO;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets initial bridge resistance.
   * @param RTDResistance Initial bridge resistance.
   */
  public void setBridgeResistance (int RTDResistance) {
    R1 = R2 = R3 = R4 = (RTDResistance >= 100 && RTDResistance <= 2000) ? RTDResistance : 100;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets bridge R1 resistance.
   * @param r1 Initial R1 resistance.
   */
  public void setBridgeR1 (int r1) {
    R1 = (r1 >= 100 && r1 <= 2000) ? r1 : 100;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets bridge R2 resistance.
   * @param r2 Initial R2 resistance.
   */
  public void setBridgeR2 (int r2) {
    R2 = (r2 >= 100 && r2 <= 2000) ? r2 : 100;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets bridge R3 resistance.
   * @param r3 Initial R3 resistance.
   */
  public void setBridgeR3 (int r3) {
    R3 = (r3 >= 100 && r3 <= 2000) ? r3 : 100;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets bridge R4 resistance.
   * @param r4 Initial R4 resistance.
   */
  public void setBridgeR4 (int r4) {
    R4 = (r4 >= 100 && r4 <= 2000) ? r4 : 100;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets bridge voltage input value.
   * @param bridgeVIn Bridge voltage in.
   */
  public void setBridgeVoltageIN (double bridgeVIn) {
    bridgeVoltageInput = (bridgeVIn >= 1 && bridgeVIn <= MAX_BRIDGE_VOLTAGE_INPUT) ? bridgeVIn : 1;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets amplifier voltage output value.
   * @param ampVOut Amplifier output voltage.
   */
  public void setAmplifierVoltageOUT (double ampVOut) {
    amplifierVoltageOutput = (ampVOut >= 1 && ampVOut <= MAX_BRIDGE_VOLTAGE_INPUT) ? ampVOut : 1;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets maximum power dissipation of this circuit.
   * @param p Max power dissipation.
   */
  public void setPower (int p) {
    power = (p >= 0 && p <= MAX_POWER) ? p : MAX_POWER;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets plot points to use for computing and graphing.
   * @param sS Plot points setting to use.
   */
  public void setStepSize (int sS) {
    stepSize = sS;
    mutated = true;

    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * None parameter method for clearing mutation flag.
   */
  public void clearMutated () {
    mutated = false;
  }

  // ---------------------------------------------------------------------------
  // ACCESSORS
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Check internal boolean value whether the data has been recently changed.
   * @return Mutated status (true or false).
   */
  public boolean hasMutated () {
    return mutated;
  }

  /**
   * Returns current low temperature.
   * @return Low temperature setting.
   */
  public int getTemperatureLow () {
    return tLow;
  }

  /**
   * Returns current high temperature.
   * @return High temperature setting.
   */
  public int getTemperatureHigh () {
    return tHigh;
  }

  /**
   * Returns current temperature coefficient.
   * @return Temperature Coefficient (alpha)
   */
  public double getTemperatureCoefficient () {
    return temperatureCoefficient;
  }

  /**
   * Returns current RTD number.
   * @return RTD's (Bridge type)
   */
  public int getRTDNumber () {
    return RTDNumber;
  }

  /**
   * Returns current bridge input voltage.
   * @return Bridge input voltage.
   */
  public double getBridgeVoltageIN () {
    return bridgeVoltageInput;
  }

  /**
   * Returns current amplifier output voltage. (Disabled)
   * @return Amplifier output voltage. (Disabled)
   */
  public double getAmplifierVoltageOUT () {
    return amplifierVoltageOutput;
  }

  /**
   * Returns current initial bridge resistance.
   * @return Initial bridge resistance.
   */
  public int getBridgeResistance () {
    // Initial resistance of resistors is equal, so R1 = R2 = R3 = R4.  Therefore,
    // only one is returned.
    return R1;
  }

  /**
   * Returns current R1 value.
   * @return R1 value.
   */
  public int getBridgeR1 () {
    return R1;
  }

  /**
   * Returns current R2 value.
   * @return R2 value.
   */
  public int getBridgeR2 () {
    return R2;
  }

  /**
   * Returns current R3 value.
   * @return R3 value.
   */
  public int getBridgeR3 () {
    return R3;
  }

  /**
   * Returns current R4 value.
   * @return R4 value.
   */
  public int getBridgeR4 () {
    return R4;
  }

  /**
   * Returns current maximum power dissipation entered. (Disabled)
   * @return Max power dissipation.
   */
  public int getPower () {
    return power;
  }

  /**
   * Returns current plot points selected.
   * @return Plot points currently selected.
   */
  public int getStepSize () {
    return stepSize;
  }

  // ---------------------------------------------------------------------------
  // MAX VALUES
  //
  //
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // ACCESSORS
  // ---------------------------------------------------------------------------
  /**
   * Returns ceiling bridge voltage in.
   * @return Ceiling voltage in.
   */
  public double getMAX_VOLTAGE_INPUT () {
    return MAX_BRIDGE_VOLTAGE_INPUT;
  }

  /**
   * Returns ceiling amplifier voltage out.
   * @return Ceiling amp voltage out.
   */
  public double getMAX_AMPLIFIER_VOLTAGE_OUT () {
    return MAX_AMPLIFIER_VOLTAGE_OUT;
  }

  /**
   * Returns ceiling power dissipation.
   * @return Ceiling power dissipation.
   */
  public int getMAX_POWER () {
    return MAX_POWER;
  }

  /**
   * Returns ceiling high temperature.
   * @return Maximum high temperature.
   */
  public int getMAX_HIGH_TEMPERATURE () {
    return MAX_HIGH_TEMPERATURE;
  }

  /**
   * Returns floor low temperature.
   * @return Returns lowers temperature entry allowed.
   */
  public int getMAX_LOW_TEMPERATURE () {
    return MAX_LOW_TEMPERATURE;
  }

  /**
   * Returns lowest temperature coefficient allowed.
   * @return Lowest temperature coefficient allowed.
   */
  public double getMIN_TEMPERATURE_COEFFICIENT () {
    return MIN_TEMPERATURE_COEFFICIENT;
  }

  /**
   * Returns Max temperature coefficient allowed.
   * @return Max temperature coefficient allowed.
   */
  public double getMAX_TEMPERATURE_COEFFICIENT () {
    return MAX_TEMPERATURE_COEFFICIENT;
  }

  /**
   * Returns highest plot points value allowed.
   * @return Highest plot points value.
   */
  public double getMAX_STEPSIZE () {
    return MAX_STEPSIZE;
  }

  /**
   * Returns lowest plot points value allowed.
   * @return Lowest plot points value allowed.
   */
  public double getMIN_STEPSIZE () {
    return MIN_STEPSIZE;
  }
  // ---------------------------------------------------------------------------
  // MUTATORS
  // ---------------------------------------------------------------------------
  /**
   * Sets highest max bridge voltage allowed.
   * @param MBV Highest bridge voltage allowed.
   */
  public void setMAX_BRIDGE_VOLTAGE_INPUT (double MBV) {
    MAX_BRIDGE_VOLTAGE_INPUT = MBV;
  }

  /**
   * Sets highest amp voltage allowed.
   * @param MAV highest amp voltage allowed.
   */
  public void setMAX_AMPLIFIER_VOLTAGE_OUT (int MAV) {
    MAX_AMPLIFIER_VOLTAGE_OUT  = MAV;
  }

  /**
   * Sets highest power specification allowed.
   * @param MP Highest power specification allowed.
   */
  public void setMAX_POWER (int MP) {
    MAX_POWER = MP;
  }

  /**
   * Sets max high temperature specification allowed.
   * @param MHT Highest temperature specification allowed.
   */
  public void setMAX_HIGH_TEMPERATURE (int MHT) {
    MAX_HIGH_TEMPERATURE = MHT;
  }

  /**
   * Sets lowest low temperature specification allowed.
   * @param MHT Lowest temperature specification allowed.
   */
  public void setMAX_LOW_TEMPERATURE (int MLT) {
    MAX_LOW_TEMPERATURE = MLT;
  }

  /**
   * Sets lowest temperature coefficient allowed.
   * @param MIN_TC Lowest alpha allowed.
   */
  public void setMIN_TEMPERATURE_COEFFICIENT (double MIN_TC) {
    MIN_TEMPERATURE_COEFFICIENT = MIN_TC;
  }

  /**
   * Sets highest temperature coefficient allowed.
   * @param MAX_TC Highest alpha allowed.
   */
  public void setMAX_TEMPERATURE_COEFFICIENT (double MAX_TC) {
    MAX_TEMPERATURE_COEFFICIENT = MAX_TC;
  }

  /**
   * Sets highest step size allowed.
   * @param MAX_S Highest step size allowed.
   */
  public void setMAX_STEPSIZE (int MAX_S) {
    MAX_STEPSIZE = MAX_S;
  }

  /**
   * Sets lowest step size allowed.
   * @param MIN_S Lowest step size allowed.
   */
  public void setMIN_STEPSIZE (int MIN_S) {
    MIN_STEPSIZE = MIN_S;
  }
  // ---------------------------------------------------------------------------
  // CUSTOM EVENTS
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
  public void removeDataChangedListener (DataChangedListener dcl) {
    eventListenerList.remove(DataChangedListener.class, dcl);
  }

  /**
   * Method for processing events whenever AWT removes events from the system queue.  Whenever
   * a {@link DataChangedEvent} occurs on this class, listeners are queried for instances of
   * {@link DataChangedListener} on which <code>dataChanged({@link DataChangedEvent} dce)</code>
   * are called.
   * @param dce A data changed event removed from stack during execution.
   */
  public void processEvent (AWTEvent dce) {
    if (dce instanceof DataChangedEvent) {
      EventListener[] listenerList = eventListenerList.getListeners(DataChangedListener.class);
      for (int i = 0; i < listenerList.length; i++) {
        ((DataChangedListener)listenerList[i]).dataChanged((DataChangedEvent)dce);
      }
    } else {
      super.processEvent(dce);
    }
  }
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}