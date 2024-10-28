package com.bridge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import java.util.*;

@SpringBootApplication
@Slf4j
public class BridgeApplication {

    public static void main(String[] args) {
//		SpringApplication.run(BridgeApplication.class, args);

//		100340 .
//		int res = maxHeightOfTriangle(2, 4);	//	3, ok
////		res = maxHeightOfTriangle(2, 1);	//	2, ok
////		res = maxHeightOfTriangle(1, 1);	//	1, ok
//		res = maxHeightOfTriangle(10, 1);	//	1, ok
//		System.out.println("res : " + res);

//		100357.
//		int[] myIntArray = {1, 2, 3, 4};
//		int[] myIntArray = {1,2,1,1,2,1,2};
//		int[] myIntArray = {1,3};
//		int[] myIntArray = {1,5,9,4,2};
//		int res = maximumLength(myIntArray);
//		System.out.println("res : " + res);

		//	Biweekly
//		100381. Find the Number of Winning Players
//	    int result = findTheNumberOfWinningPlayers();
//		System.out.println(result);

		//	Weekly
//		100378. Design Neighbor Sum Service
//		designNeighborSumService();

		//	week 410
//		finalPositionOfSnake();

		//	Biweekly 137
//		findPower();
//		findPower2();
		//
//		generateKey();
//		stringHash();

		//	weekly 411
//		countKConstraintSubstrings();
//		maxEnergyBoost();

		//	weekly 412
//		getFinalState();

		//	Biweekly 140
//		Q1
//		minElement();
//		Q2
//		maximumTotalSum();
//		Q3
//		validSequence();

		//	weekly 418
//		Q1
//		maxGoodNumbers();

        //  Biweekly 141
//        Q1
//        minBitwiseArray();

        //  Weekly 419
        //  Q1
//        findXSum();

//        Biweekly 142
        //  Q1
//        possibleStringCount();
        //  Q3
//        maxScore();

        //  Weekly 421
        lengthAfterTransformations();
	}


    public static int maxHeightOfTriangle(int red, int blue) {
        int maxLayer = 0;

        //	red at top :
        maxLayer = getMaxLayer(red, blue);
//		for (int currentLayer = 0 ; ; ) {
//			if (red - currentLayer >= 0) {
//				red -= currentLayer;
////				layer++;
//				if (blue - (currentLayer + 1) >= 0) {		//	the next layer
//					blue -= ++currentLayer;
////					layer++;
//				} else {
//					maxLayer = currentLayer;
//					break;
//				}
//			} else {
//				maxLayer = currentLayer;
//				break;
//			}
//		}

//		//	blue at top :
        maxLayer = Math.max(maxLayer, getMaxLayer(blue, red));
//		for (int layer = 1 ; ; ) {
//			if (blue - layer >= 0) {
//				blue -= layer;
//				layer++;
//				if (red - layer >= 0) {
//					red -= layer;
//					layer++;
//				} else {
//					maxLayer = Math.max(maxLayer, layer);
//					break;
//				}
//			} else {
//				maxLayer = Math.max(maxLayer, layer);
//				break;
//			}
//		}

        return maxLayer;
    }

    private static int getMaxLayer(int color1, int color2) {
        int maxLayer = 0;
        for (int currentLayer = 0 ; ; ) {
            if (color1 - (currentLayer + 1) >= 0) {
                color1 -= ++currentLayer;
//				layer++;
                if (color2 - (currentLayer + 1) >= 0) {		//	the next layer
                    color2 -= ++currentLayer;
//					layer++;
                } else {
                    maxLayer = currentLayer;
                    break;
                }
            } else {
                maxLayer = currentLayer;
                break;
            }
        }
        return maxLayer;
    }

    public static int maximumLength(int[] nums) {
//		List<Integer> difArray = new ArrayList<>();

        int even = 1;
        int odd = 1;
        for (int i = 0 ; i < nums.length - 1; i++) {
            int dif = (nums[i + 1] - nums[i]) % 2;
//			difArray.add(dif);
            if (dif == 0) {
                even++;
            } else {
                odd++;
            }
        }

        return Math.max(even, odd);
    }

