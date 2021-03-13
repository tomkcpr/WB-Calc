/*
 * @(#)GraphJPanel.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;
/**
 * <p><b>Title: GraphJPanel </b></p>
 * <p><b>Description:</b></p>
 * GraphJPanel class for drawing graphs along with their axes, curves titles and axis titles.  An array
 * of data objects (Object[N][M]) is passed where N & M <= MAX_INT;
 * Selections are then made from X and Y combo boxes selecting which column of data is plotted against another.
 * GraphJPanel requires an array of headings and a corresponding two dimensional array of values to draw from.
 * Individual parameters can be set through set and get methods.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project B (V1.0)
 * @author Tom Kacperski
 */


import java.awt.*;
import java.io.*;
import com.borland.jbcl.layout.*;
import javax.swing.*;
import javax.swing.border.*;
import TK_Beans.CanvasJPanel;
import TK_Beans.JYLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GraphJPanel extends JPanel implements Serializable {
  private JYLabel yAxisJLabel = new JYLabel();
  private JLabel titleJLabel = new JLabel();
  private JLabel xAxisJLabel = new JLabel();
  private CanvasJPanel canvasJPanel = new CanvasJPanel();

  private Object [][] graphData = null;
  private Object [] graphHeadings = null;

  private Object [][] canvasData = null;

/*  private Object [][] TvsRd = {
    {"-5", "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90",
    "95", "100"},
      {"88", "90", "92", "94", "96", "98", "100", "102", "104", "106", "108", "110", "112", "114", "116", "118", "120", "122",
    "124", "126", "128", "130"}};
  private Object [][] TvsVa = {
    {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90",
    "95", "100"},
    {"4.736", "4.792", "4.845", "4.898", "4.949", "5.00", "5.050", "5.098", "5.146", "5.192", "5.238", "5.283",
  "5.327", "5.370", "5.413", "5.455", "5.495", "5.536", "5.575", "5.614", "5.652"}};

  private Object [][] TvsVd = {
    {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90",
    "95", "100"},
    {"0.264", "0.208", "0.155", "0.102", "0.051", "0.0", "-0.050", "-0.098", "-0.146", "-0.192", "-0.238", "-0.283",
  "-0.327", "-0.370", "-0.413", "-0.455", "-0.495", "-0.536", "-0.575", "-0.614", "-0.652"}};

  private Object[][] GvsG = {{ "-9", "-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5"},
  { "-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5",  "9", "8", "7", "6"}}; */

  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JPanel axisSelectionJPanel = new JPanel();
  private JPanel xAxisJPanel = new JPanel();
  private JPanel yAxisJPanel = new JPanel();
  private JComboBox xAxisJComboBox = new JComboBox();
  private JComboBox yAxisJComboBox = new JComboBox();

  private JPanel axisSelectionFillerJPanel = new JPanel();

  private TitledBorder titledBorder1;
  private TitledBorder titledBorder2;
  private TitledBorder titledBorder3;
  private Border border1;
  private Border border2;
  private Border border3;
  private Border border4;
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private BorderLayout borderLayout3 = new BorderLayout();
  private JLabel graphTitleJLabel = new JLabel();
  private String title = "NONE";
  private JLabel bottomLeftJLabel = new JLabel();
  private boolean beingSetup = false;

  public GraphJPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    border2 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(134, 134, 134));
    titledBorder1 = new TitledBorder(border2,"Axis Selection");
    border3 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder2 = new TitledBorder(border3,"X Axis");
    border4 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder3 = new TitledBorder(border4,"Y Axis");

    canvasJPanel.setToolTipText("Data Graph of circuit.");

    yAxisJLabel.setBackground(new Color(242, 236, 210));
    yAxisJLabel.setOpaque(true);
    yAxisJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    yAxisJLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    yAxisJLabel.setLabel ("Y");
    yAxisJLabel.setToolTipText("Graph 'Y' axis");

    titleJLabel.setBackground(new Color(242, 236, 210));
    titleJLabel.setOpaque(true);
    titleJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleJLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    titleJLabel.setText ("Y vs X");
    titleJLabel.setToolTipText("Graph Title");

    xAxisJLabel.setBackground(new Color(242, 236, 210));
    xAxisJLabel.setOpaque(true);
    xAxisJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    xAxisJLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    xAxisJLabel.setText ("X");
    xAxisJLabel.setToolTipText("Graph 'X' axis");

    bottomLeftJLabel.setBackground(new Color(242, 236, 210));
    bottomLeftJLabel.setToolTipText("Graph 'X' axis");
    bottomLeftJLabel.setOpaque(true);

    //canvasJPanel.setBackground(Color.white);
    //canvasJPanel.setData(GvsG);
    //canvasJPanel.drawComponent();
    //canvasJPanel.setData(TvsVd);
    //canvasJPanel.drawComponent();
    //canvasJPanel.setData(TvsVa);
    //canvasJPanel.drawComponent();
    //canvasJPanel.setData(TvsRd);
    //canvasJPanel.drawComponent();
    clearGraph();


    xAxisJPanel.setBorder(titledBorder2);
    xAxisJPanel.setLayout(borderLayout2);
    xAxisJPanel.add(xAxisJComboBox, BorderLayout.CENTER);
    xAxisJPanel.setToolTipText("Select data array to plot on X Axis");

    yAxisJPanel.setBorder(titledBorder3);
    yAxisJPanel.setLayout(borderLayout1);
    yAxisJPanel.add(yAxisJComboBox, BorderLayout.CENTER);
    yAxisJPanel.setToolTipText("Select data array to plot on Y Axis");

    xAxisJComboBox.setMinimumSize(new Dimension (0, 24));
    xAxisJComboBox.setPreferredSize(new Dimension (64, 24));
    xAxisJComboBox.addActionListener(new CanvasAction());
    xAxisJComboBox.setToolTipText("Select data array to plot on X Axis");

    yAxisJComboBox.setMinimumSize(new Dimension (0, 24));
    yAxisJComboBox.setPreferredSize(new Dimension (64, 24));
    yAxisJComboBox.addActionListener(new CanvasAction());
    yAxisJComboBox.setToolTipText("Select data array to plot on Y Axis");

    axisSelectionFillerJPanel.setLayout(borderLayout3);
    axisSelectionFillerJPanel.setMinimumSize(new Dimension (0, 24));
    axisSelectionFillerJPanel.setPreferredSize(new Dimension (16, 24));

    graphTitleJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    graphTitleJLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    graphTitleJLabel.setText(title);
    graphTitleJLabel.setToolTipText("The circuit this data represents (" + title + " circuit.)");

    axisSelectionJPanel.setBackground(Color.lightGray);
    axisSelectionJPanel.setBorder(titledBorder1);
    axisSelectionJPanel.setLayout(gridBagLayout2);
    axisSelectionJPanel.setToolTipText("Select X & Y axis to plot vs each other.");

    axisSelectionJPanel.add(xAxisJPanel,  new GridBagConstraints(0, 0, 1, 1, 0.3, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    axisSelectionJPanel.add(yAxisJPanel,  new GridBagConstraints(1, 0, 1, 1, 0.3, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    axisSelectionJPanel.add(axisSelectionFillerJPanel,  new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    axisSelectionFillerJPanel.add(graphTitleJLabel, BorderLayout.CENTER);

    this.setLayout(gridBagLayout1);
    this.add(bottomLeftJLabel, new GridBagConstraints(0, 3, 1, 1, 0.04, 0.015
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
    this.add(yAxisJLabel,   new GridBagConstraints(0, 1, 1, 2, 0.04, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
    this.add(titleJLabel,   new GridBagConstraints(0, 0, 2, 1, 1.0, 0.015
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.add(xAxisJLabel,   new GridBagConstraints(1, 2, 1, 2, 1.0, 0.015
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
    this.add(canvasJPanel,   new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));
    this.add(axisSelectionJPanel,                 new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }

  private class CanvasAction implements ActionListener {
    public void actionPerformed (ActionEvent ae) {
      if (!beingSetup)
        drawGraph (yAxisJComboBox.getSelectedIndex(), xAxisJComboBox.getSelectedIndex());
    }
  }

  public void setTitle (String t) {
    title = t;
    graphTitleJLabel.setText(title);
  }

  public String getTitle () {
    return title;
  }

  public void drawGraph (int y, int x) {
    //System.out.println ("xAxisJComboBox.getItemCount() = " + xAxisJComboBox.getItemCount());
    if (y < 0 || x < 0 || y == yAxisJComboBox.getItemCount() || x == xAxisJComboBox.getItemCount())
      return;
    canvasData = null;
    //System.out.println("graphData = " + graphData);
    canvasData = new Object[2][graphData[0].length];
    for (int i = 0; i < graphData[0].length; i++) {
      canvasData[0][i] = graphData[y][i];
      canvasData[1][i] = graphData[x][i];
    }

    canvasJPanel.setData(canvasData);
    canvasJPanel.drawComponent();
    titleJLabel.setText(graphHeadings[y] + " vs " + graphHeadings[x]);
    yAxisJLabel.setLabel((String)graphHeadings[y]);
    xAxisJLabel.setText((String)graphHeadings[x]);
    canvasJPanel.validate();
    canvasJPanel.updateUI();
  }

  public void setHeadings (Object[] h) {
    beingSetup = true;
    graphHeadings = new Object[h.length];
    yAxisJComboBox.removeAllItems();
    xAxisJComboBox.removeAllItems();
    //System.out.println ("graphHeadings.length = " + graphHeadings.length);
    for (int i = 0; i < h.length; i++) {
      graphHeadings[i] = h[i];
      //System.out.println ("[H] = " + graphHeadings[i]);
      yAxisJComboBox.addItem(graphHeadings[i]);
      xAxisJComboBox.addItem(graphHeadings[i]);
    }
    beingSetup = false;
  }

  public void setData (Object[][] d) {
    beingSetup = true;
    //System.out.println ("d.length = " + d.length);           // 10
    //System.out.println ("d[0].length = " + d[0].length);     // 3
    //System.out.println ("____________________________");
    graphData = new Object[d[0].length][d.length];
    for (int j = 0; j < d[0].length; j++) {
      for (int i = 0; i < d.length; i++) {
        graphData[j][i] = d[i][j];
        //System.out.print(graphData[j][i] + "   ");
      };
      //System.out.println ();
    }
    beingSetup = false;
    //System.out.println ("____________________________");
  }

  public void clearGraph () {
    yAxisJLabel.setLabel("Y");
    xAxisJLabel.setText("X");
    titleJLabel.setText("Y vx X");
    graphTitleJLabel.setText("NONE");
    yAxisJComboBox.removeAllItems();
    xAxisJComboBox.removeAllItems();
    canvasJPanel.clearGraph();
    canvasJPanel.validate();
    canvasJPanel.updateUI();
  }

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }

  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}