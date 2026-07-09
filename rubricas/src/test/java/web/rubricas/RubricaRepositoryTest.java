package web.rubricas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import web.rubricas.model.Rubrica;
import web.rubricas.repository.RubricaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RubricaRepositoryTest {

    @Autowired
    private RubricaRepository rubricaRepository;

    @Test
    void shouldSaveAndListRubricas() {
        Rubrica rubrica = new Rubrica();
        rubrica.setNombre("Rúbrica de laboratorio");
        rubrica.setCurso("Programación");
        rubrica.setDescripcion("Evaluación práctica");
        rubrica.setCreador("maestro");
        rubrica.setCriterios("Participación, Entrega, Calidad");

        rubricaRepository.save(rubrica);
        List<Rubrica> rubricas = rubricaRepository.findAll();

        assertTrue(rubricas.stream().anyMatch(item -> "Rúbrica de laboratorio".equals(item.getNombre())));
        assertEquals("Programación", rubricas.stream().filter(item -> "Rúbrica de laboratorio".equals(item.getNombre())).findFirst().get().getCurso());
    }
}
