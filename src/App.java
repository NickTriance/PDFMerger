import javax.swing.*;

import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;

import java.io.IOException;

//TODO: Javadocs.
//TODO: display something when no files are open
//TODO: implement drag and drop in this view
//TODO: should separate file handling logic from the ui
public class App {

    //Things it would be useful to have global access to
    private JFrame frame;

    
    public App() {
        //set look and feel by platform.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // sqaush
        }

        FileManager.init(this);
        frame = createFrame();

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
        FileManager.openFile();
    }
    
    /**
     * Removes files from the file list
     * @param file : String, the file to be removed.
     */
    private void removeFile(String _filepath) {
        FileManager.removeFile(_filepath);
    }

    private void mergeFiles() {
        FileManager.mergeFiles();
    }

    public void refreshFrame() {
        frame.revalidate();
        frame.repaint();
    }

    public JFrame getFrame() {
        return frame;
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App();
            }
        });
    }
}
