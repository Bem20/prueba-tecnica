package app;

import app.model.Usuario;
import app.service.UsuarioService;

import java.util.List;
import java.util.Scanner;

/**
 * Clase principal con interfaz CLI para gestión de usuarios.
 * Menú y comportamiento alineados al enunciado.
 */
public class Main {

    private static final UsuarioService usuarioService = new UsuarioService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Sistema de Gestion de Usuarios");
        System.out.println("===========================================\n");

        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    listarUsuariosTabular();
                    break;
                case 2:
                    buscarUsuario();
                    break;
                case 3:
                    crearUsuario();
                    break;
                case 4:
                    actualizarUsuario();
                    break;
                case 5:
                    activarDesactivarUsuario(); // único flujo, como pide el enunciado
                    break;
                case 0:
                    salir = true;
                    System.out.println("Fin");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.\n");
            }
        }

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1) Listar usuarios");
        System.out.println("2) Buscar usuario por ID");
        System.out.println("3) Crear usuario");
        System.out.println("4) Editar usuario");
        System.out.println("5) Activar / Desactivar usuario");
        System.out.println("0) Salir");
        System.out.print("\nSeleccione una opcion: ");
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Listar: imprimir usuarios en formato tabular por consola.
     */
    private static void listarUsuariosTabular() {
        System.out.println("\n--- LISTAR USUARIOS ---");
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        System.out.printf("%-5s %-12s %-25s %-30s %-8s %-20s%n",
                "ID", "RUT-DV", "NOMBRE", "EMAIL", "ACTIVO", "FECHA_CREACION");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        for (Usuario u : usuarios) {
            String rutDv = (u.getRut() != null ? u.getRut() : "") + "-" + (u.getDv() != null ? u.getDv() : "");
            System.out.printf("%-5d %-12s %-25s %-30s %-8s %-20s%n",
                    u.getId(),
                    rutDv,
                    recortar(u.getNombre(), 25),
                    recortar(u.getEmail(), 30),
                    Boolean.TRUE.equals(u.getActivo()) ? "SI" : "NO",
                    u.getFechaCreacion()
            );
        }
    }

    private static void buscarUsuario() {
        System.out.println("\n--- BUSCAR USUARIO ---");
        System.out.print("Ingrese el ID del usuario: ");

        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Usuario usuario = usuarioService.buscarUsuarioPorId(id);

            if (usuario != null) {
                System.out.println("\nUsuario encontrado:");
                imprimirDetalle(usuario);
            } else {
                System.out.println("No se encontro usuario con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: ID invalido");
        } catch (IllegalArgumentException e) {
            System.out.println("\nError de validacion: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nError al buscar el usuario: " + e.getMessage());
        }
    }

    private static void crearUsuario() {
        System.out.println("\n--- CREAR USUARIO ---");

        System.out.print("Ingrese el RUT (formato: 12345678-9): ");
        String rutCompleto = scanner.nextLine();

        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese el email: ");
        String email = scanner.nextLine();

        try {
            RutDv rutDv = parseRut(rutCompleto);

            Usuario usuario = usuarioService.crearUsuario(rutDv.rut, rutDv.dv, nombre, email);
            System.out.println("\nUsuario creado exitosamente!");
            imprimirDetalle(usuario);

        } catch (IllegalArgumentException e) {
            System.out.println("\nError de validacion: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nError al crear el usuario: " + e.getMessage());
        }
    }

    private static void actualizarUsuario() {
        System.out.println("\n--- EDITAR USUARIO ---");

        System.out.print("Ingrese el ID del usuario a editar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Ingrese el nuevo RUT (formato: 12345678-9): ");
            String rutCompleto = scanner.nextLine();

            System.out.print("Ingrese el nuevo nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese el nuevo email: ");
            String email = scanner.nextLine();

            RutDv rutDv = parseRut(rutCompleto);

            boolean actualizado = usuarioService.actualizarUsuario(id, rutDv.rut, rutDv.dv, nombre, email);

            if (actualizado) {
                System.out.println("\nUsuario actualizado exitosamente!");
            } else {
                System.out.println("\nNo se encontro usuario con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: ID invalido");
        } catch (IllegalArgumentException e) {
            System.out.println("\nError de validacion: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nError al actualizar el usuario: " + e.getMessage());
        }
    }

    /**
     * Activar/Desactivar: cambia el estado activo según ID (toggle).
     */
    private static void activarDesactivarUsuario() {
        System.out.println("\n--- ACTIVAR / DESACTIVAR USUARIO ---");
        System.out.print("Ingrese el ID del usuario: ");

        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            boolean ok = usuarioService.activarDesactivarUsuario(id);

            if (ok) {
                System.out.println("\nEstado actualizado exitosamente!");
            } else {
                System.out.println("\nNo se encontro usuario con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: ID invalido");
        } catch (IllegalArgumentException e) {
            System.out.println("\nError de validacion: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nError al cambiar estado: " + e.getMessage());
        }
    }

    // ----------------- Helpers -----------------

    private static void imprimirDetalle(Usuario u) {
        System.out.println("ID: " + u.getId());
        System.out.println("RUT: " + u.getRut() + "-" + u.getDv());
        System.out.println("Nombre: " + u.getNombre());
        System.out.println("Email: " + u.getEmail());
        System.out.println("Activo: " + (Boolean.TRUE.equals(u.getActivo()) ? "SI" : "NO"));
        System.out.println("Fecha creación: " + u.getFechaCreacion());
    }

    private static String recortar(String s, int max) {
        if (s == null) return "";
        String t = s.trim();
        return t.length() <= max ? t : t.substring(0, max - 3) + "...";
    }

    private static RutDv parseRut(String rutCompleto) {
        if (rutCompleto == null) throw new IllegalArgumentException("RUT es obligatorio.");

        String[] partes = rutCompleto.trim().split("-");
        if (partes.length != 2) {
            throw new IllegalArgumentException("Formato de RUT invalido. Use formato 12345678-9");
        }

        String rutStr = partes[0].replace(".", "").trim();
        String dv = partes[1].trim();

        if (dv.length() != 1) {
            throw new IllegalArgumentException("DV invalido. Debe tener 1 caracter.");
        }

        try {
            Integer rut = Integer.parseInt(rutStr);
            return new RutDv(rut, dv.toUpperCase());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("RUT invalido.");
        }
    }

    private static class RutDv {
        final Integer rut;
        final String dv;

        RutDv(Integer rut, String dv) {
            this.rut = rut;
            this.dv = dv;
        }
    }
}