/*
 * @(#)WeightScaleSimulator.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package ProjectC;

import javax.swing.UIManager;
import java.awt.*;

/**
 * <p><b>Title: WeightScaleSimulator () </b></p>
 * <p><b>Description:</b></p>
 * Base class for this application containing <code>main</code> method and instantiating an object of WeightScaleSimulatorFrame from which
 * application runs.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class WeightScaleSimulator {
  private boolean packFrame = false;

  //Construct the application
  public WeightScaleSimulator() {
    WeightScaleSimulatorFrame frame = new WeightScaleSimulatorFrame();
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame) {
      frame.pack();
    }
    else {
      frame.validate();
    }
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }
  //Main method
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    new WeightScaleSimulator();
  }
}