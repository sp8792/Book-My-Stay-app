import java.util.*;

// Booking Request Class
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Hotel Inventory (Shared Resource)
class HotelInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public HotelInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
    }

    // Critical Section
    public synchronized boolean bookRoom(String roomType) {
        int available = rooms.getOrDefault(roomType, 0);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName() +
                    " booking " + roomType + " room...");
            rooms.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Room Availability:");
        for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private HotelInventory inventory;

    public BookingProcessor(BookingQueue queue, HotelInventory inventory, String name) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            BookingRequest request;

            // Fetch request safely
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) {
                break;
            }

            // Process booking safely
            boolean success = inventory.bookRoom(request.roomType);

            if (success) {
                System.out.println(getName() + " SUCCESS: " + request.guestName +
                        " got " + request.roomType + " room");
            } else {
                System.out.println(getName() + " FAILED: No " +
                        request.roomType + " room for " + request.guestName);
            }
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        HotelInventory inventory = new HotelInventory();

        // Simulate multiple guests
        queue.addRequest(new BookingRequest("Alice", "Single"));
        queue.addRequest(new BookingRequest("Bob", "Single"));
        queue.addRequest(new BookingRequest("Charlie", "Single"));
        queue.addRequest(new BookingRequest("David", "Double"));
        queue.addRequest(new BookingRequest("Eve", "Double"));
        queue.addRequest(new BookingRequest("Frank", "Double"));

        // Create multiple processor threads
        BookingProcessor t1 = new BookingProcessor(queue, inventory, "Processor-1");
        BookingProcessor t2 = new BookingProcessor(queue, inventory, "Processor-2");
        BookingProcessor t3 = new BookingProcessor(queue, inventory, "Processor-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Display final inventory
        inventory.displayInventory();
    }
}
