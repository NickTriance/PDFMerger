import javax.swing.*;

import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;

import java.io.IOException;

//TODO: Javadocs.
//TODO: Make file panel not huge.

public class App {

    //Things it would be useful to have global access to
    private JFrame frame;
    private JLabel noFilesLabel;

    //singleton
    public static App app;

    //Create the app
    public App() {

        //set look and feel by platform.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // sqaush
        }

        //initialize file manager
        FileManager.init(this);

        //set up singleton
        if (app == null) {
            app = this;
        } else {
            System.exit(1);
        }

        //create the frame
        frame = createFrame();

        //make the app visible
        frame.setVisible(true);
    }

    /**
     * Creates the frame for the app
     * @return JFrame: The app's frame
     */
    private JFrame createFrame() {

        //create and setup frame
        JFrame frame = new JFrame();
        frame.setSize(640,480);
        frame.setTitle(AppStrings.APP_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
        frame.setJMenuBar(createJMenuBar());
        
        noFilesLabel = createNoFileLabel();
        frame.add(noFilesLabel);

        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        //handlers for drag and drop stuff
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

    /**
     * Create the menu bar for the app
     * @return JMenuBar: The app's menu bar
     */
    private JMenuBar createJMenuBar() {
        JMenuBar jmb = new JMenuBar();
        jmb.add(createFileMenu());
        jmb.add(createHelpMenu());
        return jmb;
    }

    /**
     * Create the label for when there are no files open
     * @return JLabel: the no file label
     */
    private JLabel createNoFileLabel() {
        JLabel _jlab = new JLabel(AppStrings.APP_NO_FILES);
        _jlab.setFont(new Font(_jlab.getFont().getName(), Font.PLAIN, AppConstants.NO_FILE_LABEL_SIZE));
        _jlab.setHorizontalAlignment(JLabel.CENTER);
        return _jlab;
    }

    /**
     * Creates the File menu on the menu bar.
     * @return FileMenu: JMenu
     */
    private JMenu createFileMenu() {
        JMenu jmFile = new JMenu(AppStrings.APP_MENU_FILE);
        jmFile.setMnemonic(AppConstants.APP_MNEMONIC_FILE);

        //create the menu for opening files
        JMenuItem jmiOpen = new JMenuItem(AppStrings.APP_MENU_FILE_OPEN);
        jmiOpen.setMnemonic(AppConstants.APP_MNEMONIC_OPEN);
        jmiOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        jmiOpen.addActionListener((ae) -> {
            addFile();
        });
        jmFile.add(jmiOpen);

        JMenuItem jmiSave = new JMenuItem(AppStrings.APP_MENU_FILE_SAVE);
        jmiSave.setMnemonic(AppConstants.APP_MNEMONIC_SAVE);
        jmiSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        jmiSave.addActionListener((ae) -> {
            mergeFiles();
        });
        jmFile.add(jmiSave);

        return jmFile;
    }

    /**
     * creates the Help menu for the app.
     * @return Help menu: JMenu
     */
    private JMenu createHelpMenu() {
        JMenu jmHelp = new JMenu(AppStrings.APP_MENU_HELP);
        jmHelp.setMnemonic(AppConstants.APP_MNEMONIC_HELP);

        JMenuItem jmiHelp = new JMenuItem(AppStrings.APP_MENU_HELP_VIEW);
        jmiHelp.setMnemonic(AppConstants.APP_MNEMONIC_HELP);
        jmiHelp.addActionListener((ae) -> {
            JOptionPane.showMessageDialog(frame, AppStrings.APP_MENU_HELP_VIEW_STRING, AppStrings.APP_MENU_HELP, JOptionPane.INFORMATION_MESSAGE);
        });
        jmHelp.add(jmiHelp);

        jmHelp.addSeparator();

        JMenuItem jmiAbout = new JMenuItem(AppStrings.APP_MENU_HELP_ABOUT);
        jmiAbout.setMnemonic(AppConstants.APP_MNEMONIC_ABOUT);
        jmiAbout.addActionListener((ae) -> {
            JOptionPane.showMessageDialog(frame, AppStrings.APP_MENU_HELP_ABOUT_STRING, AppStrings.APP_MENU_HELP_ABOUT_TITLE, JOptionPane.INFORMATION_MESSAGE);
        });
        jmHelp.add(jmiAbout);

        return jmHelp;
    }
 
    /**Adds files to the file list. */
    private void addFile() {
        frame.remove(noFilesLabel);
        FileManager.openFile();
    }
    
    /**
     * Removes files from the file list
     * @param file : String, the file to be removed.
     */
    public void removeFile(String _filepath) {
        FileManager.removeFile(_filepath);
        if (FileManager.getFileListSize() == 0) {
            frame.add(noFilesLabel);
            refreshFrame();
        }
    }

    /** Merges all open files together */
    private void mergeFiles() {
        FileManager.mergeFiles();
    }

    /** Redraws the frame */
    public void refreshFrame() {
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Get app's frame
     * @return JFrame: the app's frame.
     */
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
