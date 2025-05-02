package model;

import java.time.LocalDate;

public class Factura {
    private int id;
    private int vendedorId;
    private int clienteId;
    private LocalDate fecha;
    private double total;

    public Factura(int id, int vendedorId, int clienteId, LocalDate fecha, double total) {
        this.id = id;
        this.vendedorId = vendedorId;
        this.clienteId = clienteId;
        this.fecha = fecha;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public int getVendedorId() {
        return vendedorId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }
}