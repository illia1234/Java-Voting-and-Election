import java.util.Observable;

/**
 * Rozhranie definujúce metódu na oznámenie pozorovateľom o udalostiach.
 */
public interface Observered {

    /**
     * Metóda na oznámenie pozorovateľom o udalostiach.
     *
     * @param event_id   ID udalosti
     * @param event_name názov udalosti
     */
    void notifyObservers(int event_id, String event_name);
}
