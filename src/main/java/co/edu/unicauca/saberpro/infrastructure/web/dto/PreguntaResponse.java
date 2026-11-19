package co.edu.unicauca.saberpro.infrastructure.web.dto;

import co.edu.unicauca.saberpro.domain.model.Pregunta;

import java.util.List;

/** Respuesta JSON de una pregunta. */
public record PreguntaResponse(String id, String enunciado, String justificacion,
                               List<OpcionResponse> opciones, String estado) {

    public record OpcionResponse(String texto, boolean correcta) {
    }

    public static PreguntaResponse desde(Pregunta p) {
        return new PreguntaResponse(
                p.getId().valor(),
                p.getEnunciado() == null ? null : p.getEnunciado().getTexto(),
                p.getJustificacion() == null ? null : p.getJustificacion().getTexto(),
                p.getOpciones().stream().map(o -> new OpcionResponse(o.getTexto(), o.isEsCorrecta())).toList(),
                p.getEstado().name());
    }
}
