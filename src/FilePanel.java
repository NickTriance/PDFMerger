import javax.swing.*;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**Displayed representation of a file to the user. */
public class FilePanel extends JPanel {

    // Components of the panel
    private JTextArea fileLabel;
    private String fileName;

    // Path of the file this panel belongs to
    private String filepath;

    /**Create a FilePanel for the given filename.
     * @param filename : String, the filename to assign to this panel.
     */
    public FilePanel(String filename) {

        // Create panel.
        setLayout(new GridLayout(3,1));
        setBorder(BorderFactory.createLineBorder(AppConstants.PANEL_BORDER, AppConstants.PANEL_BORDER_THICKNESS));
        setPreferredSize(new Dimension(AppConstants.FILE_PANEL_DESIRED_SIZE, AppConstants.FILE_PANEL_DESIRED_SIZE));

        // Create icon for file.
        ImageIcon icon = new ImageIcon(getClass().getResource("images/file.png"));
        ImageIcon scaledIcon = Utils.scaleImageIcon(icon, AppConstants.FILE_ICON_SCALE_SIZE, AppConstants.FILE_ICON_SCALE_SIZE);
        JLabel iconLabel = new JLabel(null, scaledIcon, JLabel.CENTER);
        add(iconLabel);

        // Create label for filename.
        fileName = Utils.parseFileName(filename);
        fileLabel = new JTextArea();
        fileLabel.setEditable(false);
        fileLabel.setText(fileName);
        fileLabel.setLineWrap(true);
        fileLabel.setFont(new Font(fileLabel.getFont().getName(), Font.BOLD, AppConstants.FILE_LABEL_SIZE));
        add(fileLabel);

        // Create a sub-panel to house buttons to re-order files.
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        JButton prevButton = new JButton(AppStrings.APP_NAV_PREV);
        prevButton.addActionListener((ae) -> {
            System.out.println("PREV");
            FileManager.shiftFilePrev(filepath);
        });

        JButton nextButton = new JButton(AppStrings.APP_NAV_NEXT);
        nextButton.addActionListener((ae) -> {
            System.out.println("NEXT");
            FileManager.shiftFileNext(filepath);
        });

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        add(buttonPanel);

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
     * Set the path this panel is associated with.
     * @param path : String, path of the file.
     */
    public void setPath(String path) {
        this.filepath = path;
    }

    /**
     * Get the path this panel is associated with.
     * @return String : path of file.
     */
    public String getPath() {
        return this.filepath;
    }    
}
