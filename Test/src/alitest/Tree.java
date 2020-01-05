package alitest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

//>给一个数组，生成一颗树。最大值在树的最上面,其左边数据在左边子树,其右边数据在右边子树,子树的规律相同。
  //      > 例如数组[3,2,1,6,0,5]生成树：
    //    >     6
      //  >   /   \
        //>  3     5
      //  >   \    /
       // >    2  0
        //>      \
        //>       1
       // >

//考察：算法理解、算法优化
public class Tree {

    public static void main(String[] args) {
        Tree tree = new Tree();
        Integer[] arrays = new Integer[]{3,2,1,6,0,5,10,1,29};
//        Integer[] arrays = new Integer[] {2,3};
//        TreeNode treeNode = tree.solution(arrays);
        TreeNode treeNode = tree.generateTree(arrays,0,arrays.length - 1);
        System.out.println(treeNode.toString());
    }


    /**
     *
     * @param arrays
     * @return
     */
    public TreeNode solution(Integer[] arrays) {
        return generateTree(new LinkedList<>(Arrays.asList(arrays)));
    }


    /**
     * 具体算法
     * @param arrays
     * @param low
     * @param high
     * @return
     */
    public TreeNode generateTree(Integer[] arrays, int low, int high) {
        if (low > high) {
            return null;
        }

        int maxPosition = low;
        int maxValue = arrays[low];

        for(int k = low; k <=high; k++) {
            if (arrays[k] > maxValue) {
                maxPosition = k;
                maxValue = arrays[k];
            }
        }
        TreeNode treeNode = new TreeNode();
        treeNode.setValue(maxValue);

        treeNode.setLeft(generateTree(arrays,low,maxPosition - 1));
        treeNode.setRight(generateTree(arrays,maxPosition + 1, high));
        return treeNode;
    }


    /**
     *
     * @return
     */
    public TreeNode generateTree(LinkedList<Integer> in) {

        LinkedList<Integer> left = new LinkedList<>();
        LinkedList<Integer> right = new LinkedList<>();

        TreeNode treeNode = new TreeNode();
        treeNode.setValue(getMaxOfArrays(in, left ,right));
        if(!left.isEmpty()) {
            treeNode.setLeft(generateTree(left));
        }
        if(!right.isEmpty()) {
            treeNode.setRight(generateTree(right));
        }
        return treeNode;
    }


    /**
     * 返回最大值
     * @param in
     * @param left 拆分后的左边序列
     * @param right 拆分后的右边序列
     * @return
     */
    public Integer getMaxOfArrays(List<Integer> in , List<Integer> left , List<Integer> right) {

        Integer max = 0;
        int position = 0;

        for (int i = 0 ; i < in.size(); i++) {
            if(in.get(i) > max) {
                max = in.get(i);
                position = i;
            }
        }

        if(position != 0) {
            for (int i = 0; i < position; i++) {
                left.add(in.get(i));
            }
        }

        if(position != in.size() -1) {
            for (int i = position + 1; i < in.size(); i++) {
                right.add(in.get(i));
            }
        }
        //回收原输出list，避免占用内存
        in.clear();
        return max;

    }




     public class TreeNode {
        TreeNode parent;
        TreeNode left;
        TreeNode right;
        Integer value;

        /**
         * 是否有父节点
         *
         * @return
         */
        public boolean hasParent() {
            return null != parent;
        }

        public TreeNode getParent() {
            return parent;
        }

        public void setParent(TreeNode parent) {
            this.parent = parent;
        }

        public TreeNode getLeft() {
            return left;
        }

        public void setLeft(TreeNode left) {
            this.left = left;
        }

        public TreeNode getRight() {
            return right;
        }

        public void setRight(TreeNode right) {
            this.right = right;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        /**
         * 打印
         *
         * @return
         */
        @Override
        public String toString() {
            StringBuffer str = new StringBuffer();
            if (null != this.left) {
                str.append(this.left.toString()).append(" < - ");
            }
            str.append(this.value);
            if (null != this.right) {
                str.append(" - > ").append(this.right.toString());
            }
            return str.toString();

        }
    }
}
