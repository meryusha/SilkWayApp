package silkway.merey.silkwayapp.classes;

/**
 * Created by Merey on 17.03.17.
 */

public class Location {
    private String objectId;
    private String name;

    public Location() {

    }

    public Location(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
