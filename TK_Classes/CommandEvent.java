/*
 * @(#)CommandEvent.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Classes;

import java.awt.AWTEvent;
import javax.swing.JComponent;

/**
 * <p><b>Title: CommandEvent () </b></p>
 * <p><b>Description:</b></p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * CommandEvent is used for posting custom command events on panels that receive click or button press action.
 * This event helps calling environment to retrieve and act on such click or button events.
 * <p><b>Company: NONE </b></p>
 * @version Project B (V1.0)
 * @author Tom Kacperski
 */

public class CommandEvent extends AWTEvent {
  public CommandEvent (JComponent jc) {
    super (jc, COMMAND_EVENT);
  }
  public static final int COMMAND_EVENT = AWTEvent.RESERVED_ID_MAX + 10003;
}