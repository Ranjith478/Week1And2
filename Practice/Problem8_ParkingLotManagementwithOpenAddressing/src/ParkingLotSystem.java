import java.util.*;

class ParkingSpot {
    String licensePlate;
    long entryTime;
}

public class ParkingLotSystem {

    private static final int SIZE = 500;

    ParkingSpot[] table = new ParkingSpot[SIZE];

    int totalVehicles = 0;
    int totalProbes = 0;

    // Hash function
    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % SIZE;
    }

    // Park vehicle
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index] != null) {

            index = (index + 1) % SIZE; // linear probing
            probes++;
        }

        ParkingSpot spot = new ParkingSpot();
        spot.licensePlate = plate;
        spot.entryTime = System.currentTimeMillis();

        table[index] = spot;

        totalVehicles++;
        totalProbes += probes;

        System.out.println("parkVehicle(\"" + plate + "\") → Assigned spot #"
                + index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (table[index] != null) {

            if (table[index].licensePlate.equals(plate)) {

                long duration = System.currentTimeMillis() - table[index].entryTime;

                double hours = duration / (1000.0 * 60 * 60);

                double fee = hours * 5; // example $5/hour

                table[index] = null;
                totalVehicles--;

                System.out.println("exitVehicle(\"" + plate + "\") → Spot #"
                        + index + " freed, Duration: " +
                        String.format("%.2f", hours) + "h, Fee: $" +
                        String.format("%.2f", fee));

                return;
            }

            index = (index + 1) % SIZE;
        }

        System.out.println("Vehicle not found");
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy = (totalVehicles * 100.0) / SIZE;

        double avgProbes = totalVehicles == 0 ? 0 :
                (double) totalProbes / totalVehicles;

        System.out.println("Occupancy: " + String.format("%.1f", occupancy) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
    }

    public static void main(String[] args) {

        ParkingLotSystem system = new ParkingLotSystem();

        system.parkVehicle("ABC-1234");
        system.parkVehicle("ABC-1235");
        system.parkVehicle("XYZ-9999");

        system.exitVehicle("ABC-1234");

        system.getStatistics();
    }
}