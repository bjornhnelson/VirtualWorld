import java.util.List;
import processing.core.PImage;

public interface Entity {

    String getId();
    List<PImage> getImages();
    int getImageIndex();
    void setImageIndex(int index);
    Point getPosition();
    void setPosition(Point pos);
    PImage getCurrentImage();

}
