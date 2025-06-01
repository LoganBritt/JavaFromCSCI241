package avl;

public class AVL {

  public Node root;

  private int size;

  public int getSize() {
    return size;
  }

  /** find w in the tree. return the node containing w or
  * null if not found */
  public Node search(String w) {
    return search(root, w);
  }
  private Node search(Node n, String w) {
    if (n == null) {
      return null;
    }
    if (w.equals(n.word)) {
      return n;
    } else if (w.compareTo(n.word) < 0) {
      return search(n.left, w);
    } else {
      return search(n.right, w);
    }
  }

  /** insert w into the tree as a standard BST, ignoring balance */
  public void bstInsert(String w) {
    if (root == null) {
      root = new Node(w);
      size = 1;
      return;
    }
    bstInsert(root, w);
  }

  /* insert w into the tree rooted at n, ignoring balance
   * pre: n is not null */
  private void bstInsert(Node n, String w) {
      if(n != null && !n.word.equals(w)){
          if(alphaFirst(n.word, w).equals(w)){
              if(n.left != null){
                  bstInsert(n.left, w);
              }else{
                  n.left = new Node(w, n);
                  size++;
              }
          }else{
              if(n.right != null){
                  bstInsert(n.right, w);
              }else{
                  n.right = new Node(w, n);
                  size++;
              }
          }
      }
  }

  /* Recursive declairation of alphaFirst with an index
   * Precondition: a and b are not null and a!= b and index > 0
   * Postcondition: The alphabetically first between a and b:
   * Returns a if char at index of a is alphabetically first or shorter
   * Returns b if char at index of b is alphabetically first
   * Recurses if char at index of both a and b are the same
   * Returns null if a = b
   */
  private String alphaFirst(String a, String b){
      if(a.equals(b)){
          return null;
      }else if(a.length() < b.length()){
          return a;
      }else if(a.length() > b.length()){
          return b;
      }else if(a.length() > 0 &&  b.length() > 0){
          if(a.codePointAt(0) < b.codePointAt(0)){
              return a;
          }else if(a.codePointAt(0) > b.codePointAt(0)){
              return b;
          }else{
              return a.charAt(0) + alphaFirst(a.substring(1), b.substring(1));
          }
      }
      return null;
  }

  /** insert w into the tree, maintaining AVL balance
  *  precondition: the tree is AVL balanced and any prior insertions have been
  *  performed by this method. */
  public void avlInsert(String w) {
      if(root == null){
          root = new Node(w);
          size = 1;
      }else{
          avlInsert(root, w);
      }
  }

  /* insert w into the tree, maintaining AVL balance
   *  precondition: the tree is AVL balanced and n is not null */
  private void avlInsert(Node n, String w) {
    if(n.word.equals(w) || alphaFirst(w, n.word) == null){
        return;
    }
    if(alphaFirst(w, n.word).equals(w)){
        if(n.left == null){
            n.left = new Node(w);
            n.left.parent = n;
            size++;
        }else{
            avlInsert(n.left, w);
        }
    }else if(!n.word.equals(w)){
        if(n.right == null){
            n.right = new Node(w);
            n.right.parent = n;
            size++;
        }else{
            avlInsert(n.right, w);
        }
    }else{
        return;
    }
    rebalance(n);
  }

  /** do a left rotation: rotate on the edge from x to its right child.
  *  precondition: x has a non-null right child */
  public void leftRotate(Node x) {
      if(x != null){
          Node n = x.right;
          if(n.left != null){
              x.right = n.left;
              x.right.parent = x;
          }else{
              x.right = null;
          }
          n.left = x;
          if(x.parent != null){
              n.parent = x.parent;
              if(x.parent.right == x){
                  x.parent.right = n;
              }else if(x.parent.left == x){
                  x.parent.left = n;
              }
          }else{
              root = n;
              n.parent = null;
          }
          x.parent = n;
          updateHeight(x);
          updateHeight(n);
      }
  }

  /** do a right rotation: rotate on the edge from y to its left child.
  *  precondition: y has a non-null left child */
  public void rightRotate(Node y) {
      if(y != null){
          Node n = y.left;
          if(n.right != null){
              y.left = n.right;
              y.left.parent = y;
          }else{
              y.left = null;
          }
          n.right = y;
          if(y.parent != null){
              n.parent = y.parent;
              if(y.parent.left == y){
                  y.parent.left = n;
              }else if(y.parent.right == y){
                  y.parent.right = n;
              }
          }else{
              root = n;
              n.parent = null;
          }
          y.parent = n;
          updateHeight(y);
          updateHeight(n);
      }
  }
  
  /** rebalance a node N after a potentially AVL-violoting insertion.
  *  precondition: none of n's descendants violates the AVL property */
  public void rebalance(Node n){
      if(n == null) return;
      
      Node left = n.left;
      Node right = n.right;
  
      int balance = getBalance(n);
      
      if(balance > 1){
        if(getBalance(left) < 0 && left != null){
          leftRotate(left);
        }
        rightRotate(n);
      }
      
      if(balance < -1){
        if(getBalance(right) > 0 && right != null){
          rightRotate(right);
        }
        leftRotate(n);
      }
      updateHeight(n);
  }

  /*
  * Modifies the height value of n to the max of it's children's heights + 1
  * Preconditon: n is a node
  * Postcondtion: n.height is properly modified
  */
  public void updateHeight(Node n){
      if(n == null) return;
      n.height = 1 + Math.max(getHeight(n.left), getHeight(n.right));
  }


  /* 
  * Returns the height of the passed node
  * Precondition: n is a node
  * Postcondition: -1 if n is null (leaf), the height of n else wise 
  */
  public int getHeight(Node n){
      return n == null ? -1 : n.height;
  }

  /* 
  * Returns the height of the passed node
  * Precondition: n is a node
  * Postcondition: 0 if n is null, 
  * the difference of the heights of n.left and n.right else wise
  */
  public int getBalance(Node n){
      return n == null ? 0 : getHeight(n.left) - getHeight(n.right);
  }


  /** remove the word w from the tree */
  public void remove(String w) {
    remove(root, w);
  }

  /* remove w from the tree rooted at n */
  private void remove(Node n, String w) {
      return;
  }

  /** print a sideways representation of the tree - root at left,
  * right is up, left is down. */
  public void printTree() {
    printSubtree(root, 0);
  }
  private void printSubtree(Node n, int level) {
    if (n == null) {
      return;
    }
    printSubtree(n.right, level + 1);
    for (int i = 0; i < level; i++) {
      System.out.print("        ");
    }
    System.out.println(n);
    printSubtree(n.left, level + 1);
  }

  /** inner class representing a node in the tree. */
  public class Node {
    public String word;
    public Node parent;
    public Node left;
    public Node right;
    public int height;

    public String toString() {
      return word + "(" + height + ")";
    }

    /** constructor: gives default values to all fields */
    public Node() { }

    /** constructor: sets only word */
    public Node(String w) {
      word = w;
    }

    /** constructor: sets word and parent fields */
    public Node(String w, Node p) {
      word = w;
      parent = p;
    }

    /** constructor: sets all fields */
    public Node(String w, Node p, Node l, Node r) {
      word = w;
      parent = p;
      left = l;
      right = r;
    }
    
    public void print(){
        printTree();
    }
  }
}
