import javax.swing.*;

public class AppContextMenu extends JPopupMenu {

    private FilePanel panel;
    
    public AppContextMenu() {
        JMenuItem jpmDel = new JMenuItem(AppStrings.CONTEXT_MENU_DELETE);
        jpmDel.setMnemonic('R');
        jpmDel.addActionListener((ae) -> {
            App.app.removeFile(panel.getPath());
        });
        add(jpmDel);
    } 

    public void setPanel(FilePanel _panel) {
        this.panel = _panel;
    }
}
