package co.edu.unicauca.saberpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de arranque: solo existe en la capa exterior (infraestructura de arranque). */
@SpringBootApplication
public class SaberProApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaberProApplication.class, args);
    }
}
