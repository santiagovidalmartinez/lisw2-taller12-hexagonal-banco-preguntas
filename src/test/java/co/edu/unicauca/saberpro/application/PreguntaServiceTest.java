package co.edu.unicauca.saberpro.application;

import co.edu.unicauca.saberpro.domain.exception.PreguntaNoEncontradaException;
import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import co.edu.unicauca.saberpro.domain.model.EstadoPregunta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.model.PreguntaId;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaUseCase.CrearPreguntaCommand;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaUseCase.OpcionCommand;
import co.edu.unicauca.saberpro.infrastructure.persistence.memory.InMemoryQuestionAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Prueba del caso de uso sin Spring, sin JPA y sin base de datos: solo el adaptador en memoria. */
class PreguntaServiceTest {

    private PreguntaService servicio;

    @BeforeEach
    void preparar() {
        servicio = new PreguntaService(new InMemoryQuestionAdapter());
    }

    static CrearPreguntaCommand comandoCompleto() {
        return new CrearPreguntaCommand("¿Cuál es la capital de Colombia?",
                "Bogotá es la capital y sede del gobierno nacional.",
                List.of(new OpcionCommand("Medellín", false), new OpcionCommand("Bogotá", true),
                        new OpcionCommand("Cali", false), new OpcionCommand("Popayán", false)));
    }

    @Test
    void creaUnaPreguntaEnBorradorYLaPersiste() {
        Pregunta creada = servicio.crear(comandoCompleto());
        assertEquals(EstadoPregunta.BORRADOR, creada.getEstado());
        assertEquals(4, creada.getOpciones().size());
        assertEquals(creada.getId(), servicio.obtener(creada.getId()).getId());
    }

    @Test
    void enviaARevisionUnaPreguntaCompleta() {
        Pregunta creada = servicio.crear(comandoCompleto());
        Pregunta enviada = servicio.enviarARevision(creada.getId());
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, enviada.getEstado());
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, servicio.obtener(creada.getId()).getEstado());
    }

    @Test
    void noEnviaARevisionUnaPreguntaIncompletaYSuEstadoNoCambia() {
        Pregunta creada = servicio.crear(new CrearPreguntaCommand("¿Enunciado?",
                "Una justificación suficientemente larga", List.of(new OpcionCommand("A", true))));
        assertThrows(ReglaNegocioException.class, () -> servicio.enviarARevision(creada.getId()));
        assertEquals(EstadoPregunta.BORRADOR, servicio.obtener(creada.getId()).getEstado());
    }

    @Test
    void noCreaUnaPreguntaConJustificacionCorta() {
        CrearPreguntaCommand malo = new CrearPreguntaCommand("¿Enunciado?", "corta", List.of());
        assertThrows(ReglaNegocioException.class, () -> servicio.crear(malo));
        assertTrue(servicio.listar().isEmpty(), "Nada se guarda si una regla falla");
    }

    @Test
    void noCreaUnaPreguntaConMasDeCuatroOpciones() {
        CrearPreguntaCommand malo = new CrearPreguntaCommand("¿Enunciado?", "Justificación suficientemente larga",
                List.of(new OpcionCommand("A", true), new OpcionCommand("B", false), new OpcionCommand("C", false),
                        new OpcionCommand("D", false), new OpcionCommand("E", false)));
        assertThrows(ReglaNegocioException.class, () -> servicio.crear(malo));
    }

    @Test
    void obtenerUnaPreguntaInexistenteLanzaExcepcionDeDominio() {
        assertThrows(PreguntaNoEncontradaException.class, () -> servicio.obtener(PreguntaId.nuevo()));
        assertThrows(PreguntaNoEncontradaException.class, () -> servicio.enviarARevision(PreguntaId.nuevo()));
    }

    @Test
    void listaTodasLasPreguntas() {
        servicio.crear(comandoCompleto());
        servicio.crear(comandoCompleto());
        assertEquals(2, servicio.listar().size());
    }
}
