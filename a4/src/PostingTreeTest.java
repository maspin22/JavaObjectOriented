import static org.junit.Assert.*;
import static common.JUnitUtil.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.BeforeClass;
import org.junit.Test;

public class PostingTreeTest {

    private static Network n;
    private static Person[] people;
    private static Person personA;
    private static Person personB;
    private static Person personC;
    private static Person personD;
    private static Person personE;
    private static Person personF;
    private static Person personG;
    private static Person personH;
    private static Person personI;
    private static Person personJ;
    private static Person personK;
    private static Person personL;

    @BeforeClass
    public static void setup() {
        n= new Network();
        people= new Person[]{new Person("A", n, 0),
                new Person("B", n, 0), new Person("C", n, 0),
                new Person("D", n, 0), new Person("E", n, 0), new Person("F", n, 0),
                new Person("G", n, 0), new Person("H", n, 0), new Person("I", n, 0),
                new Person("J", n, 0), new Person("K", n, 0), new Person("L", n, 0)
        };
        personA= people[0];
        personB= people[1];
        personC= people[2];
        personD= people[3];
        personE= people[4];
        personF= people[5];
        personG= people[6];
        personH= people[7];
        personI= people[8];
        personJ= people[9];
        personK= people[10];
        personL= people[11];
    }

    @Test
    public void testBuiltInGetters() {
        PostingTree st= new PostingTree(personB);
        assertEquals("B", toStringBrief(st));
    }
    
    
    /** Create a PostingTree with structure A[B[D E F[G[H[I]]]] C]
     * Doesn't rely on method add(..) of PostingTree. */ 
    private PostingTree makeTree1() {
        PostingTree dt = new PostingTree(personA); // A
        dt.insert(personB, personA); // A, B
        dt.insert(personC, personA); // A, C
        dt.insert(personD, personB); // B, D
        dt.insert(personE, personB); // B, E
        dt.insert(personF, personB); // B, F
        dt.insert(personG, personF); // F, G
        dt.insert(personH, personG); // G, H
        dt.insert(personI, personH); // H, I
        return new PostingTree(dt);
    }
    
    @Test
    public void testMakeTree1() {
        PostingTree dt= makeTree1();
        assertEquals("A[B[D E F[G[H[I]]]] C]", toStringBrief(dt)); 
    }
    
    @Test
    public void testgetSharedAncestor() {
        PostingTree st= makeTree1();
        // A.testSharedAncestorOf(A, A) is A
        assertEquals(personA, st.getSharedAncestor(personA, personA));
        
        // A.testSharedAncestorOf(A, B) is B
        assertEquals(personA, st.getSharedAncestor(personA, personB));
        
        // A.testSharedAncestorOf(E, B) is E
        assertEquals(personE, st.getSharedAncestor(personE, personE));
        
        // A.testSharedAncestorOf(D, I) is B
        assertEquals(personB, st.getSharedAncestor(personD, personI));
        // A.testSharedAncestorOf(I, D) is B
        assertEquals(personB, st.getSharedAncestor(personI, personD));
    }

    @Test
    public void test1Insert() {
        PostingTree st= new PostingTree(personB); 

        //Test add to root
        PostingTree dt2= st.insert(personC, personB);
        assertEquals("B[C]", toStringBrief(st)); // test tree
        assertEquals(people[2], dt2.getRoot());  // test return value

        //Test add to non-root
        PostingTree dt3= st.insert(personD, personC);
        assertEquals("B[C[D]]", toStringBrief(st)); // test tree
        assertEquals(people[3], dt3.getRoot());  // test return value

        //Test add second child
        PostingTree dt0= st.insert(personA, personC);
        assertEquals("B[C[A D]]", toStringBrief(st)); // test tree
        assertEquals(personA, dt0.getRoot());  // test return value
        
        //Test add child to child's child
        PostingTree dt6= st.insert(personG, personA);
        assertEquals("B[C[A[G] D]]", toStringBrief(st)); // test tree
        assertEquals(personG, dt6.getRoot());  // test return value
        assertEquals(null, st.getPostingRoute(personH));
        assertEquals(null, st.getSharedAncestor(personD, personH));
        //assertEquals(null, st.getSharedAncestor(personD, null));
        
        //Test add to child's tree
        PostingTree dt7= st.insert(personH, personG);
        assertEquals("B[C[A[G[H]] D]]", toStringBrief(st)); // test tree
        assertEquals(people[7], dt7.getRoot());  // test return value
        assertEquals(4, st.depth(personH));
        assertEquals(1, st.widthAtDepth(4));
        assertEquals("[B, C, A, G, H]", getNames(st.getPostingRoute(personH)));
        
    }
    
