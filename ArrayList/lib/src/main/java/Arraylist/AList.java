package heap;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

/** An ArrayList-like dynamic array class that allocates
* new memory when needed */
public class AList<T> {

  protected int size; // number of elements in the AList
  protected T[] a; // the backing array storage

  public int size() {
      return size;
  }

  protected int getCap() {
      return a.length;
  }

  /** Creates an AList with a default capacity of 8 */
  public AList() {
      a = createArray(8);
      size = 0;
  }

  /** Creates an AList with the given capacity */
  public AList(int capacity) {
      a = createArray(capacity);
      size = 0;
  }

  /* Grows a to double its current capacity if newSize exceeds a's capacity. Does
  * nothing if newSize <= a.length.  Grow the array by allocating a new array
  * and copying the old array's contents into the new one. This does *not*
  * change the AList's size. */
  protected void growIfNeeded(int newSize) {
      if(newSize > getCap()){
          T[] b = createArray(2*a.length);
          //Invariant: a.length
          for(int i = 0; i < a.length; i++){
              b[i] = a[i];
          }
          a = b;
          growIfNeeded(newSize);
      }
  }

  /** Resizes the AList.
  *  this *does* modify the size, and may modify the capacity if newsize
  *  exceeds capacity. */
  public void resize(int newSize) {
      growIfNeeded(newSize);
      size = newSize;
  }

  /** Gets element i from AList.
  * @throws ArrayIndexOutOfBoundsException if 0 <= i < size does not hold */
  public T get(int i) {
      if(0 > i || size <= i){
          throw new ArrayIndexOutOfBoundsException();
      }
      return a[i];
  }

  /** Sets the ith element of the list to value.
  * @throws ArrayIndexOutOfBoundsException if 0 <= i < size does not hold */
  public void put(int i, T value) {
      if(0 > i || size <= i){
          throw new ArrayIndexOutOfBoundsException();
      }
      a[i] = value;
  }

  /** Appends value at the end of the AList, increasing size by 1.
  * Grows the array if needed to fit the appended value */
  public void append(T value) {
      growIfNeeded(size+1);
      a[size++] = value;
  }

  /** Removes and returns the value at the end of the AList.
  *  this *does* modify size and cannot modify capacity.
  *  @throws NoSuchElementException if size == 0*/
  public T pop() {
      if(size == 0){
          throw new NoSuchElementException();
      }
      return a[--size];
  }

  /*  Create and return a T[] of size n.
  *  This is necessary because generics and arrays don't play well together.*/
  @SuppressWarnings("unchecked")
  protected T[] createArray(int size) {
    return (T[]) new Object[size];
  }

}
