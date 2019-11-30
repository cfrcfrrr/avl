package com.tree2;

/**
 * 二叉平衡树实现
 */
public class AVLTree extends AbstractSelfBalancingBinarySearchTree {
	
	public static void main(String[] args) {
		AVLTree avl = new AVLTree();
		avl.insert(3);
		avl.printTree();
		
		avl.insert(8);
		avl.printTree();

		avl.insert(1);
		avl.printTree();

		avl.insert(10);
		avl.printTree();
		
		avl.insert(5);
		avl.printTree();
		
		avl.insert(9);
		avl.printTree();
		
		avl.insert(7);
		avl.printTree();
		
		avl.insert(18);
		avl.printTree();
		
		avl.insert(30);
		avl.printTree();
		
		avl.insert(25);
		avl.printTree();
		
		avl.insert(38);
		avl.printTree();
		
	}

    /**
     * 平衡二叉树的插入及平衡实现
     */
    @Override
    public Node insert(int element) {
    	//这里引用平常平衡二叉树的插入操作
        Node newNode = super.insert(element);
        System.out.println("插入节点："+newNode.value);
        //对二叉树做平衡处理
        rebalance((AVLNode)newNode);
        return newNode;
    }

    /**
     * 删除节点
     */
    @Override
    public Node delete(int element) {
        Node deleteNode = super.search(element);
        if (deleteNode != null) {
            Node successorNode = super.delete(deleteNode);
            if (successorNode != null) {
                // if replaced from getMinimum(deleteNode.right) then come back there and update heights
                AVLNode minimum = successorNode.right != null ? (AVLNode)getMinimum(successorNode.right) : (AVLNode)successorNode;
                recomputeHeight(minimum);
                rebalance((AVLNode)minimum);
            } else {
                recomputeHeight((AVLNode)deleteNode.parent);
                rebalance((AVLNode)deleteNode.parent);
            }
            return successorNode;
        }
        return null;
    }
    
    /**
     * 创建节点对象
     */
    @Override
    protected Node createNode(int value, Node parent, Node left, Node right) {
        return new AVLNode(value, parent, left, right);
    }

    /**
     * 平衡二叉树平衡操作
     * @param 新插入的节点.
     */
    private void rebalance(AVLNode node) {
    	//
        while (node != null) {
            Node parent = node.parent;
            //插入节点的左边节点高度
            int leftHeight = (node.left == null) ? -1 : ((AVLNode) node.left).height;
            //插入节点的右边高度
            int rightHeight = (node.right == null) ? -1 : ((AVLNode) node.right).height;
            //计算节点的高度差
            int nodeBalance = rightHeight - leftHeight;
            // 右边比左边高，进行左旋操作
            if (nodeBalance == 2) {
            	//当前节点的
                if (node.right.right != null) {
                    node = (AVLNode)avlRotateLeft(node);
                    break;
                } else {
                    node = (AVLNode)doubleRotateRightLeft(node);
                    break;
                }
            } else if (nodeBalance == -2) {
                if (node.left.left != null) {
                    node = (AVLNode)avlRotateRight(node);
                    break;
                } else {
                    node = (AVLNode)doubleRotateLeftRight(node);
                    break;
                }
            } else {
                updateHeight(node);
            }
            //将父节点赋值给node？因为需要时刻的检测树的高度，所以需要不停的向上递归。查看插入后，父节点的左右两个子节点是否高度差超过1。
            //如果本级高度差正常，需要看父级的高度差。这里比如插入9的时候，父节点为10，父父节点为8，这两个节点都无法判断高度差，继续向上到父父父节点3。
            //这时候3左边的高度为1，3右边的高度为3，所以这个时候需要将3进行左旋，变成8的左子节点，8作为根节点。
            //同时，在创建完子节点后，还需要回头设置父节点的高度
            node = (AVLNode)parent;
        }
    }

    /**
     * 左旋，同时更新高度
     */
    public Node avlRotateLeft(Node node) {
        Node temp = super.rotateLeft(node);
        
        updateHeight((AVLNode)temp.left);
        updateHeight((AVLNode)temp);
        return temp;
    }

    /**
     * Rotates to right side.
     */
    public Node avlRotateRight(Node node) {
        Node temp = super.rotateRight(node);

        updateHeight((AVLNode)temp.right);
        updateHeight((AVLNode)temp);
        return temp;
    }

    /**
     * Take right child and rotate it to the right side first and then rotate
     * node to the left side.
     */
    public Node doubleRotateRightLeft(Node node) {
        node.right = avlRotateRight(node.right);
        return avlRotateLeft(node);
    }

    /**
     * Take right child and rotate it to the right side first and then rotate
     * node to the left side.
     */
    public Node doubleRotateLeftRight(Node node) {
        node.left = avlRotateLeft(node.left);
        return avlRotateRight(node);
    }
    
    /**
     * Recomputes height information from the node and up for all of parents. It needs to be done after delete.
     */
    public void recomputeHeight(AVLNode node) {
       while (node != null) {
          node.height = maxHeight((AVLNode)node.left, (AVLNode)node.right) + 1;
          node = (AVLNode)node.parent;
       }
    }
    
    /**
     * Returns higher height of 2 nodes. 
     */
    private int maxHeight(AVLNode node1, AVLNode node2) {
        if (node1 != null && node2 != null) {
            return node1.height > node2.height ? node1.height : node2.height;
        } else if (node1 == null) {
            return node2 != null ? node2.height : -1;
        } else if (node2 == null) {
            return node1 != null ? node1.height : -1;
        }
        return -1;
    }

    /**
     * 更新二叉平衡树节点的高度
     */
    public static final void updateHeight(AVLNode node) {
        int leftHeight = (node.left == null) ? -1 : ((AVLNode) node.left).height;
        int rightHeight = (node.right == null) ? -1 : ((AVLNode) node.right).height;
        node.height = 1 + MathUtils.getMax(leftHeight, rightHeight);
    }

    /**
     * 创建二叉平衡树节点类
     */
    public static class AVLNode extends Node {
        public int height;

        public AVLNode(int value, Node parent, Node left, Node right) {
            super(value, parent, left, right);
        }
    }

}
