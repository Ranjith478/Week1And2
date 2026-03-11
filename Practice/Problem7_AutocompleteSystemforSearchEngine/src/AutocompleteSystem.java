import java.util.*;

public class AutocompleteSystem {

    // query -> frequency
    private HashMap<String, Integer> queryFrequency = new HashMap<>();

    // Add or update query frequency
    public void updateFrequency(String query) {

        queryFrequency.put(query,
                queryFrequency.getOrDefault(query, 0) + 1);
    }

    // Search suggestions for prefix
    public List<String> search(String prefix) {

        List<Map.Entry<String, Integer>> results = new ArrayList<>();

        // Find queries starting with prefix
        for (Map.Entry<String, Integer> entry : queryFrequency.entrySet()) {

            if (entry.getKey().startsWith(prefix)) {
                results.add(entry);
            }
        }

        // Sort by frequency (descending)
        results.sort((a, b) -> b.getValue() - a.getValue());

        List<String> suggestions = new ArrayList<>();

        int count = 0;

        for (Map.Entry<String, Integer> entry : results) {

            suggestions.add(entry.getKey() +
                    " (" + entry.getValue() + " searches)");

            count++;

            if (count == 10) break;
        }

        return suggestions;
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        // Add search queries
        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java tutorial");

        // Search suggestions
        List<String> suggestions = system.search("jav");

        System.out.println("search(\"jav\") →");

        int rank = 1;

        for (String s : suggestions) {
            System.out.println(rank + ". " + s);
            rank++;
        }
    }
}