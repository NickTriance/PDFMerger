import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FilePanel extends JPanel {

    private static final int GRID_CELL_SIZE = 500; //size of the grid for drag and drop

    private JLabel fileLabel;
    private String fileName;
    private Point mouseOffset;
    private String path;


    public FilePanel(String filename) {
        setLayout(new GridLayout(2,1));

        ImageIcon icon = new ImageIcon(getClass().getResource("images/file.png"));
        JLabel iconLabel = new JLabel(null, icon, JLabel.CENTER);
        add(iconLabel);

        fileName = parseFileName(filename);
        fileLabel = new JLabel(fileName);
        add(fileLabel);

        //add a mouse listener for drag and drop
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseOffset = new Point(e.getPoint().x, e.getPoint().y);
                FilePanel.this.getParent().setComponentZOrder(FilePanel.this, 0);
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point newLocation = e.getLocationOnScreen();
                newLocation.translate(-mouseOffset.x, -mouseOffset.y);
                
                //align to nearest grid cell
                int newX = (int) (Math.round((double) newLocation.x / GRID_CELL_SIZE) * GRID_CELL_SIZE);
                int newY = (int) (Math.round((double) newLocation.y / GRID_CELL_SIZE) * GRID_CELL_SIZE);

                FilePanel.this.setLocation(newX, newY);
            }
        });
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
