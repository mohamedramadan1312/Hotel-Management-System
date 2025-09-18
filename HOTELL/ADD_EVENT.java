/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HOTELL;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.demo.DateChooserPanel;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import project.InsertUpdateDelete;
import project.Select;

/**
 *
 * @author HP
 */
public class ADD_EVENT extends javax.swing.JFrame {
    private JLabel backgroundLabel;
    private JDateChooser dateChooser;
    private JSpinner timeSpinner;
    private JSpinner  endSpinner;
    public ADD_EVENT() {
           initComponents();
          
       loadAvailableHalls();
        updateTable();
         jTextField6.setEditable(false);
        setLayout(null); // تأكد من استخدام AbsoluteLayout

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setBounds(925, 260, 293, 30);
       // add(dateChooser);
       jLabel10.add(dateChooser);
           Calendar mo = Calendar.getInstance();
           mo.add(Calendar.HOUR_OF_DAY, 1);
         SpinnerDateModel model = new SpinnerDateModel();
         model.setValue(mo.getTime());
        timeSpinner = new JSpinner(model);
               setLayout(null); // تأكد من استخدام AbsoluteLayout

        JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, "HH:mm a");
        timeSpinner.setEditor(editor);
        timeSpinner.setBounds(925, 320, 293, 30);
       jLabel10.add(timeSpinner);
        
        
        // إنشاء الوقت الحالي
Calendar now = Calendar.getInstance();
now.add(Calendar.HOUR_OF_DAY, 2); // يضيف ساعة واحدة

// إنشاء الموديل
SpinnerDateModel endModel = new SpinnerDateModel();
endModel.setValue(now.getTime());

// إعداد الـ JSpinner
        setLayout(null); // تأكد من استخدام AbsoluteLayout

endSpinner = new JSpinner(endModel);
JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endSpinner, "HH:mm a");
endSpinner.setEditor(endEditor);
endSpinner.setBounds(925, 499, 293, 30); // عدل المكان حسب تصميمك
jLabel10.add(endSpinner);


  timeSpinner.addChangeListener(e -> {
            Date start = (Date) timeSpinner.getValue();
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.add(Calendar.HOUR_OF_DAY, 2);
            endSpinner.setValue(cal.getTime());
        }); 
    

    jComboBox1.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        updatePriceAutomatically();
    }
});

((JSpinner.DefaultEditor) timeSpinner.getEditor()).getTextField().addPropertyChangeListener("value", evt -> {
    updatePriceAutomatically();
});

((JSpinner.DefaultEditor) endSpinner.getEditor()).getTextField().addPropertyChangeListener("value", evt -> {
    updatePriceAutomatically();
});
    
     


jButton5.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String searchText = jTextField7.getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter search text!");
            return;
        }

        try {
            String query = "";

            if (searchText.matches("\\d+")) {
                // بحث بالرقم
                query = "SELECT id, event_name, hall_name, date_event, time_event, guests_number, customer_name, end_time, event_price, " +
                        "CASE " +
                        "WHEN SYSDATE < (date_event + (TO_NUMBER(SUBSTR(time_event,1,2))/24)) THEN 'Not Start' " +
                        "WHEN SYSDATE BETWEEN (date_event + (TO_NUMBER(SUBSTR(time_event,1,2))/24)) " +
                        "AND (date_event + (TO_NUMBER(SUBSTR(end_time,1,2))/24)) THEN 'Running Now' " +
                        "WHEN SYSDATE > (date_event + (TO_NUMBER(SUBSTR(end_time,1,2))/24)) THEN 'Event Over' " +
                        "ELSE 'غير معروف' " +
                        "END AS current_status " +
                        "FROM eventss WHERE id = " + searchText;
            } else {
                // بحث بالاسم
                query = "SELECT id, event_name, hall_name, date_event, time_event, guests_number, customer_name, end_time, event_price, " +
                        "CASE " +
                        "WHEN SYSDATE < (date_event + (TO_NUMBER(SUBSTR(time_event,1,2))/24)) THEN 'Not Start' " +
                        "WHEN SYSDATE BETWEEN (date_event + (TO_NUMBER(SUBSTR(time_event,1,2))/24)) " +
                        "AND (date_event + (TO_NUMBER(SUBSTR(end_time,1,2))/24)) THEN 'Running Now' " +
                        "WHEN SYSDATE > (date_event + (TO_NUMBER(SUBSTR(end_time,1,2))/24)) THEN 'Event Over' " +
                        "ELSE 'غير معروف' " +
                        "END AS current_status " +
                        "FROM eventss " +
                        "WHERE LOWER(event_name) LIKE LOWER('%" + searchText + "%') " +
                        "OR LOWER(customer_name) LIKE LOWER('%" + searchText + "%')";
            }

            ResultSet rs = Select.getData(query);
            loadTable(rs);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
});
// Not Start




