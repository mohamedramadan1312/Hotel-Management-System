/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package HOTELL;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import project.ConnectionProvider;

/**
 *
 * @author HP
 */
public class DASHPORD extends javax.swing.JFrame {

    /** Creates new form DASHPORD */
    public DASHPORD() {
        initComponents();
         displayPieChart(PROPERTIES, TOP_ALIGNMENT);
       displayBarChart(TOP_ALIGNMENT);
       setCurrentMonthInTextField();

        
        
        jTextField4.setText("Display");

    // جعل الحقول غير قابلة للتعديل باستثناء jTextField4
    jTextField1.setEditable(false);
    jTextField2.setEditable(false);
    jTextField3.setEditable(false);
    jTextField5.setEditable(false);
    jTextField7.setEditable(false);  // جعل jTextField7 غير قابل للتعديل
    jTextField8.setEditable(false);  // جعل jTextField8 غير قابل للتعديل

    // إضافة KeyListener لمنع مسح النص في jTextField4
    jTextField4.addKeyListener(new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            // منع المستخدم من مسح النص في jTextField4
            if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == KeyEvent.VK_DELETE) {
                e.consume();  // منع الحذف
            }
        }
    });
      
    // عندما يضغط المستخدم على الزر، يتم تنفيذ الكود التالي
    jButton2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            getDashboardData(); // استدعاء دالة تحميل البيانات عند الضغط على الزر
            getDashboardData2();
        }
    });
    
    
    jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // استدعاء الميثود عند الضغط على الزر
                jButton5ActionPerformed(evt);
            }
        });
    
    
    
    
    
}
    
    
    
    
    
     public void displayPieChart(double bookedPercentage, double availablePercentage) {
    // إنشاء Dataset للرسم البياني
    DefaultPieDataset dataset = new DefaultPieDataset();
    dataset.setValue("Booked", bookedPercentage);
    dataset.setValue("Not Booked", availablePercentage);

    // إنشاء الرسم البياني الدائري
    JFreeChart chart = ChartFactory.createPieChart(
        "Room Status",   // عنوان الرسم البياني
        dataset,         // البيانات
        true,            // يظهر الأسطورة
        true,            // يمكن التفاعل مع الرسم
        false);          // لا يظهر العناوين

    // تخصيص الألوان الخاصة بالرسم البياني
    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setSectionPaint("Booked", new java.awt.Color(0, 128, 0));  // اللون الأخضر للحجز
    plot.setSectionPaint("Not Booked", new java.awt.Color(255, 69, 0));  // اللون الأحمر للغرف غير المحجوزة

    // إضافة الرسم البياني إلى jPanel3
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 200));  // تحديد حجم الرسم البياني
    jPanel3.setLayout(new java.awt.BorderLayout());  // تأكد من تعيين التخطيط بشكل صحيح
    jPanel3.removeAll();  // إزالة أي محتوى سابق في jPanel3
    jPanel3.add(chartPanel, BorderLayout.CENTER);  // إضافة الرسم البياني إلى jPanel3
    jPanel3.revalidate();  // إعادة التحقق من واجهة المستخدم
    jPanel3.repaint();  // تحديث الرسم البياني
}
    
 public void displayBarChart(double monthlyRevenue) {
    // إنشاء Dataset للرسم البياني العمودي
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
    // إضافة البيانات إلى dataset
    dataset.addValue(monthlyRevenue, "Revenue", "Month");

    // إنشاء الرسم البياني العمودي
    JFreeChart chart = ChartFactory.createBarChart(
        "Monthly Revenue",      // عنوان الرسم البياني
        "Month",                // المحور الأفقي (الشهر)
        "Revenue ($)",          // المحور العمودي (الإيرادات)
        dataset,                // البيانات
        org.jfree.chart.plot.PlotOrientation.VERTICAL, // اتجاه الرسم البياني
        false,                  // لا تظهر الأسطورة
        true,                   // يمكن التفاعل مع الرسم البياني
        false                   // لا تظهر العناوين
    );

    // تخصيص اللون في الرسم البياني العمودي
    CategoryPlot plot = (CategoryPlot) chart.getPlot();
    plot.getRenderer().setSeriesPaint(0, new java.awt.Color(0, 128, 255));  // اللون الأزرق للإيرادات

    // تخصيص نطاق المحور العمودي (Y-axis) ليزداد تلقائيًا
    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setAutoRangeIncludesZero(true);  // ضمان أن القيمة تبدأ من 0
    double maxRevenue = monthlyRevenue * 1.2;  // زيادة الحد الأقصى بنسبة 20% (يمكنك تعديل النسبة حسب الحاجة)
    rangeAxis.setUpperBound(maxRevenue);  // تعيين الحد الأقصى للقيمة على المحور العمودي

    // إضافة الرسم البياني إلى jPanel4
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(400, 250));  // تحديد حجم الرسم البياني (تصغيره)
    jPanel4.setLayout(new java.awt.BorderLayout());  // تأكد من تعيين التخطيط بشكل صحيح
    jPanel4.removeAll();  // إزالة أي محتوى سابق في jPanel4
    jPanel4.add(chartPanel, BorderLayout.CENTER);  // إضافة الرسم البياني إلى jPanel4
    jPanel4.revalidate();  // إعادة التحقق من واجهة المستخدم
    jPanel4.repaint();  // تحديث الرسم البياني
}
    
    
public void getDashboardData2() {
    Connection conn = null;
    ResultSet rsGuests = null;
    ResultSet rsMonthlyGuests = null;
    int currentGuests = 0;
    int monthlyGuests = 0;

    try {
        // الاتصال بقاعدة البيانات
        conn = ConnectionProvider.getCon();

        // استعلام للحصول على عدد النزلاء الحاليين (الذين لم يقوموا بتسجيل المغادرة)
        String query1 = "SELECT COUNT(*) FROM customer WHERE checkout IS NULL";
        PreparedStatement stmt = conn.prepareStatement(query1);
        rsGuests = stmt.executeQuery();
        if (rsGuests.next()) {
            currentGuests = rsGuests.getInt(1);
        }

        // استعلام للحصول على عدد النزلاء خلال الشهر الحالي مع تحويل تاريخ checkout باستخدام TO_DATE
        String query2 = "SELECT COUNT(*) FROM customer " +
                        "WHERE TO_CHAR(TO_DATE(checkin, 'yyyy/MM/dd'), 'yyyy-MM') = TO_CHAR(CURRENT_DATE, 'yyyy-MM') " +
                        "AND (checkout IS NULL OR TO_DATE(checkout, 'yyyy/MM/dd') IS NOT NULL)";
        stmt = conn.prepareStatement(query2);
        rsMonthlyGuests = stmt.executeQuery();
        if (rsMonthlyGuests.next()) {
            monthlyGuests = rsMonthlyGuests.getInt(1);
        }

        // عرض عدد النزلاء الحاليين في jTextField7
        jTextField7.setText(String.valueOf(currentGuests));

        // عرض عدد النزلاء خلال الشهر في jTextField8
        jTextField8.setText(String.valueOf(monthlyGuests));

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, ex);
    } finally {
        try {
            if (rsGuests != null) rsGuests.close();
            if (rsMonthlyGuests != null) rsMonthlyGuests.close();
            if (conn != null) conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}



     
     
     
    
    
  public void getDashboardData() {
    // إعداد الاتصال بقاعدة البيانات
    Connection conn = null;
    ResultSet rs = null;
    int bookedRooms = 0;
    int availableRooms = 0;
    double monthlyRevenue = 0.0;
     ResultSet rsGuests = null;
    ResultSet rsMonthlyGuests = null;
    int currentGuests = 0;
    int monthlyGuests = 0;
    
    // هنا نقوم باستخدام "Display" مباشرة بدلاً من قراءة قيمة التاريخ من jTextField4
    String month = jTextField4.getText().trim(); 

    // التحقق إذا كانت قيمة الجملة في jTextField4 هي "Display"
    if (!month.equals("Display")) {
        // إذا كانت القيمة ليست "Display" نعرض رسالة تحذيرية
        JOptionPane.showMessageDialog(this, "Please enter 'Display' in the text field.");
        return;  // عدم متابعة الكود إذا لم تكن القيمة هي "Display"
    }

    try {
        // الاتصال بقاعدة البيانات
        conn = ConnectionProvider.getCon();
        
        // استعلام للحصول على عدد الغرف المحجوزة
        String query1 = "SELECT COUNT(*) FROM room WHERE status = 'Booked'";
        PreparedStatement stmt = conn.prepareStatement(query1);
        rs = stmt.executeQuery();
        if (rs.next()) {
            bookedRooms = rs.getInt(1);
        }
        
        // استعلام للحصول على عدد الغرف الغير محجوزة
        String query2 = "SELECT COUNT(*) FROM room WHERE status = 'Not Booked'";
        stmt = conn.prepareStatement(query2);
        rs = stmt.executeQuery();
        if (rs.next()) {
            availableRooms = rs.getInt(1);
        }
        
        // استعلام للحصول على الإيرادات الشهرية (جمع الأسعار للغرف المحجوزة)
      //  String query3 = "SELECT SUM(totalAmount) FROM customer WHERE totalAmount";
        String query3 = "SELECT SUM(TO_NUMBER(totalAmount)) " +
                    "FROM customer " +
                    "WHERE totalAmount IS NOT NULL " +
                    "AND TO_CHAR(TO_DATE(checkout, 'yyyy/MM/dd'), 'yyyy-MM') = TO_CHAR(CURRENT_DATE, 'yyyy-MM')";


        stmt = conn.prepareStatement(query3);
        rs = stmt.executeQuery();
        if (rs.next()) {
            monthlyRevenue = rs.getDouble(1);
        }

        // التحقق إذا كانت البيانات كلها null أو 0
        if (bookedRooms == 0 && availableRooms == 0 && monthlyRevenue == 0) {
            JOptionPane.showMessageDialog(this, "No data found.");
            return;  // إذا لم توجد بيانات، لا تتابع في إظهار البيانات
        }

        // تحديث بيانات الواجهة في Java (Dashboard)
        jTextField1.setText(bookedRooms + "");  // عرض عدد الغرف المحجوزة في jTextField1
        jTextField2.setText(availableRooms + "");  // عرض عدد الغرف المتاحة في jTextField2
        jTextField3.setText("$" + monthlyRevenue);  // عرض الإيرادات الشهرية في jTextField3
        
        int totalRooms = bookedRooms + availableRooms;
        double bookedPercentage = (double) bookedRooms / totalRooms * 100;
        double availablePercentage = (double) availableRooms / totalRooms * 100;
        jTextField5.setText(String.format("Booked: %.2f%%, Not Booked: %.2f%%", bookedPercentage, availablePercentage));
                  
         displayPieChart(bookedPercentage, availablePercentage);  // استدعاء دالة عرض الرسم البياني
                 displayBarChart(monthlyRevenue);  // استدعاء دالة عرض الرسم البياني العمودي
        

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    } finally {
        try {
            if (rs != null) rs.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, e);
        }
    }
}
          
  
  
  
  
  
  
  private void setCurrentMonthInTextField() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    String currentMonth = dateFormat.format(Calendar.getInstance().getTime());
    jTextField6.setText(currentMonth);  // تعيين الشهر الحالي في jTextField6
}

