
CREATE DATABASE sistema_facturacion;
USE sistema_facturacion;
-- Tabla de usuarios para almacenar roles
CREATE TABLE users (
    username VARCHAR(50) NOT NULL UNIQUE,
    role ENUM('admin', 'vendedor', 'cliente') NOT NULL,
    PRIMARY KEY (username)
);

-- Tabla de administradores
CREATE TABLE admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username)
);

-- Tabla de vendedores
CREATE TABLE vendedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username)
);

-- Tabla de clientes
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username)
);

-- Tabla de productos
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL
);

-- Tabla de facturas
CREATE TABLE facturas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vendedor_id INT NOT NULL,
    cliente_id INT NOT NULL,
    fecha DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (vendedor_id) REFERENCES vendedores(id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- Detalles de factura
CREATE TABLE detalle_factura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    factura_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES facturas(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- Insertar algunos usuarios y roles de ejemplo
INSERT INTO users (username, role) VALUES
('admin1', 'admin'),
('vendedor1', 'vendedor'),
('cliente1', 'cliente');

-- Insertar detalles en las tablas de roles
INSERT INTO admins (username, password) VALUES ('admin1', 'admin123');
INSERT INTO vendedores (username, password) VALUES ('vendedor1', 'vendedor456');
INSERT INTO clientes (username, password) VALUES ('cliente1', 'cliente789');

-- Insertar algunos productos de ejemplo
INSERT INTO productos (nombre, precio) VALUES
('Laptop', 1200.00),
('Mouse', 25.00),
('Teclado', 75.00);

select * from users;
select * from admins;
select * from vendedores;
select * from clientes;
select * from productos;
select * from facturas;
select * from detalle_factura;