import javax.swing.*;
import java.awt.GridLayout;

public class FilePanel extends JPanel {

    private JLabel fileLabel;
    private String fileName;
    private String path;


    public FilePanel(String filename) {
        setLayout(new GridLayout(2,1));

        ImageIcon icon = new ImageIcon(getClass().getResource("images/file.png"));
        JLabel iconLabel = new JLabel(null, icon, JLabel.CENTER);
        add(iconLabel);

        fileName = parseFileName(filename);
        fileLabel = new JLabel(fileName);
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

    public void setPath(String _path) {
        this.path = _path;
    }
}
