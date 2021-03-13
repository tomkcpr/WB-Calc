/*
 * @(#)WeightScaleSimulatorFrame.java
 *
 * Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved.
 *
 */
package ProjectC;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.table.*;
import com.borland.jbcl.layout.*;
import com.borland.dbswing.*;
import javax.swing.border.*;
import TK_Beans.*;
import TK_Interfaces.DataChangedListener;
import TK_Classes.DataChangedEvent;
import TK_Interfaces.CalculateListener;
import TK_Classes.CalculateEvent;
import TK_Classes.WB_FileListener;
import TK_Interfaces.CommandListener;
import TK_Classes.CommandEvent;
import java.rmi.Naming;
import TK_Interfaces.CalculatorModel;
import TK_Classes.WB_Calculator;
import TK_Interfaces.CalculatorServerModel;

/**
 * <p><b>Title: WeightScaleSimulatorFrame () </b></p>
 * <p><b>Description:</b></p>
 * Base frame of this application.  All components and classes are instantiated and function from here.
 * <p><b>Copyright (c) 2003 Tom Kacperski & Pavel Lyssenko.  All rights reserved. </b></p>
 * <p><b>Company: NONE </b></p>
 * @version Project A (V1.0)
 * @author Tom Kacperski & Pavel Lyssenko
 */
public class WeightScaleSimulatorFrame extends JFrame {
  private JPanel WeightScaleSimulatorPane;
  private JMenuBar jMenuBar1 = new JMenuBar();
  private JMenu jMenuFile = new JMenu();
  private JMenu jMenuHelp = new JMenu();
  private JMenuItem jMenuHelpAbout = new JMenuItem();
  private JToolBar jToolBar = new JToolBar();
  private JButton openButton = new JButton();
  private JButton saveButton = new JButton();
  private JButton helpButton = new JButton();
  private ImageIcon image1;
  private ImageIcon image2;
  private ImageIcon image3;
  private JLabel statusBar = new JLabel();

  private JTabbedPane CircuitPropertiesJTabbedPane1 = new JTabbedPane();
  private JTabbedPane CircuitOutputJTabbedPane = new JTabbedPane();
  private JPanel calculationsBaseJPanel = new JPanel();

  // Graph
  private JPanel graphJPanel = new JPanel();
  private GraphJPanel circuitGraphJPanel = new GraphJPanel();

  private JSplitPane WeightScaleJSplitPane = new JSplitPane();
  private StrainGageBridgeJPanel strainGageBridgeJPanel = new StrainGageBridgeJPanel();
  private WB_FileListener MouseClicked = new WB_FileListener();

  private JPanel rtdJPanel = new JPanel();
  private RTDBridgeJPanel rtdBridgeJPanel = new RTDBridgeJPanel();

  private CalculationsJPanel calculationsJPanel = new CalculationsJPanel();


  // Java Data Table ---------------------------------------------------------------------------
  private JPanel tableDataJPanel = new JPanel();
  private Object[][] circuitTableData = {{""}, {""}, {""}};

  private Object[] circuitTableHeader = {"Column 1","Column 2","Column 3"};
  private TK_JTablePanel circuitDataTableJPanel = new TK_JTablePanel();

  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private BorderLayout borderLayout3 = new BorderLayout();
  private BorderLayout borderLayout4 = new BorderLayout();
  private BorderLayout borderLayout5 = new BorderLayout();

  private JMenuItem jMenuItem1 = new JMenuItem();
  private JMenuItem SaveMenuItem = new JMenuItem();
  private JMenuItem SaveAsMenuItem = new JMenuItem();
  private JMenuItem OpenMenuItem = new JMenuItem();
  private JMenuItem NewMenuItem = new JMenuItem();

  private WB_Calculator wbCC = new WB_Calculator();
  private TK_Interfaces.CalculatorServerModel remote_CalculatorServerModel = null;
  private int LOCAL = 0;
  private int REMOTE = 1;

  // Server Input
  // ---------------------------------------------------------------------------

