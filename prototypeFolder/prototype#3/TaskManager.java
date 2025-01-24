import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private static HashMap<LocalDate, String> tasks = new HashMap<>();
    private static final String DEFAULT_FILE_NAME = "tasks.txt";

    // Load tasks from the default file (tasks.txt) - clearing map first
    public static void loadTasks() {
        tasks.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(DEFAULT_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    LocalDate date = LocalDate.parse(parts[0]);
                    tasks.put(date, parts[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing task file found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error reading the task file: " + e.getMessage());
        }
    }

    // Save tasks to the default file
    public static void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DEFAULT_FILE_NAME))) {
            for (Map.Entry<LocalDate, String> entry : tasks.entrySet()) {
                writer.write(entry.getKey() + "|" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Import tasks from a specified file, merging them into the existing tasks map.
     * Returns the count of tasks imported.
     */
    public static int importTasksFromFile(String filePath) {
        int importCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    LocalDate date = LocalDate.parse(parts[0]);
                    tasks.put(date, parts[1]);
                    importCount++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading file '" + filePath + "': " + e.getMessage());
        }
        // After importing, save to the default file so new tasks persist
        saveTasks();
        return importCount;
    }

    // Add a task for a specific date (SAVES IMMEDIATELY to tasks.txt)
    public static void addTask(LocalDate date, String task) {
        tasks.put(date, task);
        saveTasks();  // <--- SAVE IMMEDIATELY HERE
    }

    // Get all tasks
    public static HashMap<LocalDate, String> getTasks() {
        return tasks;
    }

    // View tasks for a specific date
    public static String getTaskForDate(LocalDate date) {
        return tasks.getOrDefault(date, null);
    }

    // Delete task by task name (if it exists)
    public static boolean deleteTaskByName(String taskName) {
        for (Map.Entry<LocalDate, String> entry : tasks.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(taskName)) {
                tasks.remove(entry.getKey());
                saveTasks();
                return true;  // Task successfully removed
            }
        }
        return false;  // Task with the given name not found
    }
}
