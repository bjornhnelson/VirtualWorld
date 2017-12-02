import java.util.List;
import processing.core.PImage;

public class Blacksmith extends EntityObjects {

    public Blacksmith(String id, Point position, List<PImage> images)
    {
        super(id, position, images);
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

}
