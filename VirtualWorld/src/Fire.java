import java.util.List;
import processing.core.PImage;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.function.Function;

public class Fire extends AnimatedSchedule {

    public Fire(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<EntityObjects> target = world.findNearest(getPosition(), new MinerNotFullVisitor());  // check!
        List<PImage> freezeImages = imageStore.getImageList("freeze");

        scheduleActions(scheduler, world, imageStore);

        if (target.isPresent()) {
            moveToEntity(world, target.get(), scheduler, freezeImages);
        }
    }

    private boolean moveToEntity(WorldModel world, EntityObjects target, EventScheduler scheduler, List<PImage> freeze) {

        if (getPosition().adjacent(target.getPosition()))
        {
            target.setImages(freeze);
            target.setID("freeze");
            scheduler.unscheduleAllEvents(this);
            world.removeEntity(this);
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
