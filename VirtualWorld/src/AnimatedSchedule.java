import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public abstract class AnimatedSchedule extends AnimatedObjects {

    public AnimatedSchedule(String id, Point position,
                           List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent(this, createAnimationAction(0),
                getAnimationPeriod());
    }

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

    protected boolean moveToEntity(WorldModel world, EntityObjects target, EventScheduler scheduler) {

        if (getPosition().adjacent(target.getPosition()))
        {
            if (this instanceof MinerNotFull) {
                ((MinerNotFull)this).incrementResourceCount(1);
                world.removeEntity(target);
                scheduler.unscheduleAllEvents(target);
            }
            if (this instanceof OreBlob) {
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

}
