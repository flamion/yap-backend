PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE user (
user_id INTEGER PRIMARY KEY,
username TEXT NOT NULL,
password TEXT NOT NULL,
create_date DATE DEFAULT (strftime('%s', 'now'))
, last_login, email_address TEXT default NULL);
INSERT INTO user VALUES(1,'testuser','testpass',1612895671,'1612895671','test@test.com');
INSERT INTO user VALUES(2,'FlareFlo','Duccus Longus',1612895671,'1612895671','FlareFlo@duck.quack');
INSERT INTO user VALUES(3,'Unixcorn','Btw i use Arch',1612895671,'1612895671','Unixcorn@i-use-arch.com');
INSERT INTO user VALUES(4,'flamion','Ur Gay',1612895671,'1612895671','flamion@protonmail.com');
CREATE TABLE entry
(
    entry_id    INTEGER PRIMARY KEY,
    creator     INTEGER NOT NULL,
    create_date INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    due_date    INTEGER DEFAULT (strftime('%s', 'now', '+1 days')),
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL DEFAULT '',
    FOREIGN KEY (creator) REFERENCES user (user_id)
);
INSERT INTO entry VALUES(1,1,1612650686,1612737086,'Test Title','Test Description');
INSERT INTO entry VALUES(2,4,1612650686,1612737086,'Password Storing','Hash the passwords instead of storing in plaintext');
INSERT INTO entry VALUES(3,4,1612650686,1612737086,'Password Storing','Salt the Password before Hashing');
INSERT INTO entry VALUES(4,4,1612650686,1612737086,'Password Storing','Create new table to store the password salts');
INSERT INTO entry VALUES(5,2,1612650686,1612737086,'Duck Things','Stealing some Bread');
INSERT INTO entry VALUES(6,2,1612650686,1612737086,'Duck Things','Eating some Bread');
INSERT INTO entry VALUES(7,3,1612650686,1612737086,'Btw.','I use Arch');
CREATE TABLE groups (
group_id INTEGER PRIMARY KEY,
group_name TEXT NOT NULL,
creator INTEGER NOT NULL,
create_date INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
last_access_date INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
FOREIGN KEY(creator) REFERENCES user(user_id)
);
CREATE TABLE in_group
(
user_id INTEGER NOT NULL,
group_id INTEGER NOT NULL,
FOREIGN KEY (user_id) REFERENCES user (user_id),
FOREIGN KEY (group_id) REFERENCES groups (group_id)

);
CREATE TABLE admin_in
(
user_id INTEGER NOT NULL,
group_id INTEGER NOT NULL,
FOREIGN KEY (user_id) REFERENCES user (user_id),
FOREIGN KEY (group_id) REFERENCES groups (group_id)

);
COMMIT;
