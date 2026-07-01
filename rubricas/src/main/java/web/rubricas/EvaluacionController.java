package web.rubricas;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EvaluacionController {

    private final RubricaRepository rubricaRepository;
    private final EvaluacionRepository evaluacionRepository;

    public EvaluacionController(RubricaRepository rubricaRepository, EvaluacionRepository evaluacionRepository) {
        this.rubricaRepository = rubricaRepository;
        this.evaluacionRepository = evaluacionRepository;
    }

    @GetMapping("/maestro/evaluaciones")
    public String mostrarEvaluaciones(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !"MAESTRO".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("evaluaciones", evaluacionRepository.findAll());
        return "evaluaciones_maestro";
    }
}
