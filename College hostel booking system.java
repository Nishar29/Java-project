import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.*;

class HostelBookingSystem extends Frame {
    private Label titleLabel, usernameLabel, nameLabel, contactLabel, durationLabel, roomLabel, paymentLabel;
    private TextField usernameField, nameField, contactField, durationField, roomField, paymentField;
    private Button bookButton, viewButton, loginButton, adminButton, logoutButton, releaseButton, closeButton;
    private TextArea outputArea;
    private Panel contentPanel;

    private Map<String, Boolean> rooms = new LinkedHashMap<>();
    private String adminUsername = "admin";
    private String adminPassword = "admin123";
    private boolean isAdminLoggedIn = false;
    private boolean isUserLoggedIn = false;

    public HostelBookingSystem() {
        // Initialize frame
        setTitle("College Hostel Booking System");
        setSize(600, 700);
        setLayout(new BorderLayout());
        setResizable(false);

        // Set background color
        contentPanel = new Panel();
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBackground(new Color(230, 240, 255)); // Mild blue

        // Initialize room availability
        for (int i = 1; i <= 10; i++) {
            rooms.put("Room " + i, true);
        }

        // Title Label
        titleLabel = new Label("Welcome to Hostel Booking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // User Interface Components
        usernameLabel = new Label("Username:");
        usernameField = new TextField(20);

        nameLabel = new Label("Name:");
        nameField = new TextField(20);

        contactLabel = new Label("Contact:");
        contactField = new TextField(20);

        durationLabel = new Label("Duration (days):");
        durationField = new TextField(20);

        roomLabel = new Label("Room No:");
        roomField = new TextField(20);

        paymentLabel = new Label("Payment Amount:");
        paymentField = new TextField(20);

        bookButton = new Button("Book Room");
        bookButton.addActionListener(e -> bookRoom());

        viewButton = new Button("View Available Rooms");
        viewButton.addActionListener(e -> displayAvailableRooms());

        closeButton = new Button("Close");
        closeButton.addActionListener(e -> System.exit(0)); // Closes the application

        outputArea = new TextArea(20, 50);

        loginButton = new Button("Login as User");
        loginButton.addActionListener(e -> userLogin());

        adminButton = new Button("Login as Admin");
        adminButton.addActionListener(e -> adminLogin());

        logoutButton = new Button("Logout");
        logoutButton.addActionListener(e -> logout());

        releaseButton = new Button("Release Room");
        releaseButton.addActionListener(e -> releaseRoom());

        // Adding Components to Panel
        contentPanel.add(titleLabel);
        contentPanel.add(usernameLabel);
        contentPanel.add(usernameField);
        contentPanel.add(loginButton);
        contentPanel.add(adminButton);
        contentPanel.add(logoutButton);
        contentPanel.add(releaseButton);

        contentPanel.add(nameLabel);
        contentPanel.add(nameField);

        contentPanel.add(contactLabel);
        contentPanel.add(contactField);

        contentPanel.add(durationLabel);
        contentPanel.add(durationField);

        contentPanel.add(roomLabel);
        contentPanel.add(roomField);

        contentPanel.add(paymentLabel);
        contentPanel.add(paymentField);

        contentPanel.add(bookButton);
        contentPanel.add(viewButton);
        contentPanel.add(closeButton);
        contentPanel.add(outputArea);

        // Add panel to frame
        add(contentPanel, BorderLayout.CENTER);

        // Handle window closing
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void userLogin() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            outputArea.setText("Username is required to log in as a user.\n");
            return;
        }

        outputArea.setText("User Login Successful. Welcome, " + username + "!\n");
        isUserLoggedIn = true;

        // Clear all fields for manual entry
        nameField.setText("");
        contactField.setText("");
        durationField.setText("");
        roomField.setText("");
        paymentField.setText("");

        outputArea.append("Please enter your booking details .\n");
    }

    private void adminLogin() {
        String username = getInput("Enter Admin Username:");
        String password = getInput("Enter Admin Password:");
        if (username.equals(adminUsername) && password.equals(adminPassword)) {
            isAdminLoggedIn = true;
            outputArea.setText("Admin Login Successful.\nRooms Status:\n");
            displayAllRooms();
        } else {
            outputArea.setText("Invalid Admin Credentials!\n");
        }
    }

    private void displayAllRooms() {
        for (Map.Entry<String, Boolean> room : rooms.entrySet()) {
            outputArea.append(room.getKey() + ": " + (room.getValue() ? "Available" : "Booked") + "\n");
        }
    }

    private void displayAvailableRooms() {
        outputArea.setText("Available Rooms:\n");
        boolean available = false;
        for (Map.Entry<String, Boolean> room : rooms.entrySet()) {
            if (room.getValue()) { // Check if the room is available
                outputArea.append(room.getKey() + "\n");
                available = true;
            }
        }
        if (!available) {
            outputArea.append("No rooms are available at the moment.\n");
        }
    }

    private void bookRoom() {
        if (!isUserLoggedIn) {
            outputArea.setText("Please log in as a user to book a room.\n");
            return;
        }

        String name = nameField.getText();
        String contact = contactField.getText();
        String durationStr = durationField.getText();
        String room = roomField.getText();
        String payment = paymentField.getText();

        if (name.isEmpty() || contact.isEmpty() || durationStr.isEmpty() || room.isEmpty() || payment.isEmpty()) {
            outputArea.setText("All fields are required!\n");
            return;
        }

        try {
            int duration = Integer.parseInt(durationStr);

            if (rooms.containsKey(room) && rooms.get(room)) {
                LocalDate checkInDate = LocalDate.now();
                LocalDate checkOutDate = checkInDate.plusDays(duration);

                rooms.put(room, false); // Mark room as booked
                outputArea.setText("Room " + room + " booked successfully for " + name + ".\n");
                outputArea.append("Contact: " + contact + "\n");
                outputArea.append("Check-in: " + checkInDate + "\n");
                outputArea.append("Check-out: " + checkOutDate + "\n");
                outputArea.append("Payment: " + payment + "\n");
            } else {
                outputArea.setText("Room " + room + " is not available. Try another room.\n");
            }
        } catch (NumberFormatException e) {
            outputArea.setText("Duration must be a valid number!\n");
        }
    }

    private void logout() {
        isAdminLoggedIn = false;
        isUserLoggedIn = false;
        outputArea.setText("You have been logged out.\n");
    }

    private void releaseRoom() {
        if (!isAdminLoggedIn) {
            outputArea.setText("Only admin can release rooms. Please log in as admin.\n");
            return;
        }
        String room = getInput("Enter room number to release:");
        if (rooms.containsKey(room) && !rooms.get(room)) {
            rooms.put(room, true);
            outputArea.setText("Room " + room + " has been successfully released and is now available.\n");
        } else {
            outputArea.setText("Room " + room + " is either already available or does not exist.\n");
        }
    }

    private String getInput(String prompt) {
        return javax.swing.JOptionPane.showInputDialog(this, prompt);
    }

    public static void main(String[] args) {
        new HostelBookingSystem();
    }
}
