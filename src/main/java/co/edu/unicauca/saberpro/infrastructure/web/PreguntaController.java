package co.edu.unicauca.saberpro.infrastructure.web;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.model.PreguntaId;
import co.edu.unicauca.saberpro.domain.ports.inbound.ConsultarPreguntaUseCase;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaUseCase;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaUseCase.CrearPreguntaCommand;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaUseCase.OpcionCommand;
import co.edu.unicauca.saberpro.infrastructure.web.dto.CrearPreguntaRequest;
import co.edu.unicauca.saberpro.infrastructure.web.dto.PreguntaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * ADAPTADOR DE ENTRADA (driving): traduce HTTP/JSON a llamadas a los puertos de entrada.
 * Depende solo de interfaces del dominio, nunca del servicio concreto ni del repositorio.
 */
@RestController
@RequestMapping("/api/preguntas")
public class PreguntaController {

    private final CrearPreguntaUseCase crear;
    private final ConsultarPreguntaUseCase consultar;

    public PreguntaController(CrearPreguntaUseCase crear, ConsultarPreguntaUseCase consultar) {
        this.crear = crear;
        this.consultar = consultar;
    }

    @PostMapping
    public ResponseEntity<PreguntaResponse> crear(@RequestBody CrearPreguntaRequest peticion) {
        List<OpcionCommand> opciones = peticion.opciones() == null ? List.of()
                : peticion.opciones().stream().map(o -> new OpcionCommand(o.texto(), o.correcta())).toList();
        Pregunta creada = crear.crear(
                new CrearPreguntaCommand(peticion.enunciado(), peticion.justificacion(), opciones));
        return ResponseEntity.created(URI.create("/api/preguntas/" + creada.getId().valor()))
                .body(PreguntaResponse.desde(creada));
    }

    @PostMapping("/{id}/enviar-revision")
    public PreguntaResponse enviarARevision(@PathVariable String id) {
        return PreguntaResponse.desde(crear.enviarARevision(PreguntaId.de(id)));
    }

    @GetMapping("/{id}")
    public PreguntaResponse obtener(@PathVariable String id) {
        return PreguntaResponse.desde(consultar.obtener(PreguntaId.de(id)));
    }

    @GetMapping
    public List<PreguntaResponse> listar() {
        return consultar.listar().stream().map(PreguntaResponse::desde).toList();
    }
}
