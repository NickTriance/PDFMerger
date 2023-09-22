import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.Hashtable;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class FileManager {

    private static Hashtable<String, FilePanel> fileHashtable; //for storing files and their panels.
    private static ArrayList<String> fileList; //for keeping track of the order of files in the merger.

    private static App app;

    public static void init(App _app) {
        app = _app;
        fileHashtable = new Hashtable<String, FilePanel>();
        fileList = new ArrayList<String>();
    }

    /** Method for opening files. Creates a JFileChooser, allows the user to select a file, and adds the file's path to the list. */
    public static void openFile() {
        
        //create file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF Documents", "pdf");
        fileChooser.setFileFilter(pdfFilter);
        
        //open the file chooser
        int returnVal = fileChooser.showOpenDialog(app.getFrame());
        
        //add the file
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            //ensure that we don't open duplicates.
            if (fileList.contains(filePath)) {
                JOptionPane.showMessageDialog(app.getFrame(), "Selected file is already open.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } 
            String fileName = fileChooser.getSelectedFile().getName();
            FilePanel filePanel = new FilePanel(fileName);
            filePanel.setPath(filePath);
            fileHashtable.put(filePath, filePanel);
            fileList.add(filePath);
            app.getFrame().add(filePanel);
            app.refreshFrame();
        }
    }

    /**
     * Removes a file's path from the list. Also deletes it's associated filepanel
     * @param _filepath : String, the path to be removed.
     */
    public static void removeFile(String _filepath) {
        FilePanel _panel = fileHashtable.get(_filepath);
        app.getFrame().remove(_panel);
        app.refreshFrame();
        fileHashtable.remove(_filepath);
        fileList.remove(_filepath);
    }

    /**Merge all open files together in the order they appear in the file list */
    public static void mergeFiles() {
        String[] files = fileList.toArray(new String[0]); //convert to standard array to make our lives
        
        //create file chooser and set filter
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF Documents", "pdf");
        fileChooser.setFileFilter(pdfFilter);

        //merge and save
        int saveChoice = fileChooser.showSaveDialog(app.getFrame());
        if (saveChoice == JFileChooser.APPROVE_OPTION) {
            try {
                PDFMerger.merge(files, fileChooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(app.getFrame(), "Done", null, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(app.getFrame(), "Failed to save file " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Swaps file a with file b in the file list.
     * @param a : String, path to file a
     * @param b : String, path to file b
     */
    public static void swapFiles(String a, String b) {
        int aIndex = fileList.indexOf(a);
        int bIndex = fileList.indexOf(b);

        Collections.swap(fileList, aIndex, bIndex);
    }

    /**
     * Moves a file to a given index in the file list
     * @param filePath
     * @param index
     */
    public static void moveFile(String filePath, int index) {
        if (index > fileList.size()) {
            System.out.println("invalid index " + index);
            return;
        }
        
        fileList.remove(fileList.indexOf(filePath));
        fileList.add(index, filePath);
    }

    /**Calculates where a file should be in the list after drag and drop occurs.
     * @param filePath : String, file that was moved
     */
    public static void insertFileDragAndDrop (String filePath) {

        //get location of panel
        //TODO: THIS IS BUGGED, DOESN'T GET LOCATION RELATIVE TO FRAME
        FilePanel _panel = fileHashtable.get(filePath);
        int _panelX = _panel.getX();
        int _panelY = _panel.getY();

        Point _panelPoint = Utils.convertPointToParentLocal(new Point(_panelX, _panelY), _panel, App.app.getFrame());

        _panelX = _panelPoint.x;
        _panelY = _panelPoint.y;


        /*
         * In order for a file to be before another file in the list, it must be
         * either to the left of or above the other file on screen. 0,0 is the 
         * top left corner of the frame. Therefore, our X value must be less than
         * the other file, or our Y value must be less than the other file. We will
         * also insert before a file if we happen to be in the same location.
         */

        
        //loop through the file list to figure out where we are supposed to go
        for (int i = 0; i < fileList.size(); i++) {
            
            //get the next file in the list
            String _file = fileList.get(i);

            //if we are the next file, move on.
            if (_file.equals(filePath)){
                continue;
            }

            FilePanel _filePanel = fileHashtable.get(_file);
            int _fileX = _filePanel.getX();
            int _fileY = _filePanel.getY();

            if (((_panelX < _fileX) && (_panelY <= _fileY) || 
            ((_panelY < _fileY) && (_panelX <= _fileX)) || 
            ((_panelX == _fileX) && (_panelY == _fileY)))) {
                moveFile(filePath, fileList.indexOf(_file));
                break; //we've found where we are supposed to go
            }
        }
        redrawFiles();
    }

    /**
     * Moves a file to the previous index in the file list.
     * @param filepath : String, path of file to be moved.
     */
    public static void shiftFilePrev(String filepath) {
        
        
        int index = fileList.indexOf(filepath);
        System.out.println(index);
         
        if (index == 0 || index == -1) {
            return;
        }

        swapFiles(filepath, fileList.get(index - 1));
        redrawFiles();
    }

    /**
     * Moves a file to the next index in the file list.
     * @param filepath : String, path of file to be moved.
     */
    public static void shiftFileNext(String filepath) {
        int index = fileList.indexOf(filepath);
        if (index == fileList.size() - 1 || index == -1 ) {
            return;
        }

        swapFiles(filepath, fileList.get(index + 1));
        redrawFiles();
    }

    /**Redraws the file panels on screen to ensure order is correct */
    public static void redrawFiles() {

        //remove all the files from the frame
        for (int i = 0; i < fileList.size(); i++) {
            app.getFrame().remove(fileHashtable.get(fileList.get(i)));
        }

        //add them all back. the frame's FlowLayout will make sure the order is right
        for (int i = 0; i < fileList.size(); i++) {
            app.getFrame().add(fileHashtable.get(fileList.get(i)));
        }

        app.refreshFrame();
    }

    /**Get the index of a given filepath in the list 
     * @param filePath : String, the filepath to get the index of
     * @return index : int
    */
    public static int getIndex(String filePath) {
        return fileList.indexOf(filePath);
    }

    /**
     * Get the current size of the file list
     * @return size : int
     */
    public static int getFileListSize() {
        return fileList.size();
    }
}
