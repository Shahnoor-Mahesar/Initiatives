package my_package;

import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) throws Exception {
        DatabaseConnection.initializeDatabase();
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        new LoginPage();
    }
}
