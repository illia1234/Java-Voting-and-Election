/**
 * Výnimka InvalidPasswordException sa vyvoláva v prípade, že zadané heslo nie je platné.
 */
public class InvalidPasswordException extends Exception {

    /**
     * Vytvára novú inštanciu výnimky s danou správou.
     *
     * @param message správa popisujúca dôvod výnimky
     */
    public InvalidPasswordException(String message) {
        super(message);
    }
}
