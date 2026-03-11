import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long time;

    public Transaction(int id, int amount, String merchant, String account, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    // Add transaction
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                System.out.println("Pair Found → (" + other.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // Duplicate detection
    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.print("Duplicate Payment → " + key + " Accounts: ");

                for (Transaction t : list)
                    System.out.print(t.account + " ");

                System.out.println();
            }
        }
    }

    // K-Sum (simple recursive approach)
    public void findKSum(int k, int target, int index, List<Integer> current) {

        if (target == 0 && current.size() == k) {

            System.out.println("K-Sum Found → " + current);
            return;
        }

        if (index >= transactions.size())
            return;

        Transaction t = transactions.get(index);

        // include transaction
        current.add(t.id);
        findKSum(k, target - t.amount, index + 1, current);

        // exclude transaction
        current.remove(current.size() - 1);
        findKSum(k, target, index + 1, current);
    }

    public static void main(String[] args) {

        TransactionAnalyzer system = new TransactionAnalyzer();

        system.addTransaction(new Transaction(1, 500, "StoreA", "acc1", System.currentTimeMillis()));
        system.addTransaction(new Transaction(2, 300, "StoreB", "acc2", System.currentTimeMillis()));
        system.addTransaction(new Transaction(3, 200, "StoreC", "acc3", System.currentTimeMillis()));
        system.addTransaction(new Transaction(4, 500, "StoreA", "acc4", System.currentTimeMillis()));

        System.out.println("Two Sum (target=500):");
        system.findTwoSum(500);

        System.out.println("\nDuplicate Detection:");
        system.detectDuplicates();

        System.out.println("\nK-Sum (k=3, target=1000):");
        system.findKSum(3, 1000, 0, new ArrayList<>());
    }
}