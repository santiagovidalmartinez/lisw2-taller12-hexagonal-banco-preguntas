package co.edu.unicauca.saberpro.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/** Registro JPA de una opción de respuesta (vive solo en infraestructura). */
@Entity
@Table(name = "opciones_respuesta")
public class OpcionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String texto;

    @Column(name = "es_correcta", nullable = false)
    private boolean correcta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pregunta_id", nullable = false)
    private PreguntaJpaEntity pregunta;

    protected OpcionJpaEntity() {
    }

    public OpcionJpaEntity(String texto, boolean correcta, PreguntaJpaEntity pregunta) {
        this.texto = texto;
        this.correcta = correcta;
        this.pregunta = pregunta;
    }

    public Long getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isCorrecta() {
        return correcta;
    }

    public PreguntaJpaEntity getPregunta() {
        return pregunta;
    }
}
