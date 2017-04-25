------------------------
-- Andrew Maguire and Joseph Patricelli
-- ajm221@pitt.edu / jdp70@pitt.edu
------------------------


------------------------
-- Users
------------------------
CREATE TABLE Users (
pk					NUMBER PRIMARY KEY NOT NULL,
name				VARCHAR2(32) NOT NULL,
email				VARCHAR2(50) NOT NULL UNIQUE,
dob					DATE NOT NULL,
timestamp_login		TIMESTAMP NOT NULL
);
------------------------


------------------------
-- Friendships
------------------------
CREATE TABLE Friendships (
pk					NUMBER PRIMARY KEY NOT NULL,
fk_user1			NUMBER,
fk_user2			NUMBER,
status				VARCHAR2(11) NOT NULL,
date_established	DATE NOT NULL,
CONSTRAINT FRIENDSHIPS_FK_USER1
	FOREIGN KEY (fk_user1) REFERENCES Users (pk)
	ON DELETE SET NULL,
CONSTRAINT FRIENDSHIPS_FK_USER2
	FOREIGN KEY (fk_user2) REFERENCES Users (pk)
	ON DELETE SET NULL,
CONSTRAINT FRIENSHIPS_UNIQUE_USERS 
	CHECK (fk_user1 != fk_user2)
);
------------------------


------------------------
-- Groups
------------------------
CREATE TABLE Groups (
pk					NUMBER PRIMARY KEY NOT NULL,
name				VARCHAR2(32) NOT NULL,
description			VARCHAR2(100) NOT NULL,
user_limit			NUMBER NOT NULL -- Range 1...50
);
------------------------


------------------------
-- GroupMemberships
------------------------
CREATE TABLE GroupMemberships (
pk					NUMBER PRIMARY KEY NOT NULL,
fk_groups			NUMBER NOT NULL,
fk_users			NUMBER NOT NULL,
CONSTRAINT GroupMemberships_FK_GROUPS
	FOREIGN KEY (fk_groups) REFERENCES Groups (pk),
CONSTRAINT GroupMemberships_FK_USERS
	FOREIGN KEY (fk_users) REFERENCES Users (pk),
CONSTRAINT GROUPMEMBERSHIPS_NODUPLICATES
	UNIQUE (fk_groups, fk_users)
);
------------------------


------------------------
-- UserMessages
------------------------
CREATE TABLE UserMessages (
pk					NUMBER PRIMARY KEY NOT NULL,
subject				VARCHAR2(32) NOT NULL,
body				VARCHAR2(100) NOT NULL,
sender				NUMBER,
recipient			NUMBER,
date_sent			DATE NOT NULL,
CONSTRAINT SENDER_FK_USERS
	FOREIGN KEY (sender) REFERENCES Users (pk)
	ON DELETE SET NULL,
CONSTRAINT RECIPIENT_FK_USERS
	FOREIGN KEY (recipient) REFERENCES Users (pk)
	ON DELETE SET NULL,
CONSTRAINT USERMESSAGES_UNIQUE_USERS 
	CHECK (sender != recipient)
);
------------------------


------------------------
-- GroupMembershipLimit Trigger
------------------------
CREATE OR REPLACE TRIGGER GroupMembershipLimit
	BEFORE INSERT ON GroupMemberships
FOR EACH ROW
DECLARE
	u_count NUMBER;
	u_lim NUMBER; 
BEGIN
	SELECT COUNT(*) INTO u_count 
	FROM GroupMemberships
	WHERE fk_groups = :new.fk_groups;

	SELECT user_limit INTO u_lim 
	FROM Groups 
	WHERE pk = :new.fk_groups;

	IF u_count > u_lim THEN
		Raise_Application_Error(-20000, 'Group Membership limit has been reached.');
	END IF;
END;	
/
------------------------


------------------------
-- OnUserDrop_GroupMemberships Trigger
------------------------
CREATE OR REPLACE TRIGGER OnUserDrop_GroupMemberships
	AFTER DELETE ON Users
FOR EACH ROW
BEGIN
	DELETE FROM GroupMemberships
	WHERE fk_users = :old.pk;
END;	
/
------------------------


------------------------
-- OnUserDrop_Friendships Trigger
------------------------
CREATE OR REPLACE TRIGGER OnUserDrop_Friendships
	AFTER DELETE ON Users
BEGIN
	DELETE FROM Friendships
	WHERE fk_user1 = NULL 
	OR fk_user2 = NULL;
END;	
/
------------------------


------------------------
-- OnUserDrop_UserMessages Trigger
------------------------
CREATE OR REPLACE TRIGGER OnUserDrop_UserMessages
	AFTER DELETE ON Users
BEGIN
	DELETE FROM UserMessages
	WHERE sender IS NULL 
	AND recipient IS NULL;
END;	
/
------------------------


COMMIT;