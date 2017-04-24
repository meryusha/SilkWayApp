package silkway.merey.silkwayapp.classes;

import com.backendless.BackendlessUser;

/**
 * Created by Merey on 24.04.17.
 */

public class EasyTourProposal {
    private Tour tour;
    private String status;
    private String text;
    private String objectId;
    private BackendlessUser author;

    public EasyTourProposal() {

    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public BackendlessUser getAuthor() {
        return author;
    }

    public void setAuthor(BackendlessUser author) {
        this.author = author;
    }
}
