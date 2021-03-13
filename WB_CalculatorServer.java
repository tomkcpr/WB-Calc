/**
 * <p><b>Title: WB_CalculatorServer () </b></p>
 * <p><b>Description:</b></p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * A server side class for calculating circuit data.  Data is received and calculated table of
 * results is sent back to the client upon request.  The server is started on a 'localhost' at port
 * '2027'
 * <p><b>Company: NONE </b></p>
 * @version Project C (V1.0)
 * @author Tom Kacperski
 */

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

//import CalculatorServerModel;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.io.Serializable;

public class WB_CalculatorServer extends UnicastRemoteObject implements CalculatorServerModel {
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

  // SERVER SIDE
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


  private Object[] rtdTableHeader = {"Temperature(Degrees)","Resistance","Bridge Voltage(mV)"};
  private Object[] strainGageTableHeader = {"Weight (lb)","Bridge Voltage (mV)","Amplifier Voltage (V)"};

  private Object[] circuitTableHeader = {"Column 1","Column 2","Column 3"};
  private String tableTitle = "NONE";
  private double circuitPower;
  private NumberFormat formatter = NumberFormat.getNumberInstance(new Locale ("en", "ca"));

  // ---------------------------------------------------------------------------
  // SERVER SIDE
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Sets RTD input fields of this class.  Calculations are done based on these.
   * @param bridgeVoltage Bridge input voltage.
   * @param gageSelection Bridge type (Gages in bridge).
   * @param rtdAlpha Temperature coefficient.
   * @param tempLow Low temperature.
   * @param tempHigh High temperature.
   * @param plotPoints Plot points for graphing and calculating.
   * @param resistance Initial RTD resistance.
   * @throws RemoteException Call exception thrown when method fails to be called, completed or exception takes
   * place within it's code.
   */
  public void initRTDData (double bridgeVoltage, int gageSelection, double rtdAlpha, double tempLow,
                           double tempHigh, int plotPoints, double resistance) throws RemoteException {
    RTDbridgeVoltageInput = bridgeVoltage;
    RTDGageSelection = gageSelection;
    a = rtdAlpha;
    tmp_low = tempLow;
    tmp_high = tempHigh;
    RTDStepSize = plotPoints;
    RTDR1 = RTDR2 = RTDR3 = RTDR4 = resistance;
  }

  /**
   * Sets strain gage input fields of this class.  Calculations are done based on these.
   * @param resistance Initial strain gage resistance.
   * @param w Max weight applied to strain gage.
   * @param ampOut Amplifier max voltage.
   * @param bVI Bridge input voltage.
   * @param gs Gage selection (two or four).
   * @param ss Step size used in calculations.
   * @throws RemoteException
   */
  public void initStrainGageData (double resistance, int w, double ampOut, double bVI, int gs, int ss, int p) throws RemoteException {
    R1 = R2 = R3 = R4 = resistance;
    Weight = w;
    amplifierVoltageOutput = ampOut;
    bridgeVoltageInput = bVI;
    GageSelection = gs;
    StepSize = Weight / ss;
    power = p;
  }

  /**
   * Get the entire data block from this class.
   *
   */
  public double[][] getDataResults () throws RemoteException {
    return doubleDataTable;
  }

  /**
   * Returns maximum power circuit is under with given parameters.
   * @return Max power dissipated.
   */
  public double getCircuitPower () throws RemoteException {
    return circuitPower;
  }

  /**
   * Sets Feed back resistor outside of WB_Calculator class
   * @return Feed Back Resistor
   */
  public double getFeedBackResistor () throws RemoteException {
    return R1Amp;
  }

