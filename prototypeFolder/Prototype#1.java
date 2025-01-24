import java.io.*;
import java.time.*;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class CalendarDisplay {

    private static HashMap<LocalDate, String> tasks = new HashMap<>();
    private static final String FILE_NAME = "tasks.txt";

    public static void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    LocalDate date = LocalDate.parse(parts[0]);
                    String task = parts[1];
                    tasks.put(date, task);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing task file found. Starting fresh.");
        }
    }

    public static void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (LocalDate date : tasks.keySet()) {
                writer.write(date + "|" + tasks.get(date));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public static void displayCalendar(int year) {
        for (Month month : Month.values()) {
            System.out.println("\n" + month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + year);
            System.out.println("Sun Mon Tue Wed Thu Fri Sat");

            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();
            int firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

            // Adjust for the start of the week to Sunday (Java's default is Monday)
            firstDayOfWeek = (firstDayOfWeek % 7) + 1;

            // Print initial spaces
            for (int i = 1; i < firstDayOfWeek; i++) {
                System.out.print("    ");
            }

            // Print each day of the month
            for (int day = 1; day <= daysInMonth; day++) {
                System.out.printf("%3d ", day);
                if ((day + firstDayOfWeek - 1) % 7 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    public static void addTask(int year, int month, int day, String task) {
        LocalDate date = LocalDate.of(year, month, day);
        tasks.put(date, task);
        saveTasks();
    }

    public static void viewTasks(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        if (tasks.containsKey(date)) {
            System.out.println("Tasks for " + date + ": " + tasks.get(date));
        } else {
            System.out.println("No tasks for " + date);
        }
    }

    public static void main(String[] args) {
        loadTasks();
        Scanner scanner = new Scanner(System.in);
        int year = 2025; // You can set any year you want to display

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Display Calendar");
            System.out.println("2. Add Task");
            System.out.println("3. View Tasks");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayCalendar(year);
                    break;
                case 2:
                    System.out.print("Enter year: ");
                    int taskYear = scanner.nextInt();
                    System.out.print("Enter month (1-12): ");
                    int taskMonth = scanner.nextInt();
                    System.out.print("Enter day: ");
                    int taskDay = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter task: ");
                    String task = scanner.nextLine();
                    addTask(taskYear, taskMonth, taskDay, task);
                    System.out.println("Task added.");
                    break;
                case 3:
                    System.out.print("Enter year: ");
                    int viewYear = scanner.nextInt();
                    System.out.print("Enter month (1-12): ");
                    int viewMonth = scanner.nextInt();
                    System.out.print("Enter day: ");
                    int viewDay = scanner.nextInt();
                    viewTasks(viewYear, viewMonth, viewDay);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    saveTasks();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
