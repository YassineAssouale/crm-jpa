CREATE TABLE customers(id INT auto_increment NOT NULL, 
								lastname VARCHAR(100), 
                                firstname VARCHAR(100),
                                company VARCHAR(200),
                                mail VARCHAR(255),
                                phone VARCHAR(15),
                                mobile VARCHAR(15),
                                notes VARCHAR(255),
                                active BOOLEAN,
                                PRIMARY KEY (id));
                                    
CREATE TABLE orders(id INT auto_increment NOT NULL,
									customer_id INT,
                                    label VARCHAR(100),
                                    adr_et DECIMAL,
                                    number_of_days DECIMAL,
                                    tva DECIMAL,
                                    status VARCHAR(30),
                                    type VARCHAR(100),
                                    notes VARCHAR(255),
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (customer_id) REFERENCES customers(id));                                    

CREATE TABLE users (id INT auto_increment NOT NULL,
									   username VARCHAR(30),
                                       password VARCHAR(255),
                                       mail VARCHAR(255),
                                       PRIMARY KEY(id));