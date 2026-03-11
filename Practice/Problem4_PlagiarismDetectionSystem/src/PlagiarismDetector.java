import java.util.*;

public class PlagiarismDetector {

    // n-gram → set of document IDs containing that n-gram
    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    private int N = 5; // using 5-grams

    // Break document into n-grams
    public List<String> generateNgrams(String text) {

        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            grams.add(gram.toString().trim());
        }

        return grams;
    }

    // Add document to database
    public void indexDocument(String docId, String text) {

        List<String> grams = generateNgrams(text);

        for (String gram : grams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }
    }

    // Analyze document for plagiarism
    public void analyzeDocument(String docId, String text) {

        List<String> grams = generateNgrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : grams) {

            if (ngramIndex.containsKey(gram)) {

                for (String otherDoc : ngramIndex.get(gram)) {

                    if (!otherDoc.equals(docId)) {
                        matchCount.put(otherDoc,
                                matchCount.getOrDefault(otherDoc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Extracted " + grams.size() + " n-grams");

        for (String otherDoc : matchCount.keySet()) {

            int matches = matchCount.get(otherDoc);
            double similarity = (matches * 100.0) / grams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with \"" + otherDoc + "\"");

            System.out.println("Similarity: " +
                    String.format("%.1f", similarity) + "%");

            if (similarity > 60)
                System.out.println("PLAGIARISM DETECTED");
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 = "machine learning is a powerful tool for data analysis and prediction";
        String essay2 = "machine learning is a powerful tool used in data analysis";
        String essay3 = "deep learning models are widely used for prediction tasks";

        detector.indexDocument("essay_089.txt", essay1);
        detector.indexDocument("essay_092.txt", essay2);

        System.out.println("Analyzing essay_123.txt");
        detector.analyzeDocument("essay_123.txt", essay1);
    }
}