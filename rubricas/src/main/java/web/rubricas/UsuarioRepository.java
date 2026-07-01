package web.rubricas;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Usuario findByUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        return jdbcTemplate.query(
                sql,
                new Object[]{username},
                new BeanPropertyRowMapper<>(Usuario.class)
        ).stream().findFirst().orElse(null);
    }

    public Usuario findByUsernameAndPassword(String username, String password) {
        Usuario usuario = findByUsername(username);
        if (usuario == null) {
            return null;
        }

        String storedPassword = usuario.getPassword();
        if (passwordEncoder.matches(password, storedPassword)) {
            return usuario;
        }

        if (storedPassword != null && storedPassword.equals(password)) {
            String hashedPassword = passwordEncoder.encode(password);
            jdbcTemplate.update("UPDATE usuarios SET password = ? WHERE username = ?", hashedPassword, username);
            usuario.setPassword(hashedPassword);
            return usuario;
        }

        return null;
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public void save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, rol) VALUES (?, ?, ?)";
        String hashedPassword = passwordEncoder.encode(usuario.getPassword());
        jdbcTemplate.update(sql, usuario.getUsername(), hashedPassword, usuario.getRol());
    }
}