    private static int findTheNumberOfWinningPlayers() {
        int n = 4;
        int[][] pick = {
                {
                        0, 0
                },
                {
                        1, 0
                },
                {
                        1, 0
                },
                {
                        2, 1
                },
                {
                        2, 1
                },
                {
                        2, 0
                }
        };

        int result = 0;
        Map<Integer, Map<Integer, Integer>> choice = new HashMap<>();
        for (int i = 0; i < pick.length; i++) {
            int[] pickData = pick[i];
            int player = pickData[0];
            int color = pickData[1];

            if (choice.containsKey(player)) {
                Map<Integer, Integer> data = choice.getOrDefault(player, null);
                if (null == data) {
                    data = new HashMap<>();
                    data.put(color, 1);
//					data = Map.of(color, 1);
                } else {
                    Integer count = data.get(color);
                    count = null == count ? 0 : count;
                    data.put(color, count + 1);
                }
                choice.put(player, data);
            } else {
                Map<Integer, Integer> data = new HashMap<>();
                data.put(color, 1);
//				Map<Integer, Integer> data = Map.of(color, 1);
                choice.put(player, data);
            }
        }

        for (Map.Entry<Integer, Map<Integer, Integer>> entry : choice.entrySet()) {
            int player = entry.getKey();
            Map<Integer, Integer> data = entry.getValue();
            int max = Collections.max(data.values());
            if (max >= player + 1 || player == 0) {
                result++;
            }
//			int balls = entry.getValue().size();
//			if (balls > player + 1 || player == 0) {
//				result++;
//			}
        }

//		Map<Integer, Set<Integer>> choice = new HashMap<>();
//		for (int i = 0; i < pick.length; i++) {
//			int[] pickData = pick[i];
//			int player = pickData[0];
//			int ball = pickData[1];
//
//			if (choice.containsKey(player)) {
//				Set<Integer> data = choice.get(player);
//				data.add(ball);
//				choice.put(player, data);
//			} else {
//				Set<Integer> data = new HashSet<>();
//				data.add(ball);
//				choice.put(player, data);
//			}
//		}

//		for (Map.Entry<Integer, Set<Integer>> entry : choice.entrySet()) {
//			int player = entry.getKey();
//			int balls = entry.getValue().size();
//			if (balls > player + 1 || player == 0) {
//				result++;
//			}
//		}

        return result;
    }

    static int[][] grid = {
            {
                    0, 1, 2
            },
            {
                    3, 4, 5
            },
            {
                    6, 7, 8
            }
    };

    private static void designNeighborSumService() {
        neighborSum obj = new neighborSum(grid);
// 		int param_1 = obj.adjacentSum(value);
// 		int param_2 = obj.diagonalSum(value);
//		designNeighborSumService(grid);
//		["neighborSum", "adjacentSum", "adjacentSum", "diagonalSum", "diagonalSum"]
//
//[[[[0, 1, 2], [3, 4, 5], [6, 7, 8]]], [1], [4], [4], [8]]

//		System.out.println(adjacentSum(1));
//		System.out.println(adjacentSum(4));
//		System.out.println(diagonalSum(4));
//		System.out.println(diagonalSum(8));
    }

    static class neighborSum {
        public neighborSum(int[][] grid) {

        }

//		public int adjacentSum(int value) {
//
//		}
//
//		public int diagonalSum(int value) {
//
//		}
    }

    //		jdbcTemplate
    private static void designNeighborSumService(int[][] grid) {
//		int num1 =
    }

