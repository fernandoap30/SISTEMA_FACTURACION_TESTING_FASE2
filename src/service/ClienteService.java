package service;

import dao.ClienteDAO;
import dao.UserDAO;

public class ClienteService {
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final UserDAO userDAO = new UserDAO();

    public boolean crearCliente(String username, String password) {
        if (userDAO.findRoleByUsername(username) == null) {
            if (userDAO.createUser(username, "cliente")) { // Primero crear en users
                return clienteDAO.crearCliente(username, password) > 0; // Luego crear el cliente específico
            }
        }
        return false; // Usuario ya existe o error al crear cliente
    }
}