import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class CalendarDisplay {
    private static HashMap<LocalDate, String> tasks = new HashMap<>();
    private static final String FILE_NAME = "tasks.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    LocalDate date = LocalDate.parse(parts[0]);
                    tasks.put(date, parts[1]);
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
        System.out.println("\n===== " + year + " Calendar =====");
        for (Month month : Month.values()) {
            System.out.println("\n" + month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + year);
            System.out.println("Sun Mon Tue Wed Thu Fri Sat");

            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();
            int firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

            firstDayOfWeek = (firstDayOfWeek % 7) + 1; // Adjust to Sunday start

            for (int i = 1; i < firstDayOfWeek; i++) {
                System.out.print("    ");
            }

            for (int day = 1; day <= daysInMonth; day++) {
                System.out.printf("%3d ", day);
                if ((day + firstDayOfWeek - 1) % 7 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    public static LocalDate parseDateInput(String input) {
        input = input.trim().replace("/", "-").replace(".", "-");

        try {
            return LocalDate.parse(input, DATE_FORMATTER);
        } catch (Exception ignored) {}

        String[] parts = input.split("\\s+");
        if (parts.length == 3) {
            try {
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);
                return LocalDate.of(year, month, day);
            } catch (Exception ignored) {}
        }

        try {
            return LocalDate.parse(input, DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH));
        } catch (Exception ignored) {}

        try {
            return LocalDate.parse(input, DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));
        } catch (Exception ignored) {}

        System.out.println("Invalid date format. Please use YYYY-MM-DD or other valid formats.");
        return null;
    }

    public static void addTask(String dateInput, String task) {
        LocalDate date = parseDateInput(dateInput);
        if (date != null) {
            tasks.put(date, task);
            saveTasks();
            System.out.println("Task added successfully for " + date);
        }
    }

    public static void viewTasks(String dateInput) {
        LocalDate date = parseDateInput(dateInput);
        if (date != null) {
            if (tasks.containsKey(date)) {
                System.out.println("Tasks for " + date + ": " + tasks.get(date));
            } else {
                System.out.println("No tasks for " + date);
            }
        }
    }

    public static void viewTodayTasks() {
        LocalDate today = LocalDate.now();
        if (tasks.containsKey(today)) {
            System.out.println("Tasks for today (" + today + "): " + tasks.get(today));
        } else {
            System.out.println("No tasks for today.");
        }
    }

    public static void viewTasksForMonth(int year, int month) {
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            boolean hasTasks = false;
            System.out.println("\nTasks for " + yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + year + ":");

            for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
                LocalDate date = LocalDate.of(year, month, day);
                if (tasks.containsKey(date)) {
                    System.out.println(date + ": " + tasks.get(date));
                    hasTasks = true;
                }
            }

            if (!hasTasks) {
                System.out.println("No tasks found for this month.");
            }
        } catch (Exception e) {
            System.out.println("Invalid month input.");
        }
    }

    public static void viewTasksForYear(int year) {
        boolean hasTasks = false;
        System.out.println("\nTasks for the year " + year + ":");

        for (Month month : Month.values()) {
            YearMonth yearMonth = YearMonth.of(year, month);
            for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
                LocalDate date = LocalDate.of(year, month, day);
                if (tasks.containsKey(date)) {
                    System.out.println(date + ": " + tasks.get(date));
                    hasTasks = true;
                }
            }
        }

        if (!hasTasks) {
            System.out.println("No tasks found for this year.");
        }
    }

    public static void main(String[] args) {
        loadTasks();
        Scanner scanner = new Scanner(System.in);

        int year = LocalDate.now().getYear();
        displayCalendar(year);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks (Today, Specific Date, Month, or Year)");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter date (YYYY-MM-DD or other formats): ");
                    String taskDate = scanner.nextLine().trim();
                    System.out.print("Enter task: ");
                    String task = scanner.nextLine();
                    addTask(taskDate, task);
                    break;
                case 2:
                    System.out.println("Choose an option:");
                    System.out.println("1. View Tasks for Today");
                    System.out.println("2. View Tasks for a Specific Date");
                    System.out.println("3. View Tasks for a Specific Month");
                    System.out.println("4. View Tasks for a Specific Year");
                    System.out.print("Choose an option: ");
                    int viewChoice;
                    try {
                        viewChoice = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        continue;
                    }

                    switch (viewChoice) {
                        case 1:
                            viewTodayTasks();
                            break;
                        case 2:
                            System.out.print("Enter date (YYYY-MM-DD or other formats): ");
                            String viewDate = scanner.nextLine().trim();
                            viewTasks(viewDate);
                            break;
                        case 3:
                            System.out.print("Enter year: ");
                            int taskYear = Integer.parseInt(scanner.nextLine().trim());
                            System.out.print("Enter month (1-12 or name): ");
                            String monthInput = scanner.nextLine().trim();

                            int taskMonth = 0;
                            try {
                                taskMonth = Integer.parseInt(monthInput);
                            } catch (NumberFormatException e) {
                                for (Month month : Month.values()) {
                                    if (month.getDisplayName(TextStyle.FULL, Locale.ENGLISH).equalsIgnoreCase(monthInput) ||
                                        month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).equalsIgnoreCase(monthInput)) {
                                        taskMonth = month.getValue();
                                        break;
                                    }
                                }
                            }

                            if (taskMonth >= 1 && taskMonth <= 12) {
                                viewTasksForMonth(taskYear, taskMonth);
                            } else {
                                System.out.println("Invalid month input.");
                            }
                            break;
                        case 4:
                            System.out.print("Enter year: ");
                            int yearInput = Integer.parseInt(scanner.nextLine().trim());
                            viewTasksForYear(yearInput);
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a valid option.");
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    saveTasks();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}
