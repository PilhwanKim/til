CREATE TABLE Accounts (
    account_id      SERIAL PRIMARY KEY,
    account_name    VARCHAR(20),
    first_name      VARCHAR(20),
    last_name       VARCHAR(20),
    email           VARCHAR(100),
    password_hash   CHAR(64),
    portrait_image  BLOB,
    hourly_rate     NUMERIC(9,2)
);


# Master Table
CREATE TABLE BugStatus (
    status          VARCHAR(20) PRIMARY KEY,   
);

CREATE TABLE Bugs (
    bug_id          SERIAL PRIMARY KEY,
    date_reported   DATE NOT NULL,
    summary         VARCHAR(80),
    description     VARCHAR(1000),
    resolution      VARCHAR(1000),
    reported_by     BIGINT UNSIGNED NOT NULL,
    assigned_to     BIGINT UNSIGNED,
    verified_by     BIGINT UNSIGNED,
    status          VARCHAR(20) NOT NULL DEFAULT 'NEW',
    priority        VARCHAR(20),
    hours           NUMERIC(9,2),
    FOREIGN KEY (reported_by) REFERENCES Accounts(account_id),
    FOREIGN KEY (assigned_to) REFERENCES Accounts(account_id),
    FOREIGN KEY (verified_by) REFERENCES Accounts(account_id),
    FOREIGN KEY (status) REFERENCES BugStatus(status),
)

CREATE TABLE Comments (
    comment_id      SERIAL PRIMARY KEY,
    bug_id          BIGINT UNSIGNED NOT NULL,
    author          BIGINT UNSIGNED NOT NULL,
    comment_date    DATETIME NOT NULL,
    comment         TEXT NOT NULL,
    FOREIGN KEY (bug_id) REFERENCES Bugs(bug_id),
    FOREIGN KEY (author) REFERENCES Accounts(account_id),
)