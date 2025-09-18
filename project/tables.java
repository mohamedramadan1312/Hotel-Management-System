/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class tables {

    public static void main(String[] args) {

        Connection con = null;
        Statement st = null;
        try {
            con = ConnectionProvider.getCon();
            st = con.createStatement();
         st.executeUpdate("create table users(name VARCHAR(200), email VARCHAR(200), password VARCHAR(50), securityQuestion VARCHAR(500), answer VARCHAR(200), address VARCHAR(200), status VARCHAR(20))");
          st.executeUpdate("create table room(roomNo varchar(10), roomType varchar(200), bed varchar(200), price int,status varchar(20))");
          st.executeUpdate("create table customer(id int, name VARCHAR(200), mobileNumber VARCHAR(22), nationality VARCHAR(200), gender VARCHAR(50), email VARCHAR(200), idProof VARCHAR(200), address VARCHAR(500), checkIn VARCHAR(50), roomNo VARCHAR(10), bed VARCHAR(200), roomType VARCHAR(200), pricePerDay int, numberofDaysStay int, totalAmount VARCHAR(200), checkout VARCHAR(50))");
           st.executeUpdate("create table feedback(feedback_id number(10), user_name varchar2(200), user_email VARCHAR2(200), user_mobile VARCHAR2(22), user_rating VARCHAR2(50), user_comment VARCHAR2(500), response varchar2(500), Reply_status varchar2(25))");
             st.executeUpdate("create table eventss(id NUMBER PRIMARY KEY, event_name VARCHAR2(100)NOT NULL, hall_name VARCHAR2(100) NOT NULL, date_event DATE NOT NULL, time_event VARCHAR2(20),end_time VARCHAR2(10),guests_number NUMBER,customer_name VARCHAR2(100),event_price NUMBER)");
           st.executeUpdate("create table halls(id NUMBER PRIMARY KEY,hall_name VARCHAR2(100)UNIQUE NOT NULL,price_per_hour NUMBER NOT NULL, status VARCHAR2(20) DEFAULT 'Available')");
           st.executeUpdate("create table notifications(id NUMBER PRIMARY KEY,message VARCHAR2(500),event_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,seen_status VARCHAR2(10) DEFAULT 'unseen')");
           JOptionPane.showMessageDialog(null, "Table Created Successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                con.close();
                st.close();
            } catch (Exception e) {
            }

        }

    }
}
