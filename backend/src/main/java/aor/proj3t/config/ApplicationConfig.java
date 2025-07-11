package aor.proj3t.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * JAX-RS Application Configuration Class.
 * 
 * This class configures the base path for all REST endpoints in the
 * application.
 * All REST resources will be accessible under the "/api" path prefix.
 * 
 * <p>
 * For example, a resource mapped to "/users" will be accessible at
 * "/api/users".
 * </p>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    // No additional configuration needed - uses default JAX-RS behavior
}
