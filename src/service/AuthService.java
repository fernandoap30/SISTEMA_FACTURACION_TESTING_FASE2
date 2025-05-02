package service;

import dao.UserDAO;
import model.Admin;
import model.Cliente;
import model.User;
import model.Vendedor;

public class AuthService {
    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        String role = userDAO.findRoleByUsername(username);
        if (role != null) {
            String storedPassword = userDAO.findPasswordByUsernameAndRole(username, role);
            if (storedPassword != null && storedPassword.equals(password)) {
                int id = userDAO.findIdByUsernameAndRole(username, role);
                switch (role) {
                    case "admin":
                        return new Admin(id, username, password);
                    case "vendedor":
                        return new Vendedor(id, username, password);
                    case "cliente":
                        return new Cliente(id, username, password);
                }
            }
        }
        return null;
    }
}