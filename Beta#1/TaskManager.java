import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private static HashMap<LocalDate, String> tasks = new HashMap<>();
    private static final String DEFAULT_FILE_NAME = "tasks.txt";

    public static void loadTasks() {
        tasks.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(DEFAULT_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    try {
                        LocalDate date = LocalDate.parse(parts[0]);
                        tasks.put(date, parts[1]);
                    } catch (DateTimeParseException e) {
                        System.err.println("Skipping invalid date in tasks file: " + line);
                    }
                } else {
                    System.err.println("Skipping malformed line in tasks file: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing task file found. Starting fresh.");
        } catch (IOException e) {
            System.err.println("Error reading the task file: " + e.getMessage());
        }
    }

    public static void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DEFAULT_FILE_NAME))) {
            for (Map.Entry<LocalDate, String> entry : tasks.entrySet()) {
                writer.write(entry.getKey() + "|" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Merge tasks from a specified file (does NOT clear existing tasks first).
     */
    public static int importTasksFromFile(String filePath) {
        int importCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    try {
                        LocalDate date = LocalDate.parse(parts[0]);
                        tasks.put(date, parts[1]);  
                        importCount++;
                    } catch (DateTimeParseException e) {
                        System.err.println("Skipping invalid date in import file: " + line);
                    }
                } else {
                    System.err.println("Skipping malformed line in import file: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath + " - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file '" + filePath + "': " + e.getMessage());
        }
        saveTasks();
        return importCount;
    }

    /**
     * Add a (single) task for a specific date, then immediately save to disk.
     */
    public static void addTask(LocalDate date, String task) {
        tasks.put(date, task);
        saveTasks();
    }

    /**
     * Returns the entire tasks map (date -> single task).
     */
    public static HashMap<LocalDate, String> getTasks() {
        return tasks;
    }

    /**
     * Returns the single task for a specific date, or null if none exist.
     */
    public static String getTaskForDate(LocalDate date) {
        return tasks.getOrDefault(date, null);
    }

    /**
     * Delete a task by its name (case-insensitive).
     * Returns true if deleted, false if not found.
     */
    public static boolean deleteTaskByName(String taskName) {
        for (Map.Entry<LocalDate, String> entry : tasks.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(taskName)) {
                tasks.remove(entry.getKey());
                saveTasks();
                return true;  // Task successfully removed
            }
        }
        return false;  // Task not found
    }

    /**
     * NEW METHOD: View tasks that match *today's* date, in a style
     * similar to 'viewTasksByMonth'. Iterates over all tasks and checks
     * which ones are for today's date.
     */
    public static void viewTasksForToday() {
        LocalDate today = LocalDate.now();
        System.out.println("\n===== Tasks for " + today + " =====");
        boolean tasksFound = false;

        // Iterate over all tasks
        if (tasks != null && !tasks.isEmpty()) {
            for (Map.Entry<LocalDate, String> entry : tasks.entrySet()) {
                LocalDate taskDate = entry.getKey();
                // Compare: if it matches today's date
                if (taskDate.equals(today)) {
                    System.out.println(taskDate + ": " + entry.getValue());
                    tasksFound = true;
                }
            }
        }

        if (!tasksFound) {
            System.out.println("No tasks for today.");
        }
    }
}
