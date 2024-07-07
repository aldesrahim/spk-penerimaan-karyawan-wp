package main.forms.panels;

import com.formdev.flatlaf.FlatClientProperties;
import main.Application;
import main.enums.CriteriaAttribute;
import main.enums.Gender;
import main.enums.Religion;
import main.exceptions.DataNotFoundException;
import main.models.Applicant;
import main.models.Criteria;
import main.models.Vacancy;
import main.models.Weight;
import main.util.Dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Date;

/**
 * @author aldes
 */
public class MasterPelamarPanel extends javax.swing.JPanel {

    private Connection conn;
    private DefaultTableModel tableModel;

    /**
     * Creates new form MasterPelamarPanel
     */
    public MasterPelamarPanel() {
        this.conn = Application.getDBConnection();

        initComponents();

        header.setTitleText("Data Pelamar");

        formPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "background:darken(@background, 10%)");

        btnDelete.setVisible(false);

        gId.setTitleText("ID");
        gId.getInputField().setEnabled(false);
        gId.getInputField().putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Auto-generate oleh sistem");

        gName.setTitleText("Nama");
        gPhoneNumber.setTitleText("No.HP");
        gReligion.setTitleText("Agama");
        gGender.setTitleText("Jenis Kelamin");
        gAddress.setTitleText("Alamat");
        gVacancy.setTitleText("Lowongan");

        gDob.setTitleText("Tanggal Lahir");
        gDob.getInputField().setFormats("yyyy-MM-dd");

