package service;

import dao.UserDAO;
import dao.VendedorDAO;

public class VendedorService {
    private final VendedorDAO vendedorDAO = new VendedorDAO();
    private final UserDAO userDAO = new UserDAO();

    public boolean crearVendedor(String username, String password) {
        if (userDAO.findRoleByUsername(username) == null) {
            if (userDAO.createUser(username, "vendedor")) { // Primero crear en users
                return vendedorDAO.crearVendedor(username, password) > 0; // Luego crear el vendedor específico
            }
        }
        return false; // Usuario ya existe o error al crear vendedor
    }
}
