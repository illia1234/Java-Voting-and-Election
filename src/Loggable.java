import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Táto anotácia označuje metódu, ktorá by mala byť logovaná.
 * Metóda, ktorá je označená touto anotáciou, bude logovaná po jej vykonaní.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {
}

