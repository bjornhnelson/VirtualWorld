import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class OreBlob implements Animated {

    private static final String QUAKE_KEY = "quake";

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;

    public OreBlob(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
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
                actionPeriod);
        scheduler.scheduleEvent(this,
                createAnimationAction(0), getAnimationPeriod());
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest(position, Vein.class);  // check!
        long nextPeriod = actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToOreBlob(world, blobTarget.get(), scheduler))
            {
                Quake quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), nextPeriod);
    }

    private boolean moveToOreBlob(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (position.adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPositionOreBlob(world, target.getPosition());

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

    private Point nextPositionOreBlob(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - position.x);
        Point newPos = new Point(position.x + horiz,
                position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Ore)))
        {
            int vert = Integer.signum(destPos.y - position.y);
            newPos = new Point(position.x, position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Ore)))
            {
                newPos = position;
            }
        }

        return newPos;
    }

}
