package silkway.merey.silkwayapp.classes;

/**
 * Created by Merey on 17.03.17.
 */

public class Category {
    private String objectId;
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
