import javax.swing.*;
import java.awt.GridLayout;

public class FilePanel extends JPanel {

    private JLabel fileLabel;

    public FilePanel(String filename) {
        setLayout(new GridLayout(2,1));

        ImageIcon icon = new ImageIcon(getClass().getResource("images/file.png"));
        JLabel iconLabel = new JLabel(null, icon, JLabel.CENTER);
        add(iconLabel);

        fileLabel = new JLabel(parseFileName(filename));
        add(fileLabel);
    }

    static String parseFileName(String _filename) {
        String s = "";
        if (!(_filename.contains("\\") || _filename.contains("/"))) {
            return _filename;
        }
        if (_filename.contains("\\")) {
            String[] split = _filename.split("\\");
            s = split[split.length - 1];
        } else {
            String[] split = _filename.split("/");
            s = split[split.length - 1];
        }
        return s;
    }
}
