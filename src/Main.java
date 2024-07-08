import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Fetch data from the database
        List<Event> events = fetchEventsFromDatabase();
        List<Booking> bookings = fetchBookingsFromDatabase();

        // Create the UI
        SwingUtilities.invokeLater(() -> new EventManagementUI(events, bookings));
    }

    private static List<Event> fetchEventsFromDatabase() {
        List<Event> events = new ArrayList<>();

        try{
            String response = APIController.getData("http://localhost:8080/eventbooking/api/events","GET");
            JSONArray eventsJson = new JSONArray(response);
            for(int i =0; i<eventsJson.length();i++){
                JSONObject eventItem = eventsJson.getJSONObject(i);

                Event event = new Event(eventItem.getString("name"),eventItem.getString("date"),eventItem.getString("location"));
                events.add(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }

    private static List<Booking> fetchBookingsFromDatabase() {
        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            String query = "SELECT event_name, user_name, address, phone_number FROM bookings";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String eventName = resultSet.getString("event_name");
                String userName = resultSet.getString("user_name");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");
                bookings.add(new Booking(eventName, userName, address, phoneNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
}
