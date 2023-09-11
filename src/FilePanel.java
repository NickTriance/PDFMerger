import javax.swing.*;

import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FilePanel extends JPanel {

    //components of the panel
    private JLabel fileLabel;
    private String fileName;

    //mouse offset for drag and drop
    private Point mouseOffset;

    //path of the file this panel belongs to
    private String filepath;

    /**Create a FilePanel for the given filename
     * @param filename : String, the filename to assign to this panel
     */
    public FilePanel(String filename) {

        //create panel
        setLayout(new GridLayout(2,1));
        setBorder(BorderFactory.createLineBorder(AppConstants.PANEL_BORDER, AppConstants.PANEL_BORDER_THICKNESS));

        ImageIcon icon = new ImageIcon(getClass().getResource("images/file.png"));
        JLabel iconLabel = new JLabel(null, icon, JLabel.CENTER);
        add(iconLabel);

        fileName = parseFileName(filename);
        fileLabel = new JLabel(fileName, JLabel.CENTER);
        fileLabel.setFont(new Font(fileLabel.getFont().getName(), Font.PLAIN, AppConstants.FILE_LABEL_SIZE));
        fileLabel.setHorizontalAlignment(JLabel.CENTER);
        add(fileLabel);

        

        //add a mouse listeners 
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseOffset = new Point(e.getPoint().x, e.getPoint().y);
                FilePanel.this.getParent().setComponentZOrder(FilePanel.this, 0);
                setBorder(BorderFactory.createLineBorder(AppConstants.PANEL_BORDER_SELECTED, AppConstants.PANEL_BORDER_THICKNESS));
            }
            
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point newLocation = e.getLocationOnScreen();
                newLocation.translate(-mouseOffset.x, -mouseOffset.y);
                
                //align to nearest grid cell
                int newX = (int) (Math.round((double) newLocation.x / AppConstants.GRID_CELL_SIZE) * AppConstants.GRID_CELL_SIZE);
                int newY = (int) (Math.round((double) newLocation.y / AppConstants.GRID_CELL_SIZE) * AppConstants.GRID_CELL_SIZE);

                FilePanel.this.setLocation(newX, newY);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                FileManager.insertFileDragAndDrop(filepath);
                setBorder(BorderFactory.createLineBorder(AppConstants.PANEL_BORDER, AppConstants.PANEL_BORDER_THICKNESS));
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void  mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    AppContextMenu ctxMenu = new AppContextMenu();
                    FilePanel clickedPanel = (FilePanel) e.getSource();
                    ctxMenu.setPanel(clickedPanel);
                    ctxMenu.show(clickedPanel, e.getX(), e.getY());
                }
            }
        });
    }

    /**
     * Set the path this panel is associated with
     * @param path : String, path of the file
     */
    public void setPath(String path) {
        this.filepath = path;
    }

    /**
     * Get the path this panel is associated with
     * @return String : path of file.
     */
    public String getPath() {
        return this.filepath;
    }

    /**
     * Gets the name of a file from its path
     * @param _filename : String to parse
     * @return String : name of file.
     */
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
