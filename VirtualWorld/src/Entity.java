public interface Entity{

    <R> R accept(EntityVisitor<R> visitor);

}