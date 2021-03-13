package TK_Classes;

import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * <p>Title: File Save/Opening Class </p>
 * <p>Description: Gets data from an object(GUI or file) and saves it as text, excel or object to
 * selected file. Alost, it checks if file is already  exist or not</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Pavel Lyssenko & Tom Kacperski
 * @version 1.0
 */

public class WB_File {

  /**
   * Internal class value for convinient data exchange with calculator class
   */
  int Weight, GageSelection, StepSize,IntStepSum, i;
  private double Step,StepSum;


  /**
   * File Name returned from JFileChooser
   */
  public String FileName;

  /**
   * File Name used to get values from files
   */
  public String Text=null;

  /**
   * File Name Parameter used for HelpFrame
   */
  public String TextFileName;


  private WB_Calculator Data = new WB_Calculator();


  /**
   * WB_File Constructor
   */
  public WB_File()
     {}

  /**
   * WB_File Constructor that gets object as a parameter
   * @param wbc
   */
  public WB_File(WB_Calculator wbc)
     {
      Data = wbc;
  }

  /**
   * Saves Data from RTD Panel or Strain Gage panel depending on what circuit
   * was selected by user. It uses UNIX text separator standart "|"
   * It gets file path as a parameter.
   * @param FileName
   */
   public void saveData(String FileName)
     {

      try
      {
      PrintWriter out = new PrintWriter(new FileWriter(FileName));

      Weight = Data.Weight+1;
      StepSize = Data.StepSize;

// Saves Settings
   if(Data.circuitSelection==0)
  {
      out.println(+Data.circuitSelection+ "|" +Data.Weight+ "|" +Data.bridgeVoltageInput+ "|" +Data.R1+ "|" +Data.amplifierVoltageOutput+ "|"
                  +Data.GageSelection+ "|" +Data.StepSize+ "|" +Data.power);
      out.close();
  }
  else if(Data.circuitSelection==1)
  {
    out.println(+Data.circuitSelection+ "|" +Data.RTDbridgeVoltageInput+ "|" +Data.RTDR1+ "|" +Data.RTDGageSelection+ "|" +Data.RTDStepSize+ "|"
                    +Data.tmp_low+ "|" +Data.tmp_high);
    out.close();
  }

       }

      catch(IOException exception)
       {
        exception.printStackTrace();

       }

     }

// Saves TXT

       public void saveTxt(String FileName)
     {

      try
      {
      PrintWriter out = new PrintWriter(new FileWriter(FileName));

      Weight = Data.Weight+1;
      StepSize = Data.StepSize;

// Saves Settings
   if(Data.circuitSelection==0)
  {
      out.println(+Data.circuitSelection+ "|" +Data.Weight+ "|" +Data.bridgeVoltageInput+ "|" +Data.R1+ "|" +Data.amplifierVoltageOutput+ "|"
                  +Data.GageSelection+ "|" +Data.StepSize+ "|" +Data.power);
      out.close();
  }
  else if(Data.circuitSelection==1)
  {
    out.println(+Data.RTDbridgeVoltageInput+ "\n" +Data.tmp_low+ "\n" +Data.tmp_high);
    out.close();
  }
       }

      catch(IOException exception)
       {
        exception.printStackTrace();
       }
     }

