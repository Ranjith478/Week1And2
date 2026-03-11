import java.util.*;

public class UsernameAvailabilityChecker {

    // Stores taken usernames and their user IDs
    private HashMap<String, Integer> usernameToUserId;

    // Stores how many times each username was attempted
    private HashMap<String, Integer> attemptFrequency;

    public UsernameAvailabilityChecker() {
        usernameToUserId = new HashMap<>();
        attemptFrequency = new HashMap<>();
    }

    // Register a user
    public void registerUser(String username, int userId) {
        usernameToUserId.put(username.toLowerCase(), userId);
    }

    // Check if username is available
    public boolean checkAvailability(String username) {
        username = username.toLowerCase();

        // Track attempts
        attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);

        // O(1) average lookup
        return !usernameToUserId.containsKey(username);
    }

    // Suggest similar alternatives if username is taken
    public List<String> suggestAlternatives(String username) {
        username = username.toLowerCase();
        List<String> suggestions = new ArrayList<>();

        String[] possible = {
                username + "1",
                username + "2",
                username.replace("_", "."),
                username + "_official",
                username + "123"
        };

        for (String suggestion : possible) {
            if (!usernameToUserId.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }

    // Find the most attempted username
    public String getMostAttempted() {
        String mostAttempted = null;
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        if (mostAttempted == null) {
            return "No attempts yet";
        }

        return "\"" + mostAttempted + "\" (" + maxAttempts + " attempts)";
    }

    // Display all registered users
    public void displayRegisteredUsers() {
        System.out.println("Registered Users:");
        for (Map.Entry<String, Integer> entry : usernameToUserId.entrySet()) {
            System.out.println(entry.getKey() + " -> User ID: " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        UsernameAvailabilityChecker system = new UsernameAvailabilityChecker();

        // Pre-register some usernames
        system.registerUser("john_doe", 101);
        system.registerUser("admin", 102);
        system.registerUser("alice_99", 103);

        // Sample checks
        System.out.println("checkAvailability(\"john_doe\") -> " + system.checkAvailability("john_doe"));
        System.out.println("checkAvailability(\"jane_smith\") -> " + system.checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("suggestAlternatives(\"john_doe\") -> " + system.suggestAlternatives("john_doe"));

        // Multiple attempts to track popularity
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("john_doe");

        // Most attempted username
        System.out.println("getMostAttempted() -> " + system.getMostAttempted());
    }
}