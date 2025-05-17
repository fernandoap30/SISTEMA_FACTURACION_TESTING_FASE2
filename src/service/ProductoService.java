package service;

import dao.ProductoDAO;
import model.Producto;

public class ProductoService {
    private final ProductoDAO productoDAO = new ProductoDAO();

    public boolean crearProducto(String nombre, double precio, int cantidad) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Error: El nombre del producto no puede estar vacío.");
            return false;
        }
        if (nombre.matches("\\d+")) {
            System.out.println("Error: El nombre del producto no puede contener solo números.");
            return false;
        }
        if (productoDAO.findByNombre(nombre) != null) {
            System.out.println("Error: Ya existe un producto con el nombre \"" + nombre + "\".");
            return false;
        }
        if (precio <= 0) {
            System.out.println("Error: El precio del producto debe ser mayor que cero.");
            return false;
        }
        if (cantidad < 1) {
            System.out.println("Error: La cantidad del producto no puede ser 0 ni negativo.");
            return false;
        }
        return productoDAO.crearProducto(nombre, precio, cantidad) > 0;
    }
    public Producto obtenerProductoPorId(int id) {
        return productoDAO.findById(id);
    }

    public boolean actualizarCantidadProducto(int productoId, int nuevaCantidad) {
        return productoDAO.actualizarCantidad(productoId, nuevaCantidad);
    }
}
