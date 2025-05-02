package dao;

import db.DatabaseConnection;
import model.User;

import java.sql.*;

public class UserDAO {

    public String findRoleByUsername(String username) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT role FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int findIdByUsernameAndRole(String username, String role) {
        String tableName = "";
        String idColumn = "";
        switch (role) {
            case "admin":
                tableName = "admins";
                idColumn = "id";
                break;
            case "vendedor":
                tableName = "vendedores";
                idColumn = "id";
                break;
            case "cliente":
                tableName = "clientes";
                idColumn = "id";
                break;
            default:
                return -1;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT " + idColumn + " FROM " + tableName + " WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(idColumn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String findPasswordByUsernameAndRole(String username, String role) {
        String tableName = "";
        String passwordColumn = "password";
        switch (role) {
            case "admin":
                tableName = "admins";
                break;
            case "vendedor":
                tableName = "vendedores";
                break;
            case "cliente":
                tableName = "clientes";
                break;
            default:
                return null;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT " + passwordColumn + " FROM " + tableName + " WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(passwordColumn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}