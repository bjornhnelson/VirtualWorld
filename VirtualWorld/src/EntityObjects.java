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

    public void tryAddEntity(WorldModel world)
    {
        if (world.isOccupied(getPosition()))
        {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        world.addEntity(this);
    }

}
