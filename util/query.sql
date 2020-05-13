show databases;
create database scott;
use scott;

DROP TABLE consumer;
DROP TABLE product;
DROP TABLE cart;


DROP TABLE consumer;
CREATE TABLE consumer (
    id INT not null primary key,
    name VARCHAR(45),
    address VARCHAR(45),
    pass VARCHAR(45)
);
DESC consumer;

DROP TABLE product;
CREATE TABLE product(
	product_num INT auto_increment not null primary key,
    product_name VARCHAR(45),
    quantity INT,
    size INT
);
DESC product;

DROP TABLE cart;
CREATE TABLE cart (
    consumer_id INT,
    product_num INT,
    order_num INT auto_increment not null primary key,
    quantity INT,
    order_status TINYINT,
    ship_status TINYINT
);
DESC cart;
ALTER TABLE cart ADD CONSTRAINT consumer_id FOREIGN KEY(consumer_id) REFERENCES consumer(id);
ALTER TABLE cart ADD CONSTRAINT product_num FOREIGN KEY(product_num) REFERENCES product(product_num);

INSERT INTO consumer (id, name, address, pass) VALUES (9509212, "ice", "Sillim", "abc123");
INSERT INTO consumer (id, name, address, pass) VALUES (9405131, "jjy", "ganseo", "abc124");
INSERT INTO consumer (id, name, address, pass) VALUES (9106012, "ksh", "Seoul", "abc125");
INSERT INTO consumer (id, name, address, pass) VALUES (9210232, "kmk", "Korea", "abc126");
SELECT * FROM consumer;

INSERT INTO product (product_name, quantity, size) VALUES ("mask1", 1000, 1);
INSERT INTO product (product_name, quantity, size) VALUES ("mask2", 150, 2);
INSERT INTO product (product_name, quantity, size) VALUES ("mask3", 170, 3);
SELECT * FROM product;

INSERT INTO cart (consumer_id, product_num, quantity) VALUES (9509212, 3, 100);
SELECT * FROM cart c, product p WHERE c.product_num = p.product_num;





/*
		Cart cart1=new Cart(20200513, 9509212 , 20200513, 3, 1, 0);
		Cart cart2=new Cart(20200511, 9210232 , 20200512, 1, 0, 0);
*/