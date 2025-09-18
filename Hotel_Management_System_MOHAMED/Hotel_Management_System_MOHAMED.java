
package Hotel_Management_System_MOHAMED;

import HOTELL.signup;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import HOTELL.login;
import HOTELL.forgotPassword;
import HOTELL.home;
import HOTELL.CustomerCheckIn;
import HOTELL.CustomerCheckOut;
import HOTELL.CustomerDetailsBill;
import HOTELL.Lodaing;
import HOTELL.adminHome;
  
        import java.io.File;
import java.net.URL;
public class Hotel_Management_System_MOHAMED {

    public static void main(String[] args) {
          
//          Lodaing lodaing = new Lodaing();
//        lodaing.setVisible(true);
        
        // اسم الملف اللي عايز تلاقيه
      String fileName = "config.properties";

        // ابحث في classpath
        URL fileUrl = Hotel_Management_System_MOHAMED.class.getClassLoader().getResource(fileName);
        
        if (fileUrl != null) {
            // إذا كان الملف موجود في classpath
            System.out.println("تم العثور على الملف في: " + fileUrl.getPath());
        } else {
            System.out.println("لم يتم العثور على الملف: " + fileName);
            // إضافة مسار الـ resources للتأكد من وجوده
            String filePath = "src/main/resources/" + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                System.out.println("تم العثور على الملف في: " + file.getAbsolutePath());
            } else {
                System.out.println("الملف مش موجود في المسار المتوقع: " + filePath);
            }}}}
        
    

            
      
      
      
      
      
      
      
      
      
      
  