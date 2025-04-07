import java.io.*;
import java.util.Scanner;

public class Main {
    public static final String FILE_NAME = "textfile.txt";
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Меню:");
            System.out.println("1 - Додати рядки до файлу");
            System.out.println("2 - Прочитати весь файл");
            System.out.println("3 - Прочитати рядки з діапазону");
            System.out.println("4 - Вставити рядок у певний рядок");
            System.out.println("0 - Вийти з редактора");
            System.out.print("Ваш вибір: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    writeMultipleLines();
                    break;
                case "2":
                    readFile();
                    break;
                case "3":
                    readLineRange();
                    break;
                case "4":
                    insertLine();
                    break;
                case "0":
                    System.out.println("Вихід...");
                    return;
                default:
                    System.out.println("Невірний вибір, спробуйте ще раз");
            }
        }
    }

    public static void writeMultipleLines() {
        System.out.println("Скільки рядків додати?");
        int count = Integer.parseInt(scanner.nextLine());

        String[] lines = new String[count];
        for (int i = 0; i < count; i++) {
            System.out.print("Рядок " + (i + 1) + ": ");
            lines[i] = scanner.nextLine();
        }

        int existingLines = countLinesInFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            for (int i = 0; i < count; i++) {
                writer.write((existingLines + i + 1) + ": " + lines[i]);
                writer.newLine();
            }
            System.out.println("Рядки успішно записано у файл");
        } catch (IOException e) {
            System.out.println("Помилка запису у файл: " + e.getMessage());
        }
    }

    public static void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println(lineNumber + ": " + lineWithoutNumber(line));
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл ще не створений або відсутній");
        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
        }
    }

    public static void readLineRange() {
        System.out.print("Початковий номер рядка: ");
        int start = Integer.parseInt(scanner.nextLine());
        System.out.print("Кінцевий номер рядка: ");
        int end = Integer.parseInt(scanner.nextLine());

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (lineNumber >= start && lineNumber <= end) {
                    System.out.println(lineNumber + ": " + lineWithoutNumber(line));
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println("Помилка при зчитуванні діапазону рядків: " + e.getMessage());
        }
    }

    public static void insertLine() {
        System.out.print("Введіть номер рядка, в який потрібно вставити: ");
        int position = Integer.parseInt(scanner.nextLine());
        System.out.print("Введіть текст для вставки: ");
        String newLine = scanner.nextLine();

        String[] lines = readAllLines();
        if (lines == null) return;

        String[] newLines = new String[lines.length + 1];
        int index = 0;
        for (int i = 0; i < newLines.length; i++) {
            if (i == position - 1) {
                newLines[i] = newLine;
            } else {
                if (index < lines.length) {
                    newLines[i] = lines[index];
                    index++;
                }
            }
        }

        if (position - 1 >= lines.length) {
            newLines[lines.length] = newLine;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < newLines.length; i++) {
                writer.write((i + 1) + ": " + newLines[i]);
                writer.newLine();
            }
            System.out.println("Рядок успішно вставлено");
        } catch (IOException e) {
            System.out.println("Помилка при вставці: " + e.getMessage());
        }
    }

    public static int countLinesInFile() {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException ignored) {}
        return count;
    }

    public static String[] readAllLines() {
        int count = countLinesInFile();
        String[] lines = new String[count];
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < count) {
                lines[i] = lineWithoutNumber(line);
                i++;
            }
        } catch (IOException e) {
            System.out.println("Помилка читання: " + e.getMessage());
            return null;
        }
        return lines;
    }

    public static String lineWithoutNumber(String line) {
        int i = 0;
        while (i < line.length() && (line.charAt(i) >= '0' && line.charAt(i) <= '9' || line.charAt(i) == ':' || line.charAt(i) == ' ')) {
            i++;
            if (i > 1 && line.charAt(i - 1) == ':' && line.charAt(i) == ' ') {
                break;
            }
        }
        return line.substring(i);
    }
}
