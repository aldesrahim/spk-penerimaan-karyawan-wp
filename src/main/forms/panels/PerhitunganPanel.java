package main.forms.panels;

import com.formdev.flatlaf.FlatClientProperties;
import main.Application;
import main.enums.Gender;
import main.enums.Religion;
import main.models.Vacancy;
import main.services.CalculationService;
import main.util.Dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 *
 * @author aldes
 */
public class PerhitunganPanel extends javax.swing.JPanel {

    private Connection conn;
    private DefaultTableModel tableModelEvaluation;
    private static DefaultTableModel tableModelCalculation;
    private Vacancy currentVacancy;

    /**
     * Creates new form PerhitunganPanel
     */
    public PerhitunganPanel() {
        this.conn = Application.getDBConnection();

        initComponents();
        
        header.setTitleText("Perhitungan");

        formPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "background:darken(@background, 10%)");

        btnCalculate.setVisible(false);
        btnCancel.setVisible(false);
        btnDelete.setColor("danger");

        gVacancy.setTitleText("Lowongan Pekerjaan");

        progressBar.setVisible(false);
        progressLabel.setVisible(false);

        initCbVacancies();
        initTableEvaluation();

        initTableCalculation();
        fillTableCalculation();
    }

    private void initCbVacancies() {
        try {
            String sql = "SELECT * FROM vacancies ORDER BY id";
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            gVacancy.getInputField().removeAllItems();

            while (rs.next()) {
                Vacancy vacancy = new Vacancy();
                vacancy.setId(rs.getInt("id"));
                vacancy.setPosition(rs.getString("position"));
                vacancy.setQuota(rs.getInt("quota"));

                gVacancy.getInputField().addItem(vacancy);
            }
        } catch (Exception e) {
            System.err.println("error while init cb vacancies: " + e.getMessage());
        }
    }

    private void initTableEvaluation() {
        String[] headers = new String[]{"Nama", "No.HP", "Agama", "Jenis Kelamin", "Alamat", "Tanggal Lahir"};

        tableModelEvaluation = new DefaultTableModel(null, headers);
        tableEvaluation.setModel(tableModelEvaluation);
    }

    private void initTableCalculation() {
        String[] headers = new String[]{"Lowongan", "Nama", "No.HP", "Agama", "Jenis Kelamin", "Alamat", "Tanggal Lahir", "Ranking"};

        tableModelCalculation = new DefaultTableModel(null, headers);
        tableCalculation.setModel(tableModelCalculation);
    }

    private void fillTableEvaluation(int vacancyId) throws SQLException {
        String sql = "SELECT * FROM applicants" +
                " WHERE EXISTS (SELECT * FROM evaluations WHERE evaluations.applicant_id = applicants.id)" +
                " AND applicants.vacancy_id = ?";

        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.setInt(1, vacancyId);
        ResultSet rs = stmt.executeQuery();

        tableModelEvaluation.setRowCount(0);

        while (rs.next()) {
            tableModelEvaluation.addRow(new String[]{
                    rs.getString("name"),
                    rs.getString("phone_number"),
                    Religion.fromString(rs.getString("religion")).toString(),
                    Gender.fromInt(rs.getInt("gender")).toString(),
                    rs.getString("address"),
                    rs.getDate("dob").toString(),
            });
        }

        rs.close();
        stmt.close();
    }

    public static void fillTableCalculation() {
        String sql = "SELECT" +
                " applicants.name," +
                " applicants.phone_number," +
                " applicants.religion," +
                " applicants.gender," +
                " applicants.address," +
                " applicants.dob," +
                " vacancies.position" +
                " FROM calculations" +
                " JOIN evaluations ON evaluations.id = calculations.evaluation_id" +
                " JOIN applicants ON applicants.id = evaluations.applicant_id" +
                " JOIN vacancies ON vacancies.id = applicants.vacancy_id" +
                " ORDER BY calculations.v";

        try {
            Statement stmt = Application.getDBConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            tableModelCalculation.setRowCount(0);
            int iteration = 1;

            while (rs.next()) {
                tableModelCalculation.addRow(new String[]{
                        rs.getString("position"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        Religion.fromString(rs.getString("religion")).toString(),
                        Gender.fromInt(rs.getInt("gender")).toString(),
                        rs.getString("address"),
                        rs.getDate("dob").toString(),
                        String.valueOf(iteration++),
                });
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("error while filling table calculation: " + e.getMessage());
        }
    }

    private int getEvaluationCount(int vacancyId) {
        int count = 0;

        try {
            String sql = "SELECT COUNT(*) AS evaluation_count FROM evaluations" +
                    " WHERE EXISTS(" +
                    " SELECT * FROM applicants WHERE applicants.id = evaluations.applicant_id AND vacancy_id = ?" +
                    " )";

            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, vacancyId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                 count = rs.getInt("evaluation_count");
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.err.println("error when getting evaluation count: " + e.getMessage());
        }

        return count;
    }

    public void deleteCalculationByVacancyId(int vacancyId) throws SQLException {
        try {
            conn.setAutoCommit(false);

            String sql = "DELETE FROM calculations" +
                    " WHERE EXISTS (" +
                    " SELECT * FROM evaluations WHERE evaluations.id = calculations.evaluation_id" +
                    " AND EXISTS ( SELECT * FROM applicants WHERE applicants.id = evaluations.applicant_id AND applicants.vacancy_id = ? )" +
                    " )";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vacancyId);
            stmt.executeUpdate();

            CalculationService.recalculate_V();

            conn.commit();
        } catch (Exception e) {
            conn.rollback();

            throw e;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header = new main.components.Header();
        formPanel = new javax.swing.JPanel();
        gVacancy = new main.components.ComboBoxInputGroup();
        btnSearch = new main.components.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableEvaluation = new javax.swing.JTable();
        btnCalculate = new main.components.Button();
        btnDelete = new main.components.Button();
        btnCancel = new main.components.Button();
        progressLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableCalculation = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));

        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 7, 7, 7));

        btnSearch.setText("Cari");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        tableEvaluation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableEvaluation);

        btnCalculate.setText("Hitung");
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });

        btnDelete.setText("Batalkan Perhitungan");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnCancel.setText("Batal");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        progressLabel.setText("Mengitung x dari xx penilaian");

        javax.swing.GroupLayout formPanelLayout = new javax.swing.GroupLayout(formPanel);
        formPanel.setLayout(formPanelLayout);
        formPanelLayout.setHorizontalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gVacancy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(progressLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                .addContainerGap())
        );
        formPanelLayout.setVerticalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addComponent(gVacancy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(progressLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tableCalculation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tableCalculation);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addComponent(header, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(formPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void setCurrentVacancyState(Vacancy vacancy) {
        this.currentVacancy = vacancy;

        btnSearch.setEnabled(false);
        btnCancel.setVisible(true);
        btnCalculate.setVisible(true);
        gVacancy.getInputField().setEnabled(false);
    }

    private void resetCurrentVacancyState() {
        this.currentVacancy = null;

        btnSearch.setEnabled(true);
        btnCancel.setVisible(false);
        btnCalculate.setVisible(false);
        gVacancy.getInputField().setEnabled(true);

        tableModelEvaluation.setRowCount(0);
    }

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        Vacancy vacancy = (Vacancy) gVacancy.getSelectedItem();

        int evaluationCount = getEvaluationCount(vacancy.getId());
        if (evaluationCount == 0) {
            Dialog errorDialog = new Dialog();
            errorDialog.setMessage("Tidak ada penilaian untuk lowongan " + vacancy.getPosition());
            errorDialog.show(this);

            return;
        }

        try {
            setCurrentVacancyState(vacancy);
            fillTableEvaluation(vacancy.getId());
        } catch (Exception e) {
            System.err.println("error when searching: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        if (this.currentVacancy == null) {
            return;
        }

        boolean skipConfirmation = false;

        try {
            if (Application.isCalculationExists(currentVacancy.getId())) {
                Dialog confirm = new Dialog();
                confirm.setMessage("Lowongan " + currentVacancy.getPosition() + " sudah pernah dihitung. Apakah Anda tetap ingin menghitung ulang?");
                confirm.setMessageType(JOptionPane.QUESTION_MESSAGE);
                confirm.setOptionType(JOptionPane.YES_NO_CANCEL_OPTION);

                if (!confirm.show(this).equals(JOptionPane.YES_OPTION)) {
                    return;
                }

                deleteCalculationByVacancyId(currentVacancy.getId());
                skipConfirmation = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!skipConfirmation) {
            Dialog confirm = new Dialog();
            confirm.setMessageType(JOptionPane.QUESTION_MESSAGE);
            confirm.setOptionType(JOptionPane.YES_NO_CANCEL_OPTION);
            confirm.setMessage("Apakah Anda yakin ingin melakukan perhitungan untuk lowongan " + this.currentVacancy.getPosition() + " ?");

            if (!confirm.show(this).equals(JOptionPane.YES_OPTION)) {
                return;
            }
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new CalculationService.TaskCalculation(progressBar, progressLabel, currentVacancy.getId()).execute();
                resetCurrentVacancyState();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }//GEN-LAST:event_btnCalculateActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        resetCurrentVacancyState();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        Vacancy vacancy = (Vacancy) gVacancy.getSelectedItem();

        Dialog confirm = new Dialog();
        confirm.setMessageType(JOptionPane.QUESTION_MESSAGE);
        confirm.setOptionType(JOptionPane.YES_NO_CANCEL_OPTION);
        confirm.setMessage("Apakah Anda yakin ingin membatalkan perhitungan untuk lowongan " + vacancy.getPosition() + " ?");

        if (!confirm.show(this).equals(JOptionPane.YES_OPTION)) {
            return;
        }

        try {
            deleteCalculationByVacancyId(vacancy.getId());
            fillTableCalculation();

            Dialog dialog = new Dialog("Berhasil");
            dialog.setMessage("Data berhasil dihapus");
            dialog.show(this);
        } catch (Exception e) {
            System.err.println("error when deleting: " + e.getMessage());
        }
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private main.components.Button btnCalculate;
    private main.components.Button btnCancel;
    private main.components.Button btnDelete;
    private main.components.Button btnSearch;
    private javax.swing.JPanel formPanel;
    private main.components.ComboBoxInputGroup gVacancy;
    private main.components.Header header;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JTable tableCalculation;
    private javax.swing.JTable tableEvaluation;
    // End of variables declaration//GEN-END:variables
}
