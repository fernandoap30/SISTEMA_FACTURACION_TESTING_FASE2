package service;

import dao.ClienteDAO;
import dao.UserDAO;
import dao.VendedorDAO;
import model.Admin;
import model.Cliente;
import model.User;
import model.Vendedor;

public class AuthService {
    private UserDAO userDAO = new UserDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private VendedorDAO vendedorDAO = new VendedorDAO();

    public User login(String username, String password) {
        String role = userDAO.findRoleByUsername(username);
        if (role != null) {
            int id = userDAO.findIdByUsernameAndRole(username, role);
            switch (role) {
                case "admin":

                    String storedPasswordAdmin = userDAO.findPasswordByUsernameAndRole(username, role);
                    if (storedPasswordAdmin != null && storedPasswordAdmin.equals(password)) {
                        return new Admin(id, username, password);
                    }
                    break;
                case "vendedor":
                    Vendedor vendedor = vendedorDAO.findByUsernameAndPassword(username, password);
                    if (vendedor != null) {
                        return vendedor;
                    }
                    break;
                case "cliente":
                    Cliente cliente = clienteDAO.findByUsernameAndPassword(username, password);
                    if (cliente != null) {
                        return cliente;
                    }
                    break;
            }
        }
        return null;
    }
}