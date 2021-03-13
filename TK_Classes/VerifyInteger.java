/*
 * @(#)VerifyInteger.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Classes;

/**
 * <p><b>Title: VerifyInteger() </b></p>
 * <p><b>Description:</b></p>
 * A custom integer verifier class for use with {@link IntegerTextFieldVerifier}.  Values passed here are checked
 * against minimums and maximums and invalid characters.  Values that meet the inner criteria will cause methods here
 * to return true.  False otherwise.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class VerifyInteger {
  private int min = 1;
  private int max = 10;

  /**
   * No parameter constructor.  Default instantiation.
   */
  public VerifyInteger() { }

  /**
   * Sets constraints that values must meet for methods here to return true.
   * @param min A minimum that passed value must meet.
   * @param max A maximum that passed value must not exceed.
   */
  public void setConstraints (int min, int max) {
    this.min = min;
    this.max = max;
  }

  /**
   * Verify method for testing values to specified constraints.  Values converted to integer
   * and that fall within a range, cause this method to return true.  False otherwise.
   * @param voltageStr The string to test.
   * @return <code>true</code> or <code>false</code> if a value is a valid integer.
   */
  public boolean verify (String voltageStr) {
      int strVal = 0;
      try {
        strVal = Integer.parseInt (voltageStr);
      } catch (NumberFormatException nfe) {
        return false;
      }

      if (strVal < min || strVal > max)
        return false;

      return true;
    }
}