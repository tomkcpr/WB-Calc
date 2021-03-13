/*
 * @(#)DataTriggerListener.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Interfaces;

import java.util.EventListener;
import TK_Classes.DataTriggerEvent;

/**
 * <p><b>Title: DataTriggerListener </b></p>
 * <p><b>Description:</b></p>
 * A DataTriggerEvent listener for listening on DataTriggerEvents.  Contains one method triggerReceived(DataTriggerEvent dte);
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project B (V1.0)
 * @author Tom Kacperski
 */
public interface DataTriggerListener extends EventListener {
  public void triggerReceived (DataTriggerEvent dte);
}