package app.repository;

import app.config.DbConnection;
import app.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para acceso a datos de Usuario usando JDBC.
 * Requisito: usar PreparedStatement y cerrar recursos (try-with-resources).
 */
public class UsuarioRepository {

    private Connection getConnection() throws SQLException {
        return DbConnection.getConnection();
    }

    public List<Usuario> listar() throws SQLException {
        String sql = "SELECT id, rut, dv, nombre, email, activo, fecha_creacion " +
                "FROM usuarios ORDER BY id";

        List<Usuario> out = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(map(rs));
            }
        }

        return out;
    }

    public Usuario buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, rut, dv, nombre, email, activo, fecha_creacion " +
                "FROM usuarios WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE email = ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existeEmailParaOtroId(Long id, String email) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE email = ? AND id <> ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setLong(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existeRutDv(Integer rut, String dv) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE rut = ? AND dv = ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rut);
            ps.setString(2, dv);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existeRutDvParaOtroId(Long id, Integer rut, String dv) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE rut = ? AND dv = ? AND id <> ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rut);
            ps.setString(2, dv);
            ps.setLong(3, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public Usuario crear(Integer rut, String dv, String nombre, String email) throws SQLException {
        String sql = "INSERT INTO usuarios (rut, dv, nombre, email, activo) " +
                "VALUES (?, ?, ?, ?, TRUE) " +
                "RETURNING id, rut, dv, nombre, email, activo, fecha_creacion";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rut);
            ps.setString(2, dv);
            ps.setString(3, nombre);
            ps.setString(4, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public boolean actualizar(Long id, Integer rut, String dv, String nombre, String email) throws SQLException {
        String sql = "UPDATE usuarios SET rut = ?, dv = ?, nombre = ?, email = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rut);
            ps.setString(2, dv);
            ps.setString(3, nombre);
            ps.setString(4, email);
            ps.setLong(5, id);

            return ps.executeUpdate() == 1;
        }
    }

    public boolean actualizarActivo(Long id, boolean activo) throws SQLException {
        String sql = "UPDATE usuarios SET activo = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, activo);
            ps.setLong(2, id);

            return ps.executeUpdate() == 1;
        }
    }

    private Usuario map(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getLong("id"));
        u.setRut(rs.getInt("rut"));
        u.setDv(rs.getString("dv"));
        u.setNombre(rs.getString("nombre"));
        u.setEmail(rs.getString("email"));
        u.setActivo(rs.getBoolean("activo"));
        u.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return u;
    }
}
