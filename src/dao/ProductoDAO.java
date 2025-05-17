package dao;

import db.DatabaseConnection;
import model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class ProductoDAO {

    public int crearProducto(String nombre, double precio, int cantidad) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO productos (nombre, precio, cantidad) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nombre);
            stmt.setDouble(2, precio);
            stmt.setDouble(3, cantidad);
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

    public Producto findById(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id, nombre, precio, cantidad FROM productos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("cantidad"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizarCantidad(int productoId, int nuevaCantidad) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE productos SET cantidad = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nuevaCantidad);
            stmt.setInt(2, productoId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Producto findByNombre(String nombre) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id, nombre, precio, cantidad FROM productos WHERE LOWER(nombre) = LOWER(?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("cantidad"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
