/*
 * @(#)IntegerTextFieldVerifier.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Classes;

import javax.swing.*;

/**
 * <p><b>Title: DoubleTextFieldVerifier() </b></p>
 * <p><b>Description:</b></p>
 * Input verifier that allows only positive integer values through and prevents user from leaving a text field otherwise.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class IntegerTextFieldVerifier extends InputVerifier {
  private VerifyInteger vi = new VerifyInteger();

  /**
   * Default constructor that takes no parameters and sets no values.
   */
  public IntegerTextFieldVerifier() { super(); }

  /**
   * Sets the default verifier to be use for verification from calling environment.
   * @param vd The integer verifier to use.
   */
  public void setLimits (VerifyInteger vi) {
    this.vi = vi;
  }

  /**
   * Verifier allows focus changes only when this method returns true.  The inner
   * input verifier is used to test input.
   * @param input A JComponent from which values to be verified are retrieved.
   * @return
   */
  public boolean shouldYieldFocus (JComponent input) {
    return verify(input);
  }

  /**
   * A convenience method for verifying text passed through a component.
   * @param input The JComponent from which a value to verify will be retrieved.
   * @return
   */
  public boolean verify (JComponent input) {
    String text = ((JTextField)input).getText();
    return vi.verify (text);
  }
}