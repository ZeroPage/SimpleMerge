package SimpleMerge.diff;

import SimpleMerge.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DiffTest {
    @Test
    public void diffTest() {
        List<Character> L = new ArrayList<>();
        L.add('a');
        L.add('b');
        L.add('c');
        List<Character> R = new ArrayList<>();
        R.add('a');
        R.add('c');
        R.add('d');
        Diff<Character> diff = new Diff<>();
        diff.compare(L, R);

        List<Block> lBlock = new ArrayList<>();
        List<Block> rBlock = new ArrayList<>();

        // L: 'b' -  R: blank
        lBlock.add(new Block(1, 2));
        rBlock.add(new Block(1, 1));

        // L: blank - R: 'd'
        lBlock.add(new Block(3, 3));
        rBlock.add(new Block(2, 3));

        assertEquals(diff.getDiffBlocks(), new Pair<List<Block>>(lBlock, rBlock));
    }
}
