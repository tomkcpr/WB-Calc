/**
 * <p>Title: Calculator Class</p>
 * <p>Description: Gets values either from GUI or from file and
 * calculates Voltage Bridge Output and amplifies it to Desired System
 * Output depending on what type of system was selected(2 or 4 gages).
 * It fills in values into table depending on step size selected.
 * It calculates the feed back resistor value</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Pavel Lyssenko & Tom Kacperski
 * @version 1.0
 */

package TK_Classes;

import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.text.NumberFormat;
import java.util.EventListener;
import javax.swing.table.TableModel;
import javax.swing.event.EventListenerList;
import TK_Interfaces.CalculateListener;
import java.awt.AWTEvent;
import TK_Classes.CalculateEvent;
import javax.swing.JComponent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.Locale;
import TK_Interfaces.CalculatorModel;
import java.io.Serializable;

public class WB_Calculator implements CalculatorModel {
  /** Desired System Output   */
  double amplifierVoltageOutput;
  /** Desired Voltage Input */
  double bridgeVoltageInput;
  double RTDbridgeVoltageInput;
  /** Desired Resistance of Resistor */
  double R1, R2, R3, R4;
  double RTDR1, RTDR2, RTDR3, RTDR4;
  private int i;
  private double VoltageBridgeOut[];    // Bridge Output
  private double RTDVoltageBridgeOut[];
  private double VoltageAmpOut[];       // Amplified Output of Bridge
  private double AmplifierRate;
  private double ResistanceIncreaseRate;
  private double RTDResistance[];
  private double tmp_inc[];
  double tmp_low;
  double tmp_high;
  /** Desired mazimum power level  */
  int power;
  /** Calculated feed back resistor  */
  double R1Amp;
  private double R2Amp=20000;
  private double Step, StepSum;
  private int IntStepSum;
  /**Desired gage sleection(2 or 4 gages system) */
  int GageSelection;
  int RTDGageSelection;
  /**Calculated feed back resistor */
  double feedbackResistor = 0.0;
  /**Desired Step Size */
  int StepSize;
  int RTDStepSize;
  /**Desired Maximum Weight */
  int Weight;
  double a;
  double CircuitPower;
  int circuitSelection;

  // For Server Side calculations.
  private double[][] doubleDataTable =
  {{0.0,0.0,0.0},
    {0.0,0.0,0.0},
    {0.0,0.0,0.0},
    {0.0,0.0,0.0},
    {0.0,0.0,0.0}};

  // =============================================================================
  private Object[][] circuitTableData =
  {{"0.0","0.0","0.0"},
   {"0.0","0.0","0.0"},
   {"0.0","0.0","0.0"},
   {"0.0","0.0","0.0"},
   {"0.0","0.0","0.0"}};

  //private Object[] rtdTableHeader = {"Temperature(Degrees)","Resistance","Bridge Voltage(mV)"};
  //private Object[] strainGageTableHeader = {"Weight (lb)","Bridge Voltage (mV)","Amplifier Voltage (V)"};

  private Object[] circuitTableHeader = {"Column 1","Column 2","Column 3"};
  private String tableTitle = "NONE";
  private double circuitPower;
  private NumberFormat formatter = NumberFormat.getNumberInstance(new Locale ("en", "ca"));
  // =============================================================================

  // ---------------------------------------------------------------------------
  // SERVER SIDE
  //
  //
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Inner classes for use on Server / Client side.
  //
  //
  //
  // ---------------------------------------------------------------------------
  private class ClassData implements Serializable {
    private double _amplifierVoltageOutput;
    private double _bridgeVoltageInput;
    private double _RTDbridgeVoltageInput;
    private double _R1, _R2, _R3, _R4;
    private double _RTDR1,_RTDR2,_RTDR3,_RTDR4;
    private int _i;
    private double _VoltageBridgeOut[];
    private double _RTDVoltageBridgeOut[];
    private double _VoltageAmpOut[];
    private double _AmplifierRate;
    private double _ResistanceIncreaseRate;
    private double _RTDResistance[];
    private double _tmp_inc[];
    private double _tmp_low;
    private double _tmp_high;
    private int _power;
    private double _R1Amp;
    private double _R2Amp=20000;
    private double _Step, _StepSum;
    private int _IntStepSum;
    private int _GageSelection;
    private int _RTDGageSelection;
    private double _feedbackResistor = 0.0;
    private int _StepSize;
    private int _RTDStepSize;
    private int _Weight;
    private double _a;
    private double _CircuitPower;
    private int _circuitSelection;

