package aor.proj3t.util.authutils;

import aor.proj3t.enums.Role;
import jakarta.ws.rs.NameBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking JAX-RS resources that require JWT authentication.
 * Can be applied at the class level to secure all methods or at the method
 * level
 * for selective security within a resource class.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Secured {
    /**
     * The roles allowed to access the endpoint.
     * If empty, any authenticated user is allowed.
     * 
     * @return The allowed roles
     */
    Role[] value() default {};
}