  // ---------------------------------------------------------------------------

/**
 * WB_Calculator Constructor that sets default values (Remote implementation)
 */
public WB_CalculatorServer () throws RemoteException {
  super();
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
 * Gets Feed Back Resistor outside of WB_Calculator class
 * @param fB Feedback Resistor
 */
public void setFeedBackResistor (double fB) {
  R1Amp = fB;
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


/**
 * Resistance Increase Rate Selector method, that sets the resistance
 * Icrease rate is constant for same resistance
 */
private void ResistanceIncrement()

{
   ResistanceIncreaseRate = 0;
   ResistanceIncreaseRate = (R1*0.025)/Weight;
}

// -----------------------------------------------------------------------------
// STRAIN GAGE
//
//
//
// -----------------------------------------------------------------------------
/**
 * Two gage strain gage calculation.
 */
private void StrainGage_2Gage()
{
     IntStepSum=0;
     Step=0;
     StepSum=0;

     R4=R3=R2=R1;

     Weight++;

     VoltageBridgeOut = new double [Weight];
     VoltageAmpOut = new double [Weight];
     circuitTableData = new Object[StepSize][3];
     doubleDataTable = new double[StepSize][3];

     formatter.setMaximumFractionDigits(1);
     formatter.setMinimumFractionDigits(3);



       for(i=1;i<Weight;i++)
         {
       R4+=ResistanceIncreaseRate;
       VoltageBridgeOut[i] = (bridgeVoltageInput * ( R4/(R4+R1) - R3/(R3+R2)))*1000;
        }

      AmplifierRate = amplifierVoltageOutput / VoltageBridgeOut[Weight - 1];
      R1Amp = (R2Amp * AmplifierRate)/2;


         for(i=1;i<Weight;i++)
        {
       VoltageAmpOut[i]=(AmplifierRate*VoltageBridgeOut[i]);

         }

         for (i=0;i<StepSize;i++)
         {
           Step = Weight / StepSize;
           StepSum+=Step;
           IntStepSum=Math.round((float)StepSum);


           if (IntStepSum >= Weight)
           {
             break;
           }

           // Update Table.
           circuitTableData[i][0] = String.valueOf(IntStepSum);
           circuitTableData[i][1] = String.valueOf(formatter.format(VoltageBridgeOut[IntStepSum]));
           circuitTableData[i][2] = String.valueOf(formatter.format(VoltageAmpOut[IntStepSum]));
           doubleDataTable[i][0] = IntStepSum;
           doubleDataTable[i][1] = VoltageBridgeOut[IntStepSum];
           doubleDataTable[i][2] = VoltageAmpOut[IntStepSum];
         }
         Weight--;
}

/**
 * Four Gage strain gage calculation.
 */
private void StrainGage_4Gage()
{
     IntStepSum=0;
     Step=0;
     StepSum=0;

     R4=R3=R2=R1;

     Weight++;

     VoltageBridgeOut = new double [Weight];
     VoltageAmpOut = new double [Weight];
     circuitTableData = new Object[StepSize][3];
     doubleDataTable = new double[StepSize][3];

     formatter.setMaximumFractionDigits(1);
     formatter.setMinimumFractionDigits(3);


       for(i=1;i<Weight;i++)
         {
       R4+=ResistanceIncreaseRate;
       R2+=ResistanceIncreaseRate;
       VoltageBridgeOut[i] = (bridgeVoltageInput * ( R4/(R4+R1) - R3/(R3+R2)))*1000;

        }

      AmplifierRate = amplifierVoltageOutput / VoltageBridgeOut[Weight - 1];
      R1Amp = (R2Amp * AmplifierRate)/2;


         for(i=1;i<Weight;i++)
        {
       VoltageAmpOut[i]=(AmplifierRate*VoltageBridgeOut[i]);

         }

         for (i=0;i<StepSize;i++)
         {
           Step = Weight / StepSize;
           StepSum+=Step;
           IntStepSum=Math.round((float)StepSum);


           if (IntStepSum >= Weight)
           {
             break;
           }

           // Update Table.
           circuitTableData[i][0] = String.valueOf(IntStepSum);
           circuitTableData[i][1] = String.valueOf(formatter.format(VoltageBridgeOut[IntStepSum]));
           circuitTableData[i][2] = String.valueOf(formatter.format(VoltageAmpOut[IntStepSum]));
           doubleDataTable[i][0] = IntStepSum;
           doubleDataTable[i][1] = VoltageBridgeOut[IntStepSum];
           doubleDataTable[i][2] = VoltageAmpOut[IntStepSum];
         }
         Weight--;
}

// -----------------------------------------------------------------------------
// RTD
//
//
//
// -----------------------------------------------------------------------------
private void Tmp_TwoGages()
{

 RTDR4=RTDR3=RTDR2=RTDR1;


  double tmp_balanced=25;    // temperature at which the bridge will be balanced
                             // or Ro=100
  double tmp_inc_Rate=0.1;
  double tmp_inc[];
  double tmp_step=0;
  double tmp_diff=0;
  double tmp_res_IncreaseRate=0;  // Resistance Increase Rate for 0.1 degree
  double tmp_step_IncreaseRate=0;


  formatter.setMaximumFractionDigits(1);
  formatter.setMinimumFractionDigits(3);

  tmp_res_IncreaseRate=0;
  tmp_step_IncreaseRate=0;

   tmp_diff = tmp_high-tmp_low;
   tmp_step = tmp_diff*10;

   tmp_step_IncreaseRate=tmp_step/RTDStepSize;

   tmp_res_IncreaseRate = ((RTDR1 * ( 1 + a * ((tmp_balanced+tmp_inc_Rate) - tmp_balanced) ))-RTDR1)*tmp_step_IncreaseRate;
   RTDR4 = RTDR1 * (1+a*(tmp_low-tmp_balanced));

   RTDStepSize++;

   RTDVoltageBridgeOut = new double [RTDStepSize];
   RTDResistance = new double [RTDStepSize];
   tmp_inc = new double [RTDStepSize];
   circuitTableData = new Object[RTDStepSize][3];
   doubleDataTable = new double[RTDStepSize][3];


   tmp_inc[0] = tmp_low;
   RTDResistance[0]=RTDR4;
   RTDVoltageBridgeOut[0] = (RTDbridgeVoltageInput * ( RTDR4/(RTDR4+RTDR1) - RTDR3/(RTDR3+RTDR2)))*1000;



     for (i=1;i<RTDStepSize;i++)
      {
        Step = tmp_step / RTDStepSize;
        StepSum+=Step;
        IntStepSum=Math.round((float)StepSum)-1;

         RTDR4+=tmp_res_IncreaseRate;
         RTDResistance[i]=RTDR4;
         RTDVoltageBridgeOut[i] = (RTDbridgeVoltageInput * ( RTDR4/(RTDR4+RTDR1) - RTDR3/(RTDR3+RTDR2)))*1000;
         tmp_inc[i] =(tmp_step_IncreaseRate*tmp_inc_Rate)+tmp_inc[i-1];
      }

         CircuitPower = ((RTDbridgeVoltageInput/2)*(RTDbridgeVoltageInput/2))/RTDResistance[RTDStepSize-1];
         //System.out.println("Power " +CircuitPower);
         circuitPower = CircuitPower * 1000.0;

            for (i=0;i<RTDStepSize;i++)
            {

             // Update Table.
             //System.out.println("Resistance  " + RTDResistance[i]);
             circuitTableData[i][0] = String.valueOf(formatter.format(tmp_inc[i]));
             circuitTableData[i][1] = String.valueOf(formatter.format(RTDResistance[i]));
             circuitTableData[i][2] = String.valueOf(formatter.format(RTDVoltageBridgeOut[i]));
             doubleDataTable[i][0] = tmp_inc[i];
             doubleDataTable[i][1] = RTDResistance[i];
             doubleDataTable[i][2] = RTDVoltageBridgeOut[i];
            }
            RTDStepSize--;

}


private void Tmp_FourGages()
{
 RTDR4=RTDR2=RTDR3=RTDR1;

  double tmp_balanced=25;    // temperature at which the bridge will be balanced
                             // or Ro=100
  double tmp_inc_Rate=0.1;
  double tmp_inc[];
  double tmp_step=0;
  double tmp_diff=0;
  double tmp_res_IncreaseRate;  // Resistance Increase Rate for 0.1 degree
  double tmp_step_IncreaseRate;


  formatter.setMaximumFractionDigits(1);
  formatter.setMinimumFractionDigits(3);

  tmp_res_IncreaseRate=0;
  tmp_step_IncreaseRate=0;

   tmp_diff = tmp_high-tmp_low;
   tmp_step = tmp_diff*10;

   tmp_step_IncreaseRate=tmp_step/RTDStepSize;

   tmp_res_IncreaseRate = ((RTDR1 * ( 1 + a * ((tmp_balanced+tmp_inc_Rate) - tmp_balanced) ))-RTDR1)*tmp_step_IncreaseRate;
   RTDR4 = RTDR1 * (1+a*(tmp_low-tmp_balanced));
   RTDR2=RTDR4;

   RTDStepSize++;

   RTDVoltageBridgeOut = new double [RTDStepSize];
   RTDResistance = new double [RTDStepSize];
   tmp_inc = new double [RTDStepSize];
   circuitTableData = new Object[RTDStepSize][3];
   doubleDataTable = new double[RTDStepSize][3];


   tmp_inc[0] = tmp_low;
   RTDResistance[0]=RTDR4;
   RTDVoltageBridgeOut[0] = (RTDbridgeVoltageInput * ( RTDR4/(RTDR4+RTDR1) - RTDR3/(RTDR3+RTDR2)))*1000;



     for (i=1;i<RTDStepSize;i++)
      {
        Step = tmp_step / RTDStepSize;
        StepSum+=Step;
        IntStepSum=Math.round((float)StepSum)-1;

         RTDR2+=tmp_res_IncreaseRate;
         RTDR4+=tmp_res_IncreaseRate;
         RTDResistance[i]=RTDR4;
         RTDVoltageBridgeOut[i] = (RTDbridgeVoltageInput * ( RTDR4/(RTDR4+RTDR1) - RTDR3/(RTDR3+RTDR2)))*1000;
         tmp_inc[i] =(tmp_step_IncreaseRate*tmp_inc_Rate)+tmp_inc[i-1];
      }

         // CircuitPower = ((RTDbridgeVoltageInput/2)*(RTDbridgeVoltageInput/2))/RTDResistance[RTDStepSize];
         CircuitPower = ((RTDbridgeVoltageInput/2)*(RTDbridgeVoltageInput/2))/RTDResistance[RTDStepSize-1];

            for (i=0;i<RTDStepSize;i++)
            {

             // Update Table.
             circuitTableData[i][0] = String.valueOf(formatter.format(tmp_inc[i]));
             circuitTableData[i][1] = String.valueOf(formatter.format(RTDResistance[i]));
             circuitTableData[i][2] = String.valueOf(formatter.format(RTDVoltageBridgeOut[i]));
             doubleDataTable[i][0] = tmp_inc[i];
             doubleDataTable[i][1] = RTDResistance[i];
             doubleDataTable[i][2] = RTDVoltageBridgeOut[i];
            }
            RTDStepSize--;
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

/**
 * Returns RTD table header.
 * @return RTD table header.
 */
public Object[] getRtdTableHeader()
{
 return rtdTableHeader;
}
/**
 *  Method that calls OutputVoltage2 method from TwoGages class.
 */

/**
 * Main calculate method for data calculations.  Methods are called depending on circuit
 * selection from CalculationsJPanel JComboBox selection.
 * @param circuit Circuit to calculate.
 */
public void calculate (int circuit)  throws RemoteException {
  //System.out.println ("circuit = " + circuit);
  if (circuit == 0)
    calculate ();
  else
    if (circuit == 1)
      rtdCalculate ();
}

/**
 * Method for RTD calculations.  Depending on circuit selection, this method is called.
 */
public void rtdCalculate ()
{
  R1Amp=0;
 if (RTDGageSelection == 2)
 {
  Tmp_TwoGages();
  //System.out.println ("RTD 2 Gage");
 }
  else if (RTDGageSelection == 4)
  {
   Tmp_FourGages();
   //System.out.println ("RTD 4 Gage");
  }

  // ===========================================================================
  tableTitle = "RTD Circuit";
  setHeaders (new Object[] {"Temperature(Degrees)","Resistance", "Bridge Voltage(mV)"});
  // ===========================================================================
}

/**
 * Method that calls OutputVoltage2 method if Gages Selection is 2 or
 * that calls OutputVoltage4 method if Gages Selection is 4
 * when calculate button action listener is occured
 */
public void calculate () {
 if (GageSelection == 2) {
    ResistanceIncrement();
    StrainGage_2Gage();
//    Tmp_TwoGages();
  }
  else
    if (GageSelection == 4) {
      ResistanceIncrement();
      StrainGage_4Gage();
    }

    // ===========================================================================
    tableTitle = "Strain Gage Circuit";
    setHeaders (new Object[] {"Weight (lb)","Bridge Voltage (mV)", "Amplifier Voltage (mV)"});
    // ===========================================================================
}

/**
 * Main method of this class.  Static, declares an object of this class and binds it to
 * a host and service.
 * @param args Arguments passed on the command line.
 */
public static void main (String[] args) {
  //if (System.getSecurityManager() == null)
  //  System.setSecurityManager(new RMISecurityManager());
  try {
    CalculatorServerModel cm = new WB_CalculatorServer();
    //Naming.rebind ("rmi://localhost:2027/JDService", cm);
    Naming.rebind ("rmi://localhost:2027/TomK", cm);
    System.out.println("WB_CalculatorServer main!");
  } catch (Exception e) {
      System.out.println ("Naming.rebind (RMI) error!!  Can't start server!" + e);
  }
}
}