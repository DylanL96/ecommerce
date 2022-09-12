CREATE TABLE GROCERYLIST(
    groceryID LONG PRIMARY KEY AUTO_INCREMENT,
    groceryName VARCHAR(255),
    groceryCategory VARCHAR(255),
    groceryPrice DOUBLE,
    groceryQuantity INT
);


CREATE TABLE CART(
	groceryID LONG,
	groceryName VARCHAR(255),
	groceryCategory VARCHAR(255),
	groceryPrice DOUBLE,
	groceryQuantity INT DEFAULT 0
);

CREATE TABLE PREVIOUSCART(
	sessionToken VARCHAR(255),
	groceryName	 VARCHAR(255),
	groceryCategory	VARCHAR(255),
	groceryPrice DOUBLE,
	groceryQuantity INT
);

