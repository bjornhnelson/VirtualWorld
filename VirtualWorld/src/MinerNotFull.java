import java.util.List;
import processing.core.PImage;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.function.Function;

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

    private void incrementResourceCount(int value) {
        resourceCount += value;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<EntityObjects> target;
        Optional<EntityObjects> notFullTarget = world.findNearest(getPosition(), new OreVisitor());
        Optional<EntityObjects> obstacleTarget = world.findNearest(getPosition(), new ObstacleVisitor());
        List<PImage> minerImages = imageStore.getImageList("miner");

        if (getId() == "freeze") {
            target = obstacleTarget;
        }
        else {
            target = notFullTarget;
        }
        if (!target.isPresent() ||
           !moveToEntity(world, target.get(), scheduler, minerImages) ||
           !transformNotFull(world, scheduler, imageStore)) {
                //scheduleActions(scheduler, world, imageStore);
                scheduler.scheduleEvent(this,
                        ActivityAction.createActivityAction(this, world, imageStore),
                        getActionPeriod());
            }
    }

    private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (resourceCount >= resourceLimit)
        {
            MinerFull miner = new MinerFull(getId(), getPosition(), getImages(), resourceLimit, getActionPeriod(), getAnimationPeriod());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

    private boolean moveToEntity(WorldModel world, EntityObjects target, EventScheduler scheduler, List<PImage> minerImages) {

        ObstacleVisitor obstacleVisitor = new ObstacleVisitor();

        if (getPosition().adjacent(target.getPosition()))
        {
            if (target.accept(obstacleVisitor)) {
                setImages(minerImages);
                setID("miner");
            }
            else {
                incrementResourceCount(1);
                world.removeEntity(target);
                scheduler.unscheduleAllEvents(target);
            }
            return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos))
            {
                Optional<EntityObjects> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }

    }

    private Point nextPosition(WorldModel world, Point destPos) {
        //SingleStepPathingStrategy pathStrategy = new SingleStepPathingStrategy();
        AStarPathingStrategy pathStrategy = new AStarPathingStrategy();

        Predicate<Point> canPassThrough = p -> !world.isOccupied(p) && world.withinBounds(p);
        BiPredicate<Point, Point> withinReach = (p1, p2) -> p1.adjacent(p2);
        Function<Point, Stream<Point>> potentialNeighbors = PathingStrategy.CARDINAL_NEIGHBORS;

        List<Point> pathStrategyList = pathStrategy.computePath(getPosition(), destPos, canPassThrough, withinReach, potentialNeighbors);

        if (pathStrategyList.size() > 0) {
            return pathStrategyList.get(0);
        }
        else {
            return getPosition();
        }
    }

}
