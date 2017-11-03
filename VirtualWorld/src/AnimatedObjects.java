import java.util.List;
import processing.core.PImage;

public abstract class AnimatedObjects extends DynamicObjects implements Animated {

    private int animationPeriod;

    public AnimatedObjects(String id, Point position,
                     List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;

    }

    protected int getAnimationPeriod() {
        return animationPeriod;
    }

    public void nextImage() {
        int index = (getImageIndex() + 1) % getImages().size();
        setImageIndex(index);
    }

    public AnimationAction createAnimationAction(int repeatCount) {
        return new AnimationAction(this, null, null, repeatCount);
    }

}
