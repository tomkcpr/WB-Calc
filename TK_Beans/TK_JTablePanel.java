/*
 * @(#)TK_JTablePanel.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package TK_Beans;

import java.awt.*;
import com.borland.jbcl.layout.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.EventObject;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.table.TableColumn;
import java.io.*;

/**
 * <p><b>Title: TK_JTablePanel () </b></p>
 * <p><b>Description:</b></p>
 * <p> An output class for displaying data from circuit, in GUI / tabular form.  Data is received in the form of a two
 * dimensional array of Objects[][] and one dimensional array of heading Objects[] ({@link Object}).  This class is
 * used exclusively for displaying data. Calculations object arrays from {@link CalculationsJPanel} are sent to this
 * class for display. Resistance and power output is set or retrieved through setters and getters respectively. </p>
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski
 */
public class TK_JTablePanel extends JPanel implements Serializable {
  private JPanel calcResultsJPanel = new JPanel();
  private JLabel feedBackResistorJLabel = new JLabel();
  private JPanel circuitTableDataJPanel = new JPanel();
  private JLabel powerDissipationJLabel = new JLabel();
  private TitledBorder circuitComponentResultsTitledBorder;
  private JLabel feedBackTextJLabel = new JLabel();
  private JLabel powerTextJLabel = new JLabel();
  private Border powerBorder;
  private Border feedbackResistorBorder;
  private Border feedBackResistorNameBorder;
  private Border powerNameBorder;
  private TitledBorder circuitTableDataTitledBorder;
  private JScrollPane circuitTableDataJScrollPane = new JScrollPane();
  private BorderLayout borderLayout1 = new BorderLayout();

  // -------------------------------------------------------------------------
  // INNER DATA
  //
  //
  // -------------------------------------------------------------------------
  private NumberFormat formatter = NumberFormat.getNumberInstance(new Locale ("en", "ca"));
  private Object finalTableHeadings[] = {"#","Column 1","Column 2","Column 3"};
  private Object finalTableData[][] = {};
  private JTable circuitTableDataJTable = new JTable(new DefaultTableModel (finalTableData, finalTableHeadings));

  private double feedbackResistor = 0.0;
  private double powerDissipation = 0.0;
  private String circuitTitle = "NONE";
  private String powerText = "POWER";
  private String feedbackResistorText = "Rf (K\u03A9)";
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JPanel leftFillJPanel = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private BorderLayout leftFillJPanelBorderLayout = new BorderLayout();
  private JLabel circuitTitleJLabel = new JLabel();

  /**
   * Creates a default object of this class.  This object is empty at instantiation with only headings preset
   * and primitives set to 0.
   */
  public TK_JTablePanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    // -------------------------------------------------------------------------
    // VARIABLE INITIALIZATION
    //
    //
    // -------------------------------------------------------------------------
    circuitTableDataJTable.setRowHeight(24);
    setColumnWidth (64, 192, 0);

    formatter.setMinimumFractionDigits(1);
    formatter.setMaximumFractionDigits(2);
    formatter.setMinimumIntegerDigits(1);

