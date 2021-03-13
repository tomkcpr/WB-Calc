/*
 * @(#)CalculateListener.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Interfaces;

import java.util.EventListener;
import TK_Classes.CalculateEvent;

/**
 * <p><b>Title: CalculateListener() </b></p>
 * <p><b>Description:</b></p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * Interface for processing {@link CalculateEvent} events.
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public interface CalculateListener extends EventListener {
  public void dataCalculated (CalculateEvent ce);
}