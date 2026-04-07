import java.io.*;
import java.util.*;

// Booking Record (Serializable)
class BookingRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    String guestName;
    String roomType;

    public BookingRecord(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return guestName + " booked " + roomType;
    }
}

// Hotel Inventory (Serializable)
class HotelInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> rooms;

    public HotelInventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 2);
        rooms.put("Double", 2);
    }

    public boolean bookRoom(String roomType) {
        int available = rooms.getOrDefault(roomType, 0);
        if (available > 0) {
            rooms.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("Current Inventory:");
        for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, Integer> rooms) {
        this.rooms = rooms;
    }
}

// System State Wrapper
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<BookingRecord> bookings;
    HotelInventory inventory;

    public SystemState(List<BookingRecord> bookings, HotelInventory inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {
    private static final String FILE_NAME = "hotel_state.ser";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No saved data found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state restored successfully.");
            return (SystemState) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data. Starting with clean state.");
            return null;
        }
    }
}

// Main Class
public class Main {

    public static void main(String[] args) {

        // Step 1: Try to restore state
        SystemState state = PersistenceService.load();

        HotelInventory inventory;
        List<BookingRecord> bookings;

        if (state != null) {
            inventory = state.inventory;
            bookings = state.bookings;
        } else {
            inventory = new HotelInventory();
            bookings = new ArrayList<>();
        }

        // Step 2: Simulate bookings
        System.out.println("\nProcessing bookings...\n");

        processBooking("Alice", "Single", inventory, bookings);
        processBooking("Bob", "Double", inventory, bookings);
        processBooking("Charlie", "Single", inventory, bookings);

        // Step 3: Display state
        System.out.println("\nBooking History:");
        for (BookingRecord record : bookings) {
            System.out.println(record);
        }

        System.out.println();
        inventory.displayInventory();

        // Step 4: Save state before shutdown
        SystemState newState = new SystemState(bookings, inventory);
        PersistenceService.save(newState);

        System.out.println("\nSystem shutdown complete.");
    }

    private static void processBooking(String name, String roomType,
                                       HotelInventory inventory,
                                       List<BookingRecord> bookings) {

        if (inventory.bookRoom(roomType)) {
            BookingRecord record = new BookingRecord(name, roomType);
            bookings.add(record);
            System.out.println("SUCCESS: " + record);
        } else {
            System.out.println("FAILED: No " + roomType + " room for " + name);
        }
    }
}
