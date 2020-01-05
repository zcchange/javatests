package algorithm;


import alitest.Tree;

/**
 * @author zhengcheng
 */
public class VolatileTest {

    public static boolean checkValue = false;

    public static int passValue = 1;

    public static int value = 1;


    public static void loadInteger() {
        for(int i = 0; i < 20000; i ++) {
            value = value + 2;
        }
    }



    static class Thread1 extends Thread {
        @Override
        public void run() {
            super.run();
            while (!checkValue) {
//                System.out.println(passValue);
            }
            System.out.println(value);
            System.out.println(passValue);


        }
    }


//    static class Thread2 extends Thread {
//        @Override
//        public void run() {
//            super.run();
//            passValue = 40;
//            loadInteger();
//            checkValue = true;
//
//
//
//
//        }
//    }

    public static void main(String[] args) throws InterruptedException{

        Thread1 thread1 = new Thread1();
        thread1.start();
        Tree tree = new Tree();
        Tree.TreeNode treeNode = tree.solution(new Integer[] {1});
//
//        Thread2 thread2 = new Thread2();
//        thread2.start();
        passValue = 40;
        loadInteger();
        checkValue = true;

        ///////

//        thread2.join();
//        thread1.join();

    }
}