    private Object[][] _circuitTableData = null;
    private Object[] _rtdTableHeader = null;
    private Object[] _strainGageTableHeader = null;
    private Object[] _circuitTableHeader = null;
    private String _tableTitle = null;
    private double _circuitPower;
    // For Server Side calculations.
    private double[][] _doubleDataTable = null;

    // Constructor
    public ClassData() {
      System.out.println("[A]");
      initData();
      System.out.println("[B]");
    }

    private void initData () {
      // Small data blocks.
      _amplifierVoltageOutput = amplifierVoltageOutput;
      System.out.println ("[1]bridgeVoltageInput = " + bridgeVoltageInput + ", _bridgeVoltageInput = " + _bridgeVoltageInput);
      _bridgeVoltageInput = bridgeVoltageInput;
      _RTDbridgeVoltageInput = RTDbridgeVoltageInput;
      _R1 = R1;
      _R2 = R2;
      _R3 = R3;
      _R4 = R4;
      _RTDR1 = RTDR1;
      _RTDR2 = RTDR2;
      _RTDR3 = RTDR3;
      _RTDR4 = RTDR4;
      _i = i;
      _VoltageBridgeOut = VoltageBridgeOut;
       _RTDVoltageBridgeOut = RTDVoltageBridgeOut;
      _VoltageAmpOut = VoltageAmpOut;
      _AmplifierRate = AmplifierRate;
      _ResistanceIncreaseRate = ResistanceIncreaseRate;
      _RTDResistance = RTDResistance;
      _tmp_inc = tmp_inc;
      _tmp_low = tmp_low;
      _tmp_high = tmp_high;
      _power = power;
      _R1Amp = R1Amp;
      _R2Amp = R2Amp;
      _Step = Step;
      _StepSum = StepSum;
      _IntStepSum = IntStepSum;
      _GageSelection = GageSelection;
      _RTDGageSelection = RTDGageSelection;
      _feedbackResistor = feedbackResistor;
      _StepSize = StepSize;
       _RTDStepSize = RTDStepSize;
      _Weight = Weight;
      _a = a;
      _CircuitPower = CircuitPower;
      _circuitSelection = circuitSelection;

      // Large data blocks.
      _circuitTableData = new Object[circuitTableData.length][circuitTableData[0].length];
      _doubleDataTable = new double[doubleDataTable.length][doubleDataTable[0].length];
      System.out.println ("[1-]");
      for (int i = 0; i < circuitTableData.length; i++) {
        for (int j = 0; j < circuitTableData[0].length; j++) {
          _circuitTableData[i][j] = circuitTableData[i][j];
          _doubleDataTable[i][j] = doubleDataTable[i][j];
          System.out.print (circuitTableData[i][j]);
        }
        System.out.println ();
      }
      System.out.println ("[2-]");

      _circuitTableHeader = circuitTableHeader;
      //for (int i = 0; i < circuitTableHeader.length; i++) {
      //  _circuitTableHeader[i] = circuitTableHeader[i];
      //  System.out.print (circuitTableHeader[i] + ", ");
      //}

      //_circuitTableData = circuitTableData;
      //_circuitTableHeader = circuitTableHeader;
      //_doubleDataTable = doubleDataTable

      _tableTitle = tableTitle;
      _circuitPower = circuitPower;
    }

