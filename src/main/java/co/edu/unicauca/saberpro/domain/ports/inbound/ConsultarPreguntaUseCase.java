package co.edu.unicauca.saberpro.domain.ports.inbound;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.model.PreguntaId;

import java.util.List;

/** PUERTO DE ENTRADA (driving): consultas sobre el banco de preguntas. */
public interface ConsultarPreguntaUseCase {

    Pregunta obtener(PreguntaId id);

    List<Pregunta> listar();
}
