import java.util.LinkedList;
import java.util.Queue;

// Class representing a booking request
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Reservation [Guest=" + guestName +
                ", RoomType=" + roomType +
                ", Nights=" + nights + "]";
    }
}

// Class managing booking request queue
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    // View all queued requests
    public void viewRequests() {
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\nCurrent Booking Request Queue:");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }

    // Peek next request (without removing)
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    // Process next request (dequeue)
    public Reservation processNextRequest() {
        return requestQueue.poll();
    }
}

// Main class
public class Main {
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        // Simulating incoming booking requests
        queue.addRequest(new Reservation("Alice", "Deluxe", 2));
        queue.addRequest(new Reservation("Bob", "Standard", 1));
        queue.addRequest(new Reservation("Charlie", "Suite", 3));

        // View queue (FIFO order)
        queue.viewRequests();

        // Show next request (without removing)
        System.out.println("\nNext request to process: " + queue.peekNextRequest());

        // Process requests (FIFO behavior)
        System.out.println("\nProcessing requests...");
        while (queue.peekNextRequest() != null) {
            System.out.println("Processed: " + queue.processNextRequest());
        }

        // Final queue state
        queue.viewRequests();
    }
}