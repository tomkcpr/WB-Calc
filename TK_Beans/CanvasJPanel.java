/*
 * @(#)CanvasJPanel.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;

import java.awt.*;
import javax.swing.JPanel;
import java.io.*;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

/**
 * <p><b>Title: CanvasJPanel () </b></p>
 * <p><b>Description:</b></p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * CanvasJPanel is the actual drawing class for graphs and axis.  A data object is passed
 * (Object[2][N]; N 1 - MAX_INT) and is converted to an array of integer points, through a ratio
 * of (panelWidth - 0) / (maxX - minX) or (panelHEight - 0) / (maxX - minX) that will be used to
 * plot the actual graph points when drawCurve() is called.
 * Axis are picked and drawn depending on the values stored in the data.  Smaller values will result
 * in smaller intervals being picked while larger values will result in larger intervals being picked.
 * drawn on the CanvasJPanel.
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski
 */
public class CanvasJPanel extends JPanel implements Serializable {
  private BorderLayout borderLayout1 = new BorderLayout();
  private Object [][] data = {};
  private int [][] intData = {/*{5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100},
      {90, 92, 94, 96, 98, 100, 102, 104, 106, 108, 110, 112, 114, 116, 118, 120, 122, 124, 126, 128, 130}*/};
  private int intPoints [][] = {};
  private double maxX = Double.MIN_VALUE;
  private double minX = Double.MAX_VALUE;
  private double maxY = Double.MIN_VALUE;
  private double minY = Double.MAX_VALUE;
  private double oldMaxX = Double.MIN_VALUE;
  private double oldMinX = Double.MAX_VALUE;
  private double oldMaxY = Double.MIN_VALUE;
  private double oldMinY = Double.MAX_VALUE;

  private int panelWidth = 0;
  private int panelHeight = 0;

  private double viewTOdataRatioX = 0.0;
  private double viewTOdataRatioY = 0.0;

  private int xAxisOffset = 0;
  private int yAxisOffset = 0;

  private final int ticPatternFull[] = {10, 2, 2, 2, 2, 6, 2, 2, 2, 2};  // 8 = Major, 4 = Minor, 6 = Half-Way
  private final int ticPatternHalf[] = {8, 4};
  private final int ticPatternMin[]  = {8};
  private int xTicPattern[] = ticPatternFull;
  private int yTicPattern[] = ticPatternFull;

  // Can be taken out....above arrays and equates do this.
  private boolean majorTicsX = true;
  private boolean mediumTicsX = true;
  private boolean minorTicsX = true;
  private boolean majorTicsY = true;
  private boolean mediumTicsY = true;
  private boolean minorTicsY = true;

  private int minorTicLength = 4;
  private int mediumTicLength = 6;
  private int majorTicLength = 8;

  private final double scaleValues[] = { 0.00001, 0.0001, 0.001, 0.01, 0.1, 1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0 };
  private final int xPixelMins[] = { 56, 48, 40, 32, 24, 16, 24, 32, 40, 48, 56};
  private final int yPixelMins[] = { 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16};

  private double xIncrement = 0.0;
  private double totalXIncrement = 0.0;
  private double yIncrement = 0.0;
  private double totalYIncrement = 0.0;

  private double unitTOscaleX = 0.0;
  private double unitTOscaleY = 0.0;

  // public void calcRatio() [Variables]
  private double xLowest = 0;
  private double xdL = 0.0;
  private double yLowest = 0;
  private double ydL = 0.0;
  private int xScale = 0;
  private int yScale = 0;

  // String plotting variables.
  private String s = new String ();
  private int x = 0;
  private int y = 0;
  private int centerX = 0;
  private int centerY = 0;
  private double xTicTotal = 0.0;
  private double yTicTotal = 0.0;

  // Panel graphic variables.
  private boolean clearRequested = false;
  private boolean axisDrawn = false;
  private NumberFormat formatter = NumberFormat.getNumberInstance();
  private Color darkgreen = new Color (30, 158, 30);

  private BufferedImage buffer;
  private Graphics2D bg2d;

  private Graphics2D g2d;

  private BufferedImage mainBuffer;
  private Graphics2D mg2d;