// كود الضغط على الجافا لتحديث الإيرادات بناءً على الشهر المدخل في jTextField6
private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
    // قراءة التاريخ المدخل في jTextField6
    String enteredMonth = jTextField6.getText().trim();  // إدخال الشهر في صيغة yyyy-MM
    
    // التأكد من أن التاريخ المدخل ليس فارغًا أو غير صحيح
    if (enteredMonth.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a valid month in the format yyyy-MM.");
        return;
    }

    // التحقق من أن المدخل يتبع الصيغة الصحيحة (yyyy-MM)
    if (!enteredMonth.matches("\\d{4}-\\d{2}")) {
        JOptionPane.showMessageDialog(this, "Please enter a valid month in the format yyyy-MM.");
        return;
    }
    
    

    // إعداد الاتصال بقاعدة البيانات
    Connection conn = null;
    ResultSet rs = null;
    double monthlyRevenue = 0.0;

    // استعلام للحصول على الإيرادات الشهرية بناءً على الشهر المدخل
    String query = "SELECT SUM(TO_NUMBER(totalAmount)) " +
                   "FROM customer " +
                   "WHERE totalAmount IS NOT NULL " +
                   "AND TO_CHAR(TO_DATE(checkout, 'yyyy/MM/dd'), 'yyyy-MM') = ?";
    
    try {
        // الاتصال بقاعدة البيانات
        conn = ConnectionProvider.getCon();
        
        // إعداد الاستعلام
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, enteredMonth);  // تمرير الشهر المدخل في الجملة الاستعلامية
        
        // تنفيذ الاستعلام
        rs = stmt.executeQuery();
        
        // قراءة النتيجة من الاستعلام
        if (rs.next()) {
            monthlyRevenue = rs.getDouble(1);
        }

        // عرض الإيرادات الشهرية في jTextField3
        jTextField3.setText("$" + monthlyRevenue);  // عرض الإيرادات الشهرية
                displayBarChart(monthlyRevenue);  // استدعاء دالة عرض الرسم البياني

        
                
                
                
                
                
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    } finally {
        try {
            if (rs != null) rs.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
        
        
        
        
        
        
        
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel12 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/DASH_pord.jpg"))); // NOI18N

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/401739128288.jpg"))); // NOI18N
        jLabel6.setText("jLabel6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(50, 118));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-back.gif"))); // NOI18N
        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1420, 0, 130, 60));

        jLabel1.setFont(new java.awt.Font("Perpetua Titling MT", 1, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-bar-chart.gif"))); // NOI18N
        jLabel1.setText("Dash pord");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 13, 240, 60));

        jTextField4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 370, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Reserved and unreserved");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, -1, -1));

        jTextField5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 460, 390, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Number of unbooked rooms");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, 22));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Total Revenue");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 196, -1));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-search.gif"))); // NOI18N
        jButton2.setText("Search");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, 150, 61));

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, 390, -1));

        jTextField2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 310, 390, -1));

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 390, 390, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Number of rooms booked");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 196, -1));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 120, 110, 61));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 590, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 290, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 80, 590, 290));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 590, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 420, 590, 320));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Revenue account by date");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, -1, -1));

        jTextField6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 530, 390, -1));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-search.gif"))); // NOI18N
        jButton5.setText("Search");
        jButton5.setToolTipText("");
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 510, 150, 70));

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
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 210, 50));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Current Guests");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 600, 170, -1));

        jTextField7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 600, 390, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Number of guests per month");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 670, -1, 20));

        jTextField8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 670, 390, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/DASH_pord.jpg"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         setVisible(false);
         new adminHome().setVisible(true);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        setVisible(false);
        new DASHPORD().setVisible(true);
        
        
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
          int a = JOptionPane.showConfirmDialog(null, "Do you really Want to Logout", "Select", JOptionPane.YES_NO_OPTION);
     
        if (a == 0) {
            setVisible(false);
            new login().setVisible(true);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

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
            java.util.logging.Logger.getLogger(DASHPORD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DASHPORD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DASHPORD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DASHPORD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DASHPORD().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables

}
