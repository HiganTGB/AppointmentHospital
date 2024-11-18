package appointmenthospital.appointmentservice.model.entity;

import lombok.Getter;

@Getter
public enum TimeSlot {
    HOUR_7_8(7 * 60,8 * 60,true,"7:00 - 8:00",1),
    HOUR_8_9(8 * 60,9*60,true,"8:00 - 9:00",2),
    HOUR_9_10(9 * 60,10*60,true,"9:00 - 10:00",3),
    HOUR_10_11(10 * 60,11*60,true,"10:00 - 11:00",4),
    HOUR_13_14(13 * 60 + 30,14 * 60 + 30,false,"13:30 - 14:30",5),
    HOUR_14_15(14 * 60 + 30,15*60+30,false,"14:30 - 15:30",6),
    HOUR_15_16(15 * 60 + 30,16*60,false,"15:30 - 16:00",7);
    private final int start;
    private final int end;
    private final boolean isMorning;
    private final String name;
    private int order;

    TimeSlot(int start, int end, boolean isMorning, String name,int order) {
        this.start = start;
        this.end = end;
        this.isMorning = isMorning;
        this.name = name;
    }
}
