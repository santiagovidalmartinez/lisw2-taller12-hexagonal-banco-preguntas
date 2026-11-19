package co.edu.unicauca.saberpro.domain.ports.inbound;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.model.PreguntaId;

import java.util.List;

/** PUERTO DE ENTRADA (driving): casos de uso de creación y envío a revisión. */
public interface CrearPreguntaUseCase {

    /** Crea una pregunta en estado BORRADOR. */
    Pregunta crear(CrearPreguntaCommand comando);

    /** Pasa la pregunta a PENDIENTE_REVISION si cumple todos los invariantes. */
    Pregunta enviarARevision(PreguntaId id);

    /** Datos de una opción de respuesta. */
    record OpcionCommand(String texto, boolean correcta) {
    }

    /** Datos necesarios para crear una pregunta. */
    record CrearPreguntaCommand(String enunciado, String justificacion, List<OpcionCommand> opciones) {
    }
}
