public class Booking {
    private String eventName;
    private String userName;
    private String address;
    private String phoneNumber;

    public Booking(String eventName, String userName, String address, String phoneNumber) {
        this.eventName = eventName;
        this.userName = userName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getEventName() {
        return eventName;
    }

    public String getUserName() {
        return userName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return userName + " booked " + eventName;
    }
}
