/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HOTELL;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import project.ConnectionProvider;

/**
 *
 * @author HP
 */
public class feedback_view_rating extends javax.swing.JFrame {

    /**
     * Creates new form feedback_view_rating
     */
    public feedback_view_rating() {

        initComponents();
        loadFeedback();
        
    }
    private void updateResponseInTable(String feedbackId) {
    Connection con = null;
    PreparedStatement ps = null;

    try {
        
        con = ConnectionProvider.getCon();

        // استعلام لتحديث الرد في قاعدة البيانات
        String query = "UPDATE feedback SET response = ?, Reply_status = 'response' WHERE feedback_id = ?";
        ps = con.prepareStatement(query);

        // تعيين الرد من JTextArea1
        ps.setString(1, jTextArea1.getText());
        ps.setString(2, feedbackId);

        ps.executeUpdate();

        // تحديث الجدول
        updateReplayStatus(feedbackId);

       // JOptionPane.showMessageDialog(null, "The response has been updated in the database!");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    } finally {
        try {
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (Exception e) {}
    }
}


private void loadFeedback() {
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    try {
        con = ConnectionProvider.getCon();
        st = con.createStatement();
        rs = st.executeQuery("SELECT * FROM feedback");

        // إعداد الجدول
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // مسح الجدول قبل إضافة البيانات

       

        while (rs.next()) {
            String replyStatus = rs.getString("Reply_status");
            if (replyStatus == null || replyStatus.isEmpty()) {
                replyStatus = "Not response"; // تعيين القيمة الافتراضية
            }

            String userRating = rs.getString("user_rating");

            // تخصيص `MouseMotionListener` للكشف عن حركة الماوس على الصفوف
            jTable1.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    // الحصول على الصف الذي تم المرور عليه
                    int row = jTable1.rowAtPoint(e.getPoint());
                    if (row != -1) {
                        // الحصول على التقييم من الصف
                        String rating = jTable1.getValueAt(row, 4).toString();

                        // تغيير لون الصف بناءً على التقييم فقط عند اقتراب الماوس
                        if (rating.equals("Very bad rating") || rating.equals("Average rating")) {
                            jTable1.setRowSelectionInterval(row, row);
                            jTable1.setSelectionBackground(Color.RED); // اللون الأحمر
                        } else if (rating.equals("Fairly good") || rating.equals("Very good rating") || rating.equals("Excellent or perfect rating")) {
                            jTable1.setRowSelectionInterval(row, row);
                            jTable1.setSelectionBackground(Color.GREEN); // اللون الأخضر
                        } else {
                            jTable1.setSelectionBackground(Color.WHITE); // اللون الافتراضي
                        }
                    }
                }
            });

            // إضافة البيانات مع ID تلقائي
           Object[] row = {
        rs.getString("feedback_id"), // ✅ ده الـ ID الحقيقي من قاعدة البيانات
        rs.getString("user_name"),
        rs.getString("user_email"),
        rs.getString("user_mobile"),
        rs.getString("user_rating"),
        rs.getString("user_comment"),
        rs.getString("response"),
        replyStatus
    };

    model.addRow(row);

        }

        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // الحصول على الصف الذي تم النقر عليه
                int row = jTable1.rowAtPoint(e.getPoint());
                if (row != -1) {
                    // الحصول على الـ feedback_id والـ user_comment
                    String feedbackId = jTable1.getValueAt(row, 0).toString();
                    String userComment = jTable1.getValueAt(row, 5).toString();

                    // ملء الحقول
                    jTextField1.setText(feedbackId); // ملء ID التقييم في jTextField1
                    jTextArea1.setText(userComment); // ملء الـ JTextArea1 بالتعليق
                }
            }
        });
  
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    } finally {
        try {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null) con.close();
        } catch (Exception e) {}
    }
    
}

