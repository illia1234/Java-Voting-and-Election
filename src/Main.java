import javax.swing.*;

/**
 * Hlavná trieda aplikácie.
 */
public class Main {

    /**
     * Hlavná metóda spúšťajúca aplikáciu.
     *
     * @param args argumenty príkazového riadku
     */
    public static void main(String[] args) {

        // Načítanie udalostí z databázy
        Event event = new Event(1, "");
        event.loadEventsFromDatabase();

        // Načítanie volebných poolov z databázy
        VotingPool votingPool = new VotingPool(1, "");
        votingPool.loadVotingPoolFromDatabase();

        // Spustenie grafického používateľského rozhrania
        SwingUtilities.invokeLater(() -> {
            User.displayUsertInterface();
        });
    }
}