    // -------------------------------------------------------------------------
    // BORDERS
    //
    //
    // -------------------------------------------------------------------------
    circuitComponentResultsTitledBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.gray,Color.white),"Circuit Component Results");
    powerBorder = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    feedbackResistorBorder = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    feedBackResistorNameBorder = BorderFactory.createLineBorder(Color.white,1);
    powerNameBorder = BorderFactory.createLineBorder(Color.white,1);
    circuitTableDataTitledBorder = new TitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,new Color(134, 134, 134),new Color(93, 93, 93),Color.white,Color.white),"Circuit Table Data");

    // -------------------------------------------------------------------------
    // COMPONENTS
    //
    //
    // -------------------------------------------------------------------------
    feedBackResistorJLabel.setBackground(SystemColor.menu);
    feedBackResistorJLabel.setBorder(feedbackResistorBorder);
    feedBackResistorJLabel.setOpaque(true);
    feedBackResistorJLabel.setToolTipText("Shows required feedback resistor value under chosen parameters.");
    feedBackResistorJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    //feedBackResistorJLabel.setPreferredSize(new Dimension(128, 32));
    //feedBackResistorJLabel.setMaximumSize(new Dimension(256, 64));
    //feedBackResistorJLabel.setMinimumSize (new Dimension (64, 16));
    feedBackResistorJLabel.setText ("" + formatter.format(feedbackResistor));

    powerDissipationJLabel.setBackground(SystemColor.menu);
    powerDissipationJLabel.setBorder(powerBorder);
    powerDissipationJLabel.setOpaque(true);
    powerDissipationJLabel.setToolTipText("Power dissipation of gage.");
    powerDissipationJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    //powerDissipationJLabel.setPreferredSize(new Dimension(128, 32));
    //powerDissipationJLabel.setMaximumSize(new Dimension(256, 64));
    //powerDissipationJLabel.setMinimumSize (new Dimension (64, 16));
    powerDissipationJLabel.setText("" + formatter.format (powerDissipation));

    feedBackTextJLabel.setBackground(Color.gray);
    feedBackTextJLabel.setBorder(feedBackResistorNameBorder);
    feedBackTextJLabel.setOpaque(true);
    feedBackTextJLabel.setToolTipText("Shows required feedback resistor for this circuit on right.");
    feedBackTextJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    feedBackTextJLabel.setText(feedbackResistorText);
    //feedBackTextJLabel.setPreferredSize(new Dimension(128, 32));
    //feedBackTextJLabel.setMaximumSize(new Dimension(256, 64));
    //feedBackTextJLabel.setMinimumSize (new Dimension (64, 16));

    powerTextJLabel.setBackground(Color.gray);
    powerTextJLabel.setBorder(powerNameBorder);
    powerTextJLabel.setOpaque(true);
    powerTextJLabel.setToolTipText("Shows power dissipation of gage on right.");
    powerTextJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    powerTextJLabel.setText(powerText);
    //powerJLabel.setPreferredSize(new Dimension(128, 32));
    //powerNameJLabel.setMaximumSize(new Dimension(256, 64));
    //powerJLabel.setMinimumSize (new Dimension (64, 16));

    calcResultsJPanel.setBackground(Color.lightGray);
    calcResultsJPanel.setBorder(circuitComponentResultsTitledBorder);
    calcResultsJPanel.setToolTipText("Additional required circuit specifications required for chosen circuit inputs.");
    calcResultsJPanel.setLayout(gridBagLayout2);

    circuitTableDataJPanel.setBackground(Color.lightGray);
    circuitTableDataJPanel.setBorder(circuitTableDataTitledBorder);
    circuitTableDataJPanel.setLayout(borderLayout1);
    circuitTableDataJPanel.add(circuitTableDataJScrollPane, BorderLayout.CENTER);

    circuitTableDataJTable.setToolTipText("Displayed result data (If any) from calculations.");
    circuitTableDataJTable.getTableHeader().setToolTipText("Displayed result data (If any) from calculations.");
    circuitTableDataJScrollPane.setToolTipText("Displayed result data (If any) from calculations.");
    circuitTableDataJPanel.setToolTipText("Displayed result data (If any) from calculations.");

    circuitTableDataJScrollPane.getViewport().setBackground(Color.white);
    circuitTableDataJScrollPane.getViewport().add(circuitTableDataJTable);

    //leftFillJPanel.setPreferredSize(new Dimension(256, 32));
    //leftFillJPanel.setMaximumSize(new Dimension(128, 64));
    //leftFillJPanel.setMinimumSize (new Dimension (64, 16));
    leftFillJPanel.setLayout(leftFillJPanelBorderLayout);
    leftFillJPanel.add(circuitTitleJLabel, BorderLayout.CENTER);

    circuitTitleJLabel.setHorizontalAlignment(SwingConstants.CENTER);
    circuitTitleJLabel.setText(circuitTitle);

    calcResultsJPanel.add(feedBackResistorJLabel,  new GridBagConstraints(4, 0, 1, 1, 0.15, 0.33
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    calcResultsJPanel.add(feedBackTextJLabel,  new GridBagConstraints(3, 0, 1, 1, 0.15, 0.20
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    calcResultsJPanel.add(powerDissipationJLabel,  new GridBagConstraints(2, 0, 1, 1, 0.15, 0.33
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    calcResultsJPanel.add(powerTextJLabel,   new GridBagConstraints(1, 0, 1, 1, 0.15, 0.20
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    calcResultsJPanel.add(leftFillJPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.33
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    this.setLayout(gridBagLayout1);
    this.add(calcResultsJPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.1
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.add(circuitTableDataJPanel,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
  }

  // ---------------------------------------------------------------------------
  // METHODS
  //
  //
  // ---------------------------------------------------------------------------
  /**
   * Sets the feedback resistor in instance field of this class.
   * @param fR The feedback resistor value to set field and GUI with.
   */
  // GET & SET feedbackResistor.
  public void setfeedbackResistor (double fR) {
    feedbackResistor = fR;
    feedBackResistorJLabel.setText("" + formatter.format (feedbackResistor));
  }

  /**
   * Returns the feedback resistor that is displayed by this class.
   * @return The feedback resistor value in this class.
   */
  public double getfeedbackResistor () {
    return feedbackResistor;
  }

  /**
   * Sets the power dissipation instance field of this class.
   * @param pD The power dissipation value to set field and GUI with.
   */
  // GET & SET power
  public void setPowerDissipation (double pD) {
    powerDissipation = pD;
    powerDissipationJLabel.setText("" + formatter.format (powerDissipation));
  }

  /**
   * Returns the power dissipation field of this class.
   * @return The power dissipation value.
   */
  public double getPowerDissipation () {
    return powerDissipation;
  }

  /**
   * Sets title for the data in this table.
   * @param title Title string name.
   */
  public void setTableTitle (String title) {
    circuitTitleJLabel.setText(title);
  }

  /**
   * Returns the current table title.
   * @return Current table title.
   */
  public String getTableTitle () {
    return circuitTitleJLabel.getText();
  }

  /**
   * Returns table data object that holds the calculated results of the circuit.
   * @return The table data object array.
   */
  // TABLE
  public Object[][] getTableData () {
    return finalTableData;
  }

  /**
   * Returns the data object that holds the heading names of this table.
   * @return The table headings object array.
   */
  public Object[] getTableHeadings () {
    return finalTableHeadings;
  }

  /**
   * Sets power text of table.
   * @param ps Power text descriptor.
   */
  public void setPowerText (String ps) {
    powerText = ps;
    powerTextJLabel.setText(powerText);
  }

  /**
   * Sets displayed feedback resistor text above data table.
   * @param frs Feedback resistor text to display.
   */
  public void setFeedBackResistorText (String frs) {
    feedbackResistorText = frs;
    feedBackTextJLabel.setText(feedbackResistorText);
  }

  /**
   * Sets the table data and heading objects of this class.  For the WheatstoneBridge circuit, a data
   * array of three columns is accepted.  The three column array is transformed into a four column array, where
   * the first row contains row numbers.
   * @param td The table data vector (Object[][] or Vector[][])
   * @param th The table heading vector (Object[] or Vector[])
   */
  // METHOD FOR SETTING TABLE DATA
  public void setTableData (Object td[][], Object th[]) {
    System.out.println ("td.length = " + td.length);
    if (td.length == 0)
      finalTableData = new Object[td.length][th.length + 1];
    else
      finalTableData = new Object[td.length][td[0].length + 1];
    finalTableHeadings = new Object [th.length + 1];

    for (int i = 0; i < th.length; finalTableHeadings[i + 1] = th[i++]);
    finalTableHeadings[0] = "#";

    if (finalTableData.length != 0)
      for (int j = 0; j < finalTableData[0].length; j++) {
        for (int i = 0; i < td.length; i++) {
          finalTableData[i][j] = (j > 0) ? "" + td[i][j - 1] : "" + (i + 1);
          System.out.print(finalTableData[i][j] + "        ");
        }
        System.out.println ();
      }

    //System.out.println ("finalTableData = " + finalTableData);
    circuitTableDataJTable.setModel(new DefaultTableModel (finalTableData, finalTableHeadings));

    circuitTableDataJTable.getColumn(finalTableHeadings[0]).setCellRenderer(new JButtonRenderer());
    circuitTableDataJTable.getColumn(finalTableHeadings[0]).setCellEditor(new JButtonEditor(new JTextField()));

    for (int i = 0; i < th.length; i++) {
      circuitTableDataJTable.getColumn(th[i]).setCellRenderer(new JLabelRenderer());
      circuitTableDataJTable.getColumn(th[i]).setCellEditor(new JLabelEditor(new JTextField()));
    }

    setColumnWidth (64, 192, 0);
  }

  private void setColumnWidth (int smallest, int rest, int column) {
    TableColumn tc = null;
    for (int i = finalTableHeadings.length - 1; i >= 0; i--) {
      tc = circuitTableDataJTable.getTableHeader().getColumnModel().getColumn(i);
      if (i == column) {
        tc.setMinWidth (0);
        tc.setPreferredWidth (smallest);
      } else {
        tc.setMinWidth (0);
        tc.setPreferredWidth (rest);
      }
    }
  }
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }

  // ---------------------------------------------------------------------------
  // JBUTTON EDITOR
  //
  // ---------------------------------------------------------------------------
  private class JButtonEditor extends DefaultCellEditor {
    JButton numberedJButton = new JButton();
    public JButtonEditor (JTextField jtf) {
      super (jtf);
    }

    public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column) {
      return numberedJButton;
    }

    public Object getCellEditorValue() {
      return numberedJButton.getText();
    }

    public boolean isCellEditable (EventObject anEvent) {
      return false;
    }
  }

  private class JButtonRenderer extends JButton implements TableCellRenderer {
    public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      setText ((value == null) ? "" : value.toString());
      //setBorderPainted (false);
      return this;
    }
  }

  // ---------------------------------------------------------------------------
  // JLABEL EDITOR
  //
  // ---------------------------------------------------------------------------
  private class JLabelEditor extends DefaultCellEditor {
    JLabel label = new JLabel();
    public JLabelEditor (JTextField jtf) {
      super (jtf);
    }

    public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column) {
      return label;
    }

    public Object getCellEditorValue() {
      return label.getText();
    }

    public boolean isCellEditable (EventObject anEvent) {
      return false;
    }
  }

  private class JLabelRenderer extends JLabel implements TableCellRenderer {
    public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      setHorizontalAlignment(SwingConstants.CENTER);
      setText ((value == null) ? "" : value.toString());
      return this;
    }
  }
}