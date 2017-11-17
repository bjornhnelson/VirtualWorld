import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import processing.core.PImage;
import java.util.Optional;
import java.util.LinkedList;
import java.util.List;

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private EntityObjects occupancy[][];
   private Set<EntityObjects> entities;

   private static final int ORE_REACH = 1;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new EntityObjects[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public int getNumRows() {
      return numRows;
   }

   public int getNumCols() {
      return numCols;
   }

   public Set<EntityObjects> getEntities() {
      return entities;
   }

   public boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < numRows &&
              pos.x >= 0 && pos.x < numCols;
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }

   /*
      Assumes that there is no entity currently occupying the
      intended destination cell.
   */
   public void addEntity(EntityObjects entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   public void moveEntity(EntityObjects entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void removeEntity(EntityObjects entity)
   {
      removeEntityAt(entity.getPosition());
   }

   private void removeEntityAt(Point pos)
   {
      if (withinBounds(pos)
              && getOccupancyCell(pos) != null)
      {
         EntityObjects entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         entities.remove(entity);
         setOccupancyCell(pos, null);
      }
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(getBackgroundCell(pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   public Optional<EntityObjects> getOccupant(Point pos)
   {
      if (isOccupied(pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public EntityObjects getOccupancyCell(Point pos)
   {
      return occupancy[pos.y][pos.x];
   }

   private void setOccupancyCell(Point pos, EntityObjects entity)
   {
      occupancy[pos.y][pos.x] = entity;
   }

   public void setBackground(Point pos, Background background)
   {
      if (withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }

   private Background getBackgroundCell(Point pos)
   {
      return background[pos.y][pos.x];
   }

   private void setBackgroundCell(Point pos, Background background)
   {
      this.background[pos.y][pos.x] = background;
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
      {
         for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (withinBounds(newPt) &&
                    !isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public Optional<EntityObjects> findNearest(Point pos, EntityVisitor<Boolean> visitor)
   {
      List<EntityObjects> ofType = new LinkedList<>();
      for (EntityObjects entity : entities)
      {
         if (entity.accept(visitor))
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }

   private Optional<EntityObjects> nearestEntity(List<EntityObjects> entities, Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         EntityObjects nearest = entities.get(0);
         int nearestDistance = pos.distanceSquared(nearest.getPosition());

         for (EntityObjects other : entities)
         {
            int otherDistance = pos.distanceSquared(other.getPosition());

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

}
