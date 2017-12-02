import java.util.List;
import processing.core.PImage;

public class Obstacle extends EntityObjects {

    public Obstacle(String id, Point position, List<PImage> images)
    {
        super(id, position, images);
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

}
