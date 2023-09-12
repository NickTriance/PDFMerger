import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import java.awt.*;

public class Utils {

    /**
     * Scale an ImageIcon to a specified width and height
     * @param icon : ImageIcon, icon to scale
     * @param width : int, width of scaled icon
     * @param height int, height of scaled icon
     * @return ImageIcon: scaled icon
     */
    public static ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    /**
     * Gets the name of a file from its path
     * @param _filename : String to parse
     * @return String : name of file.
     */
    public static String parseFileName(String _filename) {
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

    public static Point convertPointToParentLocal (Point point, Container child, Container parent) {
        
        SwingUtilities.convertPointToScreen(point, child);
        SwingUtilities.convertPointFromScreen(point, parent);

        Point _ret = point; //I don't know if this is required
        return _ret;
    }
}
