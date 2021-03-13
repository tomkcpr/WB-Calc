package TK_Interfaces;
/**
 * <p><b>Title: CalculatorServerModel () </b></p>
 * <p><b>Description:</b></p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * A server side interface for calculating circuit data.  The methods here are required for remote invocation
 * through RMI.  Data is passed to server then a two dimensional array is returned to the client with
 * results.
 * <p><b>Company: NONE </b></p>
 * @version Project C (V1.0)
 * @author Tom Kacperski
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CalculatorServerModel extends Remote {
  // Get the entire data block from this class.
  public double[][] getDataResults () throws RemoteException;

  // Sets RTD data.
  public void initRTDData (double bridgeVoltage, int gageSelection, double rtdAlpha, double tempLow,
                           double tempHigh, int plotPoints, double resistance) throws RemoteException;

  // Sets Strain Gage data.
  public void initStrainGageData (double resistance, int w, double ampOut, double bVI, int gs, int ss, int p) throws RemoteException;

  // REMOTE or LOCAL calculate call.
  public void calculate (int circuit) throws RemoteException;

  public double getFeedBackResistor () throws RemoteException;

  public double getCircuitPower () throws RemoteException;
}
