import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

public class CalendarDisplay{
    public static void displayCalendar(int year) {
        for (Month month : Month.values()) {
            System.out.println("\n" + month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + year);
            System.out.println("Sun Mon Tue Wed Thu Fri Sat");

            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();
            int firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue();

            firstDayOfWeek = (firstDayOfWeek % 7) + 1;

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

    public static void main(String[] args) {
        int year = 2025;
        displayCalendar(year);
    }

}