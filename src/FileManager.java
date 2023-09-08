import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.Hashtable;
import java.util.ArrayList;

public class FileManager {
    private static Hashtable<String, FilePanel> fileHashtable; //for storing files and their panels.
    private static ArrayList<String> fileList; //for keeping track of the order of files in the merger.

    private static App app;

    public static void init(App _app) {
        app = _app;
        fileHashtable = new Hashtable<String, FilePanel>();
        fileList = new ArrayList<String>();
    }

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
            String fileName = fileChooser.getSelectedFile().getName();
            FilePanel filePanel = new FilePanel(fileName);
            //ensure that we don't open duplicates.
            if (fileList.contains(filePath)) {
                JOptionPane.showMessageDialog(app.getFrame(), "Selected file is already open.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } 
            fileHashtable.put(filePath, filePanel);
            fileList.add(filePath);
            app.getFrame().add(filePanel);
            app.refreshFrame();
        }
    }

    public static void removeFile(String _filepath) {
        FilePanel _panel = fileHashtable.get(_filepath);
        app.getFrame().remove(_panel);
        app.refreshFrame();
        fileHashtable.remove(_filepath);
        fileList.remove(_filepath);
    }

    //merge all open files together in order.
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
}
