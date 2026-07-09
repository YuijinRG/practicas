package web.rubricas;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EvaluacionRepository {

    private final JdbcTemplate jdbcTemplate;

    public EvaluacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Evaluacion evaluacion) {
        String sql = "INSERT INTO evaluaciones (estudiante, rubrica_id, curso, puntaje, criterio1, criterio2, criterio3, criterio4, criterio5, observacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                evaluacion.getEstudiante(),
                evaluacion.getRubricaId(),
                evaluacion.getCurso(),
                evaluacion.getPuntaje(),
                evaluacion.getCriterio1(),
                evaluacion.getCriterio2(),
                evaluacion.getCriterio3(),
                evaluacion.getCriterio4(),
                evaluacion.getCriterio5(),
                evaluacion.getObservacion()
        );
    }

    public List<Evaluacion> findAll() {
        String sql = "SELECT * FROM evaluaciones ORDER BY id DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Evaluacion.class));
    }

    public Optional<Evaluacion> findById(Long id) {
        String sql = "SELECT * FROM evaluaciones WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Evaluacion.class))
                .stream()
                .findFirst();
    }
}
