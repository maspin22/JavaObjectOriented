import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class A2Test {

    @Test
    void testIsMidSame() {
        assertEquals(true, A2.isMidSame(""));
        assertEquals(true, A2.isMidSame("bb"));
        assertEquals(false, A2.isMidSame("bx"));
        assertEquals(true, A2.isMidSame("124456"));
        assertEquals(false, A2.isMidSame("123456"));
        assertEquals(false, A2.isMidSame("111"));
        assertEquals(false, A2.isMidSame("123456789"));
        assertEquals(false, A2.isMidSame("1234567890"));
        assertEquals(true, A2.isMidSame("1234AA7890"));
    }
    
    @Test
    void testDupSome() {
        assertEquals("", A2.dupSome(""));
        assertEquals("bB", A2.dupSome("b"));
        assertEquals("B", A2.dupSome("B"));
        assertEquals("1aAaAbB", A2.dupSome("1aab"));
        assertEquals("1aAABbB", A2.dupSome("1aABb"));
        assertEquals("1aAAABbBB", A2.dupSome("1aAABbB"));
        assertEquals("å", A2.dupSome("å"));
        assertEquals("\u00E4", A2.dupSome("\u00E4"));

        assertEquals("aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ", 
                A2.dupSome("abcdefghijklmnopqrstuvwxyz"));
    }
    
    @Test
    void testPrevLetters() {
        assertEquals("", A2.prevLetters(""));
        assertEquals("abcz", A2.prevLetters("bcda"));
        assertEquals("1z$a", A2.prevLetters("1a$b"));
        assertEquals("ACZ", A2.prevLetters("ACZ"));
        assertEquals("zabcdefghijklmnopqrstuvwxy",
                         A2.prevLetters("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("knud", A2.prevLetters("love"));
        assertEquals("love", A2.prevLetters("mpwf"));
    }
    
    
    @Test
    void testAtLeastTwo() {
        assertEquals(false, A2.atLeastTwo("", ""));
        assertEquals(true, A2.atLeastTwo("a", ""));
        assertEquals(true, A2.atLeastTwo("abc", ""));
        assertEquals(false, A2.atLeastTwo("abbb", "c"));
        assertEquals(false, A2.atLeastTwo("abbb", "ab"));
        assertEquals(true, A2.atLeastTwo("abbbabc", "ab"));
    }
    
    @Test
    public void testAreAnagrams() {
        assertEquals(true, A2.areAnagrams("", ""));
        assertEquals(false, A2.areAnagrams("n", ""));
        assertEquals(false, A2.areAnagrams("", "m"));
        assertEquals(true, A2.areAnagrams("m", "m"));
        assertEquals(true, A2.areAnagrams("noon", "noon"));
        assertEquals(true, A2.areAnagrams("mary", "army"));
        assertEquals(false, A2.areAnagrams("hello", "world"));
        assertEquals(false, A2.areAnagrams("hello", "hellos"));
        assertEquals(true, A2.areAnagrams("tom marvolo riddle", "i am lordvoldemort"));
        assertEquals(false, A2.areAnagrams("tommarvoloriddle", "i am lordvoldemort"));
    }
    
    @Test
    public void testIsX() {
        assertEquals(false, A2.isX("", 1));
        assertEquals(true, A2.isX("$", 1));
        assertEquals(false, A2.isX("$", 2));
        assertEquals(true, A2.isX("bbbbbb", 1));
        assertEquals(true, A2.isX("bbbbbb", 2));
        assertEquals(true, A2.isX("bbbbbb", 3));
        assertEquals(false, A2.isX("bbbbbb", 4));
        assertEquals(false, A2.isX("xyzxyz", 1));
        assertEquals(false, A2.isX("xyzxyz", 2));
        assertEquals(true, A2.isX("xyzxyz", 3));
        assertEquals(false, A2.isX("xyzxyz", 18));
    }
    
   /* @Test
    public void testShorten() {
        assertEquals("",                A2.shorten(""));
        assertEquals("x",               A2.shorten("x"));
        assertEquals("x",               A2.shorten("xxxxxx"));
        assertEquals("xy",              A2.shorten("xyxyxyxy"));
        assertEquals("xyxz",            A2.shorten("xyxzxyxz"));
        assertEquals("hello",           A2.shorten("hellohellohello"));
        assertEquals("hellohelloworld", A2.shorten("hellohelloworld"));
        assertEquals("hellohel",        A2.shorten("hellohel"));
    }
 */
}