  //Construct the frame
  /**
  * Weight ScaleSimulator Frame constructor with jbInint. It constructs the
  * frame.
   */
  public WeightScaleSimulatorFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception  {
    image1 = new ImageIcon(ProjectC.WeightScaleSimulatorFrame.class.getResource("openFile.gif"));
    image2 = new ImageIcon(ProjectC.WeightScaleSimulatorFrame.class.getResource("closeFile.gif"));
    image3 = new ImageIcon(ProjectC.WeightScaleSimulatorFrame.class.getResource("help.gif"));
    WeightScaleSimulatorPane = (JPanel) this.getContentPane();
    WeightScaleSimulatorPane.setLayout(borderLayout1);
    statusBar.setText(" ");
    jMenuFile.setText("File");
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        jMenuHelpAbout_actionPerformed(e);
      }
    });

    openButton.setIcon(image1);
    openButton.setToolTipText("Open File");
    openButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        openButton_mouseReleased(e);
      }
    });

    saveButton.setIcon(image2);
    saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        saveButton_mouseReleased(e);
      }
    });

    saveButton.setToolTipText("Close File");
    helpButton.setIcon(image3);
    helpButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        helpButton_mouseReleased(e);
      }
    });

    helpButton.setToolTipText("Help");
    jToolBar.setToolTipText("File Toolbar");
    jMenuItem1.setText("Exit");
    jMenuItem1.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        jMenuFileExit_actionPerformed(e);
      }
    });

    SaveMenuItem.setText("Save");
    SaveMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        SaveMenuItem_mouseReleased(e);
      }
    });
    SaveAsMenuItem.setText("Save as...");
    SaveAsMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        SaveAsMenuItem_mouseReleased(e);
      }
    });
    OpenMenuItem.setText("Open");
    OpenMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        openButton_mouseReleased(e);
      }
    });

    NewMenuItem.setText("New");
    NewMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        NewMenuItem_mouseReleased(e);
      }
    });





    jToolBar.add(openButton);
    jToolBar.add(saveButton);
    jToolBar.add(helpButton);

    jMenuHelp.add(jMenuHelpAbout);
    //jMenuBar1.setOpaque(false);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuHelp);

    // -------------------------------------------------------------------------
    // OUR CUSTOM PANELS
    //
    //
    // -------------------------------------------------------------------------
    //instantiateCalculator(REMOTE);
    instantiateCalculator(LOCAL);
    WeightScaleSimulatorPane.setMinimumSize(new Dimension(300, 200));
    WeightScaleSimulatorPane.setPreferredSize(new Dimension(300, 200));
    WeightScaleSimulatorPane.setToolTipText("Weight Scale Simulator");
    WeightScaleSimulatorPane.add(WeightScaleJSplitPane, BorderLayout.CENTER);

    // Graph
    graphJPanel.setLayout(borderLayout3);
    graphJPanel.add(circuitGraphJPanel, BorderLayout.CENTER);

    // Calculator (No initializations)
    //wbCC.addCalculateListener(new CalculateAction());
    //private class CalculateAction implements CalculateListener {
    //  public void dataCalculated (CalculateEvent ce) {
    //    updateTable ();
    //  }
    //}

    // -------------------------------------------------------------------------
    // TABLE
    //
    //
    // -------------------------------------------------------------------------
    circuitDataTableJPanel.setFeedBackResistorText("Rf (K\u03A9)");
    circuitDataTableJPanel.setPowerText ("Power (mW)");
    tableDataJPanel.setLayout(borderLayout2);
    tableDataJPanel.add(circuitDataTableJPanel, BorderLayout.CENTER);

    WeightScaleJSplitPane.setMinimumSize(new Dimension(0, 0));
    WeightScaleJSplitPane.add(CircuitOutputJTabbedPane, JSplitPane.RIGHT);

    calculationsBaseJPanel.setLayout(borderLayout4);
    calculationsBaseJPanel.add(calculationsJPanel, BorderLayout.CENTER);
    calculationsJPanel.addCommandListener(new CommandAction());
    calculationsJPanel.addDataChangedListener(new DataChangedListener() {
      public void dataChanged (DataChangedEvent dce) { }    // No longer handled by a custom event.
    });

    CircuitOutputJTabbedPane.add(graphJPanel,   "Data Graph");
    CircuitOutputJTabbedPane.add(tableDataJPanel,  "Data Table");

    CircuitOutputJTabbedPane.setMinimumSize(new Dimension(0, 0));
    CircuitOutputJTabbedPane.setPreferredSize(new Dimension(500, 417));

    WeightScaleJSplitPane.add(CircuitPropertiesJTabbedPane1, JSplitPane.LEFT);
    WeightScaleSimulatorPane.add(statusBar, BorderLayout.SOUTH);
    WeightScaleSimulatorPane.add(jToolBar, BorderLayout.NORTH);

    rtdJPanel.setLayout(borderLayout5);

    CircuitPropertiesJTabbedPane1.setMinimumSize(new Dimension(0, 0));
    CircuitPropertiesJTabbedPane1.setPreferredSize(new Dimension(300, 377));
    CircuitPropertiesJTabbedPane1.add(calculationsBaseJPanel,   "Calculations Panel");
    CircuitPropertiesJTabbedPane1.add(strainGageBridgeJPanel,    "Strain Gage Bridge");
    CircuitPropertiesJTabbedPane1.add(rtdJPanel,  "RTD Bridge");

    rtdJPanel.add(rtdBridgeJPanel, BorderLayout.CENTER);
    rtdBridgeJPanel.getDataArbitrate().addDataChangedListener(new DataChangedListener() {
      public void dataChanged(DataChangedEvent dce) {
        MOVEArbitrateTOCalculations();
      };
    });

    strainGageBridgeJPanel.getDataIntermediate().addDataChangedListener(new DataChangedListener () {
      public void dataChanged(DataChangedEvent dce) {
        MOVEIntermediateTOCalculations();
      };
    });

    NewMenuItem.setOpaque(false);
    jMenuFile.getPopupMenu().setLightWeightPopupEnabled(false);
    jMenuFile.getPopupMenu().setOpaque(false);

    //jMenuFile.getPopupMenu().setOpaque(false);
    //jMenuFile.setOpaque(false);
    jMenuFile.add(NewMenuItem);
    jMenuFile.add(OpenMenuItem);
    jMenuFile.add(SaveAsMenuItem);
    jMenuFile.add(SaveMenuItem);
    jMenuFile.addSeparator();
    jMenuFile.add(jMenuItem1);

    //jMenuFile.setOpaque(false);
    //NewMenuItem.setOpaque(false);
    //OpenMenuItem.setOpaque(false);
    //SaveAsMenuItem.setOpaque(false);
    //SaveMenuItem.setOpaque(false);
    //jMenuItem1.setOpaque(false);
    //jMenuFile.getPopupMenu().setOpaque(false);

    WeightScaleJSplitPane.setDividerLocation(250);

    MOVEIntermediateTOCalculations();

    this.setJMenuBar(jMenuBar1);
    this.setSize(new Dimension(1000, 500));
    this.setTitle("Weight Scale Simulator");
  }

