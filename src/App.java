import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.InputEvent;

import java.util.ArrayList;
import java.util.Hashtable;

//TODO: Javadocs.
//TODO: display something when no files are open
//TODO: implement drag and drop in this view
//TODO: set windows look and feel
public class App {

    //Things it would be useful to have global access to
    private JFrame frame;

    private Hashtable<String, FilePanel> fileHashtable;
    
    public App() {
        //set look and feel by platform.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // sqaush
        }

        frame = createFrame();

        fileHashtable = new Hashtable<String, FilePanel>();

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

        return frame;
    }

    private JMenuBar createJMenuBar() {
        JMenuBar jmb = new JMenuBar();
        jmb.add(createFileMenu());
        return jmb;
    }

    private JMenu createFileMenu() {
        JMenu jmFile = new JMenu("File");
        jmFile.setMnemonic('F');

        JMenuItem jmiOpen = new JMenuItem("Open");
        jmiOpen.setMnemonic('O');
        jmiOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        jmiOpen.addActionListener((ae) -> {
        
            //create file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF Documents", "pdf");
            fileChooser.setFileFilter(pdfFilter);

            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                String fileName = fileChooser.getSelectedFile().getName();
                FilePanel filePanel = new FilePanel(fileName);

                fileHashtable.put(filePath, filePanel);
                frame.add(filePanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        jmFile.add(jmiOpen);

        JMenuItem jmiSave = new JMenuItem("Save/Export");
        jmiSave.setMnemonic('S');
        jmiSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        jmiSave.addActionListener((ae) -> {
            //TODO: This is the part where the PDFS will be combined and saved
        });
        jmFile.add(jmiSave);


        return jmFile;
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
