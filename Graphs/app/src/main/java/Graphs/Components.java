package lab6;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class Components {
    public static void main(String[] args) {
        Scanner scanner;
        
        try{
            scanner = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        printInput(scanner);
    }
    
    
    public static void printInput(Scanner input){
        int nodeCount = input.nextInt();
        int[][] matrix = new int[nodeCount][nodeCount];
        int total = 0;
        //Filling of matrix with the edges in the passed file
        while(input.hasNextInt()){
            matrix[input.nextInt()][input.nextInt()] = 1;
        }
        
        //Analysis of the subgraphs
        total = analysis(matrix, nodeCount);
        System.out.println(total);
        //printEverything(matrix, nodeCount, total);
    }
    
    /* Runs DFS on every node, if they haven't been visited
     * Adds everything in each neighborhood to visited before moving on to next node
     * Precondition: matrix != null && nodeCount > 0
     * Postcondition: Returns with the total number of neighborhoods visisted
     */
    public static int analysis(int[][] matrix, int nodeCount){
        int[] visited = new int[nodeCount];
        int total = 0;
        //For every non-visted node, run dfs on the node
        //Invariant: nodeCount does not change
        for(int i = 0; i < nodeCount; i++){
            if(visited[i] == 0){
                dfs(matrix, visited, nodeCount, i);
                total++;
            }
        }
        return total;
    }
    
    /* Preforms DFS on node
     * Precondition: matrix != null, visied != null, nodeCount > 0, node != null
     * Postcondition: visited contains all the nodes reachable by node
     */
    public static void dfs(int[][] matrix, int[] visited, int nodeCount, int node){
        visited[node] = 1;
        //For every node reachable from node, add the reachable nodes to visited
        //Invariant: nodeCount does not change
        for(int i = 0; i < nodeCount; i++){
            if(matrix[node][i] == 1 && visited[i] == 0){
                dfs(matrix, visited, nodeCount, i);
            }
        }
    }
    
    //Prints a passed adjacency matrix of size nodeCount
    public static void printMatrix(int[][] matrix, int nodeCount){
        System.out.print("            ");
        for(int i = 0; i < nodeCount; i++){
            System.out.print(i + " ");
        }
        System.out.println("");
        for(int i = 0; i < nodeCount; i++){
            System.out.print("Nodes of " + i + ": ");
            for(int j = 0; j < nodeCount; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
    }
    
    public static void printEverything(int[][] matrix, int nodeCount, int total){
        printMatrix(matrix, nodeCount);
        System.out.println("Total subgraphs: " + total);
    }
}
