CREATE TABLE users (
    ID INT NOT NULL AUTO_INCREMENT,
    Username VARCHAR(255) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE friends (
    UserID INT,
    FriendID INT,
    FOREIGN KEY (UserID) REFERENCES users(ID),
    FOREIGN KEY (FriendID) REFERENCES users(ID),
    PRIMARY KEY (UserID, FriendID)
);


-- Dummy data
INSERT INTO users (Username, Password) VALUES 
('bob', 'bobpw'),
('joe', 'joepw');

INSERT INTO friends (UserID, FriendID) VALUES 
(1, 2),
(2, 1);