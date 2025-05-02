package service;

import dao.FacturaDAO;
import dao.DetalleFacturaDAO;
import model.Factura;
import model.DetalleFactura;

import java.util.List;

public class InvoiceService {
    private FacturaDAO facturaDAO = new FacturaDAO();
    private DetalleFacturaDAO detalleFacturaDAO = new DetalleFacturaDAO();

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

    public Factura getInvoiceDetails(int invoiceId) {
        Factura factura = facturaDAO.obtenerFacturaPorId(invoiceId);
        if (factura != null) {
            // Puedes agregar aquí la lógica para obtener los detalles de la factura si es necesario en el futuro.
            // Por ahora, solo devolvemos la información básica de la factura.
            return factura;
        }
        return null;
    }

    public List<DetalleFactura> getInvoiceItems(int invoiceId) {
        return detalleFacturaDAO.obtenerDetallesPorFactura(invoiceId);
    }
}