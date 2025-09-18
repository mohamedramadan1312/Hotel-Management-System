/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HOTELL;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import project.ConnectionProvider;
import project.Select;

/**
 *
 * @author HP
 */
public class Report_events extends javax.swing.JFrame {

    /**
     * Creates new form Report_events
     */
    public Report_events() {
        initComponents();
       jTextField1.setText("display");
       jTextField6.setText("2025/02");
jTextField1.setEditable(false);

jTextField2.setEditable(false);
jTextField3.setEditable(false);
jTextField4.setEditable(false);
jTextField5.setEditable(false);
jTextField9.setEditable(false);



// تحميل البيانات مباشرة عند التشغيل

// إعادة التحديث كل ساعة
Timer timer = new Timer(3600000, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        loadDailyProfit();
        updatePieChartt();
    }
});
timer.start();

//try {
//    ResultSet rs = Select.getData("SELECT TO_CHAR(date_event, 'YYYY-MM-DD') as day, COUNT(*) as total " +
//            "FROM eventss GROUP BY TO_CHAR(date_event, 'YYYY-MM-DD') ORDER BY total DESC FETCH FIRST 1 ROWS ONLY");
//    if (rs.next()) {
//        if (jPanel2 != null) jPanel2.setToolTipText("أعلى ضغط يوم " + rs.getString("day") + " بعدد " + rs.getInt("total"));
//    }
//} catch (Exception e) {
//    e.printStackTrace();
//}
//
//try {
//    ResultSet rs = Select.getData("SELECT TO_CHAR(date_event, 'YYYY-MM-DD') as day, COUNT(*) as total " +
//            "FROM eventss GROUP BY TO_CHAR(date_event, 'YYYY-MM-DD') ORDER BY total ASC FETCH FIRST 1 ROWS ONLY");
//    if (rs.next()) {
//         if(jPanel7 != null) jPanel7.setToolTipText("أقل ضغط يوم " + rs.getString("day") + " بعدد " + rs.getInt("total"));
//    }
//} catch (Exception e) {
//    e.printStackTrace();


    }
  
    
public void loadDailyProfit() {
    try {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        ResultSet rs = Select.getData("SELECT SUM(event_price) FROM eventss WHERE TRUNC(date_event) = TO_DATE('" + date + "', 'YYYY-MM-DD')");
        if (rs.next()) {
            int total = rs.getInt(1);
            jTextField4.setText(String.valueOf(total));
        } else {
            jTextField4.setText("0");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "خطأ في loadDailyProfit\n" + e.getMessage());
    }
}

public void updatePieChartt() {
    try {
        ResultSet rs1 = Select.getData("SELECT COUNT(*) FROM halls WHERE status='Booked'");
        ResultSet rs2 = Select.getData("SELECT COUNT(*) FROM halls WHERE status='Available'");

        int booked = rs1.next() ? rs1.getInt(1) : 0;
        int available = rs2.next() ? rs2.getInt(1) : 0;
        int total = booked + available;

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (total > 0) {
            dataset.setValue("Booked", booked);
            dataset.setValue("Available", available);
        } else {
            dataset.setValue("No data", 1);
        }

        JFreeChart chart = ChartFactory.createPieChart("Halls ratio", dataset, true, true, false);
        ChartPanel piePanel = new ChartPanel(chart);
        piePanel.setPreferredSize(new java.awt.Dimension(300, 300));
        jPanel3.removeAll();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add(piePanel, BorderLayout.CENTER);
        jPanel3.revalidate();
        jPanel3.repaint();

        if (total > 0) {
            double bookedRatio = ((double) booked / total) * 100;
            double availableRatio = ((double) available / total) * 100;
            jTextField5.setText(String.format("Booked: %.1f%% , Available: %.1f%%", bookedRatio, availableRatio));
        } else {
            jTextField5.setText("No data");
        }

        jTextField2.setText(String.valueOf(booked));
        jTextField3.setText(String.valueOf(available));
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "خطأ في updatePieChartt\n" + e.getMessage());
    }
}

