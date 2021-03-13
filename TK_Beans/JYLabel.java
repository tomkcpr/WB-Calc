/*
 * @(#)JYLabel.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;
/**
 * <p><b>Title: JYLabel </b></p>
 * <p><b>Description:</b></p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * An extension of JLabel where text can be displayed vertically as well as horizontally.
 * <p><b>Company: NONE </b></p>
 * @version Project B (V1.0)
 * @author Tom Kacperski
 */

import java.awt.*;
import javax.swing.JLabel;
import java.awt.geom.AffineTransform;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.io.*;

public class JYLabel extends JLabel implements Serializable {
  Graphics2D g2d = null;
  String text = "Hello!";

  /**
   * Default constructor.
   */
  public JYLabel () {
    this.addComponentListener (new ComponentListener () {
      public void componentHidden(ComponentEvent e)  { }
      public void componentMoved(ComponentEvent e) { }
      public void componentResized(ComponentEvent e) {
        repaint();
      }
      public void componentShown(ComponentEvent e)  {
        repaint();
      }
    });
  }

  /**
   * JLabel paint method overridden to draw text vertically.
   * @param g This labels Graphics object.
   */
  public void paintComponent (Graphics g) {
    super.paintComponent(g);
    g2d = (Graphics2D)g;
    g2d.translate((double)getWidth() * 0.5, (double)getHeight() * 0.5);
    g2d.rotate(-1.5707);

    g2d.setColor(Color.black);
    g2d.drawString (text, (int)(((text.length() * 8)) * -0.5), 6);

    //System.out.println ("getWidth () = " + ((double)getWidth() * 0.5) + ", getHeight() = " + ((double)(getHeight() * 0.5)));

    g2d.rotate(1.5707);
    g2d.translate((double)-getWidth() * 0.5, (double)-getHeight() * 0.5);
  }

  /**
   * Returns the current label that is drawn by this component.
   * @return Label text.
   */
  public String getLabel() {
    return text;
  }

  /**
   * Sets a label to draw vertically for this JYLabel component.
   * @param s The label string.
   */
  public void setLabel (String s) {
    text = s;
    repaint();
  }

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }

};
