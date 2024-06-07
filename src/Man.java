import java.util.ArrayList;
import java.util.List;

/**
 * Trieda reprezentujúca mužského účastníka.
 */
public class Man extends Participant {
    private String gender;

    private static List<Man> mans;

    static {
        mans = new ArrayList<>();
    }

    /**
     * Konštruktor pre vytvorenie mužského účastníka.
     *
     * @param participantId ID účastníka
     * @param username      používateľské meno
     * @param password      heslo
     * @param accountId     ID účtu
     * @param eventState    stav udalosti
     * @param votingState   stav hlasovania
     * @param gender        pohlavie
     */
    public Man(int participantId, String username, int password, int accountId, boolean eventState, boolean votingState, String gender) {
        super(participantId, username, password, accountId, eventState, votingState);
        this.gender = gender;
    }

    /**
     * Metóda na vykonávanie akcií špecifických pre mužov.
     *
     * @return informácie o vykonaných akciách
     */
    public String performMaleActions() {
        StringBuilder result = new StringBuilder();

        // Načítanie všetkých účastníkov
        List<Participant> participants = getParticipantList();

        List<Event> events = Event.getAvailableEvents();

        // Spracovanie každého účastníka
        for (Participant participant : participants) {
            for (Event event : events) {
                if (participant.getParticipantId() == event.getEventId()) {
                    // Skontrolujte, či účastník ešte nebol pridaný do zoznamu mužov
                    if (!mans.contains(participant)) {
                        if (event.getEventName().equals("Driveway") || event.getEventName().equals("Street") || event.getEventName().equals("House")) {
                            mans.add((Man) participant); // Prenesenie typu na Man, keďže vieme, že ide o muža
                        }
                    }
                }
            }

            // Pridanie informácií o každom účastníkovi do výsledku
            result.append("Participant: ").append(participant.getUsername()).append("\n");
        }

        // Vrátenie zhromaždených informácií o všetkých účastníkoch
        return result.toString();
    }

    /**
     * Metóda na získanie pohlavia účastníka.
     *
     * @return pohlavie účastníka
     */
    public String getGender() {
        return gender;
    }

    /**
     * Metóda na nastavenie pohlavia účastníka.
     *
     * @param gender pohlavie účastníka
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

}
