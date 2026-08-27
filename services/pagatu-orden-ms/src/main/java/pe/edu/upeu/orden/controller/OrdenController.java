package pe.edu.upeu.orden.controller;

import java.util.List;
import java.util.Map;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.orden.entity.Orden;
import pe.edu.upeu.orden.repository.OrdenRepository;

@RestController
@RequestMapping("/api/v1/ordenes")
public class OrdenController {

    private final Environment environment;
    private final OrdenRepository ordenRepository;

    public OrdenController(Environment environment, OrdenRepository ordenRepository) {
        this.environment = environment;
        this.ordenRepository = ordenRepository;
    }

    @GetMapping
    public ResponseEntity<List<Orden>> listar() {
        return ResponseEntity.ok(ordenRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> obtener(@PathVariable Long id) {
        return ordenRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
                "application", environment.getProperty("spring.application.name", ""),
                "profiles", environment.getActiveProfiles(),
                "serverPort", environment.getProperty("server.port", ""),
                "datasourceUrl", environment.getProperty("spring.datasource.url", ""),
                "swaggerUiEnabled", environment.getProperty("springdoc.swagger-ui.enabled", ""),
                "loggingRoot", environment.getProperty("logging.level.root", ""),
                "configServer", "http://localhost:18888",
                "ordenes", ordenRepository.count()
        ));
    }
}
