import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class MinerFull extends AnimatedObjects implements Animated {

    private int resourceLimit;

    public MinerFull(String id, Point position,
                      List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
    }

      public static MinerFull createMinerFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new MinerFull(id, position, images, resourceLimit, actionPeriod, animationPeriod);
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
                super.getActionPeriod());
        scheduler.scheduleEvent(this, createAnimationAction(0),
                getAnimationPeriod());
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(super.getPosition(), Blacksmith.class);

        if (fullTarget.isPresent() &&
                moveToFull(world, fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }

    private boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (super.getPosition().adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = nextPositionMiner(world, target.getPosition());

            if (!super.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        MinerNotFull miner = MinerNotFull.createMinerNotFull(super.getId(), resourceLimit, super.getPosition(), super.getActionPeriod(), super.getAnimationPeriod(), super.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    private Point nextPositionMiner(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - super.getPosition().x);
        Point newPos = new Point(super.getPosition().x + horiz,
                super.getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - super.getPosition().y);
            newPos = new Point(super.getPosition().x, super.getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

}
