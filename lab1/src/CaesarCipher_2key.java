import java.util.Scanner;

public class CaesarCipher_2key {

    // Function to generate a new alphabet based on key2
    public static String generateNewAlphabet(String key2) {
        // Sort and remove duplicates from key2, convert to uppercase
        key2 = key2.toUpperCase();
        char[] key2Chars = key2.toCharArray();
        StringBuilder uniqueKey2 = new StringBuilder();

        for (char c : key2Chars) {
            if (uniqueKey2.indexOf(String.valueOf(c)) == -1) {
                uniqueKey2.append(c);
            }
        }

        // Generate the original alphabet (A-Z)
        StringBuilder alphabet = new StringBuilder();
        for (char c = 'A'; c <= 'Z'; c++) {
            alphabet.append(c);
        }

        // Create a new alphabet by adding key2 and removing its characters from the original alphabet
        StringBuilder newAlphabet = new StringBuilder(uniqueKey2.toString());

        for (int i = 0; i < alphabet.length(); i++) {
            char c = alphabet.charAt(i);
            if (uniqueKey2.indexOf(String.valueOf(c)) == -1) {
                newAlphabet.append(c);
            }
        }

        System.out.println("New Alphabet: " + newAlphabet.toString());

        return newAlphabet.toString();
    }

    // Function to perform Caesar cipher encryption/decryption
    public static String caesarCipher(String inputText, int key1, String key2, String operation) {
        // Generate the new alphabet
        String newAlphabet = generateNewAlphabet(key2);

        // Check if key1 is within the valid range
        if (!(1 <= key1 && key1 <= 25)) {
            return "Invalid key. Key must be between 1 and 25.";
        }

        // Convert input text to uppercase and remove spaces
        String modifiedText = inputText.replaceAll(" ", "").toUpperCase();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < modifiedText.length(); i++) {
            char c = modifiedText.charAt(i);
            if (newAlphabet.indexOf(c) != -1) {
                int index = newAlphabet.indexOf(c);
                int newIndex;
                if ("encrypt".equals(operation)) {
                    newIndex = (index + key1) % newAlphabet.length();
                } else if ("decrypt".equals(operation)) {
                    newIndex = (index - key1 + newAlphabet.length()) % newAlphabet.length();
                } else {
                    return "Invalid operation. Operation must be encrypt, decrypt, or exit.";
                }
                result.append(newAlphabet.charAt(newIndex));
            } else {
                return "Invalid character. Only alphabet characters (A-Z) are allowed.";
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Select operation (encrypt, decrypt, or exit to quit): ");
            String operation = scanner.nextLine().trim().toLowerCase();

            if ("exit".equals(operation)) {
                System.out.println("Exiting the program. Goodbye!");
                break;
            } else if (!operation.equals("encrypt") && !operation.equals("decrypt")) {
                System.out.println("Invalid operation. Please enter encrypt, decrypt, or exit to quit.");
                continue;
            }

            System.out.print("Enter the first key (Integer value between 1-25): ");
            String key1Input = scanner.nextLine().trim();

            int key1;
            try {
                key1 = Integer.parseInt(key1Input);
                if (!(1 <= key1 && key1 <= 25)) {
                    System.out.println("Invalid key. Key must be between 1 and 25.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid key. Key must be an integer between 1 and 25.");
                continue;
            }

            System.out.print("Enter the second key (minimum 7 characters): ");
            String key2 = scanner.nextLine().trim();

            if (key2.length() < 7) {
                System.out.println("Invalid key. Second key must be at least 7 characters long.");
                continue;
            }

            System.out.print("Enter message: ");
            String message = scanner.nextLine();
            String result = caesarCipher(message, key1, key2, operation);
            System.out.println("Result: " + result);
        }

        scanner.close();
    }
}
