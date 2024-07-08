import org.json.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;



public class EventServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        try{
            connection = DatabaseConnection.getConnection();
            PreparedStatement st = connection.prepareStatement("select * from events");
            ResultSet rs = st.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", rs.getInt("id"));
                jsonObject.put("name", rs.getString("name"));
                jsonObject.put("date", rs.getString("date"));
                jsonObject.put("location", rs.getString("location"));
                jsonArray.put(jsonObject);
            }
            out.print(jsonArray.toString());
        }catch(SQLException e){
            e.printStackTrace();
            out.print(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            out.print(e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eventName = request.getParameter("name");
        String eventdate = request.getParameter("date");
        String eventlocation = request.getParameter("location");
        PrintWriter out = response.getWriter();
        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO events(name,date,location) VALUES(?,?,?)");
            statement.setString(1, eventName);
            statement.setString(2, eventdate);
            statement.setString(3, eventlocation);

            int insertData = statement.executeUpdate();
            if (insertData == 1) {
                out.println("event_added_successfully");
            } else {
                out.println("error_while_adding_event");
            }
        }catch (SQLException e){
            e.printStackTrace();
//            out.println(e.getMessage());

        }catch (Exception e){

            e.printStackTrace();
//            out.println(e.getMessage());

        }finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eventIdStr = request.getParameter("id");

        int eventId = Integer.parseInt(eventIdStr);

        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM events WHERE id = ?");
            statement.setInt(1, eventId);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("event_deleted_successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Event not found with ID: " + eventId);
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error deleting event: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }
}