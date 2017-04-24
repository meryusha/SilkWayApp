package silkway.merey.silkwayapp.classes;

import com.backendless.BackendlessUser;

/**
 * Created by Merey on 06.02.17.
 */
public class Tour {
    private String objectId;
    private String season;
    private int view;
    private BackendlessUser author;
    private String duration;
    private int price;
    private String title;

    private String avatarUrl;
    private Category category;
    private int capacity;
    private int numberProposals;
    private String desc;
    private boolean isOpen;
    private Location location;
    private String requirements;
    private BackendlessUser operator;

    public Tour() {
    }

    public Tour(BackendlessUser author, String duration, int price, String title, String videoUrl, String season, String avatarUrl, Category category, int capacity, String desc, boolean isOpen, Location location, String requirements) {
        this.season = season;
        this.author = author;
        this.duration = duration;
        this.price = price;
        this.title = title;
        this.avatarUrl = avatarUrl;
        this.category = category;
        this.capacity = capacity;
        this.desc = desc;
        this.isOpen = isOpen;
        this.location = location;
        this.requirements = requirements;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public BackendlessUser getAuthor() {
        return author;
    }

    public void setAuthor(BackendlessUser author) {
        this.author = author;
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

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
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

    public void setTitle(String title) {
        this.title = title;
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

    public int getNumberProposals() {
        return numberProposals;
    }

    public void setNumberProposals(int numberProposals) {
        this.numberProposals = numberProposals;
    }

    public BackendlessUser getOperator() {
        return operator;
    }

    public void setOperator(BackendlessUser operator) {
        this.operator = operator;
    }
}

