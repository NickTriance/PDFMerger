import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App {
    
    public App() {
        JFrame frame = new JFrame();
        frame.setSize(640,480);
        frame.setTitle("PDFMerger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App();
            }
        });
    }
}
