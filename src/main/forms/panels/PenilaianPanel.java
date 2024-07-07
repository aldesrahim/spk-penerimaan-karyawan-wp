package main.forms.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.formdev.flatlaf.FlatClientProperties;
import main.Application;
import main.components.ComboBoxInputGroup;
import main.exceptions.DataNotFoundException;
import main.models.*;
import main.util.Dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author aldes
 */
public class PenilaianPanel extends javax.swing.JPanel {

    private Connection conn;
    private Map<Integer, ComboBoxInputGroup<SubCriteria>> detailInputGroups;
    private DefaultTableModel tableModel;

    /**
     * Creates new form PenilaianPanel
     */
    public PenilaianPanel() {
        this.conn = Application.getDBConnection();

        initComponents();

        header.setTitleText("Penilaian");

        formPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "background:darken(@background, 10%)");

        btnDelete.setVisible(false);

        gId.setTitleText("ID");
        gId.getInputField().setEnabled(false);
        gId.getInputField().putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Auto-generate oleh sistem");

        gApplicant.setTitleText("Pelamar");

        initCbApplicants();
        initDetailFormPanel();
        initDataTables();
    }

    private void initCbApplicants() {
        try {
            String sql = "SELECT" +
                    " applicants.*," +
                    " vacancies.position AS v_position" +
                    " FROM applicants" +
                    " JOIN vacancies ON vacancies.id = applicants.vacancy_id";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            gApplicant.getInputField().removeAllItems();

            while (rs.next()) {
                Applicant applicant = new Applicant();
                applicant.setId(rs.getInt("id"));
                applicant.setName(rs.getString("name"));
                applicant.setPhoneNumber(rs.getString("phone_number"));

                Vacancy vacancy = new Vacancy();
                vacancy.setId(rs.getInt("vacancy_id"));
                vacancy.setPosition(rs.getString("v_position"));
                applicant.setVacancy(vacancy);

                gApplicant.getInputField().addItem(applicant);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("error init combobox vacanies: " + e.getMessage());
        }
    }

    private void initDetailFormPanel() {
        detailFormPanel.setOpaque(false);

        detailInputGroups = new HashMap<>();

        try {
            String sql = "SELECT"
                    + " C.id AS c_id,"
                    + " C.name AS c_name,"
                    + " C.attribute AS c_attribute,"
                    + " C.normalization AS c_normalization,"
                    + " C.weight_id AS c_weight_id,"
                    + " W.weight AS c_weight,"
                    + " W.description AS c_weight_description,"
                    + " SC.id AS sc_id,"
                    + " SC.criteria_id AS sc_criteria_id,"
                    + " SC.name AS sc_name,"
                    + " SC.weight_id AS sc_weight_id,"
                    + " SW.weight AS sc_weight,"
                    + " SW.description AS sc_weight_description"
                    + " FROM sub_criterias SC"
                    + " JOIN criterias C ON C.id = SC.criteria_id"
                    + " JOIN weights W ON W.id = C.weight_id"
                    + " JOIN weights SW ON SW.id = SC.weight_id"
                    + " ORDER BY c_weight, sc_weight";

            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            detailFormPanel.removeAll();

            while (rs.next()) {
                Integer cId = rs.getInt("c_id");

                ComboBoxInputGroup<SubCriteria> gItem;

                if (detailInputGroups.containsKey(cId)) {
                    gItem = detailInputGroups.get(cId);
                } else {
                    gItem = new ComboBoxInputGroup();
                    gItem.setTitleText(rs.getString("c_name"));

                    detailFormPanel.add(gItem);
                    detailFormPanel.revalidate();

                    detailInputGroups.put(cId, gItem);
                }

                gItem.getInputField().addItem(new SubCriteria(
                        rs.getInt("sc_id"),
                        rs.getInt("sc_weight_id"),
                        rs.getInt("sc_criteria_id"),
                        rs.getString("sc_name")
                ));
            }

            double gap = 6.0;
            double maxComponents = 4.0;
            double componentsWidth = 0.0;
            double componentsHeight = 0.0;
            double componentSize = detailInputGroups.size();

            for (Component component : detailFormPanel.getComponents()) {
                componentsHeight += component.getPreferredSize().getHeight();
                componentsWidth += component.getPreferredSize().getWidth();
            }

            double componentHeight = componentsHeight / componentSize;
            double componentWidth = componentsWidth / componentSize;

            int rows = (int) Math.ceil(componentSize / maxComponents);
            int height = ((int) Math.ceil(componentHeight * rows)) + ((int) gap * rows);
            int width = ((int) Math.ceil(componentWidth * maxComponents)) + ((int) gap * ((int) maxComponents - 1));

            detailFormPanel.setPreferredSize(new Dimension(width, height));
            detailFormPanel.revalidate();
            detailFormPanel.repaint();
        } catch (Exception e) {
            System.err.println("error init detail form panel: " + e.getMessage());
        }
    }

    public List<Criteria> getCriteriaHeaders() {
        List<Criteria> headers = new ArrayList<>();

        try {
            String sqlHeader = "SELECT * FROM criterias ORDER BY id";

            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlHeader);

            while (rs.next()) {
                Criteria criteria = new Criteria();
                criteria.setId(rs.getInt("id"));
                criteria.setName(rs.getString("name"));

                headers.add(criteria);
            }
        } catch (Exception e) {
            System.err.println("error datatable: " + e.getMessage());
        }

        return headers;
    }

    private void initDataTables() {
        try {
            List<Criteria> criteriaHeaders = getCriteriaHeaders();

            String sql = "SELECT" +
                    " evaluations.*," +
                    " sub_criterias.id AS sub_criteria_id," +
                    " sub_criterias.name AS sub_criteria_name," +
                    " applicants.name AS applicant_name," +
                    " applicants.phone_number AS applicant_phone_number," +
                    " applicants.religion AS applicant_religion," +
                    " applicants.gender AS applicant_gender," +
                    " applicants.address AS applicant_address," +
                    " applicants.dob AS applicant_dob," +
                    " vacancies.id AS vacancy_id," +
                    " vacancies.position AS vacancy_position," +
                    " evaluation_details.id AS evaluation_detail_id," +
                    " evaluation_details.criteria_id" +
                    " FROM evaluations" +
                    " JOIN evaluation_details ON evaluation_details.evaluation_id = evaluations.id" +
                    " JOIN applicants ON applicants.id = evaluations.applicant_id" +
                    " JOIN vacancies ON vacancies.id = applicants.vacancy_id" +
                    " JOIN sub_criterias ON sub_criterias.id = evaluation_details.sub_criteria_id" +
                    " ORDER BY evaluations.id DESC, evaluation_details.criteria_id ASC";

            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            int rowIndex = 0;
            int columnSize = criteriaHeaders.size() + 4;

            String[] headers = new String[columnSize];

            headers[rowIndex++] = "ID";
            headers[rowIndex++] = "Nama Pelamar";
            headers[rowIndex++] = "No.HP Pelamar";
            headers[rowIndex++] = "Posisi Pelamar";

            for (Criteria criteria : criteriaHeaders) {
                headers[rowIndex++] = criteria.toString();
            }

            tableModel = new DefaultTableModel(null, headers);
            table.setModel(tableModel);

            String[] rows = new String[columnSize];

            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String applicantName = rs.getString("applicant_name");
                String applicantPhoneNumber = rs.getString("applicant_phone_number");
                String vacancyPosition = rs.getString("vacancy_position");

                if (rows[0] == null || !rows[0].equalsIgnoreCase(id)) {

                    /// jika iterasi sudah beda evaluation id, maka tambah row sebelumnya ke model
                    if (rows[0] != null) {
                        tableModel.addRow(rows);
                    }

                    rowIndex = 0;

                    rows[rowIndex++] = id;
                    rows[rowIndex++] = applicantName;
                    rows[rowIndex++] = applicantPhoneNumber;
                    rows[rowIndex++] = vacancyPosition;
                }

                rows[rowIndex++] = rs.getString("sub_criteria_name");
            }

            /// tambah row terakhir ke model
            if (rows[0] != null) {
                tableModel.addRow(rows);
            }
        } catch (Exception e) {
            System.err.println("error init data tables: " + e.getMessage());
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
        gId = new main.components.NumberInputGroup();
        gApplicant = new main.components.ComboBoxInputGroup();
        btnSave = new main.components.Button();
        btnCancel = new main.components.Button();
        btnDelete = new main.components.Button();
        detailFormPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));

        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 7, 7, 7));

        btnSave.setText("Simpan");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setText("Batal");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnDelete.setText("Hapus");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        detailFormPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 6, 5));

        javax.swing.GroupLayout formPanelLayout = new javax.swing.GroupLayout(formPanel);
        formPanel.setLayout(formPanelLayout);
        formPanelLayout.setHorizontalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addComponent(gId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gApplicant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(detailFormPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 824, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        formPanelLayout.setVerticalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(gApplicant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detailFormPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        table.setModel(new javax.swing.table.DefaultTableModel(
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
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(formPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    public Evaluation getById(String id) throws Exception {
        String sql = "SELECT" +
                " evaluations.*," +
                " evaluation_details.id AS evaluation_detail_id," +
                " evaluation_details.criteria_id," +
                " evaluation_details.sub_criteria_id" +
                " FROM evaluations" +
                " JOIN evaluation_details ON evaluation_details.evaluation_id = evaluations.id" +
                " WHERE evaluations.id = ?";

        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(id));
        ResultSet rs = stmt.executeQuery();

        Evaluation evaluation = new Evaluation();
        List<EvaluationDetail> evaluationDetails = new ArrayList<>();

        while (rs.next()) {
            if (evaluation.getId() == null) {
                evaluation.setId(rs.getInt("id"));
                evaluation.setApplicantId(rs.getInt("applicant_id"));
            }

            evaluationDetails.add(new EvaluationDetail(
                    rs.getInt("evaluation_detail_id"),
                    rs.getInt("id"),
                    rs.getInt("criteria_id"),
                    rs.getInt("sub_criteria_id")
            ));
        }

        evaluation.setEvaluationDetails(evaluationDetails);

        if (evaluation.getId() == null) {
            Dialog dialog = new Dialog();
            dialog.setMessage("Data tidak ditemukan");
            dialog.setMessageType(JOptionPane.ERROR_MESSAGE);
            dialog.show(this);

            rs.close();
            stmt.close();

            throw new DataNotFoundException("Data tidak ditemukan");
        }

        rs.close();
        stmt.close();

        return evaluation;
    }

    private Evaluation getDuplicate() throws SQLException {
        String id = gId.getInputValue();
        Applicant applicant = (Applicant) gApplicant.getSelectedItem();

        String sql = "SELECT * FROM evaluations WHERE applicant_id = ?";
        PreparedStatement stmt = this.conn.prepareStatement(sql);

        stmt.setInt(1, applicant.getId());
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            stmt.close();
            rs.close();

            return null;
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setId(rs.getInt("id"));
        evaluation.setApplicantId(rs.getInt("applicant_id"));

        stmt.close();
        rs.close();

        return evaluation;
    }

    private void fillForm(String id) {
        try {
            Evaluation evaluation = getById(id);

            gId.getInputField().setText(evaluation.getId().toString());

            Applicant applicant = new Applicant();
            applicant.setId(evaluation.getApplicantId());
            gApplicant.getInputField().setSelectedItem(applicant);

            for (EvaluationDetail evaluationDetail : evaluation.getEvaluationDetails()) {
                ComboBoxInputGroup<SubCriteria> inputGroup = detailInputGroups.get(evaluationDetail.getCriteriaId());

                SubCriteria subCriteria = new SubCriteria();
                subCriteria.setId(evaluationDetail.getSubCriteriaId());
                inputGroup.getInputField().setSelectedItem(subCriteria);
            }

            btnDelete.setVisible(true);
        } catch (Exception e) {
            System.err.println("error fill form: " + e.getMessage());
        }
    }

    private void resetForm() {
        gId.getInputField().setText("");
        gApplicant.getInputField().setSelectedIndex(0);

        detailInputGroups.forEach((key, inputGroup) -> {
            inputGroup.getInputField().setSelectedIndex(0);
        });

        btnDelete.setVisible(false);
    }

    private void deleteDetails(String id) {
        try {
            String sql = "DELETE FROM evaluation_details WHERE evaluation_id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("error delete details: " + e.getMessage());
        }
    }

    private void save() throws Exception {
        String id = gId.getInputValue();
        Applicant applicant = (Applicant) gApplicant.getSelectedItem();

        if (applicant == null) {
            Dialog errorDialog = new Dialog();
            errorDialog.setMessage("Pelamar tidak boleh kosong");
            errorDialog.show(this);

            return;
        }

        try {
            this.conn.setAutoCommit(false);

            if (id.isEmpty()) {
                Evaluation duplicate = getDuplicate();

                if (duplicate != null) {

                    if (Application.isCalculationApplicantExists(duplicate.getApplicantId())) {
                        Dialog errorDialog = new Dialog();
                        errorDialog.setMessage("Pelamar [" + applicant + "] tidak bisa dinilai, karena sudah ada perhitungan yang disimpan");
                        errorDialog.show(this);

                        return;
                    }

                    Dialog confirmExistsDialog = new Dialog();
                    confirmExistsDialog.setMessageType(JOptionPane.QUESTION_MESSAGE);
                    confirmExistsDialog.setOptionType(JOptionPane.OK_CANCEL_OPTION);
                    confirmExistsDialog.setMessage("Pelamar [" + applicant + "] sudah dinilai. Apakah Anda ingin melakukan perubahan data?");

                    if (confirmExistsDialog.show(this).equals(JOptionPane.YES_OPTION)) {
                        fillForm(String.valueOf(duplicate.getId()));
                    }

                    return;
                }

                String sql = "INSERT INTO evaluations (applicant_id) VALUES (?)";
                PreparedStatement stmt = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, applicant.getId());
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    id = String.valueOf(rs.getInt(1));
                }

                stmt.close();
            }

            deleteDetails(id);

            String sqlDetail = "INSERT INTO evaluation_details (evaluation_id, criteria_id, sub_criteria_id) VALUES (?, ?, ?)";

            for (Map.Entry<Integer, ComboBoxInputGroup<SubCriteria>> entry : detailInputGroups.entrySet()) {
                Integer criteriaId = entry.getKey();
                SubCriteria subCriteria = (SubCriteria) entry.getValue().getSelectedItem();

                final PreparedStatement stmtDetail = this.conn.prepareStatement(sqlDetail);
                stmtDetail.setInt(1, Integer.parseInt(id));
                stmtDetail.setInt(2, criteriaId);
                stmtDetail.setInt(3, subCriteria.getId());

                stmtDetail.executeUpdate();
                stmtDetail.close();
            }

            Dialog dialog = new Dialog("Berhasil");
            dialog.setMessage("Data berhasil disimpan");
            dialog.show(this);

            this.conn.commit();

            resetForm();
            initDataTables();
            initCbApplicants();
            initDetailFormPanel();
        } catch (Exception e) {
            this.conn.rollback();

            throw e;
        }
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            save();
        } catch (Exception e) {
            System.err.println("error when saving: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        resetForm();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        String id = gId.getInputValue();

        if (id.isEmpty()) {
            return;
        }

        Dialog confirm = new Dialog();
        confirm.setMessage("Apakah Anda yakin ingin menghapus data?");
        confirm.setMessageType(JOptionPane.QUESTION_MESSAGE);
        confirm.setOptionType(JOptionPane.OK_CANCEL_OPTION);

        if (!confirm.show(this).equals(JOptionPane.YES_OPTION)) {
            return;
        }

        try {
            this.getById(id);

            String sql = "DELETE FROM evaluations WHERE id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();

            stmt.close();

            Dialog dialog = new Dialog("Berhasil");
            dialog.setMessage("Data berhasil dihapus");
            dialog.show(this);

            resetForm();
            initDataTables();
        } catch (Exception e) {
            if (e instanceof SQLException) {
                Dialog dialog = new Dialog();
                dialog.setMessage("Data tidak dapat dihapus karena sudah terikat dengan data lain");
                dialog.show(this);
            }

            System.err.println("error when delete: " + e.getMessage());
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        int row = table.getSelectedRow();
        String tableId = tableModel.getValueAt(row, 0).toString();
        String currentId = gId.getInputValue();

        if (tableId.equalsIgnoreCase(currentId)) {
            return;
        }

        try {
            fillForm(tableId);
        } catch (Exception e) {
            System.err.println("error find by id: " + e.getMessage());
        }
    }//GEN-LAST:event_tableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private main.components.Button btnCancel;
    private main.components.Button btnDelete;
    private main.components.Button btnSave;
    private javax.swing.JPanel detailFormPanel;
    private javax.swing.JPanel formPanel;
    private main.components.ComboBoxInputGroup<Applicant> gApplicant;
    private main.components.NumberInputGroup gId;
    private main.components.Header header;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
