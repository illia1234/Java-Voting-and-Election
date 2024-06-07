import java.util.*;

/**
 * Reprezentuje vlastnú udalosť v systéme.
 */
public class CustomEvent {
    private int eventId; // ID udalosti
    private String eventName; // Názov udalosti

    /**
     * Konštruktor pre vytvorenie nového objektu typu CustomEvent s daným ID a názvom.
     *
     * @param eventId   ID udalosti
     * @param eventName názov udalosti
     */
    public CustomEvent(int eventId, String eventName) {
        this.eventId = eventId;
        this.eventName = eventName;
    }

    /**
     * Vráti ID udalosti.
     *
     * @return ID udalosti
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Nastaví ID udalosti.
     *
     * @param eventId ID udalosti
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * Vráti názov udalosti.
     *
     * @return názov udalosti
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Nastaví názov udalosti.
     *
     * @param eventName názov udalosti
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
