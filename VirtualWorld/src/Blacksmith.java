import java.util.List;
import processing.core.PImage;

public class Blacksmith extends EntityObjects {

    private static final String SMITH_KEY = "blacksmith";
    private static final int SMITH_NUM_PROPERTIES = 4;
    private static final int SMITH_ID = 1;
    private static final int SMITH_COL = 2;
    private static final int SMITH_ROW = 3;

    public Blacksmith(String id, Point position, List<PImage> images)
    {
        super(id, position, images);
    }

    public static Blacksmith createBlacksmith(String id, Point position, List<PImage> images)
    {
        return new Blacksmith(id, position, images);
    }

    public static boolean parseSmith(String [] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == SMITH_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                    Integer.parseInt(properties[SMITH_ROW]));
            Blacksmith entity = createBlacksmith(properties[SMITH_ID],
                    pt, imageStore.getImageList(SMITH_KEY));
            entity.tryAddEntity(world);
        }

        return properties.length == SMITH_NUM_PROPERTIES;
    }

}
