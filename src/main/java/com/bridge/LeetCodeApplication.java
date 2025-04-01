package com.bridge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

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
//        subarraySum();

        //  Biweekly 151
//        transformArray();

        //  weekly 439
//        largestInteger();

//        Biweekly 152
//        totalNumbers();

        //  Weekly 422
        maxContainers();
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

    public static int[] transformArray() {
//        int[] nums = { 4,3,2,1 };
        int[] nums = {1,5,1,4,2};

        int size = nums.length;
        int[] results = new int[size];

        int evenCount = 0;
        for (int i = 0; i < size; i++) {
            if (nums[i] % 2 == 0) {
                evenCount++;
            }
        }

        for (int i = 0; i < size; i++) {
            if (i < evenCount) {
                results[i] = 0;
            } else {
                results[i] = 1;
            }

            System.out.println(results[i]);
        }

        return results;
    }

    public static int largestInteger() {
//        int[] nums = {3,9,2,1,7};
//        int k = 3;
//        int[] nums = {3,9,7,2,1,7};
//        int k = 4;
        int[] nums = {0,0};
        int k = 1;
//        k = 2;
//        int[] nums = {0,50};
//        int k = 1;

        // brute force
        Map<Integer, Integer> appear = new HashMap<>();
        for (int i = 0; i < nums.length - k + 1; i++) {
            Set<Integer> hasCount = new HashSet<>();
            for (int j = i; j < i + k; j++) {
                int key = nums[j];
                if (hasCount.contains(key)) {
                } else {
                    if (appear.containsKey(key)) {
                        appear.put(key, appear.get(key) + 1);
                    } else {
                        appear.put(key, 1);
                    }
                    hasCount.add(key);
                }
            }
        }

        int result = -1;
        for (Map.Entry<Integer, Integer> entry : appear.entrySet()) {
            if (entry.getValue() == 1) {
                result = Math.max(result, entry.getKey());
            }
        }

//        //  smarter
//        int length = nums.length;
//        int candidate = (length - k) / 2;
//        Set removeSet = new HashSet();
//        for (int i = candidate; i < length - candidate; i++) {
//            removeSet.add(nums[i]);
//        }
//
////        candidate = candidate == 0 ? 1 : candidate;
//        int result = -1;
//        if (length == k) {
//            for (int i = 0; i < length; i++) {
//                result = Math.max(result, nums[i]);
//            }
//        } else {
//            for (int i = 0; i < candidate; i++) {
//                if (removeSet.contains(nums[i])) {
//                    continue;
//                }
//                result = Math.max(result, nums[i]);
//            }
//
//            for (int i = length - candidate; i < length; i++) {
//                if (removeSet.contains(nums[i])) {
//                    continue;
//                }
//                result = Math.max(result, nums[i]);
//            }
//        }

        System.out.println(result);
        return result;
    }

    public static int longestPalindromicSubsequence() {
        String s = "abced";
        int k = 2;

        int result = -1;
        return result;
    }

    public static void totalNumbers() {
//        int[] digits = {1, 2, 3, 4};
//        int[] digits = {0, 2, 2};
//        int[] digits = {6, 6, 6};
        int[] digits = {1, 3, 5};

        Set<Integer> nums = new HashSet<>();
        int length = digits.length;
        int result = 0;
        for (int i = 0; i < length; i++) {
            if (digits[i] == 0) {
                continue;
            }
            for (int j = 0; j < length; j++) {
                if (j == i) continue;
                for (int k = 0; k < length; k++) {
                    if (k == i || k == j) continue;
                    if (digits[k] % 2 == 0) {
                        int num = digits[i] * 100 + digits[j] * 10 + digits[k];
                        nums.add(num);
                    }
                }
            }
        }
//        for (int i = 0; i < length - 2; i++) {
//            if (digits[i] == 0) {
//                continue;
//            }
//            for (int j = i + 1; j < length - 1; j++) {
//                for (int k = j + 1; k < length; k++) {
//                    if (digits[k] % 2 == 0) {
//                        int num = digits[i] * 100 + digits[j] * 10 + digits[k];
//                        nums.add(num);
//                    }
//                }
//            }
//        }

        System.out.println("count : " + nums.size());
    }

    public static int maxContainers() {
        int n = 2;
        n = 3;
        int w = 3;
        w = 5;
        int maxWeight = 15;
        maxWeight = 20;

        int result = maxWeight / w;
        result = result > n * n ? n * n : result;

        System.out.println(result);
        return result;
    }
}
