import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

        // Try other English-friendly formats
        try {
            return LocalDate.parse(input,
                DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH));
        } catch (Exception ignored) {}

        try {
            return LocalDate.parse(input,
                DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));
        } catch (Exception ignored) {}

        System.out.println("Invalid date format. Please use YYYY-MM-DD or another valid format.");
        return null;
    }

    public static boolean isValidMonth(int month) {
        return month >= 1 && month <= 12;
    }
}
