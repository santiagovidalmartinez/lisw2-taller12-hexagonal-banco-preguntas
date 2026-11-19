package co.edu.unicauca.saberpro.domain.exception;

/** Raíz de las excepciones del dominio. */
public class DomainException extends RuntimeException {
    public DomainException(String mensaje) {
        super(mensaje);
    }
}
