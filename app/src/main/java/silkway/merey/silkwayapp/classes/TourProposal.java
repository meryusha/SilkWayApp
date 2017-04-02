package silkway.merey.silkwayapp.classes;

import com.backendless.BackendlessUser;

/**
 * Created by Merey on 08.02.17.
 */

public class TourProposal {
    private String objectId;
    private Tour tour;
    private BackendlessUser from;
    private BackendlessUser to;
    private String status;
    private String season;
    private String duration;
    private int price;
    private String title;
    private String avatarUrl;
    private Category category;
    private int capacity;
    private String desc;
    private Location location;
    private String requirements;

    public TourProposal() {

    }

    public TourProposal(Tour tour, BackendlessUser from, BackendlessUser to, String season, String duration, int price, String title, String avatarUrl, Category category, int capacity, String desc, Location location, String requirements) {
        this.tour = tour;
        this.from = from;
        this.to = to;
        this.season = season;
        this.duration = duration;
        this.price = price;
        this.title = title;
        this.avatarUrl = avatarUrl;
        this.category = category;
        this.capacity = capacity;
        this.desc = desc;
        this.location = location;
        this.requirements = requirements;
    }

    public BackendlessUser getTo() {
        return to;
    }

    public void setTo(BackendlessUser to) {
        this.to = to;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public BackendlessUser getFrom() {
        return from;
    }

    public void setFrom(BackendlessUser from) {
        this.from = from;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

