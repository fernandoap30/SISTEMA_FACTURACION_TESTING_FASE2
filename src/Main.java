import model.User;
import model.Factura;
import model.DetalleFactura;
import model.Producto;
import service.AuthService;
import service.InvoiceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static AuthService authService = new AuthService();
    private static InvoiceService invoiceService = new InvoiceService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bienvenido al sistema de facturación");

        System.out.print("Usuario: ");
        String username = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        User user = authService.login(username, password);

        if (user != null) {
            System.out.println("¡Login exitoso! Rol: " + user.getClass().getSimpleName().toLowerCase());
            showMenu(user);
        } else {
            System.out.println("Credenciales inválidas");
        }
    }

    private static void showMenu(User user) {
        boolean running = true;
        while (running) {
            System.out.println("\nSeleccione una opción:");
            switch (user.getClass().getSimpleName().toLowerCase()) {
                case "admin":
                    System.out.println("1. Ver todas las facturas");
                    System.out.println("2. Salir");
                    int adminChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (adminChoice == 1) {
                        List<Factura> invoices = invoiceService.getInvoicesForAdmin();
                        printInvoices(invoices);
                    } else {
                        running = false;
                    }
                    break;

                case "vendedor":
                    System.out.println("1. Emitir factura");
                    System.out.println("2. Ver mis facturas");
                    System.out.println("3. Salir");
                    int sellerChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (sellerChoice == 1) {
                        emitirFactura(user.getId());
                    } else if (sellerChoice == 2) {
                        List<Factura> invoices = invoiceService.getInvoicesForSeller(user.getId());
                        printInvoices(invoices);
                    } else {
                        running = false;
                    }
                    break;

                case "cliente":
                    System.out.println("1. Ver mis facturas");
                    System.out.println("2. Salir");
                    int clientChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (clientChoice == 1) {
                        List<Factura> invoices = invoiceService.getInvoicesForClient(user.getId());
                        printInvoices(invoices);
                    } else {
                        running = false;
                    }
                    break;
            }
        }
    }

    private static void emitirFactura(int vendedorId) {
        System.out.print("ID del cliente: ");
        int clienteId = scanner.nextInt();
        List<DetalleFactura> detalles = new ArrayList<>();
        double totalFactura = 0;

        boolean agregarOtroProducto = true;
        while (agregarOtroProducto) {
            System.out.print("ID del producto: ");
            int productoId = scanner.nextInt();
            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();
            System.out.print("Subtotal para este producto: ");
            double subtotal = scanner.nextDouble(); // En un sistema real, esto se calcularía basado en el precio del producto
            detalles.add(new DetalleFactura(0, 0, productoId, cantidad, subtotal));
            totalFactura += subtotal;

            System.out.print("¿Agregar otro producto? (s/n): ");
            String respuesta = scanner.next();
            agregarOtroProducto = respuesta.equalsIgnoreCase("s");
        }
        scanner.nextLine(); // Consume newline

        invoiceService.createInvoice(vendedorId, clienteId, detalles, totalFactura);
        System.out.println("Factura emitida exitosamente");
    }

    private static void printInvoices(List<Factura> invoices) {
        if (invoices.isEmpty()) {
            System.out.println("No hay facturas para mostrar.");
            return;
        }
        System.out.println("\n--- Listado de Facturas ---");
        for (Factura invoice : invoices) {
            System.out.println("Factura ID: " + invoice.getId() +
                    ", Vendedor ID: " + invoice.getVendedorId() +
                    ", Cliente ID: " + invoice.getClienteId() +
                    ", Fecha: " + invoice.getFecha() +
                    ", Monto Total: $" + invoice.getTotal());
            // Opcionalmente, podrías mostrar aquí los detalles de cada factura llamando a invoiceService.getInvoiceItems(invoice.getId())
        }
        System.out.println("---------------------------\n");
    }
}