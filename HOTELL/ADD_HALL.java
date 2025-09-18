/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HOTELL;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import project.InsertUpdateDelete;
import project.Select;

/**
 *
 * @author HP
 */
public class ADD_HALL extends javax.swing.JFrame {

    /**
     * Creates new form ADD_HALL
     */
    public ADD_HALL() {
        initComponents();
        updateHallTable();
        
        jComboBox1.setEnabled(false);

    }
    

    

 private void addHallAction() {
        String idStr = jTextField1.getText();
        String hallName = jTextField2.getText();
        String priceStr = jTextField3.getText();

        if (idStr.isEmpty() || hallName.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            int price = Integer.parseInt(priceStr);

            String sql = String.format(
                "INSERT INTO halls (id, hall_name, price_per_hour, status) VALUES (%d, '%s', %d, 'Available')",
                id, hallName, price
            );
            InsertUpdateDelete.setData(sql, "Hall added successfully!");
            updateHallTable();
            clearHallFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Price must be numbers.");
        }
    }
 
 
 
 



 
 
 
 
 private void updateHallAction() {
    if (jTextField1.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a hall to update.");
        return;
    }

    try {
        int id = Integer.parseInt(jTextField1.getText());
        String hallName = jTextField2.getText();
        int price = Integer.parseInt(jTextField3.getText());
        String status = jComboBox1.getSelectedItem().toString();

        // شرط عدم التعديل إذا كانت القاعة محجوزة
        if (status.equalsIgnoreCase("Booked") || status.equalsIgnoreCase("Booked")) {
            JOptionPane.showMessageDialog(this, "The room cannot be modified because it is already booked.");
            return;
        }

        updateHall(id, hallName, price, status);
        clearHallFields();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error updating hall: " + e.getMessage());
    }
}

 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
// private void updateHallAction() {
//        if (jTextField1.getText().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please select a hall to update.");
//            return;
//        }
//
//        try {
//            int id = Integer.parseInt(jTextField1.getText());
//            String hallName = jTextField2.getText();
//            int price = Integer.parseInt(jTextField3.getText());
//            String status = jComboBox1.getSelectedItem().toString();
//
//            updateHall(id, hallName, price, status);
//            clearHallFields();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Error updating hall: " + e.getMessage());
//        }
//    }
 
 
 
 
 
 private void deleteHallAction() {
    if (jTextField1.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a hall to delete.");
        return;
    }

    String status = jComboBox1.getSelectedItem().toString();

    // شرط عدم الحذف إذا كانت القاعة محجوزة
    if (status.equalsIgnoreCase("Booked") || status.equalsIgnoreCase("Booked")) {
        JOptionPane.showMessageDialog(this, "The room cannot be deleted because it is booked.");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this hall?", "Confirm", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        int id = Integer.parseInt(jTextField1.getText());
        deleteHall(id);
        clearHallFields();
    }
}

 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
//  private void deleteHallAction() {
//        if (jTextField1.getText().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please select a hall to delete.");
//            return;
//        }
//
//        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this hall?", "Confirm", JOptionPane.YES_NO_OPTION);
//        if (confirm == JOptionPane.YES_OPTION) {
//            int id = Integer.parseInt(jTextField1.getText());
//            deleteHall(id);
//            clearHallFields();
//        }
//    }
  
  
  
  
  
 
 
   private void clearHallFields() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jComboBox1.setSelectedIndex(0);
    }
   
   
   
   
   
 
 private void fillFormFromTable() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);  // مسح البيانات القديمة من الجدول
            jTextField1.setText(model.getValueAt(selectedRow, 0).toString());
            jTextField2.setText(model.getValueAt(selectedRow, 1).toString());
            jTextField3.setText(model.getValueAt(selectedRow, 2).toString());
            jComboBox1.setSelectedItem(model.getValueAt(selectedRow, 3).toString());
        }
    }
 
 
 
 
  private void updateHall(int id, String hallName, int pricePerHour, String status) {
        String sql = String.format(
            "UPDATE halls SET hall_name='%s', price_per_hour=%d, status='%s' WHERE id=%d",
            hallName, pricePerHour, status, id
        );
        InsertUpdateDelete.setData(sql, "Hall updated successfully!");
        updateHallTable();
    }
  
  
  
  
  
   private void deleteHall(int id) {
        String sql = "DELETE FROM halls WHERE id=" + id;
        InsertUpdateDelete.setData(sql, "Hall deleted successfully!");
        updateHallTable();
    }
   
   
   
      private void updateHallTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);  // مسح البيانات القديمة من الجدول
            ResultSet rs = Select.getData("SELECT * FROM halls");
            