        initCbGenders();
        initCbReligions();
        initCbVacancies();
        initDataTables();
    }

    private void initCbGenders() {
        gGender.getInputField().removeAllItems();

        for (Gender gender : Gender.values()) {
            gGender.getInputField().addItem(gender);
        }
    }

    private void initCbReligions() {
        gReligion.getInputField().removeAllItems();

        for (Religion religion : Religion.values()) {
            gReligion.getInputField().addItem(religion);
        }
    }

    private void initCbVacancies() {
        try {
            String sql = "SELECT * FROM vacancies";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            gVacancy.getInputField().removeAllItems();

            while (rs.next()) {
                gVacancy.getInputField().addItem(new Vacancy(
                        rs.getInt("id"),
                        rs.getString("position"),
                        rs.getInt("quota")
                ));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("error init combobox vacanies: " + e.getMessage());
        }
    }

    private void initDataTables() {
        String[] headers = new String[]{"ID", "Lowongan", "Nama", "No.HP", "Agama", "Jenis Kelamin", "Alamat", "Tanggal Lahir"};

        tableModel = new DefaultTableModel(null, headers);
        table.setModel(tableModel);

        try {
            String sql = "SELECT" +
                    " applicants.*," +
                    " vacancies.position AS v_position," +
                    " vacancies.quota AS v_quota" +
                    " FROM applicants" +
                    " JOIN vacancies ON vacancies.id = applicants.vacancy_id" +
                    " ORDER BY applicants.id";

            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("v_position"),
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
        } catch (Exception e) {
            System.err.println("error datatable: " + e.getMessage());
        }
    }

    private void resetForm() {
        gId.getInputField().setText("");
        gName.getInputField().setText("");
        gPhoneNumber.getInputField().setText("");
        gAddress.getInputField().setText("");
        gDob.getInputField().setDate(new Date());
        gReligion.getInputField().setSelectedIndex(0);
        gGender.getInputField().setSelectedIndex(0);
        gVacancy.getInputField().setSelectedIndex(0);

        btnDelete.setVisible(false);
    }

    private boolean isVacancyQuotaAvailable(Vacancy vacancy) {
        int applied = 0;
        int quota = 0;

        try {
            String sql;
            PreparedStatement stmt;
            ResultSet rs;

            sql = "SELECT quota FROM vacancies WHERE id = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, vacancy.getId());
            rs = stmt.executeQuery();

            if (rs.next()) {
                quota = rs.getInt("quota");
            }

            sql = "SELECT COUNT(*) AS applicant_count FROM applicants WHERE vacancy_id = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, vacancy.getId());
            rs = stmt.executeQuery();

            if (rs.next()) {
                applied = rs.getInt("applicant_count");
            }

            return applied < quota;
        } catch (Exception e) {
            System.err.println("error when check vacancy quota: " + e.getMessage());
        }

        return false;
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
        gName = new main.components.TextInputGroup();
        gPhoneNumber = new main.components.NumberInputGroup();
        gReligion = new main.components.ComboBoxInputGroup();
        gGender = new main.components.ComboBoxInputGroup();
        gAddress = new main.components.TextInputGroup();
        gDob = new main.components.DateInputGroup();
        gVacancy = new main.components.ComboBoxInputGroup();
        btnSave = new main.components.Button();
        btnCancel = new main.components.Button();
        btnDelete = new main.components.Button();
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

        javax.swing.GroupLayout formPanelLayout = new javax.swing.GroupLayout(formPanel);
        formPanel.setLayout(formPanelLayout);
        formPanelLayout.setHorizontalGroup(
                formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(formPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(formPanelLayout.createSequentialGroup()
                                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(formPanelLayout.createSequentialGroup()
                                                                .addComponent(gId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(gName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(gPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(formPanelLayout.createSequentialGroup()
                                                                .addComponent(gGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(gAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(gReligion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(gDob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(gVacancy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(formPanelLayout.createSequentialGroup()
                                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formPanelLayout.setVerticalGroup(
                formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(formPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(gReligion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(gPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(gName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(gId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(gGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(gAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(gDob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(gVacancy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
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
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    public Applicant getById(String id) throws Exception {
        String sql = "SELECT * FROM applicants WHERE id = ?";
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(id));
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Applicant applicant = new Applicant();
            applicant.setId(rs.getInt("id"));
            applicant.setName(rs.getString("name"));
            applicant.setPhoneNumber(rs.getString("phone_number"));
            applicant.setReligion(rs.getString("religion"));
            applicant.setGender(rs.getInt("gender"));
            applicant.setAddress(rs.getString("address"));
            applicant.setDob(rs.getDate("dob"));
            applicant.setVacancyId(rs.getInt("vacancy_id"));

            rs.close();
            stmt.close();

            return applicant;
        }

        Dialog dialog = new Dialog();
        dialog.setMessage("Data tidak ditemukan");
        dialog.setMessageType(JOptionPane.ERROR_MESSAGE);
        dialog.show(this);

        rs.close();
        stmt.close();

        throw new DataNotFoundException("Data tidak ditemukan");
    }

    private boolean isDuplicate() throws SQLException {
        String id = gId.getInputValue();
        String phoneNumber = gPhoneNumber.getInputValue();

        String sql = "SELECT * FROM applicants WHERE phone_number = ?";
        PreparedStatement stmt = this.conn.prepareStatement(sql);

        if (!id.isEmpty()) {
            sql = "SELECT * FROM applicants WHERE phone_number = ? AND id != ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setInt(2, Integer.parseInt(id));
        }

        stmt.setString(1, phoneNumber);
        ResultSet rs = stmt.executeQuery();

        boolean status = rs.next();

        stmt.close();
        rs.close();

        return status;
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String id = gId.getInputValue();
        String name = gName.getInputValue();
        String phoneNumber = gPhoneNumber.getInputValue();
        String address = gAddress.getInputValue();
        Date dob = gDob.getDate();
        Religion religion = (Religion) gReligion.getSelectedItem();
        Gender gender = (Gender) gGender.getSelectedItem();
        Vacancy vacancy = (Vacancy) gVacancy.getSelectedItem();

        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);

        if (id.isEmpty() && !isVacancyQuotaAvailable(vacancy)) {
            Dialog errorDialog = new Dialog();
            errorDialog.setMessage("Quota lowongan "+ vacancy.getPosition() +" sudah penuh");
            errorDialog.show(this);

            return;
        }

        if (name.isEmpty()) {
            Dialog errorDialog = new Dialog();
            errorDialog.setMessage("Nama tidak boleh kosong");
            errorDialog.show(this);

            return;
        }

        if (phoneNumber.isEmpty()) {
            Dialog errorDialog = new Dialog();
            errorDialog.setMessage("No.HP tidak boleh kosong");
            errorDialog.show(this);

            return;
        }

        if (phoneNumber.length() < 10) {
            Dialog errorDialog = new Dialog();
            errorDialog.setMessage("No.HP tidak valid");
            errorDialog.show(this);

            return;
        }

        if (dob.after(today)) {
            Dialog errorDialog = new Dialog();
            errorDialog.setMessage("Tanggal lahir tidak valid");
            errorDialog.show(this);

            return;
        }

        try {
            String sql = "INSERT INTO applicants (vacancy_id, name, phone_number, religion, gender, address, dob) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = this.conn.prepareStatement(sql);

            if (this.isDuplicate()) {
                Dialog errorDialog = new Dialog();
                errorDialog.setMessage("Pelamar dengan No.HP " + phoneNumber + " sudah ada");
                errorDialog.show(this);

                stmt.close();

                return;
            }

            if (!id.isEmpty()) {
                sql = "UPDATE applicants SET vacancy_id = ?, name = ?, phone_number = ?, religion = ?, gender = ?, address = ?, dob = ? WHERE id = ?";
                stmt = this.conn.prepareStatement(sql);
                stmt.setInt(8, Integer.parseInt(id));
            }

            stmt.setInt(1, vacancy.getId());
            stmt.setString(2, name);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, religion.name());
            stmt.setInt(5, gender.toInt());
            stmt.setString(6, address);
            stmt.setDate(7, new java.sql.Date(dob.getTime()));

            stmt.executeUpdate();

            stmt.close();

            Dialog dialog = new Dialog("Berhasil");
            dialog.setMessage("Data berhasil disimpan");
            dialog.show(this);

            resetForm();
            initDataTables();
        } catch (Exception e) {
            System.err.println("error when save: " + e.getMessage());
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

            String sql = "DELETE FROM applicants WHERE id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();

            stmt.close();

            Dialog dialog = new Dialog("Berhasil");
            dialog.setMessage("Data berhasil dihapus");
            dialog.show(this);

            resetForm();
            initDataTables();
            Application.recalculateNormalizations();
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
            Applicant rs = this.getById(tableId);

            gId.getInputField().setText(rs.getId().toString());
            gName.getInputField().setText(rs.getName());
            gPhoneNumber.getInputField().setText(rs.getPhoneNumber());
            gAddress.getInputField().setText(rs.getAddress());
            gReligion.getInputField().setSelectedItem(Religion.fromString(rs.getReligion()));
            gGender.getInputField().setSelectedItem(Gender.fromInt(rs.getGender()));
            gDob.getInputField().setDate(rs.getDob());

            Vacancy vacancy = new Vacancy();
            vacancy.setId(rs.getVacancyId());
            gVacancy.getInputField().setSelectedItem(vacancy);

            btnDelete.setVisible(true);
        } catch (Exception e) {
            System.err.println("error find by id: " + e.getMessage());
        }
    }//GEN-LAST:event_tableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private main.components.Button btnCancel;
    private main.components.Button btnDelete;
    private main.components.Button btnSave;
    private javax.swing.JPanel formPanel;
    private main.components.TextInputGroup gAddress;
    private main.components.DateInputGroup gDob;
    private main.components.ComboBoxInputGroup<Gender> gGender;
    private main.components.NumberInputGroup gId;
    private main.components.TextInputGroup gName;
    private main.components.NumberInputGroup gPhoneNumber;
    private main.components.ComboBoxInputGroup<Religion> gReligion;
    private main.components.ComboBoxInputGroup<Vacancy> gVacancy;
    private main.components.Header header;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
