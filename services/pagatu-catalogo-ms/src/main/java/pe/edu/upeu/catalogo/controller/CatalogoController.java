package pe.edu.upeu.catalogo.controller;

import java.util.Map;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.catalogo.repository.ProductoRepository;

@RestController
@RequestMapping("/api/v1/catalogo")
public class CatalogoController {

    private final Environment environment;
    private final ProductoRepository productoRepository;

    public CatalogoController(Environment environment, ProductoRepository productoRepository) {
        this.environment = environment;
        this.productoRepository = productoRepository;
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
                "application", environment.getProperty("spring.application.name", ""),
                "profiles", environment.getActiveProfiles(),
                "serverPort", environment.getProperty("server.port", ""),
                "datasourceUrl", environment.getProperty("spring.datasource.url", ""),
                "swaggerUiEnabled", environment.getProperty("springdoc.swagger-ui.enabled", ""),
                "productos", productoRepository.count()
        ));
    }
}
