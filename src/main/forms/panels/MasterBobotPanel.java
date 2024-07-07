package main.forms.panels;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.Application;
import main.exceptions.DataNotFoundException;
import main.models.Weight;
import main.util.Dialog;

/**
 * @author aldes
 */
public class MasterBobotPanel extends javax.swing.JPanel {

    private Connection conn;
    private DefaultTableModel tableModel;

    /**
     * Creates new form MasterBobotPanel
     */
    public MasterBobotPanel() {
        this.conn = Application.getDBConnection();

        initComponents();

        header.setTitleText("Data Bobot");

        formPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "background:darken(@background, 10%)");

        gId.setTitleText("ID");
        gId.getInputField().setEnabled(false);
        gId.getInputField().putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Auto-generate oleh sistem");

        gWeight.setTitleText("Bobot");
        gDescription.setTitleText("Deskripsi");

        btnDelete.setVisible(false);

        initDataTables();
    }

    public void initDataTables() {
        String[] headers = new String[]{"ID", "Bobot", "Deskripsi"};

        tableModel = new DefaultTableModel(null, headers);
        table.setModel(tableModel);

        try {
            String sql = "SELECT * FROM weights";
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new String[]{
                        String.valueOf(rs.getInt("id")),
                        String.valueOf(rs.getInt("weight")),
                        rs.getString("description"),
                });
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("error datatable: " + e.getMessage());
        }
    }

    public void resetForm() {
        gId.getInputField().setText("");
        gWeight.getInputField().setText("");
        gDescription.getInputField().setText("");

        btnDelete.setVisible(false);
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
        btnSave = new main.components.Button();
        btnCancel = new main.components.Button();
        gId = new main.components.NumberInputGroup();
        gWeight = new main.components.NumberInputGroup();
        gDescription = new main.components.TextInputGroup();
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
                                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(formPanelLayout.createSequentialGroup()
                                                .addComponent(gId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(gWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(gDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formPanelLayout.setVerticalGroup(
                formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(formPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(gId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(gWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(gDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    public Weight getById(String id) throws Exception {
        String sql = "SELECT * FROM weights WHERE id = ?";
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(id));
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Weight weight = new Weight();
            weight.setId(rs.getInt("id"));
            weight.setWeight(rs.getInt("weight"));
            weight.setDescription(rs.getString("description"));

            rs.close();
            stmt.close();

            return weight;
        }

        Dialog dialog = new Dialog();
        dialog.setMessage("Data tidak ditemukan");
        dialog.setMessageType(JOptionPane.ERROR_MESSAGE);
        dialog.show(this);

        rs.close();
        stmt.close();

        throw new DataNotFoundException("Data tidak ditemukan");
    }

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        int row = table.getSelectedRow();
        String tableId = tableModel.getValueAt(row, 0).toString();
        String currentId = gId.getInputValue();

        if (tableId.equalsIgnoreCase(currentId)) {
            return;
        }

        try {
            Weight rs = this.getById(tableId);

            gId.getInputField().setText(rs.getId().toString());
            gWeight.getInputField().setText(rs.getWeight().toString());
            gDescription.getInputField().setText(rs.getDescription());

            btnDelete.setVisible(true);
        } catch (Exception e) {
            System.err.println("error find by id: " + e.getMessage());
        }
    }//GEN-LAST:event_tableMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        resetForm();
    }//GEN-LAST:event_btnCancelActionPerformed

    private boolean isDuplicate() throws SQLException {
        String id = gId.getInputValue();
        String weight = gWeight.getInputValue();

        String sql = "SELECT * FROM weights WHERE weight = ?";
        PreparedStatement stmt = this.conn.prepareStatement(sql);

        if (!id.isEmpty()) {
            sql = "SELECT * FROM weights WHERE weight = ? AND id != ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setInt(2, Integer.parseInt(id));
        }

        stmt.setInt(1, Integer.parseInt(weight));
        ResultSet rs = stmt.executeQuery();

        boolean status = rs.next();

        stmt.close();
        rs.close();

        return status;
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String id = gId.getInputValue();
        String weight = gWeight.getInputValue();
        String description = gDescription.getInputValue();

        if (!id.isEmpty() && Application.isCalculationExists()) {
            Dialog errorDialog = new Dialog();
            errorDialog.setMessage("Bobot tidak bisa diubah, karena sudah ada perhitungan yang disimpan");
            errorDialog.show(this);

            return;
        }

        if (weight.isEmpty()) {
            Dialog errorDialog = new Dialog();
            errorDialog.setMessage("Bobot tidak boleh kosong");
            errorDialog.show(this);

            return;
        }

        if (description.isEmpty()) {
            Dialog errorDialog = new Dialog();
            errorDialog.setMessage("Deskripsi tidak boleh kosong");
            errorDialog.show(this);

            return;
        }

        try {
            String sql = "INSERT INTO weights (weight, description) VALUES (?, ?)";
            PreparedStatement stmt = this.conn.prepareStatement(sql);

            if (this.isDuplicate()) {
                Dialog errorDialog = new Dialog();
                errorDialog.setMessage("Bobot " + weight + " sudah ada");
                errorDialog.show(this);

                stmt.close();

                return;
            }

            if (!id.isEmpty()) {
                sql = "UPDATE weights SET weight = ?, description = ? WHERE id = ?";
                stmt = this.conn.prepareStatement(sql);
                stmt.setInt(3, Integer.parseInt(id));
            }

            stmt.setString(1, weight);
            stmt.setString(2, description);
            stmt.executeUpdate();

            stmt.close();

            Dialog dialog = new Dialog("Berhasil");
            dialog.setMessage("Data berhasil disimpan");
            dialog.show(this);

            resetForm();
            initDataTables();

            // Hitung ulang nilai normalisasi bobot, saat nilai bobot diubah
            if (!id.isEmpty()) {
                Application.recalculateNormalizations();
            }
        } catch (Exception e) {
            System.err.println("error when save: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        String id = gId.getInputValue();

        if (id.isEmpty()) {
            return;
        }

        if (Application.isCalculationExists()) {
            Dialog errorDialog = new Dialog();
            errorDialog.setMessage("Bobot tidak bisa dihapus, karena sudah ada perhitungan yang disimpan");
            errorDialog.show(this);

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
            Weight rs = this.getById(id);

            String sql = "DELETE FROM weights WHERE id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, rs.getId());
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private main.components.Button btnCancel;
    private main.components.Button btnDelete;
    private main.components.Button btnSave;
    private javax.swing.JPanel formPanel;
    private main.components.TextInputGroup gDescription;
    private main.components.NumberInputGroup gId;
    private main.components.NumberInputGroup gWeight;
    private main.components.Header header;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}