jComboBox2.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String selectedStatus = jComboBox2.getSelectedItem().toString();
        String query = "";

        try {
            if (selectedStatus.equals("Not Start")) {
                query = "SELECT id, event_name, hall_name, date_event, time_event, guests_number, customer_name, end_time, event_price, 'Not Start' AS current_status " +
                        "FROM eventss " +
                        "WHERE SYSDATE < TO_DATE(TO_CHAR(date_event, 'YYYY-MM-DD') || ' ' || time_event, 'YYYY-MM-DD HH24:MI')";
            } 
            else if (selectedStatus.equals("Running Now")) {
                query = "SELECT id, event_name, hall_name, date_event, time_event, guests_number, customer_name, end_time, event_price, 'Running Now' AS current_status " +
                        "FROM eventss " +
                        "WHERE SYSDATE BETWEEN TO_DATE(TO_CHAR(date_event, 'YYYY-MM-DD') || ' ' || time_event, 'YYYY-MM-DD HH24:MI') " +
                        "AND TO_DATE(TO_CHAR(date_event, 'YYYY-MM-DD') || ' ' || end_time, 'YYYY-MM-DD HH24:MI')";
            } 
            else if (selectedStatus.equals("Event Over")) {
                query = "SELECT id, event_name, hall_name, date_event, time_event, guests_number, customer_name, end_time, event_price, 'Event Over' AS current_status " +
                        "FROM eventss " +
                        "WHERE SYSDATE > TO_DATE(TO_CHAR(date_event, 'YYYY-MM-DD') || ' ' || end_time, 'YYYY-MM-DD HH24:MI')";
            } 
            else {
                JOptionPane.showMessageDialog(null, "Please select a valid status!");
                return;
            }

            ResultSet rs = Select.getData(query);
            loadTable(rs);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error loading table: " + ex.getMessage());
        }
    }
});


    }
    

    
    
    
    
    
    
