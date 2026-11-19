package co.edu.unicauca.saberpro.infrastructure.persistence;

import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.infrastructure.persistence.memory.InMemoryQuestionAdapter;

/** El adaptador en memoria cumple el contrato del puerto (sin Spring, sin base de datos). */
class InMemoryQuestionAdapterTest extends PreguntaRepositoryPortContractTest {
    @Override
    protected PreguntaRepositoryPort crearRepositorio() {
        return new InMemoryQuestionAdapter();
    }
}