    /**
     * Retrieves data from 'ClassData' class and initializes data fields of 'WB_Calculator' class.
     */
    public void retrieveALL (WB_Calculator target) {
      // Small data blocks.
      target.amplifierVoltageOutput = _amplifierVoltageOutput;
      System.out.println ("[0]bridgeVoltageInput = " + bridgeVoltageInput +
                          ", _bridgeVoltageInput = " + _bridgeVoltageInput);
      target.bridgeVoltageInput = _bridgeVoltageInput;
      System.out.println ("[2+]RTDbridgeVoltageInput = " + RTDbridgeVoltageInput +
                          ", _RTDbridgeVoltageInput = " + _RTDbridgeVoltageInput);
      target.RTDbridgeVoltageInput = _RTDbridgeVoltageInput;
      System.out.println ("[2-]RTDbridgeVoltageInput = " + RTDbridgeVoltageInput +
                          ", _RTDbridgeVoltageInput = " + _RTDbridgeVoltageInput);
      target.R1 = _R1;
      target.R2 = _R2;
      target.R3 = _R3;
      target.R4 = _R4;
      target.RTDR1 = _RTDR1;
      target.RTDR2 = _RTDR2;
      target.RTDR3 = _RTDR3;
      target.RTDR4 = _RTDR4;
      target.i = _i;
      target.VoltageBridgeOut = _VoltageBridgeOut;
      target.RTDVoltageBridgeOut = _RTDVoltageBridgeOut;
      target.VoltageAmpOut = _VoltageAmpOut;
      target.AmplifierRate = _AmplifierRate;
      target.ResistanceIncreaseRate = _ResistanceIncreaseRate;
      target.RTDResistance = _RTDResistance;
      target.tmp_inc = _tmp_inc;
      target.tmp_low = _tmp_low;
      target.tmp_high = _tmp_high;
      target.power = _power;
      target.R1Amp = _R1Amp;
      target.R2Amp = _R2Amp;
      target.Step = _Step;
      target.StepSum = _StepSum;
      target.IntStepSum = _IntStepSum;
      target.GageSelection = _GageSelection;
      target.RTDGageSelection = _RTDGageSelection;
      target.feedbackResistor = _feedbackResistor;
      target.StepSize = _StepSize;
      target.RTDStepSize = _RTDStepSize;
      target.Weight = _Weight;
      target.a = _a;
      target.CircuitPower = _CircuitPower;
      target.circuitSelection = _circuitSelection;

      // Large data blocks.
      target.circuitTableData = new Object[_circuitTableData.length][_circuitTableData[0].length];
      target.doubleDataTable = new double[_doubleDataTable.length][_doubleDataTable[0].length];
      for (int i = 0; i < _circuitTableData.length; i++) {
        for (int j = 0; j < _circuitTableData[0].length; j++) {
          target.circuitTableData[i][j] = _circuitTableData[i][j];
          target.doubleDataTable[i][j] = _doubleDataTable[i][j];
          System.out.print (circuitTableData[i][j]);
        }
        System.out.println ();
      }

      target.circuitTableHeader = _circuitTableHeader;
      //circuitTableHeader = new String[_circuitTableHeader.length];
      //for (int i = 0; i < circuitTableHeader.length; i++) {
      //  circuitTableHeader[i] = _circuitTableHeader[i];
      //  System.out.print (circuitTableHeader[i] + ", ");
      //}

      //circuitTableData = _circuitTableData;
      //circuitTableHeader = _circuitTableHeader;

      target.tableTitle = _tableTitle;
      target.circuitPower = _circuitPower;
    }
  }
  // Get the entire data block from this class.
  public ClassData getDataBlock (){
    return new ClassData();
  }

  // Return the entire data block to this class.
  public void setDataBlock (ClassData cd){
    cd.retrieveALL(this);            // Modifies 'cd' only......
  }

  /**
   * Sets this class 'object' and 'double' data tables from 'dr[][]'
   * @param cs Circuit for which to set data table headers and titles.
   * @param dr Double data array from which all setting will take place.
   */
  public void setDataResult (int cs, double[][] dr){
    doubleDataTable = new double [dr.length][dr[0].length];
    circuitTableData = new Object[doubleDataTable.length][doubleDataTable[0].length];

    formatter.setMaximumFractionDigits(1);
    formatter.setMinimumFractionDigits(3);
    for (int i = 0; i < dr.length; i++)
      for (int j = 0; j < dr[0].length; j++) {
        doubleDataTable[i][j] = dr[i][j];
        circuitTableData[i][j] = String.valueOf(formatter.format(doubleDataTable[i][j]));
      }

    if (cs == 0) {
      tableTitle = "Strain Gage Circuit";
      setHeaders (new Object[] {"Weight (lb)","Bridge Voltage (mV)", "Amplifier Voltage (mV)"});
    } else
      if (cs == 1) {
        tableTitle = "RTD Circuit";
        setHeaders (new Object[] {"Temperature(Degrees)","Resistance", "Bridge Voltage(mV)"});
      }
  }

  /**
   * Gets Feed Back Resistor outside of WB_Calculator class
   * @param fB Feedback Resistor
   */
  public void setFeedBackResistor (double fB) {
    R1Amp = fB;
  }

