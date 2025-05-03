import dao.ClienteDAO;
import dao.VendedorDAO;
import model.*;
import service.AuthService;
import service.ClienteService;
import service.InvoiceService;
import service.ProductoService;
import service.VendedorService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static service.InvoiceService.printInvoiceDetails;

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
                    System.out.println("5. Editar factura");
                    System.out.println("6. Eliminar factura");
                    System.out.println("7. Ver detalles de factura");
                    System.out.println("8. Salir");
                    int adminChoice = scanner.nextInt();
                    scanner.nextLine();
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
                            editarFactura();
                            break;
                        case 6:
                            eliminarFactura();
                            break;
                        case 7:
                            System.out.print("Ingrese el ID de la factura para ver los detalles: ");
                            int facturaIdDetalle = scanner.nextInt();
                            scanner.nextLine();
                            printInvoiceDetails(facturaIdDetalle);
                            break;
                        case 8:
                            running = false;
                            break;
                        default:
                            System.out.println("Opción inválida.");
                    }
                    break;

                case "vendedor":
                    System.out.println("1. Emitir factura");
                    System.out.println("2. Ver mis facturas");
                    System.out.println("3. Ver detalles de factura");
                    System.out.println("4. Salir");
                    int sellerChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    switch (sellerChoice) {
                        case 1:
                            emitirFactura(user.getId());
                            break;
                        case 2:
                            List<Factura> sellerInvoices = invoiceService.getInvoicesForSeller(user.getId());
                            printInvoices(sellerInvoices);
                            break;
                        case 3:
                            System.out.print("Ingrese el ID de la factura para ver los detalles: ");
                            int facturaIdSeller = scanner.nextInt();
                            scanner.nextLine();
                            // Validar que la factura pertenece al vendedor antes de mostrar detalles
                            Factura facturaSeller = invoiceService.getInvoiceDetails(facturaIdSeller);
                            if (facturaSeller != null && facturaSeller.getVendedorId() == user.getId()) {
                                printInvoiceDetails(facturaIdSeller);
                            } else {
                                System.out.println("Error: No tiene permiso para ver los detalles de esa factura o la factura no existe.");
                            }
                            break;
                        case 4:
                            running = false;
                            break;
                        default:
                            System.out.println("Opción inválida.");
                    }
                    break;

                case "cliente":
                    System.out.println("1. Ver mis facturas");
                    System.out.println("2. Ver detalles de factura");
                    System.out.println("3. Salir");
                    int clientChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    switch (clientChoice) {
                        case 1:
                            List<Factura> clientInvoices = invoiceService.getInvoicesForClient(user.getId());
                            printInvoices(clientInvoices);
                            break;
                        case 2:
                            System.out.print("Ingrese el ID de la factura para ver los detalles: ");
                            int facturaIdClient = scanner.nextInt();
                            scanner.nextLine();
                            // Validar que la factura pertenece al cliente antes de mostrar detalles
                            Factura facturaClient = invoiceService.getInvoiceDetails(facturaIdClient);
                            if (facturaClient != null && facturaClient.getClienteId() == user.getId()) {
                                printInvoiceDetails(facturaIdClient);
                            } else {
                                System.out.println("Error: No tiene permiso para ver los detalles de esa factura o la factura no existe.");
                            }
                            break;
                        case 3:
                            running = false;
                            break;
                        default:
                            System.out.println("Opción inválida.");
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
                continue;
            }
            System.out.print("Cantidad a comprar: ");
            int cantidadComprada = scanner.nextInt();
            if (cantidadComprada > producto.getCantidad()) {
                System.out.println("Error: No hay suficiente stock disponible para el producto \"" + producto.getNombre() + "\" (Stock actual: " + producto.getCantidad() + ").");
                continue;
            }

            double subtotal = producto.getPrecio() * cantidadComprada;
            detalles.add(new DetalleFactura(0, 0, productoId, cantidadComprada, subtotal));
            totalFactura += subtotal;

            productoService.actualizarCantidadProducto(productoId, producto.getCantidad() - cantidadComprada);

            System.out.print("¿Agregar otro producto? (s/n): ");
            String respuesta = scanner.next();
            agregarOtroProducto = respuesta.equalsIgnoreCase("s");
        }
        scanner.nextLine();

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

        double precio = 0;
        boolean precioValido = false;
        while (!precioValido) {
            System.out.print("Precio del producto: ");
            String precioStr = scanner.nextLine();
            try {
                precio = Double.parseDouble(precioStr);
                if (precio >= 0) {
                    precioValido = true;
                } else {
                    System.out.println("Error: El precio debe ser un valor positivo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un valor numérico válido para el precio.");
            }
        }


        int cantidad = 0;
        boolean cantidadValida = false;
        while (!cantidadValida) {
            System.out.print("Cantidad inicial: ");
            String cantidadStr = scanner.nextLine();
            try {
                cantidad = Integer.parseInt(cantidadStr);
                if (cantidad >= 0) {
                    cantidadValida = true;
                } else {
                    System.out.println("Error: La cantidad debe ser un valor no negativo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un valor entero válido para la cantidad.");
            }
        }

        if (productoService.crearProducto(nombre, precio, cantidad)) {
            System.out.println("Producto creado exitosamente.");
        } else {
            System.out.println("Error al crear el producto.");
        }
    }
    private static void editarFactura() {
        System.out.println("\n--- Editar Factura ---");
        System.out.print("Ingrese el ID de la factura a editar: ");
        int facturaId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Factura factura = invoiceService.getInvoiceDetails(facturaId);
        if (factura == null) {
            System.out.println("Error: No se encontró la factura con el ID proporcionado.");
            return;
        }

        System.out.println("Datos actuales de la factura:");
        System.out.println("ID: " + factura.getId() + ", Vendedor ID: " + factura.getVendedorId() + ", Cliente ID: " + factura.getClienteId() + ", Fecha: " + factura.getFecha() + ", Total: " + factura.getTotal());

        System.out.print("Nuevo ID del vendedor: ");
        int nuevoVendedorId = scanner.nextInt();
        System.out.print("Nuevo ID del cliente: ");
        int nuevoClienteId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Nueva fecha (YYYY-MM-DD): ");
        String fechaStr = scanner.nextLine();
        LocalDate nuevaFecha = LocalDate.parse(fechaStr);
        System.out.print("Nuevo total: ");
        double nuevoTotal = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (invoiceService.actualizarFactura(facturaId, nuevoVendedorId, nuevoClienteId, nuevaFecha, nuevoTotal)) {
            System.out.println("Factura actualizada exitosamente.");
        } else {
            System.out.println("Error al actualizar la factura.");
        }
    }

    private static void eliminarFactura() {
        System.out.println("\n--- Eliminar Factura ---");
        System.out.print("Ingrese el ID de la factura a eliminar: ");
        int facturaId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Factura factura = invoiceService.getInvoiceDetails(facturaId);
        if (factura == null) {
            System.out.println("Error: No se encontró la factura con el ID proporcionado.");
            return;
        }

        System.out.println("¿Está seguro que desea eliminar la factura con ID " + factura.getId() + "? (s/n)");
        String confirmacion = scanner.nextLine();
        if (confirmacion.equalsIgnoreCase("s")) {
            if (invoiceService.eliminarFactura(facturaId)) {
                System.out.println("Factura eliminada exitosamente.");
            } else {
                System.out.println("Error al eliminar la factura.");
            }
        } else {
            System.out.println("Eliminación de factura cancelada.");
        }
    }
}