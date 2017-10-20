public class ActivityAction implements ActionInterface {

    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public ActivityAction(Entity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        switch (entity.getKind())
        {
            case MINER_FULL:
                entity.executeMinerFullActivity(world, imageStore, scheduler);
                break;

            case MINER_NOT_FULL:
                entity.executeMinerNotFullActivity(world, imageStore, scheduler);
                break;

            case ORE:
                entity.executeOreActivity(world, imageStore, scheduler);
                break;

            case ORE_BLOB:
                entity.executeOreBlobActivity(world, imageStore, scheduler);
                break;

            case QUAKE:
                entity.executeQuakeActivity(world, imageStore, scheduler);
                break;

            case VEIN:
                entity.executeVeinActivity(world, imageStore, scheduler);
                break;

            default:
                throw new UnsupportedOperationException(
                        String.format("executeActivityAction not supported for %s",
                                entity.getKind()));
        }
    }


}
