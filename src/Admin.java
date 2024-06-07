import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

/**
 * Reprezentuje správcu v systéme.
 */
public class Admin extends User implements UserInterface, Observered {
    private int participantId; // ID účastníka
    private String username; // Meno správcu
    private int password; // Heslo
    private int accountId; // ID účtu

    private static List<Admin> adminList; // Zoznam správcov

    static {
        adminList = new ArrayList<>();
    }

    /**
     * Vytvára nového správcu s daným ID, menom, heslom a ID účtu.
     *
     * @param participantId ID účastníka
     * @param username      meno správcu
     * @param password      heslo
     * @param accountId     ID účtu
     */
    public Admin(int participantId, String username, int password, int accountId) {
        this.participantId = participantId;
        this.username = username;
        this.password = password;
        this.accountId = accountId;
    }

    List<String> activeEvents = new ArrayList<>(); // Zoznam aktívnych udalostí

    /**
     * Získa zoznam správcov.
     *
     * @return zoznam správcov
     */
    public List<Admin> getAdminList() {
        return adminList;
    }

    /**
     * Načíta správcov zo zoznamu a pridá ich do poľa správcov.
     *
     * @param admins zoznam správcov na načítanie
     */
    public void loadAdminsFromUser(List<Admin> admins) {
        adminList.addAll(admins);
    }

