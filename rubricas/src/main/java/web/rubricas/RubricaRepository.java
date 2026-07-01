package web.rubricas;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RubricaRepository {

    private final JdbcTemplate jdbcTemplate;

    public RubricaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Rubrica rubrica) {
        String sql = "INSERT INTO rubricas (nombre, curso, descripcion, creador, criterios) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, rubrica.getNombre(), rubrica.getCurso(), rubrica.getDescripcion(), rubrica.getCreador(), rubrica.getCriterios());
    }

    public List<Rubrica> findAll() {
        String sql = "SELECT * FROM rubricas ORDER BY id DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Rubrica.class));
    }

    public List<Rubrica> findByCurso(String curso) {
        String sql = "SELECT * FROM rubricas WHERE curso = ? ORDER BY id DESC";
        return jdbcTemplate.query(sql, new Object[]{curso}, new BeanPropertyRowMapper<>(Rubrica.class));
    }
}
