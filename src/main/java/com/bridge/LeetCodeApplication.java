package com.bridge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class LeetCodeApplication {

    public static void main(String[] args) {
        //	Weekly 422
//        isBalanced();
        minTimeToReach();
	}

    public static boolean isBalanced() {
        String num = "1234";
        num = "24123";

        int result = 0;
        for (int i = 0; i < num.length(); i++) {
            int digit = num.charAt(i) - '0';
            if (i % 2 == 0) {
                result += digit;
            } else {
                result -= digit;
            }
        }

        System.out.println("result : " + result + " , isBalanced : " + (result == 0));
        return result == 0;
    }

    public static int minTimeToReach() {
        int[][] moveTime = { { 0, 4 }, { 4, 4} };
//        int[][] moveTime = { { 0, 0, 0 }, { 0, 0, 0} };
//        int[][] moveTime = { { 0, 1 }, { 1, 2} };
//        int[][] moveTime = { { 27, 85 }, { 22, 53 } };

		int result = minTimeByDFS(moveTime, 0, 0, 0);
        System.out.println(result);
        return result;
    }

    private static int minTimeByDFS(int[][] moveTime, int currentI, int currentJ, int currentT) {
        int next = 0;
        if (currentJ < moveTime[0].length - 1) {
            //	move right
            int cost = moveTime[currentI][currentJ + 1];
            next = (currentT >= cost) ? currentT + 1 : cost + 1;
            return minTimeByDFS(moveTime, currentI, currentJ + 1, next);
        }
        if (currentI < moveTime.length - 1) {
            //	move bottom
            int cost = moveTime[currentI + 1][currentJ];
            next = (currentT >= cost) ? currentT + 1 : cost + 1;
            return minTimeByDFS(moveTime, currentI + 1, currentJ, next);
        }
        return currentT;

//        if (currentJ == moveTime[0].length - 1 && currentI == moveTime.length - 1) {
//            return currentT;
//        }
//
//        int moveRightTime = 0;
//        if (currentJ < moveTime[0].length - 1) {
//            //	move right
//            int cost = moveTime[currentI][currentJ + 1];
//            moveRightTime = (currentT >= cost) ? currentT + 1 : cost + 1;
//        }
//
//        int moveBottomTime = 0;
//        if (currentI < moveTime.length - 1) {
//            //	move bottom
//            int cost = moveTime[currentI + 1][currentJ];
//            moveBottomTime = (currentT >= cost) ? currentT + 1 : cost + 1;
//        }
//
//        if (moveRightTime < moveBottomTime) {
//            return minTimeByDFS(moveTime, currentI, currentJ + 1, moveRightTime);
//        } else {
//            return minTimeByDFS(moveTime, currentI + 1, currentJ, moveBottomTime);
//        }
    }
}
