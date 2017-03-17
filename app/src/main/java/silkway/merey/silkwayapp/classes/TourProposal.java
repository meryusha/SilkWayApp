package silkway.merey.silkwayapp.classes;

import com.backendless.BackendlessUser;

/**
 * Created by Merey on 08.02.17.
 */

public class TourProposal {
    private String objectId;
    private Tour tour;
    private BackendlessUser from;
    private String status;

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