// -----------------------------------------------------------------------------
// OUR CUSTOM METHODS
//
//
// -----------------------------------------------------------------------------
  public void instantiateCalculator(int location) {
    CalculatorServerModel temp;
    Object ot;
    try {
      if (location == LOCAL) {
        ot = Naming.lookup ("rmi://localhost:2027/TomK");
        System.out.println ("'ot' class = " + ot.getClass());
        remote_CalculatorServerModel = (CalculatorServerModel)Naming.lookup ("rmi://localhost:2027/TomK");
      } else
        if (location == REMOTE) {
          remote_CalculatorServerModel = (CalculatorServerModel)Naming.lookup ("rmi://192.75.71.100:2027/TomK");
        }
    } catch (Exception e) {
      System.out.println ("Couldn't get the object (location = " + location + ")");
      e.printStackTrace();
    }
  }

  private class CommandAction implements CommandListener {
    public void commandGiven (CommandEvent ce) {
      int command = calculationsJPanel.retrieveCurrentCommand();
      // 4 CIRCUIT SELECTION JCOMBOBOX
      // 3 GRAPH BUTTON
      // 2 CLEAR BUTTON
      // 1 CALCULATE BUTTON
      // 0 ACCEPT BUTTON
      switch (command) {
        case 0:
          MOVEIntermediateTOCalculations();
          MOVEArbitrateTOCalculations();
          break;
        case 1:
          try {
            // Initialize server side with data.

            if (calculationsJPanel.getCircuitSelection() == 1)  // RTD
              remote_CalculatorServerModel.initRTDData(
                  wbCC.getBridgeVoltageInput(),
                  wbCC.getRTDGageSelection(),
                  wbCC.getRTDAlpha(),
                  wbCC.getTmpLow(),
                  wbCC.getTmpHigh(),
                  wbCC.getRTDStepSize(),
                  wbCC.getRTDResistance());
            else
              if (calculationsJPanel.getCircuitSelection() == 0)  // Strain Gage
                remote_CalculatorServerModel.initStrainGageData(
                    wbCC.getResistance(),
                    wbCC.getWeight(),
                    wbCC.getVoltageOutput(),
                    wbCC.getVoltageIN(),
                    wbCC.getGageSelection(),
                    wbCC.getStepSize(),
                    wbCC.getPower());

              System.out.println ("[0]");
            // Calculate data.
            remote_CalculatorServerModel.calculate(calculationsJPanel.getCircuitSelection());
            System.out.println ("[1]");
            // Get data from server.
            wbCC.setDataResult(calculationsJPanel.getCircuitSelection(),
                               remote_CalculatorServerModel.getDataResults());
            System.out.println ("[2]");
            wbCC.setFeedBackResistor(remote_CalculatorServerModel.getFeedBackResistor());
            System.out.println ("[3]");
            wbCC.setCircuitPower(remote_CalculatorServerModel.getCircuitPower());
            System.out.println ("[4]");

          } catch (Exception e) {
            System.out.println ("WeightScaleSimulatorFrame Exception encountered! ...");
            e.printStackTrace();
          }
          circuitDataTableJPanel.setFeedBackResistorText("Rf (K\u03A9)");
          circuitDataTableJPanel.setPowerText ("Power (mW)");
          updateTable();
          break;
        case 2:
          wbCC.clearData();
          circuitDataTableJPanel.setFeedBackResistorText("Rf (\u03A9)");
          circuitDataTableJPanel.setPowerText ("Power (W)");
          updateTable();
          circuitGraphJPanel.clearGraph();
          break;
        case 3:
          circuitGraphJPanel.setTitle(wbCC.getTitle());
          circuitGraphJPanel.setHeadings(wbCC.getTableHeader());
          circuitGraphJPanel.setData(wbCC.getTableData());
          circuitGraphJPanel.drawGraph(0, 0);
          break;
        case 4:
          wbCC.setCircuit(calculationsJPanel.getCircuitSelection());
          break;
        default:
      }
    }
  }

  public void updateTable () {
      circuitDataTableJPanel.setTableData(wbCC.getTableData(), wbCC.getTableHeader());
      circuitDataTableJPanel.setPowerDissipation(wbCC.getCircuitPower());
      circuitDataTableJPanel.setfeedbackResistor(wbCC.getFeedBackResistor());
      circuitDataTableJPanel.setTableTitle(wbCC.getTitle());
  }

  //File | Exit action performed
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    System.exit(0);
  }

  //Help | About action performed
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    WeightScaleSimulatorFrame_AboutBox dlg = new WeightScaleSimulatorFrame_AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }

    // RTD helper methods (2 methods)
  private void MOVEArbitrateTOCalculations () {
    wbCC.setTmpHigh(rtdBridgeJPanel.getDataArbitrate().getTemperatureHigh());
    wbCC.setTmpLow(rtdBridgeJPanel.getDataArbitrate().getTemperatureLow());
    wbCC.setRTDBridgeVoltageIN(rtdBridgeJPanel.getDataArbitrate().getBridgeVoltageIN());
    wbCC.setRTDGageSelection(rtdBridgeJPanel.getDataArbitrate().getRTDNumber());
    wbCC.setRTDStepSize(rtdBridgeJPanel.getDataArbitrate().getStepSize());
    wbCC.setRTDResistance(rtdBridgeJPanel.getDataArbitrate().getBridgeResistance());
    wbCC.setRTDAlpha(rtdBridgeJPanel.getDataArbitrate().getTemperatureCoefficient());
  }

  private void MOVECalculationsTOArbitrate (WB_Calculator wb) {
    rtdBridgeJPanel.getDataArbitrate().setBridgeVoltageIN(wb.getBridgeVoltageInput());
    rtdBridgeJPanel.getDataArbitrate().setRTDNumber(wb.getRTDGageSelection());
    rtdBridgeJPanel.getDataArbitrate().setTemperatureCoefficient(wb.getRTDAlpha());
    rtdBridgeJPanel.getDataArbitrate().setLowTemperature((int)wb.getTmpLow());
    rtdBridgeJPanel.getDataArbitrate().setHighTemperature((int)wb.getTmpHigh());
    rtdBridgeJPanel.getDataArbitrate().setStepSize(wb.getRTDStepSize());
    rtdBridgeJPanel.getDataArbitrate().setBridgeResistance((int)wb.getRTDResistance());
  }

    // STRAIN GAGE helper methods (2 methods)
  private void MOVEIntermediateTOCalculations () {
    wbCC.setResistance(strainGageBridgeJPanel.getDataIntermediate().getBridgeResistance());
    wbCC.setGageSelection(strainGageBridgeJPanel.getDataIntermediate().getGageNumber());
    wbCC.setWeight(strainGageBridgeJPanel.getDataIntermediate().getWeight());
    wbCC.setStepSize(strainGageBridgeJPanel.getDataIntermediate().getStepSize());
    wbCC.setPower(strainGageBridgeJPanel.getDataIntermediate().getPower());
    wbCC.setAmpVoltageOUT(strainGageBridgeJPanel.getDataIntermediate().getAmplifierVoltageOUT());
    wbCC.setBridgeVoltageIN(strainGageBridgeJPanel.getDataIntermediate().getVoltageIN());
  }

  private void MOVECalculationsTOIntermediate (WB_Calculator wb) {
    strainGageBridgeJPanel.getDataIntermediate().setBridgeResistance((int)wb.getResistance());
    strainGageBridgeJPanel.getDataIntermediate().setWeight(wb.getWeight());
    strainGageBridgeJPanel.getDataIntermediate().setAmplifierVoltageOUT(wb.getVoltageOutput());
    strainGageBridgeJPanel.getDataIntermediate().setBridgeVoltageIN(wb.getVoltageIN());
    strainGageBridgeJPanel.getDataIntermediate().setGageNumber(wb.getGageSelection());
    strainGageBridgeJPanel.getDataIntermediate().setStepSize(wb.getStepSize());
    strainGageBridgeJPanel.getDataIntermediate().setPower(wb.getPower());
  }

