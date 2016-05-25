package SimpleMerge.control;

import java.io.*;

/**
 * Created by RedChiken on 2016-05-25.
 */
public interface Collaborator {
    void documentAdded(String title);
    void documentAdded(File file);

    void documentChanged(String title);

    void documentRemoved(String title);

    int voteForRemoval(String title);

    int voteForRemovals(String[] titles);
}