     /**
      * Opens RTD paner or Strain Gage Panel values depending on what circuit
      * was selected by user.
      * @param Path
      */
     public void openData(String Path)
     {

     try
     {
      BufferedReader in = new BufferedReader(new FileReader(Path));

      String s = in.readLine();
      StringTokenizer t = new StringTokenizer(s,"|");

 if(Data.circuitSelection==0)
  {
      Data.circuitSelection = Integer.parseInt(t.nextToken());
      Data.Weight = Integer.parseInt(t.nextToken());
      Data.bridgeVoltageInput = Double.parseDouble(t.nextToken());
      Data.R1 = Double.parseDouble(t.nextToken());
      Data.amplifierVoltageOutput = Double.parseDouble(t.nextToken());
      Data.GageSelection = Integer.parseInt(t.nextToken());
      Data.StepSize = Integer.parseInt(t.nextToken());
      Data.power = Integer.parseInt(t.nextToken());
      in.close();
   }
 else if(Data.circuitSelection==1)
   {
       Data.circuitSelection=Integer.parseInt(t.nextToken());
       Data.RTDbridgeVoltageInput=Double.parseDouble(t.nextToken());
       Data.RTDR1=Double.parseDouble(t.nextToken());
       Data.RTDGageSelection=Integer.parseInt(t.nextToken());
       Data.RTDStepSize=Integer.parseInt(t.nextToken());
       Data.tmp_low=Double.parseDouble(t.nextToken());
       Data.tmp_high=Double.parseDouble(t.nextToken());
       in.close();
   }

     }

     catch(IOException exception)
       {
        exception.printStackTrace();
       }
     }


 public void openTxt(String Path)
     {

     try
     {
      BufferedReader in = new BufferedReader(new FileReader(Path));

      String e;
      String s = in.readLine();
      String s1 = in.readLine();
      String s2 = in.readLine();
      StringTokenizer t = new StringTokenizer(s,"|");
      StringTokenizer t1 = new StringTokenizer(s1, "");


 if(Data.circuitSelection==0)
  {
      Data.circuitSelection = Integer.parseInt(t.nextToken());
      Data.Weight = Integer.parseInt(t.nextToken());
      Data.bridgeVoltageInput = Double.parseDouble(t.nextToken());
      Data.R1 = Double.parseDouble(t.nextToken());
      Data.amplifierVoltageOutput = Double.parseDouble(t.nextToken());
      Data.GageSelection = Integer.parseInt(t.nextToken());
      Data.StepSize = Integer.parseInt(t.nextToken());
      Data.power = Integer.parseInt(t.nextToken());
      in.close();
   }
 else if(Data.circuitSelection==1)
   {
       Data.RTDbridgeVoltageInput=Double.parseDouble(s);
       Data.tmp_low=Double.parseDouble(s1);
       Data.tmp_high=Double.parseDouble(s2);
       in.close();
   }

     }

     catch(IOException exception)
       {
        exception.printStackTrace();
       }
     }



     /**
      * Method that opens text. It used by Help Frame to display text in
      * JTextArea.
      */
     public void openText()
    {
      int i=0;

    try
    {
     BufferedReader in = new BufferedReader(new FileReader(TextFileName));
     Text = in.readLine();
     in.close();
    }

    catch(IOException exception)
      {
       exception.printStackTrace();
       System.err.println("File Error");
      }


    }


/**
 * Method that saves RTD panel or Strain Gage Panel values in Excel format.
 * It gets file path as a parameter.
 * @param Path
 */
   public void saveXlc(String Path)
    {

      try
     {
     PrintWriter out = new PrintWriter(new FileWriter(Path));

     Weight = Data.Weight+1;
     StepSize = Data.StepSize;

// Saves Settings

     if(Data.circuitSelection==0)
     {
     out.println("Weight	VoltageInput	Resistance	SystemOutput	GageSelection	StepSize	Power");
     out.println(Data.Weight+ "	" +Data.bridgeVoltageInput+ "	" +Data.R1+ "	" +Data.amplifierVoltageOutput+ "	"
                 +Data.GageSelection+ "	" +Data.StepSize+ "	" +Data.power);
     out.println("\n\n\n\n\n");
     out.println("	Table");
     out.println("Weight(Lb)	VoltageBridgeOuput(mV)	SystemOutput(mV)");
     for (int i = 0; i < Data.getTableData().length; i++) {
       for (int j = 0; j < Data.getTableData()[0].length; j++)
    {
      out.print(Data.getTableData()[i][j] + "	");
    }
    out.println ();
        }

     out.close();
     }
     else if (Data.circuitSelection==1)
     {
      out.println("RTDVoltageInput	RTDResistance	RTDGageSelection	RTDStepSize	TemperatureLow	TemperatureHigh");
      out.println(Data.RTDbridgeVoltageInput+ "	" +Data.RTDR1+ "	" +Data.RTDGageSelection+ "	" +Data.RTDStepSize+ "	" +Data.tmp_low+ "	" +Data.tmp_high);


      out.println("\n\n\n\n\n");
      out.println("	Table");
      out.println("Temperature(Degrees)	Resistance	VoltageBridgeOuput(mV)");
        for (int i = 0; i < Data.getTableData().length; i++) {
          for (int j = 0; j < Data.getTableData()[0].length; j++)
          {
            out.print(Data.getTableData()[i][j] + "	");
          }
          out.println ();
        }
      out.close();
     }

     }

     catch(IOException exception)
      {
       exception.printStackTrace();

      }


    }

