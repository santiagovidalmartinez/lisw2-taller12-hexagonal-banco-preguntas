package co.edu.unicauca.saberpro.application;

import co.edu.unicauca.saberpro.domain.exception.PreguntaNoEncontradaException;
import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.model.PreguntaId;
import co.edu.unicauca.saberpro.domain.ports.inbound.ConsultarPreguntaUseCase;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaUseCase;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;

import java.util.List;

/**
 * Servicio de aplicación: implementa los puertos de entrada y orquesta el dominio usando
 * únicamente el puerto de salida. Java puro: no es un componente de Spring; se registra en
 * HexagonalConfiguration.
 */
public class PreguntaService implements CrearPreguntaUseCase, ConsultarPreguntaUseCase {

    private final PreguntaRepositoryPort repositorio;

    public PreguntaService(PreguntaRepositoryPort repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Pregunta crear(CrearPreguntaCommand comando) {
        Pregunta pregunta = new Pregunta();
        pregunta.definirEnunciado(comando.enunciado());
        pregunta.definirJustificacion(comando.justificacion());
        if (comando.opciones() != null) {
            for (OpcionCommand opcion : comando.opciones()) {
                pregunta.agregarOpcion(opcion.texto(), opcion.correcta());
            }
        }
        return repositorio.save(pregunta);
    }

    @Override
    public Pregunta enviarARevision(PreguntaId id) {
        Pregunta pregunta = buscar(id);
        pregunta.enviarARevision();
        return repositorio.save(pregunta);
    }

    @Override
    public Pregunta obtener(PreguntaId id) {
        return buscar(id);
    }

    @Override
    public List<Pregunta> listar() {
        return repositorio.findAll();
    }

    private Pregunta buscar(PreguntaId id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new PreguntaNoEncontradaException(id.valor()));
    }
}
