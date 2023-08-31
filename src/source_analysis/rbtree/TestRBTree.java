package source_analysis.rbtree;

import java.util.Scanner;

/**
 * @Author: bighao周启豪
 * @Date: 2020/5/17 11:11
 * @Version 1.0
 */
public class TestRBTree {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        RBTree<String, Object> rbt = new RBTree<>();

        while (true) {
            System.out.println("请输入key");
            String key = sc.next();
            System.out.println();
            rbt.insert(key, null);

            TreeOperation.show(rbt.getRoot());
        }
    }
}