/**
 * Sets flag for saving files to check if file has been save before or not
 */
int Flag=0;

/**
 *  Action Listener. openButton clicked -> run openIconClicked method from
 * WB_Listener class and GUI set values.
 * @param e
 */
  void openButton_mouseReleased(MouseEvent e)
  {
    WB_Calculator wbc = new WB_Calculator();
    wbc = MouseClicked.openIconClicked(wbCC);

    if(wbc==null)
      return;

    if(wbc.getCircuit() == 0) {
      MOVECalculationsTOIntermediate (wbc);
    } else
      if (wbc.getCircuit() == 1) {
        MOVECalculationsTOArbitrate(wbc);
      }
  }

  /**
   * Action Listener. Save button clicked -> get values from GUI -> call saveIconClicked
   * method from WB_FileListener class
   * @param e
 */
  void saveButton_mouseReleased(MouseEvent e)
  {
    Flag=1;
    WB_Calculator wbc = new WB_Calculator();

    if(wbCC.getCircuit()==0)
    {
    //wbc.setResistance(strainGageBridgeJPanel.getDataIntermediate().getBridgeResistance());
    //wbc.setWeight(strainGageBridgeJPanel.getDataIntermediate().getWeight());
    //wbc.setAmpVoltageOUT(strainGageBridgeJPanel.getDataIntermediate().getAmplifierVoltageOUT());
    //wbc.setBridgeVoltageIN(strainGageBridgeJPanel.getDataIntermediate().getVoltageIN());
    //wbc.setGageSelection(strainGageBridgeJPanel.getDataIntermediate().getGageNumber());
    //wbc.setStepSize(strainGageBridgeJPanel.getDataIntermediate().getStepSize());
    //wbc.setPower(strainGageBridgeJPanel.getDataIntermediate().getPower());
    //wbc.calculate(0);
      //System.out.println ("[A+]wbc.getBridgeVoltageInput() = " + wbc.getBridgeVoltageInput() +
      //                    "wbCC.getBridgeVoltageInput() = " + wbCC.getBridgeVoltageInput());
      wbc.setDataBlock(wbCC.getDataBlock());
      //System.out.println ("[A-]wbc.getBridgeVoltageInput() = " + wbc.getBridgeVoltageInput() +
      //                    "wbCC.getBridgeVoltageInput() = " + wbCC.getBridgeVoltageInput());
    }
    else if(wbCC.getCircuit()==1){
             //wbc.setTmpHigh(rtdBridgeJPanel.getDataArbitrate().getTemperatureHigh());
             //wbc.setTmpLow(rtdBridgeJPanel.getDataArbitrate().getTemperatureLow());
             //wbc.setRTDBridgeVoltageIN(rtdBridgeJPanel.getDataArbitrate().getBridgeVoltageIN());
             //wbc.setRTDGageSelection(rtdBridgeJPanel.getDataArbitrate().getRTDNumber());
             //wbc.setRTDStepSize(rtdBridgeJPanel.getDataArbitrate().getStepSize());
             //wbc.setRTDResistance(rtdBridgeJPanel.getDataArbitrate().getBridgeResistance());
             //wbc.setRTDAlpha(rtdBridgeJPanel.getDataArbitrate().getTemperatureCoefficient());
             //wbc.calculate(1);
      //System.out.println ("[B+]wbc.getBridgeVoltageInput() = " + wbc.getBridgeVoltageInput() +
      //                    "wbCC.getBridgeVoltageInput() = " + wbCC.getBridgeVoltageInput());
      wbc.setDataBlock (wbCC.getDataBlock());
      //System.out.println ("[B-]wbc.getBridgeVoltageInput() = " + wbc.getBridgeVoltageInput() +
      //                    "wbCC.getBridgeVoltageInput() = " + wbCC.getBridgeVoltageInput());
            }

    wbc.setCircuit(wbCC.getCircuit());

    Flag = MouseClicked.saveIconClicked(wbc);

  }

