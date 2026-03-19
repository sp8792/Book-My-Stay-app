import java.util.HashMap;
import java.util.Map;


class RoomInventory {

    private Map<String, Integer> inventory;


    public RoomInventory() {
        inventory = new HashMap<>();


        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }


    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }


    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        } else {
            System.out.println("Room type not found: " + roomType);
        }
    }


    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}


public class Main {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println(" Book My Stay Application ");
        System.out.println(" Hotel Booking System v3.1 ");
        System.out.println("=======================================");


        RoomInventory inventory = new RoomInventory();


        inventory.displayInventory();


        System.out.println("\nUpdating availability...");
        inventory.updateAvailability("Single Room", 4);


        inventory.displayInventory();


        System.out.println("\nAvailable Suite Rooms: "
                + inventory.getAvailability("Suite Room"));
    }
}