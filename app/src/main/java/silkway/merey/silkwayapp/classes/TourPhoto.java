package silkway.merey.silkwayapp.classes;

/**
 * Created by Merey on 06.02.17.
 */
public class TourPhoto {
    private String objectId;
    private Tour tour;
    private String url;
    private String desc;

    // private

    public TourPhoto() {

    }

    public TourPhoto(Tour tour, String url, String desc) {
        this.tour = tour;
        this.url = url;
        this.desc = desc;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
