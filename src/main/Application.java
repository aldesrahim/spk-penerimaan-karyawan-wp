package main;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.EventQueue;
import java.sql.*;

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
    
    private static void initLookAndFeel() {
        FlatRobotoFont.install();
        FlatInterFont.install();

        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
        FlatLaf.registerCustomDefaultsSource("main.theme");
        FlatLightLaf.setup();
    }

    public static void authLogin(User user) {
        Application.loginForm.dispose();
        
        if (Application.mainForm == null) {
            Application.mainForm = new MainForm();
        }
        
        Application.mainForm.setCurrentUser(user);
        Application.mainForm.setVisible(true);
    }

    public static void authLogout() {
        Application.mainForm.unsetCurrentUser();
        Application.mainForm.dispose();

        Application.loginForm.setVisible(true);
    }

    public static void main(String[] args) {
        Application.initLookAndFeel();
        Application.getDBConnection();
        
        if (Application.loginForm == null) {
            Application.loginForm = new LoginForm();
        }
        
        if (Application.mainForm == null) {
            Application.mainForm = new MainForm();
        }

        EventQueue.invokeLater(() -> {
//            Application.mainForm.setVisible(true);
            Application.loginForm.setVisible(true);
        });
    }
    
    public static void recalculateNormalizations() {
        try {
            String sql = "UPDATE criterias"
                + " JOIN ("
                + " SELECT SUM(weights.weight) AS sum_weight FROM criterias JOIN weights ON criterias.weight_id = weights.id"
                + " ) AS summary"
                + " JOIN weights ON criterias.weight_id = weights.id"
                + " SET criterias.normalization = weights.weight / summary.sum_weight";
        
            Statement stmt = getDBConnection().createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.err.println("error when recalculate normalization: " + e.getMessage());
        }
    }

    public static boolean isEvaluationExists() {
        try {
            String sql = "SELECT * FROM evaluations";
            Statement stmt = getDBConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            return rs.next();
        } catch (Exception e) {
            System.err.println("error validate evaluation existence: " + e.getMessage());
        }

        return false;
    }

    public static boolean isCalculationExists(int vacancyId) {
        try {
            String sql = "SELECT * FROM calculations" +
                    " WHERE EXISTS (SELECT * FROM evaluations WHERE evaluations.id = calculations.evaluation_id" +
                    " AND EXISTS (SELECT * FROM applicants WHERE applicants.id = evaluations.applicant_id AND applicants.vacancy_id = ?)" +
                    " ) LIMIT 1";

            PreparedStatement stmt = getDBConnection().prepareStatement(sql);
            stmt.setInt(1, vacancyId);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            System.err.println("error validate calculation existence: " + e.getMessage());
        }

        return false;
    }

    public static boolean isCalculationApplicantExists(int applicantId) {
        try {
            String sql = "SELECT * FROM calculations" +
                    " WHERE EXISTS (SELECT * FROM evaluations WHERE evaluations.id = calculations.evaluation_id AND evaluations.applicant_id = ?" +
                    " ) LIMIT 1";

            PreparedStatement stmt = getDBConnection().prepareStatement(sql);
            stmt.setInt(1, applicantId);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            System.err.println("error validate calculation existence by applicant: " + e.getMessage());
        }

        return false;
    }

    public static boolean isCalculationExists() {
        try {
            String sql = "SELECT * FROM calculations LIMIT 1";

            Statement stmt = getDBConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            return rs.next();

        } catch (Exception e) {
            System.err.println("error validate calculation existence: " + e.getMessage());
        }

        return false;
    }
}
