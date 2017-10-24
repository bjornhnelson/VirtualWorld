public interface Animated extends Dynamic {

    AnimationAction createAnimationAction(int repeatCount);
    void nextImage();

}
