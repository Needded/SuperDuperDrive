CREATE TABLE IF NOT EXISTS USERS (
  userid SERIAL PRIMARY KEY,
  username VARCHAR(20),
  salt VARCHAR,
  password VARCHAR,
  firstname VARCHAR(20),
  lastname VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS NOTES (
    noteid SERIAL PRIMARY KEY,
    notetitle VARCHAR(20),
    notedescription VARCHAR (1000),
    userid INT,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS FILES (
    fileid SERIAL PRIMARY KEY,
    filename VARCHAR,
    contenttype VARCHAR,
    filesize VARCHAR,
    userid INT,
    filedata BYTEA,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS CREDENTIALS (
    credentialid SERIAL PRIMARY KEY,
    url VARCHAR(100),
    username VARCHAR (30),
    key VARCHAR,
    password VARCHAR,
    userid INT,
    foreign key (userid) references USERS(userid)
);