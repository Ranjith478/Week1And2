import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCacheSystem {

    // LRU Cache using LinkedHashMap
    private LinkedHashMap<String, DNSEntry> cache;

    private int capacity;
    private int hits = 0;
    private int misses = 0;

    public DNSCacheSystem(int capacity) {
        this.capacity = capacity;

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCacheSystem.this.capacity;
            }
        };
    }

    // Resolve domain
    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null) {

            if (!entry.isExpired()) {
                hits++;
                return "Cache HIT → " + entry.ipAddress;
            } else {
                cache.remove(domain);
            }
        }

        misses++;

        // Query upstream DNS
        String ip = queryUpstreamDNS(domain);

        DNSEntry newEntry = new DNSEntry(domain, ip, 300);
        cache.put(domain, newEntry);

        return "Cache MISS → Query upstream → " + ip + " (TTL:300s)";
    }

    // Simulated upstream DNS lookup
    private String queryUpstreamDNS(String domain) {

        // Simulated IP generation
        Random rand = new Random();
        return "172.217.14." + rand.nextInt(255);
    }

    // Cache statistics
    public void getCacheStats() {

        int total = hits + misses;

        double hitRate = (total == 0) ? 0 : ((double) hits / total) * 100;

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws InterruptedException {

        DNSCacheSystem dns = new DNSCacheSystem(5);

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        System.out.println(dns.resolve("openai.com"));
        System.out.println(dns.resolve("github.com"));

        dns.getCacheStats();
    }
}