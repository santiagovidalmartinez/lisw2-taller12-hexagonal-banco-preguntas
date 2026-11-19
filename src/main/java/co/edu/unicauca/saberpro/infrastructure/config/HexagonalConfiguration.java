package co.edu.unicauca.saberpro.infrastructure.config;

import co.edu.unicauca.saberpro.application.PreguntaService;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.infrastructure.persistence.jpa.JpaQuestionAdapter;
import co.edu.unicauca.saberpro.infrastructure.persistence.jpa.SpringDataPreguntaRepository;
import co.edu.unicauca.saberpro.infrastructure.persistence.memory.InMemoryQuestionAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ÚNICO lugar donde se decide qué adaptador de salida usa el núcleo.
 * Se elige con la propiedad  saberpro.persistence = jpa | memory  (por defecto: jpa).
 * Cambiar de adaptador NO toca ni una línea de los paquetes domain y application.
 */
@Configuration
public class HexagonalConfiguration {

    /** Adaptador JPA + H2 (por defecto). */
    @Bean
    @ConditionalOnProperty(name = "saberpro.persistence", havingValue = "jpa", matchIfMissing = true)
    public PreguntaRepositoryPort preguntaRepositoryPort(SpringDataPreguntaRepository repositorio) {
        return new JpaQuestionAdapter(repositorio);
    }

    /** Adaptador en memoria (HashMap). */
    @Bean
    @ConditionalOnProperty(name = "saberpro.persistence", havingValue = "memory")
    public PreguntaRepositoryPort preguntaRepositoryPortEnMemoria() {
        return new InMemoryQuestionAdapter();
    }

    /** El servicio de aplicación se construye con el puerto de salida que haya quedado activo. */
    @Bean
    public PreguntaService preguntaService(PreguntaRepositoryPort repositorio) {
        return new PreguntaService(repositorio);
    }
}
