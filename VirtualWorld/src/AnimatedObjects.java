import java.util.List;
import processing.core.PImage;

public abstract class AnimatedObjects extends EntityObjects implements Animated {

    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public AnimatedObjects(String id, Point position,
                     List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod)
    {
        super(id, position, images);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;

    }
    public void nextImage() {
        int index = (super.getImageIndex() + 1) % super.getImages().size();
        super.setImageIndex(index);
    }

    public AnimationAction createAnimationAction(int repeatCount) {
        return new AnimationAction(this, null, null, repeatCount);
    }

}
