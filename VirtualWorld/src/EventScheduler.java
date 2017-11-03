import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

final class EventScheduler
{
   private PriorityQueue<Event> eventQueue;
   private Map<EntityObjects, List<Event>> pendingEvents;
   private double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public void unscheduleAllEvents(EntityObjects entity)
   {
      List<Event> pending = pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            eventQueue.remove(event);
         }
      }
   }

   public void scheduleEvent(EntityObjects entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() + (long)(afterPeriod * timeScale);
      Event event = new Event(action, time, entity);

      eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = pendingEvents.getOrDefault(entity, new LinkedList<>());
      pending.add(event);
      pendingEvents.put(entity, pending);
   }

   private void removePendingEvent(Event event)
   {
      List<Event> pending = pendingEvents.get(event.getEntity());

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public void updateOnTime(long time)
   {
      while (!eventQueue.isEmpty() && eventQueue.peek().getTime() < time)
      {
         Event next = eventQueue.poll();

         removePendingEvent(next);

         next.getAction().executeAction(this);
      }
   }

}
