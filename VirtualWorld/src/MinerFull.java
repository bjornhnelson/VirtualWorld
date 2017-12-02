import java.util.List;
import processing.core.PImage;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.function.Function;

public class MinerFull extends AnimatedSchedule {

    private int resourceLimit;

    public MinerFull(String id, Point position,
                      List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<EntityObjects> fullTarget = world.findNearest(getPosition(), new BlacksmithVisitor());

        if (fullTarget.isPresent() &&
                moveToEntity(world, fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    ActivityAction.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }

    private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        MinerNotFull miner = new MinerNotFull(getId(), getPosition(), getImages(), resourceLimit, 0, getActionPeriod(), getAnimationPeriod());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

    private boolean moveToEntity(WorldModel world, EntityObjects target, EventScheduler scheduler) {

        if (getPosition().adjacent(target.getPosition()))
        {
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
