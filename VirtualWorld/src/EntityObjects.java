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

    public String getId() {
        return id;
    }

    public List<PImage> getImages() {
        return images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int index) {
        imageIndex = index;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point pos) {
        position = pos;
    }

    public PImage getCurrentImage()
    {
        return images.get(imageIndex);
    }

}
