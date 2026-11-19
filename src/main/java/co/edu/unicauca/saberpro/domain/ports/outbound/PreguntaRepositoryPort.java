package co.edu.unicauca.saberpro.domain.ports.outbound;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.model.PreguntaId;

import java.util.List;
import java.util.Optional;

/**
 * PUERTO DE SALIDA (driven): lo que el núcleo necesita para persistir preguntas.
 * No menciona JPA, SQL ni ninguna tecnología: solo operaciones puras del dominio.
 */
public interface PreguntaRepositoryPort {

    Pregunta save(Pregunta pregunta);

    Optional<Pregunta> findById(PreguntaId id);

    List<Pregunta> findAll();
}
