import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class MinerNotFull extends AnimatedSchedule {

    private int resourceLimit;
    private int resourceCount;

    public MinerNotFull(String id, Point position,
                          List<PImage> images, int resourceLimit, int resourceCount,
                          int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public static MinerNotFull createMinerNotFull(String id, int resourceLimit,
                                                  Point position, int actionPeriod, int animationPeriod,
                                                  List<PImage> images)
    {
        return new MinerNotFull(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public void incrementResourceCount(int value) {
        resourceCount += value;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<EntityObjects> notFullTarget = world.findNearest(getPosition(), Ore.class);

        if (!notFullTarget.isPresent() ||
                !moveToEntity(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    getActionPeriod());
        }
    }

    private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (resourceCount >= resourceLimit)
        {
            MinerFull miner = MinerFull.createMinerFull(getId(), resourceLimit, getPosition(), getActionPeriod(), getAnimationPeriod(), getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

}
