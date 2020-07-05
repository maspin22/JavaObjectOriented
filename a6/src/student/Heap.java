package student;
import java.lang.reflect.Array;
import java.util.*;

/** An instance is a max-heap or a min-heap of distinct values of type E
 *  with priorities of type double. */
public class Heap<E> {

    /** Class Invariant:
     *   1. c[0..size-1] represents a complete binary tree. c[0] is the root;
     *      For each h, c[2h+1] and c[2h+2] are the left and right children of c[h].
     *      If h != 0, c[(h-1)/2] (using integer division) is the parent of c[h].
     *   
     *   2. For h in 0..size-1, c[h] contains the value and its priority.
     *      The values in c[size..] may or may not be null.
     *      DO NOT RELY ON THEM BEING NULL.
     *   
     *   3. The values in c[0..size-1] are all different.
     *   
     *   4. For h in 1..size-1,
     *      if isMaxHeap, (c[h]'s priority) <= (c[h]'s parent's priority),
     *      otherwise,    (c[h]'s priority) >= (c[h]'s parent's priority).
     *   
     *   dict and the tree are in sync, meaning:
     *   
     *   5. The keys of dict are the values in c[0..size-1].
     *      This implies that size = dict.size().
     *   
     *   6. if value v is in c[h], then dict.get(v) returns h.
     */
    protected final boolean isMaxHeap;
    protected data[] c;
    protected int size;
    protected HashMap<E, Integer> dict; // "dict" for dictionary
    
    /** Constructor: an empty max-heap with capacity 10. */
    public Heap() {
        isMaxHeap= true;
        c= createVPArray(10);
        dict= new HashMap<E, Integer>();
    }

    /** Constructor: an empty heap with capacity 10.
     *  It is a max-heap if isMax is true, a min-heap if isMax is false. */
    public Heap(boolean isMax) {
        isMaxHeap= isMax;
        c= createVPArray(10);
        dict= new HashMap<E, Integer>();
    }

    /** A data object houses a value and a priority. */
    class data {
        E val;             // The value
        double priority;   // The priority

        /** An instance with value v and priority p. */
        data(E v, double p) {
            val= v;
            priority= p;
        }

        /** Return a representation of this data object. */
        @Override public String toString() {
            return "(" + val + ", " + priority + ")";
        }
    }

    /** Add v with priority p to the heap.
     *  Throw an illegalArgumentException if v is already in the heap.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap. */
    public void insert(E v, double p) throws IllegalArgumentException {
        // TODO #1:
        if (dict.containsKey(v)) {
            throw new IllegalArgumentException("v is already in the heap");
        }
        ensureSpace();
        dict.put(v, size);
        c[size]= new data(v, p);
        size= size + 1;
        bubbleUp(size-1);
    }

    /** If size = length of c, double the length of array c.
     *  The worst-case time is proportional to the length of c. */
    protected void ensureSpace() {
        //TODO #2.
        if (size == c.length)  c= Arrays.copyOf(c, 2*c.length);
    }

    /** Return the size of this heap.
     *  This operation takes constant time. */
    public int size() { // Do not change this method
        return size;
    }

    /** Swap c[h] and c[k].
     *  Precondition: 0 <= h < heap-size, 0 <= k < heap-size. */
    void swap(int h, int k) {
        assert 0 <= h  &&  h < size  &&  0 <= k  &&  k < size;
        //TODO 3:
        data temp= c[h];
        c[h]= c[k];
        c[k]= temp;
        dict.put(c[h].val, h);
        dict.put(c[k].val, k);
    }

    /** If a value with priority p1 should be above a value with priority
     *       p2 in the heap, return 1.
     *  If priority p1 and priority p2 are the same, return 0.
     *  If a value with priority p1 should be below a value with priority
     *       p2 in the heap, return -1.
     *  This is based on what kind of a heap this is,
     *  E.g. a min-heap, the value with the smallest priority is in the root.
     *  E.g. a max-heap, the value with the largest priority is in the root.
     */
    public int compareTo(double p1, double p2) {
        if (p1 == p2) return 0;
        if (isMaxHeap) {
            return p1 > p2 ? 1 : -1;
        }
        return p1 < p2 ? 1 : -1;
    }

