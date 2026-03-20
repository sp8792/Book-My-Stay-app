import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service with validation
class InventoryService {
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
        inventory.put("Standard", 2);
    }

    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    public void validateAvailability(String roomType) throws InvalidBookingException {
        int count = inventory.get(roomType);
        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int count = inventory.get(roomType);
        if (count <= 0) {
            throw new InvalidBookingException("Cannot decrement. Inventory already zero for: " + roomType);
        }
        inventory.put(roomType, count - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Booking Service with validation + fail-fast
class BookingService {
    private InventoryService inventoryService;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void processBooking(Reservation reservation) {
        try {
            System.out.println("\nProcessing booking for: " + reservation.getGuestName());

            // Step 1: Validate input
            if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty");
            }

            if (reservation.getRoomType() == null || reservation.getRoomType().isEmpty()) {
                throw new InvalidBookingException("Room type cannot be empty");
            }

            // Step 2: Validate room type
            inventoryService.validateRoomType(reservation.getRoomType());

            // Step 3: Validate availability
            inventoryService.validateAvailability(reservation.getRoomType());

            // Step 4: Allocate (safe)
            inventoryService.decrement(reservation.getRoomType());

            // Step 5: Success
            System.out.println("Booking confirmed for " + reservation.getGuestName()
                    + " [Room Type: " + reservation.getRoomType() + "]");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(inventoryService);

        // Valid booking
        bookingService.processBooking(new Reservation("Alice", "Deluxe"));

        // Invalid room type
        bookingService.processBooking(new Reservation("Bob", "Premium"));

        // Empty guest name
        bookingService.processBooking(new Reservation("", "Suite"));

        // Overbooking scenario
        bookingService.processBooking(new Reservation("Charlie", "Suite"));
        bookingService.processBooking(new Reservation("David", "Suite")); // should fail

        // Final inventory state
        inventoryService.displayInventory();
    }
}