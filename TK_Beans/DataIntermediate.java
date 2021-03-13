/*
 * @(#)DataIntermediate.java
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
 * <p><b>Title: DataIntermediate () </b></p>
 * <p><b>Description:</b></p>
 * <p>This is the default data class for {@link StrainGageBridgeJPanel}.  Any changes to the contents of this class
 * will result in GUI updates.  Any changes to the GUI in {@link WheatstoneBridgeJPanel} will results in updates to the
 * inner instance of this class.  This class contains a {@link DataChangedListener} that acts on <code>events</code> of
 * {@link DataChangedEvent}.  Modifications of data in this class will result in <code>DataChangedEvent</code>'s being
 * fired.  Listeners can be added using <code>addDataChangedListener ({@link DataChangedListener} dcl)</code> to detect data
 * changes within this class.
 *
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class DataIntermediate extends JPanel implements Serializable {
  private int gageNumber = 2;
  private double bridgeVoltageInput = 1;
  private int R1 = 120;
  private int R2 = 120;
  private int R3 = 120;
  private int R4 = 120;
  private double amplifierVoltageOutput=1;
  private int weight = 100;
  private int power = 50;
  private int stepSize = 10;

  private double MAX_BRIDGE_VOLTAGE_INPUT = 12;
  private double MAX_AMPLIFIER_VOLTAGE_OUT = 5;
  private int MAX_WEIGHT = 10000;
  private int MAX_POWER = 5000;
  private boolean mutated = false;

  // TAGS
  private int TWO = 2;
  private int FOUR = 4;

  // EVENT HANDLING
  EventListenerList eventListenerList = new EventListenerList();

  // ---------------------------------------------------------------------------
  // Constructors
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Constructs a default <code>DataIntermediate()</code> with default values.
   */
  public DataIntermediate () {}

  // ---------------------------------------------------------------------------
  // MUTATORS
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Sets all parameters in this class to custom values.
   * @param gages Number of strain gages in this circuit.
   * @param bridgeVIn Voltage input for this circuit.
   * @param gageResistance Strain gage resistance for this WheatstoneBridge (All resistors).
   * @param ampVOut Desired maximum amplifier voltage output.
   * @param w The weight applied on the strain gage(s).
   * @param p Maximum power dissipation allowed for gage(s).
   * @param sS Step size to use.
   */
  public void setCircuitParameters (int gages, int gageResistance, double bridgeVIn, double ampVOut, int w, int p, int sS) {
    gageNumber = (gages == TWO || gages == FOUR) ? gages : TWO;
    bridgeVoltageInput = (bridgeVIn >= 0 && bridgeVIn <= MAX_BRIDGE_VOLTAGE_INPUT) ? bridgeVIn : 1;
    R1 = R2 = R3 = R4 = (gageResistance >= 120) ? gageResistance : 120;
    amplifierVoltageOutput = (ampVOut >= 0 && ampVOut <= MAX_AMPLIFIER_VOLTAGE_OUT) ? ampVOut : 1;
    weight = (w >= 0 && w <= MAX_WEIGHT) ? w : 100;
    power = (p >= 0 && p <= MAX_POWER) ? p : 50;
    stepSize = (sS >= 1 && sS < weight) ? sS : 10;
    mutated = true;

    // Event fired when data changes.
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets number of gages in circuit.
   * @param gages The number of gages in the circuit.
   */
  public void setGageNumber (int gages) {
    gageNumber = (gages == TWO || gages == FOUR) ? gages : TWO;
    mutated = true;

    // Event fired when data changes.
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets desired amplifier output voltage.  Range is between 0 - <code>ampOUT</code>
   * @param ampVOut Desired max amplifier output voltage.
   */
  public void setAmplifierVoltageOUT (double ampVOut) {
    amplifierVoltageOutput = (ampVOut >= 0 && ampVOut <= MAX_AMPLIFIER_VOLTAGE_OUT) ? ampVOut : 1;
    mutated = true;

    // Event fired when data changes.
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets input voltage for the WheatstoneBridge.  Voltage is such not to exceed maximum power allowed.
   * @see #setPower
   * @param vi Input voltage to be used.
   */
  public void setBridgeVoltageIN (double vi) {
    bridgeVoltageInput = vi;
    mutated = true;

    // Event fired when data changes.
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets the gage resistance for all gages in WheatstoneBridge.
   * @param gageResistance The resistance to use (120, 350, 1000)
   */
  public void setBridgeResistance (int gageResistance) {
    R1 = R2 = R3 = R4 = (gageResistance >= 120) ? gageResistance : 120;
    mutated = true;

    // Event fired when data changes.
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets bridge resistance for R1 resistor.
   * @param R1 Ohm rating of R1.
   */
  public void setBridgeR1 (int R1) {
    this.R1 = (R1 >= 120) ? R1 : 120;
    mutated = true;

    // Event fired when data changes.
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets bridge resistance for R2 resistor.
   * @param R2 Ohm rating of R2.
   */
  public void setBridgeR2 (int R2) {
    this.R2 = (R2 >= 120) ? R2 : 120;
    mutated = true;

    // Event fired when data changes.
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets bridge resistance for R3 resistor.
   * @param R3 Ohm rating of R3.
   */
  public void setBridgeR3 (int R3) {
    this.R3 = (R3 >= 120) ? R3 : 120;
    mutated = true;

    // Event fired when data changes.
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets bridge resistance for R4 resistor.
   * @param R4 Ohm rating of R4.
   */
  public void setBridgeR4 (int R4) {
    this.R4 = (R4 >= 120) ? R4 : 120;
    mutated = true;

    // Event fired when data changes.
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets the maximum weight that will be applied to any gage.
   * @param w Maximum applied weight allowed.
   */
  public void setWeight (int w) {
    weight = (w >= 0 && w <= MAX_WEIGHT) ? w : 100;
    mutated = true;

    // Event fired when data changes.
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Maximum power allowed for any gage.  This determines the maximum voltage allowed for WheatstoneBridge.
   * @see #setVoltageIN
   * @param p Maximum power dissipation allowed.
   */
  public void setPower (int p) {
    power = (p >= 0 && p <= MAX_POWER) ? p : 50;
    mutated = true;

    // Event fired when data changes.
    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    queue.postEvent(new DataChangedEvent (this));
  }

  /**
   * Sets the steps at which calculations will occur.  This cannot exceed maximum weight allowed
   * for this circuit.
   * @see #setWeight
   * @param sS Intervals at which calculations will take place.
   */
  public void setStepSize (int sS) {
    stepSize = (sS >= 10 && sS < weight) ? sS : 10;
    mutated = true;

    // Event fired when data changes.
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
   * Returns the gage number field.
   * @return The current gage number selection.
   */
  public int getGageNumber () {
    return gageNumber;
  }

  /**
   * Returns the set or selected voltage input for this WheatstoneBridge circuit.
   * @return The voltage input.
   */
  public double getVoltageIN () {
    return bridgeVoltageInput;
  }

  /**
   * Returns selected strain gage for this circuit.
   * @return The gage resistance.
   */
  public int getBridgeResistance () {
    // Initial resistance of resistors is equal, so R1 = R2 = R3 = R4.  Therefore,
    // only one is returned.
    return R1;
  }

  /**
   * Returns the first resistor in this WheatstoneBridge.
   * @return Resistance for R1
   */
  public int getBridgeR1 () {
    return R1;
  }

  /**
   * Returns the second resistor in this WheatstoneBridge.
   * @return Resistance for R2
   */
  public int getBridgeR2 () {
    return R2;
  }

  /**
   * Returns the third resistor in this WheatstoneBridge.
   * @return Resistance for R3
   */
  public int getBridgeR3 () {
    return R3;
  }

  /**
   * Returns the fourth resistor in this WheatstoneBridge.
   * @return Resistance for R4
   */
  public int getBridgeR4 () {
    return R4;
  }

  /**
   * Returns selected amplifier voltage output.
   * @return Amplifier voltage output.
   */
  public double getAmplifierVoltageOUT () {
    return amplifierVoltageOutput;
  }

  /**
   * Returns maximum weight for this circuit.
   * @return Maximum weight applied to strain gages.
   */
  public int getWeight () {
    return weight;
  }

  /**
   * Returns maximum power allowed for strain gages.
   * @return Maximum power allowed for gages.
   */
  public int getPower () {
    return power;
  }

  /**
   * Returns the stepsize for calculstions.
   * @return Step size.
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
   * Maximum allowed input voltage set through any method in this class.
   * @see #getVoltageIN
   * @see #setVoltageIN
   * @return Maximum allowed voltage for input voltage field of this class.
   */
  public double getMAX_BRIDGE_VOLTAGE_INPUT () {
    return MAX_BRIDGE_VOLTAGE_INPUT;
  }

  /**
   * Maximum allowed amplifier output voltage set through any method in this class.
   * @see #setAmplifierVoltageOUT
   * @see #getAmplifierVoltageOUT
   * @return Maximum allowed voltage for amplifier voltage output field of this class.
   */
  public double getMAX_AMPLIFIER_VOLTAGE_OUT () {
    return MAX_AMPLIFIER_VOLTAGE_OUT;
  }

  /**
   * Maximum allowed strain gage weight set through any method of this class.
   * @see #setWeight
   * @see #getWeight
   * @return Maximum allowed weight for weight field of this class.
   */
  public int getMAX_WEIGHT () {
    return MAX_WEIGHT;
  }

  /**
   * Maximum allowed power for any gage(s) set through any method of this class.
   * @see #setPower
   * @see #getPower
   * @return Maximum allowed power for power field of this class.
   */
  public int getMAX_POWER () {
    return MAX_POWER;
  }
  // ---------------------------------------------------------------------------
  // MUTATORS
  // ---------------------------------------------------------------------------
  /**
   * Sets maximum constraint for bridge voltage.
   * @param MBV New bridge voltage max.
   */
  public void setMAX_BRIDGE_VOLTAGE_INPUT (int MBV) {
    MAX_BRIDGE_VOLTAGE_INPUT = MBV;
  }

  /**
   * Sets maximum constraint for amplifier output voltage by any selection.
   * @param MAV Max amplifier voltage.
   */
  public void setMAX_AMPLIFIER_VOLTAGE_OUT (int MAV) {
    MAX_AMPLIFIER_VOLTAGE_OUT = MAV;
  }

  /**
   * Sets constraint for maximum allowed weight by any selection.
   * @param MW Weight upper limit.
   */
  public void setMAX_WEIGHT (int MW) {
    MAX_WEIGHT = MW;
  }

  /**
   * Sets constraint for maximum power allowed by any selection.
   * @param MP Max power allowed for this circuit.
   */
  public void setMAX_POWER (int MP) {
    MAX_POWER = MP;
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