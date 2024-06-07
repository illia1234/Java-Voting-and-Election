import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda RegistrationForm reprezentuje registračný formulár v systéme.
 */
public class RegistrationForm {
    private static JButton confirmButton;
    public static void createAndShowGUI() {
        JFrame registrationFrame = new JFrame("Registration");
        registrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel registrationPanel = new JPanel();
        registrationPanel.setLayout(new BoxLayout(registrationPanel, BoxLayout.Y_AXIS));
        registrationPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel participant_typeLabel = new JLabel("Type:");

        JButton participant_typeAdminField = new JButton("admin");
        JButton participant_typeParticipantField = new JButton("participant");

        participant_typeAdminField.setPreferredSize(new Dimension(100, 40));
        participant_typeParticipantField.setPreferredSize(new Dimension(100, 40));

        participant_typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        participant_typeAdminField.setAlignmentX(Component.CENTER_ALIGNMENT);
        participant_typeParticipantField.setAlignmentX(Component.CENTER_ALIGNMENT);


        participant_typeAdminField.addActionListener(e -> {
            confirmButton.setEnabled(true);

            confirmButton.putClientProperty("participantType", "admin");
        });


        participant_typeParticipantField.addActionListener(e -> {
            confirmButton.setEnabled(true);

            confirmButton.putClientProperty("participantType", "participant");
        });

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        usernameField.setPreferredSize(new Dimension(200, 30));
        passwordField.setPreferredSize(new Dimension(200, 30));

        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmButton = new JButton("Register");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.setEnabled(false);

        confirmButton.addActionListener(e -> {
            String participantType = (String) confirmButton.getClientProperty("participantType");
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            int accountId = getLastAccountId() + 1;

            JOptionPane.showMessageDialog(registrationFrame, "Registration successful!");

            registrationFrame.dispose();

            displayAvailableEvents(accountId, participantType, username, password);
        });

        registrationPanel.add(participant_typeLabel);
        registrationPanel.add(participant_typeAdminField);
        registrationPanel.add(participant_typeParticipantField);
        registrationPanel.add(usernameLabel);
        registrationPanel.add(usernameField);
        registrationPanel.add(passwordLabel);
        registrationPanel.add(passwordField);
        registrationPanel.add(confirmButton);

        registrationFrame.add(registrationPanel);

        registrationFrame.pack();
        registrationFrame.setLocationRelativeTo(null);
        registrationFrame.setVisible(true);
    }

    private static int getLastAccountId() {
        int lastAccountId = 0;

        String url = "jdbc:mysql://127.0.0.1:3306/events";
        String ownerUsername = "root";
        String ownerPassword = "r110451006";

        String sql = "SELECT MAX(account_id) AS max_account_id FROM participants";

        try (Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                lastAccountId = resultSet.getInt("max_account_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastAccountId;
    }

    private static void displayAvailableEvents(int accountId, String participant_type, String username, String password) {
        int[] selectedEventId = new int[]{-1};

        JFrame eventsFrame = new JFrame("Available Events");
        eventsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Event> availableEvents = Event.getAvailableEvents();

        System.out.println("Number of available events: " + availableEvents.size());

        JPanel eventsPanel = new JPanel(new GridLayout(availableEvents.size(), 1));

        for (Event event : availableEvents) {
            JPanel eventPanel = new JPanel();
            JButton choiceButton = new JButton(event.getEventName() + " " + event.getEventId());
            choiceButton.setActionCommand(String.valueOf(event.getEventId()));
            choiceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String eventIdStr = choiceButton.getActionCommand();
                    selectedEventId[0] = Integer.parseInt(eventIdStr);
                }
            });
            eventPanel.add(choiceButton);

            JLabel eventIdLabel = new JLabel(String.valueOf(event.getEventId()));
            eventPanel.add(eventIdLabel);

            eventsPanel.add(eventPanel);
        }

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedEventId[0] != -1) {
                    saveSelectedEvents(accountId, participant_type, username, Integer.parseInt(password), selectedEventId[0]);

                    JOptionPane.showMessageDialog(eventsFrame, "Selected event saved successfully!");
                    eventsFrame.dispose();
                } else {

                    JOptionPane.showMessageDialog(eventsFrame, "Please select an event before confirming.");
                }
            }
        });

        eventsFrame.add(eventsPanel, BorderLayout.CENTER);
        eventsFrame.add(confirmButton, BorderLayout.SOUTH);

        eventsFrame.pack();
        eventsFrame.setLocationRelativeTo(null);
        eventsFrame.setVisible(true);
    }

    private static void saveSelectedEvents(int accountId, String participant_type, String username, int password, int selectedEventId) {
        String url = "jdbc:mysql://127.0.0.1:3306/events";
        String ownerUsername = "root";
        String ownerPassword = "r110451006";

        try {
            Connection connection = DriverManager.getConnection(url, ownerUsername, ownerPassword);

            String sql = "INSERT INTO participants (participant_type, username, password, account_id, participant_id, event_state, voting_state) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, participant_type);
                statement.setString(2, username);
                statement.setInt(3, password);
                statement.setInt(4, accountId);
                statement.setInt(5, selectedEventId);
                statement.setString(6, "false");
                statement.setString(7, "true");

                statement.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}