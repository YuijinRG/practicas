package web.rubricas;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UsuarioRepository usuarioRepository;

    public LoginController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
        model.addAttribute("usuario", usuario);
        return "maestro";
    }
}
