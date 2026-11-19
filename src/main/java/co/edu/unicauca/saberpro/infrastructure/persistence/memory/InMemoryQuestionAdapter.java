package co.edu.unicauca.saberpro.infrastructure.persistence.memory;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.model.PreguntaId;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ADAPTADOR DE SALIDA (memoria): implementa el MISMO puerto con un HashMap, sin base de datos.
 * Guarda y devuelve copias para comportarse como una persistencia real: modificar el objeto
 * devuelto no cambia lo almacenado hasta volver a llamar a save().
 */
public class InMemoryQuestionAdapter implements PreguntaRepositoryPort {

    private final Map<PreguntaId, Pregunta> almacen = new ConcurrentHashMap<>();

    @Override
    public Pregunta save(Pregunta pregunta) {
        almacen.put(pregunta.getId(), copiar(pregunta));
        return copiar(pregunta);
    }

    @Override
    public Optional<Pregunta> findById(PreguntaId id) {
        return Optional.ofNullable(almacen.get(id)).map(InMemoryQuestionAdapter::copiar);
    }

    @Override
    public List<Pregunta> findAll() {
        List<Pregunta> todas = new ArrayList<>();
        for (Pregunta p : almacen.values()) {
            todas.add(copiar(p));
        }
        return todas;
    }

    private static Pregunta copiar(Pregunta p) {
        return Pregunta.reconstruir(p.getId(), p.getEnunciado(), p.getJustificacion(),
                p.getOpciones(), p.getEstado());
    }
}
