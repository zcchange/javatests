package alitest;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;

/**
 * //请用java实现以下shell脚本的功能
 * //cat /home/admin/logs/error.log |grep "Exception" | sort | uniq -c | sort -r | head -n 10
 * //（输入：文件路径，输出：运行结果）
 */

//考察：shell命令拆解、stream流编程、大文件拆解并发工作，多线程找出关键字字符串，合并，后续处理
public class Solution {

    private final String keyWord = "Exception";

    //分配多少线程并行处理找关键字，可调优
    private final int threadNum = 4;


    //分段大小,100M为大小
    private final int sizeOfSplit = 1000000000;


    private ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(threadNum);


    private List<SplitTask> tasks = new ArrayList<>();

    private List<Future> futures = new ArrayList<>();



    public final String path = "/Users/zhengcheng/Downloads/yes/2.mp4";



    //Caller任务定义,返回带有关键字的句子
    class SplitTask implements Callable<String> {

        int position;

        RandomAccessFile file;

        SplitTask(int position,RandomAccessFile randomAccessFile) {
            this.position = position;
            this.file = randomAccessFile;
        }


        //把数据流中的关键字字段筛选出来
        @Override
        public String call() throws Exception {
            StringBuffer str = new StringBuffer();
            MappedByteBuffer byteBuffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY,position,sizeOfSplit);
            while (byteBuffer.limit() != byteBuffer.capacity()) {
                StringBuffer strLine = new StringBuffer();
                while (true) {
                    char n = byteBuffer.getChar();
                    if(!"\n".equals(n) && !"\r".equals(n)) {

                        strLine.append(n);
                     } else {
                        break;
                    }
                }
                if (strLine.toString().contains(keyWord)) {
                    str.append(strLine);
                }
            }
            return str.toString();
        }
    }




    public void splitFiles(String path) throws Exception{

        RandomAccessFile file = new RandomAccessFile(path,"r");
        System.out.println(file.length());
        //做拆分策略
        for(int positon = 0; positon < file.length(); positon +=sizeOfSplit) {
            SplitTask splitTask = new SplitTask(positon,file);
            tasks.add(splitTask);
            Future<String> future = threadPoolExecutor.submit(splitTask);
            futures.add(future);
        }

        //过滤后的String
        StringBuffer stringBufferFilted = new StringBuffer();

        for(int i = 0; i < futures.size(); i++) {
            stringBufferFilted.append(futures.get(i).get());
        }

        //最后做整体过滤


    }


    /**
     *
     * @param path
     * @return
     */
//    public String shellLog(String path) {
//        try {
//            List<String> resultList = reverseList(sortDistinct(fileToLines(path)));
//            StringBuffer result = new StringBuffer();
//            resultList.forEach( s -> {
//                result.append(s).append("\n");
//            });
//            return result.toString();
//        }catch (Exception e){
//            e.printStackTrace();
//            return "";
//        }
//    }


    /**
     * 文件依次读行，过滤掉不含Exception的行
     * @param path
     * @return
     */

    private List<String> fileToLines(String path) {
        List<String> keyWordLines = new LinkedList<>();

        FileReader fr = null;
        //
        try {
            fr = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        BufferedReader br= new BufferedReader(fr);

        String str;

        try {
            while ((str = br.readLine()) != null) {
                if (str.contains(keyWord)) {
                    keyWordLines.add(str);
                }
            }
            fr.close();
            br.close();
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return keyWordLines;
    }


    /**
     * list排序，去重，加重复个数
     * @param src
     * @return
     */
    private List<String> sortDistinct(List<String> src) {

        List<String> linkedResult = new LinkedList<>();

        Map<String,Integer> distinctResults = new TreeMap<>(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        src.forEach(s -> {
            if (distinctResults.containsKey(s)) {
                Integer num = distinctResults.get(s);
                distinctResults.put(s, num + 1);

            } else {
                distinctResults.put(s,1);
            }
        });
        Iterator<Map.Entry<String, Integer>> it = distinctResults.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            linkedResult.add(entry.getValue() + " " + entry.getKey());
        }
        return linkedResult;
    }


    /**
     * list倒置并取前十条
     * @param src
     */
    private List<String> reverseList(List<String> src) {
        List<String> listResult = new LinkedList<>();

        Collections.reverse(src);

        if(src.size() > 10){
            //取前十条
            for(int i = 0 ; i < 10 ; i++) {
                listResult.add(src.get(i));
            }
            return listResult;

        }else {
            return src;
        }

    }


    public static void main(String[] args) {
        Solution solution = new Solution();
        try {
            solution.splitFiles(solution.path);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
