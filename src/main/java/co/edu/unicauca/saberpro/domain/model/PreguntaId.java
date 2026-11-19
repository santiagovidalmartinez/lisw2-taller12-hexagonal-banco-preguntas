package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.UUID;

/** VALUE OBJECT: identificador fuertemente tipado de una pregunta (se genera en el dominio). */
public final class PreguntaId {
    private final UUID valor;

    private PreguntaId(UUID valor) {
        this.valor = valor;
    }

    public static PreguntaId nuevo() {
        return new PreguntaId(UUID.randomUUID());
    }

    public static PreguntaId de(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new ReglaNegocioException("El identificador de la pregunta es obligatorio.");
        }
        try {
            return new PreguntaId(UUID.fromString(texto.trim()));
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException("El identificador de la pregunta no es válido: " + texto);
        }
    }

    public String valor() {
        return valor.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PreguntaId otro && valor.equals(otro.valor);
    }

    @Override
    public int hashCode() {
        return valor.hashCode();
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
