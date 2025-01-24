import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;

public class HolidayManager {
    private static HashMap<LocalDate, String> holidays = new HashMap<>();

    /**
     * Loads recurring Canadian holidays (both fixed and dynamic)
     * for a wide range of years. Adjust year range if needed.
     */
    public static void loadHolidays() {
        try {
            int startYear = 2000;
            int endYear   = 2100;

            for (int year = startYear; year <= endYear; year++) {
                // New Year's Day
                holidays.put(LocalDate.of(year, 1, 1), "New Year's Day");

                // Family Day (Third Monday in February)
                LocalDate familyDay = LocalDate.of(year, 2, 1)
                        .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
                holidays.put(familyDay, "Family Day");

                // Victoria Day (Last Monday before May 25)
                LocalDate victoriaDay = LocalDate.of(year, 5, 24)
                        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                holidays.put(victoriaDay, "Victoria Day");

                // Canada Day
                holidays.put(LocalDate.of(year, 7, 1), "Canada Day");

                // Labour Day (First Monday in September)
                LocalDate labourDay = LocalDate.of(year, 9, 1)
                        .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
                holidays.put(labourDay, "Labour Day");

                // Thanksgiving (Second Monday in October)
                LocalDate thanksgiving = LocalDate.of(year, 10, 1)
                        .with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.MONDAY));
                holidays.put(thanksgiving, "Thanksgiving Day");

                // Remembrance Day (November 11)
                holidays.put(LocalDate.of(year, 11, 11), "Remembrance Day");

                // Christmas Day (December 25)
                holidays.put(LocalDate.of(year, 12, 25), "Christmas Day");

                // Boxing Day (December 26)
                holidays.put(LocalDate.of(year, 12, 26), "Boxing Day");
            }
        } catch (Exception e) {
            System.err.println("Error loading holidays: " + e.getMessage());
        }
    }

    public static HashMap<LocalDate, String> getHolidays() {
        return holidays;
    }
}
