import com.pms.ui.LoginFrame;
import com.pms.ui.UITheme;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UITheme.apply();
            new LoginFrame().setVisible(true);
        });
    }
}
