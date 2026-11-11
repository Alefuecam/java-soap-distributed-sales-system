connect 'jdbc:derby://localhost:1527/bd_parcial_02_pedidos;user=app;password=app';

DROP TABLE ordered_product;
DROP TABLE customer_order;
DROP TABLE product;
DROP TABLE category;
DROP TABLE customer;

------------------------------- 
--     CUSTOMER
-------------------------------
CREATE  TABLE  customer 
(
  id INT  NOT NULL GENERATED ALWAYS AS IDENTITY 
     (START WITH 1 ,INCREMENT BY 1) 
     CONSTRAINT CUSTOMER_PK PRIMARY KEY,
  name VARCHAR(45) NOT NULL ,
  email VARCHAR(45) NOT NULL ,
  phone VARCHAR(45) NOT NULL ,
  address VARCHAR(45) NOT NULL ,
  city_region VARCHAR(2) NOT NULL ,
  cc_number VARCHAR(19) NOT NULL
  );

INSERT INTO customer (name,email,phone,address,city_region,cc_number) VALUES
('Alejandro Fuentes','alex.fuentes@itam.com','5592887267','Rio Hondo 10','AO','08390'),
('Axel Davila','axel.davila@itam.com','5510293747','Alvaro Obregon 27','AO','08837'),
('Isaac Pimentel','isaac.pimentel@itam.com','5524368403','Bucareli 321','AO','02442'),
('Brayan Chavez','brayan.chavez@itam.com','5537987726','Chapultepec 234','AO','06733');

------------------------------- 
--     CUSTOMER_ORDER
-------------------------------
CREATE  TABLE  customer_order 
(
  id INT  NOT NULL GENERATED ALWAYS AS IDENTITY 
     (START WITH 1 ,INCREMENT BY 1) 
     CONSTRAINT CUSTOMER_ORDER_PK PRIMARY KEY,
  amount DECIMAL(6,2) NOT NULL ,
  date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  confirmation_number INT  NOT NULL ,
  customer_id INT  NOT NULL CONSTRAINT CUST_ORD_FK REFERENCES customer
);
------------------------------- 
--     CATEGORY
-------------------------------
CREATE  TABLE  category 
(
  id INT  NOT NULL GENERATED ALWAYS AS IDENTITY 
     (START WITH 1 ,INCREMENT BY 1) 
     CONSTRAINT CATEGORY_PK PRIMARY KEY,
  name VARCHAR(45) NOT NULL 
);

INSERT INTO category (name) VALUES 
('petreo'),
('ceramicos'),
('aglutinantes'),
('metalicos');

------------------------------- 
--     PRODUCTS
-------------------------------

CREATE  TABLE  product 
(
  id INT  NOT NULL GENERATED ALWAYS AS IDENTITY 
     (START WITH 1 ,INCREMENT BY 1), 
  name VARCHAR(45) NOT NULL ,
  price DECIMAL(5,2) NOT NULL ,
  description VARCHAR(50),
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  category_id INT  NOT NULL ,
  existencia  INT NOT NULL,
  PRIMARY KEY (id) ,
  CONSTRAINT fk_product_category
    FOREIGN KEY (category_id )
    REFERENCES category (id )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

INSERT INTO product (name,price,description,last_update,category_id,existencia) VALUES
('granito', 2.60, 'mezcla cuarzo',TIMESTAMP('2020-10-06 09:00:00'), 1,0),
('marmol', 1.39, 'piedra caliza',TIMESTAMP('2020-10-06 09:00:00'), 1,1000),
('ladrillo', 1.09, 'forma ortoedrica', TIMESTAMP('2020-10-06 09:00:00'),1,1000),
('cemento', 1.26, 'cohesionar y fijar', TIMESTAMP('2020-10-06 09:00:00'),1,0),
('yeso', 1.89, 'piedra de aljez', TIMESTAMP('2020-10-06 09:00:00'),2,1000),
('hormigon', 2.79, 'grava y piedra', TIMESTAMP('2020-10-06 09:00:00'),2,1000),
('aluminio', 3.29, 'metal ligero', TIMESTAMP('2020-10-06 09:00:00'),2,1000),
('cobre', 2.95, 'resistencia a corrosion', TIMESTAMP('2020-10-06 09:00:00'),2,1000),
('acero', 1.39, 'hierro y carbono', TIMESTAMP('2020-10-06 09:00:00'),3,0),
('laton', 2.29, 'cobre y zinc', TIMESTAMP('2020-10-06 09:00:00'),3,1000),
('titanio', 1.29, 'alta resistencia', TIMESTAMP('2020-10-06 09:00:00'),3,0),
('pvc', 1.09, 'plastico termoplastico', TIMESTAMP('2020-10-06 09:00:00'),3,1000),
('poliuretano', 2.05, 'polimero de bases hidroxilicas', TIMESTAMP('2020-10-06 09:00:00'),4,1000),
('poliestireno', 1.89, 'compuesto derivado del petroleo', TIMESTAMP('2020-10-06 09:00:00'),4,1000),
('caoba', 1.79, 'madera de lujo', TIMESTAMP('2020-10-06 09:00:00'),4,1000),
('roble', 2.29, 'madera dura y compacta', TIMESTAMP('2020-10-06 09:00:00'),4,0);

------------------------------- 
--     ORDERED_PRODUCTS
-------------------------------

CREATE  TABLE ordered_product
 (
  customer_order_id INT  NOT NULL, 
  product_id INT  NOT NULL ,
  quantity SMALLINT  NOT NULL DEFAULT 1 ,
  PRIMARY KEY (customer_order_id, product_id) ,
  CONSTRAINT fk_ordered_product_customer_order
    FOREIGN KEY (customer_order_id )
    REFERENCES customer_order (id )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_ordered_product_product
    FOREIGN KEY (product_id )
    REFERENCES product (id )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

disconnect;
