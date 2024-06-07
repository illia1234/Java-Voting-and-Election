import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Trieda Participant reprezentuje účastníka udalosti.
 */
public class Participant extends User implements UserInterface, Observer {
    /**
     * ID účastníka.
     */
    private int participantId;

    /**
     * Používateľské meno účastníka.
     */
    private String username;

    /**
     * Heslo účastníka.
     */
    private int password;

    /**
     * ID účtu účastníka.
     */
    private int accountId;

    /**
     * Stav aktivity udalosti.
     */
    private boolean eventState;

    /**
     * Stav hlasovania.
     */
    private boolean votingState;

    /**
     * Zoznam všetkých účastníkov.
     */
    private static List<Participant> participantList;

    /**
     * Zoznam vybraných udalostí.
     */
    private List<VotingPool> selectedEvents = new ArrayList<>();

    /**
     * Mapa frekvencie udalostí.
     */
    private static Map<Integer, Map<VotingPool, Integer>> eventFrequencyMap;

    static {
        participantList = new ArrayList<>();
        eventFrequencyMap = new HashMap<>();
    }

    /**
     * Konštruktor pre vytvorenie účastníka udalosti.
     */
    public Participant(int participantId, String username, int password, int accountId, boolean eventState, boolean votingState) {
        this.participantId = participantId;
        this.username = username;
        this.password = password;
        this.accountId = accountId;
        this.eventState = eventState;
        this.votingState = votingState;
        this.feedbackHandler = new CommentFeedback();
    }

    public void addParticipant(Participant participant) {
        participantList.add(participant);
    }

    public static List<Participant> getParticipantList() {
        return participantList;
    }

    // Metóda na načítanie údajov o účastníkoch zo zoznamu účastníkov do poľa účastníkov
    public void loadParticipantsFromUser(List<Participant> participants) {
        participantList.clear();
        participantList.addAll(participants); // Pridanie všetkých účastníkov zo zoznamu do poľa účastníkov našej triedy Participant
    }