    public static Admin getAdminByUsernamePassword(String username, String userpassword) {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username)) {
                try {
                    int passwordInt = Integer.parseInt(userpassword);
                    if (passwordInt == admin.getPassword()) {
                        return admin;
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
        JOptionPane.showMessageDialog(null, "An admin with the specified name was not found.");
        return null;
    }

    // Ďalšie atribúty a metódy pre správcu

    /**
     * Získa ID účastníka.
     *
     * @return ID účastníka
     */
    public int getParticipantId() {
        return participantId;
    }

    /**
     * Nastaví ID účastníka.
     *
     * @param participantId ID účastníka
     */
    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    /**
     * Získa meno správcu.
     *
     * @return meno správcu
     */
    public String getUsername() {
        return username;
    }

    /**
     * Nastaví meno správcu.
     *
     * @param username meno správcu
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Získa heslo.
     *
     * @return heslo
     */
    public int getPassword() {
        return password;
    }

    /**
     * Nastaví heslo.
     *
     * @param password heslo
     */
    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * Získa ID účtu.
     *
     * @return ID účtu
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * Nastaví ID účtu.
     *
     * @param accountId ID účtu
     */
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    //private boolean hasActiveEvent = false; // Premenná na ukladanie informácií o prítomnosti aktívnej udalosti

    @Override
    public void displayAdminInterface() {
        // Vytvorenie zoznamu udalostí (tu je zatiaľ prázdny)
        List<Event> events = null;

        // Vytvorenie a zobrazenie rozhrania správcu
        SwingUtilities.invokeLater(() -> {
            JFrame adminFrame = new JFrame("Administrative interface");
            adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            adminFrame.setSize(400, 300);

            // Vytvorenie tlačidiel
            JButton createEventButton = new JButton("Create a new event");
            JButton subsetEventButton = new JButton("Add subset of events");
            JButton deleteEventButton = new JButton("Delete event");
            JButton manageParticipantsButton = new JButton("Manage participants");
            JButton viewReportsButton = new JButton("View winners");
            JButton activateButton = new JButton("Activate voting");
            JButton finishButton = new JButton("Finish voting");

            // Pridať poslucháčov tlačidiel
            createEventButton.addActionListener(e -> createEvent());
            subsetEventButton.addActionListener(e -> createsubsetEvent());
            deleteEventButton.addActionListener(e -> deleteEvent());
            manageParticipantsButton.addActionListener(e -> manageParticipants());
            viewReportsButton.addActionListener(e -> viewWinners());
            activateButton.addActionListener(e -> activateEvent());
            finishButton.addActionListener(e -> finishEvent());

            // Panel na umiestnenie tlačidiel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(4, 1));
            buttonPanel.add(createEventButton);
            buttonPanel.add(subsetEventButton);
            buttonPanel.add(deleteEventButton);
            buttonPanel.add(manageParticipantsButton);
            buttonPanel.add(viewReportsButton);
            buttonPanel.add(activateButton);
            buttonPanel.add(finishButton);

            // Pridanie panelu s tlačidlami do okna
            adminFrame.getContentPane().add(buttonPanel, BorderLayout.CENTER);


            // Zobrazovacie okno
            adminFrame.setVisible(true);
        });
    }

    private void createsubsetEvent() {
        // Vytvorenie registračného okna
        JFrame addSubsetEventFrame = new JFrame("Add Subset Event");
        addSubsetEventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Vytvorenie panelu pre okno
        JPanel addSubsetEventPanel = new JPanel();
        addSubsetEventPanel.setLayout(new BoxLayout(addSubsetEventPanel, BoxLayout.Y_AXIS));
        addSubsetEventPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subsEtevent_nameLabel = new JLabel("Title:");
        JTextField subsEtevent_nameField = new JTextField();
        subsEtevent_nameField.setPreferredSize(new Dimension(200, 30));

        // Nastavenie zarovnania komponentu na stred
        subsEtevent_nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subsEtevent_nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Pridanie komponentov na panel
        addSubsetEventPanel.add(subsEtevent_nameLabel);
        addSubsetEventPanel.add(subsEtevent_nameField);

        // Vytvorenie tlačidla pre registráciu
        JButton confirmButton = new JButton("Add");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Pridanie obsluhy udalosti pre tlačidlo registrácie
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Získajte hodnoty polí
                String poolName = subsEtevent_nameField.getText();
                int poolId = selectEventId(poolName); // Získanie ID vybranej udalosti
                addSubsetEventFrame.dispose();
            }
        });

        addSubsetEventPanel.add(confirmButton);
        addSubsetEventFrame.add(addSubsetEventPanel);
        addSubsetEventFrame.pack();
        addSubsetEventFrame.setLocationRelativeTo(null); // Vycentrovanie okna na obrazovke
        addSubsetEventFrame.setVisible(true);
    }


    // Metóda na výber ID udalosti
    private int selectEventId(String poolName) {
        JFrame selectEventIdFrame = new JFrame("Select Event");
        selectEventIdFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Event> availableEvents = Event.getAvailableEvents();
        JPanel eventsPanel = new JPanel(new GridLayout(availableEvents.size(), 1));

        int[] poolId = new int[1];  // Vytvorenie poľa na uloženie ID vybranej udalosti

        for (Event event : availableEvents) {
            JButton eventButton = new JButton("For " + event.getEventName());
            eventButton.addActionListener(e -> {
                poolId[0] = event.getEventId(); // Nastavenie ID vybranej udalosti
                selectEventIdFrame.dispose();

                VotingPool votingPool = new VotingPool(poolId[0], poolName);
                votingPool.loadNewVotingPoolFromAdmin(votingPool);
            });
            eventsPanel.add(eventButton);
        }

        selectEventIdFrame.add(eventsPanel);
        selectEventIdFrame.pack();
        selectEventIdFrame.setLocationRelativeTo(null);
        selectEventIdFrame.setVisible(true);

        return poolId[0]; // Vrátenie ID vybranej udalosti
    }

    // Metódy na spracovanie stlačení tlačidiel
    private void createEvent() {
        // Создаем окно регистрации
        JFrame addEventFrame = new JFrame("addEvent");
        addEventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Vytvorenie panelu pre okno
        JPanel addEventPanel = new JPanel();
        addEventPanel.setLayout(new BoxLayout(addEventPanel, BoxLayout.Y_AXIS));
        addEventPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Vytvorenie komponentov na zadávanie údajov o účte
        JLabel event_idLabel = new JLabel("Id:");
        JTextField event_idField = new JTextField();
        JLabel event_nameLabel = new JLabel("Title:");
        JTextField event_nameField = new JTextField();

        // Nastavenie veľkosti komponentov
        event_idField.setPreferredSize(new Dimension(200, 30));
        event_nameField.setPreferredSize(new Dimension(200, 30));

        event_idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        event_idField.setAlignmentX(Component.CENTER_ALIGNMENT);
        event_nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        event_nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        addEventPanel.add(event_idLabel);
        addEventPanel.add(event_idField);
        addEventPanel.add(event_nameLabel);
        addEventPanel.add(event_nameField);

        JButton confirmButton = new JButton("Add");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addEventPanel.add(confirmButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Получаем значения полей
                String event_idString = event_idField.getText();
                int event_id = Integer.parseInt(event_idString);
                String event_name = event_nameField.getText();

                Event event = new Event(event_id, event_name);
                event.loadNewEventFromAdmin(event);

                // Serializácia novej udalosti
                EventSerialization.serializeEvent(event, "event_" + event_id + ".ser");

                JOptionPane.showMessageDialog(addEventFrame, "Add successful!");

                addEventFrame.dispose();
            }
        });

        addEventFrame.add(addEventPanel);
        addEventFrame.pack();
        addEventFrame.setLocationRelativeTo(null);
        addEventFrame.setVisible(true);
    }

    private void manageParticipants() {
        JFrame manageParticipantsFrame = new JFrame("Participant management");
        manageParticipantsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1));

        JButton deleteParticipantButton = new JButton("Delete a member");
        deleteParticipantButton.addActionListener(e -> deleteParticipant());
        buttonsPanel.add(deleteParticipantButton);

        JButton changeParticipantIdButton = new JButton("Change participant ID");
        changeParticipantIdButton.addActionListener(e -> changeParticipantId());
        buttonsPanel.add(changeParticipantIdButton);

        manageParticipantsFrame.add(buttonsPanel);

        manageParticipantsFrame.pack();
        manageParticipantsFrame.setLocationRelativeTo(null);
        manageParticipantsFrame.setVisible(true);
    }

    private void deleteEvent() {
        JFrame deleteEventFrame = new JFrame();
        deleteEventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Event> availableEvents = Event.getAvailableEvents();

        System.out.println("Number of available Events: " + availableEvents.size());

        JPanel eventsPanel = new JPanel(new GridLayout(availableEvents.size(), 1));

        for (Event event : availableEvents) {
            JButton deleteEventButton = new JButton("Delete " + event.getEventName());
            deleteEventButton.addActionListener(e -> {
                availableEvents.remove(event);

                DatabaseManager.deleteEvent(event);

                // Odstránenie serializačného súboru udalosti
                File file = new File("event_" + event.getEventId() + ".ser");
                if (file.exists()) {
                    file.delete();
                }

                deleteEventFrame.dispose();
            });
            eventsPanel.add(deleteEventButton);
        }

        deleteEventFrame.add(eventsPanel);

        deleteEventFrame.pack();
        deleteEventFrame.setLocationRelativeTo(null);
        deleteEventFrame.setVisible(true);
    }

    private void deleteParticipant() {
        JFrame deleteParticipantFrame = new JFrame("Delete a member");
        deleteParticipantFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Participant> availableParticipants = Participant.getParticipantList();

        System.out.println("Number of available Participants: " +  availableParticipants.size());

        JPanel participantsPanel = new JPanel(new GridLayout(availableParticipants.size(), 1));

        // Add buttons for each participant
        for (Participant participant : availableParticipants) {
            JPanel participantPanel = new JPanel();
            JButton deleteButton = new JButton("Delete " + participant.getUsername());
            deleteButton.addActionListener(e -> {
                // Keď kliknete na tlačidlo na odstránenie účastníka
                // Odstránenie účastníka z poľa
                availableParticipants.remove(participant);
                // Odstránenie účastníka z databázy
                DatabaseManager.deleteParticipant(participant);
// Aktualizácia rozhrania (ak je to potrebné)


// Zatvorte okno výberu účastníka na vymazanie
                deleteParticipantFrame.dispose();
            });
            participantPanel.add(deleteButton);
            participantsPanel.add(participantPanel);
        }

        deleteParticipantFrame.add(participantsPanel);

        // Отображаем окно
        deleteParticipantFrame.pack();
        deleteParticipantFrame.setLocationRelativeTo(null);
        deleteParticipantFrame.setVisible(true);
    }

    private void changeParticipantId() {
        JFrame changeIdFrame = new JFrame("Change participant ID");
        changeIdFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Participant> availableParticipants = Participant.getParticipantList();

        JPanel participantsPanel = new JPanel(new GridLayout(availableParticipants.size(), 1));

        for (Participant participant : availableParticipants) {
            JPanel participantPanel = new JPanel();
            JButton changeIdButton = new JButton("Change participant ID " + participant.getUsername());
            changeIdButton.addActionListener(e -> {
                selectNewParticipantId(participant);
                changeIdFrame.dispose();
            });
            participantPanel.add(changeIdButton);
            participantsPanel.add(participantPanel);
        }

        changeIdFrame.add(participantsPanel);

        changeIdFrame.pack();
        changeIdFrame.setLocationRelativeTo(null);
        changeIdFrame.setVisible(true);
    }

    private int selectNewParticipantId(Participant participant) {
        JFrame selectEventFrame = new JFrame("Select an event for the participant " + participant.getUsername());
        selectEventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Získanie zoznamu dostupných udalostí pre účastníka
        List<Event> availableEvents = Event.getAvailableEvents();

        JPanel eventsPanel = new JPanel(new GridLayout(availableEvents.size(), 1));

        for (Event event : availableEvents) {
            JPanel eventPanel = new JPanel();
            JButton selectEventButton = new JButton("Select an event: " + event.getEventName());
            selectEventButton.addActionListener(e -> {
                int newParticipantId = event.getEventId();
                participant.changeParticipantId(participant.getParticipantId(),newParticipantId, event.getEventId());

                DatabaseManager.updateParticipantId(participant.getAccountId(),newParticipantId);
                selectEventFrame.dispose();
            });
            eventPanel.add(selectEventButton);
            eventsPanel.add(eventPanel);
        }

        selectEventFrame.add(eventsPanel);

        selectEventFrame.pack();
        selectEventFrame.setLocationRelativeTo(null);
        selectEventFrame.setVisible(true);
        return 0;
    }

    private void viewWinners() {
        JFrame viewWinnersFrame = new JFrame("View Winners");
        viewWinnersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel winnersPanel = new JPanel(new GridLayout(1, 1));

        String[] columnNames = {"Event ID", "Pool Name", "Vote Count"};

        // Vytvorenie tabuľky
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);

        LinkedList<VotingManager> winnersList = new LinkedList<>();
        VotingManager.loadWinnersFromDatabase();

        // Naplnenie tabuľky údajmi
        for (int i = VotingManager.getWinnersList().size() - 1; i >= 0; i--) {
            winnersList.add(VotingManager.getWinnersList().get(i));
        }

        List<VotingManager> winners = winnersList.toList();
        for (VotingManager winner : winners) {
            Object[] rowData = {winner.getEventId(), winner.getPoolName(), winner.getVoteCount()};
            model.addRow(rowData);
            System.out.println("Event ID: " + winner.getEventId() + ", Pool Name: " + winner.getPoolName() + ", Vote Count: " + winner.getVoteCount());
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        winnersPanel.add(scrollPane);

        viewWinnersFrame.add(winnersPanel);

        viewWinnersFrame.pack();
        viewWinnersFrame.setLocationRelativeTo(null);
        viewWinnersFrame.setVisible(true);
    }

    private void activateEvent() {
        JFrame activateEventFrame = new JFrame("Activate Event");
        activateEventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Event> availableEvents = Event.getAvailableEvents();

        JPanel eventsPanel = new JPanel(new GridLayout(availableEvents.size() + 1, 1));

        List<Event> selectedEvents = new ArrayList<>();

        for (Event event : availableEvents) {
            JCheckBox eventCheckBox = new JCheckBox(event.getEventName());
            eventCheckBox.setActionCommand(String.valueOf(event.getEventId()));
            eventsPanel.add(eventCheckBox);

            eventCheckBox.addItemListener(e -> {
                if (eventCheckBox.isSelected()) {
                    int eventId = Integer.parseInt(eventCheckBox.getActionCommand());
                    selectedEvents.add(getEventById(availableEvents, eventId));
                } else {
                    int eventId = Integer.parseInt(eventCheckBox.getActionCommand());
                    selectedEvents.remove(getEventById(availableEvents, eventId));
                }
            });
        }

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            if (!selectedEvents.isEmpty()) {
                System.out.println("Selected events:");
                for (Event event : selectedEvents) {
                    System.out.println(event.getEventName());
                    List<Participant> availableParticipants = Participant.getParticipantList();
                    for (Participant participant : availableParticipants) {
                        if (participant.getParticipantId() == event.getEventId()) {
                            participant.setActiveEvent(true);
                            notifyObservers(event.getEventId(), event.getEventName());
                        }

                    }
                }
                activateEventFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(activateEventFrame, "Please select at least one event.");
            }
        });
        eventsPanel.add(confirmButton);

        activateEventFrame.add(eventsPanel);

        activateEventFrame.pack();
        activateEventFrame.setLocationRelativeTo(null);
        activateEventFrame.setVisible(true);
    }

    private void finishEvent() {
        // Vytvorenie rámca pre ukončenie udalosti
        JFrame finishEventFrame = new JFrame("Finish Event");
        finishEventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Získanie dostupných udalostí
        List<Event> availableEvents = Event.getAvailableEvents();

        // Panel pre udalosti
        JPanel eventsPanel = new JPanel(new GridLayout(availableEvents.size() + 1, 1));

        // Zoznam vybraných udalostí
        List<Event> selectedEvents = new ArrayList<>();

        // Vytvorenie zaškrtávacích políčok pre každú udalosť
        for (Event event : availableEvents) {
            JCheckBox eventCheckBox = new JCheckBox(event.getEventName());
            eventCheckBox.setActionCommand(String.valueOf(event.getEventId()));
            eventsPanel.add(eventCheckBox);

            // Obsluha zmeny vo výbere udalostí
            eventCheckBox.addItemListener(e -> {
                if (eventCheckBox.isSelected()) {
                    int eventId = Integer.parseInt(eventCheckBox.getActionCommand());
                    selectedEvents.add(getEventById(availableEvents, eventId));
                } else {
                    int eventId = Integer.parseInt(eventCheckBox.getActionCommand());
                    selectedEvents.remove(getEventById(availableEvents, eventId));
                }
            });
        }

        // Tlačidlo na potvrdenie výberu udalostí
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            // Ak boli vybrané nejaké udalosti
            if (!selectedEvents.isEmpty()) {
                System.out.println("Selected events:");
                for (Event event : selectedEvents) {
                    System.out.println(event.getEventName());
                    // Získanie dostupných účastníkov
                    List<Participant> availableParticipants = Participant.getParticipantList();
                    for (Participant participant : availableParticipants) {
                        // Nastavenie ukončenia udalosti pre každého účastníka
                        if (participant.getParticipantId() == event.getEventId()) {
                            participant.setFinishEvent(false);
                            notifyObservers(event.getEventId(), event.getEventName());
                        }
                    }
                    // Určenie víťaza udalosti
                    determineWinnerForEvent(event);
                }
                // Zatvorenie okna ukončenia udalosti
                finishEventFrame.dispose();
            } else {
                // Ak nebola vybraná žiadna udalosť, zobrazenie upozornenia
                JOptionPane.showMessageDialog(finishEventFrame, "Select at least one event.");
            }
        });
        eventsPanel.add(confirmButton);

        // Pridanie panelu s udalosťami do rámca
        finishEventFrame.add(eventsPanel);

        // Nastavenie veľkosti rámca na základe obsahu
        finishEventFrame.pack();
        // Zobrazenie rámca na stred obrazovky
        finishEventFrame.setLocationRelativeTo(null);
        // Zobrazenie rámca
        finishEventFrame.setVisible(true);
    }


    private void determineWinnerForEvent(Event event) {
        // Vytvorenie prázdneho účastníka len s eventId pre určenie víťaza udalosti
        Participant participant = new Participant(-1, "", 0, event.getEventId(), false, false);
        // Určenie víťaza udalosti a získanie frekvenčnej mapy
        Map<VotingPool, Integer> frequencyMap = participant.determineWinnerInParallel(event.getEventId());
        // Uloženie informácií o víťazovi udalosti
        VotingManager.saveWinnerData(event.getEventId(), frequencyMap);
    }

    // Metóda na získanie objektu udalosti podľa jeho ID zo zoznamu dostupných udalostí
    private Event getEventById(List<Event> events, int eventId) {
        for (Event event : events) {
            if (event.getEventId() == eventId) {
                return event;
            }
        }
        return null;
    }


    @Override
    public void displayParticipantInterface() {
    }

    /**
     * Oznámiť pozorovateľom udalosť.
     *
     * @param accountId ID účastníka
     * @param eventInfo informácie o udalosti
     */
    @Override
    public void notifyObservers(int accountId, String eventInfo) {
        // Získať zoznam dostupných účastníkov
        List<Participant> availableParticipants = Participant.getParticipantList();
        // Prejsť cez každého účastníka
        for (Participant participant : availableParticipants) {
            // Ak sa ID účastníka zhoduje s predaným ID, spracovať udalosť
            if (participant.getParticipantId() == accountId) {
                participant.handleEvent(eventInfo);
            }
        }
    }
}
