public class AnimationAction implements Action {

    private Animated entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public AnimationAction(Animated entity, WorldModel world,
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
                    entity.createAnimationAction(Math.max(repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }

}
