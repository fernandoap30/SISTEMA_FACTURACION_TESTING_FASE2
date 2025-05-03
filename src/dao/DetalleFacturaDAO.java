package dao;

import db.DatabaseConnection;
import model.DetalleFactura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleFacturaDAO {

    public List<DetalleFactura> obtenerDetallesPorFactura(int facturaId) {
        List<DetalleFactura> detalles = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT " +
                    "df.id, df.producto_id, df.cantidad, df.subtotal, " +
                    "p.nombre AS nombre_producto " +
                    "FROM detalle_factura df " +
                    "JOIN productos p ON df.producto_id = p.id " +
                    "WHERE df.factura_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, facturaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                detalles.add(new DetalleFactura(
                        rs.getInt("id"),
                        facturaId,
                        rs.getInt("producto_id"),
                        rs.getInt("cantidad"),
                        rs.getDouble("subtotal"),
                        rs.getString("nombre_producto")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalles;
    }
}