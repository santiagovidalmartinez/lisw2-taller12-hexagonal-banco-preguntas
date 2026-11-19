package co.edu.unicauca.saberpro.infrastructure.web;

import co.edu.unicauca.saberpro.infrastructure.persistence.memory.InMemoryQuestionAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/** PASO 2 y 3 del taller: se activa InMemoryQuestionAdapter y las MISMAS pruebas siguen pasando. */
@SpringBootTest(properties = {"saberpro.persistence=memory", "spring.datasource.url=jdbc:h2:mem:test-memory"})
@AutoConfigureMockMvc
class PreguntaApiEnMemoriaTest extends PreguntaApiContractTest {

    @Test
    void elAdaptadorActivoEsEnMemoria() {
        assertInstanceOf(InMemoryQuestionAdapter.class, adaptadorActivo);
    }
}
