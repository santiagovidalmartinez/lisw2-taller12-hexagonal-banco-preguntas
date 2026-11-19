package co.edu.unicauca.saberpro.infrastructure.web;

import co.edu.unicauca.saberpro.infrastructure.persistence.jpa.JpaQuestionAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/** PASO 1 del taller: aplicación configurada con JpaQuestionAdapter. */
@SpringBootTest(properties = {"saberpro.persistence=jpa", "spring.datasource.url=jdbc:h2:mem:test-jpa"})
@AutoConfigureMockMvc
class PreguntaApiConJpaTest extends PreguntaApiContractTest {

    @Test
    void elAdaptadorActivoEsJpa() {
        assertInstanceOf(JpaQuestionAdapter.class, adaptadorActivo);
    }
}
