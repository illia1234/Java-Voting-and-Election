import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Trieda DatabaseManager reprezentuje komponent systému zodpovedný za interakciu s databázou.
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/events";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "r110451006";

    public static void deleteEvent(Event event) {
        String url = "jdbc:mysql://127.0.0.1:3306/events";
        String ownerUsername = "root";
        String ownerPassword = "r110451006";

        String sql = "DELETE FROM events WHERE event_id = ?";

        try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, event.getEventId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Event has been successfully deleted from the database.");
            } else {
                System.out.println("Failed to delete event from the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteParticipant(Participant participant) {
        String url = "jdbc:mysql://127.0.0.1:3306/events";
        String ownerUsername = "root";
        String ownerPassword = "r110451006";

        String sql = "DELETE FROM participants WHERE participant_id = ?";

        try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, participant.getParticipantId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("The participant has been successfully deleted from the database.");
            } else {
                System.out.println("Failed to remove a participant from the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateParticipantId(int accountId, int newId) {
        String url = "jdbc:mysql://127.0.0.1:3306/events";
        String ownerUsername = "root";
        String ownerPassword = "r110451006";

        // SQL-запрос UPDATE
        String sql = "UPDATE participants SET participant_id = ? WHERE account_id = ?";

        try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, newId);
            statement.setInt(2, accountId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("The participant ID has been successfully updated.");
            } else {
                System.out.println("Failed to update participant ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateParticipantActive(int participantId) {
        String url = "jdbc:mysql://127.0.0.1:3306/events";
        String ownerUsername = "root";
        String ownerPassword = "r110451006";

        // SQL-запрос UPDATE
        String sql = "UPDATE participants SET event_state = ? WHERE participant_id = ?";

        try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "true");
            statement.setInt(2, participantId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("The participant active has been successfully updated.");
            } else {
                System.out.println("Failed to update participant active.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateParticipantInactive(int participantId) {
        String url = "jdbc:mysql://127.0.0.1:3306/events";
        String ownerUsername = "root";
        String ownerPassword = "r110451006";

        // SQL-запрос UPDATE
        String sql = "UPDATE participants SET event_state = ?, voting_state = ? WHERE participant_id = ?";

        try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "false");
            statement.setString(2, "true");
            statement.setInt(3, participantId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("The participant end has been successfully updated.");
            } else {
                System.out.println("Failed to update participant active.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateParticipantVotingInactive(int participantId) {
        String url = "jdbc:mysql://127.0.0.1:3306/events";
        String ownerUsername = "root";
        String ownerPassword = "r110451006";

        // SQL-запрос UPDATE
        String sql = "UPDATE participants SET voting_state = ? WHERE account_id = ?";

        try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "false");
            statement.setInt(2, participantId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("The participant inactive voting has been successfully updated.");
            } else {
                System.out.println("Failed to update participant inactive.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
