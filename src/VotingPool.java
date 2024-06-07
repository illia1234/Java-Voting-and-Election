import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda {@code VotingPool} predstavuje hlasovací pool.
 */
public class VotingPool {
    // Atribúty pre identifikáciu hlasovacieho poolu
    private int poolId;
    private String poolName;

    // Zoznam všetkých hlasovacích poolov
    private static List<VotingPool> votingsPool;

    // Inicializácia zoznamu v statickom bloku
    static {
        votingsPool = new ArrayList<VotingPool>();
    }

    /**
     * Získa zoznam všetkých hlasovacích poolov.
     *
     * @return Zoznam hlasovacích poolov
     */
    public static List<VotingPool> getvotingsPool() {
        return votingsPool;
    }

    /**
     * Konštruktor pre vytvorenie inštancie {@code VotingPool} s určeným ID a názvom poolu.
     *
     * @param poolId   ID poolu
     * @param poolName Názov poolu
     */
    public VotingPool(int poolId, String poolName) {
        this.poolId = poolId;
        this.poolName = poolName;
    }

    /**
     * Pridá nový hlasovací pool do zoznamu poolov.
     *
     * @param votingPool Nový hlasovací pool
     */
    public void loadNewVotingPoolFromAdmin(VotingPool votingPool) {
        votingsPool.add(votingPool); // Pridanie nového poolu do zoznamu poolov
        System.out.println("Voting pool added: " + votingPool.getPoolName());
        DatabaseManagerPool.addVotingPool(votingPool); // Pridanie poolu do databázy
        votingPool.loadVotingPoolFromDatabase(); // Načítanie poolov z databázy
    }

    // Gettery a settery pre atribúty poolu

    /**
     * Získa ID poolu.
     *
     * @return ID poolu
     */
    public int getPoolId() {
        return poolId;
    }

    /**
     * Nastaví ID poolu.
     *
     * @param poolId ID poolu
     */
    public void setPoolId(int poolId) {
        this.poolId = poolId;
    }

    /**
     * Získa názov poolu.
     *
     * @return Názov poolu
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     * Nastaví názov poolu.
     *
     * @param poolName Názov poolu
     */
    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    /**
     * Načíta hlasovacie pooly z databázy.
     */
    public static void loadVotingPoolFromDatabase() {
        String url = "jdbc:mysql://127.0.0.1:3306/events";
        String ownerUsername = "root";
        String ownerPassword = "r110451006";

        try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM votingpools")) {

            votingsPool.clear(); // Vyčistenie zoznamu poolov pred načítaním nových údajov

            while (resultSet.next()) {
                int poolId = resultSet.getInt("pool_id");
                String poolName = resultSet.getString("pool_name");

                VotingPool votingPool = new VotingPool(poolId, poolName); // Vytvorenie novej inštancie poolu
                votingsPool.add(votingPool); // Pridanie poolu do zoznamu
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Vnútorná trieda {@code DatabaseManagerPool} slúži na správu databázy pre pooly.
     */
    public static class DatabaseManagerPool {

        /**
         * Pridá nový hlasovací pool do databázy.
         *
         * @param votingPool Nový hlasovací pool
         */
        public static void addVotingPool(VotingPool votingPool) {

            String url = "jdbc:mysql://127.0.0.1:3306/events";
            String ownerUsername = "root";
            String ownerPassword = "r110451006";

            String query = "INSERT INTO votingpools (pool_id, pool_name) VALUES (?, ?)";

            try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, votingPool.getPoolId());
                statement.setString(2, votingPool.getPoolName());

                statement.executeUpdate(); // Vykonanie príkazu na pridanie poolu do databázy
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
