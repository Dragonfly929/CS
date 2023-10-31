package lab4;

import java.util.Scanner;
import java.util.Random;

public class DESKeyGeneration {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("DES Key Generation");

        boolean continueGenerating = true;
        while (continueGenerating) {
            // Choose to enter a custom key or generate a random one
            System.out.print("Do you want to enter a custom key (yes/no)? ");
            String choice = scanner.nextLine();

            String initialKey;
            if (choice.equalsIgnoreCase("yes")) {
                // Input the 64-bit initial key
                System.out.print("Enter the 64-bit initial key (in binary): ");
                initialKey = scanner.nextLine();
            } else {
                // Generate a random 64-bit initial key
                initialKey = generateRandomKey();
                System.out.println("Generated Random Key: " + initialKey);
            }

            if (initialKey.length() != 64) {
                System.out.println("The initial key must be 64 bits long.");
            } else {
                // Convert the initial key to an array of 16 subkeys (round keys)
                String[] roundKeys = generateRoundKeys(initialKey);

                // Display the round keys and intermediate steps
                System.out.println("\nIntermediate Steps:");
                for (int i = 0; i < 16; i++) {
                    System.out.println("After Round " + (i + 1) + " Left Shift: " + roundKeys[i]);
                }

                System.out.println("\nFinal Round Keys:");
                for (int i = 0; i < 16; i++) {
                    System.out.println("Round Key K" + (i + 1) + ": " + roundKeys[i]);
                }
            }

            System.out.print("Do you want to generate another key (yes/no)? ");
            String response = scanner.nextLine();
            continueGenerating = response.equalsIgnoreCase("yes");
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    // Function to generate a random 64-bit key
    public static String generateRandomKey() {
        Random rand = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            key.append(rand.nextInt(2));
        }
        return key.toString();
    }

    // Function to generate all 16 round keys
    public static String[] generateRoundKeys(String initialKey) {
        String[] roundKeys = new String[16];

        // Define the PC-1 permutation table for the initial permutation
        int[] pc1Table = {
                57, 49, 41, 33, 25, 17, 9, 1,
                58, 50, 42, 34, 26, 18, 10, 2,
                59, 51, 43, 35, 27, 19, 11, 3,
                60, 52, 44, 36, 63, 55, 47, 39,
                31, 23, 15, 7, 62, 54, 46, 38,
                30, 22, 14, 6, 61, 53, 45, 37,
                29, 21, 13, 5, 28, 20, 12, 4
        };

        // Perform the initial permutation (PC-1)
        initialKey = permute(initialKey, pc1Table);

        String C = initialKey.substring(0, 28);
        String D = initialKey.substring(28);

        // Define the left shift schedule for C and D
        int[] leftShiftSchedule = {
                1, 1, 2, 2, 2, 2, 2, 2,
                1, 2, 2, 2, 2, 2, 2, 1
        };

        // Implement the 16 rounds of subkey generation
        for (int i = 0; i < 16; i++) {
            C = leftShift(C, leftShiftSchedule[i]);
            D = leftShift(D, leftShiftSchedule[i]);

            // Concatenate C and D
            String CD = C + D;

            // Define the PC-2 permutation table for round key generation
            int[] pc2Table = {
                    14, 17, 11, 24, 1, 5, 3, 28,
                    15, 6, 21, 10, 23, 19, 12, 4,
                    26, 8, 16, 7, 27, 20, 13, 2,
                    41, 52, 31, 37, 47, 55, 30, 40,
                    51, 45, 33, 48, 44, 49, 39, 56,
                    34, 53, 46, 42, 50, 36, 29, 32
            };

            // Apply the PC-2 permutation to generate the round subkey
            roundKeys[i] = permute(CD, pc2Table);
        }

        return roundKeys;
    }

    // Function to perform permutation of data according to a given table
    public static String permute(String data, int[] table) {
        StringBuilder permutedData = new StringBuilder();
        for (int index : table) {
            permutedData.append(data.charAt(index - 1));
        }
        return permutedData.toString();
    }

    // Function to perform left shift on a string by a specified number of positions
    public static String leftShift(String str, int positions) {
        return str.substring(positions) + str.substring(0, positions);
    }

    // Function to display tables used in DES
    public static void displayTables() {
        System.out.println("Tables Used in DES:\n");

        // Define the PC-1 permutation table for the initial permutation
        int[] pc1Table = {
                57, 49, 41, 33, 25, 17, 9, 1,
                58, 50, 42, 34, 26, 18, 10, 2,
                59, 51, 43, 35, 27, 19, 11, 3,
                60, 52, 44, 36, 63, 55, 47, 39,
                31, 23, 15, 7, 62, 54, 46, 38,
                30, 22, 14, 6, 61, 53, 45, 37,
                29, 21, 13, 5, 28, 20, 12, 4
        };

        // Define the PC-2 permutation table for round key generation
        int[] pc2Table = {
                14, 17, 11, 24, 1, 5, 3, 28,
                15, 6, 21, 10, 23, 19, 12, 4,
                26, 8, 16, 7, 27, 20, 13, 2,
                41, 52, 31, 37, 47, 55, 30, 40,
                51, 45, 33, 48, 44, 49, 39, 56,
                34, 53, 46, 42, 50, 36, 29, 32
        };

        // Define the left shift schedule for C and D
        int[] leftShiftSchedule = {
                1, 1, 2, 2, 2, 2, 2, 2,
                1, 2, 2, 2, 2, 2, 2, 1
        };

        // Display the values of PC-1
        System.out.println("PC-1 Table:");
        displayTable(pc1Table);

        // Display the values of PC-2
        System.out.println("\nPC-2 Table:");
        displayTable(pc2Table);

        // Display the left shift schedule
        System.out.println("\nLeft Shift Schedule:");
        displayTable(leftShiftSchedule);
    }

    // Function to display an array as a table
    public static void displayTable(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print("\t");
            }
        }
        System.out.println();
    }

}