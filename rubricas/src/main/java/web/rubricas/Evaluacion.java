package web.rubricas;

import java.time.LocalDateTime;

public class Evaluacion {
    private Long id;
    private String estudiante;
    private Long rubricaId;
    private String curso;
    private String puntaje;
    private String criterio1;
    private String criterio2;
    private String criterio3;
    private String criterio4;
    private String criterio5;
    private String observacion;
    private LocalDateTime createdAt;

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

    public String getCriterio1() { return criterio1; }
    public void setCriterio1(String criterio1) { this.criterio1 = criterio1; }

    public String getCriterio2() { return criterio2; }
    public void setCriterio2(String criterio2) { this.criterio2 = criterio2; }

    public String getCriterio3() { return criterio3; }
    public void setCriterio3(String criterio3) { this.criterio3 = criterio3; }

    public String getCriterio4() { return criterio4; }
    public void setCriterio4(String criterio4) { this.criterio4 = criterio4; }

    public String getCriterio5() { return criterio5; }
    public void setCriterio5(String criterio5) { this.criterio5 = criterio5; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
