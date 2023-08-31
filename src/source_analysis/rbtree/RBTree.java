package source_analysis.rbtree;

/**
 * @Author: bighao周启豪
 * @Date: 2020/5/16 23:27
 * @Version 1.0
 *
 * 红黑树
 *
 * 1.创建RBTree，定义颜色
 * 2.创建RBNode
 * 3.辅助方法定义：prantOf(node)，isRed(node)，setRed(node)，setBlack(node)，inOrderPrint()
 * 4.左旋方法定义：leftRotate(node)
 * 5.右旋方法定义：rightRotate(node)
 * 6.公开插入接口方法定义：insert(K key, V vlaue)
 * 7.内部插入接口方法定义：insert(RBNode node)
 * 8.修正插入导致红黑树失衡的方法定义：insertFixUp(RBNode node)
 * 9.测试红黑树的正确性
 *
 * https://www.cnblogs.com/lycroseup/p/7324229.html
 *
 */
public class RBTree<K extends Comparable<K>, V> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    /** 根节点 */
    private RBNode<K, V> root;

    public RBNode<K, V> getRoot() {
        return root;
    }

    static class RBNode<K extends Comparable<K>, V> {
        private RBNode<K, V> parent, left, right;
        private boolean color;
        private K key;
        private V value;

        public RBNode() {}

        public RBNode(RBNode<K, V> parent, RBNode<K, V> left, RBNode<K, V> right, boolean color, K key, V value) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.color = color;
            this.key = key;
            this.value = value;
        }

        public RBNode<K, V> getParent() {
            return parent;
        }

        public RBNode<K, V> getLeft() {
            return left;
        }

        public RBNode<K, V> getRight() {
            return right;
        }

        public boolean isColor() {
            return color;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }






    /**
     * 获取当前节点的父节点
     */
    private RBNode parentOf(RBNode node) {
        if (node != null) {
            return node.parent;
        }
        return null;
    }


    /**
     * 节点是否为红色
     */
    private boolean isRed(RBNode node) {
        return (node != null && node.color == RED) ? true : false;
    }

    /**
     * 节点是否为黑色
     */
    private boolean isBlack(RBNode node) {
        return !isRed(node);
    }

    /**
     * 设置节点为红色
     */
    private void setRed(RBNode node) {
        if (node != null) {
            node.color = RED;
        }
    }


    /**
     * 设置节点为黑色
     */
    private void setBlack(RBNode node) {
        if (node != null) {
            node.color = BLACK;
        }
    }


    /**
     * 中序遍历打印二叉树
     */
    public void inOrderPrint() {
        inOrderPrint(this.root);
    }

    private void inOrderPrint(RBNode root) {
        if (root != null) {
            inOrderPrint(root.left);
            System.out.println("key:" + root.key + ",value" + root.value);
            inOrderPrint(root.right);
        }
    }


    /**
     * 左旋
     * @param x 当前节点
     *
     * 左旋示意图(对节点x进行左旋)：
     *     px                              px
     *     /                               /
     *    x                               y
     *   / \         左旋==>             / \
     *  lx  y                           x  ry
     *     / \                         / \
     *    ly  ry                      lx  ly
     *
     * 1.将x的右子节点更新为y的左子节点ly，并将y的左子节点ly的父节点更新为x
     * 2.更新y的父节点为x的父节点px，当x的父节点不为空时，将x的父节点px的子树（当前x节点的位置）更新为y，
     *          如果x的父节点为空，就将根节点指向y
     * 3.将x的父节点更新为y，将y的左子节点更新为x
     */
    private void leftRotate(RBNode x) {
        RBNode y = x.right;
        // 1.将x的右子节点更新为y的左子节点ly，并将y的左子节点ly的父节点更新为x
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }

        // 2.更新y的父节点为x的父节点px，当x的父节点不为空时，将x的父节点px的子树（当前x节点的位置）更新为y，如果x的父节点为空，就将根节点指向y
        y.parent = x.parent;
        if (x.parent != null) {
            if (x == x.parent.left) {
                x.parent.left = y;  // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
            } else {
                x.parent.right = y; // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
            }
        } else {
            // 如果x的父节点为空 即x为根节点，就将根节点指向y
            this.root = y;
        }

        // 3.将x的父节点更新为y，将y的左子节点更新为x
        x.parent = y;
        y.left = x;
    }


    /**
     * 右旋转
     * @param y 当前节点
     *
     * 对红黑树的节点(y)进行右旋转
     *
     * 右旋示意图(对节点y进行左旋)：
     *        py                       py
     *        /                         /
     *       y                         x
     *      / \       右旋==>         /  \
     *     x   ry                    lx  y
     *    / \                           / \
     *   lx  rx                        rx  ry
     *
     * 1.将y的左子节点指向x的右子节点，并且更新x的右子节点的父节点为y
     * 2.更新x的父节点为y的父节点py，当y的父节点py不为空时，将y的父节点py的子树（当前y的位置）更新为x，
     *          如果x的父节点为空，就将根节点指向x
     * 3.更新y的父节点为x，更新x的右子节点为y
     */
    private void rightRotate(RBNode y) {
        RBNode x = y.left;
        // 1.将y的左子节点指向x的右子节点，并且更新x的右子节点的父节点为y
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }

        // 2.更新x的父节点为y的父节点py，当y的父节点py不为空时，将y的父节点py的子树（当前y的位置）更新为x，
        x.parent = y.parent;
        if (y.parent != null) {
            if (y == y.parent.left) {
                y.parent.left = x;
            } else {
                y.parent.right = x;
            }
        } else {
            this.root = x;
        }

        // 3.更新y的父节点为x，更新x的右子节点为y
        y.parent = x;
        x.right = y;
    }


    /**
     * 公开的插入方法
     */
    public void insert(K key, V value) {
        RBNode node = new RBNode();
        node.key = key;
        node.value = value;
        // 新建节点一定是红色!!
        node.color = RED;
        inOrderPrint(node);
    }

    /**
     * 内部插入方法
     * @param node 要插入的节点
     */
    private void insert(RBNode node) {
        // 1.查找当前节点的父节点
        RBNode parent = null;
        RBNode x = root;

        // 遍历树查找父节点
        while (x != null) {
            parent = x;

            // cmp > 0 说明node.key 大于 x.key，需要到右子树查找
            // cmp == 0 说明node.key 等于 x.key，需要进行值的替换操作，并返回
            // cmp < 0 说明node.key 小于 x.key，需要到左子树查找
            int cmp = node.key.compareTo(x.key);
            if (cmp > 0) {
                x = x.right;
            } else if (cmp == 0) {
                x.value = node.value;
                return;
            } else {
                x = x.left;
            }
        }

        node.parent = parent;
        if (parent != null) {
            // 判断node与parent的key谁大
            int cmp = node.key.compareTo(parent.key);
            if (cmp > 0) {
                // 当node.key > parent.key，需要把node放到parent的右子节点
                parent.right = node;
            } else {
                // 当node.key < parent.key，需要把node放到parent的右子节点
                parent.left = node;
            }
        } else {
            // 说明是第一次插入
            this.root = node;
        }

        // 需要调用修复红黑树平衡方法
        insertFixUp(node);
    }


    /**
     * 插入后修复红黑树平衡的方法
     *      |---情景1：红黑树为空树，         将根节点染色为黑色
     *      |---情景2：插入节点的key已结存在， 不需要处理
     *      |---情景3：插入节点的父节点为黑色， 因为你所插入的路径，黑色节点没有变化，所以红黑树依然平衡，不需要处理
     *
     *      情景4 需要去处理
     *      |---情景4：插入节点的父节点为红色
     *          |---情景4.1：叔叔节点存在，并且为红色（父-叔 双红）,
     *                      将爸爸和叔叔染为黑色，爷爷染为红色，并且再以爷爷节点为当前节点，进行下一轮处理
     *
     *          |---情景4.2：叔叔节点不存在，或者为黑色，父节点为爷爷节点的左子树
     *              |---情景4.2.1：插入节点为其父节点的左子节点（LL情况）
     *                      将爸爸染为黑色，将爷爷染为红色，然后以爷爷节点进行右旋，就完成了
     *              |---情景4.2.2：插入节点为其父节点的右子节点（LR情况）
     *                      以爸爸节点进行左旋，得到LL双红的情景(4.2.1)，然后指定爸爸节点为当前节点，进行下一轮处理
     *
     *          |---情景4.3：叔叔节点不存在，或者为黑色，父节点为爷爷节点的右子树
     *              |---情景4.3.1：插入节点为其父节点的右子节点（RR情况）
     *                      将爸爸染为黑色，爷爷染为红色，然后以爷爷节点左旋，就完成了
     *              |---情景4.3.2：插入节点为其父节点的左子节点（RL情况）
     *                      以爸爸节点进行一次右旋，得到RR双红的情景（4.3.1），然后指定爸爸节点为当前节点，进行下一轮处理
     *
     */
    private void insertFixUp(RBNode node) {
        this.root.color = BLACK;

        RBNode parent = parentOf(node); // 爸爸节点
        RBNode gparent = parentOf(parent); // 爷爷节点

        // 情景4：插入节点的父节点为红色
        if (parent != null && isRed(parent)) {
            // 如果父节点是红色，那么一定存在爷爷节点，因为根节点不可能是红色
            RBNode uncle = null;

            // 如果爸爸在爷爷的左边，那么uncle就在爷爷的右边
            if (parent == gparent.left) {
                uncle = gparent.right;

                // 情景4.1：叔叔节点存在，并且为红色（父-叔 双红）
                if (uncle != null && isRed(uncle)) {
                    // 将爸爸和叔叔染为黑色，爷爷染为红色，并且再以爷爷节点为当前节点，进行下一轮处理
                    setBlack(parent);
                    setBlack(parent);
                    setRed(gparent);
                    insertFixUp(gparent);
                    return;
                }

                // 情景4.2：叔叔节点不存在，或者为黑色，父节点为爷爷节点的左子树
                if (uncle == null || isBlack(uncle)) {
                    // 情景4.2.1：插入节点为其父节点的左子节点（LL情况）
                    // 将爸爸染为黑色，将爷爷染为红色，然后以爷爷节点进行右旋，就完成了
                    if (node == parent.left) {
                        setBlack(parent);
                        setRed(gparent);
                        rightRotate(gparent);
                        return;
                    }

                    // 情景4.2.2：插入节点为其父节点的右子节点（LR情况）
                    // 以爸爸节点进行左旋，得到LL双红的情景(4.2.1)，然后指定爸爸节点为当前节点，进行下一轮处理
                    if (node == parent.right) {
                        leftRotate(parent);
                        insertFixUp(parent);
                        return;
                    }
                }

            } else {
                // 爸爸在爷爷的右边，那么uncle就在爷爷的左边
                uncle = gparent.left;

                // 情景4.1：叔叔节点存在，并且为红色（父-叔 双红）
                if (uncle != null && isRed(uncle)) {
                    // 将爸爸和叔叔染为黑色，爷爷染为红色，并且再以爷爷节点为当前节点，进行下一轮处理
                    setBlack(parent);
                    setBlack(parent);
                    setRed(gparent);
                    insertFixUp(gparent);
                    return;
                }

                // 情景4.3：叔叔节点不存在，或者为黑色，父节点为爷爷节点的右子树
                if (uncle == null || isBlack(uncle)) {

                    // 情景4.3.1：插入节点为其父节点的右子节点（RR情况）
                    // 将爸爸染为黑色，爷爷染为红色，然后以爷爷节点左旋，就完成了
                    if (node == parent.right) {
                        setBlack(parent);
                        setRed(gparent);
                        leftRotate(gparent);
                        return;
                    }

                    // 情景4.3.2：插入节点为其父节点的左子节点（RL情况）
                    // 以爸爸节点进行一次右旋，得到RR双红的情景（4.3.1），然后指定爸爸节点为当前节点，进行下一轮处理
                    if (node == parent.left) {
                        rightRotate(parent);
                        insertFixUp(parent);
                        return;
                    }
                }

            }
        }
    }

}
