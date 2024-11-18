package appointmenthospital.appointmentservice.lib;

import org.joda.time.LocalTime;

public class TimeConverter {
    public static String convertToTimeString(int minutesFromMidnight) {
        return new LocalTime(0, minutesFromMidnight).toString("HH:mm");
    }
}