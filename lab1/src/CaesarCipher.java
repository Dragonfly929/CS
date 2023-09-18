import java.util.Scanner;

public class CaesarCipher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Caesar Cipher Implementation");
            System.out.print("Enter 'encryption' (or 'e') or 'decryption' (or 'd'), or 'q' to quit: ");
            String operation = scanner.nextLine().toLowerCase();

            if (operation.equals("q")) {
                System.out.println("Exiting the program. Goodbye!");
                break;
            }

            if (!(operation.equals("encryption") || operation.equals("e") || operation.equals("decryption") || operation.equals("d"))) {
                System.out.println("Invalid operation. Please enter 'encryption' (or 'e') or 'decryption' (or 'd').");
                continue;
            }

            System.out.print("Enter the key (1-25 inclusive): ");
            int key = 0;

            try {
                key = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid key. Please enter a valid integer.");
                continue;
            }

            if (key < 1 || key > 25) {
                System.out.println("The key must be between 1 and 25 inclusive.");
                continue;
            }

            System.out.print("Enter the message: ");
            String message = scanner.nextLine().toUpperCase().replaceAll(" ", "");

            if (!message.matches("^[A-Z]+$")) {
                System.out.println("The message should only contain uppercase letters A-Z.");
                continue;
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
    }

    public static String encrypt(String message, int key) {
        StringBuilder encryptedText = new StringBuilder();

        for (char c : message.toCharArray()) {
            int charValue = c - 'A';
            char encryptedChar = (char) (((charValue + key) % 26) + 'A');
            encryptedText.append(encryptedChar);
        }

        return encryptedText.toString();
    }

    public static String decrypt(String message, int key) {
        StringBuilder decryptedText = new StringBuilder();

        for (char c : message.toCharArray()) {
            int charValue = c - 'A';
            char decryptedChar = (char) (((charValue - key + 26) % 26) + 'A');
            decryptedText.append(decryptedChar);
        }

        return decryptedText.toString();
    }
}
