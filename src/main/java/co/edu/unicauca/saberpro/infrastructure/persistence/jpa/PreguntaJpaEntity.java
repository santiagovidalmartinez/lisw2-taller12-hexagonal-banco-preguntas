package co.edu.unicauca.saberpro.infrastructure.persistence.jpa;

import co.edu.unicauca.saberpro.domain.model.EstadoPregunta;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/** Registro JPA de la pregunta. El dominio NO conoce esta clase. */
@Entity
@Table(name = "preguntas")
public class PreguntaJpaEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 2000)
    private String enunciado;

    @Column(length = 2000)
    private String justificacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoPregunta estado;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderColumn(name = "orden")
    private List<OpcionJpaEntity> opciones = new ArrayList<>();

    protected PreguntaJpaEntity() {
    }

    public PreguntaJpaEntity(String id, String enunciado, String justificacion, EstadoPregunta estado) {
        this.id = id;
        this.enunciado = enunciado;
        this.justificacion = justificacion;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public EstadoPregunta getEstado() {
        return estado;
    }

    public List<OpcionJpaEntity> getOpciones() {
        return opciones;
    }
}
