import java.util.List;
import processing.core.PImage;

public abstract class QuakeSchedule extends AnimatedObjects {

    public QuakeSchedule(String id, Point position,
                            List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

}