    @Test
    public void test2Size() {
        PostingTree st= new PostingTree(personB); 
        PostingTree dt2= st.insert(personC, personB);
        assertEquals(2, st.size());
    }
    
    @Test
    public void test3Depth() {
        PostingTree st= new PostingTree(personB); 
        PostingTree dt2= st.insert(personC, personB);
        assertEquals(1, st.depth(personC));
    }

    @Test
    public void test4WidthAtDepth() {
        PostingTree st= new PostingTree(personB); 
        PostingTree dt2= st.insert(personC, personB);
        assertEquals(1, st.widthAtDepth(1));
    }
    
    @Test
    public void test5getPostingRoute() {
        PostingTree st= new PostingTree(personB); 
        PostingTree dt2= st.insert(personC, personB);
        List route= st.getPostingRoute(personC);
        assertEquals("[B, C]", getNames(route));
        

        //Test add to non-root
        PostingTree dt3= st.insert(personD, personC);
        assertEquals("B[C[D]]", toStringBrief(st)); // test tree
        assertEquals(people[3], dt3.getRoot());  // test return value

        //Test add second child
        PostingTree dt0= st.insert(personA, personC);
        assertEquals("B[C[A D]]", toStringBrief(st)); // test tree
        assertEquals(personA, dt0.getRoot());  // test return value
        
        //Test add child to child's child
        PostingTree dt6= st.insert(personG, personA);
        assertEquals("B[C[A[G] D]]", toStringBrief(st)); // test tree
        assertEquals(personG, dt6.getRoot());  // test return value
        assertEquals(null, st.getPostingRoute(personH));
        assertEquals(null, st.getSharedAncestor(personD, personH));
        //assertEquals(null, st.getSharedAncestor(personD, null));
        
        //Test add to child's tree
        PostingTree dt7= st.insert(personH, personG);
        assertEquals("B[C[A[G[H]] D]]", toStringBrief(st)); // test tree
        assertEquals(people[7], dt7.getRoot());  // test return value
        assertEquals(4, st.depth(personH));
        assertEquals(1, st.widthAtDepth(4));
        assertEquals("[B, C, A, G, H]", getNames(st.getPostingRoute(personH)));
    }
    
    /** Return the names of Persons in sp, separated by ", " and delimited by [ ].
     *  Precondition: No name is the empty string. */
    private String getNames(List<Person> sp) {
        String res= "[";
        for (Person p : sp) {
            if (res.length() > 1) res= res + ", ";
            res= res + p.getName();
        }
        return res + "]";
    }
    
    @Test
    public void test6getSharedAncestor() {
        PostingTree st= new PostingTree(personB); 
        PostingTree dt2= st.insert(personC, personB);
        Person p= st.getSharedAncestor(personC, personC);
        assertEquals(personC, p);
        

        //Test add to non-root
        PostingTree dt3= st.insert(personD, personC);
        assertEquals("B[C[D]]", toStringBrief(st)); // test tree
        assertEquals(people[3], dt3.getRoot());  // test return value

        //Test add second child
        PostingTree dt0= st.insert(personA, personC);
        assertEquals("B[C[A D]]", toStringBrief(st)); // test tree
        assertEquals(personA, dt0.getRoot());  // test return value
        
        //Test add child to child's child
        PostingTree dt6= st.insert(personG, personA);
        assertEquals("B[C[A[G] D]]", toStringBrief(st)); // test tree
        assertEquals(personG, dt6.getRoot());  // test return value
        assertEquals(null, st.getPostingRoute(personH));
        assertEquals(null, st.getSharedAncestor(personD, personH));
        //assertEquals(null, st.getSharedAncestor(personD, null));
        
        //Test add to child's tree
        PostingTree dt7= st.insert(personH, personG);
        assertEquals("B[C[A[G[H]] D]]", toStringBrief(st)); // test tree
        assertEquals(people[7], dt7.getRoot());  // test return value
        assertEquals(4, st.depth(personH));
        assertEquals(1, st.widthAtDepth(4));
        assertEquals("[B, C, A, G, H]", getNames(st.getPostingRoute(personH)));
    }
    
