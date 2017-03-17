package silkway.merey.silkwayapp.classes;

/**
 * Created by Merey on 06.03.17.
 */

public class TimeInstance {
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String description;

    public TimeInstance() {

    }

    public TimeInstance(int startHour, int startMinute, int endHour, int endMinute, String description) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.description = description;
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

    public String getDescription() {
        return description;
    }
}
