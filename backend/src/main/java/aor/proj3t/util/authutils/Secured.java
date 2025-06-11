package aor.proj3t.util.authutils;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import aor.proj3t.enums.Role;
import jakarta.ws.rs.NameBinding;

/**
 * Annotation for marking JAX-RS resources that require JWT authentication.
 * Can be applied at the class level to secure all methods or at the method
 * level
 * for selective security within a resource class.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, METHOD })
public @interface Secured {
    /**
     * The roles allowed to access the endpoint.
     * If empty, any authenticated user is allowed.
     * 
     * @return The allowed roles
     */
    Role[] value() default {};
}
