import java.util.List;
import processing.core.PImage;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.function.Function;

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


    protected Point nextPosition(WorldModel world, Point destPos) {
        SingleStepPathingStrategy pathStrategy = new SingleStepPathingStrategy();

        Predicate<Point> canPassThrough = p -> !world.isOccupied(p);
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


    /*
    protected Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - getPosition().x);
        Point newPos = new Point(getPosition().x + horiz,
                getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - getPosition().y);
            newPos = new Point(getPosition().x, getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = getPosition();
            }
        }

        return newPos;
    }
    */

}
