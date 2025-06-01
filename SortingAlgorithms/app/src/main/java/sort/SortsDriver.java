package sort;
/* Author:Logan Britt
 * Date: April 24th, 2025
 * Description: Operates the front-end section of the sorting algorithms
 * and allows the user to select their desired sorting algorithm.
 * */

import java.util.Random;
import java.util.Scanner;

public class SortsDriver {

    public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      Sorts sort = new Sorts();
      System.out.printf("Enter sort (i[nsertion], q[uick], m[erge], r[adix], a[ll]): ");
      String type = input.nextLine();
      System.out.printf("Enter n (size of array to sort): ");
      int size = input.nextInt();
      int[] arrayForSort = buildRandArray(size);
      
      if(size <= 20){
          printArray(arrayForSort, size, "Unsorted");
      }
      switch(type){
        case "i":
          sort.insertionSort(arrayForSort, 0, size);
          frontSide("Insertion", arrayForSort, size, sort);
          break;
        case "q":
          sort.quickSort(arrayForSort, 0, size);
          frontSide("Quick", arrayForSort, size, sort);
          break;
        case "m":
          sort.mergeSort(arrayForSort, 0, size);
          frontSide("Merge", arrayForSort, size, sort);
          break;
        case "r":
          sort.radixSort(arrayForSort);
          frontSide("Radix", arrayForSort, size, sort);
          break;
        case "a":
          sort.insertionSort(arrayForSort, 0, size-1);
          frontSide("Insertion", arrayForSort, size, sort);
          sort.quickSort(arrayForSort, 0, size-1);
          frontSide("Quick", arrayForSort, size, sort);
          sort.mergeSort(arrayForSort, 0, size-1);
          frontSide("Merge", arrayForSort, size, sort);
          sort.radixSort(arrayForSort);
          frontSide("Radix", arrayForSort, size, sort);
          break;
        default:
          System.out.println("No sorting algorithm for entered option");
          break;
      }
    }
    
    private static void frontSide(String sortType, int[] A, int size, Sorts sort){
        if(size <= 20){
            printArray(A, size, "Sorted");
        }
        System.out.println(sortType + ": " + sort.getComparisonCount());
        sort.resetComparisonCount();
    }
    
    //Prints all values in passed array
    //Precondition: size > 0
    private static void printArray(int[] array, int size, String sorted){
      int i = 0;
      if(array != null){
        
        System.out.printf(sorted + ": [");
        //Invariant: size
        while(i < size - 1){
          System.out.printf(array[i] + " ");
          i++;
        }
        System.out.println(array[size - 1] + "]");
      }
    } 
    
    //Returns with an array of size arraySize with random values
    //Precondition: arraySize > 0
    //Postcondition: Once array is full, return array
    private static int[] buildRandArray(int arraySize){
      Random ran = new Random();
      int[] arr = new int[arraySize];
      int i = 0;
      //Invariant: arraySize
      while(i < arraySize){
        int ranInt = ran.nextInt(arraySize);
        if(ran.nextInt(2) == 0){
          ranInt = -ranInt;
        }
        arr[i] = ranInt;
        i++;
      }
      return arr;
    }
}
