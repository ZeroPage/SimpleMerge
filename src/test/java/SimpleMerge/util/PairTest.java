package SimpleMerge.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class PairTest {
    Pair first, second;
    Boolean result;
    public PairTest(Pair first, Pair second, Boolean result) {
        this.first = first;
        this.second = second;
        this.result = result;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {new Pair<>("Hello", "World"), new Pair<>("GoodBye", "World"), false},
            {new Pair<>(1, 5), new Pair<>(2, 4), false},
            {new Pair<>(2, 6), new Pair<>(2, 6), true},
            {new Pair<>(1.0, 1.0), new Pair<>(1.0, 1.0), true}
        });
    }

    @Test
    public void equalsTest() throws Exception {
        assertEquals(first.equals(second), result);
    }
}