    public static Participant getParticipantByUsernamePassword(String username, String userpassword) {
        for (Participant participant : participantList) {
            if (participant.getUsername().equals(username)) {
                try {
                    int passwordInt = Integer.parseInt(userpassword);
                    if (passwordInt == participant.getPassword()) {
                        return participant;
                    } else {
                        throw new InvalidPasswordException("Incorrect password");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Error: the password must be a number.");
                    return null;
                } catch (InvalidPasswordException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    return null;
                }
            }
        }
        // Správa, že účet so zadaným názvom nebol nájdený
        JOptionPane.showMessageDialog(null, "An participant with the specified name was not found.");
        return null;
    }

    /**
     * Získať ID účastníka.
     * @return ID účastníka.
     */
    public int getParticipantId() {
        return participantId;
    }

    /**
     * Nastaviť ID účastníka.
     * @param participantId ID účastníka na nastavenie.
     */
    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    /**
     * Získať používateľské meno účastníka.
     * @return Používateľské meno účastníka.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Nastaviť používateľské meno účastníka.
     * @param username Používateľské meno účastníka na nastavenie.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Získať heslo účastníka.
     * @return Heslo účastníka.
     */
    public int getPassword() {
        return password;
    }

    /**
     * Nastaviť heslo účastníka.
     * @param password Heslo účastníka na nastavenie.
     */
    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * Získať ID účtu účastníka.
     * @return ID účtu účastníka.
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * Nastaviť ID účtu účastníka.
     * @param accountId ID účtu účastníka na nastavenie.
     */
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    /**
     * Overiť, či účastník má aktívnu udalosť.
     * @return True, ak má účastník aktívnu udalosť, inak False.
     */
    public boolean getHasActiveEvent() {
        return eventState;
    }

    public boolean getVotingState(){return votingState;}

    public void setInactiveVoting(boolean hasInactiveVoting) {
        this.votingState = hasInactiveVoting;
        DatabaseManager.updateParticipantVotingInactive(accountId);
        System.out.println("Participant with AccountId " + accountId + " has votind state " + votingState);
    }

    public void setActiveEvent(boolean hasActivevent) {
        this.eventState = hasActivevent;
        DatabaseManager.updateParticipantActive(participantId);
        System.out.println("Participant with AccountId " + accountId + " has " + eventState);
    }

    public void setFinishEvent(boolean hasInactiveEvent) {
        this.eventState = hasInactiveEvent;
        this.votingState = true;
        DatabaseManager.updateParticipantInactive(participantId);
        System.out.println("Participant with AccountId " + accountId + " has " + eventState);
    }

    @Override
    public void displayAdminInterface() {
    }

    // Pole na ukladanie spätnej väzby a hodnotenia účastníka
    private Object[] feedbackInfo = new Object[2];

    // Indexy prvkov poľa
    private static final int FEEDBACK_INDEX = 0;
    private static final int RATING_INDEX = 1;

    // Indexy prvkov poľa// Metóda na aktualizáciu spätnej väzby poľa
    public void updateFeedback(String feedback) {
        feedbackInfo[FEEDBACK_INDEX] = feedback;
    }

    // Metóda aktualizácie odhadu v poli
    public void updateRating(int rating) {
        feedbackInfo[RATING_INDEX] = rating;
    }

    @Override
    public void displayParticipantInterface() {
        // Vytvorenie a zobrazenie rozhrania účastníka
        SwingUtilities.invokeLater(() -> {
            JFrame participantFrame = new JFrame("Participant Interface");
            participantFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            participantFrame.setSize(400, 300);

            // Vytvorenie tlačidla na účasť na udalosti
            JButton participateButton;

            if (eventState && votingState) {
                participateButton = new JButton("There are currently new active events.");
                participateButton.addActionListener(e -> participateInEvent());
            } else if(eventState && !votingState) {
                participateButton = new JButton("A " + getUsername() + " has already voted");
                participateButton.setEnabled(false);
            } else {
                participateButton = new JButton("There are currently no active events.");
                participateButton.setEnabled(false);
            }

            // Vytvorenie panelu pre tlačidlo účasti
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(participateButton);

            // Vytvorenie panelu pre spätnú väzbu
            JPanel feedbackPanel = new JPanel();
            JTextField feedbackField = new JTextField(20);
            JComboBox<String> feedbackTypeComboBox = new JComboBox<>(new String[]{"Comment", "Rating"});
            JButton submitButton = new JButton("Submit Feedback");
            feedbackPanel.add(new JLabel("Enter your feedback: "));
            feedbackPanel.add(feedbackField);
            feedbackPanel.add(new JLabel("Select feedback type: "));
            feedbackPanel.add(feedbackTypeComboBox);
            feedbackPanel.add(submitButton);

            // Vytvorenie panelu na zobrazenie spätnej väzby a hodnotenia účastníka
            JPanel feedbackInfoPanel = new JPanel(new GridLayout(2, 1));
            JLabel feedbackLabel = new JLabel("Feedback:");
            JLabel ratingLabel = new JLabel("Rating:");
            feedbackInfoPanel.add(feedbackLabel);
            feedbackInfoPanel.add(ratingLabel);

            // Pridanie panelov do rámu
            participantFrame.add(buttonPanel, BorderLayout.NORTH);
            participantFrame.add(feedbackPanel, BorderLayout.CENTER);
            participantFrame.add(feedbackInfoPanel, BorderLayout.SOUTH);

            // Umiestnenie rámu do stredu obrazovky
            participantFrame.setLocationRelativeTo(null);

            // Rámček zobrazenia
            participantFrame.setVisible(true);

            // Obsluha udalosti kliknutia na tlačidlo „Odoslať spätnú väzbu“
            submitButton.addActionListener(e -> {
                String feedback = feedbackField.getText();
                String selectedFeedbackType = (String) feedbackTypeComboBox.getSelectedItem();
                switch (selectedFeedbackType.toLowerCase()) {
                    case "comment":
                        updateFeedback(feedback);
                        feedbackLabel.setText("Feedback: " + feedbackInfo[FEEDBACK_INDEX]);
                        break;
                    case "rating":
                        try {
                            int rating = Integer.parseInt(feedback);
                            updateRating(rating);
                            ratingLabel.setText("Rating: " + feedbackInfo[RATING_INDEX]);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(participantFrame, "Please enter a valid number for rating.");
                        }
                        break;
                    default:
                        System.out.println("Error: Unknown feedback type.");
                }
            });
        });
    }

    // Spôsob spracovania stlačenia tlačidla "Participate in event"
    private void participateInEvent() {
        JFrame activateVotingPoolFrame = new JFrame("Activate Position");
        activateVotingPoolFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<VotingPool> allVotingPool = VotingPool.getvotingsPool();

        List<VotingPool> availableVotingPool = new ArrayList<>();

        for (VotingPool event : allVotingPool) {
            if (event.getPoolId() == participantId) {
                availableVotingPool.add(event);
            }
        }

        JPanel eventsPanel = new JPanel(new GridLayout(availableVotingPool.size() + 1, 1));

        for (VotingPool event : availableVotingPool) {
            JCheckBox eventCheckBox = new JCheckBox(event.getPoolName());
            eventCheckBox.setActionCommand(String.valueOf(event.getPoolId()));
            eventsPanel.add(eventCheckBox);

            eventCheckBox.addItemListener(e -> {
                int eventId = Integer.parseInt(eventCheckBox.getActionCommand());
                if (eventCheckBox.isSelected()) {
                    selectedEvents.add(event);
                    updateEventFrequency(eventId, event, 1);
                } else {
                    selectedEvents.remove(event);
                    updateEventFrequency(eventId, event, -1);
                }
            });
        }

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            if (!selectedEvents.isEmpty()) {
                setInactiveVoting(false);
                System.out.println("Selected events:");
                for (VotingPool event : selectedEvents) {
                    System.out.println(event.getPoolName());
                }
                activateVotingPoolFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(activateVotingPoolFrame, "Please select at least one event.");
            }
        });
        eventsPanel.add(confirmButton);

