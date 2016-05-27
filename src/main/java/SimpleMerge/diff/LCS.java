package SimpleMerge.diff;

import SimpleMerge.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JinGyeong Jeong on 16. 5. 20.
 */
public class LCS<T extends Comparable<T>> {
    public List<Pair<Integer>> diff(List<T> l, List<T> r) {
        int[][] length = new int[l.size() + 1][r.size() + 1];
        for (int i = 1; i < l.size() + 1; i++) {
            for (int j = 1; j < r.size() + 1; j++) {
                if (l.get(i-1).compareTo(r.get(j-1)) == 0) {
                    length[i][j] = length[i-1][j-1] + 1;
                } else {
                    length[i][j] = Math.max(length[i - 1][j], length[i][j - 1]);
                }
            }
        }
        return getDiffList(length, l, r);
    }

    private List<Pair<Integer>> getDiffList(int [][] length, List<T> l, List<T> r) {
        int i = l.size(), j = r.size();
        List<Pair<Integer>> ret = new ArrayList<>();
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && l.get(i-1).equals(r.get(j-1))) {
                ret.add(new Pair<>(i-1, j-1));
                i--;
                j--;
            } else if (j > 0 && (i == 0 || length[i][j - 1] >= length[i - 1][j])) {
                j--;
            } else if (i > 0 && (j == 0 || length[i - 1][j] >= length[i][j - 1])) {
                i--;
            }
        }
        return ret;
    }
}
