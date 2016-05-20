package main.diff;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by JinGyeong Jeong on 16. 5. 20.
 */
public class LCS {
    public List<String> diff(List<String> l, List<String> r) {
        int INITIAL = 0;
        int TOP = 1;
        int LEFT = 2;
        int LEFT_TOP = 4;

        int[][] from = new int[l.size() + 1][r.size() + 1];
        int[][] length = new int[l.size() + 1][r.size() + 1];

        for (int i = 1; i < l.size() + 1; i++) {
            for (int j = 1; j < r.size() + 1; j++) {
                if (l.get(i-1).equals(r.get(j-1))) {
                    from[i][j] = LEFT_TOP;
                    length[i][j] = length[i-1][j-1] + 1;
                } else {
                    length[i][j] = Math.max(length[i - 1][j], length[i][j - 1]);
                    from [i][j] |= (length[i][j] == length[i - 1][j]) ? TOP : 0;
                    from [i][j] |= (length[i][j] == length[i][j - 1]) ? LEFT : 0;
                }
            }
        }

        return getDiffList(length, l, r, l.size(), r.size());
    }

    private List<String> getDiffList(int [][] length, List<String> l, List<String> r, int i, int j) {
        List<String> ret = new LinkedList<String>();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && l.get(i-1).equals(r.get(j-1))) {
                ret.add(0, "  " + l.get(i-1));
                i--;
                j--;
                // printDiff(length, l, r, i-1, j-1);
                // System.out.println("  " + l.get(i-1));
            } else if (j > 0 && (i == 0 || length[i][j - 1] >= length[i - 1][j])) {
                ret.add(0, "+ " + r.get(j-1));
                j--;
                // printDiff(length, l, r, i, j-1);
                // System.out.println("+ " + r.get(j-1));
            } else if (i > 0 && (j == 0 || length[i - 1][j] >= length[i][j - 1])) {
                ret.add(0, "- " + l.get(i-1));
                i--;
                // printDiff(length, l, r, i-1, j);
                // System.out.println("- " + l.get(i-1));
            }
        }

        return ret;
    }
}
