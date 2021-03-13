/*
 * @(#)CommandListener.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Interfaces;

import java.util.EventListener;
import TK_Classes.CommandEvent;

/**
 * <p><b>Title: CommandListener () </b></p>
 * <p><b>Description:</b></p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * A CommandListener interface with one method to act on CommandEvents.
 * <p><b>Company: NONE </b></p>
 * @version Project B (V1.0)
 * @author Tom Kacperski
 */
public interface CommandListener extends EventListener {
  public void commandGiven (CommandEvent ce);
}