package TK_Classes;

import javax.swing.*;
import java.io.*;




/**
 * <p>Title: FileListener Class </p>
 * <p>Description: Runs JFileChooser when Save/Save As.../Open button or
 * menu item was clicked. It returns the directory name(specified by a user) and
 * adds file separator and a file name(specified by a user) to the end of it.
 * If file is already exist, the JOptionPane will be called to check if user
 * wants to overwrite existing file or to change the name of the file he wants
 * to save. </p>
 * <p>Copyright: Copyright (c) Pavel Lyssenko & Tom Kacperski 2003</p>
 * <p>Company: </p>
 * @author Pavel Lyssenko
 * @version 1.0
 */

public class WB_FileListener
{
  private File Directory;
  private String Name, Path;
  private String Extension=null;

  /** WB_FileListener constructor with no parameters */
  public WB_FileListener() {}

  private JFileChooser wbs_Chooser = new JFileChooser();
  private JOptionPane FileExistsPane = new JOptionPane();


  /**
   * Call JFileChooser dialog if save button was clicked -> save file to the
   * returned name by calling saveData method from WB_File class. If cancel
   * button was clicked -> do nothing. If file is already exists -> call
   * JOptionPane to check if user really wants to overwrite a file.
   * @param wbc
   * @return Flag parameter
   */
public int saveIconClicked(WB_Calculator wbc)
  {

     WB_File save = new WB_File(wbc);

//
     wbs_Chooser.addChoosableFileFilter(new Dat_Filter());
     wbs_Chooser.addChoosableFileFilter(new Obj_Filter());
     wbs_Chooser.addChoosableFileFilter(new Xlc_Filter());
     wbs_Chooser.addChoosableFileFilter(new Txt_Filter());


     int returnVal = wbs_Chooser.showSaveDialog(wbs_Chooser);

     if (returnVal == 0)
     {
//
       Extension = wbs_Chooser.getFileFilter().toString();
       Extension=Extension.substring(11,14).toLowerCase();


       Directory = wbs_Chooser.getCurrentDirectory();
       Name = wbs_Chooser.getSelectedFile().getName();

       Path = Directory.toString();
       Path = Path.concat(File.separator);

       Name = Path.concat(Name);

//
       System.out.println("Exts: "+Extension);
       if (Extension.compareTo("dat")==0)
      {
         Name = Name.concat(".dat");
         FileExistState(Name,wbs_Chooser);
         save.saveData(Name);
      }
      else if (Extension.compareTo("obj")==0)
      {
         Name = Name.concat(".obj");
         FileExistState(Name,wbs_Chooser);
         save.SaveObject(Name);

      }
      else if (Extension.compareTo("xlc")==0)
      {
         Name = Name.concat(".xlc");
         FileExistState(Name,wbs_Chooser);
         save.saveXlc(Name);
      }
      else if (Extension.compareTo(".pl")==0)
      {
          Name = Name.concat(".xlc");
          JOptionPane.showMessageDialog(null,"File Type should be selected\nFile will be Saved in " +Name+ "","Warning",2);
          FileExistState(Name,wbs_Chooser);
          save.saveXlc(Name);
      }
      else if (Extension.compareTo("txt")==0)
      {
          System.out.println("!!!TXT!!!");
          Name = Name.concat(".txt");
          FileExistState(Name,wbs_Chooser);
          save.saveTxt(Name);
      }

        else {}


//
       wbs_Chooser.resetChoosableFileFilters();
       return 1;
     }
    else {
//
    wbs_Chooser.resetChoosableFileFilters();
    return 0;
         }
  }


  /**
   * Call saveData method in WB_File class if Save menu item has been clicked.
   * @param wbc
   */
public void saveItemClicked(WB_Calculator wbc)
    {

       WB_File save = new WB_File(wbc);


       if (Extension.compareTo("dat")==0)
            {
               save.saveData(Name);
            }
            else if (Extension.compareTo("obj")==0)
            {
              save.saveData(Name);
            }
            else if (Extension.compareTo("xlc")==0)
            {
               save.saveXlc(Name);
            }
        else {}

       save.saveXlc(Name);


    }


    /**
     * Calls JFileChooser dialog if Open menu item or openButton has been
     * clicked. If Open button of dialog was clicked -> opens file and send
     * data to an object.
     * @return Object
     */
public WB_Calculator openIconClicked(WB_Calculator wbc)
{
     String Name, Path;


     wbs_Chooser.addChoosableFileFilter(new Dat_Filter());
     wbs_Chooser.addChoosableFileFilter(new Obj_Filter());
     wbs_Chooser.addChoosableFileFilter(new Txt_Filter());

     WB_File open = new WB_File();
     open.getData().setCircuit(wbc.getCircuit());

     int returnVal = wbs_Chooser.showOpenDialog(wbs_Chooser);

     if (returnVal == 0)
      {
        Extension = wbs_Chooser.getFileFilter().toString();
        Extension=Extension.substring(11,14).toLowerCase();

        Directory = wbs_Chooser.getCurrentDirectory();
        Name = wbs_Chooser.getSelectedFile().getName();
        Path = Directory.toString();
        Path = Path.concat(File.separator);
        Name = Path.concat(Name);

//
        if (Extension.compareTo("dat")==0)
        {
           open.openData(Name);
        }
        else if (Extension.compareTo("obj")==0)
        {
           open.openObject(Name);
        }
        else if (Extension.compareTo(".pl")==0)
        {
             if(Name.endsWith("dat"))
             {
               open.openData(Name);
             }
             else if(Name.endsWith("obj"))
             {
               open.openObject(Name);
             }
             else {
                    wbs_Chooser.resetChoosableFileFilters();
                    JOptionPane.showMessageDialog(null,"This File Type is not supported","Error",0);
                    return null;
                  }
        }
        else if (Extension.compareTo("txt")==0)
        {
          open.openTxt(Name);
        }
        else {}

        wbs_Chooser.resetChoosableFileFilters();
        return open.getData();
      }
    else {
    wbs_Chooser.resetChoosableFileFilters();
    return null;
         }

}

public void FileExistState(String Path, JFileChooser FileChooserObject)
  {
    File State = new File(Path);

         if (State.exists())
         {
           int act = JOptionPane.showConfirmDialog(null, State.toString()+ " is already exists.  Overwrite?");

           while (act == JOptionPane.NO_OPTION)
            {
             boolean state;
             FileChooserObject.showSaveDialog(null);
             act = JOptionPane.showConfirmDialog(null, State.toString() + " is already exists.  Overwrite?");
            }

          if (act == JOptionPane.CANCEL_OPTION)
            FileChooserObject.showSaveDialog(null);
       }


  }
}