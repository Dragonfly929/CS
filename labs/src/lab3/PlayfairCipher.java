package lab3;

import java.util.Scanner;

public class PlayfairCipher {
    private static final String romanianAlphabet = "AĂÂBCDEFGHIÎJKLMNOPQRSȘTȚUVWXYZ";

    private static final String BOLD = "\u001B[1m";
    private static final String RESET = "\u001B[0m";


    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        System.out.println(BOLD + "Playfair cipher Menu:" + RESET + "\ne - Encryption\nd - Decryption\nx - Exit\n");

        label:
        while (true) {
            System.out.print("Operation choice: ");
            String option = userInput.nextLine();
            switch (option) {
                case "e": {
                    System.out.println(BOLD + "Message Encryption" + RESET);
                    String key = getKey();
                    String message = getRomanianInput(userInput, "message");
                    //String message = userInput.nextLine().toUpperCase().replaceAll(" ", "");
                    char[][] matrix = getMatrix(key);
                    String encryptedMessage = encrypt(message, matrix);
                    System.out.println(BOLD + "\tEncrypted message: " + RESET + encryptedMessage);
                    break;
                }
                case "d": {
                    System.out.println(BOLD + "Ciphertext Decryption" + RESET);
                    String key = getKey();
                    String ciphertext = getRomanianInput(userInput, "ciphertext");
                    //String ciphertext = userInput.nextLine().toUpperCase().replaceAll(" ", "");
                    char[][] matrix = getMatrix(key);
                    String decryptedMessage = decrypt(ciphertext, matrix);
                    System.out.println(BOLD + "\tDecrypted message: " + RESET + decryptedMessage);
                    break;
                }
                case "x":
                    break label;

                default:
                    System.out.println("Invalid input. Please choose 'e' for Encryption, 'd' for Decryption, or 'x' to Exit.");
            }
        }
    }

    private static String getKey() {
        Scanner userInput = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a key (at least 7 characters): ");
            String key = userInput.nextLine().toUpperCase().replaceAll(" ", "").replace("J", "I");
            boolean isValid = key.matches("[" + romanianAlphabet + " ]+") && key.length() >= 7;
            if (isValid) {
                return key;
            } else {
                System.out.println(BOLD + "Invalid key." + RESET + "The key must be at least 7 characters and contain only letters.");
            }
        }
    }

    private static char[][] getMatrix(String key) {
        key = key.replace("Q", "O");
        String newAlphabet = key + romanianAlphabet.replaceAll("Q", "O");
        newAlphabet = newAlphabet.chars()
                .distinct()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        char[][] matrix = new char[5][6];
        int index = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                matrix[i][j] = newAlphabet.charAt(index);
                index++;
            }
        }

        for (char[] row : matrix) {
            for (char letter : row) {
                System.out.print(letter + " ");
            }
            System.out.println();
        }

        return matrix;
    }

    private static String encrypt(String message, char[][] matrix) {
        char[] messageChars = message.toCharArray();
        StringBuilder encryptedMessage = new StringBuilder();

        if (messageChars.length % 2 != 0) {
            messageChars = (message + "W").toCharArray();
        }

        int i = 0;
        while (i < messageChars.length) {
            char letter1 = messageChars[i];
            char letter2 = messageChars[i + 1];
            int[] indices1 = letterIndex(letter1, matrix);
            int[] indices2 = letterIndex(letter2, matrix);

            char encryptedLetter1, encryptedLetter2;

            if (indices1[0] == indices2[0]) {
                encryptedLetter1 = matrix[indices1[0]][(indices1[1] + 1) % 6];
                encryptedLetter2 = matrix[indices2[0]][(indices2[1] + 1) % 6];
            } else if (indices1[1] == indices2[1]) {
                encryptedLetter1 = matrix[(indices1[0] + 1) % 5][indices1[1]];
                encryptedLetter2 = matrix[(indices2[0] + 1) % 5][indices2[1]];
            } else {
                encryptedLetter1 = matrix[indices1[0]][indices2[1]];
                encryptedLetter2 = matrix[indices2[0]][indices1[1]];
            }

            encryptedMessage.append(encryptedLetter1).append(encryptedLetter2);
            i += 2;
        }

        return encryptedMessage.toString();
    }

    private static String decrypt(String ciphertext, char[][] matrix) {
        StringBuilder decryptedMessage = new StringBuilder();
        char[] ciphertextChars = ciphertext.toCharArray();

        int i = 0;
        while (i < ciphertextChars.length) {
            char letter1 = ciphertextChars[i];
            char letter2 = ciphertextChars[i + 1];
            int[] indices1 = letterIndex(letter1, matrix);
            int[] indices2 = letterIndex(letter2, matrix);

            char decryptedLetter1, decryptedLetter2;

            if (indices1[0] == indices2[0]) {
                decryptedLetter1 = matrix[indices1[0]][(indices1[1] + 5) % 6];
                decryptedLetter2 = matrix[indices2[0]][(indices2[1] + 5) % 6];
            } else if (indices1[1] == indices2[1]) {
                decryptedLetter1 = matrix[(indices1[0] + 4) % 5][indices1[1]];
                decryptedLetter2 = matrix[(indices2[0] + 4) % 5][indices2[1]];
            } else {
                decryptedLetter1 = matrix[indices1[0]][indices2[1]];
                decryptedLetter2 = matrix[indices2[0]][indices1[1]];
            }

            decryptedMessage.append(decryptedLetter1).append(decryptedLetter2);
            i += 2;
        }

        if (decryptedMessage.charAt(decryptedMessage.length() - 1) == 'W') {
            decryptedMessage.deleteCharAt(decryptedMessage.length() - 1);
        }

        return decryptedMessage.toString();
    }


    private static int[] letterIndex(char letter, char[][] matrix) {
        int[] indices = new int[2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                if (matrix[i][j] == letter) {
                    indices[0] = i;
                    indices[1] = j;
                    return indices;
                }
            }
        }
        return indices;
    }

    private static String getRomanianInput(Scanner scanner, String inputType) {
        String input;
        boolean isValid;
        do {
            System.out.print("Enter a " + inputType + ": ");
            input = scanner.nextLine().toUpperCase();
            isValid = input.matches("[" + romanianAlphabet + " ]+");
            if (!isValid) {
                System.out.println(BOLD + "Invalid " + inputType + "." + RESET + " The " + inputType + " must contain only Romanian letters.");
            }
        } while (!isValid);

        return input.replaceAll(" ", "");
    }
}
