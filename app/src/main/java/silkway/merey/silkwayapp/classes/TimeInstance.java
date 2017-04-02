package silkway.merey.silkwayapp.classes;

import java.util.Comparator;

/**
 * Created by Merey on 06.03.17.
 */

public class TimeInstance implements Comparator {
    private String objectId;
    private Tour tour;
    private int day;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String desc;

    public TimeInstance() {

    }

    public TimeInstance(Tour tour, int startHour, int startMinute, int endHour, int endMinute, String desc, int day) {
        this.tour = tour;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.desc = desc;
        this.day = day;

    }

    public String getObjectId() {
        return objectId;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public String getDesc() {
        return desc;
    }

    //returns 1 if first slot is earlier
    //else  - returns -1
    @Override
    public int compare(Object o1, Object o2) {
        TimeInstance time1 = (TimeInstance) o1;
        TimeInstance time2 = (TimeInstance) o2;
        if (time1.startHour < time2.startHour) {
            return 1;
        }
        if (time1.startHour == time2.startHour && time1.startMinute < time2.startMinute) {
            return 1;
        }
        return -1;
    }
}
