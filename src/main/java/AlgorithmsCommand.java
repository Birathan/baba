import java.util.ArrayList;
import java.util.List;

class BinaryTreeNode {
   int val;
   BinaryTreeNode left;
   BinaryTreeNode right;

   public BinaryTreeNode(int val, BinaryTreeNode left, BinaryTreeNode right){
      this.val = val;
      this.left = left;
      this.right = right;
   }

   public void setRight(BinaryTreeNode right){
      this.right = right;
   }

   public void setLeft(BinaryTreeNode left){
      this.left = left;
   }

   public String toString(){
      String tree = "";
      ArrayList<BinaryTreeNode> row = new ArrayList<>();
      row.add(this);

      while(row.size() > 0){
         ArrayList<BinaryTreeNode> new_row = new ArrayList<>();
         for(var i = 0; i < row.size(); i++){
            BinaryTreeNode node = row.get(i);
            tree += node.val;
            if (node.left != null){
               new_row.add(node.left);
            }
            if (node.right != null){
               new_row.add(node.right);
            }
         }
         tree += "\n";
         row = new_row;

      }

      return tree;
   }
}
public class AlgorithmsCommand {
   public static void main(String[] args) {
      BinaryTreeNode root = new BinaryTreeNode(1, null, null);
      root.left = new BinaryTreeNode(2, null, null);
      root.right = new BinaryTreeNode(3, null, null);
      root.left.left = new BinaryTreeNode(4, null, null);
      root.left.right = new BinaryTreeNode(5, null, null);
      root.right.left = new BinaryTreeNode(6, null, null);
      root.right.right = new BinaryTreeNode(7, null, null);
      System.out.println("wha tis up dog");
      System.out.println(root.toString());

   }

   public List<BinaryTreeNode> BFS(List<BinaryTreeNode> nodesList){
      return null;
   }

//   public BinaryTreeNode buildBinaryTree(List<BinaryTreeNode> nodesList){
//      1,2,3,4,5,6
//      1.left =2
//           1.right=3
//           2.left = 4
//           2.right = 5
//           3.left=6c
//      return null;
//   }
}
