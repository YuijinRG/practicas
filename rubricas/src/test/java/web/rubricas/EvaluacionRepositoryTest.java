package web.rubricas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EvaluacionRepositoryTest {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Test
    void shouldSaveAndListEvaluations() {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEstudiante("estudiante");
        evaluacion.setRubricaId(1L);
        evaluacion.setCurso("Programación");
        evaluacion.setPuntaje("90");
        evaluacion.setObservacion("Buen desempeño");

        evaluacionRepository.save(evaluacion);
        List<Evaluacion> evaluaciones = evaluacionRepository.findAll();

        assertTrue(evaluaciones.stream().anyMatch(item -> "estudiante".equals(item.getEstudiante())));
    }
}
