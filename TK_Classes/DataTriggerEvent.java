/*
 * @(#)DataTriggerEvent.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Classes;

import java.awt.AWTEvent;
import javax.swing.JComponent;

/**
 * <p><b>Title: DataTriggerEvent </b></p>
 * <p><b>Description:</b></p>
 * DataTriggerEvent is a modification to the DataChangedEvent.  It is to indicate that the change not only impacts
 * the calling environment but also properties of the calling component.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project B (V1.0)
 * @author Tom Kacperski
 */
public class DataTriggerEvent extends AWTEvent {
  public DataTriggerEvent(JComponent c) {
    super (c, DATA_TRIGGER);
  }
  public static final int DATA_TRIGGER = AWTEvent.RESERVED_ID_MAX + 10002;
}