        activateVotingPoolFrame.add(eventsPanel);
        activateVotingPoolFrame.pack();
        activateVotingPoolFrame.setLocationRelativeTo(null);
        activateVotingPoolFrame.setVisible(true);
    }

    public void changeParticipantId(int accountId, int newId, int eventId) {
        for (Participant participant : participantList) {
            if (participant.getAccountId() == accountId) {
                // Kontrola, či sa ID účastníka zhoduje s eventId
                if (participant.getParticipantId() == eventId) {
                    participant.setParticipantId(newId);
                    participant.setActiveEvent(true); // Nastavenie príznaku aktívnej udalosti pre tohto účastníka
                    DatabaseManager.updateParticipantActive(accountId);
                }
            }
        }
    }

    private void updateEventFrequency(int eventId, VotingPool event, int change) {
        // Získanie frekvenčnej mapy pre zadané eventId. Ak pre toto eventId nie je v eventFrequencyMap žiadny záznam, vytvorí sa nová prázdna frekvenčná mapa.
        Map<VotingPool, Integer> frequencyMap = eventFrequencyMap.getOrDefault(eventId, new HashMap<>());

        // Aktualizácia frekvencie výberu pre zadaný VotingPool
        frequencyMap.put(event, frequencyMap.getOrDefault(event, 0) + change);

        // Vložte aktualizovanú frekvenčnú mapu späť do mapy eventFrequencyMap
        eventFrequencyMap.put(eventId, frequencyMap);
    }

    public Map<VotingPool, Integer> determineWinnerInParallel(int eventId) {
        Map<VotingPool, Integer> frequencyMap = new ConcurrentHashMap<>(eventFrequencyMap.getOrDefault(eventId, new HashMap<>()));

        // Premenné na ukladanie informácií o víťazovi
        VotingPool[] winningPool = {null};
        int[] maxVotes = {Integer.MIN_VALUE};

        // Spracovanie záznamov v paralelnom režime
        frequencyMap.entrySet().parallelStream().forEach(entry -> {
            VotingPool pool = entry.getKey();
            int votes = entry.getValue();

            // Ak je aktuálny počet hlasov vyšší ako maximálny počet hlasov, aktualizujte informácie o víťazovi
            synchronized (this) {
                if (votes > maxVotes[0]) {
                    winningPool[0] = pool;
                    maxVotes[0] = votes;
                }
            }
        });

        // Výstupné informácie o víťazovi
        if (winningPool[0] != null) {
            System.out.println("For event with ID " + eventId + ", the winning pool name is: " + winningPool[0].getPoolName() + " with " + maxVotes[0] + " votes.");
        } else {
            System.out.println("No winner found for event with ID " + eventId + ".");
        }

        // Odstránenie záznamu udalosti z mapy
        eventFrequencyMap.remove(eventId);

        return frequencyMap;
    }

    public List<VotingPool> getSelectedEvents() {
        return selectedEvents;
    }

    public List<Participant> getParticipant(){
        return participantList;
    }

    @Override
    public void handleEvent(String event) {
        User.updateParticipantData(this);
    }

    // Vnorené rozhranie pre prácu so spätnou väzbou
    private interface Feedback {
        void submitFeedback(String feedback);
        String viewFeedback();
    }

    // Vnorená trieda, ktorá implementuje rozhranie spätnej väzby pre komentáre
    private class CommentFeedback implements Feedback {
        private String comment;

        @Override
        public void submitFeedback(String feedback) {
            this.comment = feedback;
        }

        @Override
        public String viewFeedback() {
            return "Comment: " + comment;
        }
    }

    // Vnorená trieda, ktorá implementuje rozhranie spätnej väzby pre vyhodnocovanie
    private class RatingFeedback implements Feedback {
        private int rating;

        @Override
        public void submitFeedback(String feedback) {
            try {
                this.rating = Integer.parseInt(feedback);
            } catch (NumberFormatException e) {
                System.out.println("Error: Feedback must be a number.");
            }
        }

        @Override
        public String viewFeedback() {
            return "Rating: " + rating;
        }
    }

    private Feedback feedbackHandler; // Pole na uloženie inštancie obsluhy spätnej väzby

    // Metóda nastavenia typu spätnej väzby
    public void setFeedbackType(String feedbackType) {
        // V závislosti od odovzdaného typu vyberte príslušnú implementáciu obsluhy
        switch (feedbackType.toLowerCase()) {
            case "comment":
                this.feedbackHandler = new CommentFeedback();
                break;
            case "rating":
                this.feedbackHandler = new RatingFeedback();
                break;
            default:
                System.out.println("Error: Unknown feedback type.");
        }
    }

    // Spôsob reprezentácie spätnej väzby
    public void provideFeedback(String feedback) {
        // Odoslanie spätnej väzby obsluhe
        feedbackHandler.submitFeedback(feedback);
    }

    // Spôsob zobrazenia spätnej väzby
    public String viewFeedback() {
        // Získanie reprezentácie spätnej väzby z obslužnej funkcie
        return feedbackHandler.viewFeedback();
    }
}