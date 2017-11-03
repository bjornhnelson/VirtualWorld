import java.util.List;
import processing.core.PImage;

public class Obstacle extends EntityObjects {

    public Obstacle(String id, Point position, List<PImage> images)
    {
        super(id, position, images);
    }

    public static Obstacle createObstacle(String id, Point position, List<PImage> images)
    {
        return new Obstacle(id, position, images);
    }

}
