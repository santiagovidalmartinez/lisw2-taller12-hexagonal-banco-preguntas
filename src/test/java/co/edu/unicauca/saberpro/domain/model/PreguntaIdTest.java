package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreguntaIdTest {

    @Test
    void dosIdentificadoresNuevosSonDistintos() {
        assertNotEquals(PreguntaId.nuevo(), PreguntaId.nuevo());
    }

    @Test
    void unIdentificadorSeReconstruyeDesdeSuTexto() {
        PreguntaId id = PreguntaId.nuevo();
        assertEquals(id, PreguntaId.de(id.valor()));
        assertEquals(id.hashCode(), PreguntaId.de(id.valor()).hashCode());
    }

    @Test
    void rechazaTextosQueNoSonUuid() {
        assertThrows(ReglaNegocioException.class, () -> PreguntaId.de("123"));
        assertThrows(ReglaNegocioException.class, () -> PreguntaId.de(" "));
        assertThrows(ReglaNegocioException.class, () -> PreguntaId.de(null));
    }

    @Test
    void reconstruirConservaElEstadoSinRevalidarElCicloDeVida() {
        PreguntaId id = PreguntaId.nuevo();
        Pregunta p = Pregunta.reconstruir(id, new Enunciado("¿Pregunta?"),
                new Justificacion("Justificación suficientemente larga"),
                java.util.List.of(new OpcionRespuesta("A", true)), EstadoPregunta.APROBADA);
        assertEquals(id, p.getId());
        assertEquals(EstadoPregunta.APROBADA, p.getEstado());
        assertEquals(1, p.getOpciones().size());
    }
}
