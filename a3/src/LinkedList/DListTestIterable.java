package LinkedList;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class DListTestIterable {

    @Test
    //This tests not only append but also toStringRev
    public void testAppend() {
        DList<String> d= new DList<String>();
        assertEquals("[]", d.toString());
        assertEquals("[]", d.toStringRev());
        assertEquals(0, d.size());
        
        d.append("3");
        assertEquals("[3]", d.toString());
        assertEquals("[3]", d.toStringRev());
        assertEquals(1, d.size());

        d.append("4");
        assertEquals("[3, 4]", d.toString());
        assertEquals("[4, 3]", d.toStringRev());
        assertEquals(2, d.size());
        
        d.append(null);
        assertEquals("[3, 4, null]", d.toString());
        assertEquals("[null, 4, 3]", d.toStringRev());
        assertEquals(3, d.size());
    }
    
    @Test
    public void testEmptyString() {
        DList<String> d= new DList<String>();
        assertEquals("[]", d.toString());
        assertEquals("[]", d.toStringRev());
        assertEquals(0, d.size());
        
        d.append("");
        assertEquals("[]", d.toString());
        assertEquals("[]", d.toStringRev());
        assertEquals(1, d.size());

        d.append("");
        assertEquals("[, ]", d.toString());
        assertEquals("[, ]", d.toStringRev());
        assertEquals(2, d.size());
        
        d.append("");
        assertEquals("[, , ]", d.toString());
        assertEquals("[, , ]", d.toStringRev());
        assertEquals(3, d.size());
    }
    
    @Test
    public void testStrange() {
        DList<String> d= new DList<String>();
        d.append("");
        assertEquals("[]", d.toString());
        assertEquals("[]", d.toStringRev());
        assertEquals(1, d.size());
        d.append("");
        assertEquals("[, ]", d.toString());
        assertEquals("[, ]", d.toStringRev());
        assertEquals(2, d.size());
    }
    
    @Test
    public void testPrepend() {
        DList<Integer> d= new DList<Integer>();
        d.prepend(3);
        assertEquals("[3]", d.toString());
        assertEquals("[3]", d.toStringRev());
        assertEquals(1, d.size());

        d.prepend(4);
        assertEquals("[4, 3]", d.toString());
        assertEquals("[3, 4]", d.toStringRev());
        assertEquals(2, d.size());
        
        d.prepend(null);
        assertEquals("[null, 4, 3]", d.toString());
        assertEquals("[3, 4, null]", d.toStringRev());
        assertEquals(3, d.size());
    }
    
    @Test
    public void testGetNode() {
        DList<Integer> d= new DList<Integer>();
        d.append(5);
        DList<Integer>.Node n= d.getNode(0);
        assertEquals(d.head(), n);
        assertEquals(5, (int)(n.value()));
        
        d.append(4);
        n= d.getNode(0);
        assertEquals(d.head(), n);
        assertEquals(5, (int)(n.value()));
        n= d.getNode(1);
        assertEquals(d.tail(), n);
        assertEquals(4, (int)(n.value()));
        
        d= new DList<Integer>();
        for (int k= 0; k < 10; k= k+1) {
            d.append(k);
        }
        for (int k= 0; k < 10; k= k+1) {
            assertEquals(k, (int)d.getNode(k).value());
        }
        d.append(10);
        for (int k= 0; k <= 10; k= k+1) {
            assertEquals(k, (int)d.getNode(k).value());
        }
    }
    
    @Test
    public void testInsertAfter() {
        DList<Integer> d= new DList<Integer>();
        d.append(5);
        d.insertAfter(3, d.head());
        assertEquals("[5, 3]", d.toString());
        assertEquals("[3, 5]", d.toStringRev());
        assertEquals(2, d.size());
        
        d.insertAfter(6, d.head());
        assertEquals("[5, 6, 3]", d.toString());
        assertEquals("[3, 6, 5]", d.toStringRev());
        assertEquals(3, d.size());
        
        d.insertAfter(7, d.tail());
        assertEquals("[5, 6, 3, 7]", d.toString());
        assertEquals("[7, 3, 6, 5]", d.toStringRev());
        assertEquals(4, d.size());
    }
    

    
    @Test
    public void testDeete() {
        DList<Integer> d= new DList<Integer>();
        d.append(5);
        d.delete(d.head());
        assertEquals("[]", d.toString());
        assertEquals("[]", d.toStringRev());
        assertEquals(0, d.size());

        d= new DList<Integer>();
        d.append(5);
        d.append(6);
        d.delete(d.head());
        assertEquals("[6]", d.toString());
        assertEquals("[6]", d.toStringRev());
        assertEquals(1, d.size());
        
        d= new DList<Integer>();
        d.append(5);
        d.append(6);
        d.delete(d.tail());
        assertEquals("[5]", d.toString());
        assertEquals("[5]", d.toStringRev());
        assertEquals(1, d.size());
        
        d= new DList<Integer>();
        d.append(5);
        d.append(6);
        d.append(7);
        d.delete(d.getNode(1));
        assertEquals("[5, 7]", d.toString());
        assertEquals("[7, 5]", d.toStringRev());
        assertEquals(2, d.size());
    }
    
    @Test
    public void testIterator() {
        // Make a list of the integers in 10..19
        DList<Integer> d= new DList<Integer>();
        for (int k= 0; k < 10; k= k+1) {
            d.append(k+10);
        }
        
        // Test that the list contains 10..19
        Iterator<Integer> dit= d.iterator();
        int k= 0;
        while (dit.hasNext()) {
            assertEquals((Integer)(k+10), (Integer) dit.next());
            k= k+1;
        }
        
        assertEquals(10, k);
    }
    
    @Test
    public void testIterable() {
        DList<Integer> d= new DList<Integer>();
        for (int k= 0; k < 10; k= k+1) {
            d.append(k);
        }
        
        int tt= 0;
        for (Object i : d) {
            assertEquals(tt, i);
            tt= tt + 1;
        }
    }
    


}
