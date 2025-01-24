import java.time.LocalDate;
import java.time.YearMonth;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class CalendarDisplay {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Repeatedly prompts the user until a valid integer is provided.
     * @param prompt The text to show before reading input.
     * @return A valid integer.
     */
    private static int parseIntegerInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    /**
     * Repeatedly prompts the user until a valid LocalDate is parsed
     * in one of several formats: yyyy-MM-dd, yyyy MM dd, MMM d, yyyy,
     * MMMM d, yyyy.
     * @param prompt The text to show before reading input.
     * @return A valid LocalDate.
     */
    private static LocalDate parseDateInput(String prompt) {
        // Define a list of possible date patterns
        List<DateTimeFormatter> formatters = new ArrayList<>();
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));         // 2025-01-24
        formatters.add(DateTimeFormatter.ofPattern("yyyy MM dd"));         // 2025 01 24
        formatters.add(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)); // January 24, 2025
        formatters.add(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));   // Jan 24, 2025

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            // Attempt each format
            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDate.parse(input, formatter);
                } catch (DateTimeParseException e) {
                    // We'll try the next format
                }
            }
            // If we reach here, none of the formats succeeded
            System.out.println("Invalid date format. Try: yyyy-MM-dd, yyyy MM dd, Jan 24, 2025, etc.");
        }
    }

    /**
     * Displays a textual calendar for the given year and month.
     * Highlights holidays ([DD]) and tasks (*DD*) if present.
     */
    public static void displayCalendar(int year, int month) {
        try {
            if (month < 1 || month > 12) {
                System.out.println("Invalid month. Must be 1–12.");
                return;
            }

            Month selectedMonth = Month.of(month);
            System.out.println("\n===== "
                    + selectedMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                    + " " + year + " =====");
            System.out.println("Sun Mon Tue Wed Thu Fri Sat");

            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();

            // dayOfWeek: 1=Mon, 7=Sun
            int firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue();
            // Convert so Sunday=1, Monday=2, etc.
            firstDayOfWeek = (firstDayOfWeek % 7) + 1;

            // Print leading spaces
            for (int i = 1; i < firstDayOfWeek; i++) {
                System.out.print("    ");
            }

            // Print each day
            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate currentDate = LocalDate.of(year, month, day);

                // Check holiday
                if (HolidayManager.getHolidays().containsKey(currentDate)) {
                    System.out.printf("[%2d]", day); // highlight holiday
                }
                // Check task
                else if (TaskManager.getTasks() != null
                        && TaskManager.getTasks().containsKey(currentDate)) {
                    System.out.printf("*%2d*", day); // highlight task date
                } else {
                    System.out.printf("%3d ", day);
                }

                // New line after Saturday
                if ((day + firstDayOfWeek - 1) % 7 == 0) {
                    System.out.println();
                }
            }

            System.out.println();
        } catch (DateTimeException e) {
            System.out.println("Error displaying calendar: " + e.getMessage());
        }
    }



        boolean tasksFound = false;
        if (TaskManager.getTasks() != null) {
            for (Map.Entry<LocalDate, String> entry : TaskManager.getTasks().entrySet()) {
                LocalDate taskDate = entry.getKey();
                if (taskDate.getYear() == year && taskDate.getMonthValue() == month) {
                    System.out.println(taskDate + ": " + entry.getValue());
                    tasksFound = true;
                }
            }
        }

        if (!tasksFound) {
            System.out.println("No tasks for this month.");
        }
    

    public static void main(String[] args) {
        // Initialize holidays & tasks
        HolidayManager.loadHolidays();
        TaskManager.loadTasks();

        while (true) {
            System.out.println("\n1. View Calendar");
            System.out.println("2. Add Task");
            System.out.println("3. View Today's Tasks");
            System.out.println("4. View Tasks by Month");
            System.out.println("5. Delete Task by Name");
            System.out.println("6. Exit");
            System.out.println("7. Import Tasks from .txt file (merge)");

            // parse menu choice
            int choice = parseIntegerInput("Enter your choice: ");

            if (choice == 1) {
                // View Calendar
                int year = parseIntegerInput("Enter year: ");
                int month = parseIntegerInput("Enter month (1–12): ");
                displayCalendar(year, month);
            }
            else if (choice == 2) {
                // Add Task (flexible date parsing)
                LocalDate date = parseDateInput("Enter date (e.g., 2025-01-24, 2025 01 24, Jan 24, 2025, etc.): ");
                System.out.print("Enter task: ");
                String task = scanner.nextLine();
                TaskManager.addTask(date, task);
                System.out.println("Task added successfully.");
            }
            else if (choice == 3) {
                // View today's tasks
                TaskManager.viewTasksForToday();
            }
            else if (choice == 4) {
                // View tasks by a specific month/year
                int year = parseIntegerInput("Enter year: ");
                int month = parseIntegerInput("Enter month (1–12): ");
                TaskManager.viewTasksByMonth(year, month);
            }
            else if (choice == 5) {
                // Delete task by name
                System.out.print("Enter the name of the task to delete: ");
                String taskName = scanner.nextLine();
                boolean deleted = TaskManager.deleteTaskByName(taskName);
                if (deleted) {
                    System.out.println("Task deleted successfully.");
                } else {
                    System.out.println("No task found with that name.");
                }
            }
            else if (choice == 6) {
                // Exit
                TaskManager.saveTasks();
                System.out.println("Exiting program...");
                break;
            }
            else if (choice == 7) {
                // Import tasks from a file
                System.out.print("Enter file path to import (default: tasks.txt): ");
                String filePath = scanner.nextLine().trim();
                if (filePath.isEmpty()) {
                    filePath = "tasks.txt";
                }
                int importedCount = TaskManager.importTasksFromFile(filePath);
                System.out.println(importedCount + " tasks imported from '" + filePath + "'");
            }
            else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