//model.setRowCount(0);
               
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("hall_name"),
                    rs.getInt("price_per_hour"),
                    rs.getString("status")
                });
            }

            if (rs != null) rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading halls: " + e.getMessage());
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

        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();

        jLabel7.setText("jLabel7");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(30, 118));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/HALL.png"))); // NOI18N
        jLabel1.setText("Halls");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 276, -1));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-close.gif"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 0, 48, 49));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(749, 191, 90, -1));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Hall Name");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(749, 289, 106, -1));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Price Per Hour");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(749, 391, 99, -1));

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(867, 187, 343, -1));

        jTextField2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(867, 285, 343, -1));

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(867, 384, 343, -1));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Status");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(749, 474, 99, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Available" }));
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(867, 471, 343, -1));

        jTable1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Hall Name", "Price Per Hour", "Status"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 187, 710, 483));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Create halls and display halls ");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 677, 1259, 74));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-plus.gif"))); // NOI18N
        jButton2.setText("ADD");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(749, 520, 140, -1));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-update-left-rotation.gif"))); // NOI18N
        jButton3.setText("UPDATE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 520, 140, -1));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-close_1.gif"))); // NOI18N
        jButton4.setText("DELETE");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(749, 611, 140, -1));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CLEAR.png"))); // NOI18N
        jButton5.setText("CLEAR");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 611, 140, -1));

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-search.gif"))); // NOI18N
        jButton6.setText("SEARCH");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 112, 157, -1));

        jTextField4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 129, 257, -1));

        jComboBox2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Available", "Booked", "everyone" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        getContentPane().add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(489, 123, 233, 35));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ADD event.jpg"))); // NOI18N
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        addHallAction();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        updateHallAction();
     

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
           setVisible(false);
        new ADD_HALL().setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        deleteHallAction();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
                        setVisible(false);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    int row = jTable1.getSelectedRow();
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

    jTextField1.setText(model.getValueAt(row, 0).toString()); // ID
    jTextField2.setText(model.getValueAt(row, 1).toString()); // name
    jTextField3.setText(model.getValueAt(row, 2).toString()); // Price Per Hour
    jComboBox1.setSelectedItem(model.getValueAt(row, 3).toString()); // availability
    

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
       String Search = jTextField4.getText();
       if (Search.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter search text!");
            return;
        }
    String keyword = jTextField4.getText().trim();
DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
model.setRowCount(0); // مسح البيانات القديمة

try {
    String query;
    
    // تحقق إذا كان المستخدم أدخل رقم فقط (ID)
    if (keyword.matches("\\d+")) {
        query = "SELECT * FROM halls WHERE id = '" + keyword + "'";
    } else {
        query = "SELECT * FROM halls WHERE hall_name = '" + keyword + "'";
    }

    ResultSet rs = Select.getData(query);
    
    while (rs.next()) {
        model.addRow(new Object[]{
            rs.getString("id"),
            rs.getString("hall_name"),
            rs.getString("price_per_hour"),
            rs.getString("status")
        });
    }

} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(null, "خطأ في البحث: " + e.getMessage());
}



    }//GEN-LAST:event_jButton6ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    String selected = jComboBox2.getSelectedItem().toString();
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); // مسح الجدول

    try {
        String query = "";
        if (selected.equals("Available") || selected.equals("Booked")) {
            query = "SELECT * FROM halls WHERE status = '" + selected + "'";
        } else {
            query = "SELECT * FROM halls"; // لو اختار "الكل"
        }

        ResultSet rs = Select.getData(query);
        while (rs.next()) {
            model.addRow(new Object[] {
                rs.getString("id"),
                rs.getString("hall_name"),
                rs.getString("price_per_hour"),
                rs.getString("status")
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }


    }//GEN-LAST:event_jComboBox2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ADD_HALL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ADD_HALL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ADD_HALL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ADD_HALL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ADD_HALL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
