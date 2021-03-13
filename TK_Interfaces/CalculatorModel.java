package TK_Interfaces;

import java.io.Serializable;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public interface CalculatorModel {
  // Return the entire data block to this class.
  public void setDataResult (int cs, double[][] dr);

  // Sets feedback resistor value of implementing object.
  public void setFeedBackResistor (double fB);

  // Sets circuit power value of implementing object.
  public void setCircuitPower (double cp);
}
