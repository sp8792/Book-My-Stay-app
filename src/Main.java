import java.util.*;

// Reservation class (with ID added for mapping)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation [ID=" + reservationId +
                ", Guest=" + guestName +
                ", RoomType=" + roomType + "]";
    }
}

// Add-On Service class
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service: " + service + " to Reservation ID: " + reservationId);
    }

    // View services for a reservation
    public void viewServices(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services for Reservation ID: " + reservationId);
            return;
        }

        System.out.println("\nServices for Reservation ID: " + reservationId);
        for (AddOnService s : services) {
            System.out.println("- " + s);
        }
    }

    // Calculate total cost of services
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0.0;

        double total = 0;
        for (AddOnService s : services) {
            total += s.getCost();
        }
        return total;
    }
}

// Main Class
public class Main {

    public static void main(String[] args) {

        // Sample reservations (already confirmed in Use Case 6)
        Reservation r1 = new Reservation("RES-101", "Alice", "Deluxe");
        Reservation r2 = new Reservation("RES-102", "Bob", "Standard");

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Define services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1200);
        AddOnService spa = new AddOnService("Spa Access", 2000);

        // Guest selects services
        serviceManager.addService(r1.getReservationId(), breakfast);
        serviceManager.addService(r1.getReservationId(), spa);

        serviceManager.addService(r2.getReservationId(), airportPickup);

        // View services
        serviceManager.viewServices(r1.getReservationId());
        serviceManager.viewServices(r2.getReservationId());

        // Calculate cost
        System.out.println("\nTotal Add-On Cost for " + r1.getReservationId() +
                ": ₹" + serviceManager.calculateTotalCost(r1.getReservationId()));

        System.out.println("Total Add-On Cost for " + r2.getReservationId() +
                ": ₹" + serviceManager.calculateTotalCost(r2.getReservationId()));
    }
}