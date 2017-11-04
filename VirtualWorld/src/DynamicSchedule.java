import java.util.List;
import processing.core.PImage;

public abstract class DynamicSchedule extends DynamicObjects{
    public DynamicSchedule(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

}
