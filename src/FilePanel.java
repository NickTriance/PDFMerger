import javax.swing.*;
import java.awt.GridLayout;
public class FilePanel extends JPanel {
    
    private String fileName;

    private JPanel panel;
    private JLabel icon;
    private JLabel label;

    public FilePanel(String filename) {
        super();

        if (filename.contains("\\") || filename.contains("/")) {
            this.fileName = parseFileName(fileName);
        } else {
            this.fileName = filename;
        }
        
        panel = createPanel();
    }

    private JPanel createPanel() {
        JPanel _jp = new JPanel();
        _jp.setLayout(new GridLayout(1,2));

        ImageIcon _ico = new ImageIcon(this.getClass().getResource("/images/file.png"));
        icon = new JLabel(_ico);

        label = new JLabel(fileName);

        _jp.add(icon);
        _jp.add(label);
        
        _jp.setVisible(true);

        return _jp;
    }

    static String parseFileName(String _fileName) {
        String s = "";
        if (_fileName.contains("\\")) {
            String[] split = _fileName.split("\\");
            s = split[s.length() - 1];
        } else {
            String[] split = _fileName.split("/");
            s = split[s.length() - 1];
        }
        return s;
    }
}
