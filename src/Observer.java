/**
 * Rozhranie definujúce metódu na spracovanie udalostí pre pozorovateľov.
 */
public interface Observer {

    /**
     * Metóda na spracovanie udalostí.
     *
     * @param event informácie o udalosti
     */
    void handleEvent(String event);
}
