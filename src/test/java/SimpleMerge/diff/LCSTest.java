package SimpleMerge.diff;

import SimpleMerge.util.Pair;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by JinGyeong Jeong on 16. 5. 20.
 */
public class LCSTest {
    private List<String> readText(String s, String delimiter) throws Exception {
        URL url = this.getClass().getResource(s);
        Path resPath = Paths.get(url.toURI());
        String str = new String(Files.readAllBytes(resPath), "UTF8");
        return Arrays.asList(str.split(delimiter));
    }

    @Test
    public void diffTest() throws Exception {
        LCS lcs = new LCS();
        List<String> l = readText("multiline-1-A.txt", "\n");
        List<String> r = readText("multiline-1-B.txt", "\n");

        List<Pair<Integer>> expected = new ArrayList<>();
        expected.add(new Pair<Integer>(2, 1));

        List<Pair<Integer>> actual = lcs.diff(l, r);

        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
