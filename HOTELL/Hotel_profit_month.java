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
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import project.Select;

/**
 *
 * @author HP
 */
public class Hotel_profit_month extends javax.swing.JFrame {

    /**
     * Creates new form Hotel_profit_month
     */
    public Hotel_profit_month() {
        initComponents();
        jTextField3.setText(new SimpleDateFormat("yyyy/MM").format(new java.util.Date()));
        jTextField1.setEditable(false);

    }

   
    
    public int getMonthProfit(String yearMonth) {
    int total = 0;
    try {
        String queryCustomer = "SELECT SUM(TO_NUMBER(totalAmount)) FROM customer WHERE TO_CHAR(TO_DATE(checkIn, 'YYYY-MM-DD'), 'YYYY/MM') = '" + yearMonth + "'";
        String queryEvents = "SELECT SUM(event_price) FROM eventss WHERE TO_CHAR(date_event, 'YYYY/MM') = '" + yearMonth + "'";

        ResultSet rs1 = Select.getData(queryCustomer);
        if (rs1.next()) total += rs1.getInt(1);

        ResultSet rs2 = Select.getData(queryEvents);
        if (rs2.next()) total += rs2.getInt(1);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "خطأ في getMonthProfit: " + e.getMessage());
    }
    return total;
}

public void drawProfitAreaChart(String monthName, int profit) {
    try {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(profit, "Profits", monthName);

        JFreeChart areaChart = ChartFactory.createAreaChart(
                "Earnings Report",
                "Month",
                "Total",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        // تحسين مظهر
        CategoryPlot plot = areaChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // تفعيل التفاعل مع الماوس
        areaChart.getPlot().setOutlinePaint(Color.BLACK);

        ChartPanel chartPanel = new ChartPanel(areaChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 400));
        chartPanel.setMouseWheelEnabled(true); // لتكبير وتصغير بالرول

        jPanel1.removeAll();
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(chartPanel, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "خطأ في Area Chart: " + e.getMessage());
    }
}

    
    
    
    
    
    
    
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-us-dollar-circled.gif"))); // NOI18N
        jLabel1.setText("Hotel profit report for the month");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 989, 62));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Total hotel during the month");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 240, -1));

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(363, 130, 370, -1));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-search.gif"))); // NOI18N
        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 110, 220, -1));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CLEAR.png"))); // NOI18N
        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(986, 150, 180, 57));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-back.gif"))); // NOI18N
        jButton2.setText("Back");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1007, 0, 166, 62));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Total hotels for a given month and loss percentage");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        jTextField2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 178, -1));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-search.gif"))); // NOI18N
        jButton4.setText("Search");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 190, 220, -1));

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 210, 168, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 280, 1140, 370));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton5.setText("Print the report in PDF format");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 680, 1150, 60));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/total report.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new adminHome().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
          setVisible(false);
         new Hotel_profit_month().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    String thisMonth = new SimpleDateFormat("yyyy/MM").format(new java.util.Date());
   int currentProfit = getMonthProfit(thisMonth);
jTextField1.setText(String.valueOf(currentProfit));
drawProfitAreaChart(thisMonth, currentProfit);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    try {
        String selectedMonth = jTextField3.getText().trim(); // مثال: 2025/02
        if (!selectedMonth.matches("\\d{4}/\\d{2}")) {
            JOptionPane.showMessageDialog(null, "برجاء إدخال التاريخ بصيغة صحيحة: yyyy/MM");
            return;
        }

        String currentMonth = new SimpleDateFormat("yyyy/MM").format(new java.util.Date());

        int selectedProfit = getMonthProfit(selectedMonth);
        int currentProfit = getMonthProfit(currentMonth);

        jTextField1.setText(String.valueOf(selectedProfit));
        drawProfitAreaChart(selectedMonth, selectedProfit);

        if (currentProfit == 0) {
            jTextField2.setText("لا يمكن الحساب لأن أرباح الشهر الحالي = 0");
            return;
        }

        double diff = selectedProfit - currentProfit;
        double percent = (diff / currentProfit) * 100;
        String status = (percent >= 0) ? "profit" : "loss";
        jTextField2.setText(status + " " + String.format("%.2f", Math.abs(percent)) + "%");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "خطأ في زر المقارنة: " + e.getMessage());
    }


    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    
    String earnings = jTextField1.getText().trim();
    String percentage = jTextField2.getText().trim();
    String selectedMonth = jTextField3.getText().trim();

    // تحقق من أن البيانات موجودة
    if (earnings.isEmpty() || percentage.isEmpty() || selectedMonth.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No data to issue the report!");
        return;
    }

    try {
        String path = "D:\\";
        Document doc = new Document();
        String fileName = "hotel_profit_report_" + selectedMonth.replace("/", "-") + ".pdf";
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(path + fileName));
        doc.open();

        // إضافة شعار الفندق
        try {
            Image logo = Image.getInstance("C:\\Users\\HP\\Documents\\NetBeansProjects\\Hotel_mohamed_ramadan\\src\\images\\Hotel_Home_mohamed_ramadan.jpg");
            logo.scaleToFit(120, 120);
            logo.setAlignment(Element.ALIGN_CENTER);
            doc.add(logo);
        } catch (Exception ex) {
            // تجاهل مشكلة الشعار وعدم إيقاف التقرير
        }

        // عنوان التقرير
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD, BaseColor.BLUE);
        Paragraph title = new Paragraph("MOHAMED RAMADAN  Hotel Flory", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);

        doc.add(new Paragraph(" ")); // سطر فارغ

        // التفاصيل
        Font contentFont = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, BaseColor.BLACK);
        Paragraph info = new Paragraph();
        info.setFont(contentFont);
        info.add("Specific month:    " + selectedMonth + "\n");
        info.add("Total Profits:     " + earnings + " pound\n");
        info.add("Percentage difference from the current month:    " + percentage + "\n");
        doc.add(info);

        doc.add(new Paragraph(" ")); // سطر فارغ

        // التقاط الرسم البياني من jPanel1 كصورة
        BufferedImage chartImage = new BufferedImage(jPanel1.getWidth(), jPanel1.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = chartImage.createGraphics();
        jPanel1.paint(g2);
        g2.dispose();

        // تحويل الصورة لـ iText Image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);
        Image chartITextImage = Image.getInstance(baos.toByteArray());
        chartITextImage.scaleToFit(500, 400);
        chartITextImage.setAlignment(Element.ALIGN_CENTER);
        doc.add(chartITextImage);
           
        
        
        Paragraph separator = new Paragraph("****************************************************************************************************************");
separator.setAlignment(Element.ALIGN_CENTER);
doc.add(separator);
        
        
        doc.close();
        writer.close();

       // JOptionPane.showConfirmDialog(null, "The report was created successfully in: " + path + fileName);
       int shoise= JOptionPane.showConfirmDialog(null, "The report was created successfully in:","Select",JOptionPane.OK_CANCEL_OPTION);
          try {
            if (shoise == JOptionPane.OK_OPTION) {
              Desktop.getDesktop().open(new File(path + fileName));
            
        }
        } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "تعذر فتح الملف تلقائيًا:\n" + e.getMessage());

        }
       
    } catch (Exception e) {//fileName//
        JOptionPane.showMessageDialog(null, "خطأ أثناء إنشاء التقرير:\n" + e.getMessage());
    }


        
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
            java.util.logging.Logger.getLogger(Hotel_profit_month.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Hotel_profit_month.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Hotel_profit_month.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Hotel_profit_month.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Hotel_profit_month().setVisible(true);
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
