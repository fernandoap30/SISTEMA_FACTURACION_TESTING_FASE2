package service;

import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.DetalleFacturaDAO;
import dao.VendedorDAO;
import model.Cliente;
import model.Factura;
import model.DetalleFactura;
import model.Vendedor;

import java.time.LocalDate;
import java.util.List;

public class InvoiceService {
    private static FacturaDAO facturaDAO = new FacturaDAO();
    private static DetalleFacturaDAO detalleFacturaDAO = new DetalleFacturaDAO();

    public void createInvoice(int sellerId, int clientId, List<DetalleFactura> detalles, double total) {
        facturaDAO.crearFactura(sellerId, clientId, total, detalles);
    }

    public List<Factura> getInvoicesForAdmin() {
        return facturaDAO.obtenerTodasLasFacturas();
    }

    public List<Factura> getInvoicesForSeller(int sellerId) {
        return facturaDAO.obtenerFacturasPorVendedor(sellerId);
    }

    public List<Factura> getInvoicesForClient(int clientId) {
        return facturaDAO.obtenerFacturasPorCliente(clientId);
    }

    public static Factura getInvoiceDetails(int invoiceId) {
        Factura factura = facturaDAO.obtenerFacturaPorId(invoiceId);
        if (factura != null) {
            return factura;
        }
        return null;
    }

    public static List<DetalleFactura> getInvoiceItems(int invoiceId) {
        return detalleFacturaDAO.obtenerDetallesPorFactura(invoiceId);
    }
    public boolean eliminarFactura(int facturaId) {
        return facturaDAO.eliminarFactura(facturaId);
    }

    public boolean actualizarFactura(int facturaId, int vendedorId, int clienteId, LocalDate fecha, double total) {
        return facturaDAO.actualizarFactura(facturaId, vendedorId, clienteId, fecha, total);
    }

    public static void printInvoiceDetails(int facturaId) {
        Factura factura = InvoiceService.getInvoiceDetails(facturaId);
        if (factura == null) {
            System.out.println("Error: No se encontró la factura con el ID proporcionado.");
            return;
        }

        // Obtener nombres de cliente y vendedor
        Cliente cliente = new ClienteDAO().findById(factura.getClienteId());
        Vendedor vendedor = new VendedorDAO().findById(factura.getVendedorId());

        System.out.println("\n--- Detalles de la Factura ID: " + factura.getId() + " ---");
        System.out.println("Vendedor: " + (vendedor != null ? vendedor.getUsername() : "N/A"));
        System.out.println("Cliente: " + (cliente != null ? cliente.getUsername() : "N/A"));
        System.out.println("Fecha: " + factura.getFecha());
        System.out.println("Monto Total: $" + factura.getTotal());
        System.out.println("\n--- Productos ---");

        List<DetalleFactura> detalles = InvoiceService.getInvoiceItems(facturaId);
        if (!detalles.isEmpty()) {
            System.out.println("ID\tNombre\t\tCantidad\tSubtotal");
            for (DetalleFactura detalle : detalles) {
                System.out.println(detalle.getId() + "\t" + detalle.getNombreProducto() + "\t\t" + detalle.getCantidad() + "\t\t$" + detalle.getSubtotal());
            }
        } else {
            System.out.println("No hay productos en esta factura.");
        }
        System.out.println("---------------------------\n");
    }
}