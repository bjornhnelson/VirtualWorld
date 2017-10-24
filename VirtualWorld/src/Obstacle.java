import java.util.List;
import processing.core.PImage;

public class Obstacle implements Entity {

    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int animationPeriod;

    public Obstacle(String id, Point position, List<PImage> images, int animationPeriod)
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

    public static Obstacle createObstacle(String id, Point position, List<PImage> images)
    {
        return new Obstacle(id, position, images, 0);
    }

    public static boolean parseObstacle(String [] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES)
        {
            Point pt = new Point(
                    Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Obstacle entity = createObstacle(properties[OBSTACLE_ID],
                    pt, imageStore.getImageList(OBSTACLE_KEY));
            entity.tryAddEntity(world);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

}
