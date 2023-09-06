import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.event.InputEvent;

import java.util.ArrayList;

//TODO: Javadocs.
//TODO: add a gridlayout to display files
//TODO: implement drag and drop in this view
public class App {

    //Things it would be useful to have global access to
    private JFrame frame;
    private ArrayList<String> fileList;
    
    public App() {
        
        frame = createFrame();
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
                
                //add file to list
                fileList.add(filePath);
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
    private void removeFile(String file) {
        int index = -1;
        index = fileList.indexOf(file);
        if (index == -1) {
            System.out.println("Error: Tried to remove a file not in the list");
            return;
        }
        fileList.remove(index);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App();
            }
        });
    }
}
