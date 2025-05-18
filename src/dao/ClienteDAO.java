package dao;

import db.DatabaseConnection;
import model.Cliente;
import model.Vendedor;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class ClienteDAO {

    public int crearCliente(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO clientes (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
            java.sql.ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public Cliente findById(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id, username, password FROM clientes WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cliente(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Cliente findByUsernameAndPassword(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id, username, password FROM clientes WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (BCrypt.checkpw(password, rs.getString("password"))) {
                    return new Cliente(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}