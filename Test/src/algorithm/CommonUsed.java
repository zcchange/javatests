package algorithm;


import java.util.Arrays;

import static java.lang.Math.max;

//动态规划dp
public class CommonUsed {


    //最大连续序列和
    int mlv(int[] arrays) {
        int maxValue = 0;
        int tempMaxValue = 0;

        int maxEndPosition = 0;

        for (int i = 0; i < arrays.length; i++) {
            tempMaxValue += arrays[i];
            if (tempMaxValue < 0) {
                tempMaxValue = 0;
            }

            if (tempMaxValue > maxValue) {
                maxValue = tempMaxValue;
                maxEndPosition = i;
            }
        }
        System.out.println(maxEndPosition);
        return maxValue;
    }



    //归并排序
    public void mergeSort(int[] arr, int low, int high) {
        if (low >= high)
            return;
        int mid = (high + low) / 2;
        mergeSort(arr, low, mid);
        mergeSort(arr, mid + 1, high);
        merge(arr, low, mid, high);
    }




    public void merge(int[] arr, int low, int mid, int high) {
        int[] aux = Arrays.copyOfRange(arr,low,high + 1);
        int left = low;
        int right = mid + 1;
        for(int k = low ; k <= high; k++) {
            if(left > mid) {
                //左半部分元素已经全部处理完毕
                arr[k] = aux[right - low];
                right++;
            } else if (right > high) {
                //右半部分元素已经全部处理完毕
                arr[k] = aux[left - low];
                left++;
            } else if (aux[left - low] < aux[right - low]) {
                //左半部分所指元素<右半部分所指元素
                arr[k] = aux[left - low];
                left++;
            } else {
                arr[k] = aux[right - low];
                right++;
            }
        }
    }








    //快速排序
    public void quickSort(int[] arr, int low, int high) {
        int i, j, temp, t;
        if (low > high) {
            return;
        }
        i = low;
        j = high;
        //temp就是基准位
        temp = arr[low];
        while (i < j) {
            //先看右边，依次往左递减
            while (temp <= arr[j] && i < j) {
                j--;
            }
            //再看左边，依次往右递增
            while (temp >= arr[i] && i < j) {
                i++;
            }
            //如果满足条件则交换
            if (i < j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }

        }
        //最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = temp;
        //递归调用左半数组
        quickSort(arr, low, j - 1);
        //递归调用右半数组
        quickSort(arr, j + 1, high);
    }


    //最大子序列
    int lcs(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int c[][] = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0 || j == 0) {
                    c[i][j] = 0;
                } else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    c[i][j] = c[i - 1][j - 1] + 1;
                } else {
                    c[i][j] = max(c[i - 1][j], c[i][j - 1]);
                }
            }
        }
        return c[len1][len2];
    }


    public static void main(String[] args) {
        CommonUsed picAlgo = new CommonUsed();
        int[] arrays = new int[]{1, 0, -1, 100, 2, 3, -1, 10, -9};
//        System.out.println(picAlgo.mlv(arrays));
        picAlgo.mergeSort(arrays,0,arrays.length - 1);
        System.out.println(arrays);
    }
}
