CREATE DATABASE IF NOT EXISTS internet_shop;

USE internet_shop;

CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT NOT NULL,
    product_name VARCHAR(25) NOT NULL,
    product_price DECIMAL(10 , 2 ) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS clients (
    id INT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(25) NOT NULL,
    middle_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT NOT NULL,
    order_date DATETIME NOT NULL,
    client_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id)
        REFERENCES clients (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (product_id)
        REFERENCES products (id)
        ON UPDATE CASCADE ON DELETE CASCADE
);