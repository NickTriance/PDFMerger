import javax.swing.*;

/**
 * Custom context menu for this app. Used to allow users to remove PDF files from the merge.
 */
public class AppContextMenu extends JPopupMenu {

    private FilePanel panel; // The panel that created this popup menu.
    
    /** Creates a context menu. */
    public AppContextMenu() {
        JMenuItem jpmDel = new JMenuItem(AppStrings.CONTEXT_MENU_DELETE);
        jpmDel.setMnemonic('R');
        jpmDel.addActionListener((ae) -> {
            App.app.removeFile(panel.getPath());
        });
        add(jpmDel);
    } 

    /**
     * Set the panel that created this popup menu.
     * @param panel : FilePanel, panel that created this popup menu.
     */
    public void setPanel(FilePanel panel) {
        this.panel = panel;
    }
}
