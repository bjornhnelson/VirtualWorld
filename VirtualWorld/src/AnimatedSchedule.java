import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public abstract class AnimatedSchedule extends AnimatedObjects {

    public AnimatedSchedule(String id, Point position,
                           List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                ActivityAction.createActivityAction(this, world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent(this, AnimationAction.createAnimationAction(this, 0),
                getAnimationPeriod());
    }

}
