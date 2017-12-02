import java.util.List;
import processing.core.PImage;

public class Fire extends AnimatedSchedule {

    private static final String FIRE_KEY = "fire";
    private static final int FIRE_ACTION_PERIOD = 1100;
    private static final int FIRE_ANIMATION_PERIOD = 100;

    public Fire(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        System.out.println("HI");
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

}