    public static int adjacentSum(int value) {
        boolean found = false;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == value) {
                    found = true;
                    int sum = 0;
                    sum += i > 0 ? grid[i-1][j] : 0;
                    sum += j > 0 ? grid[i][j - 1] : 0;
                    sum += j < grid[i].length - 1 ? grid[i][j + 1] : 0;
                    sum += i < grid.length - 1 ? grid[i + 1][j] : 0;

                    return sum;
                }
                if (found) {
                    break;
                }
            }
        }
        return 0;
    }

    public static int diagonalSum(int value) {
        boolean found = false;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == value) {
                    found = true;
                    int sum = 0;
                    sum += i > 0 && j > 0 ? grid[i - 1][j - 1] : 0;
                    sum += i > 0 && j < grid[i].length - 1 ? grid[i - 1][j + 1] : 0;
                    sum += i < grid.length - 1 && j > 0 ? grid[i + 1][j - 1] : 0;
                    sum += i < grid.length - 1 && j < grid[i].length - 1 ? grid[i + 1][j + 1] : 0;

                    return sum;
                }
                if (found) {
                    break;
                }
            }
        }
        return 0;
    }

    public static int finalPositionOfSnake() {
        int n = 2;
        List<String> commands = new ArrayList<>();
        commands.add("RIGHT");
        commands.add("DOWN");

        int i = 0;
        int j = 0;

        for (String command : commands) {
            switch (command) {
                case "UP":
                    i--;
                    break;
                case "DOWN":
                    i++;
                    break;
                case "LEFT":
                    j--;
                    break;
                case "RIGHT":
                    j++;
                    break;
            }
        }

        int result = i * n + j;
        System.out.println(result);
        return result;
//		commands.stream().forEach(c -> {
//
//		});
    }

    public static int[] findPower() {
//		int[] nums = {1,2,3,4,3,2,5};
        int k = 3;
//		int[] nums = {2,2,2,2,2};
        k = 4;
//		int[] nums = {3,2,3,2,3,2};
        k = 2;
        int[] nums = {1,3,4};
        k = 2;

        List<Integer> result = new ArrayList<>();
//		int[] result;
        for (int i = 0; i <= nums.length - k; i++) {
            int current = nums[i];
//			int largest = nums[i + k - 1];
//			if (largest - current == k - 1) {
//				result.add(largest);
//			} else {
//				result.add(-1);
//			}
            boolean valid = true;
            for (int j = i + 1; j < i + k; j++) {
                int next = nums[j];
                if (next - current == 1) {
//				if (next > current) {
                    current = next;
                } else {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                result.add(current);
            } else {
                result.add(-1);
            }
        }
        System.out.println(result);
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    public static int[] findPower2() {
//		int[] nums = {1,2,3,4,3,2,5};
//		int k = 3;
//		int[] nums = {2,2,2,2,2};
//		int k = 4;
//		int[] nums = {3,2,3,2,3,2};
//		int k = 2;
//		int[] nums = {1,3,4};   // [-1, 4]
//		int k = 2;
//		int[] nums = {1,1};
//		int k = 1;
        int[] nums = {1,1,1,2,3};
        int k = 3;

        List<Integer> result = new ArrayList<>();
        if (k == 1) {
            for (int i = 0; i < nums.length; i++) {
                result.add(nums[i]);
            }
        } else {
            boolean valid = true;
            for (int i = 1; i < k; i++) {
                int previous = nums[i - 1];
                int current = nums[i];

                if (current - previous == 1) {
                    continue;
                } else {
                    result.add(-1);
                    valid = false;
                    break;
                }
//				result.add(current);
            }
            if (valid) {
                result.add(nums[k - 1]);
            }

//		for (int i = 1; i <= nums.length - k + 1; i++) {
            for (int i = k; i < nums.length; i++) {
                int previous = nums[i - 1];
                int current = nums[i];

                if (current - previous == 1) {
                    result.add(current);
                } else {
                    if (i - k + 1 < 0) {
                        if (result.size() > i) {
                            result.set(i, -1);
                        } else {
                            result.add(-1);
                        }
                    } else if (i < result.size()) {
                        result.set(i, -1);
//				} else if (i - k + 1 < result.size()) {
//					result.set(i - k + 1, -1);
                    } else {
                        result.add(-1);
                    }
                }
            }
        }

//		int[] result;
//		for (int i = k; i <= nums.length - k + 1; i++) {
//			int previous = nums[i - 1];
//			int current = nums[i];
//
//			if (current - previous == 1) {
//				result.add(current);
//			} else {
////				result.add(-1);
//				result.set(i - k + 1, -1);
//			}
//		}
        System.out.println(result);
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    public static int countKConstraintSubstrings() {
        String s = "10101";
        int k = 1;
        s = "1010101";
        k = 2;
        s = "11111";
        k = 1;

        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            int numOf0 = 0;
            int numOf1 = 0;
            for (int j = i; j < s.length(); j++) {
                if (s.charAt(j) == '0') {
                    numOf0++;
                } else {
                    numOf1++;
                }

                if (numOf0 <= k || numOf1 <= k) {
//				if (numOf0 % 2 == 1 || numOf1 % 2 == 1) {
                    result++;
                }
            }
        }

        System.out.println(result);
        return result;
    }

    public static long maxEnergyBoost() {
        int[] energyDrinkA = {1,3,1};
        int[] energyDrinkB = {3,1,1};

        int n = energyDrinkA.length;
//		int lastEnergyA = 0;
//		int lastEnergyB = 0;
        int lastGain = 0;
        int result = 0;
//		char currentChoose = ' ';
        char lastChoose = ' ';
        for (int i = 0; i < n; i++) {
            int energyA = energyDrinkA[i];
            int energyB = energyDrinkB[i];

            int max;
            int min;
            if (energyA > energyB) {
                max = energyA;
                min = energyB;
                int dif = max - min;
                if (dif > lastGain && lastChoose == 'B') {
                    //	switch
                    result -= lastGain;
                    result += max;

                    lastChoose = 'B';
                    lastGain = max;
                } else {
                    //	no switch
                    lastGain = lastChoose == 'A' ? energyA : energyB;
                    result += lastGain;
                }
            } else if (energyA < energyB) {
                max = energyB;
                min = energyA;
                int dif = max - min;
                if (dif > lastGain && lastChoose == 'A') {
                    //	switch
                    result -= lastGain;
                    result += max;
                    lastChoose = 'A';
                } else {
                    //	no switch
                    lastGain = lastChoose == 'B' ? energyB : energyA;
                    result += lastGain;
                }
            } else if (energyA == energyB) {
                //	no switch
                result += energyA;
            }

//			int dif;
//			int max;
//			int min;
//			if (energyA > energyB) {
//				max = energyA;
//				min = energyB;
//				currentChoose = 'A';
////				lastChoose = 'A';
//			} else {
//				max = energyB;
//				min = energyA;
//				currentChoose = 'B';
////				lastChoose = 'B';
//			}
//
//			dif = max - min;
//			if (dif > lastGain && currentChoose != lastChoose) { //	switch
//				result -= lastGain;
//				result += max;
//				lastGain = max;
//				lastChoose = currentChoose;
//			} else {	//	no switch
//				result += max;
//				lastGain = max;
//				lastChoose = currentChoose;
//			}

//			int dif = Math.abs(energyA - energyB);

        }

        System.out.println(result);
        return result;
    }

    public static int[] getFinalState() {
        int[] nums = {2,1,3,5,6};
        int k = 5;
        int multiplier = 2;

//		List<Integer> list = Arrays.asList(nums);
        int time = 0;
        while (time++ < k) {
            Integer minIndex = findMinOfArray(nums);
//			Integer minIndex = mins.get
            nums[minIndex] = nums[minIndex] * multiplier;
        }

        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
        }
        return nums;
    }

    private static Integer findMinOfArray(int[] nums) {
        int minValue = nums[0];
        int minIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < minValue) {
                minValue = nums[i];
                minIndex = i;
            }
        }
        return minIndex;
