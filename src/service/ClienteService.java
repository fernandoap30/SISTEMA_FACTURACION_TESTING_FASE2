package service;

import dao.ClienteDAO;
import dao.UserDAO;

public class ClienteService {
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final UserDAO userDAO = new UserDAO();

    public boolean crearCliente(String username, String password) {
        if (userDAO.findRoleByUsername(username) == null) {

            if (password == null || password.trim().isEmpty()) {
                System.out.println("Error: La contraseña no puede estar vacía.");
                return false; // No se crea el cliente si la contraseña está vacía
            }

            if (userDAO.createUser(username, "cliente")) {
                return clienteDAO.crearCliente(username, password) > 0;
            }
        }
        return false; // Usuario ya existe o error al crear cliente
    }
}