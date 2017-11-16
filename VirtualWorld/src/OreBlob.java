import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class OreBlob extends AnimatedSchedule {

    private static final String QUAKE_KEY = "quake";

    public OreBlob(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static OreBlob createOreBlob(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new OreBlob(id, position, images, actionPeriod, animationPeriod);
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
                Quake quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), nextPeriod);
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

}
