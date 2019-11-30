package com.tree2;

/**
 * 红黑树实现。<br>
 * 	红黑树是介于二叉搜索树和平衡二叉树之间的一种数据结构<br>
 * 
 * 二叉树在某些极端的情况下，可能会变成链表形状，所以在搜索的时候效率会极端低下。
 * 
 * 平衡二叉树是一种高度平衡的二叉树，在每次进行增加和删除的时候，都要进行再平衡操作。
 * 由于维护这种高度平衡所付出的代价比从中获得的效率收益还大，故而实际的应用不多。当然，如果应用场景中对插入删除不频繁，只是对查找要求较高，那么AVL还是较优于红黑树
 *	
 *	性质1. 节点是红色或黑色。
 *	性质2. 根节点是黑色。
 *	性质3. 每个红色节点的两个子节点都是黑色。(从每个叶子到根的所有路径上不能有两个连续的红色节点)
 *	性质4. 从任一节点到其每个叶子的所有路径都包含相同数目的黑色节点。
 * 
 */
public class RedBlackTree extends AbstractSelfBalancingBinarySearchTree {

    protected enum ColorEnum {
        RED,
        BLACK
    };

    protected static final RedBlackNode nilNode = new RedBlackNode(null, null, null, null, ColorEnum.BLACK);
    
    public static void main(String[] args) {
		RedBlackTree rbt = new RedBlackTree();
		rbt.insert(3);
		rbt.printTree();
		
		rbt.insert(8);
		rbt.printTree();

		rbt.insert(1);
		rbt.printTree();

		rbt.insert(10);
		rbt.printTree();
		
		rbt.insert(5);
		rbt.printTree();
		
		rbt.insert(9);
		rbt.printTree();
		
		rbt.insert(7);
		rbt.printTree();
		
		rbt.insert(18);
		rbt.printTree();
		
		rbt.insert(30);
		rbt.printTree();
		
		rbt.insert(25);
		rbt.printTree();
		
		rbt.insert(38);
		rbt.printTree();
		
	}

    /**
     * 红黑树插入
     */
    @Override
    public Node insert(int element) {
    	//插入还是按照正常的二叉树进行插入。同时将其子节点全部设置为null
        Node newNode = super.insert(element);
        newNode.left = nilNode;
        newNode.right = nilNode;
        root.parent = nilNode;
        insertRBFixup((RedBlackNode) newNode);
        return newNode;
    }
    
    /**
     * Slightly modified delete routine for red-black tree.
     * 
     * {@inheritDoc}
     */
    @Override
    protected Node delete(Node deleteNode) {
        Node replaceNode = null; // track node that replaces removedOrMovedNode
        if (deleteNode != null && deleteNode != nilNode) {
            Node removedOrMovedNode = deleteNode; // same as deleteNode if it has only one child, and otherwise it replaces deleteNode
            ColorEnum removedOrMovedNodeColor = ((RedBlackNode)removedOrMovedNode).color;
        
            if (deleteNode.left == nilNode) {
                replaceNode = deleteNode.right;
                rbTreeTransplant(deleteNode, deleteNode.right);
            } else if (deleteNode.right == nilNode) {
                replaceNode = deleteNode.left;
                rbTreeTransplant(deleteNode, deleteNode.left);
            } else {
                removedOrMovedNode = getMinimum(deleteNode.right);
                removedOrMovedNodeColor = ((RedBlackNode)removedOrMovedNode).color;
                replaceNode = removedOrMovedNode.right;
                if (removedOrMovedNode.parent == deleteNode) {
                    replaceNode.parent = removedOrMovedNode;
                } else {
                    rbTreeTransplant(removedOrMovedNode, removedOrMovedNode.right);
                    removedOrMovedNode.right = deleteNode.right;
                    removedOrMovedNode.right.parent = removedOrMovedNode;
                }
                rbTreeTransplant(deleteNode, removedOrMovedNode);
                removedOrMovedNode.left = deleteNode.left;
                removedOrMovedNode.left.parent = removedOrMovedNode;
                ((RedBlackNode)removedOrMovedNode).color = ((RedBlackNode)deleteNode).color;
            }
            
            size--;
            if (removedOrMovedNodeColor == ColorEnum.BLACK) {
                deleteRBFixup((RedBlackNode)replaceNode);
            }
        }
        
        return replaceNode;
    }
    
