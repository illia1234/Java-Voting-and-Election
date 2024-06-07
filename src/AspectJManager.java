import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tento aspekt slúži na logovanie rôznych udalostí v Java aplikácii.
 * Definuje rôzne body zárezov (join points), vrátane volaní metód v určitých balíčkoch, metód s určitou anotáciou, metód s určitými návratovými typmi a ďalšie.
 * Potom definuje rôzne rady (advices), ktoré sa majú vykonať v týchto bodoch zárezov.
 */
@Aspect
public class AspectJManager {

    private static final Logger logger = Logger.getLogger(AspectJManager.class.getName());

    /**
     * Tento bod zárezu definuje volania metód v balíčku example.
     */
    public void inExamplePackage() {}

    /**
     * Tento bod zárezu definuje metódy s anotáciou @Loggable.
     */
    @Pointcut("@annotation(Loggable)")
    public void annotatedWithLoggable() {}

    /**
     * Tento bod zárezu definuje metódy s návratovým typom void.
     */
    @Pointcut("execution(void *(..))")
    public void voidReturnMethods() {}

    /**
     * Tento bod zárezu definuje metódy s návratovým typom, ktorý nie je void.
     */
    @Pointcut("execution(* *(..)) && !voidReturnMethods()")
    public void nonVoidReturnMethods() {}

    /**
     * Tento advices sa vykonáva pred volaním metódy a loguje informáciu o tom, ktorá metóda sa volá.
     */
    public void logBefore(JoinPoint joinPoint) {
        logger.log(Level.INFO, "Volanie metódy: " + joinPoint.getSignature().getName());
    }

    /**
     * Tento advices sa vykonáva po úspešnom dokončení metódy s anotáciou @Loggable a loguje informáciu o tom, že metóda bola úspešne dokončená.
     */
    @After("annotatedWithLoggable()")
    public void logAfterWithLoggableAnnotation(JoinPoint joinPoint) {
        logger.log(Level.INFO, "Metóda s anotáciou @Loggable úspešne dokončená: " + joinPoint.getSignature().getName());
    }

    /**
     * Tento advices sa vykonáva po dokončení metódy s návratovým typom a loguje informáciu o tom, že metóda bola úspešne dokončená.
     */
    @After("nonVoidReturnMethods()")
    public void logAfterReturning(JoinPoint joinPoint) {
        logger.log(Level.INFO, "Metóda dokončená: " + joinPoint.getSignature().getName());
    }

    /**
     * Tento advices sa vykonáva po výskytu výnimky v metóde a loguje informáciu o tom, že v metóde došlo k výnimke.
     */
    public void logAfterThrowing(JoinPoint joinPoint) {
        logger.log(Level.WARNING, "Výnimka v metóde: " + joinPoint.getSignature().getName());
    }
}
