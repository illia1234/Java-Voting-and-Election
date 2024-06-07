import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Trieda {@code VotingManager} spravuje údaje o hlasovaní a uchováva zoznam výhercov.
 */
public class VotingManager extends Event {
    // Inicializácia údajov pre pripojenie k databáze
    static String url = "jdbc:mysql://127.0.0.1:3306/events";
    static String ownerUsername = "root";
    static String ownerPassword = "r110451006";

    // Atribúty pre údaje o hlasovaní
    private int eventId;
    private String poolName;
    private int voteCount;

    // Zoznam výhercov
    static List<VotingManager> winnersList = new ArrayList<VotingManager>();

    /**
     * Konštruktor pre vytvorenie inštancie {@code VotingManager} s údajmi o hlasovaní.
     *
     * @param eventId   ID udalosti
     * @param eventName  Názov hlasovacieho poolu
     * @param voteCount Počet hlasov
     */

    public VotingManager(int eventId, String eventName, int voteCount) {
        super(eventId, eventName);
        this.voteCount = voteCount;
    }

    // Gettery a settery pre atribúty údajov o hlasovaní

    /**
     * Získa názov hlasovacieho poolu.
     *
     * @return Názov hlasovacieho poolu
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     * Nastaví názov hlasovacieho poolu.
     *
     * @param poolName Názov hlasovacieho poolu
     */
    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    /**
     * Získa počet hlasov.
     *
     * @return Počet hlasov
     */
    public int getVoteCount() {
        return voteCount;
    }

    /**
     * Nastaví počet hlasov.
     *
     * @param voteCount Počet hlasov
     */
    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    /**
     * Získa zoznam výhercov.
     *
     * @return Zoznam výhercov
     */
    public static List<VotingManager> getWinnersList() {
        return winnersList;
    }

    /**
     * Načíta údaje o víťazoch z databázy a uloží ich do zoznamu {@code winnersList}.
     */
    public static void loadWinnersFromDatabase() {
        winnersList.clear(); // Vyčistenie zoznamu výhercov pred načítaním nových údajov
        try (Connection conn = DriverManager.getConnection(url, ownerUsername, ownerPassword)) {
            String query = "SELECT * FROM result";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int eventId = rs.getInt("result_id");
                    String poolName = rs.getString("result_name");
                    int voteCount = rs.getInt("vote_count");
                    winnersList.add(new VotingManager(eventId, poolName, voteCount)); // Pridanie nového víťaza do zoznamu
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading winners from database: " + e.getMessage());
        }
    }

    /**
     * Uloží údaje o víťazovi do databázy pre danú udalosť.
     *
     * @param eventId    ID udalosti
     * @param winnerData Mapa údajov o víťazovi (hlasovací pool a počet hlasov)
     */
    public static void saveWinnerData(int eventId, Map<VotingPool, Integer> winnerData) {
        try (Connection conn = DriverManager.getConnection(url, ownerUsername, ownerPassword)) {
            VotingPool winningPool = null;
            int maxVotes = Integer.MIN_VALUE;
            for (Map.Entry<VotingPool, Integer> entry : winnerData.entrySet()) {
                int voteCount = entry.getValue();
                if (voteCount > maxVotes) {
                    maxVotes = voteCount;
                    winningPool = entry.getKey();
                }
            }
            // Uloženie údajov o víťazovi do databázy
            if (winningPool != null) {
                String query = "INSERT INTO result (result_id, result_name, vote_count) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, eventId);
                    pstmt.setString(2, winningPool.getPoolName());
                    pstmt.setInt(3, maxVotes);
                    pstmt.executeUpdate();
                }
                System.out.println("Winner data saved successfully for event with ID: " + eventId);
            } else {
                System.out.println("No winner data to save for event with ID: " + eventId);
            }
        } catch (SQLException e) {
            System.err.println("Error saving winner data: " + e.getMessage());
        }
    }
}
