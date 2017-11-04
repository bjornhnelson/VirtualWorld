import java.util.List;
import processing.core.PImage;

public abstract class DynamicObjects extends EntityObjects implements Dynamic {

    private int actionPeriod;

    public DynamicObjects(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    protected int getActionPeriod() {
        return actionPeriod;
    }

    protected ActivityAction createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction(this, world, imageStore, 0);
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}
