import processing.core.PImage;

public interface Entity {

    Point getPosition();
    void setPosition(Point pos);
    int getAnimationPeriod();
    PImage getCurrentImage();

}