//		return Map.of(minValue, minIndex);
    }

    public int countPairs() {
        int[] nums = {3,12,30,17,21};

        int result = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                boolean isAlmost = isAlmostEqual(nums[i], nums[j]);
                result += isAlmost ? 1 : 0;
            }
        }

        System.out.println(result);
        return result;
    }

    private static boolean isAlmostEqual(int num1, int num2) {
        List<Integer> num1Digits = new ArrayList<>();
        List<Integer> num2Digits = new ArrayList<>();
//		Integer[] num1Digits = new Integer[10];

        for (int i = 0; i < 10; i++) {
            num1Digits.add(0);
            num2Digits.add(0);
        }

        while (num1 != 0) {
            int digit = num1 % 10;
//			num1Digits[digit]++;
            num1Digits.set(digit, num1Digits.indexOf(digit) + 1);
//			num1Digits.add(num1 % 10);
            num1 = num1 / 10;
        }
        while (num2 != 0) {
            int digit = num2 % 10;
//			num1Digits[digit]++;
            num2Digits.add(num2 % 10);
            num2 = num2 / 10;
        }

        if (Math.abs(num1Digits.size() - num2Digits.size()) > 1) {
            return false;
        } else if (Math.abs(num1Digits.size() - num2Digits.size()) == 1) {
            int smallerSize = num1Digits.size() < num2Digits.size() ? num1Digits.size() : num2Digits.size();
            for (int i = 0; i < num1Digits.size(); i++) {
//				if (num1Digits[i] != num2Digits[i]) {
//
//				}
            }
        } else {
            int dif1 = 0;
            for (int i = 0; i < 10; i++) {
                if (num1Digits.indexOf(i) != num2Digits.indexOf(i)) {
                    dif1 = i;
                }
            }
        }

        return false;
    }

    public static int generateKey() {
        int num1 = 1;
        int num2 = 10;
        int num3 = 1000;

//		num1 = 987;
//		num2 = 879;
//		num3 = 798;

        List<Integer> arr1 = getDigits(num1);
        List<Integer> arr2 = getDigits(num2);
        List<Integer> arr3 = getDigits(num3);

        List<Integer> result = new ArrayList<>();
        for (int i = 3; i >= 0; i--) {
            int min = arr1.get(i) < arr2.get(i) ? arr1.get(i) : arr2.get(i);
            min = min < arr3.get(i) ? min : arr3.get(i);
            result.add(min);
        }

        int k = 0;
        for (int i = 0; i < result.size(); i++) {
//		for (int i = result.size() - 1; i >= 0; i--) {
            int digit = result.get(i);
            k = k * 10 + digit;
        }

        System.out.println(k);
        return k;
    }

    private static List<Integer> getDigits(int num) {
//		Integer[] arr = new Integer[4];
        List<Integer> arr = new ArrayList<>();
        while (num != 0) {
            int digit = num % 10;
            arr.add(digit);
            num /= 10;
        }
        while (arr.size() < 4) {
            arr.add(0);
        }
        return arr;
    }

    public static String stringHash() {
        String s = "abcd";
        int k = 2;
//		s = "mxz";
//		k = 3;

//		int length = s.length() / k;
        List<String> subStrings = new ArrayList<>();
        StringBuilder sB = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sB.append(s.charAt(i));
            if (sB.length() == k) {
//				subStrings.add(sB.toString());
                int v = getHash(sB.toString());
                result.append((char)v);
                sB.delete(0, sB.length());
            }
        }

        System.out.println(result.toString());
        return result.toString();
    }

    private static int getHash(String value) {
        int code = 0;

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            code += c - 'a';
        }

        code %= 26;
        return code + 'a';
    }
    private static int minElement() {
//		int[] nums = {10,12,13,14};
//		int[] nums = {1,2,3,4};
        int[] nums = {999,19,199};

        int result = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            int value = nums[i];
            while (value != 0) {
                int digit = value % 10;
                sum += digit;
                value /= 10;
            }
            result = Math.min(result, sum);
        }

        System.out.println(result);
        return result;
    }

    private static long maximumTotalSum() {
        int[] maximumHeight = {2,3,4,3};
//		int[] maximumHeight = {15,10};
//		int[] maximumHeight = {2,2,1};
//		Arrays.sort(maximumHeight);

        HashMap<Integer, Boolean> appear = new HashMap<>();
        int result = 0;
        for (int i = 0; i < maximumHeight.length; i++) {
            int num = maximumHeight[i];
            if (appear.containsKey(num)) {
                boolean foundSpace = false;
                for (int j = num - 1; j > 0 ; j--) {
                    if (appear.containsKey(j)) {
                        continue;
                    } else {
                        foundSpace = true;
                        appear.put(j, true);
                        result += j;
                        break;
                    }
                }

                if (!foundSpace) {
                    System.out.println(-1);
                    return -1;
                }
            } else {
                appear.put(num, true);
                result += num;
            }
        }

        System.out.println(result);
        return result;

//		int[] heightCount = new int[maximumHeight.length];
//
//		for (int i = 0; i < maximumHeight.length; i++) {
//			int num = maximumHeight[i];
//			if (heightCount[num] == 0) {
//				heightCount[num] = 1;
//			} else {
//				//	check whether there's empty space
//				boolean found = false;
//				for (int j = 0; j < num; j++) {
//					if (heightCount[j] == 0) {
//						heightCount[j] = 1;
//						found = true;
//						//	subtract all the heights afterward 1, start from this height.
////						for (int k = j + 1 ; k < num ; k++) {
////
////						}
//						break;
//					}
//				}
//				if (!found) {
//					System.out.println("-1");
//					return -1;
//				}
//			}
//		}
//
//		for (int i = 0; i < height)
    }

    private static int[] validSequence() {
        String word1 = "vbcca";
        String word2 = "abc";
        //
        word1 = "bacdc";
        word2 = "abc";
        //
//		word1 = "aaaaaa";
//		word2 = "aaabc";

        List<Integer> result = find(word1, word2);
        System.out.println(result);
        return result.stream().mapToInt(i -> i).toArray();
    }

    private static List<Integer> find(String word1, String word2) {
        char targetChar = word2.charAt(word2.length() - 1);
        if (word2.length() == 1) {
            return new ArrayList<Integer>(){{
                add(0);
            }};
//			return Arrays.asList(0);
        }
        for (int i = 0; i < word1.length(); i++) {
//		for (int i = word1.length() - 1; i >= 0; i--) {
            char currentChar = word1.charAt(i);
            if (targetChar == currentChar) {
                List<Integer> result = find(word1.substring(0, word1.length() - 1), word2.substring(0, word2.length() - 1));
                result.add(i);
                return result;
            }
        }
        return new ArrayList<>();
    }

	private static int maxGoodNumbers() {
//        int[] nums = {1, 2, 3};
        int[] nums = {2, 8, 16};
        String[] binaries = new String[3];

        int longestLength = 0;
        for (int i = 0; i < nums.length; i++) {
            binaries[i] = getBinary(nums[i]);
            System.out.println("num : " + nums[i] + ", binary : " + binaries[i]);
            longestLength = Math.max(longestLength, binaries[i].length());
        }
//        String binary1 = getBinary(nums[0]);
//        String binary2 = getBinary(nums[1]);
//        String binary3 = getBinary(nums[2]);
//
//        System.out.println(binary1 + " " + binary2 + " " + binary3);

        int largest = 0;
        String str012 = binaries[0] + binaries[1] + binaries[2];
        System.out.println("string : " + str012 + ", largest : " + getNum(str012));
        largest = Math.max(getNum(str012), largest);

        String str021 = binaries[0] + binaries[2] + binaries[1];
        System.out.println("string : " + str021 + ", largest : " + getNum(str021));
        largest = Math.max(getNum(str021), largest);

        String str102 = binaries[1] + binaries[0] + binaries[2];
        System.out.println("string : " + str102 + ", largest : " + getNum(str102));
        largest = Math.max(getNum(str102), largest);

        String str120 = binaries[1] + binaries[2] + binaries[0];
        System.out.println("string : " + str120 + ", largest : " + getNum(str120));
        largest = Math.max(getNum(str120), largest);

        String str201 = binaries[2] + binaries[0] + binaries[1];
        System.out.println("string : " + str201 + ", largest : " + getNum(str201));
        largest = Math.max(getNum(str201), largest);

        String str210 = binaries[2] + binaries[1] + binaries[0];
        System.out.println("string : " + str210 + ", largest : " + getNum(str210));
        largest = Math.max(getNum(str210), largest);

        //  leading 0 => doesn't work
//        for (int i = 0; i < nums.length; i++) {
//            int dif = longestLength - binaries[i].length();
//            StringBuilder leading0 = new StringBuilder();
//            for (int j = 0; j < dif; j++) {
//                leading0.append("0");
//            }
//            binaries[i] = leading0 + binaries[i];
//        }
//
//        StringBuilder result = new StringBuilder();
//        for (int i = 0; i < longestLength; i++) {
//            for (int j = 0; j < nums.length; j++) {
//
//            }
//        }

        System.out.println("result : " + largest);
        return largest;
    }

    private static String getBinary(int num) {
        StringBuilder binary = new StringBuilder();

        while (num > 0) {
            int remainder = num % 2;
            binary.append(remainder);
            num /= 2;
        }

        return binary.reverse().toString();
    }

    private static int getNum(String binary) {
        StringBuilder binaryBuilder = new StringBuilder(binary);

        String reverse = binaryBuilder.reverse().toString();
        double result = 0;
        for (int i = 0; i < reverse.length(); i++) {
            double base = Math.pow(2, i);
            result += (reverse.charAt(i) - '0') * base;
        }
        return (int) result;
    }

    private static int[] minBitwiseArray() {
        List<Integer> nums = Arrays.asList(2,3,5,7);
        nums = Arrays.asList(11,13,31);
        nums = Arrays.asList(17, 17);

        int[] result = new int[nums.size()];
//        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < nums.size(); i++) {
            Integer num = nums.get(i);
//        for (Integer num : nums) {
            List<Integer> binaries = getBinary(num);

            Integer res = getMinBitwise(num, binaries);
//            result.add(res);
//            result[nums.indexOf(num)] = res;
            result[i] = res;
        }

