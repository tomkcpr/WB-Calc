/*
 * @(#)DataChangedListener.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Interfaces;

import java.util.EventListener;
import TK_Classes.DataChangedEvent;

/**
 * <p><b>Title: CalculateListener() </b></p>
 * <p><b>Description:</b></p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * Interface for processing {@link DataChangedEvent} events.
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public interface DataChangedListener extends EventListener {
  public void dataChanged(DataChangedEvent dce);
}