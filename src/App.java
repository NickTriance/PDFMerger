import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;

import java.util.ArrayList;
import java.util.Hashtable;

//TODO: Javadocs.
//TODO: display something when no files are open
//TODO: implement drag and drop in this view
public class App {

    //Things it would be useful to have global access to
    private JFrame frame;

    private Hashtable<String, FilePanel> fileHashtable; //for storing files and their panels.
    private ArrayList<String> fileList; //for keeping track of the order of files in the merger.
    
    public App() {
        //set look and feel by platform.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // sqaush
        }

        frame = createFrame();

        fileHashtable = new Hashtable<String, FilePanel>();
        fileList = new ArrayList<String>();

        frame.setVisible(true);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setSize(640,480);
        frame.setTitle("PDFMerger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
        frame.setJMenuBar(createJMenuBar());
        
        //contentPanel = new JPanel();
        //contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        //frame.add(contentPanel);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        frame.setTransferHandler(new TransferHandler("text") {
            @Override
            public boolean canImport(TransferHandler.TransferSupport support) {
                return true;
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport support) {
                try {
                    String data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    
                    //logic to rearrange filepanels based on data goes here

                    refreshFrame();
                    return true;
                } catch (UnsupportedFlavorException | IOException e) {
                    return false;
                }
            }
        });

        return frame;
    }

    private JMenuBar createJMenuBar() {
        JMenuBar jmb = new JMenuBar();
        jmb.add(createFileMenu());
        jmb.add(createHelpMenu());
        return jmb;
    }

    /**
     * Creates the File menu on the menu bar.
     * @return FileMenu: JMenu
     */
    private JMenu createFileMenu() {
        JMenu jmFile = new JMenu("File");
        jmFile.setMnemonic('F');

        //create the menu for opening files
        JMenuItem jmiOpen = new JMenuItem("Open");
        jmiOpen.setMnemonic('O');
        jmiOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        jmiOpen.addActionListener((ae) -> {
            addFile();
        });
        jmFile.add(jmiOpen);

        JMenuItem jmiSave = new JMenuItem("Save/Export");
        jmiSave.setMnemonic('S');
        jmiSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        jmiSave.addActionListener((ae) -> {
            //TODO: This is the part where the PDFS will be combined and saved
            mergeFiles();
        });
        jmFile.add(jmiSave);

        return jmFile;
    }

    private JMenu createHelpMenu() {
        JMenu jmHelp = new JMenu("Help");
        jmHelp.setMnemonic('H');

        JMenuItem jmiHelp = new JMenuItem("View Help");
        jmiHelp.setMnemonic('H');
        jmiHelp.addActionListener((ae) -> {
            JOptionPane.showMessageDialog(frame, "Add PDF files with File -> Open. \nArrange with drag and drop. \nExport with File -> Save/Export.", "Help", JOptionPane.INFORMATION_MESSAGE);
        });
        jmHelp.add(jmiHelp);

        jmHelp.addSeparator();

        JMenuItem jmiAbout = new JMenuItem("About...");
        jmiAbout.setMnemonic('A');
        jmiAbout.addActionListener((ae) -> {
            JOptionPane.showMessageDialog(frame, "PDFMerger V0.1 by NickTriance\n www.nicktriance.com", "About", JOptionPane.INFORMATION_MESSAGE);
        });
        jmHelp.add(jmiAbout);

        return jmHelp;
    }
    private JPopupMenu createPopupMenu() { 
        JPopupMenu _jpm = new JPopupMenu(null);
       
        JMenuItem jpmDel = new JMenuItem("Delete");
        jpmDel.setMnemonic('D');
        jpmDel.addActionListener((ae) -> {
            //todo: remove file
        });

        return _jpm;
    }

    /**Adds files to the file list. */
    private void addFile() {
        
        //create file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF Documents", "pdf");
        fileChooser.setFileFilter(pdfFilter);
        //open the file chooser
        int returnVal = fileChooser.showOpenDialog(frame);
        //add the file
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            String fileName = fileChooser.getSelectedFile().getName();
            FilePanel filePanel = new FilePanel(fileName);
            //ensure that we don't open duplicates.
            if (fileList.contains(filePath)) {
                JOptionPane.showMessageDialog(frame, "Selected file is already open.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } 
            fileHashtable.put(filePath, filePanel);
            fileList.add(filePath);
            frame.add(filePanel);
            refreshFrame();
        }
    }
    
    /**
     * Removes files from the file list
     * @param file : String, the file to be removed.
     */
    private void removeFile(String _filepath) {
        //todo: implement
        FilePanel _panel = fileHashtable.get(_filepath);
        frame.remove(_panel);
        refreshFrame();
        fileHashtable.remove(_filepath);
        fileList.remove(_filepath);
    }

    private void mergeFiles() {
        String[] files = fileList.toArray(new String[0]);
        JFileChooser fileChooser = new JFileChooser();
        int saveChoice = fileChooser.showSaveDialog(frame);
        if (saveChoice == JFileChooser.APPROVE_OPTION) {
            try {
                PDFMerger.merge(files, fileChooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(frame, "Done", null, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Failed to save file " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshFrame() {
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App();
            }
        });
    }
}