  /**
   * Sets the circuit power of this circuit.  For use when passing data between
   * objects of this class.
   * @param cp
   */
  public void setCircuitPower (double cp) {
    circuitPower = cp;
  }
  // ---------------------------------------------------------------------------

/**
 * WB_Calculator Constructor that sets default values (Remote implementation)
 */
public WB_Calculator () {
  constructorInit();
}

private void constructorInit () {
// Strain Gage defaults
  bridgeVoltageInput=1;
  R1=R2=R3=R4=120;
  amplifierVoltageOutput=1;
  Weight=100;
  StepSize=25;
  GageSelection = 2;
  power = 50;

  circuitSelection=0;

// RTD defaults
  RTDbridgeVoltageInput=1;
  RTDR1=RTDR2=RTDR3=RTDR4=100;
  tmp_low=0;
  tmp_high=100;
  RTDStepSize=5;
  RTDGageSelection=2;
  a=0.004;
}

/*************************************************************************/
/* Set Values
/*
/*************************************************************************/
/**
 * Returns temperature coefficient (alpha).
 * @return Temperature coefficient (alpha).
 */
public double getRTDAlpha()
{
 return a;
}

/**
 * Returns gage selection.
 * @return Gage Selection.
 */
public int getRTDGageSelection()
{
 return RTDGageSelection;
}

/**
 * Returns initial RTD resistance.
 * @return Initial RTD resistance.
 */
public double getRTDResistance()
{
 return RTDR1;
}

/**
 * Returns plot points to use for calculations and graphing.
 * @return Graphing and calculations plot points.
 */
public int getRTDStepSize()
{
 return RTDStepSize;
}

/**
 * Returns bridge voltage input.
 * @return Bridge input voltage.
 */
public double getBridgeVoltageInput()
{
 return RTDbridgeVoltageInput;
}

/**
 * Returns bridge voltage out as object.
 * @return Object value of bridge voltage out.
 */
public Object getRTDVoltageBridgeOut()
{
 return RTDVoltageBridgeOut;
}

/**
 * Returns circuit low temperature.
 * @return Low temperature.
 */
public double getTmpLow()
{
 return tmp_low;
}

/**
 * Returns circuit high temperature.
 * @return High temperature.
 */
public double getTmpHigh()
{
 return tmp_high;
}

/**
 * Returns temperature increment.
 * @return Temperature increment.
 */
public Object getTmpInc()
{
 return tmp_inc;
}

/**
 * Returns resistance array as object.
 * @return Resistance array.
 */
public Object getResistanceArray()
{
  return RTDResistance;
}

/**
 * Sets Power outside of WB_Calculator class
 * @return power
 */
public int getPower () {
  return power;
}

// =============================================================================
/**
 * Returns maximum power circuit is under with given parameters.
 * @return Max power dissipated.
 */
public double getCircuitPower () {
  return circuitPower;
}
// =============================================================================

/**
 * Sets Feed back resistor outside of WB_Calculator class
 * @return Feed Back Resistor
 */
public double getFeedBackResistor () {
  return R1Amp;
}

/**
 * sets Bridge Resistance outside of WB_Calculator class
 * @return Bridge Resistance
 */
public double getResistance ()
{
  return R1;
}

/**
 * Sets Weight outside of WB_Calculator class
 * @return Weight
 */
public int getWeight()
{
  return Weight;
}

/**
 * Sets Voltage Input outside of WB_Calculator class
 * @return Voltage Input
 */
public double getVoltageIN()
{
 return bridgeVoltageInput;
}

/**
 * Sets System Output outside of WB_Calculator class
 * @return System Output
 */
public double getVoltageOutput()
{
  return amplifierVoltageOutput;
}

/**
 * Sets Power outside of WB_Calculator class
 * @return Gage Selection
 */
public int getGageSelection()
{
  return GageSelection;
}

/**
 * Sets Step Size outside of WB_Calculator class
 * @return Step Size
 */
public int getStepSize()
{
  return StepSize;
}
/*************************************************************************/
// Get Values
/*************************************************************************/

/**
 * Sets gage selection in this class.
 * @param rtd RTD bridge selection.
 */
public void setRTDGageSelection(int rtd)
{
 RTDGageSelection=rtd;
}

/**
 *  Sets plot points to use for calculating and graphing.
 * @param step Plot points value.
 */
public void setRTDStepSize(int step)
{
  RTDStepSize=step;
}

/**
 * Sets low temperature in this class.
 * @param low Low temperature setting.
 */
public void setTmpLow (int low)
{
  tmp_low=low;
}

/**
 * Sets high temperature in this class.
 * @param high High temperature setting.
 */
public void setTmpHigh(int high)
{
  tmp_high = high;
}

/**
 * Sets max power dissipation in this class.
 * @param p Max power dissipation.
 */
public void setPower (int p) {
  power = p;
}

/**
 * Sets initial RTD Resistance for R1 - R4
 * @param r initial RTD resistance.
 */
public void setRTDResistance(double r)
{
  RTDR1=RTDR2=RTDR3=RTDR4=r;
}

/**
 * Sets temperature coefficient value in this class.
 * @param rate Temperature coefficient (alpha).
 */
public void setRTDAlpha(double rate)
{
 a=rate;
}

/**
 * Gets Bridge Resistance outside of WB_Calculator class
 * @param r resistance of bridge.
 */
public void setResistance (int r) {
  R1 = R2 = R3 = R4 = r;
}


/**
 * gets Weight outside of WB_Calculator class
 * @param w Weight value.
 */
public void setWeight (int w) {
  this.Weight = w;
}


/**
 * Gets Voltage Input outside of WB_Calculator class
 * @param vBI Bridge voltage in.
 */
public void setBridgeVoltageIN (double vBI)
{
  this.bridgeVoltageInput = vBI;
}

public void setRTDBridgeVoltageIN (double vBI)
{
  this.RTDbridgeVoltageInput = vBI;
}
/**
 * Gets desired System Output outside of WB_Calculator class
 * @param vAO Amp voltage output.
 */
public void setAmpVoltageOUT (double vAO)
{
  this.amplifierVoltageOutput = vAO;
}


/**
 * Gets Gage Selection outside of WB_Calculator class
 * @param gS Gage selection value.
 */
public void setGageSelection (int gS)
{
  this.GageSelection = gS;
}


/**
 * Gets StepSize outside of WB_Calculator class
 * @param sS Step size value.
 */
public void setStepSize (int sS)
{
  this.StepSize = Weight / sS;
}



//==============================================================================
/**
 * Clears the table and returns a null object.
 * @return Null data object.
 */
public Object[][] clearTable() {
  Object[][] noDataObject = {};
  circuitTableData = noDataObject;

  //// Post event indicating data has changed.
  //EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
  //queue.postEvent(new CalculateEvent(this));

  return circuitTableData;
}

/**
 * Clears the table headers and reinitializes them to default values.
 * @return Default column headers object.
 */
public Object [] clearHeaders() {
  Object[] noDataHeader = {"Column 1","Column 2","Column 3"};
  circuitTableHeader = noDataHeader;

  //EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
  //queue.postEvent(new CalculateEvent(this));

  return noDataHeader;
}

/**
 * Sets headers to a header array.
 * @param hO Header object array.
 */
public void setHeaders (Object[] hO) {
  circuitTableHeader = hO;
}

/**
 * Clears table title to a default value.  Value is retrieved for table from here.
 * @return Title string.
 */
public String clearTitle () {
  //EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
  //queue.postEvent(new CalculateEvent(this));

  tableTitle = "NONE";
  return "NONE";
}

/**
 * Sets title parameter.
 * @param t Title string.
 */
public void setTitle (String t) {
  tableTitle = t;
}

/**
 * Get's the title string.
 * @return Title string.
 */
public String getTitle () {
  return tableTitle;
}

/**
 * Clears feedback resistor value.
 * @return 0.0 or cleared feedback resistor value.
 */
public double clearFeedbackResistor () {
  R1Amp = 0.0;

  //EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
  //queue.postEvent(new CalculateEvent(this));

  return 0.0;
}

/**
 * Clears circuit power setting.
 * @return 0 or circuit power setting.
 */
public int clearCircuitPower () {
  circuitPower = 0;

  return 0;
}

/**
 * Clears all relavent data fields of this class.
 */
public void clearData () {
  clearTable();
  clearHeaders();
  clearTitle();
  clearCircuitPower();
  clearFeedbackResistor();
}

/**
 * Sets current circuit selection.
 * @param circuit Circuit selection.
 */
public void setCircuit (int circuit) {
  circuitSelection = circuit;
  //System.out.println("CS: " +circuitSelection);

}

/**
 * Gets curretn circuit selection.
 * @return Current circuit selection.
 */
public int getCircuit () {
  return circuitSelection;
}
//==============================================================================

/** Gets data for each table spot outside of WB_Calculator class*/
public Object[][] getTableData () {
  //System.out.println ("getTableData()");
  return circuitTableData;
}

/**
 * Sets table data to a two dimensional array object.
 * @param td Object[N][M] of table data.
 */
public void setTableData (Object[][] td) {
  circuitTableData = td;
}

/**
 * Returns table header object.
 * @return Table header array.
 */
public Object[] getTableHeader () {
  return circuitTableHeader;
}

}