import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje udalosť v systéme.
 */
public class Event extends CustomEvent {
    private String additionalField;
    private static List<Event> events; // Zoznam dostupných udalostí
    private List<Observer> observers; // Zoznam pozorovateľov udalostí

    static {
        events = new ArrayList<>(); // Inicializácia zoznamu udalostí
    }

    /**
     * Vytvára novú udalosť s daným ID a názvom.
     *
     * @param eventId   ID udalosti
     * @param eventName názov udalosti
     */
    public Event(int eventId, String eventName) {
        super(eventId, eventName); // Вызов конструктора родительского класса
        this.observers = new ArrayList<>(); // Inicializácia zoznamu pozorovateľov
    }

    /**
     * Pridá pozorovateľa k tejto udalosti.
     *
     * @param observer pozorovateľ udalosti
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Načíta novú udalosť z administrátora.
     *
     * @param event nová udalosť
     */
    public void loadNewEventFromAdmin(Event event) {
        events.add(event); // Pridá novú udalosť do zoznamu udalostí
        DatabaseManager.addEvent(event); // Pridá novú udalosť do databázy
        event.loadEventsFromDatabase(); // Načíta udalosti z databázy
    }

    /**
     * Vymaže všetky udalosti.
     */
    public static void clearEvents() {
        events.clear();
    }

    /**
     * Vráti zoznam dostupných udalostí.
     *
     * @return zoznam dostupných udalostí
     */
    public static List<Event> getAvailableEvents() {
        return events;
    }

    /**
     * Načíta udalosti z databázy.
     */
    public static void loadEventsFromDatabase() {
        // Pripojenie k databáze
        String url = "jdbc:mysql://127.0.0.1:3306/events";
        String ownerUsername = "root";
        String ownerPassword = "r110451006";

        try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM events")) {

            events.clear(); // Vymaže existujúce udalosti

            // Načíta údaje o udalostiach z databázy
            while (resultSet.next()) {
                int event_id = resultSet.getInt("event_id");
                String event_name = resultSet.getString("event_name");

                Event event = new Event(event_id, event_name);
                events.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Spravuje prístup k databáze udalostí.
     */
    public class DatabaseManager {
        /**
         * Pridá udalosť do databázy.
         *
         * @param event udalosť na pridanie
         */
        public static void addEvent(Event event) {
            // Pripojenie k databáze
            String url = "jdbc:mysql://127.0.0.1:3306/events";
            String ownerUsername = "root";
            String ownerPassword = "r110451006";

            String query = "INSERT INTO events (event_id, event_name) VALUES (?, ?)";

            events.clear(); // Vymaže existujúce udalosti

            try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, event.getEventId());
                statement.setString(2, event.getEventName());

                statement.executeUpdate(); // Vykoná príkaz vloženia
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
