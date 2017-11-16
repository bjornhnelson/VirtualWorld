public class AnimationAction implements Action {

    private AnimatedObjects entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public AnimationAction(AnimatedObjects entity, WorldModel world,
                           ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(entity,
                    createAnimationAction(entity, Math.max(repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }

    public static AnimationAction createAnimationAction(AnimatedObjects entity, int repeatCount) {
        return new AnimationAction(entity, null, null, repeatCount);
    }

}
