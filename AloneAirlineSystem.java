import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AloneAirlineSystem {
    private final Map<String, Customer> customers = new HashMap<>();
    private final List<Ticket> tickets = new ArrayList<>();
    private final List<Flight> flights = new ArrayList<>();

    // Validate methods
    public static boolean validateCCCD(String cccd) {
        return cccd.matches("^036\\d{9}$");
    }

    public static boolean validatePhone(String phone) {
        return phone.matches("^09\\d{8}$");
    }

    public static boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean validatePassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$");
    }

    public static boolean validateFlightNumber(String flightNumber) {
        return flightNumber.matches("^[A-Z]{2}\\d{3}$");
    }

    public static boolean validateDateTime(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setLenient(false);
        try {
            sdf.parse(dateTime);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean validateDateOfBirth(String dob) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dob);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Register customer
    public void registerCustomer(String name, String cccd, String phone, String email, String password) throws IOException {
        if (!validateCCCD(cccd) || !validatePhone(phone) || !validateEmail(email) || !validatePassword(password)) {
            System.out.println("Invalid input. Registration failed.");
            return;
        }

        Customer customer = new Customer(name, cccd, phone, email, password);
        customers.put(phone, customer);

        try (PrintWriter personWriter = new PrintWriter(new FileWriter("person.txt", true));
             PrintWriter passWriter = new PrintWriter(new FileWriter("passCustomer.txt", true))) {
            personWriter.println(name + "," + cccd + "," + email);
            passWriter.println(phone + "," + password);
        }

        System.out.println("Customer registered successfully.");
    }

    // Login customer
    public boolean login(String phone, String password) {
        Customer customer = customers.get(phone);
        return customer != null && customer.getPassword().equals(password);
    }

    // Add a flight
    public void addFlight(String flightNumber, String departure, String arrival, String departureTime, String arrivalTime) {
        if (!validateFlightNumber(flightNumber) || !validateDateTime(departureTime) || !validateDateTime(arrivalTime)) {
            System.out.println("Invalid flight information.");
            return;
        }

        Flight flight = new Flight(flightNumber, departure, arrival, departureTime, arrivalTime);
        flights.add(flight);

        System.out.println("Flight added successfully.");
    }

    // Book ticket
    public void bookTicket(String flightNumber, String departure, String arrival, String departureTime, String arrivalTime, double price, String ticketType, String classType) throws IOException {
        if (!validateFlightNumber(flightNumber) || !validateDateTime(departureTime) || !validateDateTime(arrivalTime)) {
            System.out.println("Invalid flight information.");
            return;
        }

        Ticket ticket = new Ticket(flightNumber, departure, arrival, departureTime, arrivalTime, price, ticketType, classType);
        tickets.add(ticket);

        try (PrintWriter ticketWriter = new PrintWriter(new FileWriter("listtickets.csv", true))) {
            ticketWriter.println(ticket.toString());
        }

        System.out.println("Ticket booked successfully. Please arrive at least 30 minutes to 1 hour before departure.");
    }

    // Modify ticket
    public void modifyTicket(int index, Ticket newTicket) throws IOException {
        if (index < 0 || index >= tickets.size()) {
            System.out.println("Invalid ticket index.");
            return;
        }

        tickets.set(index, newTicket);
        saveTicketsToFile();
        System.out.println("Ticket modified successfully.");
    }

    // Delete ticket
    public void deleteTicket(int index) throws IOException {
        if (index < 0 || index >= tickets.size()) {
            System.out.println("Invalid ticket index.");
            return;
        }

        tickets.remove(index);
        saveTicketsToFile();
        System.out.println("Ticket deleted successfully.");
    }

    // Display tickets
    public void displayTickets() {
        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return;
        }

        for (int i = 0; i < tickets.size(); i++) {
            System.out.println("Ticket " + (i + 1) + ": " + tickets.get(i));
        }
    }

    // Display flights
    public void displayFlights() {
        if (flights.isEmpty()) {
            System.out.println("No flights available.");
            return;
        }

        for (int i = 0; i < flights.size(); i++) {
            System.out.println("Flight " + (i + 1) + ": " + flights.get(i));
        }
    }

    // Load flights from file
    private void loadFlightsFromFile() throws IOException {
        File file = new File("flights.csv");
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    flights.add(new Flight(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        }
    }

    // Load tickets from file
    private void loadTicketsFromFile() throws IOException {
        File file = new File("listtickets.csv");
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    tickets.add(new Ticket(parts[0], parts[1], parts[2], parts[3], parts[4], Double.parseDouble(parts[5]), parts[6], parts[7]));
                }
            }
        }
    }

    // Save flights to file
    private void saveFlightsToFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("flights.csv"))) {
            for (Flight flight : flights) {
                writer.println(flight.toString());
            }
        }
    }

    // Save tickets to file
    private void saveTicketsToFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("listtickets.csv"))) {
            for (Ticket ticket : tickets) {
                writer.println(ticket.toString());
            }
        }
    }

    // Load customers from file
    private void loadCustomersFromFile() throws IOException {
        File personFile = new File("person.txt");
        File passFile = new File("passCustomer.txt");

        if (!personFile.exists() || !passFile.exists()) {
            return;
        }

        try (BufferedReader personBr = new BufferedReader(new FileReader(personFile));
             BufferedReader passBr = new BufferedReader(new FileReader(passFile))) {
            String personLine;
            String passLine;

            while ((personLine = personBr.readLine()) != null && (passLine = passBr.readLine()) != null) {
                String[] personParts = personLine.split(",");
                String[] passParts = passLine.split(",");
                if (personParts.length == 3 && passParts.length == 2) {
                    String phone = passParts[0];
                    String password = passParts[1];
                    customers.put(phone, new Customer(personParts[0], personParts[1], phone, personParts[2], password));
                }
            }
        }
    }

    public AloneAirlineSystem() throws IOException {
        loadCustomersFromFile();
        loadFlightsFromFile();
        loadTicketsFromFile();
    }
}
