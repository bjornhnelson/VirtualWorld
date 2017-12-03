import java.util.List;
import processing.core.PImage;

abstract class EntityObjects implements Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public EntityObjects(String id, Point position, List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    protected String getId() {
        return id;
    }

    protected void setID(String newID) {
        id = newID;
    }

    protected List<PImage> getImages() {
        return images;
    }

    protected int getImageIndex() {
        return imageIndex;
    }

    protected void setImageIndex(int index) {
        imageIndex = index;
    }

    protected Point getPosition() {
        return position;
    }

    protected void setPosition(Point pos) {
        position = pos;
    }

    protected PImage getCurrentImage()
    {
        return images.get(imageIndex);
    }

    protected void setImages(List<PImage> newImages) {
        images = newImages;
    }

}
