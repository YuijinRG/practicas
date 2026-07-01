package web.rubricas;

public class Rubrica {
    private Long id;
    private String nombre;
    private String curso;
    private String descripcion;
    private String creador;
    private String criterios;

    public Rubrica() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCreador() { return creador; }
    public void setCreador(String creador) { this.creador = creador; }

    public String getCriterios() { return criterios; }
    public void setCriterios(String criterios) { this.criterios = criterios; }
}
