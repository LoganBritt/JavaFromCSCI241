package sort;
/* Author: Logan Britt
 * Date: Apriil 24th, 2025
 * Description: Contains all the sorting methods to be used by SortsDriver
 * */

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

public class Sorts {
   // maintains a count of comparisons performed by this Sorts object
  private int comparisonCount;

  public int getComparisonCount() {
    return comparisonCount;
  }

  public void resetComparisonCount() {
    comparisonCount = 0;
  }

  /** Sorts A[start..end] in place using insertion sort
    * Precondition: 0 <= start <= end <= A.length */
  public void insertionSort(int[] A, int start, int end) {
      for (int i = start+1; i < end; ++i) {
          int focus = A[i];
          int j = i - 1;
          
          //Invariant: focus
          while (j >= 0 && A[j] > focus) {
              comparisonCount++;
              A[j + 1] = A[j];
              j = j - 1;
          }
          A[j + 1] = focus;
      }
  }

  /** Partitions A[start..end] around the pivot A[pivIndex]; returns the
   *  pivot's new index.
   *  Precondition: start <= pivIndex < end
   *  Postcondition: If partition returns i+1, then
   *  A[start..i-1] <= A[i] <= A[i+1..end] 
   **/
  public int partition(int[] A, int start, int end, int pivIndex) {
        end--;
        int pivot = A[pivIndex];
        swap(A, pivIndex, end);
        int i = start;
        
        //Invariant: end
        for(int j = start; j < end; j++){
            comparisonCount++;
            if(A[j] <= pivot){
                swap(A, j, i);
                i++;
            }
        }
        
        swap(A, i, end);
        return i;
  }

  /** use quicksort to sort the subarray A[start..end] */
  public void quickSort(int[] A, int start, int end) {
      end--;
      if (start < end) {
          int piv = partition(A, start, end, (start + (end-start)/2));
            
          quickSort(A, start, piv - 1);
          quickSort(A, piv + 1, end);
      }
  }
  
  /** merge the sorted subarrays A[start..mid] and A[mid..end] into
   *  a single sorted array in A. */
  public void merge(int[] A, int start, int mid, int end) {
      int[] temp = new int[end-start];
      int i = start, j = mid, k = 0;
      
      //Invariants: mid && end
      while(i < mid && j < end){
          comparisonCount++;
          if(A[i] <= A[j]){
              temp[k++] = A[i++];
          }else{
              temp[k++] = A[j++];
          }
      }
      //Invariant: mid
      while(i < mid){
          temp[k++] = A[i++];
      }
      //Invariant: end
      while(j < end){
          temp[k++] = A[j++];
      }
      
      //Invariant: temp.length
      for(int l = 0; l < temp.length; l++){
          A[start + l] = temp[l];
      }
  }

  /** use mergesort to sort the subarray A[start..end] */
  public void mergeSort(int[] A, int start, int end) {
      if(end - start > 1){
          int mid = (end+start)/2;
      
          mergeSort(A, start, mid);
          mergeSort(A, mid, end);
          merge(A, start, mid, end);
      }
  }
  
  /** Sort A using LSD radix sort. */
  public static void radixSort(int A[])
    {
        int m = getMax(A, A.length);

        for (int exp = 1; m / exp > 0; exp *= 10){
            int output[] = new int[A.length];
            int count[] = new int[19];
            int i;
            Arrays.fill(count, 0);
            
            //Invariant: A.length
            for (i = 0; i < A.length; i++){
                count[getIndex(A, i, exp)]++;
            }
            
            //Invariant: count.length
            for (i = 1; i < count.length; i++){
                count[i] += count[i - 1];
            }
            
            //Invariant: 0
            for (i = A.length-1; i >= 0; i--) {
                output[count[getIndex(A, i, exp)]-1] = A[i];
                count[getIndex(A, i, exp)]--;
            }   
            
            //Invariant: A.length
            for (i = 0; i < A.length; i++){
                A[i] = output[i];
            }
        }
    }
    
  //Eliminates redundant equation for index of count
  //Precondition: A != null, exp != 0
  //Postcondition: Returns index for count
  private static int getIndex(int[] A, int i, int exp){
      return ((A[i] / exp) % 10)+9;
  }
  
  /* return the 10^d's place digit of n */
  private int getDigit(int n, int d) {
    return (n / ((int)Math.pow(10, d))) % 10;
  }

  /** swap a[i] and a[j]
   *  pre: 0 <= i, j < a.size
   *  post: values in a[i] and a[j] are swapped */
  public void swap(int[] a, int i, int j) {
    int tmp = a[i];
    a[i] = a[j];
    a[j] = tmp;
  }
  
  /** get the largest value in arr
  *   pre: 0 <= n <= arr.length
  *   post: returns largest value in arr */
  static int getMax(int arr[], int n)
    {
        int mx = arr[0];
        for (int i = 1; i < n; i++)
            if (arr[i] > mx)
                mx = arr[i];
        return mx;
    }
  
}
