import java.util.*;

// Represents a reservation (already confirmed from previous use case)
class Reservation {
    private String reservationId;
    private String guestName;

    public Reservation(String reservationId, String guestName) {
        this.reservationId = reservationId;
        this.guestName = guestName;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }
}

// Represents an add-on service
class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return name + " (₹" + cost + ")";
    }
}

// Manages add-on services
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<Service>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, Service service) {
        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service added to " + reservationId + ": " + service);
    }

    // View services for a reservation
    public void viewServices(String reservationId) {
        List<Service> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services for Reservation ID: " + reservationId);
            return;
        }

        System.out.println("\nServices for Reservation ID: " + reservationId);
        for (Service s : services) {
            System.out.println("- " + s);
        }
    }

    // Calculate total service cost
    public double calculateTotalCost(String reservationId) {
        List<Service> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;
        for (Service s : services) {
            total += s.getCost();
        }
        return total;
    }
}

// Main class
public class Main {

    public static void main(String[] args) {

        // Sample reservations
        Reservation r1 = new Reservation("RES101", "Alice");
        Reservation r2 = new Reservation("RES102", "Bob");

        // Service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Create services
        Service breakfast = new Service("Breakfast", 500);
        Service spa = new Service("Spa", 1500);
        Service pickup = new Service("Airport Pickup", 1000);

        // Add services to reservations
        manager.addService(r1.getReservationId(), breakfast);
        manager.addService(r1.getReservationId(), spa);
        manager.addService(r2.getReservationId(), pickup);

        // View services
        manager.viewServices(r1.getReservationId());
        manager.viewServices(r2.getReservationId());

        // Calculate total cost
        System.out.println("\nTotal cost for " + r1.getReservationId() +
                ": ₹" + manager.calculateTotalCost(r1.getReservationId()));

        System.out.println("Total cost for " + r2.getReservationId() +
                ": ₹" + manager.calculateTotalCost(r2.getReservationId()));
    }
}