import java.util.List;
import processing.core.PImage;

public abstract class QuakeSchedule extends AnimatedObjects {

    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public QuakeSchedule(String id, Point position,
                            List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent(this,
                createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT),
                getAnimationPeriod());
    }

}
