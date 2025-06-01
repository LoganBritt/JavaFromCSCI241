package heap;
/*
 * Author: Logan Britt
 * Date: May 23rd, 2025
 * Purpose: Implements a Heap Data Structure
 */
import java.util.NoSuchElementException;

/** An instance is a min-heap of distinct values of type V with
 *  priorities of type P. Since it's a min-heap, the value
 *  with the smallest priority is at the root of the heap. */
public final class Heap<V, P extends Comparable<P>> {

    // TODO 1.0: Read and understand the class invariants given in the
    // following comment:

    /**
     * The contents of c represent a complete binary tree. We use square-bracket
     * shorthand to denote indexing into the AList (which is actually
     * accomplished using its get method. In the complete tree,
     * c[0] is the root; c[2i+1] is the left child of c[i] and c[2i+2] is the
     * right child of i.  If c[i] is not the root, then c[(i-1)/2] (using
     * integer division) is the parent of c[i].
     *
     * Class Invariants:
     *
     *   The tree is complete:
     *     1. `c[0..c.size()-1]` are non-null
     *
     *   The tree satisfies the heap property:
     *     2. if `c[i]` has a parent, then `c[i]`'s parent's priority
     *        is smaller than `c[i]`'s priority
     *
     *   In Phase 3, the following class invariant also must be maintained:
     *     3. The tree cannot contain duplicate *values*; note that dupliate
     *        *priorities* are still allowed.
     *     4. map contains one entry for each element of the heap, so
     *        map.size() == c.size()
     *     5. For each value v in the heap, its map entry contains in the
     *        the index of v in c. Thus: map.get(c[i]) = i.
     */
    protected AList<Entry> c;
    protected HashTable<V, Integer> map;

    /** Constructor: an empty heap with capacity 10. */
    public Heap() {
        c = new AList<Entry>(10);
        map = new HashTable<V, Integer>();
    }

    /** An Entry contains a value and a priority. */
    class Entry {
        public V value;
        public P priority;

        /** An Entry with value v and priority p*/
        Entry(V v, P p) {
            value = v;
            priority = p;
        }

        public String toString() {
            return value.toString();
        }
    }

    /** Add v with priority p to the heap.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap. Precondition: p is not null.
     *  In Phase 3 only:
     *  @throws IllegalArgumentException if v is already in the heap.*/
    public void add(V v, P p) throws IllegalArgumentException {
        if(map.get(v) == null){
            c.append(new Entry(v, p));
            map.put(v, c.size() - 1);
            bubbleUp(c.size() - 1);
        }else{
            throw new IllegalArgumentException();
        }
    }

    /** Return the number of values in this heap.
     *  This operation takes constant time. */
    public int size() {
        return c.size();
    }

    /** Swap c[h] and c[k].
     *  precondition: h and k are >= 0 and < c.size() */
    protected void swap(int h, int k) {
        V v = c.get(h).value;
        V u = c.get(k).value;
        map.put(v, k);
        map.put(u, h);
        
        Entry temp = new Entry(c.get(h).value, c.get(h).priority);
        c.put(h, c.get(k));
        c.put(k, temp);
    }
    
    public void print(){
        System.out.print("[");
        //Invariant: The size of the heap should not change
        for(int i = 0; i < size(); i++){
            System.out.print(" " + c.get(i));
        }
        System.out.println("]");
    }
    
    /** Bubble c[k] up in heap to its right place.
     *  Precondition: Priority of every c[i] >= its parent's priority
     *                except perhaps for c[k] */
    protected void bubbleUp(int k) {
        int parentIndex = (k - 1) / 2;
        if(c.get(parentIndex) == null) return;
        //Invariant: The rest of c does not change outside of the swaps
        if(c.get(k).priority.compareTo(c.get(parentIndex).priority) < 0){
            swap(k, parentIndex);
            bubbleUp(parentIndex);
        }
    }

    /** Return the value of this heap with lowest priority. Do not
     *  change the heap. This operation takes constant time.
     *  @throws NoSuchElementException if the heap is empty. */
    public V peek() throws NoSuchElementException {
        if(c.size() == 0) throw new NoSuchElementException();
        return c.get(0).value;
    }

    /** Remove and return the element of this heap with lowest priority.
     *  The expectedif(c.size() == 0){
            return NoSuchElementException();
        }
        return c.get(0).value; time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws NoSuchElementException if the heap is empty. */
    public V poll() throws NoSuchElementException {
        if(c.size() == 0) throw new NoSuchElementException();
        swap(0, c.size() - 1); 
        V retVal = c.pop().value;
        map.remove(retVal);
        bubbleDown(0);
        
        return retVal;
    }

    /** Bubble c[k] down in heap until it finds the right place.
     *  If there is a choice to bubble down to both the left and
     *  right children (because their priorities are equal), choose
     *  the right child.
     *  Precondition: Each c[i]'s priority <= its childrens' priorities
     *                except perhaps for c[k] */
    protected void bubbleDown(int k) {
        int smallest = smallerChild(k);
        //Invariant: The rest of c does not change outside of the swaps
        if(smallest != k){
            swap(k, smallest);
            bubbleDown(smallest);
        }
    }

    /** Return true if the value v is in the heap, false otherwise.
     *  The average case runtime is O(1).  */
    public boolean contains(V v) {
        return map.containsKey(v);
    }

    /** Change the priority of value v to p.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws IllegalArgumentException if v is not in the heap. */
    public void changePriority(V v, P p) throws IllegalArgumentException {
        Integer foundVal = map.get(v);
        if(foundVal == null) throw new IllegalArgumentException();
        
        Entry e = c.get(foundVal);
        if(e.priority.compareTo(p) > 0){
            e.priority = p;
            bubbleUp(foundVal);
        }else if(e.priority.compareTo(p) < 0){
            e.priority = p;
            bubbleDown(foundVal);
        }
    }

    // Recommended helper method spec:
    /* Return the index of the child of k with smaller priority.
     * if only one child exists, return that child's index
     * Precondition: at least one child exists.
     * Postcondition: Returns the index of the smallest child*/
    private int smallerChild(int k) {
        int left = 2 * k + 1;
        int right = 2 * k + 2;
        int smallest = k;
        if(left < c.size() && c.get(left).priority.compareTo(c.get(smallest).priority) < 0){
            smallest = left;
        }
        
        if(right < c.size() && c.get(right).priority.compareTo(c.get(smallest).priority) < 0){
            smallest = right;
        }
        return smallest;
    }

}
