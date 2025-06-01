package heap;

/** A hash table modeled after java.util.Map. It uses chaining for collision
 * resolution and grows its underlying storage by a factor of 2 when the load
 * factor exceeds 0.8. */
public class HashTable<K,V> {

    protected Pair[] buckets; // array of list nodes that store K,V pairs
    protected int size; // how many items currently in the map


    /** class Pair stores a key-value pair and a next pointer for chaining
     * multiple values together in the same bucket, linked-list style*/
    public class Pair {
        protected K key;
        protected V value;
        protected Pair next;

        /** constructor: sets key and value */
        public Pair(K k, V v) {
            key = k;
            value = v;
            next = null;
        }

        /** constructor: sets key, value, and next */
        public Pair(K k, V v, Pair nxt) {
            key = k;
            value = v;
            next = nxt;
        }

        /** returns (k, v) String representation of the pair */
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    /** constructor: initialize with default capacity 17 */
    public HashTable() {
        this(17);
    }

    /** constructor: initialize the given capacity */
    public HashTable(int capacity) {
        buckets = createBucketArray(capacity);
    }

    /** Return the size of the map (the number of key-value mappings in the
     * table) */
    public int getSize() {
        return size;
    }

    /** Return the current capacity of the table (the size of the buckets
     * array) */
    public int getCapacity() {
        return buckets.length;
    }

    /** Return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * Runtime: average case O(1); worst case O(size) */
    public V get(K key) {
        int moded = keyToInt(key) % getCapacity();
        Pair pairWorkingWith = buckets[moded];
        if(pairWorkingWith == null){
            return null;
        }else{
            //Loops until we find the key or until the next pair is null
            //If it's null, we reached the end and the key does not exist
            //Invariant: There is a maximum chain of pairs == size
            while(keyToInt(pairWorkingWith.key) != keyToInt(key)){
                pairWorkingWith = pairWorkingWith.next;
                if(pairWorkingWith == null){
                    return null;
                }
            }
            return pairWorkingWith.value;
        }
    }
    /** Associate the specified value with the specified key in this map. If
     * the map previously contained a mapping for the key, the old value is
     * replaced. Return the previous value associated with key, or null if
     * there was no mapping for key. If the load factor exceeds 0.8 after this
     * insertion, grow the array by a factor of two and rehash.
     * Precondition: val is not null.
     * Runtime: average case O(1); worst case O(size + a.length)*/
    public V put(K key, V val) {
        int moded = (keyToInt(key) % getCapacity());
        V replacedVal = null;
        Pair pairWorkingWith = buckets[moded];
        if(pairWorkingWith == null){
            if(pairWorkingWith == null){
                buckets[moded] = new Pair(key, val);
            }else{
                pairWorkingWith.next = new Pair(key, val);
            }
            size++;
        }else{
            //Loops while pairWorkingWith is neither the matching key or the last of it's row
            //Either it will find itself as the key and it will break the loop
            //Or it will find itself at the end and break the loop
            //Either will allow the following if statement to function depending on which broke the loop
            //Invariant: There is a maximum chain of pairs == size && key does not change
            while(keyToInt(pairWorkingWith.key) != keyToInt(key) && pairWorkingWith.next != null){
                pairWorkingWith = pairWorkingWith.next;
            }
            if(keyToInt(pairWorkingWith.key) == keyToInt(key)){
                replacedVal = pairWorkingWith.value;
                pairWorkingWith.value = val;
            }else if(pairWorkingWith.next == null){
                if(pairWorkingWith == null){
                    buckets[moded] = new Pair(key, val);
                }else{
                    pairWorkingWith.next = new Pair(key, val);
                }
                size++;
            }
        }
        growIfNeeded();
        return replacedVal;
    }

    /* This will return an integer with the value of the key passed into it
     * Precondition: key != null and key is of type K
     * Postcondition: Returns with the absolute value of the toString() of the key converted into an integer
     */
    public int keyToInt(K key){
        if(key instanceof Integer){
            return (Integer) key > 0 ? (Integer) key : (-1 * (Integer) key);
        }else{
            return key.hashCode();
        }
    }