private void loadTable(ResultSet rs) {
    try {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        while (rs.next()) {
            Object[] row = new Object[]{
                rs.getInt("id"),
                rs.getString("event_name"),
                rs.getString("hall_name"),
                rs.getDate("date_event"),
                rs.getString("time_event"),
                rs.getInt("guests_number"),
                rs.getString("customer_name"),
                rs.getString("end_time"),
                rs.getInt("event_price"),
                rs.getString("current_status") != null ? rs.getString("current_status") : "Unknown"
            };
            model.addRow(row);
        }

        rs.close();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error loading table: " + ex.getMessage());
    }
}

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
private void loadAvailableHalls() {
    try {
        jComboBox1.removeAllItems();
        ResultSet rs = Select.getData("SELECT hall_name FROM halls WHERE LOWER(status) = 'available'");
        while (rs.next()) {
            jComboBox1.addItem(rs.getString("hall_name"));
        }
        rs.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading halls: " + e.getMessage());
    }
}





     //   jButton1.addActionListener(e -> bookEvent());
        private void updatePriceAutomatically() {
    try {
        if (jComboBox1.getSelectedItem() == null || 
            timeSpinner.getValue() == null || 
            endSpinner.getValue() == null) {
            jTextField6.setText("");
            return;
        }

        String hallName = jComboBox1.getSelectedItem().toString();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date start = timeFormat.parse(timeFormat.format((Date) timeSpinner.getValue()));
        Date end = timeFormat.parse(timeFormat.format((Date) endSpinner.getValue()));

        long diff = end.getTime() - start.getTime();
        int hours = (int) (diff / (1000 * 60 * 60));
        if (hours <= 0) {
            jTextField6.setText("Error in time and price");
            return;
        }

        ResultSet rs = Select.getData("SELECT price_per_hour FROM halls WHERE hall_name = '" + hallName + "'");
        int pricePerHour = 0;
        if (rs.next()) {
            pricePerHour = rs.getInt("price_per_hour");
        }
        rs.close();

        int totalPrice = pricePerHour * hours;
        jTextField6.setText(String.valueOf(totalPrice));

    } catch (Exception ex) {
        jTextField6.setText("Error");
    }
}

        
       
        
         
    
    

   

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ADD event.jpg"))); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(30, 118));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Name of the event");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(732, 154, 147, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Hall name");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(732, 205, 147, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Date of the event");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(732, 257, 147, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("the hour");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(732, 315, 147, 22));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Number of attendees");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(732, 378, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Customer name");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(732, 441, 147, -1));

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(924, 151, 292, -1));

        jTextField4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(924, 375, 292, -1));

        jTextField5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(924, 438, 292, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 153));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/EVENTS.png"))); // NOI18N
        jLabel7.setText("Events");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 276, -1));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jButton1.setText("Add Event");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 699, 1268, -1));

        jTable1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Event_id", "name event", "hall name", "date event", "hour", "number of attendees", "customer name", "end time", "Price", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 151, 702, 535));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("End Time");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 500, 147, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Price ");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(732, 559, 147, -1));

        jTextField6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(924, 556, 292, -1));

        jComboBox1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(924, 202, 292, -1));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-close.gif"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 0, 48, 48));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setText("Claer");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(732, 617, 102, 69));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/END EVENT.gif"))); // NOI18N
        jButton4.setText("End Event");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 620, 160, 69));

        jComboBox2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Not Start", "Running Now", "Event Over" }));
        getContentPane().add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 87, 227, 45));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-search.gif"))); // NOI18N
        jButton5.setText("Search Event");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 81, -1, -1));

        jTextField7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(542, 88, 172, 44));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ADD event.jpg"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    // TODO add your handling code here:
try {
    if (jTextField3.getText().isEmpty() || 
        jComboBox1.getSelectedItem() == null ||
        jTextField5.getText().isEmpty() || 
        jTextField4.getText().isEmpty() ||
        dateChooser.getDate() == null ||
        timeSpinner.getValue() == null ||
        endSpinner.getValue() == null) {
        
        JOptionPane.showMessageDialog(this, "Please fill all fields!");
        return;
    }

    String eventName = jTextField3.getText();
    String hallName = jComboBox1.getSelectedItem().toString();
    String customerName = jTextField5.getText();
    int guests = Integer.parseInt(jTextField4.getText());

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String date = sdf.format(dateChooser.getDate());

    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    String startTime = timeFormat.format((Date) timeSpinner.getValue());
    String endTime = timeFormat.format((Date) endSpinner.getValue());

    // حساب عدد الساعات
    SimpleDateFormat fullFormat = new SimpleDateFormat("HH:mm");
    Date start = fullFormat.parse(startTime);
    Date end = fullFormat.parse(endTime);

    long difference = end.getTime() - start.getTime();
    int hours = (int) (difference / (1000 * 60 * 60));
    if (hours <= 0) {
        JOptionPane.showMessageDialog(null, "End time must be after start time!");
        return;
    }

    // جلب السعر لكل ساعة
    int pricePerHour = 0;
    ResultSet rs = Select.getData("SELECT price_per_hour FROM halls WHERE hall_name = '" + hallName + "'");
    if (rs.next()) {
        pricePerHour = rs.getInt("price_per_hour");
    }
    rs.close();

    // حساب السعر النهائي
    int totalPrice = pricePerHour * hours;
    
    // عرض السعر النهائي في خانة السعر
    jTextField6.setText(String.valueOf(totalPrice));
    jTextField6.setEditable(false); // قفل خانة السعر حتى لا يتم التعديل عليها

    // تنفيذ عملية الإدخال
    String insertEvent = String.format(
        "INSERT INTO eventss (id, event_name, hall_name, date_event, time_event, guests_number, customer_name, end_time, event_price) " +
        "VALUES (event_id_sequnc.NEXTVAL, '%s', '%s', TO_DATE('%s', 'YYYY-MM-DD'), '%s', %d, '%s', '%s', %d)",
        eventName, hallName, date, startTime, guests, customerName, endTime, totalPrice
    );
    InsertUpdateDelete.setData(insertEvent, "Event booked successfully!");

    // تحديث حالة القاعة إلى "محجوزة"
    String updateHall = "UPDATE halls SET status = 'Booked' WHERE hall_name = '" + hallName + "'";
    InsertUpdateDelete.setData(updateHall, "");

    updateTable();
    loadAvailableHalls();

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
}}



