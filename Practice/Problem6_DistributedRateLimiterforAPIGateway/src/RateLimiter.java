import java.util.*;

class TokenBucket {

    int tokens;
    int maxTokens;
    long lastRefillTime;
    int refillRate; // tokens per hour

    public TokenBucket(int maxTokens) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // refill tokens every hour
    public void refill() {

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastRefillTime;

        long tokensToAdd = (elapsedTime * refillRate) / (3600 * 1000);

        if (tokensToAdd > 0) {

            tokens = (int)Math.min(maxTokens, tokens + tokensToAdd);
            lastRefillTime = currentTime;
        }
    }

    public boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }
}

public class RateLimiter {

    // clientId → TokenBucket
    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private static final int LIMIT = 1000;

    public synchronized String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(LIMIT));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {

            return "Allowed (" + bucket.tokens + " requests remaining)";
        }
        else {

            return "Denied (0 requests remaining)";
        }
    }

    public String getRateLimitStatus(String clientId) {

        if (!clients.containsKey(clientId))
            return "Client not found";

        TokenBucket bucket = clients.get(clientId);

        int used = bucket.maxTokens - bucket.tokens;

        return "{used: " + used +
                ", limit: " + bucket.maxTokens + "}";
    }

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));

        System.out.println(limiter.getRateLimitStatus("abc123"));
    }
}