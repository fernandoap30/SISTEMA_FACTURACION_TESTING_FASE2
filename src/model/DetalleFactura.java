package model;

public class DetalleFactura {
    private int id;
    private int facturaId;
    private int productoId;
    private int cantidad;
    private double subtotal;

    public DetalleFactura(int id, int facturaId, int productoId, int cantidad, double subtotal) {
        this.id = id;
        this.facturaId = facturaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public int getProductoId() {
        return productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}