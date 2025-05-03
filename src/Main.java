import dao.ClienteDAO;
import dao.VendedorDAO;
import model.*;
import service.AuthService;
import service.ClienteService;
import service.InvoiceService;
import service.ProductoService;
import service.VendedorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static AuthService authService = new AuthService();
    private static InvoiceService invoiceService = new InvoiceService();
    private static ClienteService clienteService = new ClienteService();
    private static VendedorService vendedorService = new VendedorService();
    private static ProductoService productoService = new ProductoService();
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
                    System.out.println("2. Crear cliente");
                    System.out.println("3. Crear vendedor");
                    System.out.println("4. Crear producto");
                    System.out.println("5. Salir");
                    int adminChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    switch (adminChoice) {
                        case 1:
                            List<Factura> invoices = invoiceService.getInvoicesForAdmin();
                            printInvoices(invoices);
                            break;
                        case 2:
                            crearNuevoCliente();
                            break;
                        case 3:
                            crearNuevoVendedor();
                            break;
                        case 4:
                            crearNuevoProducto();
                            break;
                        case 5:
                            running = false;
                            break;
                        default:
                            System.out.println("Opción inválida.");
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
        Vendedor vendedor = new VendedorDAO().findById(vendedorId);
        if (vendedor == null) {
            System.out.println("Error al emitir factura: El ID del vendedor no es válido.");
            return;
        }
        System.out.print("ID del cliente: ");
        int clienteId = scanner.nextInt();
        Cliente cliente = new ClienteDAO().findById(clienteId);
        if (cliente == null) {
            System.out.println("Error al emitir factura: El ID del cliente no es válido.");
            return;
        }
        List<DetalleFactura> detalles = new ArrayList<>();
        double totalFactura = 0;

        boolean agregarOtroProducto = true;
        while (agregarOtroProducto) {
            System.out.print("ID del producto: ");
            int productoId = scanner.nextInt();
            Producto producto = productoService.obtenerProductoPorId(productoId);
            if (producto == null) {
                System.out.println("Error: El ID del producto no es válido.");
                continue; // Volver a pedir el ID del producto
            }
            System.out.print("Cantidad a comprar: ");
            int cantidadComprada = scanner.nextInt();
            if (cantidadComprada > producto.getCantidad()) {
                System.out.println("Error: No hay suficiente stock disponible para el producto \"" + producto.getNombre() + "\" (Stock actual: " + producto.getCantidad() + ").");
                continue; // Volver a pedir la cantidad
            }

            double subtotal = producto.getPrecio() * cantidadComprada;
            detalles.add(new DetalleFactura(0, 0, productoId, cantidadComprada, subtotal));
            totalFactura += subtotal;

            productoService.actualizarCantidadProducto(productoId, producto.getCantidad() - cantidadComprada);

            System.out.print("¿Agregar otro producto? (s/n): ");
            String respuesta = scanner.next();
            agregarOtroProducto = respuesta.equalsIgnoreCase("s");
        }
        scanner.nextLine(); // Consume newline

        invoiceService.createInvoice(vendedorId, clienteId, detalles, totalFactura);
        System.out.println("Factura emitida exitosamente. Total: $" + totalFactura);
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
    private static void crearNuevoCliente() {
        System.out.println("\n--- Crear Nuevo Cliente ---");
        System.out.print("Nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        if (clienteService.crearCliente(username, password)) {
            System.out.println("Cliente creado exitosamente.");
        } else {
            System.out.println("Error al crear el cliente (usuario ya existe o problema interno).");
        }
    }
    private static void crearNuevoVendedor() {
        System.out.println("\n--- Crear Nuevo Vendedor ---");
        System.out.print("Nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        if (vendedorService.crearVendedor(username, password)) {
            System.out.println("Vendedor creado exitosamente.");
        } else {
            System.out.println("Error al crear el vendedor (usuario ya existe o problema interno).");
        }
    }

    private static void crearNuevoProducto() {
        System.out.println("\n--- Crear Nuevo Producto ---");
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Precio del producto: ");
        double precio = scanner.nextDouble();
        System.out.print("Cantidad inicial: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (productoService.crearProducto(nombre, precio, cantidad)) {
            System.out.println("Producto creado exitosamente.");
        } else {
            System.out.println("Error al crear el producto.");
        }
    }
}