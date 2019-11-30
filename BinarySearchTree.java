package com.tree2;

/**
 *二叉排序树的实现（二叉排序树又名二叉搜索树，二叉查找树）
 */
public class BinarySearchTree extends AbstractBinarySearchTree {

    @Override
    protected Node createNode(int value, Node parent, Node left, Node right) {
        return new Node(value, parent, left, right);
    }
    
	
    public static void main(String[] args) {
		BinarySearchTree bst = new BinarySearchTree();
		bst.insert(3);
		bst.insert(8);
		bst.insert(1);
		bst.insert(10);
		bst.insert(5);
		bst.insert(9);
		bst.insert(7);
		bst.insert(18);
		bst.insert(30);
		bst.insert(25);
		bst.insert(38);
		
		bst.printTree();
		System.out.println("\n=====================");
		bst.printTreeInOrder();
		System.out.println("\n=====================");
		bst.printTreePreOrder();
		System.out.println("\n=====================");
		bst.printTreePostOrder();
		
		boolean contains = bst.contains(9);
		System.out.println("是否包含元素9："+contains);
		
		int maximum = bst.getMaximum();
		System.out.println("获取最大元素："+maximum);
		
		int minimum = bst.getMinimum();
		System.out.println("获取最小元素："+minimum);
		
		System.out.println("\n=====================");
		bst.delete(10);
		bst.printTree();
		
		
		
	}

}
