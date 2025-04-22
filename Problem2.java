class AutocompleteSystem {
    private class TrieNode {
        Map<Character, TrieNode> children;
        Map<String, Integer> counts;

        public TrieNode() {
            children = new HashMap<>();
            counts = new HashMap<>();
        }
    }

    private TrieNode root;
    private StringBuilder currentInput;

    public AutocompleteSystem(String[] sentences, int[] times) {
        root = new TrieNode();
        currentInput = new StringBuilder();

        for (int i = 0; i < sentences.length; i++) {
            addSentence(sentences[i], times[i]);
        }
    }

    public List<String> input(char c) {
        if (c == '#') {
            addSentence(currentInput.toString(), 1);
            currentInput = new StringBuilder();
            return new ArrayList<>();
        }

        currentInput.append(c);
        return search(currentInput.toString());
    }

    private void addSentence(String sentence, int count) {
        TrieNode node = root;
        for (char c : sentence.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
            node.counts.put(sentence, node.counts.getOrDefault(sentence, 0) + count);
        }
    }

    private List<String> search(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>((a, b) -> 
            a.getValue() == b.getValue() ? a.getKey().compareTo(b.getKey()) : b.getValue() - a.getValue()
        );
        pq.addAll(node.counts.entrySet());

        List<String> result = new ArrayList<>();
        int limit = 3;
        while (!pq.isEmpty() && limit-- > 0) {
            result.add(pq.poll().getKey());
        }

        return result;
    }

    public static void main(String[] args) {
        String[] sentences = {"i love you", "island", "iroman", "i love leetcode"};
        int[] times = {5, 3, 2, 2};

        AutocompleteSystem obj = new AutocompleteSystem(sentences, times);

        System.out.println(obj.input('i')); // ["i love you", "island", "i love leetcode"]
        System.out.println(obj.input(' ')); // ["i love you", "i love leetcode"]
        System.out.println(obj.input('a')); // []
        System.out.println(obj.input('#')); // []
    }
}
