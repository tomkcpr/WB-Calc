/*
 * @(#)DataChangedEvent.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Classes;

import java.awt.AWTEvent;
import javax.swing.JComponent;

/**
 * <p><b>Title: DataChangedEvent() </b></p>
 * <p><b>Description:</b></p>
 * A data changed event for uniquely identifying / indicating data changes to parent components.  This event is used in
 * conjunction with {@link DataChangedListener} to act on posted events.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class DataChangedEvent extends AWTEvent {
  public DataChangedEvent(JComponent jc) {
    super (jc, DATACHANGED_EVENT);
  }
  public static final int DATACHANGED_EVENT = AWTEvent.RESERVED_ID_MAX + 10000;
}