package co.edu.unicauca.saberpro.infrastructure.persistence;

import co.edu.unicauca.saberpro.domain.model.EstadoPregunta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.model.PreguntaId;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CONTRATO del puerto de salida. Las mismas pruebas se ejecutan contra JpaQuestionAdapter y
 * contra InMemoryQuestionAdapter: si ambas pasan, los adaptadores son intercambiables.
 */
public abstract class PreguntaRepositoryPortContractTest {

    protected abstract PreguntaRepositoryPort crearRepositorio();

    private static Pregunta preguntaCompleta() {
        Pregunta p = new Pregunta();
        p.definirEnunciado("¿Cuál es la capital de Colombia?");
        p.definirJustificacion("Bogotá es la capital y sede del gobierno nacional.");
        p.agregarOpcion("Medellín", false);
        p.agregarOpcion("Bogotá", true);
        p.agregarOpcion("Cali", false);
        p.agregarOpcion("Popayán", false);
        return p;
    }

    @Test
    void guardaYRecuperaElAgregadoCompleto() {
        PreguntaRepositoryPort repo = crearRepositorio();
        Pregunta original = preguntaCompleta();
        repo.save(original);

        Pregunta leida = repo.findById(original.getId()).orElseThrow();
        assertEquals(original.getId(), leida.getId());
        assertEquals("¿Cuál es la capital de Colombia?", leida.getEnunciado().getTexto());
        assertEquals("Bogotá es la capital y sede del gobierno nacional.", leida.getJustificacion().getTexto());
        assertEquals(EstadoPregunta.BORRADOR, leida.getEstado());
        assertEquals(original.getOpciones(), leida.getOpciones(), "Las 4 opciones se conservan en orden");
    }

    @Test
    void devuelveVacioSiLaPreguntaNoExiste() {
        Optional<Pregunta> r = crearRepositorio().findById(PreguntaId.nuevo());
        assertTrue(r.isEmpty());
    }

    @Test
    void guardarDeNuevoActualizaElEstado() {
        PreguntaRepositoryPort repo = crearRepositorio();
        Pregunta p = preguntaCompleta();
        repo.save(p);

        Pregunta leida = repo.findById(p.getId()).orElseThrow();
        leida.enviarARevision();
        repo.save(leida);

        Pregunta releida = repo.findById(p.getId()).orElseThrow();
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, releida.getEstado());
        assertEquals(4, releida.getOpciones().size(), "Actualizar no duplica ni pierde opciones");
    }

    @Test
    void listaTodasLasPreguntasGuardadas() {
        PreguntaRepositoryPort repo = crearRepositorio();
        Pregunta a = preguntaCompleta();
        Pregunta b = preguntaCompleta();
        repo.save(a);
        repo.save(b);

        List<Pregunta> todas = repo.findAll();
        Set<PreguntaId> ids = todas.stream().map(Pregunta::getId).collect(Collectors.toSet());
        assertEquals(Set.of(a.getId(), b.getId()), ids);
    }

    @Test
    void guardaTambienUnBorradorIncompleto() {
        PreguntaRepositoryPort repo = crearRepositorio();
        Pregunta borrador = new Pregunta();
        borrador.definirEnunciado("Solo tengo enunciado");
        repo.save(borrador);

        Pregunta leida = repo.findById(borrador.getId()).orElseThrow();
        assertEquals("Solo tengo enunciado", leida.getEnunciado().getTexto());
        assertNull(leida.getJustificacion());
        assertTrue(leida.getOpciones().isEmpty());
    }

    @Test
    void modificarLoDevueltoNoCambiaLoAlmacenadoHastaGuardar() {
        PreguntaRepositoryPort repo = crearRepositorio();
        Pregunta p = preguntaCompleta();
        repo.save(p);

        Pregunta copia = repo.findById(p.getId()).orElseThrow();
        copia.enviarARevision();   // sin save()

        assertEquals(EstadoPregunta.BORRADOR, repo.findById(p.getId()).orElseThrow().getEstado());
    }
}
