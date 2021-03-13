package TK_Classes;

import java.io.*;
import javax.swing.*;



/**
 * <p>Title: </p>
 * <p>Description: Class that extends FileFilter class and follow the proper
 * syntacsis. It has to have boolean method that gets file parameter and it
 * has to have string method that returns description of the filefilter.</p>
 * <p>Copyright: Copyright (c) Tom Kacperski & Pavel Lyssenko 2003</p>
 * <p>Company: </p>
 * @author Pavel Lyssenko
 * @version 1.0
 */

public class WB_Filters //extends FileFilter
{  }


class Dat_Filter extends javax.swing.filechooser.FileFilter
{

  /**
   * Get file as a parameter. Compares each file and directory in a current
   * directory to *.dat extension.
   * @param f
   * @return
   */
  public boolean accept(File f)
 {
    String path = f.getPath().toLowerCase();

    if(f.isDirectory())
    {
     return true;
    }

    if(path.endsWith("dat"))
    {
     return true;
    }
    return false;
 }

 /**
  * Return description of *.dat file filter
  * @return
  */
public String getDescription() {
    return "Text file (*.dat)";
  }


}

class  Obj_Filter extends javax.swing.filechooser.FileFilter
{

  /**
 * Get file as a parameter. Compares each file and directory in a current
 * directory to *.dat extension
 * @param f
 * @return
   */
    public boolean accept(File f)
    {
      String path = f.getPath().toLowerCase();

      if(f.isDirectory())
      {
       return true;
      }

      if(path.endsWith("obj"))
      {
      return true;
      }

      return false;
    }

    /**
     * Return description of *.obj file filter
     * @return
    */
    public String getDescription() {
      return "Object File (*.obj)";
  }

}

class Xlc_Filter extends javax.swing.filechooser.FileFilter
{

  /**
   * Get file as a parameter. Compares each file and directory in a current
   * directory to *.dat extension
   * @param f
   * @return
   */
  public boolean accept(File f)
 {
    String path = f.getPath().toLowerCase();

    if(f.isDirectory())
    {
     return true;
    }

    if(path.endsWith("xlc"))
    {
     return true;
    }
    return false;
 }

 /**
  * Return description of *.xlc file filter
  * @return
  */
public String getDescription() {
    return "Excel File (*.xlc)";
  }


}


class  Txt_Filter extends javax.swing.filechooser.FileFilter
{

  /**
 * Get file as a parameter. Compares each file and directory in a current
 * directory to *.txt extension
 * @param f
 * @return
   */
    public boolean accept(File f)
    {
      String path = f.getPath().toLowerCase();

      if(f.isDirectory())
      {
       return true;
      }

      if(path.endsWith("txt"))
      {
      return true;
      }

      return false;
    }

    /**
     * Return description of *.obj file filter
     * @return
    */
    public String getDescription() {
      return "Text File (*.txt)";
  }

}
