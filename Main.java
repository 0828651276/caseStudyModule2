import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        AloneAirlineSystem system = new AloneAirlineSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Book Ticket");
            System.out.println("4. Modify Ticket");
            System.out.println("5. Delete Ticket");
            System.out.println("6. Display Tickets");
            System.out.println("7. Add Flight");
            System.out.println("8. Display Flights");
            System.out.println("9. Exit");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (option) {
                case 1:
                    // Register new customer
                    System.out.print("Name: ");
                    String name = scanner.nextLine();

                    String cccd;
                    do {
                        System.out.print("CCCD: ");
                        cccd = scanner.nextLine();
                    } while (!AloneAirlineSystem.validateCCCD(cccd));

                    String phone;
                    do {
                        System.out.print("Phone: ");
                        phone = scanner.nextLine();
                    } while (!AloneAirlineSystem.validatePhone(phone));

                    String email;
                    do {
                        System.out.print("Email: ");
                        email = scanner.nextLine();
                    } while (!AloneAirlineSystem.validateEmail(email));

                    String password;
                    do {
                        System.out.print("Password: ");
                        password = scanner.nextLine();
                    } while (!AloneAirlineSystem.validatePassword(password));

                    system.registerCustomer(name, cccd, phone, email, password);
                    break;

                case 2:
                    // Login
                    System.out.print("Phone: ");
                    String loginPhone = scanner.nextLine();
                    System.out.print("Password: ");
                    String loginPassword = scanner.nextLine();
                    if (system.login(loginPhone, loginPassword)) {
                        System.out.println("Login successful.");
                    } else {
                        System.out.println("Invalid phone or password.");
                    }
                    break;

                case 3:
                    // Book a ticket
                    system.displayFlights();
                    System.out.print("Select Flight Number: ");
                    String flightNumber = scanner.nextLine();

                    System.out.print("Departure: ");
                    String departure = scanner.nextLine();
                    System.out.print("Arrival: ");
                    String arrival = scanner.nextLine();

                    String departureTime;
                    do {
                        System.out.print("Departure Time (yyyy-MM-dd HH:mm): ");
                        departureTime = scanner.nextLine();
                    } while (!AloneAirlineSystem.validateDateTime(departureTime));

                    String arrivalTime;
                    do {
                        System.out.print("Arrival Time (yyyy-MM-dd HH:mm): ");
                        arrivalTime = scanner.nextLine();
                    } while (!AloneAirlineSystem.validateDateTime(arrivalTime));

                    System.out.print("Ticket Type (Round-trip/One-way): ");
                    String ticketType = scanner.nextLine();

                    System.out.print("Class Type (Economy/Business/First Class): ");
                    String classType = scanner.nextLine();

                    double price = 0;
                    if (ticketType.equalsIgnoreCase("Round-trip")) {
                        if (classType.equalsIgnoreCase("Economy")) {
                            price = 3600000;
                        } else if (classType.equalsIgnoreCase("Business")) {
                            price = 4500000;
                        } else if (classType.equalsIgnoreCase("First Class")) {
                            price = 9000000;
                        }
                    } else if (ticketType.equalsIgnoreCase("One-way")) {
                        if (classType.equalsIgnoreCase("Economy")) {
                            price = 2000000;
                        } else if (classType.equalsIgnoreCase("Business")) {
                            price = 2500000;
                        } else if (classType.equalsIgnoreCase("First Class")) {
                            price = 4799000;
                        }
                    }

                    system.bookTicket(flightNumber, departure, arrival, departureTime, arrivalTime, price, ticketType, classType);
                    break;

                case 4:
                    // Modify a ticket
                    system.displayTickets();
                    System.out.print("Enter ticket index to modify: ");
                    int modifyIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    System.out.print("New Flight Number: ");
                    String newFlightNumber = scanner.nextLine();
                    System.out.print("New Departure: ");
                    String newDeparture = scanner.nextLine();
                    System.out.print("New Arrival: ");
                    String newArrival = scanner.nextLine();

                    String newDepartureTime;
                    do {
                        System.out.print("New Departure Time (yyyy-MM-dd HH:mm): ");
                        newDepartureTime = scanner.nextLine();
                    } while (!AloneAirlineSystem.validateDateTime(newDepartureTime));

                    String newArrivalTime;
                    do {
                        System.out.print("New Arrival Time (yyyy-MM-dd HH:mm): ");
                        newArrivalTime = scanner.nextLine();
                    } while (!AloneAirlineSystem.validateDateTime(newArrivalTime));

                    System.out.print("New Price: ");
                    double newPrice = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    System.out.print("New Ticket Type (Round-trip/One-way): ");
                    String newTicketType = scanner.nextLine();
                    System.out.print("New Class Type (Economy/Business/First Class): ");
                    String newClassType = scanner.nextLine();
                    Ticket newTicket = new Ticket(newFlightNumber, newDeparture, newArrival, newDepartureTime, newArrivalTime, newPrice, newTicketType, newClassType);
                    system.modifyTicket(modifyIndex, newTicket);
                    break;

                case 5:
                    // Delete a ticket
                    system.displayTickets();
                    System.out.print("Enter ticket index to delete: ");
                    int deleteIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    system.deleteTicket(deleteIndex);
                    break;

                case 6:
                    // Display tickets
                    system.displayTickets();
                    break;

                case 7:
                    // Add flight
                    System.out.print("Flight Number: ");
                    String flightNumberToAdd = scanner.nextLine();
                    System.out.print("Departure: ");
                    String departureToAdd = scanner.nextLine();
                    System.out.print("Arrival: ");
                    String arrivalToAdd = scanner.nextLine();

                    String departureTimeToAdd;
                    do {
                        System.out.print("Departure Time (yyyy-MM-dd HH:mm): ");
                        departureTimeToAdd = scanner.nextLine();
                    } while (!AloneAirlineSystem.validateDateTime(departureTimeToAdd));

                    String arrivalTimeToAdd;
                    do {
                        System.out.print("Arrival Time (yyyy-MM-dd HH:mm): ");
                        arrivalTimeToAdd = scanner.nextLine();
                    } while (!AloneAirlineSystem.validateDateTime(arrivalTimeToAdd));

                    system.addFlight(flightNumberToAdd, departureToAdd, arrivalToAdd, departureTimeToAdd, arrivalTimeToAdd);
                    break;

                case 8:
                    // Display flights
                    system.displayFlights();
                    break;

                case 9:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
