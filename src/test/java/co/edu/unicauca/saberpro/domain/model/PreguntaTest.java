package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreguntaTest {

    private static final String JUSTIFICACION_OK = "La opción B aplica correctamente el principio.";

    /** Pregunta completa y válida, lista para enviarse a revisión. */
    private Pregunta preguntaValida() {
        Pregunta p = new Pregunta();
        p.definirEnunciado("¿Cuál es la capital de Colombia?");
        p.definirJustificacion(JUSTIFICACION_OK);
        p.agregarOpcion("Medellín", false);
        p.agregarOpcion("Bogotá", true);
        p.agregarOpcion("Cali", false);
        p.agregarOpcion("Popayán", false);
        return p;
    }

    // ------------------------------------------------------------ caso feliz
    @Test
    @DisplayName("Una pregunta válida pasa a PENDIENTE_REVISION")
    void debePermitirEnviarARevisionUnaPreguntaValida() {
        Pregunta p = preguntaValida();
        p.enviarARevision();
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, p.getEstado());
    }

    @Test
    void unaPreguntaNuevaNaceEnBorrador() {
        assertEquals(EstadoPregunta.BORRADOR, new Pregunta().getEstado());
    }

    // ------------------------------------------- invariante: exactamente 4 opciones
    @Test
    void noDebePermitirEnviarARevisionSinCuatroOpciones() {
        Pregunta pregunta = new Pregunta();
        pregunta.agregarOpcion("Opción A", true);
        assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
    }

    @Test
    void noDebePermitirEnviarConTresOpciones() {
        Pregunta p = new Pregunta();
        p.definirEnunciado("Enunciado de prueba");
        p.definirJustificacion(JUSTIFICACION_OK);
        p.agregarOpcion("A", true);
        p.agregarOpcion("B", false);
        p.agregarOpcion("C", false);
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, p::enviarARevision);
        assertTrue(ex.getMessage().contains("exactamente 4 opciones"));
    }

    @Test
    void noDebePermitirAgregarUnaQuintaOpcion() {
        Pregunta p = preguntaValida();
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> p.agregarOpcion("Pasto", false));
        assertEquals("Una pregunta no puede tener más de 4 opciones.", ex.getMessage());
        assertEquals(4, p.getOpciones().size());
    }

    @Test
    void noDebePermitirOpcionesConTextoRepetido() {
        Pregunta p = new Pregunta();
        p.agregarOpcion("Bogotá", true);
        assertThrows(ReglaNegocioException.class, () -> p.agregarOpcion("BOGOTÁ", false));
    }

    // ------------------------------------------- invariante: exactamente 1 clave
    @Test
    void noDebePermitirEnviarSinOpcionCorrecta() {
        Pregunta p = new Pregunta();
        p.definirEnunciado("Enunciado de prueba");
        p.definirJustificacion(JUSTIFICACION_OK);
        p.agregarOpcion("A", false);
        p.agregarOpcion("B", false);
        p.agregarOpcion("C", false);
        p.agregarOpcion("D", false);
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, p::enviarARevision);
        assertTrue(ex.getMessage().contains("exactamente 1 opción correcta"));
    }

    @Test
    void noDebePermitirEnviarConDosOpcionesCorrectas() {
        Pregunta p = new Pregunta();
        p.definirEnunciado("Enunciado de prueba");
        p.definirJustificacion(JUSTIFICACION_OK);
        p.agregarOpcion("A", true);
        p.agregarOpcion("B", true);
        p.agregarOpcion("C", false);
        p.agregarOpcion("D", false);
        assertThrows(ReglaNegocioException.class, p::enviarARevision);
        assertEquals(EstadoPregunta.BORRADOR, p.getEstado(), "Si falla, el estado no cambia");
    }

    // ------------------------------------------- invariante: enunciado y justificación
    @Test
    void noDebePermitirEnviarSinEnunciado() {
        Pregunta p = preguntaValida();
        Pregunta sin = new Pregunta();
        sin.definirJustificacion(JUSTIFICACION_OK);
        sin.agregarOpcion("A", true);
        sin.agregarOpcion("B", false);
        sin.agregarOpcion("C", false);
        sin.agregarOpcion("D", false);
        assertThrows(ReglaNegocioException.class, sin::enviarARevision);
        assertDoesNotThrow(p::enviarARevision);
    }

    @Test
    void noDebePermitirEnviarSinJustificacion() {
        Pregunta p = new Pregunta();
        p.definirEnunciado("Enunciado de prueba");
        p.agregarOpcion("A", true);
        p.agregarOpcion("B", false);
        p.agregarOpcion("C", false);
        p.agregarOpcion("D", false);
        assertThrows(ReglaNegocioException.class, p::enviarARevision);
    }

    @Test
    void laJustificacionCortaSeRechazaAlDefinirla() {
        Pregunta p = new Pregunta();
        assertThrows(ReglaNegocioException.class, () -> p.definirJustificacion("muy corta"));
        assertNull(p.getJustificacion());
    }

    // ------------------------------------------------ ciclo de vida (estados)
    @Test
    void noDebePermitirModificarOpcionesDespuesDeEnviar() {
        Pregunta p = preguntaValida();
        p.enviarARevision();
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> p.agregarOpcion("Otra", false));
        assertEquals("Solo se pueden modificar opciones en estado Borrador.", ex.getMessage());
    }

    @Test
    void noDebePermitirCambiarEnunciadoNiJustificacionFueraDeBorrador() {
        Pregunta p = preguntaValida();
        p.enviarARevision();
        assertThrows(ReglaNegocioException.class, () -> p.definirEnunciado("Otro enunciado"));
        assertThrows(ReglaNegocioException.class, () -> p.definirJustificacion(JUSTIFICACION_OK));
    }

    @Test
    void noDebePermitirEnviarDosVeces() {
        Pregunta p = preguntaValida();
        p.enviarARevision();
        assertThrows(ReglaNegocioException.class, p::enviarARevision);
    }

    @Test
    void soloUnaPreguntaPendienteSePuedeAprobarORechazar() {
        Pregunta borrador = preguntaValida();
        assertThrows(ReglaNegocioException.class, borrador::aprobar);
        assertThrows(ReglaNegocioException.class, borrador::rechazar);

        Pregunta pendiente = preguntaValida();
        pendiente.enviarARevision();
        pendiente.aprobar();
        assertEquals(EstadoPregunta.APROBADA, pendiente.getEstado());
        assertThrows(ReglaNegocioException.class, pendiente::rechazar);
    }

    @Test
    void unaPreguntaPendienteSePuedeRechazar() {
        Pregunta p = preguntaValida();
        p.enviarARevision();
        p.rechazar();
        assertEquals(EstadoPregunta.RECHAZADA, p.getEstado());
    }

    // ------------------------------------------------------------ encapsulación
    @Test
    void lasOpcionesExpuestasSonDeSoloLectura() {
        Pregunta p = preguntaValida();
        assertThrows(UnsupportedOperationException.class,
                () -> p.getOpciones().add(new OpcionRespuesta("Intruso", true)));
    }

    @Test
    void unaOpcionFallidaNoQuedaRegistrada() {
        Pregunta p = new Pregunta();
        assertThrows(ReglaNegocioException.class, () -> p.agregarOpcion("   ", true));
        assertTrue(p.getOpciones().isEmpty());
    }
}
