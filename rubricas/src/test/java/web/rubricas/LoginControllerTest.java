package web.rubricas;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    @Test
    void registrationShouldCreateSessionAndRedirectToStudentDashboard() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        LoginController controller = new LoginController(usuarioRepository);
        HttpSession session = mock(HttpSession.class);
        Model model = new ExtendedModelMap();

        when(usuarioRepository.existsByUsername("maria")).thenReturn(false);

        String view = controller.registrar("maria", "1234", "ESTUDIANTE", session, model);

        assertEquals("redirect:/estudiante", view);
        verify(session).setAttribute(eq("usuarioLogueado"), any(Usuario.class));
    }
}
