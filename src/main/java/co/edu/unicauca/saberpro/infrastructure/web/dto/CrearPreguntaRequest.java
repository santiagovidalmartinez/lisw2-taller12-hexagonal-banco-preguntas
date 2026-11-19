package co.edu.unicauca.saberpro.infrastructure.web.dto;

import java.util.List;

/** Cuerpo JSON de POST /api/preguntas. */
public record CrearPreguntaRequest(String enunciado, String justificacion, List<OpcionRequest> opciones) {

    public record OpcionRequest(String texto, boolean correcta) {
    }
}
