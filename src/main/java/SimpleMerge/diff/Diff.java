package SimpleMerge.diff;

import SimpleMerge.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Diff<T extends Comparable<T>> {
    interface Algorithm<T extends Comparable<T>> {
        List<Pair<Integer>> diff(List<T> l, List<T> r);
    }

    private Algorithm<T> algorithm;
    private List<Pair<Integer>> commonLineIndexes;

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

    public List<Pair<Integer>> compare(List<T> item1, List<T> item2) {
        return algorithm.diff(item1, item2);
    }

}
