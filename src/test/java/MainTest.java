import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {
    @Test
 public void encodeMessage1(){
    User.createMessage((byte) 12, "Hi there");
    assertEquals(12,User.CLIENT);
    assertEquals(1,User.NUMBER);
    assertEquals("Hi there", User.MESSAGE);
 }
    @Test
    public void encodeMessage2(){
        User.createMessage((byte) 13, "213213asdded");
        assertEquals(13,User.CLIENT);
        assertEquals(1,User.NUMBER);
        assertEquals("213213asdded", User.MESSAGE);
    }
    @Test
    public void encodeMessage3(){
        User.createMessage((byte) 1, "null");
        assertEquals(1,User.CLIENT);
        assertEquals(1,User.NUMBER);
        assertEquals("null", User.MESSAGE);
    }
}
