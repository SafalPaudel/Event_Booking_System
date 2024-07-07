import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EventManagementUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel eventTableModel;
    private DefaultListModel<Booking> bookingListModel;
    private JTextField nameField;
    private JTextField dateField;
    private JTextField locationField;
    private JTable eventTable;
    private JList<Booking> bookingList;
    private JTextField userNameField;
    private JTextField addressField;
    private JTextField phoneNumberField;
    private JComboBox<String> eventComboBox;

    public EventManagementUI(List<Event> events, List<Booking> bookings) {
        setTitle("Event Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create event table model
        eventTableModel = new DefaultTableModel(new Object[]{"Name", "Date", "Location"}, 0);
        for (Event event : events) {
            eventTableModel.addRow(new Object[]{event.getName(), event.getDate(), event.getLocation()});
        }

        bookingListModel = new DefaultListModel<>();
        for (Booking booking : bookings) {
            bookingListModel.addElement(booking);
        }

        // Create panels for different views
        JPanel startPanel = createStartPanel();
        JPanel addEventPanel = createAddEventPanel();
        JPanel bookEventPanel = createBookEventPanel(events); // Pass events to bookEventPanel
        JPanel viewBookingsPanel = createViewBookingsPanel();
        JPanel viewEventsPanel = createViewEventsPanel();

        mainPanel.add(startPanel, "start");
        mainPanel.add(addEventPanel, "addEvent");
        mainPanel.add(bookEventPanel, "bookEvent");
        mainPanel.add(viewBookingsPanel, "viewBookings");
        mainPanel.add(viewEventsPanel, "viewEvents");

        add(mainPanel);
        cardLayout.show(mainPanel, "start");

        setVisible(true);
    }

    private JPanel createStartPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        eventTable = new JTable(eventTableModel);
        JScrollPane scrollPane = new JScrollPane(eventTable);

        JButton bookEventButton = new JButton("Book Event");
        bookEventButton.setPreferredSize(new Dimension(100, 30));
        bookEventButton.addActionListener(e -> cardLayout.show(mainPanel, "bookEvent"));

        JButton viewBookingsButton = new JButton("View All Bookings");
        viewBookingsButton.setPreferredSize(new Dimension(100, 30));
        viewBookingsButton.addActionListener(e -> cardLayout.show(mainPanel, "viewBookings"));

        JButton viewEventsButton = new JButton("View All Events");
        viewEventsButton.setPreferredSize(new Dimension(100, 30));
        viewEventsButton.addActionListener(e -> cardLayout.show(mainPanel, "viewEvents"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(bookEventButton);
        buttonPanel.add(viewBookingsButton);
        buttonPanel.add(viewEventsButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAddEventPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Add New Event"));

        nameField = new JTextField();
        dateField = new JTextField();
        locationField = new JTextField();

        panel.add(new JLabel("Event Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Event Date:"));
        panel.add(dateField);
        panel.add(new JLabel("Event Location:"));
        panel.add(locationField);

        JButton addButton = new JButton("Add Event");
        addButton.setPreferredSize(new Dimension(100, 30));
        addButton.addActionListener(new AddButtonListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        panel.add(buttonPanel);

        return panel;
    }


    private JPanel createBookEventPanel(List<Event> events) {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Book Event"));

        eventComboBox = new JComboBox<>();
        for (Event event : events) {
            eventComboBox.addItem(event.getName());
        }

        userNameField = new JTextField();
        addressField = new JTextField();
        phoneNumberField = new JTextField();

        panel.add(new JLabel("Select Event:"));
        panel.add(eventComboBox);
        panel.add(new JLabel("User Name:"));
        panel.add(userNameField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneNumberField);

        JButton bookButton = new JButton("Book Event");
        bookButton.setPreferredSize(new Dimension(100, 30));
        bookButton.addActionListener(new BookButtonListener());

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "start"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);

        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createViewBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("All Bookings"));

        // Define table model with column names
        String[] columnNames = {"Event Name", "User Name", "Address", "Phone Number"};
        DefaultTableModel bookingsTableModel = new DefaultTableModel(columnNames, 0);

        // Populate table model with existing bookings
        for (int i = 0; i < bookingListModel.size(); i++) {
            Booking booking = bookingListModel.getElementAt(i);
            bookingsTableModel.addRow(new Object[]{booking.getEventName(), booking.getUserName(),
                    booking.getAddress(), booking.getPhoneNumber()});
        }

        JTable bookingsTable = new JTable(bookingsTableModel);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "start"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createViewEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("All Events"));

        eventTable = new JTable(eventTableModel);
        JScrollPane scrollPane = new JScrollPane(eventTable);

        JPanel addEventPanel = createAddEventPanel();

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "start"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(addEventPanel, BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String date = dateField.getText();
            String location = locationField.getText();

            if (name.isEmpty() || date.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(EventManagementUI.this,
                        "Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Event event = new Event(name, date, location);
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO events (name, date, location) VALUES (?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, event.getName());
                    statement.setString(2, event.getDate());
                    statement.setString(3, event.getLocation());
                    statement.executeUpdate();
                }
                eventTableModel.addRow(new Object[]{event.getName(), event.getDate(), event.getLocation()});
                nameField.setText("");
                dateField.setText("");
                locationField.setText("");
                eventComboBox.addItem(event.getName());
                JOptionPane.showMessageDialog(EventManagementUI.this,
                        "Event added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(EventManagementUI.this,
                        "Failed to add the event.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class BookButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String eventName = (String) eventComboBox.getSelectedItem();
            String userName = userNameField.getText();
            String address = addressField.getText();
            String phoneNumber = phoneNumberField.getText();

            if (!userName.isEmpty() && !address.isEmpty() && !phoneNumber.isEmpty()) {
                // Check if the event is already booked
                if (isEventAlreadyBooked(eventName)) {
                    JOptionPane.showMessageDialog(EventManagementUI.this,
                            "The event '" + eventName + "' is already booked.", "Booking Failed",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    bookEvent(eventName, userName, address, phoneNumber);
                }
            } else {
                JOptionPane.showMessageDialog(EventManagementUI.this,
                        "Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        }

        private boolean isEventAlreadyBooked(String eventName) {
            // Implement logic to check if the event is already booked
            for (int i = 0; i < bookingListModel.size(); i++) {
                Booking booking = bookingListModel.getElementAt(i);
                if (booking.getEventName().equals(eventName)) {
                    return true;
                }
            }
            return false;
        }

        private void bookEvent(String eventName, String userName, String address, String phoneNumber) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO bookings (event_name, user_name, address, phone_number) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, eventName);
                    statement.setString(2, userName);
                    statement.setString(3, address);
                    statement.setString(4, phoneNumber);
                    statement.executeUpdate();
                }
                bookingListModel.addElement(new Booking(eventName, userName, address, phoneNumber));
                userNameField.setText("");
                addressField.setText("");
                phoneNumberField.setText("");
                JOptionPane.showMessageDialog(EventManagementUI.this,
                        "Event booked successfully!", "Booking Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(EventManagementUI.this,
                        "Failed to book the event.", "Booking Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}


