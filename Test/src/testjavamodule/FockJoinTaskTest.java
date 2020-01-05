package testjavamodule;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * java 内部fockjoin分而治之  任务队列
 */
public class FockJoinTaskTest {


    private ForkJoinPool pool = new ForkJoinPool();

    public Integer caculate(int[] arrar) {
        try {
            return pool.submit(new Sum(arrar,0,arrar.length)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void shutDown() {
        if (null != pool && !pool.isShutdown()) {
            pool.shutdown();
        }
    }



    class Sum extends RecursiveTask<Integer> {

        private int[] array;

        private int from;

        private int to;



        Sum(int[] array,int from, int to) {
            this.array = array;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Integer compute() {
            int resutl = 0;
            if (to - from > 10) {
                int middle = from + (to - from)/2;
                Sum subSum = new Sum(array,from,middle);
                Sum subSum2 = new Sum(array,middle,to);
                subSum.fork();
                subSum2.fork();
                return subSum.join() + subSum2.join();
            } else {
                while (from < to) {
                    resutl += array[from];
                    from++;
                }
                return resutl;
            }
        }
    }

    public static void main(String[] args) {
        FockJoinTaskTest fockJoinTaskTest = new FockJoinTaskTest();
        System.out.println(fockJoinTaskTest.caculate(new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20}));
//        fockJoinTaskTest.shutDown();

    }
}
