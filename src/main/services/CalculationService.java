package main.services;

import main.Application;
import main.forms.panels.PerhitunganPanel;

import javax.swing.*;
import java.sql.*;
import java.util.List;

public class CalculationService {
    public static Connection conn = Application.getDBConnection();

    public static int getEvaluationCount(int vacancyId) throws SQLException {
        String sql = "SELECT COUNT(*) AS evaluation_count FROM applicants" +
                " WHERE EXISTS (SELECT * FROM evaluations WHERE evaluations.applicant_id = applicants.id)" +
                " AND applicants.vacancy_id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, vacancyId);
        ResultSet rs = stmt.executeQuery();

        int count = 0;

        if (rs.next()) {
            count = rs.getInt("evaluation_count");
        }

        return count;
    }

    public static double getCalculation_S(int evaluationId) throws SQLException {
        String sql = "SELECT" +
                " evaluation_details.*," +
                " (criterias.attribute * criterias.normalization) AS normalization," +
                " weights.weight AS criteria_weight," +
                " sub_weights.weight AS sub_criteria_weight" +
                " FROM evaluation_details" +
                " JOIN criterias ON criterias.id = evaluation_details.criteria_id" +
                " JOIN weights ON weights.id = criterias.weight_id" +
                " JOIN sub_criterias ON sub_criterias.id = evaluation_details.sub_criteria_id" +
                " JOIN weights sub_weights ON sub_weights.id = sub_criterias.weight_id" +
                " WHERE evaluation_details.evaluation_id = ?";

        System.out.println(sql);

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, evaluationId);
        ResultSet rs = stmt.executeQuery();

        Double total = null;

        while (rs.next()) {
            double subWeight = rs.getDouble("sub_criteria_weight");
            double normalization = rs.getDouble("normalization");
            double s = Math.pow(subWeight, normalization);

            if (total == null) {
                total = s;
            } else {
                total *= s;
            }

            System.out.println("subWeight: " + subWeight + "; normalization: " + normalization + "; s: " + s);
        }

        System.out.println("total: " + total);

        if (total == null) {
            total = 0.0;
        }

        return total;
    }

    public static void recalculate_V() throws SQLException {
        String sql = "UPDATE calculations" +
                " JOIN (SELECT SUM(s) AS sum_s FROM calculations) AS summary" +
                " SET v = s / sum_s";

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
    }

    public static class TaskCalculation extends SwingWorker<Void, Integer> {

        JProgressBar progressBar;
        JLabel progressLabel;
        int vacancyId;
        int max;

        public TaskCalculation(JProgressBar progressBar, JLabel progressLabel, int vacancyId) throws SQLException {
            this.progressBar = progressBar;
            this.progressLabel = progressLabel;
            this.vacancyId = vacancyId;
            this.max = CalculationService.getEvaluationCount(vacancyId) + 1;
            this.progressBar.setMaximum(this.max);

            progressBar.setVisible(true);
            progressLabel.setVisible(true);

            progressLabel.setText("Menghitung 0 dari " + max + " penilaian");
        }

        public void sleepWhenDataIsTiny() throws InterruptedException {
            if (max < 10) {
                Thread.sleep(350);
            } else if (max < 50) {
                Thread.sleep(250);
            } else if (max < 100) {
                Thread.sleep(150);
            } else if (max < 150) {
                Thread.sleep(50);
            }
        }

        @Override
        protected void process(List<Integer> chunks) {
            int size = chunks.size();
            int iteration = chunks.get(size - 1);
            progressBar.setValue(iteration); // The last value in this array is all we care about.

            System.out.println("iteration: " + iteration);

            if (iteration == max) {
                progressLabel.setText("Menghitung nilai V data yang sudah masuk");
            } else {
                progressLabel.setText("Menghitung " + iteration + " dari " + max + " penilaian");
            }
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                conn.setAutoCommit(false);

                String sql = "SELECT * FROM evaluations" +
                        " WHERE EXISTS (SELECT * FROM applicants WHERE evaluations.applicant_id = applicants.id AND applicants.vacancy_id = ?)";

                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, vacancyId);
                ResultSet rs = stmt.executeQuery();

                int iteration = 1;

                while (rs.next()) {
                    int evaluationId = rs.getInt("id");

                    String insertSql = "INSERT INTO calculations (evaluation_id, s) VALUES (?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.setInt(1, evaluationId);
                    insertStmt.setDouble(2, CalculationService.getCalculation_S(evaluationId));
                    insertStmt.executeUpdate();

                    publish(iteration++);

                    sleepWhenDataIsTiny();
                }

                // hitung V
                CalculationService.recalculate_V();

                publish(iteration);

                sleepWhenDataIsTiny();

                conn.commit();
            } catch (Exception e) {
                conn.rollback();

                System.err.println("error when calculating S and V: " + e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void done() {
            try {
                get();

                progressBar.setVisible(false);
                progressLabel.setVisible(false);

                JOptionPane.showMessageDialog(
                        progressBar.getRootPane().getContentPane(),
                        "Perhitungan berhasil disimpan",
                        "Berhasil",
                        JOptionPane.INFORMATION_MESSAGE
                );

                PerhitunganPanel.fillTableCalculation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
