package aor.proj3t.util.authutils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.ws.rs.NameBinding;

/**
 * Annotation for marking JAX-RS resources that accept temporary 2FA tokens.
 * This annotation indicates that the endpoint can work with temporary tokens
 * issued during the two-factor authentication process, rather than requiring
 * full session tokens.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface TwoFactorAuth {
}
