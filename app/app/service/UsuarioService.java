package app.service;

import app.model.Usuario;
import app.repository.UsuarioRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio de lógica de negocio para Usuario.
 * Incluye validaciones y coordinación con el repositorio.
 */
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Validación simple de email (suficiente para prueba técnica)
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
    }

    /**
     * Lista todos los usuarios.
     */
    public List<Usuario> listarUsuarios() {
        try {
            return usuarioRepository.listar();
        } catch (SQLException e) {
            // Para CLI: propagamos como RuntimeException para que Main muestre el mensaje.
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un usuario por ID.
     */
    public Usuario buscarUsuarioPorId(Long id) {
        validarId(id);

        try {
            return usuarioRepository.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Crea un nuevo usuario.
     */
    public Usuario crearUsuario(Integer rut, String dv, String nombre, String email) {
        validarDatos(rut, dv, nombre, email);

        String dvNorm = normalizarDv(dv);
        String nombreNorm = nombre.trim();
        String emailNorm = email.trim().toLowerCase();

        try {
            // Validar duplicados antes de intentar insertar (mejor UX)
            if (usuarioRepository.existeEmail(emailNorm)) {
                throw new IllegalArgumentException("El email ya existe.");
            }
            if (usuarioRepository.existeRutDv(rut, dvNorm)) {
                throw new IllegalArgumentException("El RUT-DV ya existe.");
            }

            Usuario creado = usuarioRepository.crear(rut, dvNorm, nombreNorm, emailNorm);
            if (creado == null) {
                throw new RuntimeException("No se pudo crear el usuario.");
            }
            return creado;

        } catch (SQLException e) {
            // Si igual cae por UNIQUE, entregamos mensaje claro
            if ("23505".equals(e.getSQLState())) {
                throw new IllegalArgumentException("Ya existe un usuario con ese email o RUT-DV.");
            }
            throw new RuntimeException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos de un usuario existente.
     */
    public boolean actualizarUsuario(Long id, Integer rut, String dv, String nombre, String email) {
        validarId(id);
        validarDatos(rut, dv, nombre, email);

        String dvNorm = normalizarDv(dv);
        String nombreNorm = nombre.trim();
        String emailNorm = email.trim().toLowerCase();

        try {
            Usuario existente = usuarioRepository.buscarPorId(id);
            if (existente == null) {
                return false;
            }

            // Validar duplicados excluyendo el mismo ID
            if (usuarioRepository.existeEmailParaOtroId(id, emailNorm)) {
                throw new IllegalArgumentException("El email ya existe para otro usuario.");
            }
            if (usuarioRepository.existeRutDvParaOtroId(id, rut, dvNorm)) {
                throw new IllegalArgumentException("El RUT-DV ya existe para otro usuario.");
            }

            return usuarioRepository.actualizar(id, rut, dvNorm, nombreNorm, emailNorm);

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new IllegalArgumentException("Ya existe un usuario con ese email o RUT-DV.");
            }
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Activa un usuario (deja en true).
     */
    public boolean activarUsuario(Long id) {
        validarId(id);

        try {
            Usuario u = usuarioRepository.buscarPorId(id);
            if (u == null) return false;

            if (Boolean.TRUE.equals(u.getActivo())) {
                return true; // ya estaba activo
            }
            return usuarioRepository.actualizarActivo(id, true);

        } catch (SQLException e) {
            throw new RuntimeException("Error al activar usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Desactiva un usuario (deja en false).
     */
    public boolean desactivarUsuario(Long id) {
        validarId(id);

        try {
            Usuario u = usuarioRepository.buscarPorId(id);
            if (u == null) return false;

            if (Boolean.FALSE.equals(u.getActivo())) {
                return true; // ya estaba inactivo
            }
            return usuarioRepository.actualizarActivo(id, false);

        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Toggle para cumplir el menú "Activar / Desactivar" en una sola opción.
     */
    public boolean activarDesactivarUsuario(Long id) {
        validarId(id);

        try {
            Usuario u = usuarioRepository.buscarPorId(id);
            if (u == null) return false;

            boolean nuevoEstado = !Boolean.TRUE.equals(u.getActivo());
            return usuarioRepository.actualizarActivo(id, nuevoEstado);

        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar estado del usuario: " + e.getMessage(), e);
        }
    }

    // ------------------ Validaciones ------------------

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido. Debe ser mayor a 0.");
        }
    }

    private void validarDatos(Integer rut, String dv, String nombre, String email) {
        if (rut == null || rut <= 0) {
            throw new IllegalArgumentException("RUT es obligatorio y debe ser mayor a 0.");
        }

        if (dv == null || dv.trim().isEmpty() || dv.trim().length() != 1) {
            throw new IllegalArgumentException("DV es obligatorio y debe tener 1 carácter.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre es obligatorio.");
        }

        if (nombre.trim().length() > 200) {
            throw new IllegalArgumentException("Nombre excede 200 caracteres.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email es obligatorio.");
        }

        if (email.trim().length() > 200) {
            throw new IllegalArgumentException("Email excede 200 caracteres.");
        }

        String emailNorm = email.trim();
        if (!EMAIL_PATTERN.matcher(emailNorm).matches()) {
            throw new IllegalArgumentException("Formato de email inválido.");
        }
    }

    private String normalizarDv(String dv) {
        return dv.trim().toUpperCase();
    }
}