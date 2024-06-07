import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda reprezentujúca používateľa aplikácie.
 */
public class User implements UserInterface {
    private List<Admin> admins;
    private List<Participant> participants;
    private List<User> users;

    /**
     * Konštruktor pre triedu User.
     */
    public User() {
        this.admins = new ArrayList<>();
        this.participants = new ArrayList<>();
        this.users = new ArrayList<>();
    }


    /**
     * Metóda načítava používateľov z databázy.
     */
    public void loadUsersFromDatabase() {

        String url = "jdbc:mysql://127.0.0.1:3306/events";
        String ownerUsername = "root";
        String ownerPassword = "r110451006";

        try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword)) {

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM participants")) {
                while (resultSet.next()) {
                    int participantId = resultSet.getInt("participant_id");
                    String participantType = resultSet.getString("participant_type");
                    String username = resultSet.getString("username");
                    int password = resultSet.getInt("password");
                    int accountId = resultSet.getInt("account_id");
                    boolean hasActiveEvent = resultSet.getBoolean("event_state");
                    boolean votingState = resultSet.getBoolean("voting_state");

                    Admin admin = new Admin(participantId, username, password, accountId);
                    Participant participant = new Participant(participantId, username, password, accountId,hasActiveEvent, votingState);
                    User user = new User();

                    switch (participantType) {
                        case "admin":
                            admins.add(admin);
                            break;
                        case "participant":
                            participants.add(participant);
                            break;
                        default:
                            continue;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Načítanie správcov z objektu User do poľa správcov
        for (Admin admin : admins) {
            admin.loadAdminsFromUser(admins);
        }

        // Načítanie účastníkov z objektu User do poľa účastníkov
        for (Participant participant : participants) {
            participant.loadParticipantsFromUser(participants);
        }
    }

    /**
     * Metóda vráti zoznam správcov.
     *
     * @return Zoznam správcov.
     */
    public List<Admin> getAdmins() {
        return admins;
    }

    /**
     * Metóda vráti zoznam účastníkov.
     *
     * @return Zoznam účastníkov.
     */
    public List<Participant> getParticipants() {
        return participants;
    }


    @Override
    public void displayAdminInterface() {
    }

    @Override
    public void displayParticipantInterface() {

    }

    private static List<Participant> participantDataList = new ArrayList<>();

    private static JTextArea participantInfoTextArea; // TextArea pre výpis informácií o účastníkoch

    public static void displayUsertInterface() {
            JFrame frame = new JFrame("CitizenPulse");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel usernameLabel = new JLabel("User Name:");
            JTextArea usernameField = new JTextArea(1, 10);
            JLabel passwordLabel = new JLabel("Password:");
            JPasswordField passwordField = new JPasswordField(10);

            usernameLabel.setBorder(new EmptyBorder(40, 0, 10, 0));
            passwordLabel.setBorder(new EmptyBorder(40, 0, 10, 0));

            GUIUtils.setComponentSize(usernameField, 300, 50);
            GUIUtils.setComponentSize(passwordField, 300, 50);

            usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
            passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(usernameLabel);
            panel.add(usernameField);
            panel.add(passwordLabel);
            panel.add(passwordField);

            JButton loginButton = new JButton("Login");
            JButton registerButton = new JButton("Registration");
            loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            GUIUtils.setComponentSize(loginButton, 100, 50);
            GUIUtils.setComponentSize(registerButton, 100, 50);

            panel.add(loginButton);
            panel.add(registerButton);

        // Vytvorenie textovej oblasti a nastavenie oblasti posúvania
            participantInfoTextArea = new JTextArea(10, 40);
            JScrollPane scrollPane = new JScrollPane(participantInfoTextArea);


            JLabel eventsStatusLabel = new JLabel("Events status");
            eventsStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(eventsStatusLabel);
            panel.add(scrollPane);

            class LoginActionListener implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());

                    // Skontrolujte, či sú vyplnené obe polia
                    if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please fill in both fields.");
                        return;
                    }

                        User user = new User();
                        user.loadUsersFromDatabase();

                        user = getUser(username,password);

                        if (user != null) {
                            if (user instanceof Participant) {
                                Participant participant = (Participant) user;

                                System.out.println("ID participant: " + participant.getParticipantId());
                                System.out.println("Participant's name: " + participant.getUsername());

                                participant.displayParticipantInterface();

                            } else if (user instanceof Admin) {
                                Admin admin = (Admin) user;

                                System.out.println("ID administrator : " + admin.getParticipantId());
                                System.out.println("Administrator's name: " + admin.getUsername());

                                admin.displayAdminInterface();
                            }
                        }
                }

                private User getUser(String username, String password) {
                    Participant participant = Participant.getParticipantByUsernamePassword(username,password);
                    if (participant != null) {
                        return participant;
                    }

                    Admin admin = Admin.getAdminByUsernamePassword(username,password);
                    if (admin != null) {
                        return admin;
                    }

                    return null;
                }
            }

            loginButton.addActionListener(new LoginActionListener());

            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RegistrationForm.createAndShowGUI();
                }
            });

            frame.add(panel);

            frame.setVisible(true);
        }

        public static void updateParticipantData(Participant new_participant) {
            boolean participantExists = false;
            for (Participant participant : participantDataList) {
                if (participant.getUsername() == new_participant.getUsername()) {
                    participantExists = true;
                    break;
                }
            }
            if (!participantExists) {
                participantDataList.add(new_participant);
            }
            // Výstupné informácie o stave udalosti pre každého účastníka
            participantInfoTextArea.setText(""); // Vyčistite textovú oblasť pred pridaním nových informácií
            List<Event> eventList = Event.getAvailableEvents();
            for (Participant participant : participantDataList) {
                String statusMessage = "Participant " + participant.getUsername() + " with accountId " + participant.getAccountId();
                if (participant.getHasActiveEvent() == true) {
                    for (Event event : eventList) {
                        if (participant.getParticipantId() == event.getEventId()) {
                            statusMessage += " has active event: " + event.getEventName();
                            break; // Prerušíme cyklus, keď sa nájde príslušná udalosť
                        }
                    }
                } else {
                    for (Event event : eventList) {
                        if (participant.getParticipantId() == event.getEventId()) {
                            statusMessage += " has not active event: " + event.getEventName();
                            break; // Prerušenie cyklu po nájdení príslušnej udalosti
                        }
                    }
                }
                participantInfoTextArea.append(statusMessage + "\n"); // Pridáme správu o stave udalosti pre aktuálneho účastníka
            }
        }
}
