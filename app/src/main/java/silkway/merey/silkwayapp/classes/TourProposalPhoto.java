package silkway.merey.silkwayapp.classes;

/**
 * Created by Merey on 17.04.17.
 */

public class TourProposalPhoto {
    private String objectId;
    private TourProposal tourProposal;
    private String url;
    private String desc;

    // private

    public TourProposalPhoto() {

    }

    public TourProposalPhoto(TourProposal tourProposal, String url, String desc) {
        this.tourProposal = tourProposal;
        this.url = url;
        this.desc = desc;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public TourProposal getTourProposal() {
        return tourProposal;
    }

    public void setTourProposal(TourProposal tourProposal) {
        this.tourProposal = tourProposal;
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
