/**
 * Building a BinaryTree from outside the BinaryTree class.
 * Note that left and right are private, so can't set them directly.  Normally use
 * parseNewick method to create a tree, here we do it manually.
 * @author Tim Pierson, Dartmouth CS10, Spring 2019
 *
 */
public class BuildTree {
	/**
	 * Build tree:
	 * 				G
	 * 			  /   \
	 * 			B		F
	 * 		   / \     / \
	 * 		  A   C	  D   E
	 */

	public static void main(String[] args) {
		//create left subtree
		BinaryTree<String> left = new BinaryTree<String>("B",
				new BinaryTree<String>("A"), 
				new BinaryTree<String>("C"));
		//create right subtree
		BinaryTree<String> right = new BinaryTree<String>("F",
				new BinaryTree<String>("D"), 
				new BinaryTree<String>("E"));
		//create root
		BinaryTree<String> root = new BinaryTree<String>("G",left,right);
		System.out.println(root);
		
	}

}
