package com.tree2;

/**
 * Abstract binary search tree implementation. Its basically fully implemented
 * binary search tree, just template method is provided for creating Node (other
 * trees can have slightly different nodes with more info). This way some code
 * from standart binary search tree can be reused for other kinds of binary
 * trees.
 * 
 * @author Ignas Lelys
 * @created Jun 29, 2011
 * 
 */
public abstract class AbstractBinarySearchTree {

    /** Root node where whole tree starts. */
    public Node root;

    /** Tree size. */
    public int size;

    /**
     * Because this is abstract class and various trees have different additional information on 
     * different nodes subclasses uses this abstract method to create nodes (maybe of class {@link Node}
     * or maybe some different node sub class).
     * 
     * @param value Value that node will have.
     * @param parent Node's parent.
     * @param left Node's left child.
     * @param right Node's right child.
     * @return Created node instance.
     */
    protected abstract Node createNode(int value, Node parent, Node left, Node right);

    /**
     * 获取指定元素
     */
    public Node search(int element) {
        Node node = root;
        while (node != null && node.value != null && node.value != element) {
            if (element < node.value) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return node;
    }

    /**
     * 二叉树的插入
     */
    public Node insert(int element) {
    	//如果是根节点，直接创建
        if (root == null) {
            root = createNode(element, null, null, null);
            size++;
            return root;
        }

        Node insertParentNode = null;
        Node searchTempNode = root;
        //每次插入都需要从根节点进行数据的比较
        while (searchTempNode != null && searchTempNode.value != null) {
            insertParentNode = searchTempNode;
            //
            if (element < searchTempNode.value) {
                searchTempNode = searchTempNode.left;
            } else {
                searchTempNode = searchTempNode.right;
            }
        }
        
        //创建节点，并创建父节点和子节点的关联
        Node newNode = createNode(element, insertParentNode, null, null);
        if (insertParentNode.value > newNode.value) {
            insertParentNode.left = newNode;
        } else {
            insertParentNode.right = newNode;
        }

        size++;
        return newNode;
    }

    /**
     * Removes element if node with such value exists.
     * 
     * @param element
     *            Element value to remove.
     * 
     * @return New node that is in place of deleted node. Or null if element for
     *         delete was not found.
     */
    public Node delete(int element) {
        Node deleteNode = search(element);
        if (deleteNode != null) {
            return delete(deleteNode);
        } else {
            return null;
        }
    }

    /**
     * 二叉树删除逻辑
     */
    protected Node delete(Node deleteNode) {
        if (deleteNode != null) {
            Node nodeToReturn = null;
            if (deleteNode != null) {
            	//TODO 添加该逻辑，当删除的节点是叶子节点，直接将父节点的左右节点置空即可
            	if(deleteNode.left == null && deleteNode.right == null){
            		if(deleteNode == root){
            			root = null;
            		} else {
            			Node parent = deleteNode.parent;
            			if(deleteNode == parent.left){//判断当前节点是左节点还是右节点
            				parent.left = null;
            			} else {
            				parent.right = null;
            			}
            		}
            	} else
            	
            	//删除节点的左节点为空
                if (deleteNode.left == null) {
                    nodeToReturn = transplant(deleteNode, deleteNode.right);
                //删除节点的右节点为空
                } else if (deleteNode.right == null) {
                    nodeToReturn = transplant(deleteNode, deleteNode.left);
                //删除节点是中间节点
                } else {
                	//获取删除节点右子树的最小节点
                    Node successorNode = getMinimum(deleteNode.right);
                    if (successorNode.parent != deleteNode) {
                        transplant(successorNode, successorNode.right);
                        successorNode.right = deleteNode.right;
                        successorNode.right.parent = successorNode;
                    }
                    transplant(deleteNode, successorNode);
                    successorNode.left = deleteNode.left;
                    successorNode.left.parent = successorNode;
                    nodeToReturn = successorNode;
                }
                size--;
            }
    
            return nodeToReturn;
        }
        return null;
    }

    /**
     * Put one node from tree (newNode) to the place of another (nodeToReplace).
     * 
     * @param nodeToReplace
     *            Node which is replaced by newNode and removed from tree.
     * @param newNode
     *            New node.
     * 
     * @return New replaced node.
     */
    private Node transplant(Node nodeToReplace, Node newNode) {
        if (nodeToReplace.parent == null) {
            this.root = newNode;
        } else if (nodeToReplace == nodeToReplace.parent.left) {
            nodeToReplace.parent.left = newNode;
        } else {
            nodeToReplace.parent.right = newNode;
        }
        if (newNode != null) {
            newNode.parent = nodeToReplace.parent;
        }
        return newNode;
    }

    /**
     * 数据查找
     * @param element
     * @return true if tree contains element.
     */
    public boolean contains(int element) {
        return search(element) != null;
    }

    /**
     * @return Minimum element in tree.
     */
    public int getMinimum() {
        return getMinimum(root).value;
    }

    /**
     * @return Maximum element in tree.
     */
    public int getMaximum() {
        return getMaximum(root).value;
    }

    /**
     * Get next element element who is bigger than provided element.
     * 
     * @param element
     *            Element for whom descendand element is searched
     * @return Successor value.
     */
    // TODO Predecessor
    public int getSuccessor(int element) {
        return getSuccessor(search(element)).value;
    }

    /**
     * @return Number of elements in the tree.
     */
    public int getSize() {
        return size;
    }

    /**
     * 中序遍历
     */
    public void printTreeInOrder() {
        printTreeInOrder(root);
    }

    /**
     * 前序遍历
     */
    public void printTreePreOrder() {
        printTreePreOrder(root);
    }

    /**
     * 后序遍历
     */
    public void printTreePostOrder() {
        printTreePostOrder(root);
    }

    /*-------------------PRIVATE HELPER METHODS-------------------*/

    private void printTreeInOrder(Node entry) {
        if (entry != null) {
            printTreeInOrder(entry.left);
            if (entry.value != null) {
                System.out.print(entry.value+" ");
            }
            printTreeInOrder(entry.right);
        }
    }

    private void printTreePreOrder(Node entry) {
        if (entry != null) {
            if (entry.value != null) {
                System.out.print(entry.value+" ");
            }
            //改成这种方式
            printTreePreOrder(entry.left);
            printTreePreOrder(entry.right);
            //这里遍历有问题
            //printTreeInOrder(entry.left);
            //printTreeInOrder(entry.right);
        }
    }

 
    private void printTreePostOrder(Node entry) {
        if (entry != null) {
        	//改成这种方式
        	printTreePostOrder(entry.left);
        	printTreePostOrder(entry.right);
        	//这里遍历有问题
        	//printTreeInOrder(entry.left);
        	//printTreeInOrder(entry.right);
            if (entry.value != null) {
                System.out.print(entry.value+" ");
            }
        }
    }

    protected Node getMinimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node getMaximum(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private Node getSuccessor(Node node) {
        // if there is right branch, then successor is leftmost node of that
        // subtree
        if (node.right != null) {
            return getMinimum(node.right);
        } else { // otherwise it is a lowest ancestor whose left child is also
            // ancestor of node
            Node currentNode = node;
            Node parentNode = node.parent;
            while (parentNode != null && currentNode == parentNode.right) {
                // go up until we find parent that currentNode is not in right
                // subtree.
                currentNode = parentNode;
                parentNode = parentNode.parent;
            }
            return parentNode;
        }
    }
    
    //-------------------------------- TREE PRINTING ------------------------------------

    /**
     * 以图形的形式将二叉树打印到控制台
     */
    public void printTree() {
        printSubtree(root);
    }
    
    public void printSubtree(Node node) {
        if (node.right != null) {
            printTree(node.right, true, "");
        }
        printNodeValue(node);
        if (node.left != null) {
            printTree(node.left, false, "");
        }
    }
    
    private void printNodeValue(Node node) {
        if (node.value == null) {
            System.out.print("<null>");
        } else {
            System.out.print(node.value.toString());
        }
        System.out.println();
    }
    
    private void printTree(Node node, boolean isRight, String indent) {
        if (node.right != null) {
            printTree(node.right, true, indent + (isRight ? "        " : " |      "));
        }
        System.out.print(indent);
        if (isRight) {
            System.out.print(" /");
        } else {
            System.out.print(" \\");
        }
        System.out.print("----- ");
        printNodeValue(node);
        if (node.left != null) {
            printTree(node.left, false, indent + (isRight ? " |      " : "        "));
        }
    }


    public static class Node {
        public Node(Integer value, Node parent, Node left, Node right) {
            super();
            this.value = value;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public Integer value;
        public Node parent;
        public Node left;
        public Node right;
        
        public boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

    }
}
