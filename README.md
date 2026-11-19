# Taller 12 — Arquitectura Hexagonal y DDD (Fase I)

Universidad del Cauca · Ingeniería de Software II · 2026.2
Banco de Preguntas Saber PRO · Java 17 · Spring Boot 3.4.5 · H2 · JUnit 5

El núcleo (dominio + aplicación) es **Java puro**. Todo lo tecnológico (REST, JPA, memoria) vive en `infrastructure` como adaptador.

```
co.edu.unicauca.saberpro
├── domain
│   ├── model        Pregunta (Aggregate Root), Enunciado, Justificacion, OpcionRespuesta, PreguntaId, EstadoPregunta
│   ├── exception    DomainException, ReglaNegocioException, PreguntaNoEncontradaException
│   └── ports
│       ├── inbound  CrearPreguntaUseCase, ConsultarPreguntaUseCase      (puertos de entrada)
│       └── outbound PreguntaRepositoryPort                              (puerto de salida)
├── application      PreguntaService (implementa los puertos de entrada)
└── infrastructure
    ├── web          PreguntaController, ApiExceptionHandler, dto/        (adaptador de entrada REST)
    ├── persistence
    │   ├── jpa      JpaQuestionAdapter + PreguntaJpaEntity + Spring Data (adaptador de salida JPA/H2)
    │   └── memory   InMemoryQuestionAdapter                              (adaptador de salida HashMap)
    └── config       HexagonalConfiguration                               (elige el adaptador activo)
```

Regla de dependencias: `infrastructure → application → domain`. Nunca al revés.

## Ejecutar

```
mvn spring-boot:run                                          # adaptador JPA + H2 (por defecto)
mvn spring-boot:run "-Dspring-boot.run.profiles=memory"      # adaptador en memoria
```

En PowerShell las comillas son necesarias. La API queda en `http://localhost:8085/api/preguntas`
y la consola de H2 en `http://localhost:8085/h2-console` (JDBC URL `jdbc:h2:mem:saberprodb`, usuario `sa`, sin clave).

| Método | Ruta | Respuesta |
|---|---|---|
| POST | `/api/preguntas` | 201 con la pregunta en `BORRADOR`; 400 si viola una regla |
| POST | `/api/preguntas/{id}/enviar-revision` | 200 con `PENDIENTE_REVISION`; 400 si no cumple los invariantes |
| GET | `/api/preguntas/{id}` | 200 / 404 |
| GET | `/api/preguntas` | 200 |

La colección `postman/Taller12_Hexagonal.postman_collection.json` se ejecuta igual con ambos adaptadores.

## Prueba de intercambiabilidad

El adaptador activo se decide en un único lugar, `HexagonalConfiguration`, con la propiedad `saberpro.persistence` (`jpa` | `memory`).
Cambiarla no modifica ninguna línea de `domain` ni de `application`.

```
mvn clean test
```

Las pruebas `PreguntaRepositoryPortContractTest` (contrato del puerto) y `PreguntaApiContractTest` (API REST completa) se ejecutan
contra **ambos** adaptadores: `InMemoryQuestionAdapterTest` / `JpaQuestionAdapterTest` y `PreguntaApiEnMemoriaTest` / `PreguntaApiConJpaTest`.
