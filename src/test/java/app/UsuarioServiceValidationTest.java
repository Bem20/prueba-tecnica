package app;

import app.service.UsuarioService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsuarioServiceValidationTest {

    @Test
    void crearUsuario_emailInvalido_debeFallar() {
        UsuarioService service = new UsuarioService();

        assertThrows(IllegalArgumentException.class, () ->
                service.crearUsuario(12345678, "9", "Nombre Prueba", "correo-invalido")
        );
    }

    @Test
    void crearUsuario_nombreVacio_debeFallar() {
        UsuarioService service = new UsuarioService();

        assertThrows(IllegalArgumentException.class, () ->
                service.crearUsuario(12345678, "9", "   ", "test@mail.com")
        );
    }
}
