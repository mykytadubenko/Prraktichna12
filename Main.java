import java.io.*;
import java.util.Scanner;

public class Main {
    public static final String FILE_NAME = "textfile.txt";
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Меню:");
            System.out.println("1 - Записати рядки до файлу");
            System.out.println("2 - Прочитати весь файл з нумерацією");
            System.out.println("3 - Прочитати діапазон рядків з файлу");
            System.out.println("0 - Вийти з редактора");
            System.out.print("Ваш вибір: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    writeMultiLines();
                    break;
                case "2":
                    readWithNumbers();
                    break;
                case "3":
                    readRange();
                    break;
                case "0":
                    System.out.println("Вихід...");
                    return;
                default:
                    System.out.println("Невірний вибір, спробуйте ще раз");
            }
        }
    }

    public static void writeMultiLines() {
        System.out.print("Скільки рядків бажаєте додати:");
        int count = readNumber();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            int currentLineNumber = countLineInFile();
            for (int i = 0; i < count; i++) {
                System.out.print("Введіть рядок №" + (currentLineNumber + 1) + ": ");
                String line = scanner.nextLine();
                writer.write(line);
                writer.newLine();
                currentLineNumber++;
            }
            System.out.println("Рядки успішно записані у файл");
        } catch (IOException e) {
            System.out.println("Помилка запису у файл:" + e.getMessage());
        }
    }

    public static void readWithNumbers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int lineNumber = 1;
            System.out.println("Вміст файлу:");
            while ((line = reader.readLine()) != null) {
                System.out.println(lineNumber + ": " + line);
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл ще не створений або відсутній");
        } catch (IOException e) {
            System.out.println("Помилка читання файлу:" + e.getMessage());
        }
    }

    public static void readRange() {
        int totalLines = countLineInFile();
        if (totalLines == 0) {
            System.out.println("Файл порожній");
            return;
        }

        System.out.print("Введіть номер початкового рядка (від 1):");
        int start = readNumber();
        System.out.print("Введіть номер кінцевого рядка:");
        int end = readNumber();

        if (start > end || start < 1 || end > totalLines) {
            System.out.println("Некоректний діапазон рядків");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (lineNumber >= start && lineNumber <= end) {
                    System.out.println(lineNumber + ": " + line);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println("Помилка читання файлу:" + e.getMessage());
        }
    }

    public static int countLineInFile() {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException ignored) {
        }
        return count;
    }

    public static int readNumber() {
        while (true) {
            String input = scanner.nextLine();
            boolean isValid = true;
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (c < '0' || c > '9') {
                    isValid = false;
                    break;
                }
            }
            if (isValid && !input.equals("")) {
                return Integer.parseInt(input);
            }
            System.out.print("Введіть коректне число:");
        }
    }
}
