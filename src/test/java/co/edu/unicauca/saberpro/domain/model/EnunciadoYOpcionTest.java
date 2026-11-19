package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnunciadoYOpcionTest {

    @Test
    void elEnunciadoNoPuedeSerNuloNiVacio() {
        assertThrows(ReglaNegocioException.class, () -> new Enunciado(null));
        assertThrows(ReglaNegocioException.class, () -> new Enunciado("   "));
    }

    @Test
    void elEnunciadoSeRecortaYEsInmutable() {
        Enunciado e = new Enunciado("  ¿Qué es un agregado?  ");
        assertEquals("¿Qué es un agregado?", e.getTexto());
        assertEquals(e, new Enunciado("¿Qué es un agregado?"));
    }

    @Test
    void laOpcionExigeTexto() {
        assertThrows(ReglaNegocioException.class, () -> new OpcionRespuesta(null, true));
        assertThrows(ReglaNegocioException.class, () -> new OpcionRespuesta("  ", false));
    }

    @Test
    void laOpcionGuardaTextoYClave() {
        OpcionRespuesta o = new OpcionRespuesta(" Bogotá ", true);
        assertEquals("Bogotá", o.getTexto());
        assertTrue(o.isEsCorrecta());
        assertEquals(o, new OpcionRespuesta("Bogotá", true));
        assertNotEquals(o, new OpcionRespuesta("Bogotá", false));
    }
}
