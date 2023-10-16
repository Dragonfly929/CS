package lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayfairCipherRomanian {
//    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÎÂĂȘȚ";
    private static final String ALPHABET = "AĂÂBCDEFGHIÎJKLMNOPQRSȘTȚUVWXYZ";
//    private static final String REPLACEMENTS = "ÎÂĂȘȚ";
//    private static final String REPLACEMENT_MAPPING = "IABST";
    private static final String REPLACEMENTS = "ĂÂÎȘȚ";
    private static final String REPLACEMENT_MAPPING = "AXIST";
    private String key;
    private char[][] matrix;

    public PlayfairCipherRomanian(String key) {
        if (key.length() < 7) {
            throw new IllegalArgumentException("Key length must be at least 7 characters.");
        }

        key = key.toUpperCase().replaceAll(" ", "");
        key = key.replaceAll(REPLACEMENTS, REPLACEMENT_MAPPING);

        this.key = key;
        this.matrix = createPlayfairMatrix(key);
    }

    private char[][] createPlayfairMatrix(String key) {
        List<Character> uniqueChars = new ArrayList<>();
        for (char c : key.toCharArray()) {
            if (!uniqueChars.contains(c) && ALPHABET.contains(String.valueOf(c))) {
                uniqueChars.add(c);
            }
        }

        for (char c : ALPHABET.toCharArray()) {
            if (!uniqueChars.contains(c)) {
                uniqueChars.add(c);
            }
        }

        int numRows = 6;  // for 31 characters
        int numCols = (int) Math.ceil((double) uniqueChars.size() / numRows);

        char[][] matrix = new char[numRows][numCols];

        int index = 0;
        for (int j = 0; j < numCols; j++) {
            for (int i = 0; i < numRows; i++) {
                if (index < uniqueChars.size()) {
                    matrix[i][j] = uniqueChars.get(index++);
                }
            }
        }

        return matrix;
    }

    private String prepareMessage(String message) {
        message = message.toUpperCase();
        message = message.replaceAll(REPLACEMENTS, REPLACEMENT_MAPPING);

        StringBuilder preparedMessage = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (ALPHABET.contains(String.valueOf(c))) {
                preparedMessage.append(c);
            }
        }

        for (int i = 1; i < preparedMessage.length(); i++) {
            if (preparedMessage.charAt(i) == preparedMessage.charAt(i - 1)) {
                preparedMessage.insert(i, 'X');
                i++;
            }
        }

        if (preparedMessage.length() % 2 == 1) {
            preparedMessage.append('X');
        }

        return preparedMessage.toString();
    }

    private int[] findPosition(char c) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (matrix[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private String encryptPair(String pair) {
        int[] pos1 = findPosition(pair.charAt(0));
        int[] pos2 = findPosition(pair.charAt(1));

        if (pos1 == null || pos2 == null) {
            throw new IllegalArgumentException("Characters not found in the matrix.");
        }

        if (pos1[0] == pos2[0]) {
            // same row, shift columns to the right
            pos1[1] = (pos1[1] + 1) % 6;
            pos2[1] = (pos2[1] + 1) % 6;
        } else if (pos1[1] == pos2[1]) {
            // same column, shift rows down
            pos1[0] = (pos1[0] + 1) % 6;
            pos2[0] = (pos2[0] + 1) % 6;
        } else {
            // different rows and columns, swap positions
            int temp = pos1[1];
            pos1[1] = pos2[1];
            pos2[1] = temp;
        }

        return String.valueOf(matrix[pos1[0]][pos1[1]]) + matrix[pos2[0]][pos2[1]];
    }

    private String decryptPair(String pair) {
        int[] pos1 = findPosition(pair.charAt(0));
        int[] pos2 = findPosition(pair.charAt(1));

        if (pos1 == null || pos2 == null) {
            throw new IllegalArgumentException("Characters not found in the matrix.");
        }

        if (pos1[0] == pos2[0]) {
            // same row, shift columns to the left
            pos1[1] = (pos1[1] + 5) % 6;
            pos2[1] = (pos2[1] + 5) % 6;
        } else if (pos1[1] == pos2[1]) {
            // same column, shift rows up
            pos1[0] = (pos1[0] + 5) % 6;
            pos2[0] = (pos2[0] + 5) % 6;
        } else {
            // different rows and columns, swap positions
            int temp = pos1[1];
            pos1[1] = pos2[1];
            pos2[1] = temp;
        }

        return String.valueOf(matrix[pos1[0]][pos1[1]]) + matrix[pos2[0]][pos2[1]];
    }

    public String encrypt(String message) {
        String preparedMessage = prepareMessage(message);

        StringBuilder encryptedMessage = new StringBuilder();

        for (int i = 0; i < preparedMessage.length(); i += 2) {
            String pair = preparedMessage.substring(i, i + 2);
            String encryptedPair = encryptPair(pair);
            encryptedMessage.append(encryptedPair);
        }

        return encryptedMessage.toString();
    }

    public String decrypt(String encryptedMessage) {
        String preparedMessage = prepareMessage(encryptedMessage);

        StringBuilder decryptedMessage = new StringBuilder();

        for (int i = 0; i < preparedMessage.length(); i += 2) {
            String pair = preparedMessage.substring(i, i + 2);
            String decryptedPair = decryptPair(pair);
            decryptedMessage.append(decryptedPair);
        }

        return decryptedMessage.toString().replaceAll("X", "");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Enter a key (at least 7 characters): ");
            String key = scanner.nextLine();

            if (!key.matches("^[a-zăâîșțA-ZĂÂÎȘȚ]+$") || key.length() < 7) {
                System.out.println("Invalid key. The key must be at least 7 characters and contain only letters.");
            } else {
                try {
                    PlayfairCipherRomanian cipher = new PlayfairCipherRomanian(key);

                    System.out.println("Choose operation (1 for encryption, 2 for decryption): ");
                    String operationStr = scanner.nextLine();

                    if (!operationStr.matches("^[12]$")) {
                        System.out.println("Invalid operation choice. Please enter 1 for encryption or 2 for decryption.");
                        continue;
                    }

                    int operation = Integer.parseInt(operationStr);

                    if (operation == 1) {
                        System.out.println("Enter a message to encrypt: ");
                        String message = scanner.nextLine();

                        if (!message.matches("^[a-zăâîșțA-ZĂÂÎȘȚ]+$")) {
                            System.out.println("Invalid message. The message must contain only letters.");
                            continue;
                        }

                        String encryptedMessage = cipher.encrypt(message);
                        System.out.println("Encrypted message: " + encryptedMessage);
                    } else if (operation == 2) {
                        System.out.println("Enter a ciphertext to decrypt: ");
                        String encryptedMessage = scanner.nextLine();

                        if (!encryptedMessage.matches("^[a-zăâîșțA-ZĂÂÎȘȚ]+$")) {
                            System.out.println("Invalid ciphertext. The ciphertext must contain only letters.");
                            continue;
                        }

                        String decryptedMessage = cipher.decrypt(encryptedMessage);
                        System.out.println("Decrypted message: " + decryptedMessage);
                    } else {
                        System.out.println("Invalid operation choice. Please enter 1 for encryption or 2 for decryption.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("Do you want to perform another operation? (1 for yes, 0 for no): ");
            String choiceStr = scanner.nextLine();

            if (!choiceStr.matches("^[01]$")) {
                System.out.println("Invalid choice. Please enter 1 for yes or 0 for no.");
                continue;
            }

            int choice = Integer.parseInt(choiceStr);

            if (choice != 1) {
                exit = true;
            }
        }

        scanner.close();
    }
}
