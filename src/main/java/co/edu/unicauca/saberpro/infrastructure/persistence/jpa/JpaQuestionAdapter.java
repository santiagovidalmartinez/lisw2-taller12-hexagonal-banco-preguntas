package co.edu.unicauca.saberpro.infrastructure.persistence.jpa;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.model.PreguntaId;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ADAPTADOR DE SALIDA (JPA): implementa el puerto del dominio con Spring Data JPA sobre H2.
 * No se anota como componente: HexagonalConfiguration decide si se activa.
 */
public class JpaQuestionAdapter implements PreguntaRepositoryPort {

    private final SpringDataPreguntaRepository repositorio;

    public JpaQuestionAdapter(SpringDataPreguntaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    @Transactional
    public Pregunta save(Pregunta pregunta) {
        PreguntaJpaEntity guardada = repositorio.save(PreguntaJpaMapper.aEntidad(pregunta));
        return PreguntaJpaMapper.aDominio(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pregunta> findById(PreguntaId id) {
        return repositorio.findById(id.valor()).map(PreguntaJpaMapper::aDominio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pregunta> findAll() {
        return repositorio.findAll().stream().map(PreguntaJpaMapper::aDominio).toList();
    }
}
