import java.util.List;
import processing.core.PImage;

public abstract class DynamicSchedule extends DynamicObjects{

    public DynamicSchedule(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                ActivityAction.createActivityAction(this, world, imageStore),
                getActionPeriod());
    }

}
