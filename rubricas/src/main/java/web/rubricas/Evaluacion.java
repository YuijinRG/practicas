package web.rubricas;

public class Evaluacion {
    private Long id;
    private String estudiante;
    private Long rubricaId;
    private String curso;
    private String puntaje;
    private String observacion;

    public Evaluacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEstudiante() { return estudiante; }
    public void setEstudiante(String estudiante) { this.estudiante = estudiante; }

    public Long getRubricaId() { return rubricaId; }
    public void setRubricaId(Long rubricaId) { this.rubricaId = rubricaId; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getPuntaje() { return puntaje; }
    public void setPuntaje(String puntaje) { this.puntaje = puntaje; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
