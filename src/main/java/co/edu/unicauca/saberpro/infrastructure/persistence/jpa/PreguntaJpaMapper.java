package co.edu.unicauca.saberpro.infrastructure.persistence.jpa;

import co.edu.unicauca.saberpro.domain.model.Enunciado;
import co.edu.unicauca.saberpro.domain.model.Justificacion;
import co.edu.unicauca.saberpro.domain.model.OpcionRespuesta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.model.PreguntaId;

import java.util.List;

/** Traduce entre el agregado de dominio y los registros JPA. */
final class PreguntaJpaMapper {

    private PreguntaJpaMapper() {
    }

    static PreguntaJpaEntity aEntidad(Pregunta p) {
        PreguntaJpaEntity e = new PreguntaJpaEntity(
                p.getId().valor(),
                p.getEnunciado() == null ? null : p.getEnunciado().getTexto(),
                p.getJustificacion() == null ? null : p.getJustificacion().getTexto(),
                p.getEstado());
        for (OpcionRespuesta o : p.getOpciones()) {
            e.getOpciones().add(new OpcionJpaEntity(o.getTexto(), o.isEsCorrecta(), e));
        }
        return e;
    }

    static Pregunta aDominio(PreguntaJpaEntity e) {
        List<OpcionRespuesta> opciones = e.getOpciones().stream()
                .map(o -> new OpcionRespuesta(o.getTexto(), o.isCorrecta()))
                .toList();
        return Pregunta.reconstruir(
                PreguntaId.de(e.getId()),
                e.getEnunciado() == null ? null : new Enunciado(e.getEnunciado()),
                e.getJustificacion() == null ? null : new Justificacion(e.getJustificacion()),
                opciones,
                e.getEstado());
    }
}