/**
 * Action Listener. Save As... menu item clicked -> get values from GUI -> call saveIconClicked
 * method from WB_FileListener class
 * @param e
 */
  void SaveAsMenuItem_mouseReleased(MouseEvent e)
  {

    Flag=1;
       WB_Calculator wbc = new WB_Calculator();

       if(wbCC.getCircuit()==0)
       {
       //wbc.setResistance(strainGageBridgeJPanel.getDataIntermediate().getBridgeResistance());
       //wbc.setWeight(strainGageBridgeJPanel.getDataIntermediate().getWeight());
       //wbc.setAmpVoltageOUT(strainGageBridgeJPanel.getDataIntermediate().getAmplifierVoltageOUT());
       //wbc.setBridgeVoltageIN(strainGageBridgeJPanel.getDataIntermediate().getVoltageIN());
       //wbc.setGageSelection(strainGageBridgeJPanel.getDataIntermediate().getGageNumber());
       //wbc.setStepSize(strainGageBridgeJPanel.getDataIntermediate().getStepSize());
       //wbc.setPower(strainGageBridgeJPanel.getDataIntermediate().getPower());
       //wbc.calculate(0);
       wbc.setDataBlock (wbCC.getDataBlock());
       }
       else if(wbCC.getCircuit()==1){
                //wbc.setTmpHigh(rtdBridgeJPanel.getDataArbitrate().getTemperatureHigh());
                //wbc.setTmpLow(rtdBridgeJPanel.getDataArbitrate().getTemperatureLow());
                //wbc.setRTDBridgeVoltageIN(rtdBridgeJPanel.getDataArbitrate().getBridgeVoltageIN());
                //wbc.setRTDGageSelection(rtdBridgeJPanel.getDataArbitrate().getRTDNumber());
                //wbc.setRTDStepSize(rtdBridgeJPanel.getDataArbitrate().getStepSize());
                //wbc.setRTDResistance(rtdBridgeJPanel.getDataArbitrate().getBridgeResistance());
                //wbc.setRTDAlpha(rtdBridgeJPanel.getDataArbitrate().getTemperatureCoefficient());
                //wbc.calculate(1);
                wbc.setDataBlock (wbCC.getDataBlock());
               }

       wbc.setCircuit(wbCC.getCircuit());

       Flag = MouseClicked.saveIconClicked(wbc);


  }

