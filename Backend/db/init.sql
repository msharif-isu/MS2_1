CREATE TABLE users (
    ID INT PRIMARY KEY,
    Username VARCHAR(255) NOT NULL,
    Password VARCHAR(255) NOT NULL
);

CREATE TABLE friends (
    UserID INT,
    FriendID INT,
    FOREIGN KEY (UserID) REFERENCES users(ID),
    FOREIGN KEY (FriendID) REFERENCES users(ID),
    PRIMARY KEY (UserID, FriendID)
);


-- Dummy data
INSERT INTO users (ID, Username, Password) VALUES 
(1, 'bob', 'bobpw'),
(2, 'joe', 'joepw');

INSERT INTO friends (UserID, FriendID) VALUES 
(1, 2),
(2, 1);