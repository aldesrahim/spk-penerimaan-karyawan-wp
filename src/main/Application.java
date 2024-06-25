package main;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.SQLException;
import main.forms.LoginForm;
import main.forms.MainForm;
import main.models.User;
import main.util.Database;

/**
 *
 */
public class Application {

    public static LoginForm loginForm;
    public static MainForm mainForm;
    private static Connection dbConnection;

    public static Connection getDBConnection() {
        if (Application.dbConnection == null) {
            try {
                Database.getInstance().connect();
                Application.dbConnection = Database.getInstance().getConnection();
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println("Gagal terkoneksi ke database: " + ex.getMessage());
            }
        }
        
        return Application.dbConnection;
    }

    private static void initForms() {
        FlatRobotoFont.install();
        FlatInterFont.install();

        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
        FlatLaf.registerCustomDefaultsSource("main.theme");
        FlatLightLaf.setup();
        
        if (Application.loginForm == null) {
            Application.loginForm = new LoginForm();
        }

        if (Application.mainForm == null) {
            Application.mainForm = new MainForm();
        }
    }

    public static void authLogin(User user) {
        Application.loginForm.dispose();
        
        Application.mainForm.setCurrentUser(user);
        Application.mainForm.setVisible(true);
    }

    public static void authLogout() {
        Application.mainForm.unsetCurrentUser();
        Application.mainForm.dispose();

        Application.loginForm.setVisible(true);
    }

    public static void main(String[] args) {
        Application.initForms();
        Application.getDBConnection();

        EventQueue.invokeLater(() -> {
            Application.loginForm.setVisible(true);
        });
    }

}
