package web.rubricas.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.rubricas.model.Rubrica;
import web.rubricas.model.Usuario;
import web.rubricas.repository.RubricaRepository;

@Controller
public class RubricaController {

    private final RubricaRepository rubricaRepository;

    public RubricaController(RubricaRepository rubricaRepository) {
        this.rubricaRepository = rubricaRepository;
    }

    @GetMapping("/maestro/rubricas")
    public String mostrarRubricasMaestro(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !"MAESTRO".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("rubricas", rubricaRepository.findByCreador(usuario.getUsername()));
        return "rubricas_maestro";
    }

    @PostMapping("/maestro/rubricas")
    public String crearRubrica(@RequestParam String nombre,
                               @RequestParam String curso,
                               @RequestParam String descripcion,
                               @RequestParam String criterios,
                               HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !"MAESTRO".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/login";
        }

        Rubrica rubrica = new Rubrica();
        rubrica.setNombre(nombre);
        rubrica.setCurso(curso);
        rubrica.setDescripcion(descripcion);
        rubrica.setCriterios(criterios);
        rubrica.setCreador(usuario.getUsername());
        rubricaRepository.save(rubrica);

        return "redirect:/maestro/rubricas";
    }

    @GetMapping("/estudiante/rubricas")
    public String mostrarRubricasEstudiante(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !"ESTUDIANTE".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rubricas", rubricaRepository.findAll());
        return "rubricas_estudiante";
    }
}
