import java.util.List;
import processing.core.PImage;

public class Quake implements Animated {

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;

    public Quake(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
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

    public void nextImage()
    {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public PImage getCurrentImage()
    {
        return images.get(imageIndex);
    }

    public static Quake createQuake(Point position, List<PImage> images)
    {
        return new Quake(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public ActivityAction createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction(this, world, imageStore, 0);
    }

    public AnimationAction createAnimationAction(int repeatCount)
    {
        return new AnimationAction(this, null, null, repeatCount);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                actionPeriod);
        scheduler.scheduleEvent(this,
                createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT),
                getAnimationPeriod());

    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

}
