package alitest;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * //请用java实现以下shell脚本的功能
 * //cat /home/admin/logs/error.log |grep "Exception" | sort | uniq -c | sort -r | head -n 10
 * //（输入：文件路径，输出：运行结果）
 */

//考察：shell命令拆解、stream流编程、大文件拆解并发工作
public class Solution {

    private final String keyWord = "Exception";

    public final String path = "/Users/zhengcheng/Downloads/yes/2.mp4";




    public void splitFiles(String path) throws Exception{

        int size = 10000;
        byte[] bytes = new byte[size];
        RandomAccessFile file = new RandomAccessFile(path,"r");
        System.out.println(file.length());
        MappedByteBuffer byteBuffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY,10,size);
        byteBuffer.get(bytes);
        String result = new String(bytes,"UTF-8");
        System.out.println(result);
    }


    /**
     *
     * @param path
     * @return
     */
    public String shellLog(String path) {
        try {
            List<String> resultList = reverseList(sortDistinct(fileToLines(path)));
            StringBuffer result = new StringBuffer();
            resultList.forEach( s -> {
                result.append(s).append("\n");
            });
            return result.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }


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
