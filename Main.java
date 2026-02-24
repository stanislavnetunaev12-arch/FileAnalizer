import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите путь к файлу: ");
        String filePath = scanner.nextLine();

        System.out.print("Введите кодировку файла ");
        String charsetName = scanner.nextLine();
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("Ошибка: Файл не существует.");
            return;
        }


        try {
            if (!Charset.isSupported(charsetName)) {
                throw new UnsupportedCharsetException("Кодировка " + charsetName + " не поддерживается.");
            }
            analyzeFile(file, charsetName, scanner);

        } catch (UnsupportedCharsetException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void analyzeFile(File file, String charsetName, Scanner scanner) throws IOException {
        int totalLines = 0;
        int totalChars = 0;
        int nonEmptyLines = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), charsetName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                totalChars += line.length();

                if (!line.isBlank()) {
                    nonEmptyLines++;
                }
            }
        }

        System.out.println("\nСтатистика файла:");
        System.out.println("Общее количество строк: " + totalLines);
        System.out.println("Общее количество символов (без учета символов перевода строки): " + totalChars);
        System.out.println("Количество непустых строк: " + nonEmptyLines);

        System.out.print("\nХотите найти количество вхождений слова? (y/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (answer.equals("y")) {
            System.out.print("Введите слово для поиска: ");
            String searchWord = scanner.nextLine().trim();

            if (!searchWord.isEmpty()) {
                int wordCount = countWordOccurrences(file, charsetName, searchWord);
                System.out.println("Количество вхождений слова '" + searchWord + "': " + wordCount);
            } else {
                System.out.println("Слово для поиска не может быть пустым.");
            }
        }
    }

    private static int countWordOccurrences(File file, String charsetName, String searchWord) throws IOException {
        int count = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), charsetName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (word.equals(searchWord)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }
}