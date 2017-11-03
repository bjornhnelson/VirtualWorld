import java.util.List;
import processing.core.PImage;

public class Blacksmith extends EntityObjects {

    public Blacksmith(String id, Point position, List<PImage> images)
    {
        super(id, position, images);
    }

    public static Blacksmith createBlacksmith(String id, Point position, List<PImage> images)
    {
        return new Blacksmith(id, position, images);
    }

}
