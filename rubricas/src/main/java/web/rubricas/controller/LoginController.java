package web.rubricas.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.rubricas.model.Evaluacion;
import web.rubricas.model.Rubrica;
import web.rubricas.model.Usuario;
import web.rubricas.repository.EvaluacionRepository;
import web.rubricas.repository.RubricaRepository;
import web.rubricas.repository.UsuarioRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    private final UsuarioRepository usuarioRepository;
    private final RubricaRepository rubricaRepository;
    private final EvaluacionRepository evaluacionRepository;

    public LoginController(UsuarioRepository usuarioRepository,
                           RubricaRepository rubricaRepository,
                           EvaluacionRepository evaluacionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rubricaRepository = rubricaRepository;
        this.evaluacionRepository = evaluacionRepository;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Usuario usuario = usuarioRepository.findByUsernameAndPassword(username, password);
        if (usuario == null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }

        session.setAttribute("usuarioLogueado", usuario);
        if ("MAESTRO".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/maestro";
        }
        return "redirect:/estudiante";
    }

    @GetMapping("/registro")
    public String registroForm() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam String rol,
                            HttpSession session,
                            Model model) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("error", "Completa todos los campos");
            return "registro";
        }

        if (usuarioRepository.existsByUsername(username)) {
            model.addAttribute("error", "Ese usuario ya existe");
            return "registro";
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setRol(rol.toUpperCase());

        usuarioRepository.save(nuevoUsuario);
        session.setAttribute("usuarioLogueado", nuevoUsuario);

        if ("MAESTRO".equalsIgnoreCase(nuevoUsuario.getRol())) {
            return "redirect:/maestro";
        }
        return "redirect:/estudiante";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/estudiante")
    public String estudiante(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !"ESTUDIANTE".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        return "estudiante";
    }

    @GetMapping("/maestro")
    public String maestro(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !"MAESTRO".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/login";
        }
        List<Rubrica> rubricas = rubricaRepository.findByCreador(usuario.getUsername());
        Set<Long> rubricaIds = rubricas.stream()
                .map(Rubrica::getId)
                .collect(Collectors.toSet());

        List<Evaluacion> evaluacionesRecientes = evaluacionRepository.findAll().stream()
                .filter(evaluacion -> rubricaIds.contains(evaluacion.getRubricaId()))
                .limit(5)
                .toList();

        model.addAttribute("usuario", usuario);
        model.addAttribute("rubricas", rubricas);
        model.addAttribute("evaluacionesRecientes", evaluacionesRecientes);
        return "maestro";
    }
}
