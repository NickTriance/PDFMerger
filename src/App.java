import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.event.InputEvent;


public class App {

    //Things it would be useful to have global access to
    private JFrame frame;
    
    public App() {
        
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
            //TODO: Open files
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
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App();
            }
        });
    }
}