//        return result.toArray();
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }

        return result;
    }

    private static List<Integer> getBinary(Integer num) {
//    private String getBinary(Integer num) {
//        StringBuilder sb = new StringBuilder();
        List<Integer> binaryList = new ArrayList<>();

        while (num > 0) {
            int remain = num % 2;
            binaryList.add(remain);
            num /= 2;
        }

//        Collections.reverse(binaryList);
        return binaryList;
    }

    private static Integer getMinBitwise(int current, List<Integer> binary) {
        Integer minBitWise = Integer.MAX_VALUE;

        for (int j = 0; j < binary.size(); j++) {
            Integer bit = binary.get(j);
//        for (Integer bit : binary) {
            if (bit.equals(1)) {
                List<Integer> target = new ArrayList<>();
                target.addAll(binary);
//                Collections.copy(binary, target);

//                int index = binary.indexOf(bit);
                int index = j;
                target.set(index, 0);

                //  target + 1
                List<Integer> targetPlus1 = new ArrayList<>();
//                Collections.copy(target, targetPlus1);
                targetPlus1.addAll(target);
                for (int i = 0; i < targetPlus1.size(); i++) {
                    Integer digit = targetPlus1.get(i);
                    if (digit.equals(0)) {
                        targetPlus1.set(i, 1);
                        break;
                    } else {    //  carry
                        targetPlus1.set(i, 0);
//                        break;
                    }
                }

                //  target OR target+1
                List<Integer> orResult = new ArrayList<>();
                for (int i = 0; i < target.size(); i++) {
                    if (target.get(i).equals(1) || targetPlus1.get(i).equals(1)) {
                        orResult.add(1);
//                        orResult.set(i, 1);
                    } else {
                        orResult.add(0);
//                        orResult.set(i, 0);
                    }
                }

                //  transform
                int currentNum = getNumFromBinary(orResult);

                if (currentNum == current) {
                    int targetNum = getNumFromBinary(target);
                    minBitWise = minBitWise > targetNum ? targetNum : minBitWise;
                }
            }
        }

        return minBitWise == Integer.MAX_VALUE ? -1 : minBitWise;
    }

    private static Integer getNumFromBinary(List<Integer> num) {
        double result = 0;

        for (int i = 0; i < num.size(); i++) {
            if (num.get(i).equals(1)) {
                double base = Math.pow(2, i);
                result += base;
            }
        }

        return (int) result;
    }

    private static int[] findXSum() {
        int[] nums = {1,1,2,2,3,4,2,3};
        int k = 6;
        int x = 2;

        int length = nums.length;
        for (int i = 0; i < length - k + 1; i++) {
            HashMap<Integer, Integer> count = new HashMap<>();
            for (int j = 0; j < k; j++) {


            }
        }

        return null;
    }

    public static int possibleStringCount() {
        String word = "abbcccc";
//        word = "abcd";
//        word = "aaaa";
//        word = "ere";

        int result = possibleCounts(word);
//        Map<Character, Integer> appear = new HashMap<>();
//        int result = 1;
//        for (int i = 0; i < word.length(); i++) {
//            Character c = word.charAt(i);
//
//            if (appear.containsKey(c)) {
//                int times = appear.get(c);
//                appear.put(c, times + 1);
//                result++;
//            } else {
//                appear.put(c, 1);
//            }
//        }

        System.out.println(result);
        return result;
    }

    private static int possibleCounts(String word) {
        if (word.length() == 1) {
            return 1;
        }
        Character lastCharacter = word.charAt(word.length() - 1);
        Character lastButOneCharacter = word.charAt(word.length() - 2);
        if (lastCharacter.equals(lastButOneCharacter)) {
            return 1 + possibleCounts(word.substring(0, word.length() - 1));
        } else {
            return possibleCounts(word.substring(0, word.length() - 1));
        }
    }

    public static int maxScore() {
        int n = 2;
        int k = 1;
        int[][] stayScore = {{2, 3}};
        int[][] travelScore = {{0, 2}, {1, 0}};

        int result = 0;
        for (int i = 0; i < n; i++) {
            result = Math.max(result, sumScore(n, k, 0, i, 0, stayScore, travelScore));
        }

//        //  stay strategy
//        for (int c = 0; c < n; c++) {
//            int sum = 0;
//            for (int d = 0; d < k; d++) {
//                sum += stayScore[d][c];
//            }
//
//            result = Math.max(sum, result);
//        }
//
//        //  travel strategy
////        for (int c = )

        System.out.println(result);
        return result;
    }

    private static int sumScore(int n, int k, int previousCity, int currentCity, int currentDay, int[][] stayScore, int[][] travelScore) {
        if (k == currentDay + 1) {  //  the last day
            if (previousCity == currentCity) { //    stay
                return stayScore[currentDay][previousCity];
            } else {
                return travelScore[previousCity][currentCity];
            }
        } else {
            int result = 0;
            for (int i = 0; i < n; i++) {   //  choose the next city
                result = Math.max(result, sumScore(n, k, currentCity, i, currentDay + 1, stayScore, travelScore));
            }
            return result;
        }
    }

    public static int lengthAfterTransformations() {
        String s = "abcyy";
        int t = 2;

//        s = "azbk";
//        t = 1;

//        s = "jqktcurgdvlibczdsvnsg";
//        t = 7517;

//        s = "cu";
//        t = 5;

        String result = "";
        for (int i = 0; i < s.length(); i++) {
             result += getStringAfterTransform(s.charAt(i), t);
        }
        System.out.println("result : " + result + ", length : " + result.length());
        return result.length();

//        int i = 0;
//        String result = null;
//        while (i++ < t) {
//            int nextStep = 0;
//            for (int j = 0; j < s.length(); j = j + nextStep) {
//                Character originalC = s.charAt(j);
//
//                String newStr;
//                if (originalC.equals('z')) {
//                    newStr = "ab";
////                    j += 2;
//                    nextStep = 2;
//                } else {
//                    Character newC = ++originalC;
//                    newStr = String.valueOf(newC);
////                    j++;
//                    nextStep = 1;
//                }
//
//                //  postStr
//                int endIndex = Math.max(s.length(), j + 1);
//                String postStr = s.substring(j + 1, endIndex);
//
//                s = s.substring(0, j) + newStr + postStr;
////                System.out.println("result : " + s + ", length : " + s.length());
//            }
//        }

//        System.out.println("result : " + s + ", length : " + s.length());
//        return s.length();
    }

    private static String getStringAfterTransform(Character ch, int t) {
        int newCh = ch + t;
        String newStr;
        if (newCh > 'z') {
            int dif = newCh - 'z' - 1;
            return getStringAfterTransform('z', dif);
//            newStr = "ab";
//        } else if (newCh == 'z') {
//            return "z";
        } else {
            char newC = (char) newCh;
            return String.valueOf(newC);
        }
    }
}
