package SimpleMerge.diff;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BlockTest {
    final private int start, end, offset, startOffset, endOffset;
    Block block;

    public BlockTest(int start, int end, int offset, int startOffset, int endOffset) {
        this.start = start;
        this.end = end;
        this.offset = offset;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {0, 1, 0, 5, 10}, {10, 7, 10, 2, 5}, {3, 6, 23, 12, 4}, {7, 6, 7, 8, 8}, {1, 0, 0, 4, 3}, {7, 9, 1, 4, 1}
        });
    }

    @Before
    public void setUp() {
        block = new Block(start, end);
    }

    @Test
    public void startTest() {
        assertEquals(start, block.start());
    }

    @Test
    public void endTest() {
        assertEquals(end, block.end());
    }

    @Test
    public void updateSameOffsetTest() {
        block.update(offset);
        assertEquals(start + offset, block.start());
        assertEquals(end + offset, block.end());
    }

    @Test
    public void updateTest() {
        block.update(startOffset, endOffset);
        assertEquals(start + startOffset, block.start());
        assertEquals(end + endOffset, block.end());

    }
}
