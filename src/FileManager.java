import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collections;


public class FileManager {

    private static Hashtable<String, FilePanel> fileHashtable; // For storing files and their panels.
    private static ArrayList<String> fileList; // For keeping track of the order of files in the merger.
    
    private static JFileChooser openFileChooser; 

    private static App app;

    /**
     * Initializes the FileManager.
     * @param app
     */
    public static void init(App app) {

        FileManager.app = app;
        FileManager.fileHashtable = new Hashtable<String, FilePanel>();
        FileManager.fileList = new ArrayList<String>();

        // Initialize file chooser.
        openFileChooser = new JFileChooser();
        openFileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter(AppStrings.APP_MENU_FILE_EXTENSION_FILTER_DESCRIPTION, AppStrings.APP_MENU_FILE_EXTENSION_FILTER);
        openFileChooser.setFileFilter(pdfFilter);
    }

    /** Method for opening files. Creates a JFileChooser, allows the user to select a file, and adds the file's path to the list. */
    public static void openFile() {
        
        // Open the file chooser.
        int returnVal = openFileChooser.showOpenDialog(app.getFrame());
        
        // Add the file
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            String filePath = openFileChooser.getSelectedFile().getAbsolutePath();

            // Ensure that we don't open duplicates.
            if (fileList.contains(filePath)) {
                JOptionPane.showMessageDialog(app.getFrame(), AppStrings.APP_ERROR_FILE_OPEN, AppStrings.APP_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                return;
            } 

            String fileName = openFileChooser.getSelectedFile().getName();
            FilePanel filePanel = new FilePanel(fileName);
            filePanel.setPath(filePath);
            fileHashtable.put(filePath, filePanel);
            fileList.add(filePath);
            app.getFrame().add(filePanel);
            app.refreshFrame();
        }
    }

    /**
     * Removes a file's path from the list. Also deletes it's associated filepanel.
     * @param _filepath : String, the path to be removed.
     */
    public static void removeFile(String _filepath) {
        FilePanel _panel = fileHashtable.get(_filepath);
        app.getFrame().remove(_panel);
        app.refreshFrame();
        fileHashtable.remove(_filepath);
        fileList.remove(_filepath);
    }

    /**Merge all open files together in the order they appear in the file list. */
    public static void mergeFiles() {

        // Display an error if nothing there is nothing to merge.
        if (fileList.size() == 0) {
            JOptionPane.showMessageDialog(app.getFrame(), AppStrings.APP_ERROR_NO_FILES, AppStrings.APP_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] files = fileList.toArray(new String[0]); // Convert to standard array to make our lives easier.
        
        // Create file chooser for the save file and set filter.
        JFileChooser saveFileChooser = new JFileChooser();
        saveFileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter(AppStrings.APP_MENU_FILE_EXTENSION_FILTER_DESCRIPTION, AppStrings.APP_MENU_FILE_EXTENSION_FILTER);
        saveFileChooser.setFileFilter(pdfFilter);

        // Merge and save.
        int saveChoice = saveFileChooser.showSaveDialog(app.getFrame());
        if (saveChoice == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = saveFileChooser.getSelectedFile().getAbsolutePath();

                // Quick check to make sure that the file name the user chose ends in .pdf, if it doesn't, we will add it for them.
                if (!(filePath.substring(filePath.length() - 4).equals(AppStrings.APP_MENU_FILE_EXTENSION))) {
                    filePath+=AppStrings.APP_MENU_FILE_EXTENSION;
                }

                PDFMerger.merge(files, filePath);
                JOptionPane.showMessageDialog(app.getFrame(), AppStrings.APP_MENU_INFO_COMPLETE, AppStrings.APP_MENU_INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(app.getFrame(), AppStrings.APP_ERROR_SAVE_FAIL + e.getMessage(), AppStrings.APP_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
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
     * Moves a file to a given index in the file list.
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

    /**Redraws the file panels on screen, taking advantage of our FlowLayout to ensure order is correct. */
    public static void redrawFiles() {

        // Remove all the files from the frame
        for (int i = 0; i < fileList.size(); i++) {
            app.getFrame().remove(fileHashtable.get(fileList.get(i)));
        }

        // Add them all back. the frame's FlowLayout will make sure the order is right
        for (int i = 0; i < fileList.size(); i++) {
            app.getFrame().add(fileHashtable.get(fileList.get(i)));
        }

        app.refreshFrame();
    }

    /**
     * Get the current size of the file list.
     * @return size : int
     */
    public static int getFileListSize() {
        return fileList.size();
    }
}
