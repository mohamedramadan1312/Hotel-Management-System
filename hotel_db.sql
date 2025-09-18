-- إنشاء الجداول الأساسية
CREATE TABLE room (
  roomno NUMBER PRIMARY KEY,
  roomType VARCHAR2(50),
  price NUMBER,
  status VARCHAR2(20)
);

CREATE TABLE customer (
  id NUMBER PRIMARY KEY,
  name VARCHAR2(100),
  mobileNumber VARCHAR2(20),
  email VARCHAR2(100) UNIQUE
);

CREATE TABLE halls (
  id NUMBER PRIMARY KEY,
  hall_name VARCHAR2(100),
  price_per_hour NUMBER,
  status VARCHAR2(20)
);

CREATE TABLE feedback (
  id NUMBER PRIMARY KEY,
  user_email VARCHAR2(200) UNIQUE,
  message VARCHAR2(500)
);

CREATE TABLE userss (
  id NUMBER PRIMARY KEY,
  username VARCHAR2(100),
  email VARCHAR2(200) UNIQUE,
  password VARCHAR2(100)
);

CREATE TABLE user_action_log (
  id NUMBER PRIMARY KEY,
  user_email VARCHAR2(200),
  table_name VARCHAR2(100),
  operation_type VARCHAR2(50),
  action_time VARCHAR2(50)
);

-- Backup Tables
CREATE TABLE room_backup AS SELECT * FROM room WHERE 1=2;
CREATE TABLE customer_backup AS SELECT * FROM customer WHERE 1=2;
CREATE TABLE halls_backup AS SELECT * FROM halls WHERE 1=2;

-- Sequences
CREATE SEQUENCE user_action_seqer START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE event_id_sequnc START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- Triggers
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
/

-- قيود إضافية
ALTER TABLE halls ADD CONSTRAINT unique_hall_name UNIQUE (hall_name);
ALTER TABLE halls ADD CONSTRAINT check_min_price CHECK (price_per_hour >= 100);
ALTER TABLE halls ADD CONSTRAINT check_price_per_hour CHECK (price_per_hour BETWEEN 100 AND 10000);

ALTER TABLE customer ADD CONSTRAINT CK_MOBILENUMBER CHECK(LENGTH(MOBILENUMBER)=11);
ALTER TABLE room ADD CONSTRAINT CK_price CHECK (PRICE >= 300);

ALTER TABLE customer ADD CONSTRAINT pk_customer_id PRIMARY KEY (id);
ALTER TABLE room ADD CONSTRAINT pk_room_id PRIMARY KEY (roomno);
ALTER TABLE userss ADD CONSTRAINT uq_email UNIQUE (email);

-- بيانات تجريبية
INSERT INTO room (roomno, roomType, price, status) VALUES (101, 'Single', 300, 'Available');
INSERT INTO room (roomno, roomType, price, status) VALUES (102, 'Double', 500, 'Occupied');
INSERT INTO room (roomno, roomType, price, status) VALUES (103, 'Suite', 1000, 'Available');

INSERT INTO customer (id, name, mobileNumber, email) VALUES (1, 'Ahmed Ali', '01012345678', 'ahmed@example.com');
INSERT INTO customer (id, name, mobileNumber, email) VALUES (2, 'Sara Mohamed', '01087654321', 'sara@example.com');

INSERT INTO halls (id, hall_name, price_per_hour, status) VALUES (1, 'Royal Hall', 2000, 'Available');
INSERT INTO halls (id, hall_name, price_per_hour, status) VALUES (2, 'Classic Hall', 1500, 'Booked');

INSERT INTO userss (id, username, email, password) VALUES (1, 'admin', 'admin@hotel.com', 'admin123');
INSERT INTO userss (id, username, email, password) VALUES (2, 'staff1', 'staff@hotel.com', 'staff123');

INSERT INTO feedback (id, user_email, message) VALUES (1, 'ahmed@example.com', 'Great service!');
INSERT INTO feedback (id, user_email, message) VALUES (2, 'sara@example.com', 'Room was very clean.');
