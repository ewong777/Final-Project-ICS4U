import java.time.LocalDate;
import java.time.YearMonth;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.DateTimeException;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class CalendarDisplay {

    // Method to display the calendar for a specific month and year
    public static void displayCalendar(int year, int month) {
        try {
            if (month < 1 || month > 12) {
                System.out.println("Invalid month. Please enter a month between 1 and 12.");
                return;
            }

            Month selectedMonth = Month.of(month);
            System.out.println("\n===== "
                    + selectedMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                    + " " + year + " =====");
            System.out.println("Sun Mon Tue Wed Thu Fri Sat");

            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();

            // 1 = Monday, 7 = Sunday
            int firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue();
            // Adjust so that Sunday = 1, Monday = 2, ... Saturday = 7
            firstDayOfWeek = (firstDayOfWeek % 7) + 1;

            // Print leading spaces
            for (int i = 1; i < firstDayOfWeek; i++) {
                System.out.print("    ");
            }

            // Print days
            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate currentDate = LocalDate.of(year, month, day);

                // Check if date is a holiday
                if (HolidayManager.getHolidays().containsKey(currentDate)) {
                    System.out.printf("[%2d]", day); // Highlight holiday
                }
                // Check if date has a task
                else if (TaskManager.getTasks() != null
                        && TaskManager.getTasks().containsKey(currentDate)) {
                    System.out.printf("*%2d*", day); // Highlight task date
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
            System.out.println("Invalid month or year.");
        }
    }

    // Method to view tasks for a specific month and year
    public static void viewTasksByMonth(int year, int month) {
        System.out.println("\n===== Tasks for "
                + Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                + " " + year + " =====");
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
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize holidays and tasks
        HolidayManager.loadHolidays();
        TaskManager.loadTasks();  // Load from tasks.txt at startup

        // Main menu loop
        while (true) {
            System.out.println("\n1. View Calendar");
            System.out.println("2. Add Task");
            System.out.println("3. View Today's Tasks");
            System.out.println("4. View Tasks by Month");
            System.out.println("5. Delete Task by Name");
            System.out.println("6. Exit");
            System.out.println("7. Import Tasks from .txt file (merge)");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter year: ");
                int year = scanner.nextInt();
                System.out.print("Enter month (1-12): ");
                int month = scanner.nextInt();
                displayCalendar(year, month);
            } 
            else if (choice == 2) {
                scanner.nextLine(); // Consume newline
                System.out.print("Enter date (YYYY-MM-DD): ");
                String dateInput = scanner.nextLine();

                // Add exception handling for invalid date input
                try {
                    LocalDate date = LocalDate.parse(dateInput);
                    System.out.print("Enter task: ");
                    String task = scanner.nextLine();
                    TaskManager.addTask(date, task);
                    System.out.println("Task added successfully (and saved).");
                } catch (Exception e) {
                    System.out.println("Invalid date format. Please enter a valid date in YYYY-MM-DD format.");
                }
            }
            else if (choice == 3) {
                // View today's tasks
                LocalDate today = LocalDate.now();
                String taskForToday = TaskManager.getTaskForDate(today);
                if (taskForToday != null) {
                    System.out.println("Today's task: " + taskForToday);
                } else {
                    System.out.println("No tasks for today.");
                }
            }
            else if (choice == 4) {
                // View tasks for a specific month and year
                System.out.print("Enter year: ");
                int year = scanner.nextInt();
                System.out.print("Enter month (1-12): ");
                int month = scanner.nextInt();
                viewTasksByMonth(year, month);
            }
            else if (choice == 5) {
                // Delete task by task name
                scanner.nextLine(); // Consume newline
                System.out.print("Enter the name of the task to delete: ");
                String taskName = scanner.nextLine();

                boolean deleted = TaskManager.deleteTaskByName(taskName);
                if (deleted) {
                    System.out.println("Task deleted successfully.");
                } else {
                    System.out.println("Task with that name not found.");
                }
            }
            else if (choice == 6) {
                TaskManager.saveTasks();  // Save tasks one last time before exiting
                break;
            }
            else if (choice == 7) {
                // Import tasks from another .txt file (merging with existing tasks)
                scanner.nextLine(); // Consume newline
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
