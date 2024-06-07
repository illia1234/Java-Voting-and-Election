import java.util.ArrayList;
import java.util.List;

/**
 * Trieda {@code Woman} predstavuje ženu, ktorá je účastníkom.
 */
public class Woman extends Participant {
    // Atribút pre pohlavie ženy
    private String gender;

    // Zoznam všetkých žien
    private static List<Woman> womans;

    // Inicializácia zoznamu v statickom bloku
    static {
        womans = new ArrayList<>();
    }

    /**
     * Získa zoznam všetkých žien.
     *
     * @return Zoznam žien
     */
    public List<Woman> getWomanList() {
        return womans;
    }

    /**
     * Konštruktor pre vytvorenie inštancie {@code Woman} s určenými vlastnosťami.
     *
     * @param participantId ID účastníka
     * @param username      Používateľské meno
     * @param password      Heslo
     * @param accountId     ID účtu
     * @param eventState    Stav udalosti
     * @param votingState   Stav hlasovania
     * @param gender        Pohlavie
     */
    public Woman(int participantId, String username, int password, int accountId, boolean eventState, boolean votingState, String gender) {
        // Volanie konštruktora nadtriedy Participant
        super(participantId, username, password, accountId, eventState, votingState);
        this.gender = gender;
    }

    /**
     * Metóda na vykonanie akcií špecifických pre ženy.
     *
     * @return Reťazec s informáciami o vykonaných akciách
     */
    public String performFemaleActions() {
        StringBuilder result = new StringBuilder();

        // Získanie všetkých účastníkov
        List<Participant> participants = getParticipantList();

        // Získanie všetkých dostupných udalostí
        List<Event> events = Event.getAvailableEvents();

        // Spracovanie každého účastníka
        for (Participant participant : participants) {
            for (Event event : events) {
                if (participant.getParticipantId() == event.getEventId()) {
                    // Overenie, že účastník ešte nie je pridaný do zoznamu žien
                    if (!womans.contains(participant)) {
                        // Ak je názov udalosti "Flowers on the lawn" alebo "Celebration", pridaj účastníka do zoznamu žien
                        if (event.getEventName().equals("Flowers on the lawn") || event.getEventName().equals("Celebration")) {
                            womans.add((Woman) participant); // Prevedenie typu na Woman, pretože vieme, že ide o ženu
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

    // Gettery a settery pre atribút gender

    /**
     * Získa pohlavie ženy.
     *
     * @return Pohlavie ženy
     */
    public String getGender() {
        return gender;
    }

    /**
     * Nastaví pohlavie ženy.
     *
     * @param gender Pohlavie ženy
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

}
