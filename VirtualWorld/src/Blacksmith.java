import java.util.List;
import processing.core.PImage;

public class Blacksmith implements Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int animationPeriod;

    public Blacksmith(String id, Point position, List<PImage> images, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.animationPeriod = animationPeriod;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point pos) {
        position = pos;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public PImage getCurrentImage()
    {
        return images.get(imageIndex);
    }

    public void tryAddEntity(WorldModel world)
    {
        if (world.isOccupied(position))
        {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        world.addEntity(this);
    }

    public static Blacksmith createBlacksmith(String id, Point position, List<PImage> images)
    {
        return new Blacksmith(id, position, images, 0);
    }

}