    @Test
    public void test7equals() {
        PostingTree st= new PostingTree(personB); 
        PostingTree zt= new PostingTree(personB); 
        assertEquals(true, st.equals(zt));
        
        //Test add to root
        st.insert(personC, personB);
        zt.insert(personC, personB);

        //Test add to non-root
        st.insert(personD, personC);
        zt.insert(personA, personC);
        

        //Test add second child
        st.insert(personA, personC);
        zt.insert(personD, personC);
        
        //Test add child to child's child
        st.insert(personG, personA);
        zt.insert(personG, personA);
        
        assertEquals("B[C[A[G] D]]", toStringBrief(st)); // test tree
        assertEquals("B[C[A[G] D]]", toStringBrief(zt));
        
      /*  System.out.println(st);
        System.out.println(zt);*/
        assertEquals(true, st.equals(zt));
        
        //Test add to child's tree
        st.insert(personH, personG);
        assertEquals("B[C[A[G[H]] D]]", toStringBrief(st)); // test tree
        assertEquals(false, st.equals(zt));
       
        zt.insert(personH, personG);
        assertEquals(true, st.equals(zt));
        zt.insert(personK, personC);
        assertEquals(false, st.equals(zt));
        
        assertEquals(false, st.equals(null));
        assertEquals(false, st.equals(new PostingTree(personL)));
        
    }
    
    /** Return a representation of this tree. This representation is:
     * (1) the name of the Person at the root, followed by
     * (2) the representations of the children (in alphabetical
     *     order of the children's names).
     * There are two cases concerning the children.
     *
     * No children? Their representation is the empty string.
     * Children? Their representation is the representation of each child, with
     * a blank between adjacent ones and delimited by "[" and "]".
     * Examples:
     * One-node tree: "A"
     * root A with children B, C, D: "A[B C D]"
     * root A with children B, C, D and B has a child F: "A[B[F] C D]"
     */
    public static String toStringBrief(PostingTree t) {
        String res= t.getRoot().getName();

        Object[] childs= t.getChildren().toArray();
        if (childs.length == 0) return res;
        res= res + "[";
        selectionSort1(childs);

        for (int k= 0; k < childs.length; k= k+1) {
            if (k > 0) res= res + " ";
            res= res + toStringBrief(((PostingTree)childs[k]));
        }
        return res + "]";
    }

    /** Sort b --put its elements in ascending order.
     * Sort on the name of the Person at the root of each SharingTree
     * Throw a cast-class exception if b's elements are not SharingTrees */
    public static void selectionSort1(Object[] b) {
        int j= 0;
        // {inv P: b[0..j-1] is sorted and b[0..j-1] <= b[j..]}
        // 0---------------j--------------- b.length
        // inv : b | sorted, <= | >= |
        // --------------------------------
        while (j != b.length) {
            // Put into p the index of smallest element in b[j..]
            int p= j;
            for (int i= j+1; i != b.length; i++) {
                String bi= ((PostingTree)b[i]).getRoot().getName();
                String bp= ((PostingTree)b[p]).getRoot().getName();
                if (bi.compareTo(bp) < 0) {
                    p= i;

                }
            }
            // Swap b[j] and b[p]
            Object t= b[j]; b[j]= b[p]; b[p]= t;
            j= j+1;
        }
    }

}
