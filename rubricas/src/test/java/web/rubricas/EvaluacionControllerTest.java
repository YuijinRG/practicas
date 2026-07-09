package web.rubricas;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EvaluacionControllerTest {

    @Test
    void shouldCreateEvaluationAndRedirectToTeacherHistory() {
        RubricaRepository rubricaRepository = mock(RubricaRepository.class);
        EvaluacionRepository evaluacionRepository = mock(EvaluacionRepository.class);
        EvaluacionController controller = new EvaluacionController(rubricaRepository, evaluacionRepository);
        HttpSession session = mock(HttpSession.class);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        Usuario maestro = new Usuario(1L, "ana", "hash", "MAESTRO");
        Rubrica rubrica = new Rubrica();
        rubrica.setId(1L);
        rubrica.setCurso("Historia");
        rubrica.setCreador("ana");

        when(session.getAttribute("usuarioLogueado")).thenReturn(maestro);
        when(rubricaRepository.findById(1L)).thenReturn(Optional.of(rubrica));

        String view = controller.crearEvaluacion(
                "1",
                null,
                1L,
                "Ensayo",
                "ensayo",
                "logrado",
                "logrado",
                "sobresaliente",
                "en-desarrollo",
                "logrado",
                "Muy buen avance",
                session,
                redirectAttributes
        );

        assertEquals("redirect:/maestro/evaluaciones", view);
        verify(evaluacionRepository).save(any(Evaluacion.class));
    }

    @Test
    void shouldRejectEvaluationWhenAnyCriteriaIsMissing() {
        RubricaRepository rubricaRepository = mock(RubricaRepository.class);
        EvaluacionRepository evaluacionRepository = mock(EvaluacionRepository.class);
        EvaluacionController controller = new EvaluacionController(rubricaRepository, evaluacionRepository);
        HttpSession session = mock(HttpSession.class);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        Usuario maestro = new Usuario(1L, "ana", "hash", "MAESTRO");
        when(session.getAttribute("usuarioLogueado")).thenReturn(maestro);

        String view = controller.crearEvaluacion(
                "2",
                null,
                1L,
                "Ensayo",
                "ensayo",
                "logrado",
                "logrado",
                null,
                "en-desarrollo",
                "logrado",
                "",
                session,
                redirectAttributes
        );

        assertEquals("redirect:/maestro", view);
    }

    @Test
    void shouldFallbackToFirstRubricWhenRubricIsNotProvided() {
        RubricaRepository rubricaRepository = mock(RubricaRepository.class);
        EvaluacionRepository evaluacionRepository = mock(EvaluacionRepository.class);
        EvaluacionController controller = new EvaluacionController(rubricaRepository, evaluacionRepository);
        HttpSession session = mock(HttpSession.class);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        Usuario maestro = new Usuario(1L, "ana", "hash", "MAESTRO");
        Rubrica rubrica = new Rubrica();
        rubrica.setId(9L);
        rubrica.setCurso("Historia");
        rubrica.setCreador("ana");

        when(session.getAttribute("usuarioLogueado")).thenReturn(maestro);
        when(rubricaRepository.findByCreador("ana")).thenReturn(List.of(rubrica));
        when(rubricaRepository.findById(9L)).thenReturn(Optional.of(rubrica));

        String view = controller.crearEvaluacion(
                "3",
                null,
                null,
                "Proyecto",
                "proyecto",
                "logrado",
                "logrado",
                "logrado",
                "logrado",
                "logrado",
                "Buen trabajo",
                session,
                redirectAttributes
        );

        assertEquals("redirect:/maestro/evaluaciones", view);
        verify(evaluacionRepository).save(any(Evaluacion.class));
    }
}
