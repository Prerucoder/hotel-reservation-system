import java.io.*;
import java.util.*;

// Class to represent a Room
class Room {
    private int roomNumber;
    private String roomType;
    private double price;
    private boolean isAvailable;

    public Room(int roomNumber, String roomType, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.isAvailable = true;  // Initially, all rooms are available
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void reserve() {
        isAvailable = false;  // Room reserved
    }

    public void checkIn() {
        isAvailable = false;  // Room checked in
    }

    public void checkOut() {
        isAvailable = true;  // Room available again
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + roomType + ") - Price: $" + price + " - " + (isAvailable ? "Available" : "Occupied");
    }
}

// Class to represent a Guest
class Guest {
    private String name;
    private int roomNumber;

    public Guest(String name, int roomNumber) {
        this.name = name;
        this.roomNumber = roomNumber;
    }

    public String getName() {
        return name;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    @Override
    public String toString() {
        return name + " (Room " + roomNumber + ")";
    }
}

// Main class to manage the hotel system
public class HotelRSystem {
    private static List<Room> rooms = new ArrayList<>();
    private static List<Guest> guests = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        loadRooms();
        loadGuests();
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addRoom();
                    break;
                case 2:
                    viewRooms();
                    break;
                case 3:
                    makeReservation();
                    break;
                case 4:
                    checkIn();
                    break;
                case 5:
                    checkOut();
                    break;
                case 6:
                    saveData();
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n--- Hotel Reservation System ---");
        System.out.println("1. Add Room");
        System.out.println("2. View Rooms");
        System.out.println("3. Make Reservation");
        System.out.println("4. Check-In");
        System.out.println("5. Check-Out");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addRoom() {
        System.out.print("Enter room number: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter room type (Single/Double/Suite): ");
        String roomType = scanner.nextLine();
        System.out.print("Enter room price: ");
        double price = scanner.nextDouble();

        Room room = new Room(roomNumber, roomType, price);
        rooms.add(room);
        System.out.println("Room added successfully.");
    }

    private static void viewRooms() {
        System.out.println("\n--- Available Rooms ---");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }

    private static void makeReservation() {
        System.out.print("Enter room number to reserve: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Room room = findRoomByNumber(roomNumber);
        if (room != null && room.isAvailable()) {
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();
            room.reserve();
            guests.add(new Guest(guestName, roomNumber));
            System.out.println("Reservation made successfully for " + guestName + " in Room " + roomNumber);
        } else {
            System.out.println("Room is either not available or does not exist.");
        }
    }

    private static void checkIn() {
        System.out.print("Enter room number to check-in: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Room room = findRoomByNumber(roomNumber);
        if (room != null && !room.isAvailable()) {
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();
            room.checkIn();
            guests.add(new Guest(guestName, roomNumber));
            System.out.println("Check-in successful for " + guestName);
        } else {
            System.out.println("Room is either available or does not exist.");
        }
    }

    private static void checkOut() {
        System.out.print("Enter room number to check-out: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Room room = findRoomByNumber(roomNumber);
        if (room != null && !room.isAvailable()) {
            room.checkOut();
            guests.removeIf(guest -> guest.getRoomNumber() == roomNumber);
            System.out.println("Check-out successful for Room " + roomNumber);
        } else {
            System.out.println("Room is either not checked in or does not exist.");
        }
    }

    private static Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    private static void loadRooms() throws IOException {
        File file = new File("rooms.txt");
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] roomData = line.split(",");
                int roomNumber = Integer.parseInt(roomData[0]);
                String roomType = roomData[1];
                double price = Double.parseDouble(roomData[2]);
                rooms.add(new Room(roomNumber, roomType, price));
            }
            reader.close();
        }
    }

    private static void loadGuests() throws IOException {
        File file = new File("guests.txt");
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] guestData = line.split(",");
                String guestName = guestData[0];
                int roomNumber = Integer.parseInt(guestData[1]);
                guests.add(new Guest(guestName, roomNumber));
            }
            reader.close();
        }
    }

    private static void saveData() throws IOException {
        // Save rooms data
        BufferedWriter roomWriter = new BufferedWriter(new FileWriter("rooms.txt"));
        for (Room room : rooms) {
            roomWriter.write(room.getRoomNumber() + "," + room.getRoomType() + "," + room.getPrice());
            roomWriter.newLine();
        }
        roomWriter.close();

        // Save guests data
        BufferedWriter guestWriter = new BufferedWriter(new FileWriter("guests.txt"));
        for (Guest guest : guests) {
            guestWriter.write(guest.getName() + "," + guest.getRoomNumber());
            guestWriter.newLine();
        }
        guestWriter.close();
    }
}
