# 🏨 Hotel Management System

نظام إدارة فندق متكامل باستخدام **Java + Oracle Database 11g**  
المشروع يشمل إدارة الغرف، العملاء، الفواتير، المناسبات، القاعات، مع **Backup + Triggers + Logs** لحفظ كل العمليات.

---

## 📌 المتطلبات
1. **Java JDK 8 أو أعلى**
2. **NetBeans IDE** (أو أي IDE آخر يدعم Java Swing)
3. **Oracle Database 11g Express Edition (XE)**

---

## ⚡ تثبيت قاعدة البيانات Oracle 11g
1. نزّل **Oracle Database 11g XE** من الموقع الرسمي:  
   🔗 [تحميل Oracle 11g XE](https://www.oracle.com/database/technologies/appdev/xe.html)

2. أثناء التثبيت:
   - **Username (Schema):** `FLORY`  
   - **Password:** `FLORY`  

---

## 🗄️ إعداد قاعدة البيانات
بعد تثبيت Oracle افتح **SQL Developer** أو **SQL*Plus** وسجّل الدخول:

```sql
#  انشاء الجداول
في Netbeans الجداول تم انشاءها للتاكيد اضغط ران لكل جدول


#اضافه الكونسترينت (Constraints)
ALTER TABLE halls ADD CONSTRAINT unique_hall_name UNIQUE (hall_name);
ALTER TABLE halls ADD CONSTRAINT check_min_price CHECK (price_per_hour >= 100);

ALTER TABLE customer ADD CONSTRAINT CK_MOBILENUMBER CHECK(LENGTH(MOBILENUMBER)=11);
ALTER TABLE room ADD CONSTRAINT CK_price CHECK (PRICE >=300);


# انشاء السكوينس(sequnce)
CREATE SEQUENCE event_id_sequnc
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;



# انشاء الباكاب(Backap)
وهو نسخه من البينات علي كل جدول لو تم حذف البيانات لرجوعها
CREATE TABLE room_backup AS SELECT * FROM room WHERE 1=2;
CREATE TABLE customer_backup AS SELECT * FROM customer WHERE 1=2;
CREATE TABLE halls_backup AS SELECT * FROM halls WHERE 1=2;



#انشاء التريجر (Triggers)
لكل جدول
CREATE OR REPLACE TRIGGER trg_log_halls
AFTER INSERT OR UPDATE OR DELETE ON halls
FOR EACH ROW
DECLARE
    v_user_email VARCHAR2(200);
    v_time VARCHAR2(100);
    v_op VARCHAR2(10);
BEGIN
    v_user_email := SYS_CONTEXT('USERENV', 'CLIENT_IDENTIFIER');

    IF INSERTING THEN
        v_op := 'INSERT';
    ELSIF UPDATING THEN
        v_op := 'UPDATE';
    ELSIF DELETING THEN
        v_op := 'DELETE';

        -- Backup عند الحذف
        INSERT INTO halls_backup VALUES (
            :OLD.id, :OLD.hall_name, :OLD.price_per_hour, :OLD.status
        );
    END IF;

    v_time := TO_CHAR(SYSDATE, 'DD-MON-YYYY HH24:MI:SS');

    INSERT INTO user_action_log (id, user_email, table_name, operation_type, action_time)
    VALUES (user_action_seqer.NEXTVAL, v_user_email, 'halls', v_op, v_time);
END;




# استعلامات مهمه (Select )
-- عرض السجلات بالترتيب
SELECT * FROM room ORDER BY roomno ASC;

-- عرض جدول الباكاب
SELECT * FROM customer_backup;

-- عرض سجل العمليات
SELECT * FROM user_action_log;





---

ت أوامر **Insert Demo Data** بحيث اللي يجرب يلاقي بيانات جاهزة بدل ما يبدأ من الصفر؟
باسم   (hotel_db.sql)

افتح SQL Plus أو SQL Developer وسجّل دخولك كـ FLORY/FLORY.
نفذ الملف ده الhotel_db.sql

فتح المشروع من NetBeans.

اتأكد إن كلاس الاتصال مضبوط:
دوس Run.

هتلاقي البيانات التجريبية جاهزة قدامك من أول تشغيل 🎉
⚡ معلومة: لو عايز تبدأ بقاعدة بيانات فاضية من غير Demo Data → متشغلش hotel_db.sql















username: FLORY
password: FLORY
