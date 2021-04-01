PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "users" (
user_id INTEGER PRIMARY KEY,
username TEXT NOT NULL,
password TEXT NOT NULL,
create_date DATE DEFAULT (strftime('%s', 'now'))
, last_login, email_address TEXT default NULL);
INSERT INTO users VALUES(1,'testuser','testpass',1612895671000,1612895671000,'test@test.com');
INSERT INTO users VALUES(2,'FlareFlo','Duccus Longus',1612895671000,1612895671000,'FlareFlo@duck.quack');
INSERT INTO users VALUES(3,'Unixcorn','Btw i use Arch',1612895671000,1612895671000,'Unixcorn@i-use-arch.com');
INSERT INTO users VALUES(4,'flamion','Ur Gay',1612895671000,1612895671000,'flamion@protonmail.com');
INSERT INTO users VALUES(5,'samantharose','horny',1616615755268,1616615755268,'yes@yes.horny');
INSERT INTO users VALUES(6,'Sirgottlos','stift',1616788519750,1616788519750,'yes@yes.no');
INSERT INTO users VALUES(7,'testuser','testus',1616929148981,1616929148981,'test@test.com');
INSERT INTO users VALUES(8,'samantharose','horny',1616947696980,1616947696980,'yes@yes.horny');
INSERT INTO users VALUES(9,'FlareFlo','yes',1617128499495,1617128499495,'FlareFlo@duck.quack');
INSERT INTO users VALUES(10,'FlareFlo','yes',1617128500515,1617128500515,'FlareFlo@duck.quack');
INSERT INTO users VALUES(11,'FlareFlo','yes',1617128502422,1617128502422,'FlareFlo@duck.quack');
INSERT INTO users VALUES(12,'FlareFlo','yes',1617128504309,1617128504309,'FlareFlo@duck.quack');
INSERT INTO users VALUES(13,'FlareFlo','yes',1617128504858,1617128504858,'FlareFlo@duck.quack');
INSERT INTO users VALUES(14,'FlareFlo','yes',1617128505250,1617128505250,'FlareFlo@duck.quack');
INSERT INTO users VALUES(15,'FlareFlo','yes',1617128505664,1617128505664,'FlareFlo@duck.quack');
INSERT INTO users VALUES(16,'FlareFlo','yes',1617128506075,1617128506075,'FlareFlo@duck.quack');
CREATE TABLE entry
(
    entry_id    INTEGER PRIMARY KEY,
    creator     INTEGER NOT NULL,
    create_date INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    due_date    INTEGER DEFAULT (strftime('%s', 'now', '+1 days')),
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL DEFAULT '',
    FOREIGN KEY (creator) REFERENCES "users" (user_id)
);
INSERT INTO entry VALUES(1,1,1612650686000,1612737086000,'Test Title','Test Description');
INSERT INTO entry VALUES(2,4,1612650686000,1612737086000,'Password Storing','Hash the passwords instead of storing in plaintext');
INSERT INTO entry VALUES(3,4,1612650686000,1612737086000,'Password Storing','Salt the Password before Hashing');
INSERT INTO entry VALUES(4,4,1612650686000,1612737086000,'Password Storing','Create new table to store the password salts');
INSERT INTO entry VALUES(5,2,1612650686000,1612737086000,'Duck Things','Stealing some Bread');
INSERT INTO entry VALUES(6,2,1612650686000,1612737086000,'Duck Things','Eating some Bread');
INSERT INTO entry VALUES(7,3,1612650686000,1612737086000,'Btw.','I use Arch');
INSERT INTO entry VALUES(8,4,1617108943303,1612737086069,'Haha, attacked twice','Create new table to store the password salts');
INSERT INTO entry VALUES(9,4,1617108944464,1612737086069,'Haha, attacked twice','Create new table to store the password salts');
CREATE TABLE groups (
group_id INTEGER PRIMARY KEY,
group_name TEXT NOT NULL,
creator INTEGER NOT NULL,
create_date INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
last_access_date INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
FOREIGN KEY(creator) REFERENCES "users"(user_id)
);
INSERT INTO "groups" VALUES(1,'Test Group',2,1615141669067,1615141669067);
CREATE TABLE in_group
(
user_id INTEGER NOT NULL,
group_id INTEGER NOT NULL,
FOREIGN KEY (user_id) REFERENCES "users" (user_id),
FOREIGN KEY (group_id) REFERENCES groups (group_id)

);
CREATE TABLE admin_in
(
user_id INTEGER NOT NULL,
group_id INTEGER NOT NULL,
FOREIGN KEY (user_id) REFERENCES "users" (user_id),
FOREIGN KEY (group_id) REFERENCES groups (group_id)

);
COMMIT;
