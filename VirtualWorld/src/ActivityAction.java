public class ActivityAction implements Action {

    private DynamicObjects entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public ActivityAction(DynamicObjects entity, WorldModel world,
                          ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.executeActivity(world, imageStore, scheduler);
    }

    public static ActivityAction createActivityAction(DynamicObjects entity, WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction(entity, world, imageStore, 0);
    }
}
