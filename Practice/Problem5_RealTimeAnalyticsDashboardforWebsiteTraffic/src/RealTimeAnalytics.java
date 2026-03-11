import java.util.*;

public class RealTimeAnalytics {

    // pageUrl -> visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> set of unique users
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process incoming event
    public void processEvent(String url, String userId, String source) {

        // Update page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Count traffic source
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("Top Pages:");

        // Sort pages by views
        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());

        int count = 0;

        for (Map.Entry<String, Integer> entry : list) {

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println(page + " - " + views + " views (" + unique + " unique)");

            count++;
            if (count == 10) break;
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;
        for (int c : trafficSources.values())
            total += c;

        for (String source : trafficSources.keySet()) {

            int countSrc = trafficSources.get(source);
            double percent = (countSrc * 100.0) / total;

            System.out.println(source + ": " + String.format("%.1f", percent) + "%");
        }
    }

    public static void main(String[] args) {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent("/article/breaking-news", "user_123", "Google");
        analytics.processEvent("/article/breaking-news", "user_456", "Facebook");
        analytics.processEvent("/sports/championship", "user_999", "Direct");
        analytics.processEvent("/sports/championship", "user_888", "Google");
        analytics.processEvent("/sports/championship", "user_777", "Google");

        analytics.getDashboard();
    }
}