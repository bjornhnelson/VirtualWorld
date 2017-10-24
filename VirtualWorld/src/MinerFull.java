import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class MinerFull implements Animated {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int actionPeriod;
    private int animationPeriod;

    public MinerFull(String id, Point position,
                      List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
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
                actionPeriod);
        scheduler.scheduleEvent(this, createAnimationAction(0),
                getAnimationPeriod());
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(position, Blacksmith.class);

        if (fullTarget.isPresent() &&
                moveToFull(world, fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    actionPeriod);
        }
    }

    private boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (position.adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = nextPositionMiner(world, target.getPosition());

            if (!position.equals(nextPos))
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
        MinerNotFull miner = MinerNotFull.createMinerNotFull(id, resourceLimit, position, actionPeriod, animationPeriod, images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    private Point nextPositionMiner(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - position.x);
        Point newPos = new Point(position.x + horiz,
                position.y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - position.y);
            newPos = new Point(position.x, position.y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = position;
            }
        }

        return newPos;
    }

}