public void drawBarChart(int monthlyProfit) {
    try {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(monthlyProfit, "profits", jTextField6.getText());

        JFreeChart barChart = ChartFactory.createBarChart(
                "Earnings for the specified month",
                "month",
                "Profits",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel barPanel = new ChartPanel(barChart);
        barPanel.setPreferredSize(new java.awt.Dimension(400, 300));
        jPanel6.removeAll();
        jPanel6.setLayout(new BorderLayout());
        jPanel6.add(barPanel, BorderLayout.CENTER);
        jPanel6.revalidate();
        jPanel6.repaint();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "خطأ في drawBarChart\n" + e.getMessage());
    }
}
public void loadCurrentMonthProfit() {
    try {
        String currentYear = new SimpleDateFormat("yyyy").format(new java.util.Date());
        String currentMonth = new SimpleDateFormat("MM").format(new java.util.Date());

        ResultSet rs = Select.getData("SELECT SUM(event_price) FROM eventss WHERE TO_CHAR(date_event, 'YYYY') = '" + currentYear + "' AND TO_CHAR(date_event, 'MM') = '" + currentMonth + "'");
        if (rs.next()) {
            int monthlyTotal = rs.getInt(1);
            jTextField6.setText(currentYear + "/" + currentMonth); // لعرض الشهر الحالي
            jTextField9.setText(String.valueOf(monthlyTotal));
            drawBarChart(monthlyTotal); // رسم الأرباح
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "خطأ في loadCurrentMonthProfit\n" + e.getMessage());
    }
}

    
    
    
    
    public void loadSelectedMonthProfit(String year, String month) {
    try {
        ResultSet rs = Select.getData("SELECT SUM(event_price) FROM eventss WHERE TO_CHAR(date_event, 'YYYY') = '" + year + "' AND TO_CHAR(date_event, 'MM') = '" + month + "'");
        if (rs.next()) {
            int total = rs.getInt(1);
            jTextField4.setText(String.valueOf(total));  // هنا الشهر
            drawBarChart(total);
        } else {
            jTextField4.setText("0");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "خطأ في loadSelectedMonthProfit\n" + e.getMessage());
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

        jLabel1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-bar-chart.gif"))); // NOI18N
        jLabel1.setText("Report Events");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 418, -1));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-logout_1.gif"))); // NOI18N
        jButton4.setText("LogOut");
        jButton4.setToolTipText("");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 210, 60));

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, 300, -1));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-search.gif"))); // NOI18N
        jButton2.setText("Search");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 100, 136, 61));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 100, 106, 61));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Number of Halls booked");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 218, 201, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Number of unbooked Halls");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 201, -1));

        jTextField2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 212, 300, -1));

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 310, 300, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Total monthly revenue");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 201, -1));

        jTextField4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 410, 300, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Reserved and unreserved");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 201, -1));

        jTextField5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 520, 300, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Revenue account by date");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 640, 201, -1));

        jTextField6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 640, 300, -1));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-search.gif"))); // NOI18N
        jButton5.setText("Search");
        jButton5.setToolTipText("");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 620, -1, 70));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Total daily revenue");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 770, 210, -1));

        jTextField9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 770, 300, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 130, 880, 300));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 880, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 470, 880, 330));

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-back.gif"))); // NOI18N
        jButton7.setText("Back");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1640, 0, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Report_events.jpg"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int a = JOptionPane.showConfirmDialog(null, "Do you really Want to Logout", "Select", JOptionPane.YES_NO_OPTION);

        if (a == 0) {
            setVisible(false);
            new login().setVisible(true);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
       // في زر jButton1
//try {
//    Connection con = ConnectionProvider.getCon();
//    Statement st = con.createStatement();
//
//    // القاعات المحجوزة
//    ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM halls WHERE status = 'Booked'");
//    int reserved = rs1.next() ? rs1.getInt(1) : 0;
//    jTextField2.setText(String.valueOf(reserved));
//
//    // القاعات المتاحة
//    ResultSet rs2 = st.executeQuery("SELECT COUNT(*) FROM halls WHERE status = 'Available'");
//    int available = rs2.next() ? rs2.getInt(1) : 0;
//    jTextField3.setText(String.valueOf(available));
//
//    // النسبة
//    double percent = reserved + available == 0 ? 0 : ((double) reserved / (reserved + available)) * 100;
//    jTextField5.setText(String.format("%.2f%%", percent));
//
//    // رسم بياني دائري
//    DefaultPieDataset pieDataset = new DefaultPieDataset();
//    pieDataset.setValue("محجوزة", reserved);
//    pieDataset.setValue("متاحة", available);
//    JFreeChart chart = ChartFactory.createPieChart("نسبة القاعات", pieDataset, true, true, false);
//    ChartPanel piePanel = new ChartPanel(chart);
//    if (jPanel3 != null) { jPanel3.removeAll();
//    jPanel3.setLayout(new BorderLayout());
//    jPanel3.add(piePanel, BorderLayout.CENTER);
//    jPanel3.validate(); jPanel3.repaint(); }
//    jPanel3.repaint();
//
//    // الشهر الحالي
//    String thisMonth = jTextField6.getText().trim(); // استخدم النص الموجود في الحقل لتحديد الشهر
//    if (thisMonth.isEmpty()) {
//        thisMonth = new SimpleDateFormat("yyyy/MM").format(new Date());
//        jTextField6.setText(thisMonth); // ثبت الشهر تلقائيًا في الحقل
//    }
//
//    // أرباح الشهر الحالي
//    ResultSet rs4 = st.executeQuery("SELECT SUM(event_price) FROM eventss WHERE TO_CHAR(date_event, 'YYYY/MM') = '" + thisMonth + "'");
//    if (rs4.next()) {
//        double profit = rs4.getDouble(1);
//        jTextField4.setText(String.format("%.2f", profit));
//    }
//
//    // أرباح يومية في الشهر الحالي (رسم بياني)
//    ResultSet rs5 = st.executeQuery("SELECT TO_CHAR(date_event, 'DD') AS day, SUM(event_price) AS total " +
//            "FROM eventss WHERE TO_CHAR(date_event, 'YYYY/MM') = '" + thisMonth + "' GROUP BY TO_CHAR(date_event, 'DD') ORDER BY day");
//    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//    while (rs5.next()) {
//        dataset.addValue(rs5.getDouble("total"), "أرباح", rs5.getString("day"));
//    }
//    JFreeChart barChart = ChartFactory.createBarChart("أرباح الشهر", "اليوم", "الإجمالي", dataset);
//    ChartPanel barPanel = new ChartPanel(barChart);
//    if (jPanel6 != null) { jPanel6.removeAll();
//    jPanel6.add(barPanel);
//    jPanel6.validate(); jPanel6.repaint(); }
//
//    // عدد الزائرين اليوم
//    ResultSet todayGuests = st.executeQuery("SELECT COUNT(DISTINCT customer_name) FROM eventss WHERE date_event = TRUNC(SYSDATE)");
//    if (todayGuests.next()) {
//        jTextField7.setText(String.valueOf(todayGuests.getInt(1)));
//    }
//
//    // عدد الزائرين خلال الشهر
//    ResultSet monthGuests = st.executeQuery("SELECT COUNT(DISTINCT customer_name) FROM eventss WHERE TO_CHAR(date_event, 'YYYY/MM') = '" + thisMonth + "'");
//    if (monthGuests.next()) {
//        jTextField8.setText(String.valueOf(monthGuests.getInt(1)));
//    }
//
//    // أرباح اليوم
//    ResultSet todayProfit = st.executeQuery("SELECT SUM(event_price) FROM eventss WHERE date_event = TRUNC(SYSDATE)");
//    if (todayProfit.next()) {
//        jTextField9.setText(String.format("%.2f", todayProfit.getDouble(1)));
//    }
//
//} catch (Exception e) {
//    e.printStackTrace();
//    JOptionPane.showMessageDialog(null, "حدث خطأ أثناء تحميل البيانات: " + e.getMessage());
//}


    if (!jTextField1.getText().equals("display")) {
        JOptionPane.showMessageDialog(null, "اكتب display أولاً في الحقل");
        return;
    }
    

   loadDailyProfit();
updatePieChartt();
loadCurrentMonthProfit(); // لرسم أرباح الشهر الحالي تلقائيًا

String currentMonth = new SimpleDateFormat("yyyy/MM").format(new Date());
jTextField4.setText(currentMonth);

String[] parts = currentMonth.split("/");
String year = parts[0];
String month = parts[1];
loadSelectedMonthProfit(year, month);


    



    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        setVisible(false);
        
            new Report_events().setVisible(true);
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
     // في زر jButton5
//try {
//    String selectedMonth = jTextField6.getText(); // تأكد أنه بصيغة yyyy/MM
//
//    Connection con = ConnectionProvider.getCon();
//    Statement st = con.createStatement();
//
//    // أرباح الشهر المختار
//    ResultSet rs = st.executeQuery("SELECT SUM(event_price) FROM eventss WHERE TO_CHAR(date_event, 'YYYY/MM') = '" + selectedMonth + "'");
//    if (rs.next()) {
//        jTextField4.setText(String.format("%.2f", rs.getDouble(1)));
//    }
//
//    // أرباح يومية
//    rs = st.executeQuery("SELECT TO_CHAR(date_event, 'DD') AS day, SUM(event_price) AS total " +
//            "FROM eventss WHERE TO_CHAR(date_event, 'YYYY/MM') = '" + selectedMonth + "' GROUP BY TO_CHAR(date_event, 'DD') ORDER BY day");
//    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//    while (rs.next()) {
//        dataset.addValue(rs.getDouble("total"), "أرباح", rs.getString("day"));
//    }
//    JFreeChart barChart = ChartFactory.createBarChart("أرباح الشهر المختار", "اليوم", "الإجمالي", dataset);
//    ChartPanel barPanel = new ChartPanel(barChart);
//    if (jPanel6 != null) { jPanel6.removeAll();
//    jPanel6.add(barPanel);
//    jPanel6.validate(); jPanel6.repaint(); }
//
//    // عدد النزلاء الحاليين
//    ResultSet current = st.executeQuery("SELECT COUNT(DISTINCT customer_name) FROM eventss WHERE date_event = TRUNC(SYSDATE)");
//    if (current.next()) jTextField7.setText(String.valueOf(current.getInt(1)));
//
//    // عدد نزلاء الشهر
//    ResultSet monthGuests = st.executeQuery("SELECT COUNT(DISTINCT customer_name) FROM eventss WHERE TO_CHAR(date_event, 'YYYY/MM') = '" + selectedMonth + "'");
//    if (monthGuests.next()) jTextField8.setText(String.valueOf(monthGuests.getInt(1)));
//
//    // أرباح اليوم
//    ResultSet today = st.executeQuery("SELECT SUM(event_price) FROM eventss WHERE date_event = TRUNC(SYSDATE)");
//    if (today.next()) jTextField9.setText(String.format("%.2f", today.getDouble(1)));
//
//} catch (Exception e) {
//    e.printStackTrace();
//}


    try {
        String[] parts = jTextField6.getText().split("/"); // مثال: 2025/02
        if (parts.length != 2) {
            JOptionPane.showMessageDialog(null, "Please enter the date in the format: 2025/02");
            return;
        }

        String year = parts[0];
        String month = parts[1];

        ResultSet rs = Select.getData("SELECT SUM(event_price) FROM eventss WHERE TO_CHAR(date_event, 'YYYY') = '" + year + "' AND TO_CHAR(date_event, 'MM') = '" + month + "'");
        if (rs.next()) {
            int monthlyTotal = rs.getInt(1);
            jTextField4.setText(String.valueOf(monthlyTotal));
            drawBarChart(monthlyTotal);
        } else {
            jTextField4.setText("0");
            drawBarChart(0);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "خطأ في الزرار رقم 5:\n" + e.getMessage());
    
}

    



    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
         setVisible(false);
         new adminHome().setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

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
            java.util.logging.Logger.getLogger(Report_events.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Report_events.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Report_events.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Report_events.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
         new Report_events().setVisible(true);
                
            }});}
       
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