    /** Return true if this map contains a mapping for the specified key.
     *  Runtime: average case O(1); worst case O(size) */
    public boolean containsKey(K key) {
        int moded = (keyToInt(key) % getCapacity());
        Pair pairWorkingWith = buckets[moded];
        //This is gonna run until we reach the end, only returning early if we run into
        //a pair in which it's key matches that of the passed key.
        //If it reaches the end, then we didn't find the key in the hash table
        //Invariant: There is a maximum chain of pairs == size
        while(pairWorkingWith != null){
            if(keyToInt(pairWorkingWith.key) == keyToInt(key)) return true;
            pairWorkingWith = pairWorkingWith.next;
        }
        return false;
    }

    /** Remove the mapping for the specified key from this map if present.
     *  Return the previous value associated with key, or null if there was no
     *  mapping for key.
     *  Runtime: average case O(1); worst case O(size)*/
    public V remove(K key){
        V retVal = null;
        int moded = (keyToInt(key) % getCapacity());
        Pair pairWorkingWith = buckets[moded];
        
        if(pairWorkingWith != null){
            if(keyToInt(pairWorkingWith.key) == keyToInt(key)){
                retVal = pairWorkingWith.value;
                if(pairWorkingWith.next != null){
                    buckets[moded] = pairWorkingWith.next;
                }else{
                    buckets[moded] = null;
                }
                size--;
            }else{
                //There is a maximum number of chained pairs == size
                while(pairWorkingWith.next != null){
                    if(keyToInt(pairWorkingWith.next.key) == keyToInt(key)){
                        retVal = pairWorkingWith.next.value;
                        if(pairWorkingWith.next.next != null){
                            pairWorkingWith.next = pairWorkingWith.next.next;
                        }else{
                            pairWorkingWith.next = null;
                        }
                        size--;
                    }else{
                        pairWorkingWith = pairWorkingWith.next;
                    }
                }
            }
        }
        return retVal;
    }

    // suggested helper method:
    /* check the load factor; if it exceeds 0.8, double the capacity 
     * and rehash values from the old array to the new array */
    private void growIfNeeded() {
        float loadFactor = (float) size / (float) getCapacity();
        if(loadFactor > 0.8){
            Pair[] oldTable = buckets;
            buckets = createBucketArray(2 * getCapacity());
            //Invariant: oldTable does not change
            for(int i = 0; i < oldTable.length; i++){
                if(oldTable[i] != null){
                    Pair pairWorkingWith = oldTable[i];
                    //Invariant: There is a maximum number of chained pairs == size
                    while(pairWorkingWith != null){
                        int moded = keyToInt(pairWorkingWith.key) % getCapacity();
                        buckets[moded] = new Pair(pairWorkingWith.key, pairWorkingWith.value);
                        pairWorkingWith = pairWorkingWith.next;
                    }
                }
            }
        }
    }

    /* useful method for debugging - prints a representation of the current
     * state of the hash table by traversing each bucket and printing the
     * key-value pairs in linked-list representation */
    protected void dump() {
        System.out.println("Table size: " + getSize() + " capacity: " +
                getCapacity());
        for (int i = 0; i < buckets.length; i++) {
            System.out.print(i + ": --");
            Pair node = buckets[i];
            while (node != null) {
                System.out.print(">" + node + "--");
                node = node.next;

            }
            System.out.println("|");
        }
    }

    /*  Create and return a bucket array with the specified size, initializing
     *  each element of the bucket array to be an empty LinkedList of Pairs.
     *  The casting and warning suppression is necessary because generics and
     *  arrays don't play well together.*/
    @SuppressWarnings("unchecked")
    protected Pair[] createBucketArray(int size) {
        return (Pair[]) new HashTable<?,?>.Pair[size];
    }
}