private void Email_sending(String email, String response) {
   
    

// إعداد خصائص الاتصال بخادم البريد الإلكتروني (SMTP)
  
    Properties proo = new Properties();
proo.put("mail.smtp.host", "smtp.gmail.com");
proo.put("mail.smtp.port", "587");
proo.put("mail.smtp.auth", "true");
proo.put("mail.smtp.starttls.enable", "true");  
proo.put("mail.smtp.ssl.trust", "smtp.gmail.com"); 

    
    
    
    String user = "ssyny775@gmail.com";
String password = "rxcv xkhn fpho urpx";
Session session = Session.getInstance(proo, new Authenticator() {
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }
});
    try {
        // إعداد الرسالة
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user));  // المرسل
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));  // المستقبل
        message.setSubject("Reply to the review");  // الموضوع
        message.setText(response);  // نص الرسالة

        // إرسال البريد الإلكتروني
        Transport.send(message);
JOptionPane.showMessageDialog(null, "Your email reply has been sent successfully.");
        } catch (Exception e) {
        
        JOptionPane.showMessageDialog(null,e);
    }
}




























private void sendResponse() {
    String feedbackId = jTextField1.getText(); // الحصول على ID التقييم من JTextField
    String response = jTextArea1.getText(); // الحصول على الرد من JTextArea

    if (feedbackId.isEmpty()) {
    }

    if (response.isEmpty()) {
    }
    
    
    
    
    
    
    // تحديث حالة الرد في قاعدة البيانات
    
    // إرسال الرد عبر البريد الإلكتروني
    // يمكن استدعاء دالة Email_sending هنا إذا أردت إرسال الرد عبر البريد

    // تحديث الجدول لإظهار حالة الرد
    
     String recipientEmail = getEmailFromFeedback(feedbackId); 

    if (recipientEmail != null && !recipientEmail.isEmpty()) {
        // إرسال الرد عبر البريد الإلكتروني

                Email_sending(recipientEmail, response);  // تمرير البريد الإلكتروني مع الرد

        // تحديث حالة الرد في قاعدة البيانات
        updateResponseInTable(feedbackId);
        
        
        // تحديث الجدول لإظهار حالة الرد
        updateReplayStatus(feedbackId);
    } else {
        JOptionPane.showMessageDialog(null, "No email was found for this review.");
    }
}

private String getEmailFromFeedback(String feedbackId) {
    String email = null;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = ConnectionProvider.getCon();
        String query = "SELECT user_email FROM feedback WHERE feedback_id = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, feedbackId);
        rs = ps.executeQuery();

        if (rs.next()) {
            email = rs.getString("user_email"); // الحصول على البريد الإلكتروني من قاعدة البيانات
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (Exception e) {}
    }

    return email;
}
    

























  
private void updateReplayStatus(String feedbackId) {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
        String id = model.getValueAt(i, 0).toString(); // الحصول على ID التقييم
        if (id.equals(feedbackId)) {
            model.setValueAt("Yes reply", i, 7); // وضع "تم الرد" في العمود الجديد
            break;
        }
    }

    // إعادة رسم الجدول ليظل التغيير مرئيًا
    jTable1.revalidate();
    jTable1.repaint();
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(30, 118));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 181, 217, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black));
        jScrollPane2.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 78, 711, 218));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-reply-50.png"))); // NOI18N
        jButton2.setText("Add reply");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 149, 299, 76));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-rating-50.png"))); // NOI18N
        jLabel1.setText("View customer rating");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, 217, -1));

        jTable1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Email", "Mobile", "Rating", "Comment", "Response", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 327, 1337, -1));

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-close.gif"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1300, 0, 60, 60));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Clear ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1239, 275, 110, 40));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/401739128288.jpg"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
sendResponse();


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new feedback_view_rating().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(feedback_view_rating.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(feedback_view_rating.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(feedback_view_rating.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(feedback_view_rating.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new feedback_view_rating().setVisible(true);
                

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
