import java.util.List;
import processing.core.PImage;

public abstract class AnimatedObjects extends EntityObjects implements Animated {

    private int actionPeriod;
    private int animationPeriod;

    public AnimatedObjects(String id, Point position,
                     List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;

    }
    public void nextImage() {
        int index = (super.getImageIndex() + 1) % super.getImages().size();
        super.setImageIndex(index);
    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public AnimationAction createAnimationAction(int repeatCount) {
        return new AnimationAction(this, null, null, repeatCount);
    }

}