    /**
     * Method that saves RTD panel values or Strain Gage values as an object
     * depending on user selection. It gets file path as a parameter
     * @param Path
     */
    public void SaveObject(String Path)
   {
     try {
          ObjectOutputStream saveObject = new ObjectOutputStream(new FileOutputStream(Path));

        if(Data.circuitSelection==0)
        {
          saveObject.writeInt(Data.circuitSelection);
          saveObject.writeInt(Data.Weight);
          saveObject.writeDouble(Data.bridgeVoltageInput);
          saveObject.writeDouble(Data.R1);
          saveObject.writeDouble(Data.amplifierVoltageOutput);
          saveObject.writeInt(Data.GageSelection);
          saveObject.writeInt(Data.StepSize);
          saveObject.writeInt(Data.power);
          saveObject.close();
        }

        else if (Data.circuitSelection==1)
        {
          saveObject.writeInt(Data.circuitSelection);
          saveObject.writeDouble(Data.RTDbridgeVoltageInput);
          saveObject.writeDouble(Data.RTDR1);
          saveObject.writeInt(Data.RTDGageSelection);
          saveObject.writeInt(Data.RTDStepSize);
          saveObject.writeDouble(Data.tmp_low);
          saveObject.writeDouble(Data.tmp_high);
          saveObject.close();
        }
          }

      catch(IOException exception)
     {
       exception.printStackTrace();
     }
   }

   /**
    * Methos that read RTD values or Strain Gage values from a file containing
    * object data. It gets file path as a parameter.
    * @param Path
    */
  public void openObject(String Path)
   {
       try {
      ObjectInputStream openObject = new ObjectInputStream(new FileInputStream(Path));

      System.out.println("Selection: " + Data.circuitSelection);

      if(Data.circuitSelection==0)
      {
      Data.circuitSelection=openObject.readInt();
      Data.Weight=openObject.readInt();
      Data.bridgeVoltageInput=openObject.readDouble();
      Data.R1=openObject.readDouble();
      Data.amplifierVoltageOutput=openObject.readDouble();
      Data.GageSelection=openObject.readInt();
      Data.StepSize=openObject.readInt();
      Data.power=openObject.readInt();
      openObject.close();
      }

      else if (Data.circuitSelection==1)
      {
        Data.circuitSelection=openObject.readInt();
        Data.RTDbridgeVoltageInput=openObject.readDouble();
        Data.RTDR1=openObject.readDouble();
        Data.RTDGageSelection=openObject.readInt();
        Data.RTDStepSize=openObject.readInt();
        Data.tmp_low=openObject.readDouble();
        Data.tmp_high=openObject.readDouble();
        openObject.close();
      }
           }

     catch(IOException exception)
      {
     exception.printStackTrace();
     }

   }

//


/**
 * Method that gets calculator data as an object
 * @return
 */
public WB_Calculator getData () {
  return Data;
}

}