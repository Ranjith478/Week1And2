import java.util.*;

public class FlashSaleInventoryManager {

    // productId -> stock count
    private HashMap<String, Integer> stockMap;

    // productId -> waiting list of userIds (FIFO)
    private HashMap<String, Queue<Integer>> waitingListMap;

    public FlashSaleInventoryManager() {
        stockMap = new HashMap<>();
        waitingListMap = new HashMap<>();
    }

    // Add product with initial stock
    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingListMap.put(productId, new LinkedList<Integer>());
    }

    // Instant stock check: O(1) average
    public String checkStock(String productId) {
        if (!stockMap.containsKey(productId)) {
            return "Product not found";
        }
        return stockMap.get(productId) + " units available";
    }

    // Synchronized to prevent overselling during concurrent requests
    public synchronized String purchaseItem(String productId, int userId) {
        if (!stockMap.containsKey(productId)) {
            return "Product not found";
        }

        int currentStock = stockMap.get(productId);

        if (currentStock > 0) {
            stockMap.put(productId, currentStock - 1);
            return "Success, " + (currentStock - 1) + " units remaining";
        } else {
            Queue<Integer> waitingQueue = waitingListMap.get(productId);
            waitingQueue.offer(userId);
            return "Added to waiting list, position #" + waitingQueue.size();
        }
    }

    // Show waiting list for a product
    public void displayWaitingList(String productId) {
        if (!waitingListMap.containsKey(productId)) {
            System.out.println("Product not found");
            return;
        }

        System.out.println("Waiting List for " + productId + ": " + waitingListMap.get(productId));
    }

    public static void main(String[] args) {
        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        // Add product with limited stock
        manager.addProduct("IPHONE15_256GB", 3);

        // Sample stock check
        System.out.println("checkStock(\"IPHONE15_256GB\") -> " + manager.checkStock("IPHONE15_256GB"));

        // Purchases
        System.out.println("purchaseItem(\"IPHONE15_256GB\", 12345) -> "
                + manager.purchaseItem("IPHONE15_256GB", 12345));

        System.out.println("purchaseItem(\"IPHONE15_256GB\", 67890) -> "
                + manager.purchaseItem("IPHONE15_256GB", 67890));

        System.out.println("purchaseItem(\"IPHONE15_256GB\", 22222) -> "
                + manager.purchaseItem("IPHONE15_256GB", 22222));

        // Stock finished, next goes to waiting list
        System.out.println("purchaseItem(\"IPHONE15_256GB\", 99999) -> "
                + manager.purchaseItem("IPHONE15_256GB", 99999));

        System.out.println("purchaseItem(\"IPHONE15_256GB\", 55555) -> "
                + manager.purchaseItem("IPHONE15_256GB", 55555));

        manager.displayWaitingList("IPHONE15_256GB");
    }
}
