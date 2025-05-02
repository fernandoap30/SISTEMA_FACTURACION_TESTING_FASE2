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
            String sql = "SELECT id, producto_id, cantidad, subtotal FROM detalle_factura WHERE factura_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, facturaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                detalles.add(new DetalleFactura(
                        rs.getInt("id"),
                        facturaId,
                        rs.getInt("producto_id"),
                        rs.getInt("cantidad"),
                        rs.getDouble("subtotal")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalles;
    }
}