    /** If c[m] should be above c[n] in the heap, return 1.
     *  If c[m]'s priority and c[n]'s priority are the same, return 0.
     *  If c[m] should be below c[n] in the heap, return -1.
     *  This is based on what kind of a heap this is,
     *  E.g. a max-heap, the value with the largest priority is in the root.
     *  E.g. a min-heap, the value with the smallest priority is in the root.
     */
    public int compareTo(int m, int n) {
        return compareTo(c[m].priority, c[n].priority);
    }

    /** Bubble c[k] up the heap to its right place.
     *  Precondition: 0 <= k < size and
     *       The class invariant is true, except perhaps
     *       that c[k] belongs above its parent (if k > 0)
     *       in the heap, not below it. */
    void bubbleUp(int k) {
        // TODO #4
        assert 0 <= k  &&  k < size;

        // Inv: 0 <= k < size and
        //      The class invariant is true, except perhaps
        //      that c[k] belongs above its parent (if k > 0)
        //      in the heap, not below it.
        while (k > 0) {
            int p= (k-1) / 2; // p is k's parent
            if (compareTo(k, p) <= 0) return;
            swap(k, p);
            k= p;
        }
    }

    /** If this is a max-heap, return the heap value with highest priority
     *  If this is a min-heap, return the heap value with lowest priority.
     *  Do not change the heap. This operation takes constant time.
     *  Throw a NoSuchElementException if the heap is empty. */
    public E peek() {
        // TODO 5:
        if (size <= 0) throw new NoSuchElementException("heap is empty");
        return c[0].val;
    }

    /** Bubble c[k] down in heap until it finds the right place.
     *  If there is a choice to bubble down to both the left and
     *  right children (because their priorities are equal), choose
     *  the right child.
     *  Precondition: 0 <= k < size   and
     *           Class invariant is true except that perhaps
     *           c[k] belongs below one or both of its children. */
    void bubbleDown(int k) {
        // TODO 6:
        assert 0 <= k  &&  k < size;

        // Invariant: Class invariant is true except that perhaps
        //            c[k] belongs below one or both of its children
        while (2*k+1 < size) { // while c[k] has a child
            int uc= upperChild(k);
            if (compareTo(k, uc) >= 0) return;
            swap(k, uc);
            k= uc;
        }
    }

    /** If c[n] doesn't exist or has no child, return n.
     *  If c[n] has one child, return its index.
     *  If c[n] has two children with the same priority, return the
     *      index of the right one.
     *  If c[n] has two children with different priorities return the
     *      index of the one that must appear above the other in a heap. */
    protected int upperChild(int n) {
        if (size <= n) return n;
        int lc= 2*n + 1;                  // index of n's left child
        if (size <= lc) return n;         // n has no child
        if (size  ==  lc + 1) return lc;  // n has exactly one child
        return compareTo(lc, lc+1) > 0 ? lc : lc+1;
    }

    /** If this is a max-heap, remove and return heap value with highest priority.
     * If this is a min-heap, remove and return heap value with lowest priority.
     * The expected time is logarithmic and the worst-case time is linear
     * in the size of the heap.
     * Throw a NoSuchElementException if the heap is empty. */
    public E poll() {
        // TODO 7:
        if (size <= 0) throw new NoSuchElementException("heap is empty");

        E v= c[0].val;
        swap(0, size-1);
        dict.remove(v);
        size= size - 1;
        if (size > 0) bubbleDown(0);
        return v;
    }

    /** Change the priority of value v to p.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  Throw an IllegalArgumentException if v is not in the heap. */
    public void changePriority(E v, double p) {
        // TODO 8: 
        Integer index= dict.get(v);
        if (index == null) throw new IllegalArgumentException("v is not in the priority queue");
        double oldP= c[index].priority;
        c[index].priority= p;
        int t= compareTo(p, oldP);
        if (t == 0) return;
        if (t < 0) bubbleDown(index);
        else bubbleUp(index);
    }

    /** Create and return an array of size n.
     *  This is necessary because generics and arrays don't interoperate nicely.
     *  A student in CS2110 would not be expected to know about the need
     *  for this method and how to write it. We had to search the web to
     *  find out how to do it. */
    data[] createVPArray(int n) {
        return (data[]) Array.newInstance(data.class, n);
    }
}