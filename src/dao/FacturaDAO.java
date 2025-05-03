package dao;

import db.DatabaseConnection;
import model.Factura;
import model.DetalleFactura;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    public int crearFactura(int vendedorId, int clienteId, double total, List<DetalleFactura> detalles) {
        Connection conn = null;
        PreparedStatement stmtFactura = null;
        PreparedStatement stmtDetalle = null;
        ResultSet rs = null;
        int facturaId = -1;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlFactura = "INSERT INTO facturas (vendedor_id, cliente_id, fecha, total) VALUES (?, ?, ?, ?)";
            stmtFactura = conn.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);
            stmtFactura.setInt(1, vendedorId);
            stmtFactura.setInt(2, clienteId);
            stmtFactura.setDate(3, Date.valueOf(LocalDate.now()));
            stmtFactura.setDouble(4, total);
            stmtFactura.executeUpdate();

            rs = stmtFactura.getGeneratedKeys();
            if (rs.next()) {
                facturaId = rs.getInt(1);
                String sqlDetalle = "INSERT INTO detalle_factura (factura_id, producto_id, cantidad, subtotal) VALUES (?, ?, ?, ?)";
                stmtDetalle = conn.prepareStatement(sqlDetalle);
                for (DetalleFactura detalle : detalles) {
                    stmtDetalle.setInt(1, facturaId);
                    stmtDetalle.setInt(2, detalle.getProductoId());
                    stmtDetalle.setInt(3, detalle.getCantidad());
                    stmtDetalle.setDouble(4, detalle.getSubtotal());
                    stmtDetalle.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(rs, stmtFactura, null);
            DatabaseConnection.closeResources(null, stmtDetalle, conn);
        }
        return facturaId;
    }

    public List<Factura> obtenerFacturasPorCliente(int clienteId) {
        return getFacturas("WHERE cliente_id = " + clienteId);
    }

    public List<Factura> obtenerFacturasPorVendedor(int vendedorId) {
        return getFacturas("WHERE vendedor_id = " + vendedorId);
    }

    public List<Factura> obtenerTodasLasFacturas() {
        return getFacturas("");
    }

    public Factura obtenerFacturaPorId(int facturaId) {
        Factura factura = null;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM facturas WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, facturaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                factura = new Factura(
                        rs.getInt("id"),
                        rs.getInt("vendedor_id"),
                        rs.getInt("cliente_id"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getDouble("total")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factura;
    }

    private List<Factura> getFacturas(String condition) {
        List<Factura> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM facturas " + condition;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(new Factura(
                        rs.getInt("id"),
                        rs.getInt("vendedor_id"),
                        rs.getInt("cliente_id"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getDouble("total")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    public boolean eliminarFactura(int facturaId) {
        Connection conn = null;
        PreparedStatement stmtFactura = null;
        PreparedStatement stmtDetalle = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlDetalle = "DELETE FROM detalle_factura WHERE factura_id = ?";
            stmtDetalle = conn.prepareStatement(sqlDetalle);
            stmtDetalle.setInt(1, facturaId);
            stmtDetalle.executeUpdate();

            String sqlFactura = "DELETE FROM facturas WHERE id = ?";
            stmtFactura = conn.prepareStatement(sqlFactura);
            stmtFactura.setInt(1, facturaId);
            int affectedRows = stmtFactura.executeUpdate();

            conn.commit();
            return affectedRows > 0;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            DatabaseConnection.closeResources(null, stmtDetalle, null);
            DatabaseConnection.closeResources(null, stmtFactura, conn);
        }
    }
    public boolean actualizarFactura(int facturaId, int vendedorId, int clienteId, LocalDate fecha, double total) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE facturas SET vendedor_id = ?, cliente_id = ?, fecha = ?, total = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vendedorId);
            stmt.setInt(2, clienteId);
            stmt.setDate(3, Date.valueOf(fecha));
            stmt.setDouble(4, total);
            stmt.setInt(5, facturaId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}