    /**
     * @see org.intelligentjava.algos.trees.AbstractBinarySearchTree#createNode(int, org.intelligentjava.algos.trees.AbstractBinarySearchTree.Node, org.intelligentjava.algos.trees.AbstractBinarySearchTree.Node, org.intelligentjava.algos.trees.AbstractBinarySearchTree.Node)
     */
    @Override
    protected Node createNode(int value, Node parent, Node left, Node right) {
        return new RedBlackNode(value, parent, left, right, ColorEnum.RED);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Node getMinimum(Node node) {
        while (node.left != nilNode) {
            node = node.left;
        }
        return node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getMaximum(Node node) {
        while (node.right != nilNode) {
            node = node.right;
        }
        return node;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Node rotateLeft(Node node) {
        Node temp = node.right;
        temp.parent = node.parent;
        
        node.right = temp.left;
        if (node.right != nilNode) {
            node.right.parent = node;
        }

        temp.left = node;
        node.parent = temp;

        // temp took over node's place so now its parent should point to temp
        if (temp.parent != nilNode) {
            if (node == temp.parent.left) {
                temp.parent.left = temp;
            } else {
                temp.parent.right = temp;
            }
        } else {
            root = temp;
        }
        
        return temp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node rotateRight(Node node) {
        Node temp = node.left;
        temp.parent = node.parent;

        node.left = temp.right;
        if (node.left != nilNode) {
            node.left.parent = node;
        }

        temp.right = node;
        node.parent = temp;

        // temp took over node's place so now its parent should point to temp
        if (temp.parent != nilNode) {
            if (node == temp.parent.left) {
                temp.parent.left = temp;
            } else {
                temp.parent.right = temp;
            }
        } else {
            root = temp;
        }
        
        return temp;
    }

    
    /**
     * Similar to original transplant() method in BST but uses nilNode instead of null.
     */
    private Node rbTreeTransplant(Node nodeToReplace, Node newNode) {
        if (nodeToReplace.parent == nilNode) {
            this.root = newNode;
        } else if (nodeToReplace == nodeToReplace.parent.left) {
            nodeToReplace.parent.left = newNode;
        } else {
            nodeToReplace.parent.right = newNode;
        }
        newNode.parent = nodeToReplace.parent;
        return newNode;
    }
    
    /**
     * Restores Red-Black tree properties after delete if needed.
     */
    private void deleteRBFixup(RedBlackNode x) {
        while (x != root && isBlack(x)) {
            
            if (x == x.parent.left) {
                RedBlackNode w = (RedBlackNode)x.parent.right;
                if (isRed(w)) { // case 1 - sibling is red
                    w.color = ColorEnum.BLACK;
                    ((RedBlackNode)x.parent).color = ColorEnum.RED;
                    rotateLeft(x.parent);
                    w = (RedBlackNode)x.parent.right; // converted to case 2, 3 or 4
                }
                // case 2 sibling is black and both of its children are black
                if (isBlack(w.left) && isBlack(w.right)) {
                    w.color = ColorEnum.RED;
                    x = (RedBlackNode)x.parent;
                } else if (w != nilNode) {
                    if (isBlack(w.right)) { // case 3 sibling is black and its left child is red and right child is black
                        ((RedBlackNode)w.left).color = ColorEnum.BLACK;
                        w.color = ColorEnum.RED;
                        rotateRight(w);
                        w = (RedBlackNode)x.parent.right;
                    }
                    w.color = ((RedBlackNode)x.parent).color; // case 4 sibling is black and right child is red
                    ((RedBlackNode)x.parent).color = ColorEnum.BLACK;
                    ((RedBlackNode)w.right).color = ColorEnum.BLACK;
                    rotateLeft(x.parent);
                    x = (RedBlackNode)root;
                } else {
                    x.color = ColorEnum.BLACK;
                    x = (RedBlackNode)x.parent;
                }
            } else {
                RedBlackNode w = (RedBlackNode)x.parent.left;
                if (isRed(w)) { // case 1 - sibling is red
                    w.color = ColorEnum.BLACK;
                    ((RedBlackNode)x.parent).color = ColorEnum.RED;
                    rotateRight(x.parent);
                    w = (RedBlackNode)x.parent.left; // converted to case 2, 3 or 4
                }
                // case 2 sibling is black and both of its children are black
                if (isBlack(w.left) && isBlack(w.right)) {
                    w.color = ColorEnum.RED;
                    x = (RedBlackNode)x.parent;
                } else if (w != nilNode) {
                    if (isBlack(w.left)) { // case 3 sibling is black and its right child is red and left child is black
                        ((RedBlackNode)w.right).color = ColorEnum.BLACK;
                        w.color = ColorEnum.RED;
                        rotateLeft(w);
                        w = (RedBlackNode)x.parent.left;
                    }
                    w.color = ((RedBlackNode)x.parent).color; // case 4 sibling is black and left child is red
                    ((RedBlackNode)x.parent).color = ColorEnum.BLACK;
                    ((RedBlackNode)w.left).color = ColorEnum.BLACK;
                    rotateRight(x.parent);
                    x = (RedBlackNode)root;
                } else {
                    x.color = ColorEnum.BLACK;
                    x = (RedBlackNode)x.parent;
                }
            }
            
        }
    }
    
    private boolean isBlack(Node node) {
        return node != null ? ((RedBlackNode)node).color == ColorEnum.BLACK : false;
    }
    
    private boolean isRed(Node node) {
        return node != null ? ((RedBlackNode)node).color == ColorEnum.RED : false;
    }

    /**
     * 红黑树的特点：
     * 	1.节点是红色或黑色。<br>
	 * 	2.根节点是黑色。<br>
	 *	3.每个红色节点的两个子节点都是黑色。(从每个叶子到根的所有路径上不能有两个连续的红色节点)<br>
	 *	4.从任一节点到其每个叶子的所有路径都包含相同数目的黑色节点。<br>
	 *
	 * 这里需要注意的两个地方：
	 * 	1.需要保证插入的节点为红色，理解这一条就好办了，因为在插入的节点后面可以挂两个空节点。
	 * 	2.父节点和叔叔节点的颜色不一致，需要进行旋转，将祖父节点降为父节点的子节点，同时变更父节点的颜色
	 * 	具体变换可参考：https://www.jianshu.com/p/350b003aa226
	 * 
	 * 插入流程：
	 * 	1.从根结点开始查找；
	 *	2.若根结点为空，那么插入结点作为根结点，结束。
	 *	3.若根结点不为空，那么把根结点作为当前结点；
	 *	4.若当前结点为null，返回当前结点的父结点，结束。
	 *	5.若当前结点key等于查找key，那么该key所在结点就是插入结点，更新结点的值，结束。
	 *	6.若当前结点key大于查找key，把当前结点的左子结点设置为当前结点，重复步骤4；
	 *	7.若当前结点key小于查找key，把当前结点的右子结点设置为当前结点，重复步骤4；
	 *
	 * 变更的流程是局部的。不能考虑全局的
	 * 
     */
    private void insertRBFixup(RedBlackNode currentNode) {
        // current node is always RED, so if its parent is red it breaks
        // Red-Black property, otherwise no fixup needed and loop can terminate
    	//当前节点的父节点为红色
        while (currentNode.parent != root && ((RedBlackNode) currentNode.parent).color == ColorEnum.RED) {
        	//当前节点的父节点
            RedBlackNode parent = (RedBlackNode) currentNode.parent;
            //当前节点的祖父节点
            RedBlackNode grandParent = (RedBlackNode) parent.parent;
            //父节点是左节点。左右节点的逻辑是一样的只不过是获取叔叔节点的左右有分别，其他变更逻辑基本一致
            if (parent == grandParent.left) {
            	//获取上上级节点的右节点（父节点的同级节点）
                RedBlackNode uncle = (RedBlackNode) grandParent.right;
                // case1 - uncle and parent are both red
                // re color both of them to black
                //父节点为红色，父节点的同级节点为红色
                if (((RedBlackNode) uncle).color == ColorEnum.RED) {
                	//将父节点和父节点的同级节点都设置为黑色
                    parent.color = ColorEnum.BLACK;
                    uncle.color = ColorEnum.BLACK;
                    //将上上级节点设置为红色
                    grandParent.color = ColorEnum.RED;
                    // grandparent was recolored to red, so in next iteration we
                    // check if it does not break Red-Black property
                    currentNode = grandParent;
                } 
                // case 2/3 uncle is black - then we perform rotations
                //父节点为红色，父节点同级节点为黑色
                else {
                	//当前节点是右子节点
                    if (currentNode == parent.right) { // case 2, first rotate left
                        currentNode = parent;
                        rotateLeft(currentNode);
                        parent = (RedBlackNode) currentNode.parent;
                    }
                    // do not use parent
                    parent.color = ColorEnum.BLACK; // case 3
                    grandParent.color = ColorEnum.RED;
                    rotateRight(grandParent);
                }
            //父节点是右子节点
            } else if (parent == grandParent.right) {
                RedBlackNode uncle = (RedBlackNode) grandParent.left;
                // case1 - uncle and parent are both red
                // re color both of them to black
                //父节点为红色，父节点的同级节点为红色
                if (((RedBlackNode) uncle).color == ColorEnum.RED) {
                	//将父节点和父节点的同级节点设置为黑色
                    parent.color = ColorEnum.BLACK;
                    uncle.color = ColorEnum.BLACK;
                    //将上上级节点设置为红色
                    grandParent.color = ColorEnum.RED;
                    // grandparent was recolored to red, so in next iteration we
                    // check if it does not break Red-Black property
                    currentNode = grandParent;
                }
                // case 2/3 uncle is black - then we perform rotations
                //父节点为红色，父节点的统计节点为黑色
                else {
                    if (currentNode == parent.left) { // case 2, first rotate right
                        currentNode = parent;
                        rotateRight(currentNode);
                        parent = (RedBlackNode) currentNode.parent;
                    }
                    // do not use parent
                    parent.color = ColorEnum.BLACK; // case 3
                    grandParent.color = ColorEnum.RED;
                    rotateLeft(grandParent);
                }
            }

        }
        // 根节点是黑色的
        ((RedBlackNode) root).color = ColorEnum.BLACK;
    }

    /**
     * 红黑树节点特性类
     * 颜色、值、左子节点、右子节点、父节点
     */
    protected static class RedBlackNode extends Node {
        public ColorEnum color;

        public RedBlackNode(Integer value, Node parent, Node left, Node right, ColorEnum color) {
            super(value, parent, left, right);
            this.color = color;
        }
    }

}
