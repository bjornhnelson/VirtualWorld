import processing.core.PApplet;
import processing.core.PImage;
import java.util.Optional;

final class WorldView
{
   private PApplet screen;
   private WorldModel world;
   private int tileWidth;
   private int tileHeight;
   private Viewport viewport;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world, int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }

   public Viewport getViewport() {
      return viewport;
   }

   private void drawBackground()
   {
      for (int row = 0; row < viewport.getNumRows(); row++)
      {
         for (int col = 0; col < viewport.getNumCols(); col++)
         {
            Point worldPoint = viewport.viewportToWorld(col, row);
            Optional<PImage> image = world.getBackgroundImage(worldPoint);
            if (image.isPresent())
            {
               screen.image(image.get(), col * tileWidth,
                       row * tileHeight);
            }
         }
      }
   }

   private void drawEntities()
   {
      for (EntityObjects entity : world.getEntities())
      {
         Point pos = entity.getPosition();

         if (viewport.contains(pos))
         {
            Point viewPoint = viewport.worldToViewport(pos.x, pos.y);
            screen.image(entity.getCurrentImage(),
                    viewPoint.x * tileWidth, viewPoint.y * tileHeight);
         }
      }
   }

   public void drawViewport()
   {
      drawBackground();
      drawEntities();
   }

   public void shiftView(int colDelta, int rowDelta)
   {
      int newCol = Functions.clamp(viewport.getCol() + colDelta, 0,
              world.getNumCols() - viewport.getNumCols());
      int newRow = Functions.clamp(viewport.getRow() + rowDelta, 0,
              world.getNumRows() - viewport.getNumRows());

      viewport.shift(newCol, newRow);
   }

}
