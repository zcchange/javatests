package testjavamodule;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;

/**
 *
 */
public class TestConcurrentQueue {

    ConcurrentLinkedQueue<Data> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
    LongAdder longAdder = new LongAdder();
    Producer producer;
    Consumer consumer;

    static class Data {
        String msg;

        public Data(String msg) {
            this.msg = msg;
        }
    }

    TestConcurrentQueue(int size) {
        producer = new Producer(size);
        consumer = new Consumer(size);
    }

    public void startTest() {
        producer.startProduce();
        consumer.startConsume();
    }

    public long stopTestAndReturn() {
        producer.stopProduce();
        consumer.stopConsume();
        return longAdder.longValue();
    }


    class Producer{

        ExecutorService service;

        List<ProduceWorker> workers = new LinkedList<>();

        Producer(int concurrentNum) {
            service = Executors.newFixedThreadPool(4);
            for(int i = 0; i < concurrentNum; i++) {
                workers.add(new ProduceWorker());
            }
        }

        public void startProduce() {
            workers.forEach(worker -> service.execute(worker));
        }

        public void stopProduce() {
            service.shutdownNow();
        }



        class ProduceWorker implements Runnable {

            @Override
            public void run() {
                for(;;) {
                    if(!Thread.currentThread().isInterrupted()) {
//                        try {
//                            Thread.currentThread().sleep(50);
//                        } catch (InterruptedException e) {
//                            break;
//                        }
                        concurrentLinkedQueue.offer(new Data(randomString(10)));
                    } else {
                        break;
                    }
                }
            }
        }

    }


    class Consumer{
        ExecutorService service;

        List<ConsumeWorker> workers = new LinkedList<>();

        Consumer(int concurrentNum) {
            service = Executors.newFixedThreadPool(concurrentNum);
            for(int i = 0; i < concurrentNum; i++) {
                workers.add(new ConsumeWorker());
            }
        }

        class ConsumeWorker implements Runnable {

            @Override
            public void run() {
                for(;;) {
                    if(!Thread.currentThread().isInterrupted()) {
//                        try {
//                            Thread.currentThread().sleep(50);
//                        } catch (InterruptedException e) {
//                            break;
//                        }
                        Data data = concurrentLinkedQueue.poll();
                        if (null != data) {
                            longAdder.increment();
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        public void startConsume() {
            workers.forEach(worker -> service.execute(worker));
        }

        public void stopConsume() {
            service.shutdownNow();
        }
    }

    static String randomString(int strLength) {
        Random rnd = ThreadLocalRandom.current();
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < strLength; i++) {
            boolean isChar = (rnd.nextInt(2) % 2 == 0);// 输出字母还是数字
            if (isChar) { // 字符串
                int choice = rnd.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
                ret.append((char) (choice + rnd.nextInt(26)));
            } else { // 数字
                ret.append(Integer.toString(rnd.nextInt(10)));
            }
        }
        return ret.toString();
    }


    public static void main(String[] args) {
        long timeStart = System.currentTimeMillis();
        System.out.println(timeStart);
        TestConcurrentQueue testQueue = new TestConcurrentQueue(1000);
        testQueue.startTest();
        try {
            Thread.currentThread().sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long result = testQueue.stopTestAndReturn();
        System.out.println("最终结果为; " + result);
        long timeEnd = System.currentTimeMillis();
        System.out.println(timeEnd);
        //计算每s吞吐
        double average = (result / (timeEnd -timeStart)) * 1000;
        System.out.println("2每秒吞吐: " + average);
    }


}
