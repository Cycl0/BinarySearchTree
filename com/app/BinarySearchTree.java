import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/*
Considerações:

Balanceamento: A implementação inicial pode ser feita sem balanceamento, mas é importante mencionar a necessidade de balanceamento para garantir o desempenho da árvore em casos de inserções e remoções frequentes.
Genericidade: A implementação pode ser generalizada para aceitar qualquer tipo de dado, utilizando generics.
Exceções: Implementar tratamento de exceções para casos como árvore vazia, elemento não encontrado, etc.
*/

public class BinarySearchTree<T extends Comparable<T>> {

  Node root;

  BinarySearchTree(T data) {
    if (data == null) {
        throw new IllegalArgumentException("Valor não deve ser nulo.");
    }
    this.root = new Node(data);
  }

  BinarySearchTree(List<T> dataList) {
      if (dataList == null || dataList.isEmpty()) {
          throw new IllegalArgumentException("A lista de valores não deve ser nula ou vazia.");
      }
      this.root = new Node(dataList.get(0));
      for (int i = 1; i < dataList.size(); i++) {
          insert(dataList.get(i));
      }
  }
  
  class Node {
    T data;
    Node left;
    Node right;

    Node(T data) {
      if (data == null) {
          throw new IllegalArgumentException("Valor não deve ser nulo.");
      }
      this.data = data;
    }
  }

  public Node insert(Node node, T data) {
    
    if (data == null) {
        throw new IllegalArgumentException("Valor não deve ser nulo.");
    }
    
    if (node == null) {
      return new Node(data);
    }

    if(data.compareTo(node.data) > 0) { // right
      node.right = insert(node.right, data);
    } else if (data.compareTo(node.data) < 0) { // left
      node.left = insert(node.left, data);
    } 
    // top
    return node;
    
  }

  public Node insert(T data) {
    return insert(root, data);
  }
  
  public boolean search(Node node, T data) {

    if (data == null) {
        throw new IllegalArgumentException("Valor não deve ser nulo.");
    }
    
    if (root == null) {
      throw new EmptyTreeException("Árvore vazia");
    }
    
    if(node == null) {
      return false;
    }
    
    if(data.compareTo(node.data) > 0) { // right
      return search(node.right, data);
    } else if (data.compareTo(node.data) < 0) { // left
      return search(node.left, data);
    }
    // top
    return true;
  }

  public boolean search(T data) {
    return search(root, data);
  }

  public Node delete(Node node, T data) {

    if (data == null) {
        throw new IllegalArgumentException("Valor não deve ser nulo.");
    }
    
    if (root == null) {
      throw new EmptyTreeException("Árvore vazia");
    }

    if (node == null) {
        throw new ElementNotFoundException("Elemento não enconntrado.");
    }

    if(data.compareTo(node.data) > 0) { // right
      node.right = delete(node.right, data);
    } else if (data.compareTo(node.data) < 0) { // left
      node.left = delete(node.left, data);
    } else { // top
      
      // Filhos no Right ou nenhum filho
      if (node.left == null) {
          Node temp = node.right;
          node.right = null;
          return temp;
      }

      // Filhos no Left
      if (node.right == null) {
          Node temp = node.left;
          node.left = null;
          return temp;
      }
      // Filhos em ambos
      Node successor = getSuccessor(node);
      node.data = successor.data; // Sobrescreve node atual com o successor
      node.right = delete(node.right, successor.data); // Deleta successor localizado no "branch" da direita
      
    }

    return node;  // top
  }

  public Node delete(T data) {
    return delete(root, data);
  }
  
  // Traversals

  public void inOrderTraversal(Node node) {
    if (node == null) return;
    inOrderTraversal(node.left);
    System.out.println(node.data + " ");
    inOrderTraversal(node.right);
  }

  public void inOrderTraversal() {
      inOrderTraversal(root);
  }

  public void preOrderTraversal(Node node) {
    if (node == null) return;
    System.out.println(node.data + " ");
    preOrderTraversal(node.left);
    preOrderTraversal(node.right);
  }

  public void preOrderTraversal() {
    preOrderTraversal(root);
  }

  public void postOrderTraversal(Node node) {
    if (node == null) return;
    postOrderTraversal(node.left);
    postOrderTraversal(node.right);
    System.out.println(node.data + " ");
  }

  public void postOrderTraversal() {
    postOrderTraversal(root);
  }

  Node getSuccessor(Node node) {
      Node current = node.right;  // Entra no "branch da direita"
      while (current != null && current.left != null) {
          current = current.left; // Loop ate encontrar o node mais a esquerda
      }
      return current;
  }


  // Procura de nodes pelo valor em String (ex: BinarySearchTree$Node@65ab7765)
  public Node findNodeByString(String nodeString) {
    return findNodeByStringHelper(root, nodeString);
  }

  private Node findNodeByStringHelper(Node node, String nodeString) {
    if (node == null) {
        return null;
    }
    if (node.toString().equals(nodeString)) {
        return node;
    }
    Node leftResult = findNodeByStringHelper(node.left, nodeString);
    if (leftResult != null) {
        return leftResult;
    }
    return findNodeByStringHelper(node.right, nodeString);
  }

  public void printAllNodes() {
    System.out.println("\n      -- LISTA NODES --       \n");
    printAllNodesHelper(root);
    System.out.println("\n\n");
  }

  private void printAllNodesHelper(Node node) {
    if (node == null) {
        return;
    }
    System.out.println(node.toString() + " : " + node.data);
    printAllNodesHelper(node.left);
    printAllNodesHelper(node.right);
  }

  // Balanciamento
  
  private void storeInArray(Node node, ArrayList<T> arr) {
      if (node == null)
          return;

      storeInArray(node.left, arr);
      arr.add(node.data);
      storeInArray(node.right, arr);
  }

  private Node buildBalancedBST(ArrayList<T> arr, int start, int end) {
      if (start > end)
          return null;

      int mid = (start + end) / 2;
      Node node = new Node(arr.get(mid));

      node.left = buildBalancedBST(arr, start, mid - 1);
      node.right = buildBalancedBST(arr, mid + 1, end);

      return node;
  }

  public void balanceTree() {
      ArrayList<T> arr = new ArrayList<>();
      storeInArray(root, arr);
      Collections.sort(arr);
      root = buildBalancedBST(arr, 0, arr.size() - 1);
  }


  // Print
  
  static final int COUNT = 10;
  
  private void printUtil(Node root, int space) {
      if (root == null)
          return;

      space += COUNT;

      printUtil(root.right, space);

      System.out.println();
      for (int i = COUNT; i < space; i++)
          System.out.print(" ");
      System.out.println(root.data);

      printUtil(root.left, space);
  }

   public void print() {
      System.out.println("\n######### ÁRVORE #########\n");
      printUtil(root, 0);
      System.out.println("\n##########################\n");
      printAllNodes();
  }

}


 // Exceptions

class EmptyTreeException extends RuntimeException {
  public EmptyTreeException(String message) {
      super(message);
  }
}

class ElementNotFoundException extends RuntimeException {
  public ElementNotFoundException(String message) {
      super(message);
  }
}