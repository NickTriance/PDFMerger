import javax.swing.*;

import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.InputEvent;

public class App {

    // Things it would be useful to have global access to...
    private JFrame frame;
    private JLabel noFilesLabel;

    // Singleton
    public static App app;

    // Create the app.
    public App() {

        // Set look and feel by platform.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // sqaush
        }

        // Initialize file manager
        FileManager.init(this);

        // Set up singleton.
        if (app == null) {
            app = this;
        } else {
            System.exit(1);
        }

        // Create the frame.
        frame = createFrame();

        // Make the app visible.
        frame.setVisible(true);
    }

    /**
     * Creates the frame for the app.
     * @return JFrame: the app's frame.
     */
    private JFrame createFrame() {

        // Create and setup frame.
        JFrame frame = new JFrame();
        frame.setSize(640,480);
        frame.setTitle(AppStrings.APP_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
        frame.setJMenuBar(createJMenuBar());
        
        ImageIcon icon = new ImageIcon(getClass().getResource("images/appicon.png"));
        frame.setIconImage(icon.getImage());
        
        noFilesLabel = createNoFileLabel();
        frame.add(noFilesLabel);

        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        return frame;
    }

    /**
     * Create the menu bar for the app.
     * @return JMenuBar: the app's menu bar.
     */
    private JMenuBar createJMenuBar() {
        JMenuBar jmb = new JMenuBar();
        jmb.add(createFileMenu());
        jmb.add(createHelpMenu());
        return jmb;
    }

    /**
     * Create the label that is displayed when there are currently no files open.
     * @return JLabel: the no file label.
     */
    private JLabel createNoFileLabel() {
        JLabel jlab = new JLabel(AppStrings.APP_NO_FILES);
        jlab.setFont(new Font(jlab.getFont().getName(), Font.PLAIN, AppConstants.NO_FILE_LABEL_SIZE));
        jlab.setHorizontalAlignment(JLabel.CENTER);
        return jlab;
    }

    /**
     * Creates the File menu on the menu bar.
     * @return FileMenu: JMenu
     */
    private JMenu createFileMenu() {
        JMenu jmFile = new JMenu(AppStrings.APP_MENU_FILE);
        jmFile.setMnemonic(AppConstants.APP_MNEMONIC_FILE);

        // Create the menu for opening files
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
     * Creates the Help menu for the app.
     * @return Help menu: JMenu.
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
     * Removes files from the file list.
     * @param file : String, the file to be removed.
     */
    public void removeFile(String file) {
        FileManager.removeFile(file);
        if (FileManager.getFileListSize() == 0) {
            frame.add(noFilesLabel);
            refreshFrame();
        }
    }

    /** Merges all open files together. */
    private void mergeFiles() {
        FileManager.mergeFiles();
    }

    /** Redraws the frame. */
    public void refreshFrame() {
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Get app's frame.
     * @return JFrame: the app's frame.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Entry point.
     */
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App();
            }
        });
    }
}
