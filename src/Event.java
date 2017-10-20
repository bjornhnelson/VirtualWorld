final class Event
{
   private ActionInterface action;
   private long time;
   private Entity entity;

   public Event(ActionInterface action, long time, Entity entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }

   public ActionInterface getAction() {
      return action;
   }

   public long getTime() {
      return time;
   }

   public Entity getEntity() {
      return entity;
   }
}