// دالة لتحديث البيانات في الجدول
// دالة تحديث الجدول
private void updateTable() {
    try {
        ResultSet rs = Select.getData("SELECT * FROM eventss");
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        Date now = new Date();
        String today = dateFormat.format(now);
        String nowTime = timeFormat.format(now);
        Date nowTimeParsed = timeFormat.parse(nowTime);

        while (rs.next()) {
            String status = "Unknown";
            String startTimeStr = rs.getString("time_event");
            String endTimeStr = rs.getString("end_time");

            if (startTimeStr != null && endTimeStr != null) {
                Date startTime = timeFormat.parse(startTimeStr);
                Date endTime = timeFormat.parse(endTimeStr);
                Date eventDate = rs.getDate("date_event");

                if (dateFormat.format(eventDate).equals(today)) {
                    if (nowTimeParsed.before(startTime)) {
                        status = "Not start";
                    } else if (!nowTimeParsed.before(startTime) && !nowTimeParsed.after(endTime)) {
                        status = "Running now";
                    } else {
                        status = "Event over";
                        String hallName = rs.getString("hall_name");
                        InsertUpdateDelete.setData("UPDATE halls SET status = 'Available' WHERE hall_name = '" + hallName + "'", "");
                    }
                } else if (eventDate.before(dateFormat.parse(today))) {
                    status = "Event over";
                    String hallName = rs.getString("hall_name");
                    InsertUpdateDelete.setData("UPDATE halls SET status = 'Available' WHERE hall_name = '" + hallName + "'", "");
                } else {
                    status = "Not start";
                }
            }

            model.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("event_name"),
                rs.getString("hall_name"),
                rs.getDate("date_event"),
                startTimeStr,
                rs.getInt("guests_number"),
                rs.getString("customer_name"),
                endTimeStr,
                rs.getInt("event_price"),
                status
            });
        }

        rs.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading events: " + e.getMessage());
    }
    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);


       
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
                setVisible(false);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new ADD_EVENT().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
  // TODO add your handling code here:
try {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a row in the table.");
        return;
    }

    String eventName = jTable1.getValueAt(selectedRow, 1).toString(); // Event Name
    String hallName = jTable1.getValueAt(selectedRow, 2).toString();  // Hall Name

    // تأكيد قبل الحذف
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this event?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    // حذف الحجز من جدول eventss
    String deleteEvent = "DELETE FROM eventss WHERE event_name = '" + eventName + "'";
    InsertUpdateDelete.setData(deleteEvent, "Event cancelled successfully!");

    // تحديث حالة القاعة إلى Available
    String updateHallStatus = "UPDATE halls SET status = 'Available' WHERE hall_name = '" + hallName + "'";
    InsertUpdateDelete.setData(updateHallStatus, "");

    // تحديث الجدول
    updateTable();
    loadAvailableHalls();

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Error cancelling event: " + e.getMessage());
    e.printStackTrace();
}

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(ADD_EVENT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ADD_EVENT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ADD_EVENT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ADD_EVENT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ADD_EVENT().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
