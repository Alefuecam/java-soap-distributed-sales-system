connect 'jdbc:derby://localhost:1527/bd_parcial_02_credito;user=app;password=app';

DROP TABLE credito;

------------------------------- 
--     CREDITO
-------------------------------
CREATE  TABLE  credito 
(
  id INT  NOT NULL GENERATED ALWAYS AS IDENTITY 
     (START WITH 1 ,INCREMENT BY 1) 
     CONSTRAINT CUSTOMER_PK PRIMARY KEY,
  name        VARCHAR(45) NOT NULL ,
  email       VARCHAR(45) NOT NULL ,
  phone       VARCHAR(45) NOT NULL ,
  address     VARCHAR(45) NOT NULL ,
  city_region VARCHAR(2) NOT NULL ,
  cc_number   VARCHAR(19) NOT NULL,
  credito     DECIMAL(10,2) NOT NULL   
  );

INSERT INTO credito (name,email,phone,address,city_region,cc_number,credito) VALUES
('Alejandro Fuentes','alex.fuentes@itam.com','5592887267','Rio Hondo 10','AO','08390',1000.0),
('Axel Davila','axel.davila@itam.com','5510293747','Alvaro Obregon 27','AO','08837',1000.0),
('Isaac Pimentel','isaac.pimentel@itam.com','5524368403','Bucareli 321','AO','02442',1000.0),
('Brayan Chavez','brayan.chavez@itam.com','5537987726','Chapultepec 234','AO','06733',1000.0);



disconnect;
