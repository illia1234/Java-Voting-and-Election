import java.io.*;

/**
 * Trieda na serializáciu a deserializáciu objektov typu Event.
 */
public class EventSerialization {

    /**
     * Serializuje objekt Event a zapíše ho do súboru.
     *
     * @param event    objekt Event na serializáciu
     * @param fileName názov súboru, do ktorého bude zapísaný serializovaný objekt
     */
    public static void serializeEvent(Event event, String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(event);
            System.out.println("Udalosť úspešne serializovaná.");
        } catch (IOException e) {
            System.err.println("Chyba pri serializácii udalosti: " + e.getMessage());
        }
    }

    /**
     * Deserializuje objekt Event zo súboru.
     *
     * @param fileName názov súboru, z ktorého bude prečítaný serializovaný objekt
     * @return objekt Event, ktorý bol deserializovaný zo súboru
     */
    public static Event deserializeEvent(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Event event = (Event) inputStream.readObject();
            System.out.println("Udalosť úspešne deserializovaná.");
            return event;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Chyba pri deserializácii udalosti: " + e.getMessage());
            return null;
        }
    }
}
