package co.edu.unicauca.saberpro.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/** Detalle técnico de Spring Data: solo lo usa el adaptador JPA. */
public interface SpringDataPreguntaRepository extends JpaRepository<PreguntaJpaEntity, String> {
}
