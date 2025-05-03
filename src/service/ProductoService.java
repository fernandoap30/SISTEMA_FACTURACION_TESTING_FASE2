package service;

import dao.ProductoDAO;
import model.Producto;

public class ProductoService {
    private final ProductoDAO productoDAO = new ProductoDAO();

    public boolean crearProducto(String nombre, double precio, int cantidad) {
        return productoDAO.crearProducto(nombre, precio, cantidad) > 0;
    }
    public Producto obtenerProductoPorId(int id) {
        return productoDAO.findById(id);
    }

    public boolean actualizarCantidadProducto(int productoId, int nuevaCantidad) {
        return productoDAO.actualizarCantidad(productoId, nuevaCantidad);
    }
}
