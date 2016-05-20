package main.diff;

import org.junit.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        List<String> expected = readText("multiline-1-expected.txt", "\n");
        List<String> actual = lcs.diff(l, r);

        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertTrue(actual.get(i).equals(expected.get(i)));
        }
    }
}
