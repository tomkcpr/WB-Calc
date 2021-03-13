/*
 * @(#)VerifyDouble.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Classes;

/**
 * <p><b>Title: VerifyDouble() </b></p>
 * <p><b>Description:</b></p>
 * A custom double verifier class for use with {@link DoubleTextFieldVerifier}.  Values passed here are checked
 * against minimums and maximums and invalid characters.  Values that meet the inner criteria will cause methods here
 * to return true.  False otherwise.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class VerifyDouble {
  private double min = 1.0;
  private double max = 10.0;

  /**
   * No parameter constructor.  Default instantiation.
   */
  public VerifyDouble() {}

  /**
   * Sets constraints that values must meet for methods here to return true.
   * @param min A minimum that passed value must meets.
   * @param max A maximum that passed value must not exceed.
   */
  public void setConstraints (double min, double max) {
    this.min = min;
    this.max = max;
  }

  /**
   * Verify method for testing values to specified constraints.  Values converted to double
   * and that fall within a range, cause this method to return true.  False otherwise.
   * @param voltageStr The string to test.
   * @return <code>true</code> or <code>false</code> if a value is a valid double.
   */
  public boolean verify (String voltageStr) {
    double strVal = 0.0;
    try {
      strVal = Double.parseDouble (voltageStr);
    } catch (NumberFormatException nfe) {
      return false;
    }

    if (strVal < min || strVal > max)
      return false;

    return true;
  }
}