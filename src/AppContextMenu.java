import javax.swing.*;

public class AppContextMenu extends JPopupMenu {

    private FilePanel panel; //panel that created this popup menu
    
    /**Creates a context menu */
    public AppContextMenu() {
        JMenuItem jpmDel = new JMenuItem(AppStrings.CONTEXT_MENU_DELETE);
        jpmDel.setMnemonic('R');
        jpmDel.addActionListener((ae) -> {
            App.app.removeFile(panel.getPath());
        });
        add(jpmDel);
    } 

    /**
     * set the panel that created this popup menu
     * @param _panel : FilePanel, panel that created this popup menu
     */
    public void setPanel(FilePanel _panel) {
        this.panel = _panel;
    }
}
