import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class OreBlob extends AnimatedObjects implements Animated {

    private static final String QUAKE_KEY = "quake";


    public OreBlob(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static OreBlob createOreBlob(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new OreBlob(id, position, images, actionPeriod, animationPeriod);
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
        scheduler.scheduleEvent(this,
                createAnimationAction(0), getAnimationPeriod());
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest(super.getPosition(), Vein.class);  // check!
        long nextPeriod = super.getActionPeriod();

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToOreBlob(world, blobTarget.get(), scheduler))
            {
                Quake quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += super.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), nextPeriod);
    }

    private boolean moveToOreBlob(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (super.getPosition().adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPositionOreBlob(world, target.getPosition());

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

    private Point nextPositionOreBlob(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - super.getPosition().x);
        Point newPos = new Point(super.getPosition().x + horiz,
                super.getPosition().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Ore)))
        {
            int vert = Integer.signum(destPos.y - super.getPosition().y);
            newPos = new Point(super.getPosition().x, super.getPosition().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Ore)))
            {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

}
