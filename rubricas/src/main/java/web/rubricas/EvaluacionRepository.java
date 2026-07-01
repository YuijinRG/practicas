package web.rubricas;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EvaluacionRepository {

    private final JdbcTemplate jdbcTemplate;

    public EvaluacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Evaluacion evaluacion) {
        String sql = "INSERT INTO evaluaciones (estudiante, rubrica_id, curso, puntaje, observacion) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, evaluacion.getEstudiante(), evaluacion.getRubricaId(), evaluacion.getCurso(), evaluacion.getPuntaje(), evaluacion.getObservacion());
    }

    public List<Evaluacion> findAll() {
        String sql = "SELECT * FROM evaluaciones ORDER BY id DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Evaluacion.class));
    }
}
