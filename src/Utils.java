import javax.swing.ImageIcon;

import java.awt.*;

public class Utils {

    /**
     * Scale an ImageIcon to a specified width and height.
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
     * Gets the name of a file from its path.
     * @param filename : String to parse.
     * @return String : name of file.
     */
    public static String parseFileName(String filename) {
        String s = "";
        if (!(filename.contains("\\") || filename.contains("/"))) {
            return filename;
        }
        if (filename.contains("\\")) {
            String[] split = filename.split("\\");
            s = split[split.length - 1];
        } else {
            String[] split = filename.split("/");
            s = split[split.length - 1];
        }
        return s;
    }
}