/**
 * Action Listener. Save Item Clicked: Open File Chooser Dialog if Save As... Item ir Save Button
 * wasn't clicked before. Save File to the same file name and to the same location
 *  if Save As... Item was already clicked
 * @param e
 */
  void SaveMenuItem_mouseReleased(MouseEvent e)
  {
        WB_Calculator wbc = new WB_Calculator();

        if(wbCC.getCircuit()==0)
      {
      //wbc.setResistance(strainGageBridgeJPanel.getDataIntermediate().getBridgeResistance());
      //wbc.setWeight(strainGageBridgeJPanel.getDataIntermediate().getWeight());
      //wbc.setAmpVoltageOUT(strainGageBridgeJPanel.getDataIntermediate().getAmplifierVoltageOUT());
      //wbc.setBridgeVoltageIN(strainGageBridgeJPanel.getDataIntermediate().getVoltageIN());
      //wbc.setGageSelection(strainGageBridgeJPanel.getDataIntermediate().getGageNumber());
      //wbc.setStepSize(strainGageBridgeJPanel.getDataIntermediate().getStepSize());
      //wbc.setPower(strainGageBridgeJPanel.getDataIntermediate().getPower());
      //wbc.calculate(0);
      wbc.setDataBlock (wbCC.getDataBlock());
      }
      else if(wbCC.getCircuit()==1){
               //wbc.setTmpHigh(rtdBridgeJPanel.getDataArbitrate().getTemperatureHigh());
               //wbc.setTmpLow(rtdBridgeJPanel.getDataArbitrate().getTemperatureLow());
               //wbc.setRTDBridgeVoltageIN(rtdBridgeJPanel.getDataArbitrate().getBridgeVoltageIN());
               //wbc.setRTDGageSelection(rtdBridgeJPanel.getDataArbitrate().getRTDNumber());
               //wbc.setRTDStepSize(rtdBridgeJPanel.getDataArbitrate().getStepSize());
               //wbc.setRTDResistance(rtdBridgeJPanel.getDataArbitrate().getBridgeResistance());
               //wbc.setRTDAlpha(rtdBridgeJPanel.getDataArbitrate().getTemperatureCoefficient());
               //wbc.calculate(1);
               wbc.setDataBlock (wbCC.getDataBlock());
               }
     //System.out.println(" "+Flag);

     if(Flag==0)
     {
      Flag = MouseClicked.saveIconClicked(wbc);
     }

     else if(Flag==1)
     {
      MouseClicked.saveItemClicked(wbc);
     }

     else {}

     //System.out.println(" "+Flag);
  }

  /**
     * Action Listener. helpButton clicked -> show Help Frame
     * @param e
   */
  void helpButton_mouseReleased(MouseEvent e)
  {
       HelpFrame HelpFrame = new HelpFrame();
       HelpFrame.show();
  }

  /**
   * Action Listener. New menu item clicked -> set defaults values in GUI
   * and erase file name for Save menu item
   * @param e
   */
  void NewMenuItem_mouseReleased(MouseEvent e)
  {
    Flag=0;
    if (wbCC.getCircuit()==0)
    {
    strainGageBridgeJPanel.getDataIntermediate().setBridgeResistance(120);
    strainGageBridgeJPanel.getDataIntermediate().setWeight(100);
    strainGageBridgeJPanel.getDataIntermediate().setAmplifierVoltageOUT(1);
    strainGageBridgeJPanel.getDataIntermediate().setBridgeVoltageIN(1);
    strainGageBridgeJPanel.getDataIntermediate().setGageNumber(2);
    strainGageBridgeJPanel.getDataIntermediate().setStepSize(10);
    }
    else if (wbCC.getCircuit()==1)
    {
        rtdBridgeJPanel.getDataArbitrate().setBridgeVoltageIN(1.0);
        rtdBridgeJPanel.getDataArbitrate().setRTDNumber(2);
        rtdBridgeJPanel.getDataArbitrate().setTemperatureCoefficient(0.004);
        rtdBridgeJPanel.getDataArbitrate().setLowTemperature(0);
        rtdBridgeJPanel.getDataArbitrate().setHighTemperature(100);
        rtdBridgeJPanel.getDataArbitrate().setStepSize(5);
        rtdBridgeJPanel.getDataArbitrate().setBridgeResistance(100);
    }


  }
}