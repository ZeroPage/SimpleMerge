package SimpleMerge.diff;

import SimpleMerge.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Diff<T extends Comparable<T>> {
    interface Algorithm<T extends Comparable<T>> {
        List<Pair<Integer>> diff(List<T> l, List<T> r);
    }

    private Algorithm<T> algorithm;
    private List<Pair<Integer>> commonLineIndexes;
    private Pair<List<Pair<Integer>>> diffBlocksPair;
    private List<T> list1, list2;

    public Diff() {
        this.algorithm = getDefaultAlgorithm();
    }

    public Diff(List<T> list1, List<T> list2) {
        this.algorithm = getDefaultAlgorithm();
        this.compare(list1, list2);
    }

    private Algorithm<T> getDefaultAlgorithm() {
        return new LCS<T>();
    }

    public void setAlgorithm(Algorithm<T> algorithm) {
        this.algorithm = algorithm;
    }

    public List<Pair<Integer>> compare(List<T> list1, List<T> list2) {
        this.list1 = list1;
        this.list2 = list2;
        commonLineIndexes = algorithm.diff(list1, list2);
        diffBlocksPair = null;
        return commonLineIndexes;
    }

    public Pair<List<Pair<Integer>>> getDiffBlocks() {
        if (diffBlocksPair != null) {
            return diffBlocksPair;
        }
        List<Pair<Integer>> blocks1 = new ArrayList<>(), blocks2 = new ArrayList<>();

        int lastLineIndex1 = -1, lastLineIndex2 = -1;
        for (Pair<Integer> pair : commonLineIndexes) {
            blocks1.add(new Pair<Integer>(lastLineIndex1 + 1, pair.first - 1));
            blocks2.add(new Pair<Integer>(lastLineIndex2 + 1, pair.second - 1));
            lastLineIndex1 = pair.first;
            lastLineIndex2 = pair.second;
        }
        blocks1.add(new Pair<>(lastLineIndex1 + 1, list1.size() - 1));
        blocks2.add(new Pair<>(lastLineIndex2 + 1, list2.size() - 1));

        Predicate<Pair<Integer>> isInvalidBlock = new Predicate<Pair<Integer>>() {
            @Override
            public boolean test(Pair<Integer> integerPair) {
                return integerPair.first > integerPair.second;
            }
        };
        blocks1.removeIf(isInvalidBlock);
        blocks2.removeIf(isInvalidBlock);

        diffBlocksPair = new Pair<>(blocks1, blocks2);
        return diffBlocksPair;
    }
}