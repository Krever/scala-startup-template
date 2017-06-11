
CREATE TABLE NOTEBOOKS (
  ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  NAME TEXT
);

CREATE TABLE NOTES (
  ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  TITLE TEXT,
  CONTENT TEXT,
  NOTEBOOK_ID INTEGER,
  FOREIGN KEY(NOTEBOOK_ID) REFERENCES NOTEBOOKS(ID)
);
