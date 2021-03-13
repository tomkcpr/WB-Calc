/*
 * @(#)CalculateEvent.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Classes;

import java.awt.AWTEvent;
import javax.swing.JComponent;

/**
 * <p><b>Title: CalculateEvent () </b></p>
 * <p><b>Description:</b></p>
 * A calculate event for uniquely identifying / indicating data calculations to parent components.  This event is used in
 * conjunction with {@link CalculateListener} to act on posted events [calculations].
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project B (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class CalculateEvent extends AWTEvent {
  public CalculateEvent (JComponent jc) {
    super (jc, CALCULATE_ID);
  }
  public static final int CALCULATE_ID = RESERVED_ID_MAX + 10001;
}