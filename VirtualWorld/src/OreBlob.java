import java.util.List;

import processing.core.PImage;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.function.Function;

public class OreBlob extends AnimatedSchedule {

    private static final String QUAKE_KEY = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public OreBlob(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<EntityObjects> blobTarget = world.findNearest(getPosition(), new VeinVisitor());  // check!
        long nextPeriod = getActionPeriod();

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToEntity(world, blobTarget.get(), scheduler))
            {
                Quake quake = new Quake(QUAKE_KEY, tgtPos,
                        imageStore.getImageList(QUAKE_KEY), QUAKE_ACTION_PERIOD,
                        QUAKE_ANIMATION_PERIOD);

                world.addEntity(quake);
                nextPeriod += getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, ActivityAction.createActivityAction(this, world, imageStore), nextPeriod);
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

    private boolean moveToEntity(WorldModel world, EntityObjects target, EventScheduler scheduler) {

        if (getPosition().adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
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

        Predicate<Point> canPassThrough = p -> world.withinBounds(p) && (world.getOccupant(p).equals(Optional.empty()) || world.getOccupant(p).equals(new OreVisitor()));
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
