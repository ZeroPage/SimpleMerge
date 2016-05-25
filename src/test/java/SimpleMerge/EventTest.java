package SimpleMerge;

import static org.easymock.EasyMock.*;

import SimpleMerge.control.ClassTested;
import SimpleMerge.control.ClassUnderTest;
import SimpleMerge.control.Collaborator;
import org.easymock.*;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by RedChiken on 2016-05-25.
 */
public class EventTest extends EasyMockSupport{

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

    @Mock
    private Collaborator collaborator;

    @TestSubject
    private final ClassTested classUnderTest = new ClassTested();

    @Test
    public void addDocument(){
        collaborator.documentAdded("New Document");
        replayAll();
        classUnderTest.addDocument("New Document", "content");
        verifyAll();
    }

}
