package appointmenthospital.scheduleservice.model.entity;

import lombok.Getter;

@Getter
public enum DayOfWeek {
     MONDAY(1),
     TUESDAY(2),
     WEDNESDAY(3),
     THURSDAY(4),
     FRIDAY(5),
     SATURDAY(6),
     SUNDAY(0);

     private final int value;

     DayOfWeek(int value) {
          this.value = value;
     }
     public static DayOfWeek getDayOfWeekFromInt(int value) {
          for (DayOfWeek day : DayOfWeek.values()) {
               if (day.getValue() == value) {
                    return day;
               }
          }
          throw new IllegalArgumentException("Invalid day of week value: " + value);
     }
}