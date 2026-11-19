package co.edu.unicauca.saberpro.infrastructure.persistence;

import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.infrastructure.persistence.jpa.JpaQuestionAdapter;
import co.edu.unicauca.saberpro.infrastructure.persistence.jpa.SpringDataPreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/** El adaptador JPA (H2) cumple EXACTAMENTE el mismo contrato que el adaptador en memoria. */
@DataJpaTest
class JpaQuestionAdapterTest extends PreguntaRepositoryPortContractTest {

    @Autowired
    private SpringDataPreguntaRepository springData;

    @Override
    protected PreguntaRepositoryPort crearRepositorio() {
        return new JpaQuestionAdapter(springData);
    }
}
