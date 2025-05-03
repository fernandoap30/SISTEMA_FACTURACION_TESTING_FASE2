package service;

import dao.UserDAO;
import dao.VendedorDAO;

public class VendedorService {
    private final VendedorDAO vendedorDAO = new VendedorDAO();
    private final UserDAO userDAO = new UserDAO();

    public boolean crearVendedor(String username, String password) {
        if (userDAO.findRoleByUsername(username) == null) {
            if (userDAO.createUser(username, "vendedor")) {
                return vendedorDAO.crearVendedor(username, password) > 0;
            }
        }
        return false;
    }
}
