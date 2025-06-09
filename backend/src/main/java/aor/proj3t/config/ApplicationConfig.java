package aor.proj3t.config;

import aor.proj3t.resource.AuthResource;
import aor.proj3t.util.authutils.JwtAuthFilter;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.Set;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(JwtAuthFilter.class, AuthResource.class);
    }
}
