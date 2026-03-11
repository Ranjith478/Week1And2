import java.util.*;

class VideoData {
    String videoId;
    String data;

    public VideoData(String videoId, String data) {
        this.videoId = videoId;
        this.data = data;
    }
}

public class MultiLevelCache {

    // L1 Cache (Memory) with LRU
    private LinkedHashMap<String, VideoData> L1;

    // L2 Cache (SSD)
    private HashMap<String, VideoData> L2;

    // Access counter
    private HashMap<String, Integer> accessCount;

    int L1Hits = 0;
    int L2Hits = 0;
    int L3Hits = 0;

    private static final int L1_CAPACITY = 10000;
    private static final int PROMOTION_THRESHOLD = 2;

    public MultiLevelCache() {

        L1 = new LinkedHashMap<String, VideoData>(L1_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1_CAPACITY;
            }
        };

        L2 = new HashMap<>();
        accessCount = new HashMap<>();
    }

    // Simulated database
    private VideoData fetchFromDatabase(String videoId) {

        return new VideoData(videoId, "VideoContent_" + videoId);
    }

    public VideoData getVideo(String videoId) {

        // L1 Cache
        if (L1.containsKey(videoId)) {

            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 Cache
        if (L2.containsKey(videoId)) {

            L2Hits++;

            System.out.println("L2 Cache HIT (5ms)");

            int count = accessCount.getOrDefault(videoId, 0) + 1;
            accessCount.put(videoId, count);

            // Promote to L1
            if (count >= PROMOTION_THRESHOLD) {
                L1.put(videoId, L2.get(videoId));
                System.out.println("Promoted to L1");
            }

            return L2.get(videoId);
        }

        System.out.println("L2 Cache MISS");

        // L3 Database
        L3Hits++;

        System.out.println("L3 Database HIT (150ms)");

        VideoData video = fetchFromDatabase(videoId);

        L2.put(videoId, video);
        accessCount.put(videoId, 1);

        return video;
    }

    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        System.out.println("\nCache Statistics:");

        System.out.println("L1 Hit Rate: " +
                (100.0 * L1Hits / total) + "%");

        System.out.println("L2 Hit Rate: " +
                (100.0 * L2Hits / total) + "%");

        System.out.println("L3 Hit Rate: " +
                (100.0 * L3Hits / total) + "%");
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}