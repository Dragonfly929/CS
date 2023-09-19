import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Caesar Cipher Implementation");
            System.out.println("Choose an option:");
            System.out.println("1. Single-Key Caesar Cipher");
            System.out.println("2. Two-Key Caesar Cipher");
            System.out.print("Enter '1', '2', or 'q' to quit: ");
            String option = scanner.nextLine().toLowerCase();

            if (option.equals("q")) {
                System.out.println("Exiting the program. Goodbye!");
                break;
            }

            if (!(option.equals("1") || option.equals("2"))) {
                System.out.println("Invalid option. Please enter '1' for Single-Key or '2' for Two-Key.");
                continue;
            }

            if (option.equals("1")) {
                performSingleKeyCaesarCipher(scanner);
            } else if (option.equals("2")) {
                performTwoKeyCaesarCipher(scanner);
            }
        }

        scanner.close();
    }

    public static void performSingleKeyCaesarCipher(Scanner scanner) {
        System.out.print("Enter 'encryption' (or 'e') or 'decryption' (or 'd'): ");
        String operation = scanner.nextLine().toLowerCase();

        if (!(operation.equals("encryption") || operation.equals("e") || operation.equals("decryption") || operation.equals("d"))) {
            System.out.println("Invalid operation. Please enter 'encryption' (or 'e') or 'decryption' (or 'd').");
            return;
        }

        System.out.print("Enter the key (1-25 inclusive): ");
        int key = 0;

        try {
            key = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid key. Please enter a valid integer.");
            return;
        }

        if (key < 1 || key > 25) {
            System.out.println("The key must be between 1 and 25 inclusive.");
            return;
        }

        System.out.print("Enter the message: ");
        String message = scanner.nextLine().toUpperCase().replaceAll(" ", "");

        if (!message.matches("^[A-Z]+$")) {
            System.out.println("The message should only contain letters A-Z.");
            return;
        }

        String result = "";

        if (operation.equals("encryption") || operation.equals("e")) {
            result = encrypt(message, key);
            System.out.println("Encrypted message: " + result);
        } else {
            result = decrypt(message, key);
            System.out.println("Decrypted message: " + result);
        }
    }

    public static void performTwoKeyCaesarCipher(Scanner scanner) {
        System.out.print("Enter operation (e for encrypt, d for decrypt, or q to quit): ");
        String operation = scanner.nextLine().trim().toLowerCase();

        if ("q".equals(operation)) {
            System.out.println("Exiting the program. Goodbye!");
            return;
        } else if (!operation.equals("e") && !operation.equals("d")) {
            System.out.println("Invalid operation. Please enter e for encrypt, d for decrypt, or q to quit.");
            return;
        }

        System.out.print("Enter the first key (Integer value between 1-25): ");
        String key1Input = scanner.nextLine().trim();

        int key1;
        try {
            key1 = Integer.parseInt(key1Input);
            if (!(1 <= key1 && key1 <= 25)) {
                System.out.println("Invalid key. Key must be between 1 and 25.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid key. Key must be an integer between 1 and 25.");
            return;
        }

        System.out.print("Enter the second key (minimum 7 characters): ");
        String key2 = scanner.nextLine().trim();

        if (key2.length() < 7) {
            System.out.println("Invalid key. Second key must be at least 7 characters long.");
            return;
        }

        System.out.print("Enter message: ");
        String message = scanner.nextLine();
        String result = "";

        if (operation.equals("e")) {
            result = CaesarCipher_2key.caesarCipher(message, key1, key2, "encrypt");
        } else if (operation.equals("d")) {
            result = CaesarCipher_2key.caesarCipher(message, key1, key2, "decrypt");
        }

        System.out.println("Result: " + result);
    }

    public static String encrypt(String message, int key) {
        StringBuilder encryptedText = new StringBuilder();

        for (char c : message.toCharArray()) {
            char encryptedChar = (char) (c + key);
            if (encryptedChar > 'Z') {
                encryptedChar = (char) (encryptedChar - 26);
            }
            encryptedText.append(encryptedChar);
        }

        return encryptedText.toString();
    }

    public static String decrypt(String message, int key) {
        StringBuilder decryptedText = new StringBuilder();

        for (char c : message.toCharArray()) {
            char decryptedChar = (char) (c - key);
            if (decryptedChar < 'A') {
                decryptedChar = (char) (decryptedChar + 26);
            }
            decryptedText.append(decryptedChar);
        }

        return decryptedText.toString();
    }
}