  public CanvasJPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    // Panel Listeners
    this.setLayout(borderLayout1);
    this.addComponentListener (new ComponentListener () {
      public void componentHidden(ComponentEvent e)  { }
      public void componentMoved(ComponentEvent e) { }
      public void componentResized(ComponentEvent e) {
        panelWidth = getWidth();
        panelHeight = getHeight();
        if (panelWidth < 64 || panelHeight < 64)
          return;
        if (clearRequested)
          clearGraph();
        else
          drawComponent ();
        repaint();
        //System.out.println ("panelWidth = " + panelWidth + ", panelHeight = " + panelHeight);
      }
      public void componentShown(ComponentEvent e)  { }
    });

    this.addMouseMotionListener(new MouseMotionListener () {
      public void mouseDragged (MouseEvent e) {
        drawIndicators (e);
      }

      public void mouseMoved (MouseEvent e) {
        drawIndicators (e);
      }
    });

    formatter.setMinimumIntegerDigits(1);
    formatter.setMinimumFractionDigits(0);
  }

  // ---------------------------------------------------------------------------
  // CUSTOM DRAW METHODS
  //
  // ---------------------------------------------------------------------------
  /**
   * Default paint component overriden to paint our graph.  The actual graph is drawn in
   * a buffer and is redrawn from this buffer.  There are no calculations done when
   * this panel is relocated on the screen, only when the panel is resized.
   * @param g Graphics object of this component.
   */
  public void paintComponent (Graphics g) {
    super.paintComponent (g);
    g2d = (Graphics2D)g;
    if (mainBuffer != null)
      g2d.drawImage(mainBuffer, 0, 0, this);
  }

  // ---------------------------------------------------------------------------
  // DRAWING METHODS
  //
  // (Size dependant methods.)
  // ---------------------------------------------------------------------------
  /**
   * Draws arrow indicators when arrow is dragged on screen.  They move along each axis showing
   * the respective X or Y selection.
   * @param e MouseEvent that is generated when mouse is dragged.
   */
  public void drawIndicators (MouseEvent e) {
  }

  /**
   * Clears the graph.  This method resets all relevant variables, then paitn and fills a rectangle
   * the size of this panel.
   */
  public void clearGraph () {
    //System.out.println ("[0]CLEARED");
    clearRequested = true;
    axisDrawn = false;
    if (panelWidth < 64 || panelHeight < 64)
      return;
    setupDrawSurface();
    data = null;
    intData = null;
    intPoints = null;
    maxX = Double.MIN_VALUE;
    minX = Double.MAX_VALUE;
    maxY = Double.MIN_VALUE;
    minY = Double.MAX_VALUE;
    oldMaxX = Double.MIN_VALUE;
    oldMinX = Double.MAX_VALUE;
    oldMaxY = Double.MIN_VALUE;
    oldMinY = Double.MAX_VALUE;
    //System.out.println ("[1]CLEARED");
  }

  /**
   * Convenience method called from calling environment.  This does all housekeeping, calculations
   *  and draws graph and curve to this panel.
   */
  public void drawComponent () {
    if (panelWidth < 64 || panelHeight < 64 || data == null)
      return;
    setupDrawSurface ();
    calcRatio ();
    setIntegerPoints();
    drawAxis();
    drawCurve();
    if (buffer != null)
      mg2d.drawImage(buffer, 0, 0, this);
  }

  /**
   * Sets up draw surface for rendering.  All relevant buffers are instantiated and cleared for
   * drawing.
   */
  public void setupDrawSurface () {
    // Graph Buffer
    if (buffer != null)
      buffer.flush();
    if (bg2d != null)
      bg2d.dispose();
    buffer = new BufferedImage ((int)panelWidth, (int)panelHeight, BufferedImage.TYPE_INT_RGB);
    bg2d = (Graphics2D)buffer.getGraphics();
    bg2d.setBackground(Color.white);
    bg2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    bg2d.fillRect(0, 0, panelWidth, panelHeight);

    // Main Buffer
    if (mainBuffer != null)
      mainBuffer.flush();
    if (mg2d != null)
      mg2d.dispose();
    mainBuffer = new BufferedImage ((int)panelWidth, (int)panelHeight, BufferedImage.TYPE_INT_RGB);
    mg2d = (Graphics2D)mainBuffer.getGraphics();
    mg2d.setBackground(Color.white);
    mg2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    mg2d.fillRect(0, 0, panelWidth, panelHeight);
  }

  /**
   * Calcualtes ratio required for correct axis and curve drawing.  These determine plotting, axis scales
   * and constants to be used for graph drawing.
   */
  public void calcRatio () {
    maxX = oldMaxX;
    minX = oldMinX;
    maxY = oldMaxY;
    minY = oldMinY;
    //System.out.println ("panelWidth = " + panelWidth + ", panelHeight = " + panelHeight);
    //System.out.println ("maxX = " + maxX + ", minX = " + minX + ", maxY = " + maxY + ", minY = " + minY);

    while (true) {
    // viewTOdataRatioX = (double)(maxX - minX) / (double)panelWidth;
      viewTOdataRatioX = (double)panelWidth / (maxX - minX);

    // X Axis
      for (int i = 0; i < scaleValues.length; i++) {
        if (viewTOdataRatioX * scaleValues[i] > xPixelMins[i]) {
          unitTOscaleX = viewTOdataRatioX * scaleValues[i];
          xScale = i;
          break;
        }
      }
      xdL = minX / scaleValues[xScale];
      xLowest = (double)((int)xdL) * scaleValues[xScale];
      if (xLowest < minX)
        minX = xLowest;
      else
        break;
    }

    //System.out.println ("drawAxis[0] --> unitTOscaleX = " + unitTOscaleX + ", xScale = " + xScale + ", scaleValues[xScale] = " + scaleValues[xScale]);
    while (true) {
      // viewTOdataRatioY = (double)(maxY - minY) / (double)panelHeight;
      viewTOdataRatioY = (double)panelHeight / (maxY - minY);

      // Y Axis
      for (int i = 0; i < scaleValues.length; i++) {
        if (viewTOdataRatioY * scaleValues[i] > yPixelMins[i]) {
          unitTOscaleY = viewTOdataRatioY * scaleValues[i];
          yScale = i;
          break;
        }
      }
      ydL = minY / scaleValues[yScale];
      yLowest = (double)((int)ydL) * scaleValues[yScale];
      if (yLowest < minY)
        minY = yLowest;
      else
        break;
    }

    //System.out.println ("drawAxis[3] --> unitTOscaleY = " + unitTOscaleY + ", yScale = " + yScale +", scaleValues[yScale] = " + scaleValues[yScale]);
    //System.out.println ("viewTOdataRatioX = " + viewTOdataRatioX + ", viewTOdataRatioY = " + viewTOdataRatioY);
  }

  /**
   * Method for converting the Object or String points to their double graph counterparts.  The method
   * converts each value and determines it's graph corresponding integer value.
   */
  public void setIntegerPoints () {
    //System.out.println ("setIntegerPoints[START]");
    double dV = 0.0;
    intData = new int [data.length][data[0].length];
    //System.out.println ("data.length = " + data.length + ", data[0].length = " + data[0].length);
    //System.out.println ("minY = " + minY + ", minX = " + minX);
    for (int i = 0; i < data.length; i++)
      for (int j = 0; j < data[0].length; j++) {
         try {
           if (i == 0) {
             dV = Double.parseDouble ((String)data[i][j]) - minY;
             //System.out.println ("[A]");
           } else {
             if (i == 1) {
               dV = Double.parseDouble ((String)data[i][j]) - minX;
               //System.out.println ("[B]");
             }
           }
         } catch (NumberFormatException nfe) { /*System.out.println ("setIntegerPoints () NumberFormatException!");*/ }
         //System.out.println("i = " + i + ", j = " + j);
         if (i == 0) {
           intData[i][j] = (int)(dV * viewTOdataRatioY + 0.5);
           //System.out.println ("dV * viewTOdataRatioY + 0.5 = " + (dV * viewTOdataRatioY + 0.5));
           //System.out.println ("intData[i][j] = " + intData[i][j]);
           //System.out.println ("(int)(dV * viewTOdataRatioY + 0.5) = " + (int)(dV * viewTOdataRatioY + 0.5));
         }
         if (i == 1) {
           intData[i][j] = (int)(dV * viewTOdataRatioX + 0.5);
           //System.out.println ("dV * viewTOdataRatioX + 0.5 = " + (dV * viewTOdataRatioX + 0.5));
           //System.out.println ("intData[i][j] = " + intData[i][j]);
           //System.out.println ("(int)(dV * viewTOdataRatioX + 0.5) = " + (int)(dV * viewTOdataRatioX + 0.5));
         }
      };
      //System.out.println ("setIntegerPoints[END]");
  }

  /**
   * Draws graph axis.  These are determined and computed, taking in various considerations, then drawn on
   * screen.  The axis drawing is dependent on the ratios derived from data to panel ratio.
   */
  public void drawAxis () {
    //System.out.println ("drawAxis[START]");
    //
    // X Axis
    //
    if (unitTOscaleX < 4) {
      xTicPattern = ticPatternMin;
      xIncrement = unitTOscaleX;
    } else
      if (unitTOscaleX < 20) {
        xTicPattern = ticPatternHalf;
        xIncrement = unitTOscaleX * 0.5;
      } else
        if (unitTOscaleX >= 20) {
          xTicPattern = ticPatternFull;
          xIncrement = unitTOscaleX * 0.1;
        }
    //System.out.println ("drawAxis[1] --> xIncrement = " + xIncrement + ", xTicPattern = " + xTicPattern.toString());

    // TopY / (TopY + BottomY)
    if (maxX > minX && minX < 0) {
      xAxisOffset = (int)(.5 + (0.0 - minX) / (maxX - minX) * (double)panelWidth);
      centerX = 0;
    } else
      if (maxX > minX && minX >= 0) {
        xAxisOffset = 0;
        centerX = 1;
      } else
        if (maxX > minX && maxX < 0) {
          xAxisOffset = panelWidth;
          centerX = -1;
        } else
         if (maxX < minX)
           return;

    //System.out.println ("drawAxis[2] --> xAxisOffset = " + xAxisOffset);

    //
    // Y Axis
    //
    if (unitTOscaleY < 4) {
      yTicPattern = ticPatternMin;
      yIncrement = unitTOscaleY;
    } else
      if (unitTOscaleY < 20) {
        yTicPattern = ticPatternHalf;
        yIncrement = unitTOscaleY * 0.5;
      } else
        if (unitTOscaleY >= 20) {
          yTicPattern = ticPatternFull;
          yIncrement = unitTOscaleY * 0.1;
        }
    //System.out.println ("drawAxis[4] --> yIncrement = " + yIncrement + ", yTicPattern = " + yTicPattern.toString());

    // TopY / (TopY + BottomY)
    if (maxY > minY && minY < 0) {
      yAxisOffset = (int)(0.5 + (maxY - 0.0) / (maxY - minY) * (double)panelHeight);
      centerY = 0;
    } else
      if (maxY > minY && minY >= 0) {
        yAxisOffset = panelHeight;
        centerY = 1;
      } else
        if (maxY > minY && maxY < 0) {
          yAxisOffset = 0;
          centerY = -1;
        } else
          if (maxY < minY) {
            return;
          }
    //System.out.println ("drawAxis[5] --> yAxisOffset = " + yAxisOffset);

    // ----------------
    // X Axis Drawing
    // ----------------
    //String test = new String();
    //System.out.println("test.valueOf(5.0).length() = " + test.valueOf(5.0).length());
    boolean westDrawn = false;
    boolean eastDrawn = false;
    int xTic = 0;
    int xPixelValue = 0;
    int xTicSide = (panelHeight - yAxisOffset < xTicPattern[0]) ? -1 : 1;
    int xNumberSide = ((panelHeight - yAxisOffset) < (xTicPattern[0] + 12)) ? 0 : 1;
    //System.out.println ("xTicSide = " + xTicSide + ", xNumberSide = " + xNumberSide);
    double nextTicNum = 0.0;
    totalXIncrement = 0.0;
    bg2d.setColor(Color.black);
    xTicTotal = 0.0;
    //System.out.println ("xTicPattern.length = " + xTicPattern.length + ", centerX = " + centerX);
    while (!westDrawn || !eastDrawn) {
      //System.out.println ("[I] --> totalXIncrement = " + totalXIncrement + ", xPixelValue = " + xPixelValue);
      if (!westDrawn)
        bg2d.drawLine(xAxisOffset - xPixelValue, yAxisOffset,
                      xAxisOffset - xPixelValue, yAxisOffset + (xTicPattern[xTic] * xTicSide));

      if (!eastDrawn)
        bg2d.drawLine(xAxisOffset + xPixelValue, yAxisOffset,
                      xAxisOffset + xPixelValue, yAxisOffset + (xTicPattern[xTic] * xTicSide));

      totalXIncrement += xIncrement;
      xPixelValue = (int)(totalXIncrement + 0.5);
      xTic++;

      if (xTic >= xTicPattern.length) {
        //System.out.println ("xTic = " + xTic);
        if (xTicSide == -1) {
          y = -1 - xTicPattern[0];
        } else
          if (xNumberSide == 0) {
            y = -1;
          } else
            if (xNumberSide == 1) {
              y = xTicPattern[0] + 12;
            }

        xTicTotal += scaleValues[xScale];
        if (centerX == 0) {
          if (!westDrawn) {
            x = (int)(0.5 + (double)s.valueOf(formatter.format(xTicTotal * -1)).length() * 4.0);   // 4.0 = 8.0 * 0.5
            if (xAxisOffset - xPixelValue - x >= 0)
              bg2d.drawString(s.valueOf(formatter.format(xTicTotal * -1)), xAxisOffset - xPixelValue - x, yAxisOffset + y);
          }
          if (!eastDrawn) {
            x = (int)(0.5 + (double)s.valueOf(formatter.format(xTicTotal)).length() * 4.0);
            if (xAxisOffset + xPixelValue + x <= panelWidth)
              bg2d.drawString (s.valueOf(formatter.format(xTicTotal)), xAxisOffset + xPixelValue - x, yAxisOffset + y);
          }
        } else
          if (centerX == -1) {
            x = (int)(0.5 + (double)s.valueOf(formatter.format(minX - xTicTotal)).length() * 4.0);
            if (xAxisOffset - xPixelValue - x >= 0)
              bg2d.drawString(s.valueOf(formatter.format(minX - xTicTotal)), xAxisOffset - xPixelValue - x, yAxisOffset + y);
          } else
            if (centerX == 1) {
              x = (int)(0.5 + (double)s.valueOf(formatter.format(minX + xTicTotal)).length() * 4.0);
              if (xAxisOffset + xPixelValue + x <= panelWidth)
                bg2d.drawString(s.valueOf(formatter.format(minX + xTicTotal)), xAxisOffset + xPixelValue - x, yAxisOffset + y);
            }
        xTic = 0;
      }

      if (xAxisOffset < xPixelValue) {
        westDrawn = true;
        //System.out.println ("westDrawn = " + westDrawn);
      }
      if (panelWidth - xAxisOffset < xPixelValue) {
        eastDrawn = true;
        //System.out.println ("eastDrawn = " + eastDrawn);
      }
    }
    //System.out.println ("drawAxis[6]");

    // -----------------
    // Y Axis Drawing
    // -----------------
    boolean northDrawn = false;
    boolean southDrawn = false;
    int yTic = 0;
    int yPixelValue = 0;
    int yTicSide = (xAxisOffset < 8) ? 1 : -1;
    int yNumberLength = ((s.valueOf (formatter.format(scaleValues[yScale])).length() + 2) * 8) + yTicPattern[0];
    int yNumberSide = (xAxisOffset < (yNumberLength)) ? 0 : 1;
    bg2d.setColor(Color.black);
    totalYIncrement = 0.0;
    yTicTotal = 0.0;
    //System.out.println ("yTicPattern.length = " + yTicPattern.length);
    while (!northDrawn || !southDrawn) {
      if (!northDrawn)
        bg2d.drawLine(xAxisOffset, yAxisOffset - yPixelValue,
                      xAxisOffset + (yTicPattern[yTic] * yTicSide), yAxisOffset - yPixelValue);

      if (!southDrawn)
        bg2d.drawLine(xAxisOffset, yAxisOffset + yPixelValue,
                      xAxisOffset + (yTicPattern[yTic] * yTicSide), yAxisOffset + yPixelValue);

      totalYIncrement += yIncrement;
      yPixelValue = (int)(totalYIncrement + 0.5);

      yTic++;
      if (yTic >= yTicPattern.length) {
        if (yNumberSide == 0 && yTicSide == -1) {
          x = 1;
        } else
          if (yNumberSide == 0 && yTicSide == 1) {
            x = yTicPattern[0] + 1;
          } else
            if (yNumberSide == 1) {
              x = -yNumberLength;
            }

        yTicTotal += scaleValues[yScale];
        y = 4;
        if (centerY == 0) {
          if (!northDrawn)
            if (yAxisOffset - yPixelValue - y >= 0)
              bg2d.drawString(s.valueOf(formatter.format(yTicTotal)), xAxisOffset + x, yAxisOffset - yPixelValue + y);
          if (!southDrawn)
            if (yAxisOffset + yPixelValue + y <= panelHeight)
              bg2d.drawString(s.valueOf(formatter.format(-yTicTotal)), xAxisOffset + x, yAxisOffset + yPixelValue + y);
        } else
          if (centerY == 1) {
            if (yAxisOffset - yPixelValue - y >= 0)
              bg2d.drawString(s.valueOf (s.valueOf(formatter.format(minY + yTicTotal))), xAxisOffset + x, yAxisOffset - yPixelValue + y);
          } else
            if (centerY == -1) {
              if (yAxisOffset + yPixelValue + y <= panelHeight)
                bg2d.drawString (s.valueOf(formatter.format(minY - yTicTotal)), xAxisOffset + x, yAxisOffset + yPixelValue + y);
            }
        yTic = 0;
      }

      if (yAxisOffset < yPixelValue) {
        northDrawn = true;
        //System.out.println ("northDrawn = true");
      }
      if ((yPixelValue + yAxisOffset) > panelHeight) {
        southDrawn = true;
        //System.out.println ("southDrawn = true");
      }
      //System.out.println ("[I] --> totalYIncrement = " + totalYIncrement + ", yPixelValue = " + yPixelValue);
    }

    //System.out.println ("drawAxis[7]");
    //System.out.println ("drawAxis[END]");
    axisDrawn = true;
  }

  /**
   * Draws a curve on this canvas JPanel corresponding to data array passed as
   * value earlier.
   */
  public void drawCurve () {
    bg2d.setColor(darkgreen);
    int next = 0;
    int oldX = 0;
    int oldY = 0;
    for (int i = 0; i < intData[0].length; i++) {
      if (next >= 1)
        bg2d.drawLine(intData[1][i], panelHeight - intData[0][i], oldX, oldY);
      bg2d.fillOval(intData[1][i] - 3, panelHeight - intData[0][i] - 3, 6, 6);
      oldX = intData[1][i];
      oldY = panelHeight - intData[0][i];
      next++;
    }
  }

  // ---------------------------------------------------------------------------
  // Sets data and determines maximums and minimums for each axis.
  //
  // (Size independent methods.)
  // ---------------------------------------------------------------------------
  /**
   * Sets X Axis and Y Axis data for this canvas.  An array of Object[2][N] is passed from
   * which minimums and maximums are found and set.  These values are then used to set find
   * accurate ratios and plot the graph.
   * @param d An Object[2][N] array where N = MAX_INT
   * @return
   */
  public boolean setData (Object[][] d) {
    //NumberFormat f = NumberFormat.getInstance();
    //f.setMinimumFractionDigits(1);
    //f.setMinimumIntegerDigits(1);
    //System.out.println ("f.format(\"1,570.02\") = " + f.format("1,570.02"));
    double doubleValue = 0;
    if (d.length < 2)
      return false;
    data = d;

    maxX = Double.MIN_VALUE;
    minX = Double.MAX_VALUE;
    maxY = Double.MIN_VALUE;
    minY = Double.MAX_VALUE;

    for (int i = 0; i < data[0].length; i++) {
      // Y Axis values.
      doubleValue = 0;
      try {
        doubleValue = Double.parseDouble ((String)data[0][i]);
      } catch (NumberFormatException nfe) {
          //System.out.println ("false returned! (String)data[0][i] = " + (String)data[0][i]); return false;
      }
      //System.out.println ("doubleValue [y]= " + doubleValue);
      if (maxY < doubleValue) maxY = doubleValue;
      if (minY > doubleValue) minY = doubleValue;

      // X Axis values.
      doubleValue = 0;
      try {
        doubleValue = Double.parseDouble((String)data[1][i]);
      } catch (NumberFormatException nfe) {
          //System.out.println ("false returned! (String)data[1][i] = " + (String)data[1][i]); return false;
      }
      //System.out.println ("doubleValue [x]= " + doubleValue);
      if (maxX < doubleValue) maxX = doubleValue;
      if (minX > doubleValue) minX = doubleValue;
    }
    //System.out.println ("maxX = " + maxX + ", minX = " + minX + ", maxY = " + maxY + ", minY = " + minY);
    oldMaxX = maxX;
    oldMinX = minX;
    oldMaxY = maxY;
    oldMinY = minY;
    clearRequested = false;
    return true;
  }

  public Object[][] getData () {
    return data;
  }

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }

  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}