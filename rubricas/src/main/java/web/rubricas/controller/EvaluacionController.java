package web.rubricas.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.rubricas.model.Evaluacion;
import web.rubricas.model.Rubrica;
import web.rubricas.model.Usuario;
import web.rubricas.repository.EvaluacionRepository;
import web.rubricas.repository.RubricaRepository;

import java.util.Optional;


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

    @GetMapping("/maestro/evaluar")
    public String mostrarFormularioEvaluacion(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !"MAESTRO".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "evaluar";
    }

    @PostMapping({"/maestro/evaluaciones/crear", "/maestro/evaluaciones/guardar"})
    public String crearEvaluacion(
            @RequestParam(required = false) String estudianteId,
            @RequestParam(required = false) String estudiante,
            @RequestParam(required = false) Long rubricaId,
            @RequestParam(required = false) String tarea,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String criterio1,
            @RequestParam(required = false) String criterio2,
            @RequestParam(required = false) String criterio3,
            @RequestParam(required = false) String criterio4,
            @RequestParam(required = false) String criterio5,
            @RequestParam(required = false) String retroalimentacion,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !"MAESTRO".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/login";
        }

        String nombreEstudiante = resolveStudentName(estudianteId, estudiante);
        if (nombreEstudiante.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Debes seleccionar un estudiante");
            return "redirect:/maestro";
        }

        if (!hasText(criterio1) || !hasText(criterio2) || !hasText(criterio3) || !hasText(criterio4) || !hasText(criterio5)) {
            redirectAttributes.addFlashAttribute("error", "Debes calificar los 5 criterios antes de guardar");
            return "redirect:/maestro";
        }

        Long effectiveRubricaId = rubricaId;
        if (effectiveRubricaId == null) {
            Optional<Rubrica> primeraRubrica = rubricaRepository.findByCreador(usuario.getUsername()).stream().findFirst();
            if (primeraRubrica.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No hay rubricas disponibles para evaluar");
                return "redirect:/maestro/rubricas";
            }
            effectiveRubricaId = primeraRubrica.get().getId();
        }

        Optional<Rubrica> rubrica = rubricaRepository.findById(effectiveRubricaId);
        if (rubrica.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "La rubrica seleccionada no existe");
            return "redirect:/maestro";
        }

        if (!usuario.getUsername().equalsIgnoreCase(rubrica.get().getCreador())) {
            redirectAttributes.addFlashAttribute("error", "Solo puedes evaluar con tus propias rubricas");
            return "redirect:/maestro";
        }

        int score = scoreForLevel(criterio1) + scoreForLevel(criterio2) + scoreForLevel(criterio3)
                + scoreForLevel(criterio4) + scoreForLevel(criterio5);
        int percentage = score * 5;

        String observacion = normalizeText(retroalimentacion);
        if (hasText(tarea) || hasText(tipo)) {
            StringBuilder resumen = new StringBuilder();
            if (hasText(tarea)) {
                resumen.append("Tarea: ").append(tarea.trim());
            }
            if (hasText(tipo)) {
                if (resumen.length() > 0) {
                    resumen.append(" | ");
                }
                resumen.append("Tipo: ").append(tipo.trim());
            }

            if (!observacion.isBlank()) {
                observacion = resumen + "\n" + observacion;
            } else {
                observacion = resumen.toString();
            }
        }

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEstudiante(nombreEstudiante);
        evaluacion.setRubricaId(effectiveRubricaId);
        evaluacion.setCurso(rubrica.get().getCurso());
        evaluacion.setPuntaje(String.valueOf(percentage));
        evaluacion.setCriterio1(criterio1);
        evaluacion.setCriterio2(criterio2);
        evaluacion.setCriterio3(criterio3);
        evaluacion.setCriterio4(criterio4);
        evaluacion.setCriterio5(criterio5);
        evaluacion.setObservacion(observacion);
        evaluacionRepository.save(evaluacion);

        redirectAttributes.addFlashAttribute("mensaje", "Evaluacion guardada correctamente para " + nombreEstudiante);
        return "redirect:/maestro/evaluaciones";
    }

    private String resolveStudentName(String estudianteId, String estudiante) {
        if (hasText(estudiante)) {
            return estudiante.trim();
        }

        if (!hasText(estudianteId)) {
            return "";
        }

        return switch (estudianteId.trim()) {
            case "1" -> "Carlos Venegas";
            case "2" -> "Maria Gonzalez";
            case "3" -> "Jose Martinez";
            case "4" -> "Sofia Lopez";
            case "5" -> "Diego Fernandez";
            default -> "Estudiante " + estudianteId.trim();
        };
    }

    private int scoreForLevel(String level) {
        if (level == null) {
            return 0;
        }

        return switch (level.trim().toLowerCase()) {
            case "inicial" -> 1;
            case "en-desarrollo" -> 2;
            case "logrado" -> 3;
            case "sobresaliente" -> 4;
            default -> 0;
        };
    }

    private String normalizeText(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
