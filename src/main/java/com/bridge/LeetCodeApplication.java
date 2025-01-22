package com.bridge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class LeetCodeApplication {

    public static void main(String[] args) {
        //	Weekly 422
//        isBalanced();
//        minTimeToReach();

        //  BiWeekly 143
//        smallestNumber();
//        maxFrequency();

        //  Biweekly 147
//         hasMatch();

        //  Weekly 433
        subarraySum();
	}

    public static int smallestNumber() {
        int n = 10;
        int t = 2;
        n = 15;
        t = 3;
        n = 21;
        t = 4;

        int result;
        while (true) {
            result = find(n, t);
            if (result > 0) {
                break;
            }
            n++;
        }

        System.out.println("result : " + result);
        return result;
    }

    private static int find(int n, int t) {
        int n2 = n;
        int sum = 1;
        while (n2 != 0) {
            int digit = n2 % 10;
            sum *= digit;
            if (digit % t == 0 || sum % t == 0) {
//                System.out.println("result : " + n);
                return n;
            }
            n2 /= 10;
        }
        return -1;
    }

    public static int maxFrequency() {
        int[] nums = new int[]{ 1, 4, 5 };
        int k = 1;
        int numOperations = 2;

//        int[] nums = new int[] { 5,11,20,20};
//        k = 5;
//        numOperations = 1;

        HashMap<Integer, Integer> occurrences = new HashMap<>();
        int mostFrequentNum = nums[0];
        int maxAppearCount = 0;
        for (int i = 0; i < nums.length; i++) {
            int key = nums[i];
            int value = 0;
            if (occurrences.containsKey(key)) {
                value = occurrences.get(key);
            }
            occurrences.put(key, ++value);
            if (maxAppearCount < value) {
                maxAppearCount = value;
                mostFrequentNum = key;
            }
//            maxAppearCount = Math.max(maxAppearCount, value);
        }

        for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            if (key != mostFrequentNum && Math.abs(key - mostFrequentNum) <= k) {
                int increment = Math.min(numOperations, value);
                maxAppearCount += increment;

                numOperations -= value;
                if (numOperations <= 0) {
                    break;
                }
            }
        }

        System.out.println("result : " + maxAppearCount);
        return maxAppearCount;
    }

    public static boolean hasMatch() {
        String s = "leetcode";
        String p = "ee*e";

//        s = "car";
//        p = "c*v";

//        s = "luck";
//        p = "u*";

//        s = "ckckkk";
//        p = "ck*kc";

        boolean result = false;
        //  1st :
        if (p.indexOf("*") == 0) {
            p = p.substring(1);
            result = s.contains(p);
        } else if (p.indexOf("*") == p.length() - 1) {
            p = p.substring(0, p.length() - 1);
            result = s.contains(p);
        } else {
            String[] items = p.split("\\*");
            int index1 = s.indexOf(items[0]);
            int index2 = s.lastIndexOf(items[1]);
            if (index1 >= 0 && index2 >= 0 && index1 + items[0].length() - 1 < index2) {
                result = true;
//                System.out.println("true");
//                return true;
            }
        }

        System.out.println(result);
        return result;
    }

    public static int subarraySum() {
        int[] nums = {2,3,1};
//        int[] nums = {3,1,1,2};

        int result = nums[0];
        for (int i = 1; i < nums.length; i++) {
//            int[] subArray = new int[2];
            int start = Math.max(0, i - nums[i]);
//            int startIndex = nums[start];
//            int endIndex = i;
            for (int i2 = start; i2 <= i; i2++) {
                result += nums[i2];
            }
        }

        System.out.println(result);
        return result;
    }
}
