package com.tree2;

/**
 * Abstract class for self balancing binary search trees. Contains some methods
 * that is used for self balancing trees.
 * 
 * @author Ignas Lelys
 * @created Jul 24, 2011
 * 
 */
public abstract class AbstractSelfBalancingBinarySearchTree extends AbstractBinarySearchTree {

    /**
     *左旋。
     *<br>
     *	当前节点的右节点升级为父节点，当前节点降级其子节点的左子节点，当前节点右节点的左节点变更为当前节点的左子节点。
     *<br>
     *	当前节点的父节点指向当前节点右子节点的父节点
     *<br>
     *	中间会设计到三个节点的双向关联，所以这里的指针变更最低得6个，同时这里需要判别指向的顶级节点的父节点是左边还是右边，所以需要进行值的判断。
     *<br>
     *	最好的办法就是将图形画出来，同时不要考虑全局，而只考虑这部分的变更。
     */
	public Node rotateLeft(Node node) {
		//1.获取当前节点的右子节点，即将升级的节点
        Node temp = node.right;
        //2.当前节点的父节点作为即将升级的节点的父节点
        temp.parent = node.parent;
        //3.即将升级的节点的左子节点变更为当前节点的右子节点
        node.right = temp.left;
        
        if (node.right != null) {
        	//4.将即将升级的节点的父节点指向当前节点，是第3步的逆向
            node.right.parent = node;
        }
        //5.node作为即将升级节点的左子节点
        temp.left = node;
        //6.当前节点的父节点指向即将升级的节点，是第5步的逆向
        node.parent = temp;

        //7.将即将升级的节点的父节点指向即将升级的节点，是第2步的逆向。只是这里需要知道当前节点，或者是即将升级的节点是作为父类的左子节点还是右子节点。
        if (temp.parent != null) {
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
     * 右旋
     */
	public Node rotateRight(Node node) {
        Node temp = node.left;
        temp.parent = node.parent;

        node.left = temp.right;
        if (node.left != null) {
            node.left.parent = node;
        }

        temp.right = node;
        node.parent = temp;

        // temp took over node's place so now its parent should point to temp
        if (temp.parent != null) {
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

}
